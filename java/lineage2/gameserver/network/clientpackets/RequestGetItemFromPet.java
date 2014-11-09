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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.items.PetInventory;
import lineage2.gameserver.network.serverpackets.SystemMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestGetItemFromPet extends L2GameClientPacket
{
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(RequestGetItemFromPet.class);
	private int _objectId;
	private long _amount;
	@SuppressWarnings("unused")
	private int _unknown;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_amount = readQ();
		_unknown = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		
		if ((activeChar == null) || (_amount < 1))
		{
			return;
		}
		
		PetInstance pet = activeChar.getSummonList().getPet();
		
		if (pet == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		
		if (activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}
		
		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM));
			return;
		}
		
		if (activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}
		
		if (activeChar.isFishing())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_DO_THAT_WHILE_FISHING));
			return;
		}
		
		PetInventory petInventory = pet.getInventory();
		PcInventory playerInventory = activeChar.getInventory();
		ItemInstance item = petInventory.getItemByObjectId(_objectId);
		
		if ((item == null) || (item.getCount() < _amount) || item.isEquipped())
		{
			activeChar.sendActionFailed();
			return;
		}
		
		int slots = 0;
		long weight = item.getTemplate().getWeight() * _amount;
		
		if (!item.getTemplate().isStackable() || (activeChar.getInventory().getItemByItemId(item.getId()) == null))
		{
			slots = 1;
		}
		
		if (!activeChar.getInventory().validateWeight(weight))
		{
			sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT));
			return;
		}
		
		if (!activeChar.getInventory().validateCapacity(slots))
		{
			sendPacket(new SystemMessage(SystemMessage.YOUR_INVENTORY_IS_FULL));
			return;
		}
		
		playerInventory.addItem(petInventory.removeItemByObjectId(_objectId, _amount));
		pet.sendChanges();
		activeChar.sendChanges();
	}
}
