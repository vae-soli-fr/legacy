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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.boat.AirShip;
import lineage2.gameserver.model.entity.boat.ClanAirShip;
import lineage2.gameserver.utils.Location;

public class ExAirShipInfo extends L2GameServerPacket
{
	private final int _objId;
	private final int _speed1;
	private final int _speed2;
	private int _fuel;
	private int _maxFuel;
	private int _driverObjId;
	private int _controlKey;
	private final Location _loc;
	
	public ExAirShipInfo(AirShip ship)
	{
		_objId = ship.getObjectId();
		_loc = ship.getLoc();
		_speed1 = ship.getRunSpeed();
		_speed2 = ship.getRotationSpeed();
		
		if (ship.isClanAirShip())
		{
			_fuel = ((ClanAirShip) ship).getCurrentFuel();
			_maxFuel = ((ClanAirShip) ship).getMaxFuel();
			Player driver = ((ClanAirShip) ship).getDriver();
			_driverObjId = driver == null ? 0 : driver.getObjectId();
			_controlKey = ((ClanAirShip) ship).getControlKey().getObjectId();
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x61);
		writeD(_objId);
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ());
		writeD(_loc.getHeading());
		writeD(_driverObjId); // object id of player who control ship
		writeD(_speed1);
		writeD(_speed2);
		writeD(_controlKey);
		
		if (_controlKey != 0)
		{
			writeD(0x16e); // Controller X
			writeD(0x00); // Controller Y
			writeD(0x6b); // Controller Z
			writeD(0x15c); // Captain X
			writeD(0x00); // Captain Y
			writeD(0x69); // Captain Z
		}
		else
		{
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
		}
		
		writeD(_fuel); // current fuel
		writeD(_maxFuel); // max fuel
	}
}