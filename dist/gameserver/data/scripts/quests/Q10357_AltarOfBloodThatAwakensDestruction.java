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

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10357_AltarOfBloodThatAwakensDestruction extends Quest implements ScriptFile
{
	// npc
	private static final int JORJINO = 33515;
	private static final int ELKARDIA = 32798;
	private static final String A_LIST = "a_list";
	private static final String B_LIST = "b_list";
	
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
	
	public Q10357_AltarOfBloodThatAwakensDestruction()
	{
		super(true);
		addStartNpc(JORJINO);
		addTalkId(JORJINO);
		addTalkId(ELKARDIA);
		addKillNpcWithLog(2, A_LIST, 1, 25876); // recheck this npc (more than one!)
		addKillNpcWithLog(2, B_LIST, 1, 25877);
		addLevelCheck(95, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("33515-4.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		
		if (event.equalsIgnoreCase("32798-1.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		
		if (event.startsWith("give"))
		{
			if (event.equalsIgnoreCase("givematerials"))
			{
				st.giveItems(19305, 1);
				st.giveItems(19306, 1);
				st.giveItems(19307, 1);
				st.giveItems(19308, 1);
			}
			else if (event.equalsIgnoreCase("giveenchants"))
			{
				st.giveItems(22561, 2);
			}
			else if (event.equalsIgnoreCase("givesacks"))
			{
				st.giveItems(34861, 2);
			}
			
			st.addExpAndSp(11000000, 5000000);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
			return "33515-7.htm";
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		
		if (state == COMPLETED)
		{
			return "33515-comp.htm";
		}
		
		if (st.getPlayer().getLevel() < 95)
		{
			return "33515-lvl.htm";
		}
		
		if (npcId == JORJINO)
		{
			if (cond == 0)
			{
				return "33515.htm";
			}
			else if (cond == 1)
			{
				return "33515-5.htm";
			}
			else if (cond == 2)
			{
				return "33515-5.htm";
			}
			else if (cond == 3)
			{
				return "33515-6.htm";
			}
		}
		else if (npcId == ELKARDIA)
		{
			if (cond == 1)
			{
				return "32798.htm";
			}
			else if (cond == 2)
			{
				return "32798-2.htm";
			}
			else if (cond == 3)
			{
				return "32798-5.htm";
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		
		if (cond != 2)
		{
			return null;
		}
		
		boolean doneKill = updateKill(npc, st);
		
		if (doneKill)
		{
			st.unset(A_LIST);
			st.unset(B_LIST);
			st.setCond(3);
		}
		
		return null;
	}
}