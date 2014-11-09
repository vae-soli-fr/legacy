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
package instances;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;

public final class Nursery extends Reflection
{
	final int Creature1 = 23033;
	final int Creature2 = 23034;
	final int Creature3 = 23035;
	final int Creature4 = 23036;
	final int Creature5 = 23037;
	int reward;
	NpcInstance tuy;
	@SuppressWarnings("unused")
	private final DeathListener _deathListener = new DeathListener();
	
	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
	}
	
	private class DeathListener implements OnDeathListener
	{
		public DeathListener()
		{
		}
		
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (self.isNpc())
			{
				if ((self.getId() == Creature1) || (self.getId() == Creature2) || (self.getId() == Creature3) || (self.getId() == Creature4) || (self.getId() == Creature5))
				{
					Rnd.get(15);
				}
			}
		}
	}
}