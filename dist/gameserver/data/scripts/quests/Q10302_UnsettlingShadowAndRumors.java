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

public class Q10302_UnsettlingShadowAndRumors extends Quest implements ScriptFile
{
	// npc
	private static final int KANIBYS = 32898;
	private static final int ISHAEL = 32894;
	private static final int KES = 32901;
	private static final int KEY = 32903;
	private static final int KIK = 32902;
	
	// Reward
	private static final int PAPER = 34033;
	
	public Q10302_UnsettlingShadowAndRumors()
	{
		super(false);
		addStartNpc(KANIBYS);
		addTalkId(KANIBYS, ISHAEL, KES, KEY, KIK);
		addLevelCheck(90, 99);
		addQuestCompletedCheck(Q10301_ShadowOfTerrorBlackishRedFog.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
		
		switch (event)
		{
			case "32898-4.htm":
			{
				st.setCond(1);
				st.setState(STARTED);
				st.playSound(SOUND_ACCEPT);
				break;
			}
			case "32898-8.htm":
			{
				st.addExpAndSp(6728850, 755280);
				st.giveItems(57, 2177190);
				st.giveItems(PAPER, 1);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(false);
				break;
			}
			case "32894-1.htm":
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
				break;
			}
			case "32901-1.htm":
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(3);
				break;
			}
			case "32903-1.htm":
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(4);
				break;
			}
			case "32902-1.htm":
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(5);
				break;
			}
			case "32894-5.htm":
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(6);
				break;
			}
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		st.getPlayer();
		String htmlText = NO_QUEST_DIALOG;
		int npcId = npc.getId();
		int state = st.getState();
		int cond = st.getCond();
		
		if (state == COMPLETED)
		{
			return "32898-comp.htm";
		}
		
		if (st.getPlayer().getLevel() < 90)
		{
			return "32898-lvl.htm";
		}
		
		QuestState qs = st.getPlayer().getQuestState(Q10301_ShadowOfTerrorBlackishRedFog.class);
		
		if ((qs == null) || !qs.isCompleted())
		{
			return "32898-lvl.htm";
		}
		
		switch (npcId)
		{
			case KANIBYS:
			{
				if (cond == 0)
				{
					return "32898.htm";
				}
				else if ((cond >= 1) && (cond < 6))
				{
					return "32898-5.htm";
				}
				else if (cond == 6)
				{
					return "32898-6.htm";
				}
				break;
			}
			case ISHAEL:
			{
				if (cond == 1)
				{
					return "32894.htm";
				}
				else if ((cond >= 2) && (cond < 5))
				{
					return "32894-2.htm";
				}
				else if (cond == 5)
				{
					return "32894-3.htm";
				}
				else if (cond == 6)
				{
					return "32894-6.htm";
				}
				break;
			}
			case KES:
			{
				if (cond == 2)
				{
					return "32901.htm";
				}
				
				return "32901-2.htm";
			}
			case KEY:
			{
				if (cond == 3)
				{
					return "32903.htm";
				}
				
				return "32903-2.htm";
			}
			case KIK:
			{
				if (cond == 4)
				{
					return "32902.htm";
				}
				
				return "32902-2.htm";
			}
		}
		return htmlText;
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