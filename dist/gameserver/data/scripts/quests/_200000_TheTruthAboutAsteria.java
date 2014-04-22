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

import lineage2.gameserver.network.serverpackets.Earthquake;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _200000_TheTruthAboutAsteria extends Quest implements ScriptFile
{
	private static final int NPC1 = 200001;
	private static final int NPC2 = 200002;
	private static final int NPC3 = 200003;
	private static final int NPC4 = 200004;
	private static final int NPC5 = 200005;
	private static final int NPC6 = 200006;
	private static final int NPC7 = 200007;
	private static final int NPC8 = 200008;
	private static final int NPC9 = 200009;
	private static final int NPC10 = 200010;
	private static final int NPC11 = 200011;
	private static final int NPC12 = 200012;
	private static final int NPC13 = 200013;
	private static final int NPC14 = 200014;
	private static final int NPC15 = 200015;
	private static final int NPC16 = 200016;
	private static final int NPC17 = 200017;
	private static final int NPC18 = 200018;
	private static final int NPC19 = 200019;
	private static final int NPC20 = 200020;
	private static final int NPC21 = 200021;
	private static final int NPC22 = 200022;
	private static final int NPC23 = 200023;
	

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
	
	public _200000_TheTruthAboutAsteria()
	{
		super(false);
		addStartNpc(NPC1);
		addTalkId(NPC2);
		addTalkId(NPC3);
		addTalkId(NPC4);
		addTalkId(NPC5);
		addTalkId(NPC6);
		addTalkId(NPC7);
		addTalkId(NPC8);
		addTalkId(NPC9);
		addTalkId(NPC10);
		addTalkId(NPC11);
		addTalkId(NPC12);
		addTalkId(NPC13);
		addTalkId(NPC14);
		addTalkId(NPC15);
		addTalkId(NPC16);
		addTalkId(NPC17);
		addTalkId(NPC18);
		addTalkId(NPC19);
		addTalkId(NPC20);
		addTalkId(NPC21);
		addTalkId(NPC22);
		addTalkId(NPC23);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		switch (event.toLowerCase()) {
		case NPC1 + ".htm":
			st.setCond(1, false);
			st.playSound(SOUND_ACCEPT);
			break;
		case "finished.htm":
			st.playSound(SOUND_FINISH);
			st.getPlayer().sendPacket(new Earthquake(st.getPlayer().getLoc(), 20, 10));
			break;
		case "teleport.htm":
			st.exitCurrentQuest(true);
			st.getPlayer().setTransformation(0);
			st.getPlayer().teleToLocation(-113640, 246008, -3696);
			break;
		case NPC2 + ".htm":
			st.setCond(2, false);
			break;
		case NPC3 + ".htm":
			st.setCond(3, false);
			break;
		case NPC4 + ".htm":
			//st.setCond(4, false);
			st.setCond(23, false); // BETATEST
			break;
		case NPC5 + ".htm":
			st.setCond(5, false);
			break;
		case NPC6 + ".htm":
			st.setCond(6, false);
			break;
		case NPC7 + ".htm":
			st.setCond(7, false);
			break;
		case NPC8 + ".htm":
			st.setCond(8, false);
			break;
		case NPC9 + ".htm":
			st.setCond(9, false);
			break;
		case NPC10 + ".htm":
			st.setCond(10, false);
			break;
		case NPC11 + ".htm":
			st.setCond(11, false);
			break;
		case NPC12 + ".htm":
			st.setCond(12, false);
			break;
		case NPC13 + ".htm":
			st.setCond(13, false);
			break;
		case NPC14 + ".htm":
			st.setCond(14, false);
			break;
		case NPC15 + ".htm":
			st.setCond(15, false);
			break;
		case NPC16 + ".htm":
			st.setCond(16, false);
			break;
		case NPC17 + ".htm":
			st.setCond(17, false);
			break;
		case NPC18 + ".htm":
			st.setCond(18, false);
			break;
		case NPC19 + ".htm":
			st.setCond(19, false);
			break;
		case NPC20 + ".htm":
			st.setCond(20, false);
			break;
		case NPC21 + ".htm":
			st.setCond(21, false);
			break;
		case NPC22 + ".htm":
			st.setCond(22, false);
			break;
		case NPC23 + ".htm":
			st.setCond(23, false);
			break;
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st) {
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int cond = st.getCond();
		switch (npcId) {
		case NPC1:
			if (cond > 1) {
				if (cond != 23) {
					htmltext = "notfinished.htm";
				} else {
					htmltext = "finished.htm";
				}
			} else {
				htmltext = NPC1 + ".htm";
			}
			break;
		case NPC2:
			htmltext = (cond != 1) ? "npcbusy.htm" : NPC2 + ".htm";
			break;
		case NPC3:
			htmltext = (cond != 2) ? "npcbusy.htm" : NPC3 + ".htm";
			break;
		case NPC4:
			htmltext = (cond != 3) ? "npcbusy.htm" : NPC4 + ".htm";
			break;
		case NPC5:
			htmltext = (cond != 4) ? "npcbusy.htm" : NPC5 + ".htm";
			break;
		case NPC6:
			htmltext = (cond != 5) ? "npcbusy.htm" : NPC6 + ".htm";
			break;
		case NPC7:
			htmltext = (cond != 6) ? "npcbusy.htm" : NPC7 + ".htm";
			break;
		case NPC8:
			htmltext = (cond != 7) ? "npcbusy.htm" : NPC8 + ".htm";
			break;
		case NPC9:
			htmltext = (cond != 8) ? "npcbusy.htm" : NPC9 + ".htm";
			break;
		case NPC10:
			htmltext = (cond != 9) ? "npcbusy.htm" : NPC10 + ".htm";
			break;
		case NPC11:
			htmltext = (cond != 10) ? "npcbusy.htm" : NPC11 + ".htm";
			break;
		case NPC12:
			htmltext = (cond != 11) ? "npcbusy.htm" : NPC12 + ".htm";
			break;
		case NPC13:
			htmltext = (cond != 12) ? "npcbusy.htm" : NPC13 + ".htm";
			break;
		case NPC14:
			htmltext = (cond != 13) ? "npcbusy.htm" : NPC14 + ".htm";
			break;
		case NPC15:
			htmltext = (cond != 14) ? "npcbusy.htm" : NPC15 + ".htm";
			break;
		case NPC16:
			htmltext = (cond != 15) ? "npcbusy.htm" : NPC16 + ".htm";
			break;
		case NPC17:
			htmltext = (cond != 16) ? "npcbusy.htm" : NPC17 + ".htm";
			break;
		case NPC18:
			htmltext = (cond != 17) ? "npcbusy.htm" : NPC18 + ".htm";
			break;
		case NPC19:
			htmltext = (cond != 18) ? "npcbusy.htm" : NPC19 + ".htm";
			break;
		case NPC20:
			htmltext = (cond != 19) ? "npcbusy.htm" : NPC20 + ".htm";
			break;
		case NPC21:
			htmltext = (cond != 20) ? "npcbusy.htm" : NPC21 + ".htm";
			break;
		case NPC22:
			htmltext = (cond != 21) ? "npcbusy.htm" : NPC22 + ".htm";
			break;
		case NPC23:
			htmltext = (cond != 22) ? "npcbusy.htm" : NPC23 + ".htm";
			break;
		}
		return htmltext;
	}
	
}
