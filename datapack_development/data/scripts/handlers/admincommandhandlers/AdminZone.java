/*
 * $Header: AdminTest.java, 25/07/2005 17:15:21 luisantonioa Exp $
 *
 * $Author: luisantonioa $
 * $Date: 25/07/2005 17:15:21 $
 * $Revision: 1 $
 * $Log: AdminTest.java,v $
 * Revision 1  25/07/2005 17:15:21  luisantonioa
 * Added copyright notice
 *
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import com.l2jserver.Config;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.datatables.MapRegionTable;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.instancemanager.ZoneManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.L2WorldRegion;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;


public class AdminZone implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_zone_check",
		"admin_zone_reload"
	};
	
	/**
	 * 
	 * @see com.l2jserver.gameserver.handler.IAdminCommandHandler#useAdminCommand(java.lang.String, com.l2jserver.gameserver.model.actor.instance.L2PcInstance)
	 */
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (activeChar == null)
			return false;
		
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken(); // Get actual command
		
		//String val = "";
		//if (st.countTokens() >= 1) {val = st.nextToken();}
		
		if (actualCommand.equalsIgnoreCase("admin_zone_check"))
		{
			final String htmContent = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/admin/zone.htm");
			NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			adminReply.setHtml(htmContent);
			adminReply.replace("%PEACE%", (activeChar.isInsideZone(L2Character.ZONE_PEACE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%PVP%", (activeChar.isInsideZone(L2Character.ZONE_PVP) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%SIEGE%", (activeChar.isInsideZone(L2Character.ZONE_SIEGE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%TOWN%", (activeChar.isInsideZone(L2Character.ZONE_TOWN) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%CASTLE%", (activeChar.isInsideZone(L2Character.ZONE_CASTLE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%FORT%", (activeChar.isInsideZone(L2Character.ZONE_FORT) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%NOHQ%", (activeChar.isInsideZone(L2Character.ZONE_NOHQ) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%CLANHALL%", (activeChar.isInsideZone(L2Character.ZONE_CLANHALL) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%LAND%", (activeChar.isInsideZone(L2Character.ZONE_LANDING) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%NOLAND%", (activeChar.isInsideZone(L2Character.ZONE_NOLANDING) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%NOSUMMON%", (activeChar.isInsideZone(L2Character.ZONE_NOSUMMONFRIEND) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%WATER%", (activeChar.isInsideZone(L2Character.ZONE_WATER) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%SWAMP%", (activeChar.isInsideZone(L2Character.ZONE_SWAMP) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%DANGER%", (activeChar.isInsideZone(L2Character.ZONE_DANGERAREA) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%NOSTORE%", (activeChar.isInsideZone(L2Character.ZONE_NOSTORE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			adminReply.replace("%SCRIPT%", (activeChar.isInsideZone(L2Character.ZONE_SCRIPT) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
			String _zones = "";
			L2WorldRegion region = L2World.getInstance().getRegion(activeChar.getX(), activeChar.getY());
			for (L2ZoneType zone : region.getZones())
			{
				if(zone.isCharacterInZone(activeChar))
				{
					_zones = _zones + zone.getId() + " ";
				}
			}
			adminReply.replace("%ZLIST%", _zones);
			activeChar.sendPacket(adminReply);
			
			activeChar.sendMessage("MapRegion: x:" + MapRegionTable.getInstance().getMapRegionX(activeChar.getX()) + " y:" + MapRegionTable.getInstance().getMapRegionX(activeChar.getY()) + " ("+MapRegionTable.getInstance().getMapRegion(activeChar.getX(),activeChar.getY())+")");
			activeChar.sendMessage("GeoRegion: " + getGeoOffset(activeChar.getX(),activeChar.getY()) );
			activeChar.sendMessage("Closest Town: " + MapRegionTable.getInstance().getClosestTownName(activeChar));
			
			Location loc;
			
			loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.Castle);
			activeChar.sendMessage("TeleToLocation (Castle): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
			
			loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.ClanHall);
			activeChar.sendMessage("TeleToLocation (ClanHall): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
			
			loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.SiegeFlag);
			activeChar.sendMessage("TeleToLocation (SiegeFlag): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
			
			loc = MapRegionTable.getInstance().getTeleToLocation(activeChar, MapRegionTable.TeleportWhereType.Town);
			activeChar.sendMessage("TeleToLocation (Town): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
		}
		else if (actualCommand.equalsIgnoreCase("admin_zone_reload"))
		{
			ZoneManager.getInstance().reload();
			activeChar.sendMessage("All Zones have been reloaded");
		}
		return true;
	}
	
	public short getGeoOffset(int x, int y)
	{
		int rx = x >> 11; // =/(256 * 8)
		int ry = y >> 11;
		return (short) (((rx + Config.WORLD_X_MIN) << 5) + (ry + Config.WORLD_Y_MIN));
	}
	/**
	 * 
	 * @see com.l2jserver.gameserver.handler.IAdminCommandHandler#getAdminCommandList()
	 */
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
}
