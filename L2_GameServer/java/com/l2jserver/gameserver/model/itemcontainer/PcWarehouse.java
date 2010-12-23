/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.model.itemcontainer;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.L2ItemInstance.ItemLocation;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

public class PcWarehouse extends Warehouse
{
	//private static final Logger _log = Logger.getLogger(PcWarehouse.class.getName());
	
	private L2PcInstance _owner;
	
	public PcWarehouse(L2PcInstance owner)
	{
		_owner = owner;
	}
	
	@Override
	public String getName() { return "Warehouse"; }
	
	@Override
	public L2PcInstance getOwner() { return _owner; }
	@Override
	public ItemLocation getBaseLocation() { return ItemLocation.WAREHOUSE; }
	public String getLocationId() { return "0"; }
	public int getLocationId(boolean dummy) { return 0; }
	public void setLocationId(L2PcInstance dummy) {}
	
	@Override
	public boolean validateCapacity(int slots)
	{
		return (_items.size() + slots <= _owner.getWareHouseLimit());
	}

    @Override
    public void restore()
    {
            Connection con = null;
            try
            {
                con = L2DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement;
                statement = con.prepareStatement("SELECT object_id, item_id, count, enchant_level, loc, loc_data, custom_type1, custom_type2, mana_left, time, account FROM items WHERE account=? AND loc=\"WAREHOUSE\"");
                statement.setString(1, _owner.getAccountName());
                ResultSet inv = statement.executeQuery();

                L2ItemInstance item;
                while (inv.next())
                {
                    item = L2ItemInstance.restoreFromDb(getOwnerId(), inv);
                    if (item == null)
                        continue;

                    L2World.getInstance().storeObject(item);

                    L2PcInstance owner = getOwner() == null ? null : getOwner().getActingPlayer();

                    // If stackable item is found in inventory just add to current quantity
                    if (item.isStackable() && getItemByItemId(item.getItemId()) != null)
                        addItem("Restore", item, owner, null);
                    else
                        addItem(item);
                }

                inv.close();
                statement.close();
                refreshWeight();
            }
            catch (Exception e)
            {
                _log.log(Level.WARNING, "could not restore container:", e);
            }
            finally
            {
                L2DatabaseFactory.close(con);
            }
    }

}
