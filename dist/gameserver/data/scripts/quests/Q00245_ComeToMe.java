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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

/*
 * @author JustForFun
 * @date 14/04/13
 */
public class Q00245_ComeToMe extends Quest implements ScriptFile
{
	private static final int NPC_FERRIS = 30847;
	private static final int ITEM_FLAME_ASHES = 30322;
	private static final int ITEM_CRYSTAL_OF_EXPERIENCE = 30323;
	private static final int ITEM_CRYSTAL_A = 1461;
	private static final int REWARD_RING = 30383;
	private static final int[] monstersSwamp1 = new int[]
	{
		21110,
		21111
	};
	private static final int[] monstersSwamp2 = new int[]
	{
		21112,
		21113,
		21115,
		21116
	};
	private Player sponsoredPlayer;
	private Player sponsorPlayer;
	private int sponsorObjId;
	
	public Q00245_ComeToMe()
	{
		super(PARTY_NONE);
		
		addStartNpc(NPC_FERRIS);
		addKillId(monstersSwamp1);
		addKillId(monstersSwamp2);
		addFirstTalkId(NPC_FERRIS);
		addQuestItem(ITEM_CRYSTAL_OF_EXPERIENCE, ITEM_FLAME_ASHES);
		addLevelCheck(70, 75);
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
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();
		
		if ((player.getObjectId() == sponsorObjId) && (event.equals("30847-13.htm")))
		{
			if ((sponsoredPlayer != null) && sponsoredPlayer.isOnline())
			{
				if (sponsoredPlayer.getInventory().destroyItemByItemId(ITEM_CRYSTAL_A, 100))
				{
					sponsorPlayer.getQuestState(getName()).setCond(3);
					return event;
				}
				
				return "30847-14.htm";
			}
			
			return "30847-12.htm";
		}
		
		if (event.equals("30847-04.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("30847-07.htm"))
		{
			st.set("talk", "1");
			st.takeAllItems(ITEM_FLAME_ASHES);
			st.playSound("ItemSound.quest_middle");
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		String htmltext = "noquest";
		
		if (npc.getId() == NPC_FERRIS)
		{
			switch (st.getState())
			{
				case COMPLETED:
					htmltext = "30847-03.htm";
					break;
				case CREATED:
					if ((player.getLevel() > 70) && (player.getLevel() < 75) && (player.getPledgeType() == Clan.SUBUNIT_ACADEMY) && (player.getSponsor() != 0))
					{
						htmltext = "30847-01.htm";
					}
					else
					{
						htmltext = "30847-02.htm";
						
						st.exitCurrentQuest(true);
					}
					
					break;
				case STARTED:
					switch (st.getCond())
					{
						case 1:
							htmltext = "30847-05.htm";
							break;
						case 2:
							if (st.getInt("talk") == 1)
							{
								htmltext = "30847-06.htm";
							}
							else if ((player.getPledgeType() == Clan.SUBUNIT_ACADEMY) && (player.getSponsor() != 0))
							{
								sponsorObjId = player.getSponsor();
								sponsoredPlayer = st.getPlayer();
								
								for (UnitMember unitMember : player.getClan().getAllMembers())
								{
									if (unitMember.getObjectId() == sponsorObjId)
									{
										sponsorPlayer = unitMember.getPlayer();
									}
								}
								
								if ((sponsorPlayer != null) && (sponsorPlayer.isOnline()) && (player.getDistance(sponsorPlayer) < 200))
								{
									htmltext = "30847-10.htm";
								}
								else
								{
									htmltext = "30847-08.htm";
								}
							}
							else
							{
								htmltext = "30847-09.htm";
							}
							
							break;
						case 3:
							htmltext = "30847-17.htm";
							
							st.setCond(4);
							st.playSound("ItemSound.quest_middle");
							break;
						case 4:
							htmltext = "30847-18.htm";
							break;
						case 5:
							htmltext = "30847-19.htm";
							
							st.takeAllItems(ITEM_CRYSTAL_OF_EXPERIENCE);
							st.addExpAndSp(2018733, 200158);
							st.giveItems(REWARD_RING, 1);
							
							if (player.getPledgeType() == Clan.SUBUNIT_ACADEMY)
							{
								player.getClan().incReputation(1000, true, "quest245");
							}
							
							st.playSound("ItemSound.quest_finish");
							st.exitCurrentQuest(false);
					}
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		if ((player.getObjectId() == sponsorObjId) && (npc.getId() == NPC_FERRIS))
		{
			if ((sponsoredPlayer != null) && sponsoredPlayer.isOnline())
			{
				return "30847-11.htm";
			}
		}
		
		return "";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getState() != STARTED)
		{
			return null;
		}
		
		Player player = st.getPlayer();
		
		if (st.getCond() == 1)
		{
			if ((Util.contains(monstersSwamp1, npc.getId())) && (Rnd.chance(50)))
			{
				st.giveItems(ITEM_FLAME_ASHES, 1, true);
				st.playSound("ItemSound.quest_itemget");
				
				if (st.getQuestItemsCount(ITEM_FLAME_ASHES) >= 15)
				{
					st.setCond(2);
					st.playSound("ItemSound.quest_middle");
				}
			}
		}
		else if (st.getCond() == 4)
		{
			if (Util.contains(monstersSwamp2, npc.getId()))
			{
				if ((player.getPledgeType() == Clan.SUBUNIT_ACADEMY) && (player.getSponsor() != 0))
				{
					if (player.getDistance(sponsorPlayer) <= 400)
					{
						st.giveItems(ITEM_CRYSTAL_OF_EXPERIENCE, 1, true);
						st.playSound("ItemSound.quest_itemget");
						
						if (st.getQuestItemsCount(ITEM_CRYSTAL_OF_EXPERIENCE) >= 12)
						{
							st.setCond(5);
							st.playSound("ItemSound.quest_middle");
						}
					}
				}
			}
		}
		
		return null;
	}
}