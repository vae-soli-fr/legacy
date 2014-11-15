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

import java.util.StringTokenizer;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

public class Q00455_WingsOfSand extends Quest implements ScriptFile
{
	private static final int[] SeparatedSoul =
	{
		32864,
		32865,
		32866,
		32867,
		32868,
		32869,
		32870
	};
	private static final int LargeDragon = 17250;
	private static final int[] raids =
	{
		25718,
		25719,
		25720,
		25721,
		25722,
		25723,
		25724
	};
	private static final int[] reward_resipes_w =
	{
		15815,
		15816,
		15817,
		15818,
		15819,
		15820,
		15821,
		15822,
		15823,
		15824,
		15825
	};
	private static final int[] reward_resipes_a =
	{
		15792,
		15793,
		15794,
		15795,
		15796,
		15797,
		15798,
		15799,
		15800,
		15801,
		15802,
		15803,
		15804,
		15805,
		15806,
		15807,
		15808
	};
	private static final int[] reward_resipes_acc =
	{
		15809,
		15810,
		15811
	};
	private static final int[] reward_mats_w =
	{
		15634,
		15635,
		15636,
		15637,
		15638,
		15639,
		15640,
		15641,
		15642,
		15643,
		15644
	};
	private static final int[] reward_mats_a =
	{
		15660,
		15661,
		15662,
		15663,
		15664,
		15665,
		15666,
		15667,
		15668,
		15669,
		15670,
		15671,
		15672,
		15673,
		15674,
		15675,
		15691,
		15693
	};
	private static final int[] reward_mats_acc =
	{
		15769,
		15770,
		15771
	};
	private static final int[] reward_attr_crystal =
	{
		9552,
		9553,
		9554,
		9555,
		9556,
		9557
	};
	private static final int[] reward_ench_scroll =
	{
		6577,
		6578
	};
	
	public Q00455_WingsOfSand()
	{
		super(PARTY_ALL);
		addStartNpc(SeparatedSoul);
		addQuestItem(LargeDragon);
		addKillId(raids);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("sepsoul_q455_05.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.startsWith("sepsoul_q455_08.htm"))
		{
			qs.takeAllItems(LargeDragon);
			StringTokenizer tokenizer = new StringTokenizer(event);
			tokenizer.nextToken();
			
			switch (Integer.parseInt(tokenizer.nextToken()))
			{
				case 1:
					qs.giveItems(reward_mats_w[Rnd.get(reward_mats_w.length)], Rnd.get(1, 2));
					break;
				
				case 2:
					qs.giveItems(reward_mats_a[Rnd.get(reward_mats_a.length)], Rnd.get(1, 2));
					break;
				
				case 3:
					qs.giveItems(reward_mats_acc[Rnd.get(reward_mats_acc.length)], Rnd.get(1, 2));
					break;
				
				case 4:
					qs.giveItems(reward_attr_crystal[Rnd.get(reward_attr_crystal.length)], 1);
					break;
				
				default:
					break;
			}
			
			htmltext = "sepsoul_q455_08.htm";
			qs.setState(COMPLETED);
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(this);
		}
		else if (event.startsWith("sepsoul_q455_11.htm"))
		{
			qs.takeAllItems(LargeDragon);
			StringTokenizer tokenizer = new StringTokenizer(event);
			tokenizer.nextToken();
			
			switch (Integer.parseInt(tokenizer.nextToken()))
			{
				case 1:
					qs.giveItems(reward_resipes_w[Rnd.get(reward_resipes_w.length)], 1);
					break;
				
				case 2:
					qs.giveItems(reward_resipes_a[Rnd.get(reward_resipes_a.length)], 1);
					break;
				
				case 3:
					qs.giveItems(reward_resipes_acc[Rnd.get(reward_resipes_acc.length)], 1);
					break;
				
				case 4:
					qs.giveItems(reward_attr_crystal[Rnd.get(reward_attr_crystal.length)], 2);
					break;
				
				default:
					break;
			}
			
			if (Rnd.chance(25))
			{
				qs.giveItems(reward_ench_scroll[Rnd.get(reward_ench_scroll.length)], 1);
			}
			
			htmltext = "sepsoul_q455_11.htm";
			qs.setState(COMPLETED);
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(this);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (Util.contains(SeparatedSoul, npc.getId()))
		{
			switch (qs.getState())
			{
				case CREATED:
					if (qs.isNowAvailableByTime())
					{
						if (qs.getPlayer().getLevel() >= 80)
						{
							htmltext = "sepsoul_q455_01.htm";
						}
						else
						{
							htmltext = "sepsoul_q455_00.htm";
							qs.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "sepsoul_q455_00a.htm";
					}
					break;
				
				case STARTED:
					if (cond == 1)
					{
						htmltext = "sepsoul_q455_06.htm";
					}
					else if (cond == 2)
					{
						htmltext = "sepsoul_q455_07.htm";
					}
					else if (cond == 3)
					{
						htmltext = "sepsoul_q455_10.htm";
					}
					break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		if (cond == 1)
		{
			qs.giveItems(LargeDragon, 1);
			qs.setCond(2);
		}
		else if (cond == 2)
		{
			qs.setCond(3);
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
