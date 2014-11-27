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
package npc.model;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SteelCitadelTeleporterInstance extends NpcInstance
{
	
	/**
	 * Constructor for SteelCitadelTeleporterInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public SteelCitadelTeleporterInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		
		if (!player.isInParty())
		{
			showChatWindow(player, "default/32745-1.htm");
			return;
		}
		
		if (player.getParty().getPartyLeader() != player)
		{
			showChatWindow(player, "default/32745-2.htm");
			return;
		}
		
		if (!rangeCheck(player))
		{
			showChatWindow(player, "default/32745-2.htm");
			return;
		}
		
		switch (command)
		{
			case "01_up":
				player.getParty().Teleport(new Location(-22208, 277122, -13376));
				return;
				
			case "02_up":
				player.getParty().Teleport(new Location(-22208, 277106, -11648));
				return;
				
			case "02_down":
				player.getParty().Teleport(new Location(-22208, 277074, -15040));
				return;
				
			case "03_up":
				player.getParty().Teleport(new Location(-22208, 277120, -9920));
				return;
				
			case "03_down":
				player.getParty().Teleport(new Location(-22208, 277120, -13376));
				return;
				
			case "04_up":
				player.getParty().Teleport(new Location(-19024, 277126, -8256));
				return;
				
			case "04_down":
				player.getParty().Teleport(new Location(-22208, 277106, -11648));
				return;
				
			case "06_up":
				player.getParty().Teleport(new Location(-19024, 277106, -9920));
				return;
				
			case "06_down":
				player.getParty().Teleport(new Location(-22208, 277122, -9920));
				return;
				
			case "07_up":
				player.getParty().Teleport(new Location(-19008, 277100, -11648));
				return;
				
			case "07_down":
				player.getParty().Teleport(new Location(-19024, 277122, -8256));
				return;
				
			case "08_up":
				player.getParty().Teleport(new Location(-19008, 277100, -13376));
				return;
				
			case "08_down":
				player.getParty().Teleport(new Location(-19008, 277106, -9920));
				return;
				
			case "09_up":
				player.getParty().Teleport(new Location(14602, 283179, -7500));
				return;
				
			case "09_down":
				player.getParty().Teleport(new Location(-19008, 277100, -11648));
				return;
				
			case "facedemon":
				enterInstance(player, 5);
				return;
				
			case "faceranku":
				enterInstance(player, 6);
				return;
				
			case "leave":
				player.getReflection().collapse();
				return;
				
			default:
				super.onBypassFeedback(player, command);
				break;
		}
	}
	
	/**
	 * Method rangeCheck.
	 * @param pl Player
	 * @return boolean
	 */
	private boolean rangeCheck(Player pl)
	{
		for (Player m : pl.getParty().getPartyMembers())
		{
			if (!pl.isInRange(m, 400))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Method getIz.
	 * @param floor int
	 * @return int
	 */
	private int getIz(int floor)
	{
		if (floor == 5)
		{
			return 3;
		}
		
		return 4;
	}
	
	/**
	 * Method enterInstance.
	 * @param player Player
	 * @param floor int
	 */
	private void enterInstance(Player player, int floor)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(getIz(floor)))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(getIz(floor)))
		{
			ReflectionUtils.enterReflection(player, getIz(floor));
		}
	}
}
