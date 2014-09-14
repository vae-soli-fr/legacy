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
package quests;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * Done By Darvin (c)Java-Team
 */
public class Q00152_ShardsOfGolem extends Quest implements ScriptFile
{
	private static final int HARRYS_RECEIPT1 = 1008;
	private static final int HARRYS_RECEIPT2 = 1009;
	private static final int GOLEM_SHARD = 1010;
	private static final int TOOL_BOX = 1011;
	private static final int WOODEN_BP = 23;
	
	public Q00152_ShardsOfGolem()
	{
		super(false);
		
		addStartNpc(30035);
		
		addTalkId(30035);
		addTalkId(30035);
		addTalkId(30283);
		addTalkId(30035);
		
		addKillId(20016);
		addKillId(20101);
		
		addQuestItem(HARRYS_RECEIPT1, GOLEM_SHARD, TOOL_BOX, HARRYS_RECEIPT2);
	}
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equals("30035-04.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			if (st.getQuestItemsCount(HARRYS_RECEIPT1) == 0)
			{
				st.giveItems(HARRYS_RECEIPT1, 1);
			}
		}
		else if (event.equals("152_2"))
		{
			st.takeItems(HARRYS_RECEIPT1, -1);
			if (st.getQuestItemsCount(HARRYS_RECEIPT2) == 0)
			{
				st.giveItems(HARRYS_RECEIPT2, 1);
				st.setCond(2);
			}
			htmltext = "30283-02.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int cond = st.getCond();
		if (npcId == 30035)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 10)
				{
					htmltext = "30035-03.htm";
					return htmltext;
				}
				htmltext = "30035-02.htm";
				st.exitCurrentQuest(true);
			}
			else if ((cond == 1) && (st.getQuestItemsCount(HARRYS_RECEIPT1) != 0))
			{
				htmltext = "30035-05.htm";
			}
			else if ((cond == 2) && (st.getQuestItemsCount(HARRYS_RECEIPT2) != 0))
			{
				htmltext = "30035-05.htm";
			}
			else if ((cond == 4) && (st.getQuestItemsCount(TOOL_BOX) != 0))
			{
				st.takeItems(TOOL_BOX, -1);
				st.takeItems(HARRYS_RECEIPT2, -1);
				st.setCond(0);
				st.playSound(SOUND_FINISH);
				st.giveItems(WOODEN_BP, 1);
				st.addExpAndSp(5000, 0);
				htmltext = "30035-06.htm";
				st.exitCurrentQuest(false);
			}
		}
		else if (npcId == 30283)
		{
			if ((cond == 1) && (st.getQuestItemsCount(HARRYS_RECEIPT1) != 0))
			{
				htmltext = "30283-01.htm";
			}
			else if ((cond == 2) && (st.getQuestItemsCount(HARRYS_RECEIPT2) != 0) && (st.getQuestItemsCount(GOLEM_SHARD) < 5))
			{
				htmltext = "30283-03.htm";
			}
			else if ((cond == 3) && (st.getQuestItemsCount(HARRYS_RECEIPT2) != 0) && (st.getQuestItemsCount(GOLEM_SHARD) == 5))
			{
				st.takeItems(GOLEM_SHARD, -1);
				if (st.getQuestItemsCount(TOOL_BOX) == 0)
				{
					st.giveItems(TOOL_BOX, 1);
					st.setCond(4);
				}
				htmltext = "30283-04.htm";
			}
		}
		else if ((cond == 4) && (st.getQuestItemsCount(HARRYS_RECEIPT2) != 0) && (st.getQuestItemsCount(TOOL_BOX) != 0))
		{
			htmltext = "30283-05.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((st.getCond() == 2) && Rnd.chance(30) && (st.getQuestItemsCount(GOLEM_SHARD) < 5))
		{
			st.giveItems(GOLEM_SHARD, 1);
			if (st.getQuestItemsCount(GOLEM_SHARD) == 5)
			{
				st.setCond(3);
				st.playSound(SOUND_MIDDLE);
			}
			else
			{
				st.playSound(SOUND_ITEMGET);
			}
		}
		return null;
	}
}