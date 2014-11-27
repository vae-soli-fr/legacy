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

/**
 * @author GodWorld & Bonux
 */
public class Q10748_MysteriousSuggestionPart1 extends Quest implements ScriptFile
{
	// Npc
	private static final int MUSTERIOUS_BUTLER = 33685;
	// Items
	private static final int TOURNAMENT_REMNANTS_I = 35544;
	private static final int MYSTERIOUS_MARK = 34900;
	
	public Q10748_MysteriousSuggestionPart1()
	{
		super(false);
		addStartNpc(MUSTERIOUS_BUTLER);
		addTalkId(MUSTERIOUS_BUTLER);
		addQuestItem(TOURNAMENT_REMNANTS_I);
		addLevelCheck(76, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("grankain_lumiere_q10748_03.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (qs.isStarted())
		{
			if (cond == 1)
			{
				htmltext = "grankain_lumiere_q10748_06.htm";
			}
			else if (cond == 2)
			{
				qs.giveItems(MYSTERIOUS_MARK, 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				htmltext = "grankain_lumiere_q10748_07.htm";
			}
		}
		else
		{
			if (isAvailableFor(qs.getPlayer()) && ((qs.getPlayer().getClan() != null) || (qs.getPlayer().getClan().getLevel() > 3)))
			{
				if (qs.isNowAvailable())
				{
					htmltext = "grankain_lumiere_q10748_01.htm";
				}
				else
				{
					htmltext = "grankain_lumiere_q10748_05.htm";
				}
			}
			else
			{
				htmltext = "grankain_lumiere_q10748_04.htm";
			}
		}
		
		return htmltext;
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