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

public class Q00345_MethodToRaiseTheDead extends Quest implements ScriptFile
{
	
	private static final int VICTIMS_ARM_BONE = 4274;
	private static final int VICTIMS_THIGH_BONE = 4275;
	private static final int VICTIMS_SKULL = 4276;
	private static final int VICTIMS_RIB_BONE = 4277;
	private static final int VICTIMS_SPINE = 4278;
	private static final int USELESS_BONE_PIECES = 4280;
	private static final int POWDER_TO_SUMMON_DEAD_SOULS = 4281;
	private static final int BILL_OF_IASON_HEINE = 4310;
	private static final int CHANCE = 15;
	private static final int CHANCE2 = 50;
	
	public Q00345_MethodToRaiseTheDead()
	{
		super(false);
		
		addStartNpc(30970);
		
		addTalkId(30970);
		addTalkId(30970);
		addTalkId(30912);
		addTalkId(30973);
		
		addQuestItem(VICTIMS_ARM_BONE, VICTIMS_THIGH_BONE, VICTIMS_SKULL, VICTIMS_RIB_BONE, VICTIMS_SPINE, POWDER_TO_SUMMON_DEAD_SOULS);
		
		addKillId(20789);
		addKillId(20791);
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
		String htmltext = event;
		switch (event)
		{
			case "1":
				st.setCond(1);
				st.setState(STARTED);
				htmltext = "dorothy_the_locksmith_q0345_03.htm";
				st.playSound(SOUND_ACCEPT);
				break;
			case "2":
				st.setCond(2);
				htmltext = "dorothy_the_locksmith_q0345_07.htm";
				break;
			case "3":
				if (st.getQuestItemsCount(ADENA_ID) >= 1000)
				{
					st.takeItems(ADENA_ID, 1000);
					st.giveItems(POWDER_TO_SUMMON_DEAD_SOULS, 1);
					st.setCond(3);
					htmltext = "magister_xenovia_q0345_03.htm";
					st.playSound(SOUND_ITEMGET);
				}
				else
				{
					htmltext = "<html><head><body>You dont have enough adena!</body></html>";
				}
				break;
			case "4":
				htmltext = "medium_jar_q0345_07.htm";
				st.takeItems(POWDER_TO_SUMMON_DEAD_SOULS, -1);
				st.takeItems(VICTIMS_ARM_BONE, -1);
				st.takeItems(VICTIMS_THIGH_BONE, -1);
				st.takeItems(VICTIMS_SKULL, -1);
				st.takeItems(VICTIMS_RIB_BONE, -1);
				st.takeItems(VICTIMS_SPINE, -1);
				st.setCond(6);
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int id = st.getState();
		int level = st.getPlayer().getLevel();
		int cond = st.getCond();
		long amount = st.getQuestItemsCount(USELESS_BONE_PIECES);
		if (npcId == 30970)
		{
			if (id == CREATED)
			{
				if (level >= 35)
				{
					htmltext = "dorothy_the_locksmith_q0345_02.htm";
				}
				else
				{
					htmltext = "dorothy_the_locksmith_q0345_01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if ((cond == 1) && (st.getQuestItemsCount(VICTIMS_ARM_BONE) > 0) && (st.getQuestItemsCount(VICTIMS_THIGH_BONE) > 0) && (st.getQuestItemsCount(VICTIMS_SKULL) > 0) && (st.getQuestItemsCount(VICTIMS_RIB_BONE) > 0) && (st.getQuestItemsCount(VICTIMS_SPINE) > 0))
			{
				htmltext = "dorothy_the_locksmith_q0345_06.htm";
			}
			else if ((cond == 1) && ((st.getQuestItemsCount(VICTIMS_ARM_BONE) + st.getQuestItemsCount(VICTIMS_THIGH_BONE) + st.getQuestItemsCount(VICTIMS_SKULL) + st.getQuestItemsCount(VICTIMS_RIB_BONE) + st.getQuestItemsCount(VICTIMS_SPINE)) < 5))
			{
				htmltext = "dorothy_the_locksmith_q0345_05.htm";
			}
			else if (cond == 7)
			{
				htmltext = "dorothy_the_locksmith_q0345_14.htm";
				st.setCond(1);
				st.giveItems(ADENA_ID, amount * 238);
				st.giveItems(BILL_OF_IASON_HEINE, Rnd.get(7) + 1);
				st.takeItems(USELESS_BONE_PIECES, -1);
			}
		}
		if (npcId == 30912)
		{
			if (cond == 2)
			{
				htmltext = "magister_xenovia_q0345_01.htm";
				st.playSound(SOUND_MIDDLE);
			}
			else if (cond == 3)
			{
				htmltext = "<html><head><body>What did the urn say?</body></html>";
			}
			else if (cond == 6)
			{
				htmltext = "magister_xenovia_q0345_07.htm";
				st.setCond(7);
			}
		}
		if (npcId == 30973)
		{
			if (cond == 3)
			{
				htmltext = "medium_jar_q0345_01.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int random = Rnd.get(100);
		if (random <= CHANCE)
		{
			if (st.getQuestItemsCount(VICTIMS_ARM_BONE) == 0)
			{
				st.giveItems(VICTIMS_ARM_BONE, 1);
			}
			else if (st.getQuestItemsCount(VICTIMS_THIGH_BONE) == 0)
			{
				st.giveItems(VICTIMS_THIGH_BONE, 1);
			}
			else if (st.getQuestItemsCount(VICTIMS_SKULL) == 0)
			{
				st.giveItems(VICTIMS_SKULL, 1);
			}
			else if (st.getQuestItemsCount(VICTIMS_RIB_BONE) == 0)
			{
				st.giveItems(VICTIMS_RIB_BONE, 1);
			}
			else if (st.getQuestItemsCount(VICTIMS_SPINE) == 0)
			{
				st.giveItems(VICTIMS_SPINE, 1);
			}
		}
		if (random <= CHANCE2)
		{
			st.giveItems(USELESS_BONE_PIECES, Rnd.get(8) + 1);
		}
		return null;
	}
}