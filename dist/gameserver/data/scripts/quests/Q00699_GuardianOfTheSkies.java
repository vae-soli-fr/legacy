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

public class Q00699_GuardianOfTheSkies extends Quest implements ScriptFile
{
	private static final int Lekon = 32557;
	private static final int VulturesGoldenFeather = 13871;
	private static final int VultureRider1 = 22614;
	private static final int VultureRider2 = 22615;
	private static final int EliteRider = 25633;
	private static final int Valdstone = 25623;
	private static final int _featherprice = 5000;
	
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
	
	public Q00699_GuardianOfTheSkies()
	{
		super(false);
		addStartNpc(Lekon);
		addTalkId(Lekon);
		addKillId(VultureRider1, VultureRider2, EliteRider, Valdstone);
		addQuestItem(VulturesGoldenFeather);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int cond = st.getCond();
		String htmltext = event;
		
		if (event.equals("lekon_q699_2.htm") && (cond == 0))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("ex_feathers") && (cond == 1))
		{
			if (st.getQuestItemsCount(VulturesGoldenFeather) >= 1)
			{
				st.giveItems(ADENA_ID, st.getQuestItemsCount(VulturesGoldenFeather) * _featherprice);
				st.takeItems(VulturesGoldenFeather, -1);
				htmltext = "lekon_q699_4.htm";
			}
			else
			{
				htmltext = "lekon_q699_3a.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		QuestState GoodDayToFly = st.getPlayer().getQuestState(Q10273_GoodDayToFly.class);
		
		if (npcId == Lekon)
		{
			if (cond == 0)
			{
				if ((st.getPlayer().getLevel() >= 75) && (GoodDayToFly != null) && GoodDayToFly.isCompleted())
				{
					htmltext = "lekon_q699_1.htm";
				}
				else
				{
					htmltext = "lekon_q699_0.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				if (st.getQuestItemsCount(VulturesGoldenFeather) >= 1)
				{
					htmltext = "lekon_q699_3.htm";
				}
				else
				{
					htmltext = "lekon_q699_3a.htm";
				}
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		
		if (cond == 1)
		{
			if ((npcId == VultureRider1) || (npcId == VultureRider2) || (npcId == EliteRider))
			{
				st.giveItems(VulturesGoldenFeather, 1);
				st.playSound(SOUND_ITEMGET);
			}
			else if (npcId == Valdstone)
			{
				st.giveItems(VulturesGoldenFeather, 50);
				st.playSound(SOUND_ITEMGET);
			}
		}
		
		return null;
	}
}
