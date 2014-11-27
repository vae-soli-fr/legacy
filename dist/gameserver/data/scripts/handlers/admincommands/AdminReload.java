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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.dao.OlympiadNobleDAO;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.BuyListHolder;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.data.xml.holder.ProductHolder;
import lineage2.gameserver.data.xml.parser.NpcParser;
import lineage2.gameserver.handlers.AdminCommandHandler;
import lineage2.gameserver.handlers.IAdminCommandHandler;
import lineage2.gameserver.instancemanager.SpawnManager;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.olympiad.OlympiadDatabase;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.FishTable;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminReload implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_reload",
		"admin_reload_config",
		"admin_reload_multisell",
		"admin_reload_gmaccess",
		"admin_reload_htm",
		"admin_reload_qs",
		"admin_reload_qs_help",
		"admin_reload_skills",
		"admin_reload_npc",
		"admin_reload_spawn",
		"admin_reload_fish",
		"admin_reload_abuse",
		"admin_reload_shops",
		"admin_reload_static",
		"admin_reload_pets",
		"admin_reload_nobles",
		"admin_reload_im"
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
			case "admin_reload":
				break;
			
			case "admin_reload_config":
			{
				try
				{
					Config.load();
				}
				catch (Exception e)
				{
					activeChar.sendMessage("Error: " + e.getMessage() + "!");
					return false;
				}
				
				activeChar.sendMessage("Config reloaded!");
				break;
			}
			
			case "admin_reload_multisell":
			{
				try
				{
					MultiSellHolder.getInstance().reload();
				}
				catch (Exception e)
				{
					return false;
				}
				
				activeChar.sendMessage("Multisell list reloaded!");
				break;
			}
			
			case "admin_reload_gmaccess":
			{
				try
				{
					Config.loadPlayerAccess();
					
					for (Player player : GameObjectsStorage.getAllPlayersForIterate())
					{
						if (Config.EVERYONE_HAS_ADMIN_RIGHTS)
						{
							player.setAccessLevel(Config.DEFAULT_ACCESS_FOR_EVERYONE);
						}
						else
						{
							player.setAccessLevel(player.getAccessLevel());
						}
					}
				}
				catch (Exception e)
				{
					return false;
				}
				
				activeChar.sendMessage("GMAccess reloaded!");
				break;
			}
			
			case "admin_reload_htm":
			{
				HtmCache.getInstance().clear();
				if (Config.HTM_CACHE_MODE == 2)
				{
					HtmCache.getInstance().reload();
				}
				activeChar.sendMessage("HtmCache reloaded!");
				break;
			}
			
			case "admin_reload_qs":
			{
				if (fullString.endsWith("all"))
				{
					for (Player p : GameObjectsStorage.getAllPlayersForIterate())
					{
						reloadQuestStates(p);
					}
				}
				else
				{
					GameObject t = activeChar.getTarget();
					
					if ((t != null) && t.isPlayer())
					{
						Player p = (Player) t;
						reloadQuestStates(p);
					}
					else
					{
						reloadQuestStates(activeChar);
					}
				}
				break;
			}
			
			case "admin_reload_qs_help":
			{
				activeChar.sendMessage("");
				activeChar.sendMessage("Quest Help:");
				activeChar.sendMessage("reload_qs_help - This Message.");
				activeChar.sendMessage("reload_qs <selected target> - reload all quest states for target.");
				activeChar.sendMessage("reload_qs <no target or target is not player> - reload quests for self.");
				activeChar.sendMessage("reload_qs all - reload quests for all players in world.");
				activeChar.sendMessage("");
				break;
			}
			
			case "admin_reload_skills":
			{
				SkillTable.getInstance().reload();
				break;
			}
			
			case "admin_reload_npc":
			{
				NpcParser.getInstance().reload();
				break;
			}
			
			case "admin_reload_spawn":
			{
				ThreadPoolManager.getInstance().execute(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						SpawnManager.getInstance().reloadAll();
					}
				});
				break;
			}
			
			case "admin_reload_fish":
			{
				FishTable.getInstance().reload();
				break;
			}
			
			case "admin_reload_abuse":
			{
				Config.abuseLoad();
				break;
			}
			
			case "admin_reload_shops":
			{
				BuyListHolder.reload();
				break;
			}
			
			case "admin_reload_static":
			{
				break;
			}
			
			case "admin_reload_pets":
			{
				PetDataTable.getInstance().reload();
				break;
			}
			
			case "admin_reload_nobles":
			{
				OlympiadNobleDAO.getInstance().select();
				OlympiadDatabase.loadNoblesRank();
				break;
			}
			
			case "admin_reload_im":
			{
				ProductHolder.getInstance().reload();
				break;
			}
		}
		
		activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/reload.htm"));
		return true;
	}
	
	/**
	 * Method reloadQuestStates.
	 * @param p Player
	 */
	private void reloadQuestStates(Player p)
	{
		for (QuestState qs : p.getAllQuestsStates())
		{
			p.removeQuestState(qs.getQuest().getName());
		}
		
		Quest.restoreQuestStates(p);
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
