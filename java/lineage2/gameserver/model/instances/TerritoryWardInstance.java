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
package lineage2.gameserver.model.instances;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.entity.events.objects.TerritoryWardObject;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Smo
 */
public class TerritoryWardInstance extends NpcInstance
{
	private final TerritoryWardObject _territoryWard;
	
	public TerritoryWardInstance(int objectId, NpcTemplate template, TerritoryWardObject territoryWardObject)
	{
		super(objectId, template);
		setHasChatWindow(false);
		_territoryWard = territoryWardObject;
	}
	
	@Override
	public void onDeath(Creature killer)
	{
		super.onDeath(killer);
		Player player = killer.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (_territoryWard.canPickUp(player))
		{
			_territoryWard.pickUp(player);
			decayMe();
		}
	}
	
	@Override
	protected void onDecay()
	{
		decayMe();
		
		_spawnAnimation = 2;
	}
	
	@Override
	public boolean isAttackable(Creature attacker)
	{
		return isAutoAttackable(attacker);
	}
	
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		DominionSiegeEvent siegeEvent = getEvent(DominionSiegeEvent.class);
		if (siegeEvent == null)
		{
			return false;
		}
		DominionSiegeEvent siegeEvent2 = attacker.getEvent(DominionSiegeEvent.class);
		if (siegeEvent2 == null)
		{
			return false;
		}
		if (siegeEvent == siegeEvent2)
		{
			return false;
		}
		if (siegeEvent2.getResidence().getOwner() != attacker.getClan())
		{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isInvul()
	{
		return false;
	}
	
	@Override
	public Clan getClan()
	{
		return null;
	}
}
