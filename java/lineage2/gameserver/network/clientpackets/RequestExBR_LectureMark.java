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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExBR_LectureMark extends L2GameClientPacket
{
	private static final int INITIAL_MARK = 1;
	private static final int EVANGELIST_MARK = 2;
	private static final int OFF_MARK = 3;
	private int _mark;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_mark = readC();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		
		if ((player == null) || !Config.EX_LECTURE_MARK)
		{
			return;
		}
		
		switch (_mark)
		{
			case INITIAL_MARK:
			case EVANGELIST_MARK:
			case OFF_MARK:
				player.setLectureMark(_mark);
				player.broadcastUserInfo();
				break;
		}
	}
}
