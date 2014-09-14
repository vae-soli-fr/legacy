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

import java.util.Collection;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SubClass;

/**
 * @author ALF
 * @author Darvin
 * @data 09.02.2012
 */
public class ExSubjobInfo extends L2GameServerPacket
{
	private final Collection<SubClass> _subClasses;
	private final int _raceId;
	private final int _classId;
	private final boolean _openStatus;
	
	public ExSubjobInfo(Player player, boolean openStatus)
	{
		_openStatus = openStatus;
		_raceId = player.getRace().ordinal();
		_classId = player.getClassId().ordinal();
		_subClasses = player.getSubClassList().values();
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xEA);
		writeC(_openStatus);
		writeD(_classId);
		writeD(_raceId);
		writeD(_subClasses.size());
		
		for (SubClass subClass : _subClasses)
		{
			writeD(subClass.getIndex());
			writeD(subClass.getClassId());
			writeD(subClass.getLevel());
			writeC(subClass.getType().ordinal());
		}
	}
}
