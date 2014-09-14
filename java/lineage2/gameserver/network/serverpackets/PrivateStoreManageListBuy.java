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

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.model.items.Warehouse.ItemClassComparator;
import lineage2.gameserver.templates.item.ItemTemplate;

public class PrivateStoreManageListBuy extends L2GameServerPacket
{
	private final int _buyerId;
	private final long _adena;
	private final List<TradeItem> _buyList0;
	private final List<TradeItem> _buyList;
	
	public PrivateStoreManageListBuy(Player buyer)
	{
		_buyerId = buyer.getObjectId();
		_adena = buyer.getAdena();
		_buyList0 = buyer.getBuyList();
		_buyList = new ArrayList<>();
		ItemInstance[] items = buyer.getInventory().getItems();
		ArrayUtils.eqSort(items, ItemClassComparator.getInstance());
		TradeItem bi;
		
		for (ItemInstance item : items)
		{
			if (item.canBeTraded(buyer) && (item.getItemId() != ItemTemplate.ITEM_ID_ADENA))
			{
				_buyList.add(bi = new TradeItem(item));
				bi.setObjectId(0);
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xBD);
		// section 1
		writeD(_buyerId);
		writeQ(_adena);
		// section2
		writeD(_buyList.size());// for potential sells
		
		for (TradeItem bi : _buyList)
		{
			writeItemInfo(bi);
			writeQ(bi.getStorePrice());
		}
		
		// section 3
		writeD(_buyList0.size());// count for any items already added for sell
		
		for (TradeItem bi : _buyList0)
		{
			writeItemInfo(bi);
			writeQ(bi.getOwnersPrice());
			writeQ(bi.getStorePrice());
			writeQ(bi.getCount());
		}
	}
}