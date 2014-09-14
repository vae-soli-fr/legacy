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

import lineage2.gameserver.templates.jump.JumpPoint;

/**
 * @author K1mel
 * @twitter http://twitter.com/k1mel_developer
 */
public class ExFlyMove extends L2GameServerPacket
{
	private static final int MANY_WAY_TYPE = 0;
	private static final int ONE_WAY_TYPE = 2;
	private final int _objId;
	private final JumpPoint[] _points;
	private int _type;
	private final int _trackId;
	
	public ExFlyMove(int objId, JumpPoint[] points, int trackId)
	{
		_objId = objId;
		_points = points;
		
		if (_points.length > 1)
		{
			_type = MANY_WAY_TYPE;
		}
		else
		{
			_type = ONE_WAY_TYPE;
		}
		
		_trackId = trackId;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xE8);
		writeD(_objId); // Player Object ID
		writeD(_type); // Fly Type (1 - Many Way, 2 - One Way)
		writeD(0x00); // UNK
		writeD(_trackId); // Track ID
		writeD(_points.length); // Next Points Count
		
		for (JumpPoint point : _points)
		{
			writeD(point.getNextWayId()); // Next Way ID
			writeD(0x00); // UNK
			writeD(point.getLocation().getX());
			writeD(point.getLocation().getY());
			writeD(point.getLocation().getZ());
		}
	}
}
