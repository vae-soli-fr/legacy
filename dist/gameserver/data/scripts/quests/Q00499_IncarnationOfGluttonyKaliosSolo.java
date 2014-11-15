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

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author cruel
 * @category Daily quest. Solo
 * @version http://l2on.net/?c=quests&id=499
 */
public class Q00499_IncarnationOfGluttonyKaliosSolo extends Quest implements ScriptFile
{
	private static final int KARTIA_RESEARCHER = 33647;
	private static final int KARTIA_RB = 19255;
	private static final int KARTIA_BOX = 34932;
	
	public Q00499_IncarnationOfGluttonyKaliosSolo()
	{
		super(true);
		addStartNpc(KARTIA_RESEARCHER);
		addTalkId(KARTIA_RESEARCHER);
		addKillId(KARTIA_RB);
		addLevelCheck(95, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "33647-07.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "33647-10.htm":
				qs.giveItems(KARTIA_BOX, 1);
				qs.setState(COMPLETED);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (qs.getCond())
		{
			case 0:
				if (qs.getPlayer().getLevel() >= 95)
				{
					if (qs.isNowAvailableByTime())
					{
						htmltext = "33647-01.htm";
					}
					else
					{
						htmltext = "33647-03.htm";
					}
				}
				else
				{
					htmltext = "33647-02.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				htmltext = "33647-08.htm";
				break;
			
			case 2:
				htmltext = "33647-09.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Party party = qs.getPlayer().getParty();
		
		if (party != null)
		{
			for (Player member : party.getPartyMembers())
			{
				final QuestState state = member.getQuestState(getClass());
				
				if ((state != null) && state.isStarted())
				{
					if ((state.getCond() == 1) && (npc.getId() == KARTIA_RB))
					{
						state.setCond(2);
					}
				}
			}
		}
		else
		{
			if ((qs.getCond() == 1) && (npc.getId() == KARTIA_RB))
			{
				qs.setCond(2);
			}
		}
		
		return null;
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
}