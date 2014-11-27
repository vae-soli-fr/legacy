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
package handlers.admincommands;

import java.util.List;

import lineage2.gameserver.Announcements;
import lineage2.gameserver.handlers.AdminCommandHandler;
import lineage2.gameserver.handlers.IAdminCommandHandler;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminAnnouncements implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_list_announcements",
		"admin_announce_announcements",
		"admin_add_announcement",
		"admin_del_announcement",
		"admin_announce",
		"admin_a",
		"admin_announce_menu",
		"admin_crit_announce",
		"admin_c",
		"admin_toscreen",
		"admin_s",
		"admin_reload_announcements"
	};
	
	/**
	 * Method useAdminCommand.
	 * @param command String
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean
	 * @see lineage2.gameserver.handlers.IAdminCommandHandler#useAdminCommand(String, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(String command, String[] wordList, String fullString, Player activeChar)
	{
		if (!activeChar.getPlayerAccess().CanAnnounce)
		{
			return false;
		}
		
		switch (command)
		{
			case "admin_list_announcements":
				listAnnouncements(activeChar);
				break;
			
			case "admin_announce_menu":
				if ((fullString.length() > 20) && (fullString.length() <= 3020))
				{
					Announcements.getInstance().announceToAll(fullString.substring(20));
				}
				listAnnouncements(activeChar);
				break;
			
			case "admin_announce_announcements":
				for (Player player : GameObjectsStorage.getAllPlayersForIterate())
				{
					Announcements.getInstance().showAnnouncements(player);
				}
				
				listAnnouncements(activeChar);
				break;
			
			case "admin_add_announcement":
				if (wordList.length < 3)
				{
					return false;
				}
				
				try
				{
					int time = Integer.parseInt(wordList[1]);
					StringBuilder builder = new StringBuilder();
					
					for (int i = 2; i < wordList.length; i++)
					{
						builder.append(' ').append(wordList[i]);
					}
					
					Announcements.getInstance().addAnnouncement(time, builder.toString(), true);
					listAnnouncements(activeChar);
				}
				catch (Exception e)
				{
					// empty catch clause
				}
				break;
			
			case "admin_del_announcement":
				if (wordList.length != 2)
				{
					return false;
				}
				
				int val = Integer.parseInt(wordList[1]);
				Announcements.getInstance().delAnnouncement(val);
				listAnnouncements(activeChar);
				break;
			
			case "admin_announce":
				Announcements.getInstance().announceToAll(fullString.substring(15));
				break;
			
			case "dmin_a":
				Announcements.getInstance().announceToAll(fullString.substring(8));
				break;
			
			case "admin_crit_announce":
			case "admin_c":
				if (wordList.length < 2)
				{
					return false;
				}
				
				Announcements.getInstance().announceToAll(activeChar.getName() + ": " + fullString.replaceFirst("admin_crit_announce ", "").replaceFirst("admin_c ", ""), ChatType.CRITICAL_ANNOUNCE);
				break;
			
			case "admin_toscreen":
			case "admin_s":
				if (wordList.length < 2)
				{
					return false;
				}
				
				String text = activeChar.getName() + ": " + fullString.replaceFirst("admin_toscreen ", "").replaceFirst("admin_s ", "");
				int time = 3000 + (text.length() * 100);
				ExShowScreenMessage sm = new ExShowScreenMessage(text, time, ScreenMessageAlign.TOP_CENTER, text.length() < 64);
				
				for (Player player : GameObjectsStorage.getAllPlayersForIterate())
				{
					player.sendPacket(sm);
				}
				break;
			
			case "admin_reload_announcements":
				Announcements.getInstance().loadAnnouncements();
				listAnnouncements(activeChar);
				activeChar.sendMessage("Announcements reloaded.");
				break;
		}
		
		return true;
	}
	
	/**
	 * Method listAnnouncements.
	 * @param activeChar Player
	 */
	private void listAnnouncements(Player activeChar)
	{
		List<Announcements.Announce> announcements = Announcements.getInstance().getAnnouncements();
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuilder replyMSG = new StringBuilder("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=45 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=180><center>Announcements Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_admin\" width=45 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<br><br>");
		replyMSG.append("<center>New announcement (1-3000 characters)</center>");
		replyMSG.append("<center><multiedit var=\"new_announcement\" width=260 height=80></center><br>");
		replyMSG.append("<center>Time (in seconds, 0 for permanent)<edit var=\"time\" width=45 height=18></center><br>");
		replyMSG.append("<center><table><tr><td>");
		replyMSG.append("<button value=\"Add\" action=\"bypass -h admin_add_announcement $time $new_announcement\" width=60 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Announce\" action=\"bypass -h admin_announce_menu $new_announcement\" width=64 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Reload\" action=\"bypass -h admin_reload_announcements\" width=60 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td><td>");
		replyMSG.append("<button value=\"Broadcast\" action=\"bypass -h admin_announce_announcements\" width=70 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">");
		replyMSG.append("</td></tr></table></center>");
		replyMSG.append("<br>");
		
		for (int i = 0; i < announcements.size(); i++)
		{
			Announcements.Announce announce = announcements.get(i);
			replyMSG.append("<table width=260><tr><td width=180>" + announce.getAnnounce() + "</td><td width=40>" + announce.getTime() + "</td><<td width=40>");
			replyMSG.append("<button value=\"Delete\" action=\"bypass -h admin_del_announcement " + i + "\" width=60 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr></table>");
		}
		
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return String[]
	 * @see lineage2.gameserver.handlers.IAdminCommandHandler#getAdminCommandList()
	 */
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
}
