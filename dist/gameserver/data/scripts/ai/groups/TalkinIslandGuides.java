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
package ai.groups;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExRotation;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/**
 * This AI is used in GoD starting village quests for NPCs that are spawned when player takes quest. These NPCs shows path to next NPC by quest. Because of these NPCs not so much, we can implement their movement statically
 */
public final class TalkinIslandGuides extends DefaultAI
{
	private final static int SEARCHING_MYST_POWER_SOLDIER = 33016; // By quest "Searching for the Mysterious Power
	private final static int GOING_INTO_REAL_WAR_SOLDIER = 33014; // By quest "Going into the Real War"
	private final static int BACKUP_SEEKERS_ASSASSIN = 33204; // By quest "Backup Seekers"
	
	private final static NpcString SEARCHING_MYST_POWER_STRING = NpcString.S1_COME_FOLLOW_ME;
	private final static NpcString GOING_INTO_REAL_WAR_STRING = NpcString.S1_COME_FOLLOW_ME;
	private final static NpcString BACKUP_SEEKERS_STRING = NpcString.HEY_KID_HURRY_UP_AND_FOLLOW_ME;
	
	private static final int SAY_INTERVAL = 6000;
	
	// @formatter:off
	// "Searching for the Mysterious Power" soldier coords
	private final static int[][] SMP_COORDS =
	{
			{-111979, 255327, -1432},
			{-112442, 254703, -1528},
			{-112188, 254235, -1536},
			{-111471, 253984, -1712},
			{-110689, 253733, -1792}
	};
	// In "Going into the Real War" quest guides can move by left or by right side
	// Side is choosen by distance between player location and first coord in set
	private final static int[][] GRW_COORDS_LEFT =
	{
			{-110885, 253533, -1776},
			{-111050, 253183, -1776},
			{-111007, 252706, -1832},
			{-110957, 252400, -1928},
			{-110643, 252365, -1976}
	};
	private final static int[][] GRW_COORDS_RIGHT =
	{
			{-110618, 253655, -1792},
			{-110296, 253160, -1848},
			{-110271, 253163, -1816},
			{-110156, 252874, -1888},
			{-110206, 252422, -1984}
	};
	
	// "Backup Seekers" assassin coords
	private final static int[][] BS_COORDS =
	{
			{-117996, 255845, -1320},
			{-117103, 255538, -1296},
			{-115719, 254792, -1504},
			{-114695, 254741, -1528},
			{-114589, 253517, -1528}
	};
	// @formatter:on
	
	private int currentState; // This npc state identifies what coords to use
	private long lastSayTime = 0;
	
	public TalkinIslandGuides(NpcInstance actor)
	{
		super(actor);
		currentState = 0;
		lastSayTime = 0;
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		Creature target = actor.getFollowTarget();
		
		if ((target == null) || !(target instanceof Player))
		{
			actor.deleteMe();
			return false;
		}
		
		final int npcId = actor.getId();
		int[][] coords;
		NpcString string;
		NpcString end_String;
		// Select coords
		switch (npcId)
		{
			case SEARCHING_MYST_POWER_SOLDIER:
				coords = SMP_COORDS;
				string = SEARCHING_MYST_POWER_STRING;
				end_String = NpcString.S1_THAT_MAN_IN_FRONT_IS_IBANE;
				break;
			
			case BACKUP_SEEKERS_ASSASSIN:
				coords = BS_COORDS;
				string = BACKUP_SEEKERS_STRING;
				end_String = NpcString.TALK_TO_THAT_APPRENTICE_AND_GET_ON_KOOKARU;
				break;
			
			case GOING_INTO_REAL_WAR_SOLDIER:
				final double distLeft = target.getDistance(GRW_COORDS_LEFT[0][0], GRW_COORDS_LEFT[0][1], GRW_COORDS_LEFT[0][2]);
				final double distRight = target.getDistance(GRW_COORDS_RIGHT[0][0], GRW_COORDS_RIGHT[0][1], GRW_COORDS_RIGHT[0][2]);
				
				if (distLeft <= distRight)
				{
					coords = GRW_COORDS_LEFT;
				}
				else
				{
					coords = GRW_COORDS_RIGHT;
				}
				
				string = GOING_INTO_REAL_WAR_STRING;
				end_String = NpcString.S1_THAT_MAN_IN_FRONT_IS_HOLDEN;
				break;
			
			default:
				return false;
		}
		
		// This NPC is running
		actor.setRunning();
		
		if ((actor.getDistance(target) < 100) || (currentState == 0) || (currentState >= coords.length))
		{
			if (currentState < coords.length)
			{
				actor.moveToLocation(coords[currentState][0], coords[currentState][1], coords[currentState][2], Rnd.get(0, 50), true);
				
				if (actor.getDestination() == null)
				{
					++currentState;
				}
			}
			else
			{
				Functions.npcSay(actor, end_String, ChatType.NPC_SAY, 800, target.getName());
				actor.deleteMe();
			}
		}
		else if (((lastSayTime + SAY_INTERVAL) < System.currentTimeMillis()) && (actor.getDestination() == null))
		{
			final int heading = actor.calcHeading(target.getX(), target.getY());
			actor.setHeading(heading);
			actor.broadcastPacket(new ExRotation(actor.getObjectId(), heading));
			lastSayTime = System.currentTimeMillis();
			Functions.npcSay(actor, string, ChatType.NPC_SAY, 800, target.getName());
		}
		
		return true;
	}
	
	/**
	 * These NPC does not have random walk
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}