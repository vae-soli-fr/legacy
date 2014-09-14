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

import java.util.List;

import lineage2.gameserver.instancemanager.WorldStatisticsManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.worldstatistics.CharacterStatisticElement;
import lineage2.gameserver.network.serverpackets.ExLoadStatUser;

public class RequestUserStatistics extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
	}
	
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		
		if (player == null)
		{
			return;
		}
		
		List<CharacterStatisticElement> stat = WorldStatisticsManager.getInstance().getCurrentStatisticsForPlayer(player.getObjectId());
		player.sendPacket(new ExLoadStatUser(stat));
	}
}