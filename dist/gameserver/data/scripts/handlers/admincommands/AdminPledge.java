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

import lineage2.gameserver.Config;
import lineage2.gameserver.handlers.AdminCommandHandler;
import lineage2.gameserver.handlers.IAdminCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.VillageMasterInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.SubUnit;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import lineage2.gameserver.network.serverpackets.PledgeStatusChanged;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminPledge implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_pledge"
	};
	
	/**
	 * Method useAdminCommand.
	 * @param command String
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean
	 */
	@Override
	public boolean useAdminCommand(String command, String[] wordList, String fullString, Player activeChar)
	{
		if ((activeChar.getPlayerAccess() == null) || !activeChar.getPlayerAccess().CanEditPledge || (activeChar.getTarget() == null) || !activeChar.getTarget().isPlayer())
		{
			return false;
		}
		
		Player target = (Player) activeChar.getTarget();
		
		if (fullString.startsWith("admin_pledge"))
		{
			StringTokenizer st = new StringTokenizer(fullString);
			st.nextToken();
			String action = st.nextToken();
			
			if (action.equals("create"))
			{
				try
				{
					if (target == null)
					{
						activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
						return false;
					}
					
					if (target.getPlayer().getLevel() < 10)
					{
						activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_ARE_NOT_QUALIFIED_TO_CREATE_A_CLAN));
						return false;
					}
					
					String pledgeName = st.nextToken();
					
					if (pledgeName.length() > 16)
					{
						activeChar.sendPacket(new SystemMessage(SystemMessage.CLAN_NAMES_LENGTH_IS_INCORRECT));
						return false;
					}
					
					if (!Util.isMatchingRegexp(pledgeName, Config.CLAN_NAME_TEMPLATE))
					{
						activeChar.sendPacket(new SystemMessage(SystemMessage.CLAN_NAME_IS_INCORRECT));
						return false;
					}
					
					Clan clan = ClanTable.getInstance().createClan(target, pledgeName);
					
					if (clan != null)
					{
						target.sendPacket(clan.listAll());
						target.sendPacket(new PledgeShowInfoUpdate(clan), new SystemMessage(SystemMessage.CLAN_HAS_BEEN_CREATED));
						target.updatePledgeClass();
						target.sendUserInfo();
						return true;
					}
					
					activeChar.sendPacket(new SystemMessage(SystemMessage.THIS_NAME_ALREADY_EXISTS));
					return false;
				}
				catch (Exception e)
				{
					// empty catch clause
				}
			}
			else if (action.equals("setlevel"))
			{
				if (target.getClan() == null)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
					return false;
				}
				
				try
				{
					int level = Integer.parseInt(st.nextToken());
					Clan clan = target.getClan();
					activeChar.sendMessage("You set level " + level + " for clan " + clan.getName());
					clan.setLevel(level);
					clan.updateClanInDB();
					
					if (level == 5)
					{
						target.sendPacket(new SystemMessage(SystemMessage.NOW_THAT_YOUR_CLAN_LEVEL_IS_ABOVE_LEVEL_5_IT_CAN_ACCUMULATE_CLAN_REPUTATION_POINTS));
					}
					
					PledgeShowInfoUpdate pu = new PledgeShowInfoUpdate(clan);
					PledgeStatusChanged ps = new PledgeStatusChanged(clan);
					
					for (Player member : clan.getOnlineMembers(0))
					{
						member.updatePledgeClass();
						member.sendPacket(new SystemMessage(SystemMessage.CLANS_SKILL_LEVEL_HAS_INCREASED), pu, ps);
						member.broadcastUserInfo();
					}
					
					return true;
				}
				catch (Exception e)
				{
					// empty catch clause
				}
			}
			else if (action.equals("resetcreate"))
			{
				if (target.getClan() == null)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
					return false;
				}
				
				target.getClan().setExpelledMemberTime(0);
				activeChar.sendMessage("The penalty for creating a clan has been lifted for " + target.getName());
			}
			else if (action.equals("resetwait"))
			{
				target.setLeaveClanTime(0);
				activeChar.sendMessage("The penalty for leaving a clan has been lifted for " + target.getName());
			}
			else if (action.equals("addrep"))
			{
				try
				{
					int rep = Integer.parseInt(st.nextToken());
					
					if ((target.getClan() == null) || (target.getClan().getLevel() < 5))
					{
						activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
						return false;
					}
					
					target.getClan().incReputation(rep, false, "admin_manual");
					activeChar.sendMessage("Added " + rep + " clan points to clan " + target.getClan().getName() + ".");
				}
				catch (NumberFormatException nfe)
				{
					activeChar.sendMessage("Please specify a number of clan points to add.");
				}
			}
			else if (action.equals("setleader"))
			{
				Clan clan = target.getClan();
				
				if (target.getClan() == null)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
					return false;
				}
				
				String newLeaderName = null;
				
				if (st.hasMoreTokens())
				{
					newLeaderName = st.nextToken();
				}
				else
				{
					newLeaderName = target.getName();
				}
				
				SubUnit mainUnit = clan.getSubUnit(Clan.SUBUNIT_MAIN_CLAN);
				UnitMember newLeader = mainUnit.getUnitMember(newLeaderName);
				
				if (newLeader == null)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
					return false;
				}
				
				VillageMasterInstance.setLeader(activeChar, clan, mainUnit, newLeader);
			}
		}
		
		return false;
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
