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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import services.SupportMagic;

public class Q00482_RecertificationOfValue extends Quest implements ScriptFile
{
	// npc
	public static final int LILEJ = 33155;
	public static final int KUORI = 33358;
	public static final String A_LIST = "a_list";
	
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
	
	public Q00482_RecertificationOfValue()
	{
		super(true);
		addStartNpc(LILEJ);
		addTalkId(KUORI);
		addLevelCheck(48, 100);
		addKillNpcWithLog(2, A_LIST, 10, 23044, 23045, 23046, 23047, 23048, 23049, 23050, 23051, 23052, 23053, 23054, 23055, 23056, 23057, 23058, 23059, 23060, 23061, 23062, 23063, 23064, 23065, 23066, 23067, 23068, 23102, 23103, 23104, 23105, 23106, 23107, 23108, 23109, 23110, 23111, 23112);
		addQuestCompletedCheck(Q10353_CertificationOfValue.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();
		
		if (event.equalsIgnoreCase("SupportPlayer"))
		{
			SupportMagic.getSupportMagic(npc, player);
			return "33155-6.htm";
		}
		else if (event.equalsIgnoreCase("SupportPet"))
		{
			SupportMagic.getSupportServitorMagic(npc, player);
			return "33155-6.htm";
		}
		else if (event.equalsIgnoreCase("Goto"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			player.teleToLocation(119656, 16072, -5120);
			return null;
		}
		else if (event.equalsIgnoreCase("33358-3.htm"))
		{
			st.setCond(2);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		int npcId = npc.getId();
		int state = st.getState();
		int cond = st.getCond();
		
		if (state == 1)
		{
			if (player.getLevel() < 48)
			{
				return "33155-lvl.htm";
			}
			
			if (!st.isNowAvailable())
			{
				return "33155-comp.htm";
			}
		}
		
		if (npcId == LILEJ)
		{
			if (cond == 0)
			{
				return "33155.htm";
			}
			
			if (cond == 1)
			{
				return "33155-11.htm";
			}
		}
		
		if (npcId == KUORI)
		{
			if (cond == 1)
			{
				return "33358.htm";
			}
			
			if (cond == 2)
			{
				return "33358-5.htm";
			}
			
			if (cond == 3)
			{
				st.addExpAndSp(1500000, 1250000);
				st.giveItems(17624, 1);
				st.unset("cond");
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(this);
				return "33358-6.htm";
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
			st.setCond(3);
		}
		
		return null;
	}
}