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
package handlers.items;

import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExAutoSoulShot;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.item.WeaponTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SpiritShot extends ScriptItemHandler
{
	private static final int[] _itemIds =
	{
		5790,
		2509,
		2510,
		2511,
		2512,
		2513,
		2514,
		22077,
		22078,
		22079,
		22080,
		22081,
		19441,
		33787
	};
	private static final int[] _skillIds =
	{
		2047,
		2155,
		2156,
		2157,
		2158,
		2159,
		9194
	};
	
	/**
	 * Method useItem.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean
	 * @see lineage2.gameserver.handlers.IItemHandler#useItem(Playable, ItemInstance, boolean)
	 */
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if ((playable == null) || !playable.isPlayer())
		{
			return false;
		}
		
		final Player player = (Player) playable;
		final ItemInstance weaponInst = player.getActiveWeaponInstance();
		final WeaponTemplate weaponItem = player.getActiveWeaponItem();
		final int SoulshotId = item.getId();
		boolean isAutoSoulShot = false;
		
		if (player.getAutoSoulShot().contains(SoulshotId))
		{
			isAutoSoulShot = true;
		}
		
		if (weaponInst == null)
		{
			if (!isAutoSoulShot)
			{
				player.sendPacket(new SystemMessage(SystemMessage.CANNOT_USE_SPIRITSHOTS));
			}
			
			return false;
		}
		
		if (weaponInst.getChargedSpiritshot() != ItemInstance.CHARGED_NONE)
		{
			return false;
		}
		
		final int SpiritshotId = item.getId();
		final int grade = weaponItem.getCrystalType().externalOrdinal;
		final int soulSpiritConsumption = weaponItem.getSpiritShotCount();
		final long count = item.getCount();
		
		if (soulSpiritConsumption == 0)
		{
			if (isAutoSoulShot)
			{
				player.removeAutoSoulShot(SoulshotId);
				player.sendPacket(new ExAutoSoulShot(SoulshotId, false), new SystemMessage(SystemMessage.THE_AUTOMATIC_USE_OF_S1_WILL_NOW_BE_CANCELLED).addItemName(SoulshotId));
				return false;
			}
			
			player.sendPacket(new SystemMessage(SystemMessage.CANNOT_USE_SPIRITSHOTS));
			return false;
		}
		
		if (((grade == 0) && (SpiritshotId != 5790) && (SpiritshotId != 2509)) || ((grade == 1) && (SpiritshotId != 2510) && (SpiritshotId != 22077)) || ((grade == 2) && (SpiritshotId != 2511) && (SpiritshotId != 22078)) || ((grade == 3) && (SpiritshotId != 2512) && (SpiritshotId != 22079)) || ((grade == 4) && (SpiritshotId != 2513) && (SpiritshotId != 22080)) || ((grade == 5) && (SpiritshotId != 2514) && (SpiritshotId != 22081)) || ((grade == 6) && (SpiritshotId != 19441) && (SpiritshotId != 33787)))
		{
			if (isAutoSoulShot)
			{
				return false;
			}
			
			player.sendPacket(new SystemMessage(SystemMessage.SPIRITSHOT_DOES_NOT_MATCH_WEAPON_GRADE));
			return false;
		}
		
		if (count < soulSpiritConsumption)
		{
			if (isAutoSoulShot)
			{
				player.removeAutoSoulShot(SoulshotId);
				player.sendPacket(new ExAutoSoulShot(SoulshotId, false), new SystemMessage(SystemMessage.THE_AUTOMATIC_USE_OF_S1_WILL_NOW_BE_CANCELLED).addItemName(SoulshotId));
				return false;
			}
			
			player.sendPacket(new SystemMessage(SystemMessage.NOT_ENOUGH_SPIRITSHOTS));
			return false;
		}
		
		if (player.getInventory().destroyItem(item, soulSpiritConsumption))
		{
			weaponInst.setChargedSpiritshot(ItemInstance.CHARGED_SPIRITSHOT);
			player.sendPacket(new SystemMessage(SystemMessage.POWER_OF_MANA_ENABLED));
			player.broadcastPacket(new MagicSkillUse(player, player, _skillIds[grade], 1, 0, 0));
		}
		
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[]
	 * @see lineage2.gameserver.handlers.IItemHandler#getItemIds()
	 */
	@Override
	public final int[] getItemIds()
	{
		return _itemIds;
	}
}
