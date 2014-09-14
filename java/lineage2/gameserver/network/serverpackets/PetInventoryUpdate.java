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

import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;

public class PetInventoryUpdate extends L2GameServerPacket
{
	private static final int ADDED = 1;
	private static final int MODIFIED = 2;
	private static final int REMOVED = 3;
	private final List<ItemInfo> _items = new ArrayList<>(1);
	
	public PetInventoryUpdate()
	{
	}
	
	public PetInventoryUpdate addNewItem(ItemInstance item)
	{
		addItem(item).setLastChange(ADDED);
		return this;
	}
	
	public PetInventoryUpdate addModifiedItem(ItemInstance item)
	{
		addItem(item).setLastChange(MODIFIED);
		return this;
	}
	
	public PetInventoryUpdate addRemovedItem(ItemInstance item)
	{
		addItem(item).setLastChange(REMOVED);
		return this;
	}
	
	private ItemInfo addItem(ItemInstance item)
	{
		ItemInfo info;
		_items.add(info = new ItemInfo(item));
		return info;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xb4);
		writeH(_items.size());
		
		for (ItemInfo temp : _items)
		{
			writeH(temp.getLastChange());
			writeItemInfo(temp);
		}
	}
}