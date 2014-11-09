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
import lineage2.gameserver.templates.item.ItemTemplate;

public class Q00037_MakeFormalWear extends Quest implements ScriptFile
{
	private static final int MYSTERIOUS_CLOTH = 7076;
	private static final int JEWEL_BOX = 7077;
	private static final int SEWING_KIT = 7078;
	private static final int DRESS_SHOES_BOX = 7113;
	private static final int SIGNET_RING = 7164;
	private static final int ICE_WINE = 7160;
	private static final int BOX_OF_COOKIES = 7159;
	
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
	
	public Q00037_MakeFormalWear()
	{
		super(false);
		addStartNpc(30842);
		addTalkId(30842);
		addTalkId(31520);
		addTalkId(31521);
		addTalkId(31627);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("30842-1.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("31520-1.htm"))
		{
			st.giveItems(SIGNET_RING, 1);
			st.setCond(2);
		}
		else if (event.equals("31521-1.htm"))
		{
			st.takeItems(SIGNET_RING, 1);
			st.giveItems(ICE_WINE, 1);
			st.setCond(3);
		}
		else if (event.equals("31627-1.htm"))
		{
			if (st.getQuestItemsCount(ICE_WINE) > 0)
			{
				st.takeItems(ICE_WINE, 1);
				st.setCond(4);
			}
			else
			{
				htmltext = "You don't have enough materials";
			}
		}
		else if (event.equals("31521-3.htm"))
		{
			st.giveItems(BOX_OF_COOKIES, 1);
			st.setCond(5);
		}
		else if (event.equals("31520-3.htm"))
		{
			st.takeItems(BOX_OF_COOKIES, 1);
			st.setCond(6);
		}
		else if (event.equals("31520-5.htm"))
		{
			st.takeItems(MYSTERIOUS_CLOTH, 1);
			st.takeItems(JEWEL_BOX, 1);
			st.takeItems(SEWING_KIT, 1);
			st.setCond(7);
		}
		else if (event.equals("31520-7.htm"))
		{
			if (st.getQuestItemsCount(DRESS_SHOES_BOX) > 0)
			{
				st.takeItems(DRESS_SHOES_BOX, 1);
				st.giveItems(ItemTemplate.ITEM_ID_FORMAL_WEAR, 1);
				st.unset("cond");
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
			else
			{
				htmltext = "You don't have enough materials";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getId();
		int cond = st.getCond();
		
		if (npcId == 30842)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 60)
				{
					htmltext = "30842-0.htm";
				}
				else
				{
					htmltext = "30842-2.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "30842-1.htm";
			}
		}
		else if (npcId == 31520)
		{
			if (cond == 1)
			{
				htmltext = "31520-0.htm";
			}
			else if (cond == 2)
			{
				htmltext = "31520-1.htm";
			}
			else if ((cond == 5) || (cond == 6))
			{
				if ((st.getQuestItemsCount(MYSTERIOUS_CLOTH) > 0) && (st.getQuestItemsCount(JEWEL_BOX) > 0) && (st.getQuestItemsCount(SEWING_KIT) > 0))
				{
					htmltext = "31520-4.htm";
				}
				else if (st.getQuestItemsCount(BOX_OF_COOKIES) > 0)
				{
					htmltext = "31520-2.htm";
				}
				else
				{
					htmltext = "31520-3.htm";
				}
			}
			else if (cond == 7)
			{
				if (st.getQuestItemsCount(DRESS_SHOES_BOX) > 0)
				{
					htmltext = "31520-6.htm";
				}
				else
				{
					htmltext = "31520-5.htm";
				}
			}
		}
		else if (npcId == 31521)
		{
			if (st.getQuestItemsCount(SIGNET_RING) > 0)
			{
				htmltext = "31521-0.htm";
			}
			else if (cond == 3)
			{
				htmltext = "31521-1.htm";
			}
			else if (cond == 4)
			{
				htmltext = "31521-2.htm";
			}
			else if (cond == 5)
			{
				htmltext = "31521-3.htm";
			}
		}
		else if (npcId == 31627)
		{
			htmltext = "31627-0.htm";
		}
		
		return htmltext;
	}
}
