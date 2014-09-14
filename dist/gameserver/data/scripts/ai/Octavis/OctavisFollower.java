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
package ai.Octavis;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

public final class OctavisFollower extends DefaultAI
{
	private static final int Rider = 29192;
	private static final int DRIFT_DISTANCE = 10;
	private long _wait_timeout = 0;
	private HardReference<? extends Creature> _octavisRef = HardReferences.emptyRef();
	
	public OctavisFollower(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	@Override
	protected boolean randomAnimation()
	{
		return false;
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		Creature octavis = _octavisRef.get();
		
		if (octavis == null)
		{
			if (System.currentTimeMillis() > _wait_timeout)
			{
				_wait_timeout = System.currentTimeMillis() + 1000;
				
				for (NpcInstance npc : World.getAroundNpc(actor))
				{
					if (npc.getNpcId() == Rider)
					{
						_octavisRef = npc.getRef();
						return true;
					}
				}
			}
		}
		else if (!actor.isMoving)
		{
			int x = (octavis.getX() + Rnd.get(2 * DRIFT_DISTANCE)) - DRIFT_DISTANCE;
			int y = (octavis.getY() + Rnd.get(2 * DRIFT_DISTANCE)) - DRIFT_DISTANCE;
			int z = octavis.getZ();
			actor.setRunning();
			actor.moveToLocation(x, y, z, 0, true);
			startSkillAttackTask();
			return true;
		}
		
		return false;
	}
	
	private void startSkillAttackTask()
	{
		NpcInstance npc = getActor();
		Skill skill = SkillTable.getInstance().getInfo(14285, 1); // TODO[K] - Skills lighting Octavis
		
		for (Creature creature : npc.getAroundCharacters(10000, 100))
		{
			if (Rnd.chance(30))
			{
				npc.altOnMagicUseTimer(creature, skill);
			}
		}
		
		doTask();
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
	}
	
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
	}
}