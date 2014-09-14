/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.serverpackets;

import java.util.Collection;
import java.util.Collections;

import lineage2.gameserver.model.quest.dynamic.DynamicQuestParticipant;
import lineage2.gameserver.model.quest.dynamic.DynamicQuestTask;

/**
 * @author KilRoyS 0xfe:0xe8 :: ExDynamicQuestPacket
 */
public class ExDynamicQuestPacket extends L2GameServerPacket
{
	/*
	 * private final int type; private final int subType; private final int questId; private final int step; private final int remainingTime; private final Map<Integer, DynamicQuest.DynamicQuestTask> tasks;
	 */
	private final DynamicQuestInfo questInfo;
	
	/*
	 * public ExDynamicQuestPacket(int type, int subType, int questId, int step) { this(type, subType, questId, step, 0); } public ExDynamicQuestPacket(int type, int subType, int questId, int step, int remainingTime) { this(type, subType, questId, step, remainingTime, (Map<Integer,
	 * DynamicQuest.DynamicQuestTask>) Collections.EMPTY_MAP); } public ExDynamicQuestPacket(int type, int subType, int questId, int step, int remainingTime, Map<Integer, DynamicQuest.DynamicQuestTask> tasks) { this.type = type; // 0- Campaign, 1 - Zone Quest, this.subType = subType; // 0 - start, 1
	 * - end, 2 - progress, 3 - statistic this.questId = questId; this.step = step; this.remainingTime = remainingTime; this.tasks = tasks; }
	 */
	public ExDynamicQuestPacket(DynamicQuestInfo questInfo)
	{
		this.questInfo = questInfo;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xE9);
		writeC(questInfo.questType); // isCampaign
		writeC(questInfo.subType); // subType
		writeD(questInfo.questId); // campaignId
		writeD(questInfo.step); // step
		questInfo.write(this);
		/*
		 * switch (subType) { case 2: writeC(0x00);// 0 - progress, 1 - receive reward, 2 - check results, 3 - campaign failed writeD(remainingTime); // remaining time (in second) if (type == DynamicQuestManager.TYPE_ZONE_QUEST) { writeD(0x00); // participant count? } writeD(tasks.size()); // tasks
		 * size for (int taskId : tasks.keySet()) { DynamicQuest.DynamicQuestTask task = tasks.get(taskId); writeD(taskId);// taskId writeD(task.getCurrentPoints());// currentCount writeD(task.getMaxPoints());// totalCount } break; case 3: if (type == DynamicQuestManager.TYPE_ZONE_QUEST) {
		 * writeD(remainingTime); // remaining time writeD(0x00); // party members count writeD(0x00); // tasks size? for (int i = 0; i < 0; i++) { writeS(""); // name writeD(0x00); // taskId writeD(0x00); // additional writeD(0x00); // total } } else { writeD(0x00); // participants size? for (int i
		 * = 0; i < 0; i++) { writeS(""); // name } } break; }
		 */
	}
	
	public static class DynamicQuestInfo
	{
		// Base class, subType = 0 - start, subType = 1 - end quest
		public int questType;
		public int questId;
		public int step;
		final int subType;
		
		public DynamicQuestInfo(int subType)
		{
			this.subType = subType;
		}
		
		public void write(ExDynamicQuestPacket packet)
		{
			// Overriden
		}
	}
	
	public static class StartedQuest extends DynamicQuestInfo
	{
		private final int state;
		private final int remainingTime;
		private final int participantsCount;
		private Collection<DynamicQuestTask> tasks = Collections.emptyList();
		
		public StartedQuest(int state, int remainingTime, int participantsCount, Collection<DynamicQuestTask> tasks)
		{
			super(2);
			this.state = state;
			this.remainingTime = remainingTime;
			this.participantsCount = participantsCount;
			this.tasks = tasks;
		}
		
		@Override
		public void write(ExDynamicQuestPacket packet)
		{
			packet.writeC(state);
			packet.writeD(remainingTime);
			
			if (questType == 1)
			{
				packet.writeD(participantsCount);
			}
			
			packet.writeD(tasks.size());
			
			for (DynamicQuestTask task : tasks)
			{
				packet.writeD(task.taskId);
				packet.writeD(task.getCurrentPoints());
				packet.writeD(task.getMaxPoints());
			}
		}
	}
	
	public static class ScoreBoardInfo extends DynamicQuestInfo
	{
		private final int remainingTime;
		private final int friendsCount;
		private final Collection<DynamicQuestParticipant> participants;
		
		public ScoreBoardInfo(int remainingTime, int friendsCount, Collection<DynamicQuestParticipant> participants)
		{
			super(3);
			this.remainingTime = remainingTime;
			this.friendsCount = friendsCount;
			this.participants = participants;
		}
		
		@Override
		public void write(ExDynamicQuestPacket packet)
		{
			if (questType == 1)
			{
				packet.writeD(remainingTime); // remaining time
				packet.writeD(friendsCount); // party members count
				packet.writeD(participants.size()); // participants size
				
				for (DynamicQuestParticipant participant : participants)
				{
					packet.writeS(participant.getName());
					packet.writeD(participant.getCurrentPoints());// current points
					packet.writeD(participant.getAdditionalPoints()); // additional
					packet.writeD(participant.getCurrentPoints() + participant.getAdditionalPoints()); // total
				}
			}
			else
			{
				packet.writeD(participants.size()); // participants size
				
				for (DynamicQuestParticipant participant : participants)
				{
					packet.writeS(participant.getName()); // name
				}
			}
		}
	}
}