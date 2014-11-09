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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10271_TheEnvelopingDarkness extends Quest implements ScriptFile
{
	private static final int Orbyu = 32560;
	private static final int El = 32556;
	private static final int MedibalsCorpse = 32528;
	private static final int InspectorMedibalsDocument = 13852;
	private static final int CC_MINIMUM = 36;
	
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
	
	public Q10271_TheEnvelopingDarkness()
	{
		super(false);
		addStartNpc(Orbyu);
		addTalkId(Orbyu);
		addTalkId(El);
		addTalkId(MedibalsCorpse);
		addQuestItem(InspectorMedibalsDocument);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int cond = st.getCond();
		String htmltext = event;
		
		if (event.equalsIgnoreCase("orbyu_q10271_3.htm") && (cond == 0))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("el_q10271_2.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("medibalscorpse_q10271_2.htm"))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
			st.giveItems(InspectorMedibalsDocument, 1);
		}
		else if (event.equalsIgnoreCase("el_q10271_4.htm"))
		{
			st.setCond(4);
			st.playSound(SOUND_MIDDLE);
			st.takeItems(InspectorMedibalsDocument, -1);
		}
		else if (event.equalsIgnoreCase("orbyu_q10271_5.htm"))
		{
			st.giveItems(ADENA_ID, 236510);
			st.addExpAndSp(1109665, 1229015);
			st.setState(COMPLETED);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getId();
		int cond = st.getCond();
		Player player = st.getPlayer();
		QuestState ToTheSeedOfDestruction = player.getQuestState(Q10269_ToTheSeedOfDestruction.class);
		
		if (npcId == Orbyu)
		{
			if (cond == 0)
			{
				if ((player.getLevel() >= 75) && (ToTheSeedOfDestruction != null) && ToTheSeedOfDestruction.isCompleted() && (player.getParty() != null) && (player.getParty().getCommandChannel() != null) && (player.getParty().getCommandChannel().getMemberCount() >= CC_MINIMUM))
				{
					htmltext = "orbyu_q10271_1.htm";
				}
				else
				{
					htmltext = "orbyu_q10271_0.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 4)
			{
				htmltext = "orbyu_q10271_4.htm";
			}
		}
		else if (npcId == El)
		{
			if (cond == 1)
			{
				htmltext = "el_q10271_1.htm";
			}
			else if ((cond == 3) && (st.getQuestItemsCount(InspectorMedibalsDocument) >= 1))
			{
				htmltext = "el_q10271_3.htm";
			}
			else if ((cond == 3) && (st.getQuestItemsCount(InspectorMedibalsDocument) < 1))
			{
				htmltext = "el_q10271_0.htm";
			}
		}
		else if (npcId == MedibalsCorpse)
		{
			if (cond == 2)
			{
				htmltext = "medibalscorpse_q10271_1.htm";
			}
		}
		
		return htmltext;
	}
}
