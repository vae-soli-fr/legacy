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
package lineage2.gameserver.network.serverpackets;

import java.util.Collections;
import java.util.List;

import lineage2.gameserver.model.entity.boat.ClanAirShip;
import lineage2.gameserver.model.entity.events.objects.BoatPoint;

public class ExAirShipTeleportList extends L2GameServerPacket
{
	private final int _fuel;
	private List<BoatPoint> _airports = Collections.emptyList();
	
	public ExAirShipTeleportList(ClanAirShip ship)
	{
		_fuel = ship.getCurrentFuel();
		_airports = ship.getDock().getTeleportList();
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x9B);
		writeD(_fuel); // current fuel
		writeD(_airports.size());
		
		for (int i = 0; i < _airports.size(); i++)
		{
			BoatPoint point = _airports.get(i);
			writeD(i - 1); // AirportID
			writeD(point.getFuel()); // need fuel
			writeD(point.getX()); // Airport x
			writeD(point.getY()); // Airport y
			writeD(point.getZ()); // Airport z
		}
	}
}