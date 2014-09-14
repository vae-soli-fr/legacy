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

public class StartRotating extends L2GameServerPacket
{
	private final int _charId;
	private final int _degree;
	private final int _side;
	private final int _speed;
	
	public StartRotating(Creature cha, int degree, int side, int speed)
	{
		_charId = cha.getObjectId();
		_degree = degree;
		_side = side;
		_speed = speed;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x7a);
		writeD(_charId);
		writeD(_degree);
		writeD(_side);
		writeD(_speed);
	}
}