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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;

public class TradeStart extends L2GameServerPacket
{
	private final List<ItemInfo> _tradelist = new ArrayList<>();
	private final int targetId;
	
	public TradeStart(Player player, Player target)
	{
		targetId = target.getObjectId();
		ItemInstance[] items = player.getInventory().getItems();
		
		for (ItemInstance item : items)
		{
			if (item.canBeTraded(player))
			{
				_tradelist.add(new ItemInfo(item));
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x14);
		writeD(targetId);
		writeH(_tradelist.size());
		
		for (ItemInfo item : _tradelist)
		{
			writeItemInfo(item);
		}
	}
}