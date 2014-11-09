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

import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.Location;

public class PartyMemberPosition extends L2GameServerPacket
{
	private final Map<Integer, Location> positions = new HashMap<>();
	
	public PartyMemberPosition add(Player actor)
	{
		positions.put(actor.getObjectId(), actor.getLoc());
		return this;
	}
	
	public int size()
	{
		return positions.size();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xba);
		writeD(positions.size());
		
		for (Map.Entry<Integer, Location> e : positions.entrySet())
		{
			writeD(e.getKey());
			writeD(e.getValue().getX());
			writeD(e.getValue().getY());
			writeD(e.getValue().getZ());
		}
	}
}