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

import lineage2.gameserver.model.items.ItemInfo;

public class TradeUpdate extends L2GameServerPacket
{
	private final ItemInfo _item;
	private final long _amount;
	
	public TradeUpdate(ItemInfo item, long amount)
	{
		_item = item;
		_amount = amount;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x81);
		writeH(1);
		writeH((_amount > 0) && _item.getItem().isStackable() ? 3 : 2);
		writeH(_item.getItem().getType1());
		writeD(_item.getObjectId());
		writeD(_item.getId());
		writeQ(_amount);
		writeH(_item.getItem().getType2ForPackets());
		writeH(_item.getCustomType1());
		writeD(_item.getItem().getBodyPart());
		writeH(_item.getEnchantLevel());
		writeH(0x00);
		writeH(_item.getCustomType2());
		writeH(_item.getAttackElement());
		writeH(_item.getAttackElementValue());
		writeH(_item.getDefenceFire());
		writeH(_item.getDefenceWater());
		writeH(_item.getDefenceWind());
		writeH(_item.getDefenceEarth());
		writeH(_item.getDefenceHoly());
		writeH(_item.getDefenceUnholy());
		writeH(0);
		writeH(0);
		writeH(0);
		writeD(0x00);// Visible itemID
	}
}