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

public class ExBR_GamePoint extends L2GameServerPacket
{
	private final int _objectId;
	private final long _points;
	
	public ExBR_GamePoint(Player player)
	{
		_objectId = player.getObjectId();
		_points = player.getPremiumPoints();
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xD6);
		writeD(_objectId);
		writeQ(_points);
		writeD(0x00); // ??
	}
}