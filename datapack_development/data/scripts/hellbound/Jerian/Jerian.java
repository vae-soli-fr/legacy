/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

/*
# Update by U3Games 17-03-2011
# Special thanks to contributors users l2jserver
# Imported: L2jTW, thx!
 */
 
package hellbound.Jerian;

import com.l2jserver.gameserver.instancemanager.HellboundManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * @author pmq
 */

public class Jerian extends Quest
{
	public Jerian(int id, String name, String descr)
	{
		super(id, name, descr);
		addStartNpc(JERIAN);
		addFirstTalkId(JERIAN);
		addTalkId(JERIAN);
	}

	// NPC:
	private static final int JERIAN = 32302;

	static int [][] teleportCoord =
	{
			{ -22200, 277100, -15047 },
			{ -22200, 277100, -13382 },
			{ -22200, 277100, -11654 },
			{ -22200, 277100, -9926 },
			{ -22200, 277100, -8262 },
			{ -19050, 277100, -8262 },
			{ -19050, 277100, -9926 },
			{ -19050, 277100, -11654 },
			{ -19050, 277100, -13382 },
			{ -19050, 277100, -15047 }
	};

	private static void teleportPlayer(L2PcInstance player, int[] coords)
	{
		if (player.getParty() == null /*|| !player.isInsideRadius(player.getParty().getLeader(), 1000, true, true)*/)
			return;
		player.teleToLocation(coords[0], coords[1], coords[2], true);
		final L2Summon pet = player.getPet();
		if (pet != null)
			pet.teleToLocation(coords[0], coords[1], coords[2], true);
	}

	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
			st = newQuestState(player);

		int npcId = npc.getNpcId();
		if(npcId == JERIAN && event.equalsIgnoreCase("Enter"))
		{
			if(player.getParty() != null)
			{
				for(L2PcInstance pc : player.getParty().getPartyMembers())
					if(pc.getFirstEffect(2357) == null)
						return "32302-1.htm";
				for(L2PcInstance pc : player.getParty().getPartyMembers())
					teleportPlayer(pc, teleportCoord[0]);
			}
			else return "32302-2.htm";
		}
		return "";
	}

	public String onTalk(L2Npc npc, L2PcInstance talker)
	{
		int npcId = npc.getNpcId();
		return npcId + ".htm";
	}

	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
			st = newQuestState(player);

		int hellboundLevel = HellboundManager.getInstance().getLevel();
		if (hellboundLevel <= 10)
			return "32302-3.htm";
		
		return "32302.htm";
	}

	public static void main(String[] args)
	{
		new Jerian(-1, Jerian.class.getSimpleName(), "hellbound");
	}
}