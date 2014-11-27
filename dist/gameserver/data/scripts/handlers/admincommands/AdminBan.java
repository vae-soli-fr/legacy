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

import java.util.Collection;
import java.util.StringTokenizer;

import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.handlers.AdminCommandHandler;
import lineage2.gameserver.handlers.IAdminCommandHandler;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.items.ManufactureItem;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.loginservercon.LoginServerCommunication;
import lineage2.gameserver.network.loginservercon.gspackets.ChangeAccessLevel;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.AdminFunctions;
import lineage2.gameserver.utils.AutoBan;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminBan implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_ban",
		"admin_unban",
		"admin_cban",
		"admin_chatban",
		"admin_chatunban",
		"admin_accban",
		"admin_accunban",
		"admin_trade_ban",
		"admin_trade_unban",
		"admin_jail",
		"admin_unjail",
		"admin_permaban"
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
		StringTokenizer st = new StringTokenizer(fullString);
		
		if (activeChar.getPlayerAccess().CanTradeBanUnban)
		{
			switch (command)
			{
				case "admin_trade_ban":
					return tradeBan(st, activeChar);
					
				case "admin_trade_unban":
					return tradeUnban(st, activeChar);
					
				default:
					break;
			}
		}
		
		if (activeChar.getPlayerAccess().CanBan)
		{
			switch (command)
			{
				case "admin_ban":
					ban(st, activeChar);
					break;
				
				case "admin_accban":
				{
					st.nextToken();
					int level = 0;
					int banExpire = 0;
					String account = st.nextToken();
					
					if (st.hasMoreTokens())
					{
						banExpire = (int) (System.currentTimeMillis() / 1000L) + (Integer.parseInt(st.nextToken()) * 60);
					}
					else
					{
						level = -100;
					}
					
					LoginServerCommunication.getInstance().sendPacket(new ChangeAccessLevel(account, level, banExpire));
					GameClient client = LoginServerCommunication.getInstance().getAuthedClient(account);
					
					if (client != null)
					{
						Player player = client.getActiveChar();
						
						if (player != null)
						{
							player.kick();
							activeChar.sendMessage("Player " + player.getName() + " kicked.");
						}
					}
					break;
				}
				
				case "admin_accunban":
				{
					st.nextToken();
					String account = st.nextToken();
					LoginServerCommunication.getInstance().sendPacket(new ChangeAccessLevel(account, 0, 0));
					break;
				}
				
				case "admin_trade_ban":
					return tradeBan(st, activeChar);
					
				case "admin_trade_unban":
					return tradeUnban(st, activeChar);
					
				case "admin_chatban":
					try
					{
						st.nextToken();
						String player = st.nextToken();
						String period = st.nextToken();
						String bmsg = "admin_chatban " + player + " " + period + " ";
						String msg = fullString.substring(bmsg.length(), fullString.length());
						
						if (AutoBan.ChatBan(player, Integer.parseInt(period), msg, activeChar.getName()))
						{
							activeChar.sendMessage("You ban chat for " + player + ".");
						}
						else
						{
							activeChar.sendMessage("Can't find char " + player + ".");
						}
					}
					catch (Exception e)
					{
						activeChar.sendMessage("Command syntax: //chatban char_name period reason");
					}
					break;
				
				case "admin_chatunban":
					try
					{
						st.nextToken();
						String player = st.nextToken();
						
						if (AutoBan.ChatUnBan(player, activeChar.getName()))
						{
							activeChar.sendMessage("You unban chat for " + player + ".");
						}
						else
						{
							activeChar.sendMessage("Can't find char " + player + ".");
						}
					}
					catch (Exception e)
					{
						activeChar.sendMessage("Command syntax: //chatunban char_name");
					}
					break;
				
				case "admin_jail":
					try
					{
						st.nextToken();
						String player = st.nextToken();
						String period = st.nextToken();
						String reason = st.nextToken();
						Player target = World.getPlayer(player);
						
						if (target != null)
						{
							target.setVar("jailedFrom", target.getX() + ";" + target.getY() + ";" + target.getZ() + ";" + target.getReflectionId(), -1);
							target.setVar("jailed", period, -1);
							target.startUnjailTask(target, Integer.parseInt(period));
							target.teleToLocation(Location.findPointToStay(target, AdminFunctions.JAIL_SPAWN, 50, 200), ReflectionManager.JAIL);
							
							if (activeChar.isInStoreMode())
							{
								activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
							}
							
							target.sitDown(null);
							target.block();
							target.sendMessage("You moved to jail, time to escape - " + period + " minutes, reason - " + reason + " .");
							activeChar.sendMessage("You jailed " + player + ".");
						}
						else
						{
							activeChar.sendMessage("Can't find char " + player + ".");
						}
					}
					catch (Exception e)
					{
						activeChar.sendMessage("Command syntax: //jail char_name period reason");
					}
					break;
				
				case "admin_unjail":
					try
					{
						st.nextToken();
						String player = st.nextToken();
						Player target = World.getPlayer(player);
						
						if ((target != null) && (target.getVar("jailed") != null))
						{
							String[] re = target.getVar("jailedFrom").split(";");
							target.teleToLocation(Integer.parseInt(re[0]), Integer.parseInt(re[1]), Integer.parseInt(re[2]));
							target.setReflection(re.length > 3 ? Integer.parseInt(re[3]) : 0);
							target.stopUnjailTask();
							target.unsetVar("jailedFrom");
							target.unsetVar("jailed");
							target.unblock();
							target.standUp();
							activeChar.sendMessage("You unjailed " + player + ".");
						}
						else
						{
							activeChar.sendMessage("Can't find char " + player + ".");
						}
					}
					catch (Exception e)
					{
						activeChar.sendMessage("Command syntax: //unjail char_name");
					}
					break;
				
				case "admin_cban":
					activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/cban.htm"));
					break;
				
				case "admin_permaban":
					if ((activeChar.getTarget() == null) || !activeChar.getTarget().isPlayer())
					{
						Functions.sendDebugMessage(activeChar, "Target should be set and be a player instance");
						return false;
					}
					
					Player banned = activeChar.getTarget().getPlayer();
					String banaccount = banned.getAccountName();
					LoginServerCommunication.getInstance().sendPacket(new ChangeAccessLevel(banaccount, -100, 0));
					
					if (banned.isInOfflineMode())
					{
						banned.setOfflineMode(false);
					}
					
					banned.kick();
					Functions.sendDebugMessage(activeChar, "Player account " + banaccount + " is banned, player " + banned.getName() + " kicked.");
					break;
				
				default:
					activeChar.sendMessage("Command isn't implemented yet.");
					break;
			}
		}
		
		return true;
	}
	
	/**
	 * Method tradeBan.
	 * @param st StringTokenizer
	 * @param activeChar Player
	 * @return boolean
	 */
	private boolean tradeBan(StringTokenizer st, Player activeChar)
	{
		if ((activeChar.getTarget() == null) || !activeChar.getTarget().isPlayer())
		{
			return false;
		}
		
		st.nextToken();
		Player targ = (Player) activeChar.getTarget();
		long days = -1;
		long time = -1;
		
		if (st.hasMoreTokens())
		{
			days = Long.parseLong(st.nextToken());
			time = (days * 24 * 60 * 60 * 1000L) + System.currentTimeMillis();
		}
		
		targ.setVar("tradeBan", String.valueOf(time), -1);
		String msg = activeChar.getName() + " заблокировал торговл�? пер�?онажу " + targ.getName() + (days == -1 ? " на бе�?�?рочный период." : " на " + days + " дней.");
		Log.add(targ.getName() + ":" + days + tradeToString(targ, targ.getPrivateStoreType()), "tradeBan", activeChar);
		
		if (targ.isInOfflineMode())
		{
			targ.setOfflineMode(false);
			targ.kick();
		}
		else if (targ.isInStoreMode())
		{
			targ.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
			targ.standUp();
			targ.broadcastCharInfo();
			targ.getBuyList().clear();
		}
		
		if (Config.BANCHAT_ANNOUNCE_FOR_ALL_WORLD)
		{
			Announcements.getInstance().announceToAll(msg);
		}
		else
		{
			Announcements.shout(activeChar, msg, ChatType.CRITICAL_ANNOUNCE);
		}
		
		return true;
	}
	
	/**
	 * Method tradeToString.
	 * @param targ Player
	 * @param trade int
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	private static String tradeToString(Player targ, int trade)
	{
		String ret;
		Collection<?> list;
		
		switch (trade)
		{
			case Player.STORE_PRIVATE_BUY:
				list = targ.getBuyList();
				
				if ((list == null) || list.isEmpty())
				{
					return "";
				}
				
				ret = ":buy:";
				
				for (TradeItem i : (Collection<TradeItem>) list)
				{
					ret += i.getId() + ";" + i.getCount() + ";" + i.getOwnersPrice() + ":";
				}
				
				return ret;
				
			case Player.STORE_PRIVATE_SELL:
			case Player.STORE_PRIVATE_SELL_PACKAGE:
				list = targ.getSellList();
				
				if ((list == null) || list.isEmpty())
				{
					return "";
				}
				
				ret = ":sell:";
				
				for (TradeItem i : (Collection<TradeItem>) list)
				{
					ret += i.getId() + ";" + i.getCount() + ";" + i.getOwnersPrice() + ":";
				}
				
				return ret;
				
			case Player.STORE_PRIVATE_MANUFACTURE:
				list = targ.getCreateList();
				
				if ((list == null) || list.isEmpty())
				{
					return "";
				}
				
				ret = ":mf:";
				
				for (ManufactureItem i : (Collection<ManufactureItem>) list)
				{
					ret += i.getRecipeId() + ";" + i.getCost() + ":";
				}
				
				return ret;
				
			default:
				return "";
		}
	}
	
	/**
	 * Method tradeUnban.
	 * @param st StringTokenizer
	 * @param activeChar Player
	 * @return boolean
	 */
	private boolean tradeUnban(StringTokenizer st, Player activeChar)
	{
		if ((activeChar.getTarget() == null) || !activeChar.getTarget().isPlayer())
		{
			return false;
		}
		
		Player targ = (Player) activeChar.getTarget();
		targ.unsetVar("tradeBan");
		
		if (Config.BANCHAT_ANNOUNCE_FOR_ALL_WORLD)
		{
			Announcements.getInstance().announceToAll(activeChar + " has banned " + targ + "from chat.");
		}
		else
		{
			Announcements.shout(activeChar, activeChar + " has banned " + targ + "from chat.", ChatType.CRITICAL_ANNOUNCE);
		}
		
		Log.add(activeChar + " has banned " + targ + "from chat.", "tradeBan", activeChar);
		return true;
	}
	
	/**
	 * Method ban.
	 * @param st StringTokenizer
	 * @param activeChar Player
	 * @return boolean
	 */
	private boolean ban(StringTokenizer st, Player activeChar)
	{
		try
		{
			st.nextToken();
			String player = st.nextToken();
			int time = 0;
			String msg = "";
			
			if (st.hasMoreTokens())
			{
				time = Integer.parseInt(st.nextToken());
			}
			
			if (st.hasMoreTokens())
			{
				msg = "admin_ban " + player + " " + time + " ";
				
				while (st.hasMoreTokens())
				{
					msg += st.nextToken() + " ";
				}
				
				msg.trim();
			}
			
			Player plyr = World.getPlayer(player);
			
			if (plyr != null)
			{
				plyr.sendMessage("You are banned by a GM.");
				plyr.setAccessLevel(-100);
				AutoBan.Banned(plyr, time, msg, activeChar.getName());
				plyr.kick();
				activeChar.sendMessage("You banned " + plyr.getName());
			}
			else if (AutoBan.Banned(player, -100, time, msg, activeChar.getName()))
			{
				activeChar.sendMessage("You banned " + player);
			}
			else
			{
				activeChar.sendMessage("Can't find char: " + player);
			}
		}
		catch (Exception e)
		{
			activeChar.sendMessage("Command syntax: //ban char_name days reason");
		}
		
		return true;
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
