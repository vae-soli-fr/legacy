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
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00463_IMustBeAGenius extends Quest implements ScriptFile
{
	// Npc
	private static final int GUTENHAGEN = 32069;
	// Items
	private static final int CORPSE_LOG = 15510;
	private static final int COLLECTION = 15511;
	// Monsters
	private static final int[] MOBS =
	{
		22801,
		22802,
		22804,
		22805,
		22807,
		22808,
		22809,
		22810,
		22811,
		22812
	};
	
	public Q00463_IMustBeAGenius()
	{
		super(false);
		addStartNpc(GUTENHAGEN);
		addTalkId(GUTENHAGEN);
		addQuestItem(CORPSE_LOG, COLLECTION);
		addKillId(MOBS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "collecter_gutenhagen_q0463_05.htm":
				qs.playSound(SOUND_ACCEPT);
				qs.setState(STARTED);
				qs.setCond(1);
				int _number = Rnd.get(500, 600);
				qs.set("number", String.valueOf(_number));
				for (int _mob : MOBS)
				{
					int _rand = Rnd.get(-2, 4);
					
					if (_rand == 0)
					{
						_rand = 5;
					}
					
					qs.set(String.valueOf(_mob), String.valueOf(_rand));
				}
				qs.set(String.valueOf(MOBS[Rnd.get(0, MOBS.length - 1)]), String.valueOf(Rnd.get(1, 100)));
				htmltext = HtmCache.getInstance().getNotNull("quests/Q00463_IMustBeAGenius/" + event, qs.getPlayer());
				htmltext = htmltext.replace("%num%", String.valueOf(_number));
				break;
			
			case "collecter_gutenhagen_q0463_07.htm":
				htmltext = HtmCache.getInstance().getNotNull("quests/Q00463_IMustBeAGenius/" + event, qs.getPlayer());
				htmltext = htmltext.replace("%num%", qs.get("number"));
				break;
			
			case "reward":
				int diff = qs.getInt("number") - 500;
				if (diff == 0)
				{
					qs.addExpAndSp(198725, 15892);
					htmltext = "collecter_gutenhagen_q0463_09.htm";
				}
				else if ((diff >= 1) && (diff < 5))
				{
					qs.addExpAndSp(278216, 22249);
					htmltext = "collecter_gutenhagen_q0463_10.htm";
				}
				else if ((diff >= 5) && (diff < 10))
				{
					qs.addExpAndSp(317961, 25427);
					htmltext = "collecter_gutenhagen_q0463_11.htm";
				}
				else if ((diff >= 10) && (diff < 25))
				{
					qs.addExpAndSp(357706, 28606);
					htmltext = "collecter_gutenhagen_q0463_11.htm";
				}
				else if ((diff >= 25) && (diff < 40))
				{
					qs.addExpAndSp(397451, 31784);
					htmltext = "collecter_gutenhagen_q0463_12.htm";
				}
				else if ((diff >= 40) && (diff < 60))
				{
					qs.addExpAndSp(596176, 47677);
					htmltext = "collecter_gutenhagen_q0463_13.htm";
				}
				else if ((diff >= 60) && (diff < 72))
				{
					qs.addExpAndSp(715411, 57212);
					htmltext = "collecter_gutenhagen_q0463_14.htm";
				}
				else if ((diff >= 72) && (diff < 81))
				{
					qs.addExpAndSp(794901, 63569);
					htmltext = "collecter_gutenhagen_q0463_14.htm";
				}
				else if ((diff >= 81) && (diff < 89))
				{
					qs.addExpAndSp(914137, 73104);
					htmltext = "collecter_gutenhagen_q0463_15.htm";
				}
				else
				{
					qs.addExpAndSp(1192352, 95353);
					htmltext = "collecter_gutenhagen_q0463_15.htm";
				}
				qs.unset("cond");
				qs.unset("number");
				for (int _mob : MOBS)
				{
					qs.unset(String.valueOf(_mob));
				}
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final Player player = qs.getPlayer();
		
		switch (qs.getState())
		{
			case CREATED:
				if (player.getLevel() >= 70)
				{
					if (qs.isNowAvailableByTime())
					{
						htmltext = "collecter_gutenhagen_q0463_01.htm";
					}
					else
					{
						htmltext = "collecter_gutenhagen_q0463_03.htm";
					}
				}
				else
				{
					htmltext = "collecter_gutenhagen_q0463_02.htm";
				}
				break;
			
			case STARTED:
				if (qs.getCond() == 1)
				{
					htmltext = "collecter_gutenhagen_q0463_06.htm";
				}
				else if (qs.getCond() == 2)
				{
					if (qs.getQuestItemsCount(COLLECTION) > 0)
					{
						qs.takeItems(COLLECTION, -1);
						htmltext = "collecter_gutenhagen_q0463_08.htm";
					}
					else
					{
						htmltext = "collecter_gutenhagen_q0463_08a.htm";
					}
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getState() == STARTED) && (qs.getCond() == 1))
		{
			int _day_number = qs.getInt("number");
			int _number = qs.getInt(String.valueOf(npc.getId()));
			
			if (_number > 0)
			{
				qs.giveItems(CORPSE_LOG, _number);
				qs.playSound(SOUND_ITEMGET);
				Functions.npcSay(npc, NpcString.ATT__ATTACK__S1__RO__ROGUE__S2, qs.getPlayer().getName(), String.valueOf(_number));
			}
			else if ((_number < 0) && ((qs.getQuestItemsCount(CORPSE_LOG) + _number) > 0))
			{
				qs.takeItems(CORPSE_LOG, Math.abs(_number));
				qs.playSound(SOUND_ITEMGET);
				Functions.npcSay(npc, NpcString.ATT__ATTACK__S1__RO__ROGUE__S2, qs.getPlayer().getName(), String.valueOf(_number));
			}
			
			if (qs.getQuestItemsCount(CORPSE_LOG) >= _day_number)
			{
				qs.takeItems(CORPSE_LOG, -1);
				qs.giveItems(COLLECTION, 1);
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
