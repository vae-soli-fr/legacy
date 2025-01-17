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

import lineage2.gameserver.data.xml.holder.HennaHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.templates.Henna;

public class HennaEquipList extends L2GameServerPacket
{
	private final int _emptySlots;
	private final long _adena;
	private final List<HennaData> _hennas = new ArrayList<>();
	
	public HennaEquipList(Player player)
	{
		_adena = player.getAdena();
		_emptySlots = player.getHennaEmptySlots();
		List<Henna> list = HennaHolder.getInstance().generateList(player);
		
		for (Henna element : list)
		{
			if (player.getInventory().getItemByItemId(element.getDyeId()) != null)
			{
				_hennas.add(new HennaData(element, element.isForThisClass(player) ? 0x01 : 0x00));
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xEE);
		writeQ(_adena);
		writeD(_emptySlots);
		
		if (_hennas.size() != 0)
		{
			writeD(_hennas.size());
			
			for (HennaData henna : _hennas)
			{
				writeD(henna._symbol.getSymbolId()); // symbolid
				writeD(henna._symbol.getDyeId()); // itemid of dye
				writeQ(henna._symbol.getDrawCount());
				writeQ(henna._symbol.getPrice());
				writeD(henna._valid); // meet the requirement or not
			}
		}
		else
		{
			writeD(0x01);
			writeD(0x00);
			writeD(0x00);
			writeQ(0x00);
			writeQ(0x00);
			writeD(0x00);
		}
	}
	
	private class HennaData
	{
		final Henna _symbol;
		final int _valid;
		
		public HennaData(Henna he, int valid)
		{
			_symbol = he;
			_valid = valid;
		}
	}
}