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

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00197_SevenSignsTheSacredBookOfSeal extends Quest implements ScriptFile
{
	private static final int Wood = 32593;
	private static final int Orven = 30857;
	private static final int Leopard = 32594;
	private static final int Lawrence = 32595;
	private static final int ShilensEvilThoughts = 27396;
	private static final int Sofia = 32596;
	private static final int PieceofDoubt = 14354;
	private static final int MysteriousHandwrittenText = 13829;
	
	public Q00197_SevenSignsTheSacredBookOfSeal()
	{
		super(false);
		addStartNpc(Wood);
		addTalkId(Wood, Orven, Leopard, Lawrence, Sofia);
		addKillId(ShilensEvilThoughts);
		addQuestItem(PieceofDoubt, MysteriousHandwrittenText);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();
		String htmltext = event;
		
		if (event.equalsIgnoreCase("wood_q197_2.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("orven_q197_2.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("leopard_q197_2.htm"))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("lawrence_q197_2.htm"))
		{
			NpcInstance mob = st.addSpawn(ShilensEvilThoughts, 152520, -57502, -3408, 0, 0, 180000);
			Functions.npcSay(mob, "Shilen's power is endless!");
			mob.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, 100000);
			st.set("evilthought", 1);
		}
		else if (event.equalsIgnoreCase("lawrence_q197_4.htm"))
		{
			st.setCond(5);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("sofia_q197_2.htm"))
		{
			st.setCond(6);
			st.giveItems(MysteriousHandwrittenText, 1);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("wood_q197_4.htm"))
		{
			if (player.getBaseClassId() == player.getActiveClassId())
			{
				st.takeItems(PieceofDoubt, -1);
				st.takeItems(MysteriousHandwrittenText, -1);
				st.addExpAndSp(10000000, 2500000);
				st.setState(COMPLETED);
				st.exitCurrentQuest(false);
				st.playSound(SOUND_FINISH);
			}
			else
			{
				return "subclass_forbidden.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		Player player = st.getPlayer();
		String htmltext = "noquest";
		
		if (npcId == Wood)
		{
			QuestState qs = player.getQuestState(Q00196_SevenSignsSealOfTheEmperor.class);
			
			if (cond == 0)
			{
				if ((player.getLevel() >= 79) && (qs != null) && qs.isCompleted())
				{
					htmltext = "wood_q197_1.htm";
				}
				else
				{
					htmltext = "wood_q197_0.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 6)
			{
				htmltext = "wood_q197_3.htm";
			}
			else
			{
				htmltext = "wood_q197_5.htm";
			}
		}
		else if (npcId == Orven)
		{
			if (cond == 1)
			{
				htmltext = "orven_q197_1.htm";
			}
			else if (cond == 2)
			{
				htmltext = "orven_q197_3.htm";
			}
		}
		else if (npcId == Leopard)
		{
			if (cond == 2)
			{
				htmltext = "leopard_q197_1.htm";
			}
			else if (cond == 3)
			{
				htmltext = "leopard_q197_3.htm";
			}
		}
		else if (npcId == Lawrence)
		{
			if (cond == 3)
			{
				if ((st.get("evilthought") != null) && (Integer.parseInt(st.get("evilthought")) == 1))
				{
					htmltext = "lawrence_q197_0.htm";
				}
				else
				{
					htmltext = "lawrence_q197_1.htm";
				}
			}
			else if (cond == 4)
			{
				htmltext = "lawrence_q197_3.htm";
			}
			else if (cond == 5)
			{
				htmltext = "lawrence_q197_5.htm";
			}
		}
		else if (npcId == Sofia)
		{
			if (cond == 5)
			{
				htmltext = "sofia_q197_1.htm";
			}
			else if (cond == 6)
			{
				htmltext = "sofia_q197_3.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		Player player = st.getPlayer();
		
		if (player == null)
		{
			return null;
		}
		
		if ((npc.getNpcId() == ShilensEvilThoughts) && (cond == 3))
		{
			st.setCond(4);
			st.playSound(SOUND_ITEMGET);
			st.giveItems(PieceofDoubt, 1);
			st.set("evilthought", 2);
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
