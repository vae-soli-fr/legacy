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

import org.apache.commons.lang3.StringUtils;

public class PrivateStoreMsgSell extends L2GameServerPacket
{
	private final int _objId;
	private final String _name;
	private final boolean _pkg;
	
	public PrivateStoreMsgSell(Player player)
	{
		_objId = player.getObjectId();
		_pkg = player.getPrivateStoreType() == Player.STORE_PRIVATE_SELL_PACKAGE;
		_name = StringUtils.defaultString(player.getSellStoreName());
	}
	
	@Override
	protected final void writeImpl()
	{
		if (_pkg)
		{
			writeEx(0x81);
		}
		else
		{
			writeC(0xA2);
		}
		
		writeD(_objId);
		writeS(_name);
	}
}