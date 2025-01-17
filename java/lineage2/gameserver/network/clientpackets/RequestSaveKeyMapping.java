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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExUISetting;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestSaveKeyMapping extends L2GameClientPacket
{
	private byte[] _data;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		int length = readD();
		
		if ((length > _buf.remaining()) || (length > Short.MAX_VALUE) || (length < 0))
		{
			_data = null;
			return;
		}
		
		_data = new byte[length];
		readB(_data);
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		
		if ((activeChar == null) || (_data == null))
		{
			return;
		}
		
		activeChar.setKeyBindings(_data);
		activeChar.sendPacket(new ExUISetting(activeChar));
	}
}
