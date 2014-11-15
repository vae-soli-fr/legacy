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

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.RadarControl;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00348_AnArrogantSearch extends Quest implements ScriptFile
{
	private final static int ARK_GUARDIAN_ELBEROTH = 27182;
	private final static int ARK_GUARDIAN_SHADOWFANG = 27183;
	private final static int ANGEL_KILLER = 27184;
	private final static int PLATINUM_TRIBE_SHAMAN = 20828;
	private final static int PLATINUM_TRIBE_OVERLORD = 20829;
	private final static int Yintzu = 20647;
	private final static int Paliote = 20648;
	private final static int GUARDIAN_ANGEL = 20859;
	private final static int SEAL_ANGEL = 20860;
	private final static int HANELLIN = 30864;
	private final static int HOLY_ARK_OF_SECRECY_1 = 30977;
	private final static int HOLY_ARK_OF_SECRECY_2 = 30978;
	private final static int HOLY_ARK_OF_SECRECY_3 = 30979;
	private final static int ARK_GUARDIANS_CORPSE = 30980;
	private final static int HARNE = 30144;
	private final static int CLAUDIA_ATHEBALT = 31001;
	private final static int MARTIEN = 30645;
	private final static int SHELL_OF_MONSTERS = 14857;
	private final static int HANELLINS_FIRST_LETTER = 4288;
	private final static int HANELLINS_SECOND_LETTER = 4289;
	private final static int HANELLINS_THIRD_LETTER = 4290;
	private final static int FIRST_KEY_OF_ARK = 4291;
	private final static int SECOND_KEY_OF_ARK = 4292;
	private final static int THIRD_KEY_OF_ARK = 4293;
	private final static int WHITE_FABRIC_1 = 4294; // to use on Platinum Tribe Shamans/Overlords
	private final static int BLOODED_FABRIC = 4295;
	private final static int HANELLINS_WHITE_FLOWER = 4394;
	private final static int HANELLINS_RED_FLOWER = 4395;
	private final static int HANELLINS_YELLOW_FLOWER = 4396;
	private final static int BOOK_OF_SAINT = 4397; // Ark2 (after fight with Elberoth)
	private final static int BLOOD_OF_SAINT = 4398; // Ark1 (after fight with Angel Killer)
	private final static int BRANCH_OF_SAINT = 4399; // Ark3 (after fight with Shadowfang)
	private final static int WHITE_FABRIC_0 = 4400; // talk to Hanellin to see what to do (for companions)
	private final static int WHITE_FABRIC_2 = 5232; // to use on Guardian Angels and Seal Angels
	private final static int ANTIDOTE = 1831;
	private final static int HEALING_POTION = 1061;
	// ARK: [key, summon, no-key text, openning-with-key text, already-openned text, content item]
	private final static Map<Integer, Integer[]> ARKS = new HashMap<>();
	private final static Map<Integer, String[]> ARKS_TEXT = new HashMap<>();
	// npc: letter to take, item to check for, 1st time htm, return htm, completed part htm, [x,y,z of chest]
	private final static Map<Integer, Integer[]> ARK_OWNERS = new HashMap<>();
	private final static Map<Integer, String[]> ARK_OWNERS_TEXT = new HashMap<>();
	// mob: cond, giveItem, amount, chance%, takeItem (assumed to take only 1 of it)
	private final static Map<Integer, Integer[]> DROPS = new HashMap<>();
	static
	{
		ARKS.put(HOLY_ARK_OF_SECRECY_1, new Integer[]
		{
			FIRST_KEY_OF_ARK,
			0,
			BLOOD_OF_SAINT
		});
		ARKS.put(HOLY_ARK_OF_SECRECY_2, new Integer[]
		{
			SECOND_KEY_OF_ARK,
			ARK_GUARDIAN_ELBEROTH,
			BOOK_OF_SAINT
		});
		ARKS.put(HOLY_ARK_OF_SECRECY_3, new Integer[]
		{
			THIRD_KEY_OF_ARK,
			ARK_GUARDIAN_SHADOWFANG,
			BRANCH_OF_SAINT
		});
		ARKS_TEXT.put(HOLY_ARK_OF_SECRECY_1, new String[]
		{
			"30977-01.htm",
			"30977-02.htm",
			"30977-03.htm"
		});
		ARKS_TEXT.put(HOLY_ARK_OF_SECRECY_2, new String[]
		{
			"That doesn't belong to you.  Don't touch it!",
			"30978-02.htm",
			"30978-03.htm"
		});
		ARKS_TEXT.put(HOLY_ARK_OF_SECRECY_3, new String[]
		{
			"Get off my sight, you infidels!",
			"30979-02.htm",
			"30979-03.htm"
		});
		ARK_OWNERS.put(HARNE, new Integer[]
		{
			HANELLINS_FIRST_LETTER,
			BLOOD_OF_SAINT,
			-418,
			44174,
			-3568
		});
		ARK_OWNERS.put(CLAUDIA_ATHEBALT, new Integer[]
		{
			HANELLINS_SECOND_LETTER,
			BOOK_OF_SAINT,
			181472,
			7158,
			-2725
		});
		ARK_OWNERS.put(MARTIEN, new Integer[]
		{
			HANELLINS_THIRD_LETTER,
			BRANCH_OF_SAINT,
			50693,
			158674,
			376
		});
		ARK_OWNERS_TEXT.put(HARNE, new String[]
		{
			"30144-01.htm",
			"30144-02.htm",
			"30144-03.htm"
		});
		ARK_OWNERS_TEXT.put(CLAUDIA_ATHEBALT, new String[]
		{
			"31001-01.htm",
			"31001-02.htm",
			"31001-03.htm"
		});
		ARK_OWNERS_TEXT.put(MARTIEN, new String[]
		{
			"30645-01.htm",
			"30645-02.htm",
			"30645-03.htm"
		});
		// NPC, { min cond, item to give, max count, chance, item to take }
		DROPS.put(Yintzu, new Integer[]
		{
			2,
			SHELL_OF_MONSTERS,
			1,
			10,
			0
		});
		DROPS.put(Paliote, new Integer[]
		{
			2,
			SHELL_OF_MONSTERS,
			1,
			10,
			0
		});
		DROPS.put(ANGEL_KILLER, new Integer[]
		{
			5,
			FIRST_KEY_OF_ARK,
			1,
			100,
			0
		});
		DROPS.put(ARK_GUARDIAN_ELBEROTH, new Integer[]
		{
			5,
			SECOND_KEY_OF_ARK,
			1,
			100,
			0
		});
		DROPS.put(ARK_GUARDIAN_SHADOWFANG, new Integer[]
		{
			5,
			THIRD_KEY_OF_ARK,
			1,
			100,
			0
		});
		DROPS.put(PLATINUM_TRIBE_SHAMAN, new Integer[]
		{
			25,
			BLOODED_FABRIC,
			Integer.MAX_VALUE,
			10,
			WHITE_FABRIC_1
		});
		DROPS.put(PLATINUM_TRIBE_OVERLORD, new Integer[]
		{
			25,
			BLOODED_FABRIC,
			Integer.MAX_VALUE,
			10,
			WHITE_FABRIC_1
		});
		DROPS.put(GUARDIAN_ANGEL, new Integer[]
		{
			25,
			BLOODED_FABRIC,
			Integer.MAX_VALUE,
			25,
			WHITE_FABRIC_2
		});
		DROPS.put(SEAL_ANGEL, new Integer[]
		{
			25,
			BLOODED_FABRIC,
			Integer.MAX_VALUE,
			25,
			WHITE_FABRIC_2
		});
	}
	
	public Q00348_AnArrogantSearch()
	{
		super(true);
		addStartNpc(HANELLIN);
		addTalkId(ARK_GUARDIANS_CORPSE);
		addQuestItem(HANELLINS_FIRST_LETTER, HANELLINS_SECOND_LETTER, HANELLINS_THIRD_LETTER, HANELLINS_WHITE_FLOWER, HANELLINS_RED_FLOWER, HANELLINS_YELLOW_FLOWER, BOOK_OF_SAINT, WHITE_FABRIC_1, BLOOD_OF_SAINT, BRANCH_OF_SAINT, WHITE_FABRIC_0, WHITE_FABRIC_2, FIRST_KEY_OF_ARK, SECOND_KEY_OF_ARK, THIRD_KEY_OF_ARK);
		for (int i : ARK_OWNERS.keySet())
		{
			addTalkId(i);
		}
		for (int i : ARKS.keySet())
		{
			addTalkId(i);
		}
		for (int i : DROPS.keySet())
		{
			addKillId(i);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30864_02":
				qs.setCond(2);
				htmltext = "30864-03.htm";
				break;
			
			case "30864_04a":
				qs.setCond(4);
				qs.takeItems(SHELL_OF_MONSTERS, -1);
				htmltext = "30864-04c.htm";
				qs.set("companions", "0");
				break;
			
			case "30864_04b":
				qs.setCond(3);
				qs.set("companions", "1");
				qs.takeItems(SHELL_OF_MONSTERS, -1);
				htmltext = "not yet implemented"; // TODO: give flowers & handle the multiperson quest...
				break;
			
			case "30864-09a.htm":
				qs.setCond(29);
				qs.giveItems(WHITE_FABRIC_2, 10);
				break;
			
			case "30864-10a.htm":
				if (qs.getQuestItemsCount(WHITE_FABRIC_2) < 10)
				{
					qs.giveItems(WHITE_FABRIC_2, 10 - qs.getQuestItemsCount(WHITE_FABRIC_2));
				}
				htmltext = "30864-10.htm";
				break;
			
			case "30864-10b.htm":
				if (qs.getQuestItemsCount(BLOODED_FABRIC) > 1)
				{
					long count = qs.takeItems(BLOODED_FABRIC, -1);
					qs.giveItems(ADENA_ID, count * 5000, true);
					htmltext = "30864-10.htm";
				}
				else
				{
					htmltext = "30864-11.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		final int id = qs.getState();
		
		if (npcId == HANELLIN)
		{
			if (id == CREATED)
			// if the quest was completed and the player still has a blooded fabric
			// tell them the "secret" that they can use it in order to visit Baium.
			{
				if (qs.getQuestItemsCount(BLOODED_FABRIC) >= 1)
				{
					htmltext = "30864-Baium.htm";
					qs.exitCurrentQuest(true);
				}
				else
				// else, start the quest normally
				{
					qs.setCond(0);
					
					if (qs.getPlayer().getLevel() < 60)
					{
						htmltext = "30864-01.htm"; // not qualified
						qs.exitCurrentQuest(true);
					}
					else if (cond == 0)
					{
						qs.setState(STARTED);
						qs.setCond(1);
						htmltext = "30864-02.htm"; // Successful start: begin the dialog which will set cond=2
					}
				}
			}
			// Player abandoned in the middle of last dialog...repeat the dialog.
			else if (cond == 1)
			{
				htmltext = "30864-02.htm"; // begin the dialog which will set cond=2
			}
			// Has returned before getting the powerstone
			else if ((cond == 2) && (qs.getQuestItemsCount(SHELL_OF_MONSTERS) == 0))
			{
				htmltext = "30864-03a.htm"; // go get the titan's powerstone
			}
			else if (cond == 2)
			{
				htmltext = "30864-04.htm"; // Ask "work alone or in group?"...only alone is implemented in v0.1
			}
			else if (cond == 4)
			{
				qs.setCond(5);
				qs.giveItems(HANELLINS_FIRST_LETTER, 1);
				qs.giveItems(HANELLINS_SECOND_LETTER, 1);
				qs.giveItems(HANELLINS_THIRD_LETTER, 1);
				htmltext = "30864-05.htm"; // Go get the 3 sacred relics
			}
			else if ((cond == 5) && ((qs.getQuestItemsCount(BOOK_OF_SAINT) + qs.getQuestItemsCount(BLOOD_OF_SAINT) + qs.getQuestItemsCount(BRANCH_OF_SAINT)) < 3))
			{
				htmltext = "30864-05.htm"; // Repeat: Go get the 3 sacred relics
			}
			else if (cond == 5)
			{
				htmltext = "30864-06.htm"; // All relics collected!...Get me antidotes & greater healing
				qs.takeItems(BOOK_OF_SAINT, -1);
				qs.takeItems(BLOOD_OF_SAINT, -1);
				qs.takeItems(BRANCH_OF_SAINT, -1);
				qs.setCond(22);
			}
			else if ((cond == 22) && (qs.getQuestItemsCount(ANTIDOTE) < 5) && (qs.getQuestItemsCount(HEALING_POTION) < 1))
			{
				htmltext = "30864-06a.htm"; // where are my antidotes & greater healing
			}
			else if (cond == 22)
			{
				qs.takeItems(ANTIDOTE, 5);
				qs.takeItems(HEALING_POTION, 1);
				
				if (qs.getInt("companions") == 0)
				{
					qs.setCond(25);
					htmltext = "30864-07.htm"; // go get platinum tribe blood...
					qs.giveItems(WHITE_FABRIC_1, 1);
				}
				else
				{
					qs.setCond(23);
					htmltext = "not implemented yet";
					qs.giveItems(WHITE_FABRIC_0, 3);
				}
			}
			else if ((cond == 25) && (qs.getQuestItemsCount(BLOODED_FABRIC) < 1))
			{
				if (qs.getQuestItemsCount(WHITE_FABRIC_1) < 1)
				{
					qs.giveItems(WHITE_FABRIC_1, 1);
				}
				
				htmltext = "30864-07a.htm";
			}
			else if ((cond == 26) && (qs.getQuestItemsCount(BLOODED_FABRIC) < 1))
			{
				if (qs.getQuestItemsCount(WHITE_FABRIC_2) < 1)
				{
					qs.giveItems(WHITE_FABRIC_2, 1);
				}
				
				htmltext = "30864-07a.htm";
			}
			else if (((cond == 25) && (qs.getQuestItemsCount(BLOODED_FABRIC) > 0)) || (cond == 28))
			{
				if (cond != 28)
				{
					qs.setCond(28);
				}
				
				htmltext = "30864-09.htm";
			}
			else if (cond == 29)
			{
				htmltext = "30864-10.htm";
			}
		}
		// Other NPCs follow:
		else if (cond == 5)
		{
			if (ARK_OWNERS.containsKey(npcId))
			{
				// first meeting...have the letter
				if (qs.getQuestItemsCount(ARK_OWNERS.get(npcId)[0]) == 1)
				{
					qs.takeItems(ARK_OWNERS.get(npcId)[0], 1);
					htmltext = ARK_OWNERS_TEXT.get(npcId)[0];
					qs.getPlayer().sendPacket(new RadarControl(0, 1, ARK_OWNERS.get(npcId)[2], ARK_OWNERS.get(npcId)[3], ARK_OWNERS.get(npcId)[4]));
				}
				// do not have letter and do not have the item
				else if (qs.getQuestItemsCount(ARK_OWNERS.get(npcId)[1]) < 1)
				{
					htmltext = ARK_OWNERS_TEXT.get(npcId)[1];
					qs.getPlayer().sendPacket(new RadarControl(0, 1, ARK_OWNERS.get(npcId)[2], ARK_OWNERS.get(npcId)[3], ARK_OWNERS.get(npcId)[4]));
				}
				else
				// have the item (done)
				{
					htmltext = ARK_OWNERS_TEXT.get(npcId)[2];
				}
			}
			else if (ARKS.containsKey(npcId))
			{
				// if you do not have the key (first meeting)
				if (qs.getQuestItemsCount(ARKS.get(npcId)[0]) == 0)
				{
					if (ARKS.get(npcId)[1] != 0)
					{
						qs.addSpawn(ARKS.get(npcId)[1], 120000);
					}
					
					return ARKS_TEXT.get(npcId)[0];
				}
				// if the player already has openned the chest and has its content, show "chest empty"
				else if (qs.getQuestItemsCount(ARKS.get(npcId)[2]) == 1)
				{
					htmltext = ARKS_TEXT.get(npcId)[2];
				}
				else
				// the player has the key and doesn't have the contents, give the contents
				{
					htmltext = ARKS_TEXT.get(npcId)[1];
					qs.takeItems(ARKS.get(npcId)[0], 1);
					qs.giveItems(ARKS.get(npcId)[2], 1);
				}
			}
			else if (npcId == ARK_GUARDIANS_CORPSE)
			// if you do not have the key (first meeting)
			{
				if ((qs.getQuestItemsCount(FIRST_KEY_OF_ARK) == 0) && (qs.getInt("angelKillerIsDefeated") == 0))
				{
					qs.addSpawn(ANGEL_KILLER, 120000);
					htmltext = "30980-01.htm";
				}
				else if ((qs.getQuestItemsCount(FIRST_KEY_OF_ARK) == 0) && (qs.getInt("angelKillerIsDefeated") == 1))
				{
					qs.giveItems(FIRST_KEY_OF_ARK, 1);
					htmltext = "30980-02.htm";
				}
				else
				{
					htmltext = "30980-03.htm";
				}
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		Integer[] drop = DROPS.get(npcId);
		
		if (drop != null)
		{
			int cond = drop[0];
			int item = drop[1];
			int max = drop[2];
			int chance = drop[3];
			int take = drop[4];
			
			if ((qs.getCond() >= cond) && (qs.getQuestItemsCount(item) < max) && ((take == 0) || (qs.getQuestItemsCount(take) > 0)) && Rnd.chance(chance))
			{
				qs.giveItems(item, 1);
				qs.playSound(SOUND_ITEMGET);
				
				if (take != 0)
				{
					qs.takeItems(take, 1);
				}
				
				if ((BLOODED_FABRIC == item) && (qs.getQuestItemsCount(BLOODED_FABRIC) >= 30))
				{
				}
			}
		}
		
		if (npcId == ANGEL_KILLER)
		{
			return "Ha, that was fun! If you wish to find the key, search the corpse.";
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
