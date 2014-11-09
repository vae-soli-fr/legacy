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
 * @author Iqman
 */
public class Q00753_ReactingToACrisis extends Quest implements ScriptFile
{
	// q items
	private static final int KEY = 36054;
	// reward items
	private static final int SCROLL = 36082;
	private static final int BERNA = 33796;
	private static final String GOLEM_KILL = "Golem_kill";
	
	public Q00753_ReactingToACrisis()
	{
		super(false);
		addTalkId(BERNA);
		addQuestItem(KEY);
		addKillId(23270, 23271, 23272, 23272, 23274, 23275, 23276);
		addKillNpcWithLog(1, GOLEM_KILL, 5, 19296);
		addLevelCheck(93, 100);
		addQuestCompletedCheck(Q10386_MysteriousJourney.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equalsIgnoreCase("accepted.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		
		if (event.equalsIgnoreCase("endquest.htm"))
		{
			st.getPlayer().unsetVar("q753doneKill");
			st.takeAllItems(KEY);
			st.getPlayer().addExpAndSp(408665250, 40866525);
			st.giveItems(SCROLL, 1);
			st.exitCurrentQuest(this);
			st.playSound(SOUND_FINISH);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getId();
		String htmltext = "noquest";
		QuestState Mj = st.getPlayer().getQuestState(Q10386_MysteriousJourney.class);
		
		if ((Mj == null) || !Mj.isCompleted())
		{
			return "you cannot procceed with this quest until you have completed the Mystrerious Journey quest";
		}
		
		if (st.isNowAvailable())
		{
			if (npcId == BERNA)
			{
				if (cond == 0)
				{
					htmltext = "start.htm";
				}
				else if (cond == 1)
				{
					htmltext = "notcollected.htm";
				}
				else if (cond == 2)
				{
					htmltext = "collected.htm";
				}
			}
		}
		else
		{
			htmltext = "You have completed this quest today, come back tomorow at 6:30!";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs == null)
		{
			return null;
		}
		
		if (qs.getState() != STARTED)
		{
			return null;
		}
		
		if (qs.getCond() != 1)
		{
			return null;
		}
		
		if ((qs.getQuestItemsCount(KEY) < 30) && Rnd.chance(10))
		{
			qs.giveItems(KEY, 1);
		}
		
		if ((qs.getQuestItemsCount(KEY) >= 30) && qs.getPlayer().getVarB("q753doneKill"))
		{
			qs.setCond(2);
		}
		
		if ((npc.getId() == 19296) && !qs.getPlayer().getVarB("q753doneKill"))
		{
			boolean doneKill = updateKill(npc, qs);
			
			if (doneKill)
			{
				qs.unset(GOLEM_KILL);
				
				if (qs.getQuestItemsCount(KEY) >= 30)
				{
					qs.setCond(2);
				}
				else
				{
					qs.getPlayer().setVar("q753doneKill", "1", -1);
				}
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