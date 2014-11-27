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

import java.util.List;

import lineage2.gameserver.model.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowBoard extends L2GameServerPacket
{
	private static final Logger _log = LoggerFactory.getLogger(ShowBoard.class);
	private String _htmlCode;
	private String _id;
	private List<String> _arg;
	private String _addFav = "";
	
	public static void separateAndSend(String html, Player player)
	{
		if (html.length() < 8180)
		{
			player.sendPacket(new ShowBoard(html, "101", player));
			player.sendPacket(new ShowBoard(null, "102", player));
			player.sendPacket(new ShowBoard(null, "103", player));
		}
		else if (html.length() < (8180 * 2))
		{
			player.sendPacket(new ShowBoard(html.substring(0, 8180), "101", player));
			player.sendPacket(new ShowBoard(html.substring(8180, html.length()), "102", player));
			player.sendPacket(new ShowBoard(null, "103", player));
		}
		else if (html.length() < (8180 * 3))
		{
			player.sendPacket(new ShowBoard(html.substring(0, 8180), "101", player));
			player.sendPacket(new ShowBoard(html.substring(8180, 8180 * 2), "102", player));
			player.sendPacket(new ShowBoard(html.substring(8180 * 2, html.length()), "103", player));
		}
	}
	
	public ShowBoard(String htmlCode, String id, Player player)
	{
		if ((htmlCode != null) && (htmlCode.length() > 8192)) // html code must not
		// exceed 8192 bytes
		{
			_log.warn("Html '" + htmlCode + "' is too long! this will crash the client!");
			_htmlCode = "<html><body>Html was too long</body></html>";
			return;
		}
		
		_id = id;
		
		if (player.getSessionVar("add_fav") != null)
		{
			_addFav = "bypass _bbsaddfav_List";
		}
		
		if (htmlCode != null)
		{
			if (id.equals("101"))
			{
				player.cleanBypasses(true);
			}
			
			_htmlCode = player.encodeBypasses(htmlCode, true);
		}
		else
		{
			_htmlCode = null;
		}
	}
	
	public ShowBoard(List<String> arg)
	{
		_id = "1002";
		_htmlCode = null;
		_arg = arg;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x7b);
		writeC(0x01); // c4 1 to show community 00 to hide
		writeS("bypass _bbshome");
		writeS("bypass _bbsgetfav");
		writeS("bypass _bbsloc");
		writeS("bypass _bbsclan");
		writeS("bypass _bbsmemo");
		writeS("bypass _maillist_0_1_0_");
		writeS("bypass _friendlist_0_");
		writeS(_addFav);
		String str = _id + "\u0008";
		
		if (!_id.equals("1002"))
		{
			if (_htmlCode != null)
			{
				str += _htmlCode;
			}
		}
		else
		{
			for (String arg : _arg)
			{
				str += arg + " \u0008";
			}
		}
		
		writeS(str);
	}
}