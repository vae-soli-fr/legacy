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
import java.util.Collections;
import java.util.List;

import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.instancemanager.MatchingRoomManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;

/**
 * Format:(ch) d [sdd]
 */
public class ExListPartyMatchingWaitingRoom extends L2GameServerPacket
{
	private List<PartyMatchingWaitingInfo> _waitingList = Collections.emptyList();
	private final int _fullSize;
	
	public ExListPartyMatchingWaitingRoom(Player searcher, int minLevel, int maxLevel, int page, int[] classes)
	{
		int first = (page - 1) * 64;
		int firstNot = page * 64;
		int i = 0;
		List<Player> temp = MatchingRoomManager.getInstance().getWaitingList(minLevel, maxLevel, classes);
		_fullSize = temp.size();
		_waitingList = new ArrayList<>(_fullSize);
		
		for (Player pc : temp)
		{
			if ((i < first) || (i >= firstNot))
			{
				continue;
			}
			
			_waitingList.add(new PartyMatchingWaitingInfo(pc));
			i++;
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x36);
		writeD(_fullSize);
		writeD(_waitingList.size());
		
		for (PartyMatchingWaitingInfo waiting_info : _waitingList)
		{
			writeS(waiting_info.name);
			writeD(waiting_info.classId);
			writeD(waiting_info.level);
			writeD(waiting_info.currentInstance);
			writeD(waiting_info.instanceReuses.length);
			
			for (int i : waiting_info.instanceReuses)
			{
				writeD(i);
			}
		}
	}
	
	private static class PartyMatchingWaitingInfo
	{
		final int classId;
		final int level;
		final int currentInstance;
		final String name;
		final int[] instanceReuses;
		
		PartyMatchingWaitingInfo(Player member)
		{
			name = member.getName();
			classId = member.getClassId().getId();
			level = member.getLevel();
			Reflection ref = member.getReflection();
			currentInstance = ref == null ? 0 : ref.getInstancedZoneId();
			instanceReuses = ArrayUtils.toArray(member.getInstanceReuses().keySet());
		}
	}
}