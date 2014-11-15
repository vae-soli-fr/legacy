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

public class Q00336_CoinsOfMagic extends Quest implements ScriptFile
{
	private static final int COIN_DIAGRAM = 3811;
	private static final int KALDIS_COIN = 3812;
	private static final int MEMBERSHIP_1 = 3813;
	private static final int MEMBERSHIP_2 = 3814;
	private static final int MEMBERSHIP_3 = 3815;
	private static final int BLOOD_MEDUSA = 3472;
	private static final int BLOOD_WEREWOLF = 3473;
	private static final int BLOOD_BASILISK = 3474;
	private static final int BLOOD_DREVANUL = 3475;
	private static final int BLOOD_SUCCUBUS = 3476;
	private static final int GOLD_WYVERN = 3482;
	private static final int GOLD_KNIGHT = 3483;
	private static final int GOLD_GIANT = 3484;
	private static final int GOLD_DRAKE = 3485;
	private static final int GOLD_WYRM = 3486;
	private static final int SILVER_UNICORN = 3490;
	private static final int SILVER_FAIRY = 3491;
	private static final int SILVER_DRYAD = 3492;
	private static final int SILVER_GOLEM = 3494;
	private static final int SILVER_UNDINE = 3495;
	private static final int[] BASIC_COINS =
	{
		BLOOD_MEDUSA,
		GOLD_WYVERN,
		SILVER_UNICORN
	};
	private static final int SORINT = 30232;
	private static final int BERNARD = 30702;
	private static final int PAGE = 30696;
	private static final int HAGGER = 30183;
	private static final int STAN = 30200;
	private static final int RALFORD = 30165;
	private static final int FERRIS = 30847;
	private static final int COLLOB = 30092;
	private static final int PANO = 30078;
	private static final int DUNING = 30688;
	private static final int LORAIN = 30673;
	private static final int TimakOrcArcher = 20584;
	private static final int TimakOrcSoldier = 20585;
	private static final int TimakOrcShaman = 20587;
	private static final int Lakin = 20604;
	private static final int TorturedUndead = 20678;
	private static final int HatarHanishee = 20663;
	private static final int Shackle = 20235;
	private static final int TimakOrc = 20583;
	private static final int HeadlessKnight = 20146;
	private static final int RoyalCaveServant = 20240;
	private static final int MalrukSuccubusTuren = 20245;
	private static final int Formor = 20568;
	private static final int FormorElder = 20569;
	private static final int VanorSilenosShaman = 20685;
	private static final int TarlkBugbearHighWarrior = 20572;
	private static final int OelMahum = 20161;
	private static final int OelMahumWarrior = 20575;
	private static final int HaritLizardmanMatriarch = 20645;
	private static final int HaritLizardmanShaman = 20644;
	private static final int GraveLich = 21003;
	private static final int DoomServant = 21006;
	private static final int DoomArcher = 21008;
	private static final int DoomKnight = 20674;
	private static final int Kookaburra2 = 21276;
	private static final int Kookaburra3 = 21275;
	private static final int Kookaburra4 = 21274;
	private static final int Antelope2 = 21278;
	private static final int Antelope3 = 21279;
	private static final int Antelope4 = 21280;
	private static final int Bandersnatch2 = 21282;
	private static final int Bandersnatch3 = 21284;
	private static final int Bandersnatch4 = 21283;
	private static final int Buffalo2 = 21287;
	private static final int Buffalo3 = 21288;
	private static final int Buffalo4 = 21286;
	private static final int ClawsofSplendor = 21521;
	private static final int WisdomofSplendor = 21526;
	private static final int PunishmentofSplendor = 21531;
	private static final int WailingofSplendor = 21539;
	private static final int HungeredCorpse = 20954;
	private static final int BloodyGhost = 20960;
	private static final int NihilInvader = 20957;
	private static final int DarkGuard = 20959;
	private static final int[][] PROMOTE =
	{
		{},
		{},
		{
			SILVER_DRYAD,
			BLOOD_BASILISK,
			BLOOD_SUCCUBUS,
			SILVER_UNDINE,
			GOLD_GIANT,
			GOLD_WYRM
		},
		{
			BLOOD_WEREWOLF,
			GOLD_DRAKE,
			SILVER_FAIRY,
			BLOOD_DREVANUL,
			GOLD_KNIGHT,
			SILVER_GOLEM
		}
	};
	private static final int[][] EXCHANGE_LEVEL =
	{
		{
			PAGE,
			3
		},
		{
			LORAIN,
			3
		},
		{
			HAGGER,
			3
		},
		{
			RALFORD,
			2
		},
		{
			STAN,
			2
		},
		{
			DUNING,
			2
		},
		{
			FERRIS,
			1
		},
		{
			COLLOB,
			1
		},
		{
			PANO,
			1
		},
	};
	private static final int[][] DROPLIST =
	{
		{
			TimakOrcArcher,
			BLOOD_MEDUSA
		},
		{
			TimakOrcSoldier,
			BLOOD_MEDUSA
		},
		{
			TimakOrcShaman,
			BLOOD_MEDUSA
		},
		{
			Lakin,
			BLOOD_MEDUSA
		},
		{
			TorturedUndead,
			BLOOD_MEDUSA
		},
		{
			HatarHanishee,
			BLOOD_MEDUSA
		},
		{
			TimakOrc,
			GOLD_WYVERN
		},
		{
			Shackle,
			GOLD_WYVERN
		},
		{
			HeadlessKnight,
			GOLD_WYVERN
		},
		{
			RoyalCaveServant,
			GOLD_WYVERN
		},
		{
			MalrukSuccubusTuren,
			GOLD_WYVERN
		},
		{
			Formor,
			SILVER_UNICORN
		},
		{
			FormorElder,
			SILVER_UNICORN
		},
		{
			VanorSilenosShaman,
			SILVER_UNICORN
		},
		{
			TarlkBugbearHighWarrior,
			SILVER_UNICORN
		},
		{
			OelMahum,
			SILVER_UNICORN
		},
		{
			OelMahumWarrior,
			SILVER_UNICORN
		},
	};
	private static final int[] MONSTERS =
	{
		GraveLich,
		DoomServant,
		DoomArcher,
		DoomKnight,
		Kookaburra2,
		Kookaburra3,
		Kookaburra4,
		Antelope2,
		Antelope3,
		Antelope4,
		Bandersnatch2,
		Bandersnatch3,
		Bandersnatch4,
		Buffalo2,
		Buffalo3,
		Buffalo4,
		ClawsofSplendor,
		WisdomofSplendor,
		PunishmentofSplendor,
		WailingofSplendor,
		HungeredCorpse,
		BloodyGhost,
		NihilInvader,
		DarkGuard
	};
	
	public Q00336_CoinsOfMagic()
	{
		super(true);
		addStartNpc(SORINT);
		addTalkId(SORINT, BERNARD, PAGE, HAGGER, STAN, RALFORD, FERRIS, COLLOB, PANO, DUNING, LORAIN);
		addQuestItem(COIN_DIAGRAM, KALDIS_COIN, MEMBERSHIP_1, MEMBERSHIP_2, MEMBERSHIP_3);
		addKillId(HaritLizardmanMatriarch, HaritLizardmanShaman);
		addKillId(MONSTERS);
		for (int mob[] : DROPLIST)
		{
			addKillId(mob[0]);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "30702-06.htm":
				if (cond < 7)
				{
					qs.setCond(7);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "30232-22.htm":
				if (cond < 6)
				{
					qs.setCond(6);
				}
				break;
			
			case "30232-23.htm":
				if (cond < 5)
				{
					qs.setCond(5);
				}
				break;
			
			case "30702-02.htm":
				qs.setCond(2);
				break;
			
			case "30232-05.htm":
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				qs.giveItems(COIN_DIAGRAM, 1);
				qs.setCond(1);
				break;
			
			case "30232-04.htm":
			case "30232-18a.htm":
				qs.exitCurrentQuest(true);
				qs.playSound(SOUND_GIVEUP);
				break;
			
			case "raise":
				htmltext = promote(qs);
				break;
		}
		
		return htmltext;
	}
	
	private String promote(QuestState qs)
	{
		final int grade = qs.getInt("grade");
		String html;
		
		if (grade == 1)
		{
			html = "30232-15.htm";
		}
		else
		{
			int h = 0;
			
			for (int i : PROMOTE[grade])
			{
				if (qs.getQuestItemsCount(i) > 0)
				{
					h += 1;
				}
			}
			
			if (h == 6)
			{
				for (int i : PROMOTE[grade])
				{
					qs.takeItems(i, 1);
				}
				
				html = "30232-" + str(19 - grade) + ".htm";
				qs.takeItems(3812 + grade, -1);
				qs.giveItems(3811 + grade, 1);
				qs.set("grade", str(grade - 1));
				
				if (grade == 3)
				{
					qs.setCond(9);
				}
				else if (grade == 2)
				{
					qs.setCond(11);
				}
				
				qs.playSound(SOUND_FANFARE_MIDDLE);
			}
			else
			{
				html = "30232-" + str(16 - grade) + ".htm";
				
				if (grade == 3)
				{
					qs.setCond(8);
				}
				else if (grade == 2)
				{
					qs.setCond(9);
				}
			}
		}
		
		return html;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int npcId = npc.getId();
		final int id = qs.getState();
		final int grade = qs.getInt("grade");
		
		switch (npcId)
		{
			case SORINT:
				if (id == CREATED)
				{
					if (qs.getPlayer().getLevel() < 40)
					{
						htmltext = "30232-01.htm";
						qs.exitCurrentQuest(true);
					}
					else
					{
						htmltext = "30232-02.htm";
					}
				}
				else if (qs.getQuestItemsCount(COIN_DIAGRAM) > 0)
				{
					if (qs.getQuestItemsCount(KALDIS_COIN) > 0)
					{
						qs.takeItems(KALDIS_COIN, -1);
						qs.takeItems(COIN_DIAGRAM, -1);
						qs.giveItems(MEMBERSHIP_3, 1);
						qs.set("grade", "3");
						qs.setCond(4);
						qs.playSound(SOUND_FANFARE_MIDDLE);
						htmltext = "30232-07.htm";
					}
					else
					{
						htmltext = "30232-06.htm";
					}
				}
				else if (grade == 3)
				{
					htmltext = "30232-12.htm";
				}
				else if (grade == 2)
				{
					htmltext = "30232-11.htm";
				}
				else if (grade == 1)
				{
					htmltext = "30232-10.htm";
				}
				break;
			
			case BERNARD:
				if ((qs.getQuestItemsCount(COIN_DIAGRAM) > 0) && (grade == 0))
				{
					htmltext = "30702-01.htm";
				}
				else if (grade == 3)
				{
					htmltext = "30702-05.htm";
				}
				break;
			
			default:
				for (int e[] : EXCHANGE_LEVEL)
				{
					if ((npcId == e[0]) && (grade <= e[1]))
					{
						htmltext = npcId + "-01.htm";
					}
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		final int grade = qs.getInt("grade");
		final int chance = (npc.getLevel() + (grade * 3)) - 20;
		final int npcId = npc.getId();
		
		if ((npcId == HaritLizardmanMatriarch) || (npcId == HaritLizardmanShaman))
		{
			if (cond == 2)
			{
				if (qs.rollAndGive(KALDIS_COIN, 1, 1, 1, 10 * npc.getTemplate().rateHp))
				{
					qs.setCond(3);
				}
			}
			
			return null;
		}
		
		for (int[] e : DROPLIST)
		{
			if (e[0] == npcId)
			{
				qs.rollAndGive(e[1], 1, chance);
				return null;
			}
		}
		
		for (int u : MONSTERS)
		{
			if (u == npcId)
			{
				qs.rollAndGive(BASIC_COINS[Rnd.get(BASIC_COINS.length)], 1, chance * npc.getTemplate().rateHp);
				return null;
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
