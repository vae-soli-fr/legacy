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

public class VehicleDeparture extends L2GameServerPacket
{
	private final int _moveSpeed;
	private final int _rotationSpeed;
	private final int _boatObjId;
	private final Location _loc;
	
	public VehicleDeparture(Boat boat)
	{
		_boatObjId = boat.getBoatId();
		_moveSpeed = boat.getMoveSpeed();
		_rotationSpeed = boat.getRotationSpeed();
		_loc = boat.getDestination();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x6c);
		writeD(_boatObjId);
		writeD(_moveSpeed);
		writeD(_rotationSpeed);
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ());
	}
}