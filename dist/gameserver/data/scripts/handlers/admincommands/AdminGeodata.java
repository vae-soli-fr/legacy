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

import lineage2.gameserver.Config;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.handlers.AdminCommandHandler;
import lineage2.gameserver.handlers.IAdminCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminGeodata implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_geo_z",
		"admin_geo_type",
		"admin_geo_nswe",
		"admin_geo_los",
		"admin_geo_load",
		"admin_geo_dump",
		"admin_geo_trace",
		"admin_geo_map"
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
		if (!activeChar.getPlayerAccess().CanReload)
		{
			return false;
		}
		
		switch (command)
		{
			case "admin_geo_z":
				activeChar.sendMessage("GeoEngine: Geo_Z = " + GeoEngine.getHeight(activeChar.getLoc(), activeChar.getReflectionId()) + " Loc_Z = " + activeChar.getZ());
				break;
			
			case "admin_geo_type":
				int type = GeoEngine.getType(activeChar.getX(), activeChar.getY(), activeChar.getReflectionId());
				activeChar.sendMessage("GeoEngine: Geo_Type = " + type);
				break;
			
			case "admin_geo_nswe":
				String result = "";
				byte nswe = GeoEngine.getNSWE(activeChar.getX(), activeChar.getY(), activeChar.getZ(), activeChar.getReflectionId());
				
				if ((nswe & 8) == 0)
				{
					result += " N";
				}
				
				if ((nswe & 4) == 0)
				{
					result += " S";
				}
				
				if ((nswe & 2) == 0)
				{
					result += " W";
				}
				
				if ((nswe & 1) == 0)
				{
					result += " E";
				}
				
				activeChar.sendMessage("GeoEngine: Geo_NSWE -> " + nswe + "->" + result);
				break;
			
			case "admin_geo_los":
				if (activeChar.getTarget() != null)
				{
					if (GeoEngine.canSeeTarget(activeChar, activeChar.getTarget(), false))
					{
						activeChar.sendMessage("GeoEngine: Can See Target");
					}
					else
					{
						activeChar.sendMessage("GeoEngine: Can't See Target");
					}
				}
				else
				{
					activeChar.sendMessage("None Target!");
				}
				break;
			
			case "admin_geo_load":
				if (wordList.length != 3)
				{
					activeChar.sendMessage("Usage: //geo_load <regionX> <regionY>");
				}
				else
				{
					try
					{
						byte rx = Byte.parseByte(wordList[1]);
						byte ry = Byte.parseByte(wordList[2]);
						
						if (GeoEngine.LoadGeodataFile(rx, ry))
						{
							GeoEngine.LoadGeodata(rx, ry, 0);
							activeChar.sendMessage("GeoEngine: Region [" + rx + "," + ry + "] loaded.");
						}
						else
						{
							activeChar.sendMessage("GeoEngine: Region [" + rx + "," + ry + "] not loaded.");
						}
					}
					catch (Exception e)
					{
						activeChar.sendMessage("Error.");
					}
				}
				break;
			
			case "admin_geo_dump":
				if (wordList.length > 2)
				{
					GeoEngine.DumpGeodataFileMap(Byte.parseByte(wordList[1]), Byte.parseByte(wordList[2]));
					activeChar.sendMessage("Geo square saved " + wordList[1] + "_" + wordList[2]);
				}
				
				GeoEngine.DumpGeodataFile(activeChar.getX(), activeChar.getY());
				activeChar.sendMessage("Actual geo square saved.");
				break;
			
			case "admin_geo_trace":
				if (wordList.length < 2)
				{
					activeChar.sendMessage("Usage: //geo_trace on|off");
					return false;
				}
				
				if (wordList[1].equals("on"))
				{
					activeChar.setVar("trace", "1", -1);
				}
				else if (wordList[1].equals("off"))
				{
					activeChar.unsetVar("trace");
				}
				else
				{
					activeChar.sendMessage("Usage: //geo_trace on|off");
				}
				break;
			
			case "admin_geo_map":
				int x = ((activeChar.getX() - World.MAP_MIN_X) >> 15) + Config.GEO_X_FIRST;
				int y = ((activeChar.getY() - World.MAP_MIN_Y) >> 15) + Config.GEO_Y_FIRST;
				activeChar.sendMessage("GeoMap: " + x + "_" + y);
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