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

import lineage2.gameserver.handlers.AdminCommandHandler;
import lineage2.gameserver.handlers.IAdminCommandHandler;
import lineage2.gameserver.instancemanager.HellboundManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminHellbound implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_hbadd",
		"admin_hbsub",
		"admin_hbset",
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
		
		switch (command)
		{
			case "admin_hbadd":
				HellboundManager.addConfidence(Long.parseLong(wordList[1]));
				activeChar.sendMessage("Added " + Integer.valueOf(wordList[1]) + " to Hellbound confidence");
				activeChar.sendMessage("Hellbound confidence is now " + HellboundManager.getConfidence());
				break;
			
			case "admin_hbsub":
				HellboundManager.reduceConfidence(Long.parseLong(wordList[1]));
				activeChar.sendMessage("Reduced confidence by " + Integer.valueOf(wordList[1]));
				activeChar.sendMessage("Hellbound confidence is now " + HellboundManager.getConfidence());
				break;
			
			case "admin_hbset":
				HellboundManager.setConfidence(Long.parseLong(wordList[1]));
				activeChar.sendMessage("Hellbound confidence is now " + HellboundManager.getConfidence());
				break;
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
