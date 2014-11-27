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
package lineage2.gameserver.model.base;

import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.items.ItemAttributes;

public class MultiSellIngredient implements Cloneable
{
	private int _itemId;
	private long _itemCount;
	private int _itemEnchant;
	private ItemAttributes _itemAttributes;
	private boolean _mantainIngredient;
	private int _chance = -1;
	private int _augmentationId = 0;
	
	public MultiSellIngredient(int itemId, long itemCount)
	{
		this(itemId, itemCount, 0);
	}
	
	public MultiSellIngredient(int itemId, long itemCount, int enchant)
	{
		_itemId = itemId;
		_itemCount = itemCount;
		_itemEnchant = enchant;
		_mantainIngredient = false;
		_itemAttributes = new ItemAttributes();
	}
	
	@Override
	public MultiSellIngredient clone()
	{
		MultiSellIngredient mi = new MultiSellIngredient(_itemId, _itemCount, _itemEnchant);
		mi.setChance(_chance);
		mi.setMantainIngredient(_mantainIngredient);
		mi.setItemAttributes(_itemAttributes.clone());
		return mi;
	}
	
	/**
	 * @param itemId The itemId to set.
	 */
	public void setItemId(int itemId)
	{
		_itemId = itemId;
	}
	
	/**
	 * @return Returns the itemId.
	 */
	public int getItemId()
	{
		return _itemId;
	}
	
	/**
	 * @param itemCount The itemCount to set.
	 */
	public void setItemCount(long itemCount)
	{
		_itemCount = itemCount;
	}
	
	/**
	 * @return Returns the itemCount.
	 */
	public long getItemCount()
	{
		return _itemCount;
	}
	
	/**
	 * Returns if item is stackable
	 * @return boolean
	 */
	public boolean isStackable()
	{
		return (_itemId <= 0) || ItemHolder.getInstance().getTemplate(_itemId).isStackable();
	}
	
	/**
	 * @param itemEnchant The itemEnchant to set.
	 */
	public void setItemEnchant(int itemEnchant)
	{
		_itemEnchant = itemEnchant;
	}
	
	/**
	 * @return Returns the itemEnchant.
	 */
	public int getItemEnchant()
	{
		return _itemEnchant;
	}
	
	public ItemAttributes getItemAttributes()
	{
		return _itemAttributes;
	}
	
	public void setItemAttributes(ItemAttributes attr)
	{
		_itemAttributes = attr;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) (_itemCount ^ (_itemCount >>> 32));
		for (Element e : Element.VALUES)
		{
			result = (prime * result) + _itemAttributes.getValue(e);
		}
		result = (prime * result) + _itemEnchant;
		result = (prime * result) + _itemId;
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		MultiSellIngredient other = (MultiSellIngredient) obj;
		if (_itemId != other._itemId)
		{
			return false;
		}
		if (_itemCount != other._itemCount)
		{
			return false;
		}
		if (_itemEnchant != other._itemEnchant)
		{
			return false;
		}
		for (Element e : Element.VALUES)
		{
			if (_itemAttributes.getValue(e) != other._itemAttributes.getValue(e))
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean getMantainIngredient()
	{
		return _mantainIngredient;
	}
	
	public void setMantainIngredient(boolean mantainIngredient)
	{
		_mantainIngredient = mantainIngredient;
	}
	
	public int getChance()
	{
		return getChance(false);
	}
	
	public int getChance(final boolean clientSide)
	{
		if (clientSide && (_chance == -1))
		{
			return 100;
		}
		else if (clientSide && (_chance > 0) && (_chance < 70))
		{
			if (_chance >= 50)
			{
				return 15;
			}
			if (_chance >= 20)
			{
				return 8;
			}
		}
		return _chance;
	}
	
	public void setChance(int chance)
	{
		_chance = chance;
	}
	
	public int getAugmentationId()
	{
		return _augmentationId;
	}
	
	public void setAugmentationId(int augmentation)
	{
		_augmentationId = augmentation;
	}
}