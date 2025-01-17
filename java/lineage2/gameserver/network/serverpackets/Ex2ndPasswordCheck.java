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

/**
 * Format (ch)dd d: window type d: ban user (1)
 */
public class Ex2ndPasswordCheck extends L2GameServerPacket
{
	public static final int PASSWORD_NEW = 0x00;
	public static final int PASSWORD_PROMPT = 0x01;
	public static final int PASSWORD_OK = 0x02;
	private final int _windowType;
	
	public Ex2ndPasswordCheck(int windowType)
	{
		_windowType = windowType;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x10A);
		writeD(_windowType);
		writeD(0x00);
	}
}
