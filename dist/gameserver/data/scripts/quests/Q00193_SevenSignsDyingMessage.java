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

import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00193_SevenSignsDyingMessage extends Quest implements ScriptFile
{
	private static final int Hollint = 30191;
	private static final int Cain = 32569;
	private static final int Eric = 32570;
	private static final int SirGustavAthebaldt = 30760;
	private static final int ShilensEvilThoughts = 27343;
	private static final int JacobsNecklace = 13814;
	private static final int DeadmansHerb = 13813;
	private static final int SculptureofDoubt = 14352;
	private static final Map<Integer, Integer> spawns = new HashMap<>();
	
	public Q00193_SevenSignsDyingMessage()
	{
		super(false);
		addStartNpc(Hollint);
		addTalkId(Cain, Eric, SirGustavAthebaldt);
		addKillId(ShilensEvilThoughts);
		addQuestItem(JacobsNecklace, DeadmansHerb, SculptureofDoubt);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();
		String htmltext = event;
		
		if (event.equalsIgnoreCase("30191-02.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			st.giveItems(JacobsNecklace, 1);
		}
		else if (event.equalsIgnoreCase("32569-05.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32570-02.htm"))
		{
			st.setCond(3);
			st.giveItems(DeadmansHerb, 1);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30760-02.htm"))
		{
			if (player.getBaseClassId() == player.getActiveClassId())
			{
				st.addExpAndSp(10000000, 2500000);
				st.setState(COMPLETED);
				st.exitCurrentQuest(false);
				st.playSound(SOUND_FINISH);
			}
			else
			{
				return "subclass_forbidden.htm";
			}
		}
		else if (event.equalsIgnoreCase("close_your_eyes"))
		{
			st.setCond(4);
			st.takeItems(DeadmansHerb, -1);
			st.playSound(SOUND_MIDDLE);
			player.showQuestMovie(ExStartScenePlayer.SCENE_SSQ_DYING_MASSAGE);
			return "";
		}
		else if (event.equalsIgnoreCase("32569-09.htm"))
		{
			htmltext = "32569-09.htm";
			Functions.npcSay(npc, st.getPlayer().getName() + "! That stranger must be defeated. Here is the ultimate help!");
			NpcInstance mob = st.addSpawn(ShilensEvilThoughts, 82425, 47232, -3216, 0, 0, 180000);
			spawns.put(player.getObjectId(), mob.getObjectId());
			mob.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, 100000);
		}
		else if (event.equalsIgnoreCase("32569-13.htm"))
		{
			st.setCond(6);
			st.takeItems(SculptureofDoubt, -1);
			st.playSound(SOUND_MIDDLE);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		int id = st.getState();
		Player player = st.getPlayer();
		
		if (npcId == Hollint)
		{
			if (id == CREATED)
			{
				if (player.getLevel() < 79)
				{
					st.exitCurrentQuest(true);
					return "30191-00.htm";
				}
				
				QuestState qs = player.getQuestState(Q00192_SevenSignsSeriesOfDoubt.class);
				
				if ((qs == null) || !qs.isCompleted())
				{
					st.exitCurrentQuest(true);
					return "noquest";
				}
				
				return "30191-01.htm";
			}
			else if (cond == 1)
			{
				return "30191-03.htm";
			}
		}
		else if (npcId == Cain)
		{
			if (cond == 1)
			{
				return "32569-01.htm";
			}
			else if (cond == 2)
			{
				return "32569-06.htm";
			}
			else if (cond == 3)
			{
				return "32569-07.htm";
			}
			else if (cond == 4)
			{
				Integer obj_id = spawns.get(player.getObjectId());
				NpcInstance mob = obj_id != null ? GameObjectsStorage.getNpc(obj_id) : null;
				
				if ((mob == null) || mob.isDead())
				{
					return "32569-08.htm";
				}
				
				return "32569-09.htm";
			}
			else if (cond == 5)
			{
				return "32569-10.htm";
			}
			else if (cond == 6)
			{
				return "32569-13.htm";
			}
		}
		else if (npcId == Eric)
		{
			if (cond == 2)
			{
				return "32570-01.htm";
			}
			else if (cond == 3)
			{
				return "32570-03.htm";
			}
		}
		else if (npcId == SirGustavAthebaldt)
		{
			if (cond == 6)
			{
				return "30760-01.htm";
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		Player player = st.getPlayer();
		
		if (player == null)
		{
			return null;
		}
		
		if ((npcId == ShilensEvilThoughts) && (cond == 4))
		{
			Integer obj_id = spawns.get(player.getObjectId());
			
			if ((obj_id != null) && (obj_id.intValue() == npc.getObjectId()))
			{
				spawns.remove(player.getObjectId());
				st.setCond(5);
				st.playSound(SOUND_ITEMGET);
				st.giveItems(SculptureofDoubt, 1);
			}
		}
		
		return null;
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
