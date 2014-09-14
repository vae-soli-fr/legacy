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

import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.TradeItem;

public class PrivateStoreListSell extends L2GameServerPacket
{
	private final int _sellerId;
	private final long _adena;
	private final boolean _package;
	private final List<TradeItem> _sellList;
	
	public PrivateStoreListSell(Player buyer, Player seller)
	{
		_sellerId = seller.getObjectId();
		_adena = buyer.getAdena();
		_package = seller.getPrivateStoreType() == Player.STORE_PRIVATE_SELL_PACKAGE;
		_sellList = seller.getSellList();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xA1);
		writeD(_sellerId);
		writeD(_package ? 1 : 0);
		writeQ(_adena);
		writeD(0x00); // L2WT GOD
		writeD(_sellList.size());
		
		for (TradeItem si : _sellList)
		{
			writeItemInfo(si);
			writeQ(si.getOwnersPrice());
			writeQ(si.getStorePrice());
		}
	}
}