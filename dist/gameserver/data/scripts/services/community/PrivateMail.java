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
package services.community;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.handlers.CommunityBoardManager;
import lineage2.gameserver.handlers.ICommunityBoardHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExMailArrived;
import lineage2.gameserver.network.serverpackets.ShowBoard;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class PrivateMail extends Functions implements ScriptFile, ICommunityBoardHandler
{
	private static final Logger _log = LoggerFactory.getLogger(PrivateMail.class);
	private static final int MESSAGE_PER_PAGE = 10;
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		if (Config.COMMUNITYBOARD_ENABLED)
		{
			_log.info("CommunityBoard: Private Mail service loaded.");
			CommunityBoardManager.getInstance().registerHandler(this);
		}
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		if (Config.COMMUNITYBOARD_ENABLED)
		{
			CommunityBoardManager.getInstance().removeHandler(this);
		}
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
	
	/**
	 * Method getBypassCommands.
	 * @return String[] * @see lineage2.gameserver.handlers.bbs.ICommunityBoardHandler#getBypassCommands()
	 */
	@Override
	public String[] getBypassCommands()
	{
		return new String[]
		{
			"_maillist_",
			"_mailsearch_",
			"_mailread_",
			"_maildelete_"
		};
	}
	
	/**
	 * Method onBypassCommand.
	 * @param player Player
	 * @param bypass String
	 * @see lineage2.gameserver.handlers.ICommunityBoardHandler#onBypassCommand(Player, String)
	 */
	@Override
	public void onBypassCommand(Player player, String bypass)
	{
		StringTokenizer st = new StringTokenizer(bypass, "_");
		String cmd = st.nextToken();
		player.setSessionVar("add_fav", null);
		
		if ("maillist".equals(cmd))
		{
			int type = Integer.parseInt(st.nextToken());
			int page = Integer.parseInt(st.nextToken());
			int byTitle = Integer.parseInt(st.nextToken());
			String search = st.hasMoreTokens() ? st.nextToken() : "";
			String html = HtmCache.getInstance().getNotNull("scripts/services/community/bbs_mail_list.htm", player);
			int inbox = 0;
			int send = 0;
			
			try (Connection con = DatabaseFactory.getInstance().getConnection();)
			{
				PreparedStatement statement = con.prepareStatement("SELECT count(*) as cnt FROM `bbs_mail` WHERE `box_type` = 0 and `to_object_id` = ?");
				
				statement.setInt(1, player.getObjectId());
				ResultSet rset = statement.executeQuery();
				
				if (rset.next())
				{
					inbox = rset.getInt("cnt");
				}
				rset.close();
				statement.close();
				
				try (PreparedStatement statement2 = con.prepareStatement("SELECT count(*) as cnt FROM `bbs_mail` WHERE `box_type` = 1 and `from_object_id` = ?");)
				{
					statement2.setInt(1, player.getObjectId());
					rset = statement2.executeQuery();
					
					if (rset.next())
					{
						send = rset.getInt("cnt");
					}
					rset.close();
					statement2.close();
				}
			}
			catch (Exception e)
			{
				// empty catch clause
			}
			
			List<MailData> mailList = null;
			
			switch (type)
			{
				case 0:
					html = html.replace("%inbox_link%", "[&$917;]");
					html = html.replace("%sentbox_link%", "<a action=\"bypass _maillist_1_1_0_\">[&$918;]</a>");
					html = html.replace("%archive_link%", "<a action=\"bypass _maillist_2_1_0_\">[&$919;]</a>");
					html = html.replace("%temp_archive_link%", "<a action=\"bypass _maillist_3_1_0_\">[&$920;]</a>");
					html = html.replace("%TREE%", "&$917;");
					html = html.replace("%writer_header%", "&$911;");
					mailList = getMailList(player, type, search, byTitle == 1);
					break;
				
				case 1:
					html = html.replace("%inbox_link%", "<a action=\"bypass _maillist_0_1_0_\">[&$917;]</a>");
					html = html.replace("%sentbox_link%", "[&$918;]");
					html = html.replace("%archive_link%", "<a action=\"bypass _maillist_2_1_0_\">[&$919;]</a>");
					html = html.replace("%temp_archive_link%", "<a action=\"bypass _maillist_3_1_0_\">[&$920;]</a>");
					html = html.replace("%TREE%", "&$918;");
					html = html.replace("%writer_header%", "&$909;");
					mailList = getMailList(player, type, search, byTitle == 1);
					break;
				
				case 2:
					html = html.replace("%inbox_link%", "<a action=\"bypass _maillist_0_1_0_\">[&$917;]</a>");
					html = html.replace("%sentbox_link%", "<a action=\"bypass _maillist_1_1_0_\">[&$918;]</a>");
					html = html.replace("%archive_link%", "[&$919;]");
					html = html.replace("%temp_archive_link%", "<a action=\"bypass _maillist_3_1_0_\">[&$920;]</a>");
					html = html.replace("%TREE%", "&$919;");
					html = html.replace("%writer_header%", "&$911;");
					break;
				
				case 3:
					html = html.replace("%inbox_link%", "<a action=\"bypass _maillist_0_1_0_\">[&$917;]</a>");
					html = html.replace("%sentbox_link%", "<a action=\"bypass _maillist_1_1_0_\">[&$918;]</a>");
					html = html.replace("%archive_link%", "<a action=\"bypass _maillist_2_1_0_\">[&$919;]</a>");
					html = html.replace("%temp_archive_link%", "[&$920;]");
					html = html.replace("%TREE%", "&$920;");
					html = html.replace("%writer_header%", "&$909;");
					break;
			}
			
			if (mailList != null)
			{
				int start = (page - 1) * MESSAGE_PER_PAGE;
				int end = Math.min(page * MESSAGE_PER_PAGE, mailList.size());
				
				if (page == 1)
				{
					html = html.replace("%ACTION_GO_LEFT%", "");
					html = html.replace("%GO_LIST%", "");
					html = html.replace("%NPAGE%", "1");
				}
				else
				{
					html = html.replace("%ACTION_GO_LEFT%", "bypass _maillist_" + type + "_" + (page - 1) + "_" + byTitle + "_" + search);
					html = html.replace("%NPAGE%", String.valueOf(page));
					StringBuilder goList = new StringBuilder("");
					
					for (int i = page > 10 ? page - 10 : 1; i < page; i++)
					{
						goList.append("<td><a action=\"bypass _maillist_").append(type).append('_').append(i).append('_').append(byTitle).append('_').append(search).append("\"> ").append(i).append(" </a> </td>\n\n");
					}
					
					html = html.replace("%GO_LIST%", goList.toString());
				}
				
				int pages = Math.max(mailList.size() / MESSAGE_PER_PAGE, 1);
				
				if (mailList.size() > (pages * MESSAGE_PER_PAGE))
				{
					pages++;
				}
				
				if (pages > page)
				{
					html = html.replace("%ACTION_GO_RIGHT%", "bypass _maillist_" + type + "_" + (page + 1) + "_" + byTitle + "_" + search);
					int ep = Math.min(page + 10, pages);
					StringBuilder goList = new StringBuilder("");
					
					for (int i = page + 1; i <= ep; i++)
					{
						goList.append("<td><a action=\"bypass _maillist_").append(type).append('_').append(i).append('_').append(byTitle).append('_').append(search).append("\"> ").append(i).append(" </a> </td>\n\n");
					}
					
					html = html.replace("%GO_LIST2%", goList.toString());
				}
				else
				{
					html = html.replace("%ACTION_GO_RIGHT%", "");
					html = html.replace("%GO_LIST2%", "");
				}
				
				StringBuilder ml = new StringBuilder("");
				String tpl = HtmCache.getInstance().getNotNull("scripts/services/community/bbs_mailtpl.htm", player);
				
				for (int i = start; i < end; i++)
				{
					MailData md = mailList.get(i);
					String mailtpl = tpl;
					mailtpl = mailtpl.replace("%action%", "bypass _mailread_" + md.messageId + "_" + type + "_" + page + "_" + byTitle + "_" + search);
					mailtpl = mailtpl.replace("%writer%", md.author);
					mailtpl = mailtpl.replace("%title%", md.title);
					mailtpl = mailtpl.replace("%post_date%", md.postDate);
					ml.append(mailtpl);
				}
				
				html = html.replace("%MAIL_LIST%", ml.toString());
			}
			else
			{
				html = html.replace("%ACTION_GO_LEFT%", "");
				html = html.replace("%GO_LIST%", "");
				html = html.replace("%NPAGE%", "1");
				html = html.replace("%GO_LIST2%", "");
				html = html.replace("%ACTION_GO_RIGHT%", "");
				html = html.replace("%MAIL_LIST%", "");
			}
			
			html = html.replace("%mailbox_type%", String.valueOf(type));
			html = html.replace("%incomming_mail_no%", String.valueOf(inbox));
			html = html.replace("%sent_mail_no%", String.valueOf(send));
			html = html.replace("%archived_mail_no%", "0");
			html = html.replace("%temp_mail_no%", "0");
			ShowBoard.separateAndSend(html, player);
		}
		else if ("mailread".equals(cmd))
		{
			int messageId = Integer.parseInt(st.nextToken());
			int type = Integer.parseInt(st.nextToken());
			int page = Integer.parseInt(st.nextToken());
			int byTitle = Integer.parseInt(st.nextToken());
			String search = st.hasMoreTokens() ? st.nextToken() : "";
			
			try (Connection con = DatabaseFactory.getInstance().getConnection();)
			{
				
				PreparedStatement statement = con.prepareStatement("SELECT * FROM `bbs_mail` WHERE `message_id` = ? and `box_type` = ? and `to_object_id` = ?");
				statement.setInt(1, messageId);
				statement.setInt(2, type);
				statement.setInt(3, player.getObjectId());
				ResultSet rset = statement.executeQuery();
				
				if (rset.next())
				{
					String html = HtmCache.getInstance().getNotNull("scripts/services/community/bbs_mail_read.htm", player);
					
					switch (type)
					{
						case 0:
							html = html.replace("%TREE%", "<a action=\"bypass _maillist_0_1_0_\">&$917;</a>");
							break;
						
						case 1:
							html = html.replace("%TREE%", "<a action=\"bypass _maillist_1_1_0__\">&$918;</a>");
							break;
						
						case 2:
							html = html.replace("%TREE%", "<a action=\"bypass _maillist_2_1_0__\">&$919;</a>");
							break;
						
						case 3:
							html = html.replace("%TREE%", "<a action=\"bypass _maillist_3_1_0__\">&$920;</a>");
							break;
					}
					
					html = html.replace("%writer%", rset.getString("from_name"));
					html = html.replace("%post_date%", String.format("%1$te-%1$tm-%1$tY", new Date(rset.getInt("post_date") * 1000L)));
					html = html.replace("%del_date%", String.format("%1$te-%1$tm-%1$tY", new Date((rset.getInt("post_date") + (90 * 24 * 60 * 60)) * 1000L)));
					html = html.replace("%char_name%", rset.getString("to_name"));
					html = html.replace("%title%", rset.getString("title"));
					html = html.replace("%CONTENT%", rset.getString("message").replace("\n", "<br1>"));
					html = html.replace("%GOTO_LIST_LINK%", "bypass _maillist_" + type + "_" + page + "_" + byTitle + "_" + search);
					html = html.replace("%message_id%", String.valueOf(messageId));
					html = html.replace("%mailbox_type%", String.valueOf(type));
					player.setSessionVar("add_fav", bypass + "&" + rset.getString("title"));
					
					statement.close();
					statement = con.prepareStatement("UPDATE `bbs_mail` SET `read` = `read` + 1 WHERE message_id = ?");
					statement.setInt(1, messageId);
					statement.execute();
					statement.close();
					ShowBoard.separateAndSend(html, player);
					return;
				}
				rset.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			onBypassCommand(player, "_maillist_" + type + "_" + page + "_" + byTitle + "_" + search);
		}
		else if ("maildelete".equals(cmd))
		{
			int type = Integer.parseInt(st.nextToken());
			int messageId = Integer.parseInt(st.nextToken());
			
			try (Connection con = DatabaseFactory.getInstance().getConnection();)
			{
				
				PreparedStatement statement = con.prepareStatement("DELETE FROM `bbs_mail` WHERE `box_type` = ? and `message_id` = ? and `to_object_id` = ?");
				statement.setInt(1, type);
				statement.setInt(2, messageId);
				statement.setInt(3, player.getObjectId());
				statement.execute();
				statement.close();
			}
			catch (Exception e)
			{
				// empty catch clause
			}
			
			onBypassCommand(player, "_maillist_" + type + "_1_0_");
		}
	}
	
	/**
	 * Method onWriteCommand.
	 * @param player Player
	 * @param bypass String
	 * @param arg1 String
	 * @param arg2 String
	 * @param arg3 String
	 * @param arg4 String
	 * @param arg5 String
	 * @see lineage2.gameserver.handlers.ICommunityBoardHandler#onWriteCommand(Player, String, String, String, String, String, String)
	 */
	@Override
	public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
		StringTokenizer st = new StringTokenizer(bypass, "_");
		String cmd = st.nextToken();
		
		if ("mailsearch".equals(cmd))
		{
			onBypassCommand(player, "_maillist_" + st.nextToken() + "_1_" + ("Title".equals(arg3) ? "1_" : "0_") + (arg5 != null ? arg5 : ""));
		}
	}
	
	/**
	 * Method OnPlayerEnter.
	 * @param player Player
	 */
	public static void OnPlayerEnter(Player player)
	{
		
		try (Connection con = DatabaseFactory.getInstance().getConnection();)
		{
			
			PreparedStatement statement = con.prepareStatement("SELECT * FROM `bbs_mail` WHERE `box_type` = 0 and `read` = 0 and `to_object_id` = ?");
			statement.setInt(1, player.getObjectId());
			ResultSet rset = statement.executeQuery();
			
			if (rset.next())
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOUVE_GOT_MAIL));
				player.sendPacket(ExMailArrived.STATIC);
			}
			rset.close();
			statement.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method getMailList.
	 * @param player Player
	 * @param type int
	 * @param search String
	 * @param byTitle boolean
	 * @return List<MailData>
	 */
	private static List<MailData> getMailList(Player player, int type, String search, boolean byTitle)
	{
		List<MailData> list = new ArrayList<>();
		
		try (Connection con = DatabaseFactory.getInstance().getConnection();)
		{
			
			PreparedStatement statement = con.prepareStatement("DELETE FROM `bbs_mail` WHERE `to_object_id` = ? and `post_date` < ?");
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, (int) (System.currentTimeMillis() / 1000) - (90 * 24 * 60 * 60));
			statement.execute();
			statement.close();
			String column_name = type == 0 ? "from_name" : "to_name";
			statement = con.prepareStatement("SELECT * FROM `bbs_mail` WHERE `box_type` = ? and `to_object_id` = ? ORDER BY post_date DESC");
			statement.setInt(1, type);
			statement.setInt(2, player.getObjectId());
			ResultSet rset = statement.executeQuery();
			
			while (rset.next())
			{
				if (search.isEmpty())
				{
					list.add(new MailData(rset.getString(column_name), rset.getString("title"), rset.getInt("post_date"), rset.getInt("message_id")));
				}
				else if (byTitle && !search.isEmpty() && rset.getString("title").toLowerCase().contains(search.toLowerCase()))
				{
					list.add(new MailData(rset.getString(column_name), rset.getString("title"), rset.getInt("post_date"), rset.getInt("message_id")));
				}
				else if (!byTitle && !search.isEmpty() && rset.getString(column_name).toLowerCase().contains(search.toLowerCase()))
				{
					list.add(new MailData(rset.getString(column_name), rset.getString("title"), rset.getInt("post_date"), rset.getInt("message_id")));
				}
			}
			rset.close();
			statement.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * @author Mobius
	 */
	private static class MailData
	{
		public String author;
		public String title;
		public String postDate;
		public int messageId;
		
		/**
		 * Constructor for MailData.
		 * @param _author String
		 * @param _title String
		 * @param _postDate int
		 * @param _messageId int
		 */
		public MailData(String _author, String _title, int _postDate, int _messageId)
		{
			author = _author;
			title = _title;
			postDate = String.format(String.format("%1$te-%1$tm-%1$tY", new Date(_postDate * 1000L)));
			messageId = _messageId;
		}
	}
}
