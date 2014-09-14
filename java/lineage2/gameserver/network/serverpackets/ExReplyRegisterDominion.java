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

import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.residence.Dominion;

/**
 * @author Smo
 */
public class ExReplyRegisterDominion extends L2GameServerPacket
{
	private final int _dominionId;
	private final int _clanCount;
	private final int _playerCount;
	private final boolean _success;
	private final boolean _join;
	private final boolean _asClan;
	
	public ExReplyRegisterDominion(Dominion dominion, boolean success, boolean join, boolean asClan)
	{
		_success = success;
		_join = join;
		_asClan = asClan;
		_dominionId = dominion.getId();
		
		DominionSiegeEvent siegeEvent = dominion.getSiegeEvent();
		
		_playerCount = siegeEvent.getObjects(DominionSiegeEvent.DEFENDER_PLAYERS).size();
		_clanCount = siegeEvent.getObjects(SiegeEvent.DEFENDERS).size() + 1;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x92);
		writeD(_dominionId);
		writeD(_asClan);
		writeD(_join);
		writeD(_success);
		writeD(_clanCount);
		writeD(_playerCount);
	}
}
