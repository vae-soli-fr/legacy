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

/**
 * @author Smo
 */
public class ExCuriousHouseResult extends L2GameServerPacket
{
	public ExCuriousHouseResult()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x12A);
		writeD(0);
		writeH(1);
		writeD(1);
		writeD(268483021);
		writeD(1);
		writeD(146);
		writeD(146);
		writeD(2);
	}
	
	@Override
	public String getType()
	{
		return "[S] FE:12A ExCuriousHouseResult";
	}
}
