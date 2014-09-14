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
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.ExMentorList;
import lineage2.gameserver.utils.MentorUtil;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestMentorCancel extends L2GameClientPacket
{
	private int _mtype;
	private String _charName;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_mtype = readD();
		_charName = readS();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		Player menteeChar = World.getPlayer(_charName);
		activeChar.getMentorSystem().remove(_charName, _mtype == 1, true);
		activeChar.sendPacket(new ExMentorList(activeChar));
		
		if ((menteeChar != null) && (menteeChar.isOnline()))
		{
			menteeChar.getMentorSystem().remove(activeChar.getName(), _mtype != 1, false);
			menteeChar.sendPacket(new ExMentorList(menteeChar));
		}
		
		MentorUtil.removeConditions(activeChar);
		MentorUtil.setTimePenalty((_mtype == 1) ? activeChar.getObjectId() : activeChar.getMentorSystem().getMentor(), System.currentTimeMillis() + (2 * 24 * 3600 * 1000L), -1);
	}
}
