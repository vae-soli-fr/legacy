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

import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.MultiSellHolder.MultiSellListContainer;
import lineage2.gameserver.model.base.MultiSellEntry;
import lineage2.gameserver.model.base.MultiSellIngredient;
import lineage2.gameserver.templates.item.ItemTemplate;

public class MultiSellList extends L2GameServerPacket
{
	private final int _page;
	private final int _finished;
	private final int _listId;
	private final List<MultiSellEntry> _list;
	
	/**
	 * Constructor for MultiSellList.
	 * @param list MultiSellListContainer
	 * @param page int
	 * @param finished int
	 */
	public MultiSellList(MultiSellListContainer list, int page, int finished)
	{
		_list = list.getEntries();
		_listId = list.getListId();
		_page = page;
		_finished = finished;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xD0);
		writeD(_listId);
		writeD(_page);
		writeD(_finished);
		writeD(Config.MULTISELL_SIZE);
		writeD(_list.size());
		writeC(0x00);
		List<MultiSellIngredient> ingredients;
		
		for (MultiSellEntry ent : _list)
		{
			ingredients = fixIngredients(ent.getIngredients());
			writeD(ent.getEntryId());
			writeC(!ent.getProduction().isEmpty() && ent.getProduction().get(0).isStackable() ? 1 : 0);
			writeH(0x00);
			writeD(0x00);
			writeD(0x00);
			writeItemElements();
			writeH(ent.getProduction().size());
			writeH(ingredients.size());
			
			for (MultiSellIngredient prod : ent.getProduction())
			{
				int itemId = prod.getItemId();
				ItemTemplate template = itemId > 0 ? ItemHolder.getInstance().getTemplate(prod.getItemId()) : null;
				writeD(itemId);
				writeD((itemId > 0) && (template != null) ? template.getBodyPart() : 0);
				writeH((itemId > 0) && (template != null) ? template.getType2ForPackets() : 0);
				writeQ(prod.getItemCount());
				writeH(prod.getItemEnchant());
				writeD(prod.getChance());
				writeD(0x00);
				writeD(0x00);
				writeItemElements(prod);
			}
			
			for (MultiSellIngredient i : ingredients)
			{
				int itemId = i.getItemId();
				final ItemTemplate item = itemId > 0 ? ItemHolder.getInstance().getTemplate(i.getItemId()) : null;
				writeD(itemId);
				writeH((itemId > 0) && (item != null) ? item.getType2() : 0xffff);
				writeQ(i.getItemCount());
				writeH(i.getItemEnchant());
				writeD(0x00);
				writeD(0x00);
				writeItemElements(i);
			}
		}
	}
	
	/**
	 * Method fixIngredients.
	 * @param ingredients List<MultiSellIngredient>
	 * @return List<MultiSellIngredient>
	 */
	private static List<MultiSellIngredient> fixIngredients(List<MultiSellIngredient> ingredients)
	{
		int needFix = 0;
		
		for (MultiSellIngredient ingredient : ingredients)
		{
			if (ingredient.getItemCount() > Integer.MAX_VALUE)
			{
				needFix++;
			}
		}
		
		if (needFix == 0)
		{
			return ingredients;
		}
		
		MultiSellIngredient temp;
		List<MultiSellIngredient> result = new ArrayList<>(ingredients.size() + needFix);
		
		for (MultiSellIngredient ingredient : ingredients)
		{
			ingredient = ingredient.clone();
			
			while (ingredient.getItemCount() > Integer.MAX_VALUE)
			{
				temp = ingredient.clone();
				temp.setItemCount(2000000000);
				result.add(temp);
				ingredient.setItemCount(ingredient.getItemCount() - 2000000000);
			}
			
			if (ingredient.getItemCount() > 0)
			{
				result.add(ingredient);
			}
		}
		
		return result;
	}
}
