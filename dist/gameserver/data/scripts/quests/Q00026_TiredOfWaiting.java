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

public class Q00026_TiredOfWaiting extends Quest implements ScriptFile
{
	// Npcs
	private final static int ISAEL = 30655;
	private final static int KITZKA = 31045;
	// Items
	private final static int LARGE_DRAGON_BONE = 17248;
	private final static int WILL_OF_ANTHARAS = 17266;
	private final static int SEALED_BLOOD_CRYSTAL = 17267;
	
	public Q00026_TiredOfWaiting()
	{
		super(false);
		addStartNpc(ISAEL);
		addTalkId(KITZKA);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_accept":
				htmltext = "isael_q0026_05.htm";
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "LARGE_DRAGON_BONE":
				htmltext = "kitzka_q0026_03.htm";
				qs.giveItems(LARGE_DRAGON_BONE, 1, false);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
			
			case "WILL_OF_ANTHARAS":
				htmltext = "kitzka_q0026_04.htm";
				qs.giveItems(WILL_OF_ANTHARAS, 1, false);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
			
			case "SEALED_BLOOD_CRYSTAL":
				htmltext = "kitzka_q0026_05.htm";
				qs.giveItems(SEALED_BLOOD_CRYSTAL, 1, false);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
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
		
		switch (npcId)
		{
			case ISAEL:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() >= 80)
					{
						htmltext = "isael_q0026_02.htm";
					}
					else
					{
						htmltext = "isael_q0026_01.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond == 1)
				{
					htmltext = "isael_q0026_03.htm";
				}
				break;
			
			case KITZKA:
				if (cond == 1)
				{
					htmltext = "kitzka_q0026_01.htm";
					qs.playSound(SOUND_MIDDLE);
				}
				break;
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
