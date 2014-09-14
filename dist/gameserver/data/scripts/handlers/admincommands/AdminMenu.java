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

import java.util.StringTokenizer;

import lineage2.gameserver.handlers.AdminCommandHandler;
import lineage2.gameserver.handlers.IAdminCommandHandler;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.AdminFunctions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminMenu implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_char_manage",
		"admin_teleport_character_to_menu",
		"admin_recall_char_menu",
		"admin_goto_char_menu",
		"admin_kick_menu",
		"admin_kill_menu",
		"admin_ban_menu",
		"admin_unban_menu"
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
		if (!activeChar.getPlayerAccess().Menu)
		{
			return false;
		}
		
		if (fullString.startsWith("admin_teleport_character_to_menu"))
		{
			String[] data = fullString.split(" ");
			
			if (data.length == 5)
			{
				String playerName = data[1];
				Player player = World.getPlayer(playerName);
				
				if (player != null)
				{
					teleportCharacter(player, new Location(Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4])), activeChar);
				}
			}
		}
		else if (fullString.startsWith("admin_recall_char_menu"))
		{
			try
			{
				String targetName = fullString.substring(23);
				Player player = World.getPlayer(targetName);
				teleportCharacter(player, activeChar.getLoc(), activeChar);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				// empty catch clause
			}
		}
		else if (fullString.startsWith("admin_goto_char_menu"))
		{
			try
			{
				String targetName = fullString.substring(21);
				Player player = World.getPlayer(targetName);
				teleportToCharacter(activeChar, player);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				// empty catch clause
			}
		}
		else if (fullString.equals("admin_kill_menu"))
		{
			GameObject obj = activeChar.getTarget();
			StringTokenizer st = new StringTokenizer(fullString);
			
			if (st.countTokens() > 1)
			{
				st.nextToken();
				String player = st.nextToken();
				Player plyr = World.getPlayer(player);
				
				if (plyr == null)
				{
					activeChar.sendMessage("Player " + player + " not found in game.");
				}
				
				obj = plyr;
			}
			
			if ((obj != null) && obj.isCreature())
			{
				Creature target = (Creature) obj;
				target.reduceCurrentHp(target.getMaxHp() + 1, 0, activeChar, null, true, true, true, false, false, false, true);
			}
			else
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			}
		}
		else if (fullString.startsWith("admin_kick_menu"))
		{
			StringTokenizer st = new StringTokenizer(fullString);
			
			if (st.countTokens() > 1)
			{
				st.nextToken();
				String player = st.nextToken();
				
				if (AdminFunctions.kick(player, "kick"))
				{
					activeChar.sendMessage("Player kicked.");
				}
			}
		}
		
		activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/charmanage.htm"));
		return true;
	}
	
	/**
	 * Method teleportCharacter.
	 * @param player Player
	 * @param loc Location
	 * @param activeChar Player
	 */
	private void teleportCharacter(Player player, Location loc, Player activeChar)
	{
		if (player != null)
		{
			player.sendMessage("Admin is teleporting you.");
			player.teleToLocation(loc);
		}
	}
	
	/**
	 * Method teleportToCharacter.
	 * @param activeChar Player
	 * @param target GameObject
	 */
	private void teleportToCharacter(Player activeChar, GameObject target)
	{
		Player player;
		
		if ((target != null) && target.isPlayer())
		{
			player = (Player) target;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return;
		}
		
		if (player.getObjectId() == activeChar.getObjectId())
		{
			activeChar.sendMessage("You cannot self teleport.");
		}
		else
		{
			activeChar.teleToLocation(player.getLoc());
			activeChar.sendMessage("You have teleported to character " + player.getName() + ".");
		}
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
