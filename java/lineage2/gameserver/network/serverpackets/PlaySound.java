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

import lineage2.gameserver.utils.Location;

public class PlaySound extends L2GameServerPacket
{
	public static final L2GameServerPacket SIEGE_VICTORY = new PlaySound("Siege_Victory");
	public static final L2GameServerPacket B04_S01 = new PlaySound("B04_S01");
	public static final L2GameServerPacket HB01 = new PlaySound(PlaySound.Type.MUSIC, "HB01", 0, 0, 0, 0, 0);
	
	public enum Type
	{
		SOUND,
		MUSIC,
		VOICE
	}
	
	private final Type _type;
	private final String _soundFile;
	private final int _hasCenterObject;
	private final int _objectId;
	private final int _x;
	private final int _y;
	private final int _z;
	
	public PlaySound(String soundFile)
	{
		this(Type.SOUND, soundFile, 0, 0, 0, 0, 0);
	}
	
	public PlaySound(Type type, String soundFile, int c, int objectId, Location loc)
	{
		this(type, soundFile, c, objectId, loc == null ? 0 : loc.getX(), loc == null ? 0 : loc.getY(), loc == null ? 0 : loc.getZ());
	}
	
	public PlaySound(Type type, String soundFile, int c, int objectId, int x, int y, int z)
	{
		_type = type;
		_soundFile = soundFile;
		_hasCenterObject = c;
		_objectId = objectId;
		_x = x;
		_y = y;
		_z = z;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x9e);
		// dSdddddd
		writeD(_type.ordinal()); // 0 for quest and ship, c4 toturial = 2
		writeS(_soundFile);
		writeD(_hasCenterObject); // 0 for quest; 1 for ship;
		writeD(_objectId); // 0 for quest; objectId of ship
		writeD(_x); // x
		writeD(_y); // y
		writeD(_z); // z
	}
}