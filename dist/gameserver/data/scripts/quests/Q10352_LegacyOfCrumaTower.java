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

import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import services.SupportMagic;

public class Q10352_LegacyOfCrumaTower extends Quest implements ScriptFile
{
	// npc
	private static final int LILEJ = 33155;
	private static final int LINKENS = 33163;
	private static final int MARTES_NPC = 33292; // martes NPC
	private static final int MARTES_RB = 25829; // martes RB
	// items
	private static final int TRESURE_TOOL = 17619;
	private static final int MARTES_CORE = 17728;
	
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
	
	public Q10352_LegacyOfCrumaTower()
	{
		super(true);
		addStartNpc(LILEJ);
		addTalkId(LINKENS);
		addTalkId(MARTES_NPC);
		addKillId(MARTES_RB);
		addQuestItem(TRESURE_TOOL);
		addQuestItem(MARTES_CORE);
		addLevelCheck(38, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();
		
		if (event.equalsIgnoreCase("33155-9.htm"))
		{
			SupportMagic.getSupportMagic(npc, player);
		}
		
		if (event.equalsIgnoreCase("33155-10.htm"))
		{
			SupportMagic.getSupportServitorMagic(npc, player);
		}
		
		if (event.equalsIgnoreCase("advanceCond3"))
		{
			if (st.getCond() != 3)
			{
				st.setCond(3);
			}
			
			return null;
		}
		
		if (event.equalsIgnoreCase("teleportCruma"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			player.teleToLocation(17192, 114173, -3439);
			return null;
		}
		
		if (event.equalsIgnoreCase("33163-8.htm"))
		{
			if (st.getQuestItemsCount(TRESURE_TOOL) == 0)
			{
				st.giveItems(TRESURE_TOOL, 30);
				st.setCond(2);
			}
			else
			{
				return "33163-12.htm";
			}
		}
		
		if (event.equalsIgnoreCase("EnterInstance"))
		{
			if (player.getParty() == null)
			{
				player.sendMessage("You cannot enter without party!"); // pts message?
				return null;
			}
			
			for (Player member : player.getParty().getPartyMembers())
			{
				QuestState qs = member.getQuestState(Q10352_LegacyOfCrumaTower.class);
				
				if ((qs == null) || (qs.getCond() != 3))
				{
				} // nothing as I've seen everybody can enter this instance
				else if (qs.getCond() == 3)
				{
					qs.setCond(4);
				}
			}
			
			ReflectionUtils.enterReflection(player, 198);
			return null;
		}
		
		if (event.equalsIgnoreCase("LeaveInstance"))
		{
			player.teleToLocation(17192, 114173, -3439, ReflectionManager.DEFAULT);
			return null;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		
		if (player.getLevel() < 38)
		{
			return "33155-lvl.htm";
		}
		
		if (npcId == LILEJ)
		{
			if (cond < 5)
			{
				return "33155.htm";
			}
		}
		
		if (npcId == LINKENS)
		{
			if (cond == 1)
			{
				return "33163.htm";
			}
			
			if (cond == 2)
			{
				return "33163-5.htm";
			}
			
			if (cond == 5)
			{
				if (st.getQuestItemsCount(MARTES_CORE) == 0)
				{
					return "33163-14.htm";
				}
				else if (st.getQuestItemsCount(MARTES_CORE) != 0)
				{
					st.takeItems(MARTES_CORE, -1);
					st.takeItems(TRESURE_TOOL, -1);
					st.addExpAndSp(480000, 312000);
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(false);
					return "33163-15.htm";
				}
			}
		}
		
		if (npcId == MARTES_NPC)
		{
			if (cond == 3)
			{
				return "25829.htm";
			}
			
			if (cond == 5)
			{
				return "25829-1.htm";
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		
		if (player.getParty() == null)
		{
			st.setCond(5);
		}
		else
		{
			for (Player member : player.getParty().getPartyMembers())
			{
				QuestState qs = member.getQuestState(Q10352_LegacyOfCrumaTower.class);
				
				if ((qs == null) || (qs.getCond() != 4))
				{
					continue;
				}
				
				qs.setCond(5);
			}
		}
		
		st.getPlayer().getReflection().addSpawnWithoutRespawn(MARTES_NPC, Location.findPointToStay(st.getPlayer(), 50, 100), st.getPlayer().getGeoIndex());
		return null;
	}
}