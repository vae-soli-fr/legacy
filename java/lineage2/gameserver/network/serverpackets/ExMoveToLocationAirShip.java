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

import lineage2.gameserver.model.entity.boat.Boat;
import lineage2.gameserver.utils.Location;

public class ExMoveToLocationAirShip extends L2GameServerPacket
{
	private final int _objectId;
	private final Location _origin;
	private final Location _destination;
	
	public ExMoveToLocationAirShip(Boat boat)
	{
		_objectId = boat.getBoatId();
		_origin = boat.getLoc();
		_destination = boat.getDestination();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x66);
		writeD(_objectId);
		writeD(_destination.getX());
		writeD(_destination.getY());
		writeD(_destination.getZ());
		writeD(_origin.getX());
		writeD(_origin.getY());
		writeD(_origin.getZ());
	}
}