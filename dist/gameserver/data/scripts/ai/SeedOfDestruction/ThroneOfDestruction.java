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
package ai.SeedOfDestruction;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ThroneOfDestruction extends DefaultAI
{
	private static final int DOOR = 12240031;
	private static final int TIAT_NPC_ID = 29163;
	private static final Location TIAT_LOC = new Location(-250403, 207273, -11952, 16384);
	private static final int[] checkNpcs =
	{
		18778,
		18777
	};
	
	/**
	 * Constructor for ThroneofDestruction.
	 * @param actor NpcInstance
	 */
	public ThroneOfDestruction(NpcInstance actor)
	{
		super(actor);
		actor.block();
		actor.startDamageBlocked();
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		final Reflection ref = actor.getReflection();
		
		if (checkAllDestroyed(actor.getId()))
		{
			ref.openDoor(DOOR);
			ref.addSpawnWithoutRespawn(TIAT_NPC_ID, TIAT_LOC, 0);
		}
		
		super.onEvtDead(killer);
	}
	
	/**
	 * Method checkAllDestroyed.
	 * @param mobId int
	 * @return boolean
	 */
	private boolean checkAllDestroyed(int mobId)
	{
		for (NpcInstance npc : getActor().getReflection().getNpcs())
		{
			if (Util.contains(checkNpcs, npc.getId()) && !npc.isDead())
			{
				return false;
			}
		}
		
		return true;
	}
}
