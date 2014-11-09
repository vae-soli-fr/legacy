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
package ai.Teredor;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class TeredorEggs extends Fighter
{
	static final int[] eggMonsters =
	{
		18993,
		18994
	};
	private static final int monsterSpawnDelay = 3;
	private static final int poisonId = 14561;
	private static final int poisonLevel = 1;
	private static final int distanceToDebuff = 400;
	boolean _activated = false;
	final NpcInstance actor = getActor();
	
	/**
	 * Constructor for TeredorEggs.
	 * @param actor NpcInstance
	 */
	public TeredorEggs(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}
	
	/**
	 * Method thinkAttack.
	 */
	@Override
	protected void thinkAttack()
	{
		if (!_activated)
		{
			final Player player = (Player) actor.getAggroList().getMostHated();
			final Reflection ref = actor.getReflection();
			ThreadPoolManager.getInstance().schedule(new SpawnMonster(actor, player, ref), monsterSpawnDelay * 1000);
			
			if (player.getParty() != null)
			{
				for (Playable playable : player.getParty().getPartyMembersWithPets())
				{
					if ((playable != null) && (actor.getDistance(playable.getLoc()) <= distanceToDebuff))
					{
						actor.doCast(SkillTable.getInstance().getInfo(poisonId, poisonLevel), playable, true);
					}
				}
			}
			
			_activated = true;
		}
		
		super.thinkAttack();
	}
	
	/**
	 * @author Mobius
	 */
	private class SpawnMonster extends RunnableImpl
	{
		NpcInstance _npc;
		Player _player;
		Reflection _ref;
		
		/**
		 * Constructor for SpawnMonster.
		 * @param npc NpcInstance
		 * @param player Player
		 * @param ref Reflection
		 */
		SpawnMonster(NpcInstance npc, Player player, Reflection ref)
		{
			_npc = npc;
			_player = player;
			_ref = ref;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if ((_npc != null) && (!_npc.isDead()))
			{
				if (_player != null)
				{
					_npc.decayMe();
					final int chosenMonster = eggMonsters[Rnd.get(eggMonsters.length)];
					final Location coords = Location.findPointToStay(actor, 100, 120);
					final NpcInstance npc = _ref.addSpawnWithoutRespawn(chosenMonster, coords, 0);
					
					if (npc.getId() == 18994)
					{
						npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, _player, Rnd.get(1, 100));
					}
				}
				else
				{
					_npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
				}
			}
		}
	}
}
