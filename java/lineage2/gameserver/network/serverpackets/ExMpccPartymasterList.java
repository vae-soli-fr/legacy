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

import java.util.Collections;
import java.util.Set;

/**
 * @author VISTALL
 * @date 6:22/12.06.2011
 */
public class ExMpccPartymasterList extends L2GameServerPacket
{
	private Set<String> _members = Collections.emptySet();
	
	public ExMpccPartymasterList(Set<String> s)
	{
		_members = s;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xA3);
		writeD(_members.size());
		
		for (String t : _members)
		{
			writeS(t);
		}
	}
}
