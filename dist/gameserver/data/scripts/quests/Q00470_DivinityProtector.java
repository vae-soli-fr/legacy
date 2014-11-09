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
import lineage2.gameserver.utils.Util;

public class Q00470_DivinityProtector extends Quest implements ScriptFile
{
	// npc
	public static final int GUIDE = 33463;
	public static final int APRIGEL = 31348;
	// mobs
	private final int[] Mobs =
	{
		21520,
		21521,
		21522,
		21523,
		21524,
		21525,
		21526,
		21542,
		21543,
		21527,
		21528,
		21529,
		21541,
		21530,
		21531,
		21532,
		21533,
		21534,
		21535,
		21536,
		21545,
		21546,
		21537,
		21538,
		21539,
		21540,
		21544
	};
	// q items
	public static final int COLORLESS_SOUL = 19489;
	
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
	
	public Q00470_DivinityProtector()
	{
		super(true);
		addStartNpc(GUIDE);
		addTalkId(APRIGEL);
		addKillId(Mobs);
		addQuestItem(COLORLESS_SOUL);
		addLevelCheck(60, 64);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
		
		if (event.equalsIgnoreCase("33463-3.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		st.getPlayer();
		int npcId = npc.getId();
		int state = st.getState();
		int cond = st.getCond();
		
		if (npcId == GUIDE)
		{
			if (state == 1)
			{
				if (!st.isNowAvailableByTime())
				{
					return "33463-comp.htm";
				}
				
				return "33463.htm";
			}
			
			if (state == 2)
			{
				if (cond == 1)
				{
					return "33463-4.htm";
				}
			}
		}
		
		if ((npcId == APRIGEL) && (state == 2))
		{
			if (cond == 1)
			{
				return "31348-1.htm";
			}
			
			if (cond == 2)
			{
				st.giveItems(57, 194000);
				st.addExpAndSp(1879400, 1782000);
				st.takeItems(COLORLESS_SOUL, -1);
				st.unset("cond");
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(this);
				return "31348.htm"; // no further html do here
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		
		if ((cond != 1) || (npc == null))
		{
			return null;
		}
		
		if (Util.contains(Mobs, npc.getId()) && Rnd.chance(50))
		{
			st.giveItems(COLORLESS_SOUL, 1);
			st.playSound(SOUND_MIDDLE);
		}
		
		if (st.getQuestItemsCount(COLORLESS_SOUL) >= 20)
		{
			st.setCond(2);
		}
		
		return null;
	}
}