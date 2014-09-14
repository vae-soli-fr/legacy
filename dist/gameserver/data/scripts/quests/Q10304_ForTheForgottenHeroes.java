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

public class Q10304_ForTheForgottenHeroes extends Quest implements ScriptFile
{
	// npc
	private static final int ISHAEL = 32894;
	
	// mobs
	private static final int YUI = 25837;
	private static final int KINEN = 25840;
	private static final int KONJAN = 25845;
	private static final int RASINDA = 25841;
	
	private static final int MAKYSHA = 25838;
	private static final int HORNAPI = 25839;
	
	private static final int YONTYMAK = 25846;
	private static final int FRON = 25825;
	
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
	
	public Q10304_ForTheForgottenHeroes()
	{
		super(PARTY_ALL);
		addStartNpc(ISHAEL);
		addTalkId(ISHAEL);
		addKillId(YUI);
		addKillId(KINEN);
		addKillId(KONJAN);
		addKillId(RASINDA);
		addKillId(MAKYSHA);
		addKillId(HORNAPI);
		addKillId(YONTYMAK);
		addKillId(FRON);
		
		addLevelCheck(90, 99);
		addQuestCompletedCheck(Q10302_UnsettlingShadowAndRumors.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
		if (event.equalsIgnoreCase("32894-7.htm"))
		{
			st.setCond(2);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			st.takeItems(17618, -1);
		}
		else if (event.equalsIgnoreCase("32894-11.htm"))
		{
			st.addExpAndSp(15197798, 6502166);
			st.giveItems(57, 47085998);
			st.giveItems(33467, 1);
			st.giveItems(33466, 1);
			st.giveItems(32779, 1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		st.getPlayer();
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		
		if (state == COMPLETED)
		{
			return "32894-comp.htm";
		}
		
		if (st.getPlayer().getLevel() < 90)
		{
			return "32894-lvl.htm";
		}
		QuestState qs = st.getPlayer().getQuestState(Q10302_UnsettlingShadowAndRumors.class);
		if ((qs == null) || !qs.isCompleted())
		{
			return "32894-lvl.htm";
		}
		
		if (npcId == ISHAEL)
		{
			if (cond == 1)
			{
				return "32894-3.htm";
			}
			else if (cond == 2)
			{
				return "32894-15.htm";
			}
			else if (cond == 9)
			{
				return "32894-10.htm";
			}
		}
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		int cond = qs.getCond();
		int npcId = npc.getNpcId();
		if ((npcId == YUI) && (cond == 2))
		{
			qs.setCond(3);
		}
		else if ((npcId == KINEN) && (cond == 3))
		{
			qs.setCond(4);
		}
		else if ((npcId == KONJAN) && (cond == 4))
		{
			qs.setCond(5);
		}
		else if ((npcId == RASINDA) && (cond == 5))
		{
			qs.setCond(6);
		}
		else if ((npcId == MAKYSHA) && (cond == 6))
		{
			qs.getPlayer().setVar("MarkywaKilled", "true", -1);
			checkVars(qs, qs.getPlayer());
		}
		else if ((npcId == HORNAPI) && (cond == 6))
		{
			qs.getPlayer().setVar("HornapiKilled", "true", -1);
			checkVars(qs, qs.getPlayer());
		}
		else if ((npcId == YONTYMAK) && (cond == 7))
		{
			qs.setCond(8);
		}
		else if ((npcId == FRON) && (cond == 8))
		{
			qs.setCond(9);
		}
		return null;
	}
	
	private static void checkVars(QuestState qs, Player player)
	{
		if (player == null)
		{
			return;
		}
		if ((player.getVar("MarkywaKilled") != null) && (player.getVar("HornapiKilled") != null))
		{
			qs.setCond(7);
		}
	}
}