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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.utils.Location;

/**
 * format dddddd (player id, target id, distance, startx, starty, startz)
 * <p>
 */
public class ValidateLocation extends L2GameServerPacket
{
	private final int _chaObjId;
	private final Location _loc;
	
	public ValidateLocation(Creature cha)
	{
		_chaObjId = cha.getObjectId();
		_loc = cha.getLoc();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x79);
		writeD(_chaObjId);
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ());
		writeD(_loc.getHeading());
	}
}