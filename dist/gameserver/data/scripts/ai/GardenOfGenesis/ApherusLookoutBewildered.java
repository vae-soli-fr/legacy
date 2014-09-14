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
package ai.GardenOfGenesis;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

public final class ApherusLookoutBewildered extends Fighter
{
	public ApherusLookoutBewildered(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		NpcInstance actor = getActor();
		
		if ((actor != null) && (killer != null) && (actor != killer))
		{
			NpcUtils.spawnSingle(19002, new Location(killer.getX() - Rnd.get(40), killer.getY() - Rnd.get(40), killer.getZ(), 0));
			NpcUtils.spawnSingle(19001, new Location(killer.getX() - Rnd.get(40), killer.getY() - Rnd.get(40), killer.getZ(), 0));
			NpcUtils.spawnSingle(19002, new Location(killer.getX() - Rnd.get(40), killer.getY() - Rnd.get(40), killer.getZ(), 0));
		}
	}
}