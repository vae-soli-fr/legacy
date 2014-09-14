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

/**
 * Format: (ch) dc d: character object id c: 1 if won 0 if failed
 */
public class ExFishingEnd extends L2GameServerPacket
{
	private final int _charId;
	private final boolean _win;
	
	public ExFishingEnd(Player character, boolean win)
	{
		_charId = character.getObjectId();
		_win = win;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x1f);
		writeD(_charId);
		writeC(_win ? 1 : 0);
	}
}