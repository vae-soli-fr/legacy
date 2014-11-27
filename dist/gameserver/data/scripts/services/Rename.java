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
package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lineage2.gameserver.Config;
import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SubClass;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.base.SubClassType;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.SubUnit;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.tables.SubClassTable;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class Rename extends Functions
{
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(Rename.class);
	
	/**
	 * Method rename_page.
	 */
	public void rename_page()
	{
		Player player = getSelf();
		
		if (player == null)
		{
			return;
		}
		
		String append = "!Rename";
		append += "<br>";
		append += "<font color=\"LEVEL\">Rename character for " + Util.formatAdena(Config.SERVICES_CHANGE_NICK_PRICE) + " " + Config.SERVICES_CHANGE_NICK_ITEM + ".</font>";
		append += "<table>";
		append += "<tr><td>New name: <edit var=\"new_name\" width=80></td></tr>";
		append += "<tr><td></td></tr>";
		append += "<tr><td><button value=\"Rename\" action=\"bypass -h scripts_services.Rename:rename $new_name\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";
		append += "</table>";
		show(append, player);
	}
	
	/**
	 * Method changesex_page.
	 */
	public void changesex_page()
	{
		Player player = getSelf();
		
		if (player == null)
		{
			return;
		}
		
		if (!player.isInPeaceZone())
		{
			show("You must be in peace zone to use this service.", player);
			return;
		}
		
		String append = "!Change Character Sex";
		append += "<br>";
		append += "<font color=\"LEVEL\">" + "Change character sex for " + Util.formatAdena(Config.SERVICES_CHANGE_SEX_PRICE) + " " + Config.SERVICES_CHANGE_SEX_ITEM + ". Not available for Kamael.</font>";
		append += "<table>";
		append += "<tr><td><button value=\"Change\" action=\"bypass -h scripts_services.Rename:changesex\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";
		append += "</table>";
		show(append, player);
	}
	
	/**
	 * Method separate_page.
	 */
	public void separate_page()
	{
		Player player = getSelf();
		
		if (player == null)
		{
			return;
		}
		
		if (player.isHero())
		{
			show("Not available for heroes.", player);
			return;
		}
		
		if (player.getSubClassList().size() == 1)
		{
			show("You must have at least 1 subclass.", player);
			return;
		}
		
		if (!player.isBaseClassActive())
		{
			show("You must be at main class.", player);
			return;
		}
		
		if (player.getActiveSubClass().getLevel() < 75)
		{
			show("You must be at least level 75.", player);
			return;
		}
		
		String append = "!Seperate Subclass";
		append += "<br>";
		append += "<font color=\"LEVEL\">" + "Separate subclass for " + Util.formatAdena(Config.SERVICES_SEPARATE_SUB_PRICE) + " " + Config.SERVICES_SEPARATE_SUB_ITEM + ". Inspector and Judicator can't be separated. You must specify name of existing level 1 character from same account:</font>&nbsp;";
		append += "<edit var=\"name\" width=80 height=15 /><br>";
		append += "<table>";
		
		for (SubClass s : player.getSubClassList().values())
		{
			if (!s.isBase() && (s.getClassId() != ClassId.INSPECTOR.getId()) && (s.getClassId() != ClassId.JUDICATOR.getId()))
			{
				append += "<tr><td><button value=\"" + "Separate " + ClassId.VALUES[s.getClassId()].toString() + "\" action=\"bypass -h scripts_services.Rename:separate " + s.getClassId() + " $name\" width=200 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";
			}
		}
		
		append += "</table>";
		show(append, player);
	}
	
	/**
	 * Method separate.
	 * @param param String[]
	 */
	public void separate(String[] param)
	{
		Player player = getSelf();
		
		if (player == null)
		{
			return;
		}
		
		if (player.isHero())
		{
			show("Not available for heroes.", player);
			return;
		}
		
		if (player.getSubClassList().size() == 1)
		{
			show("You must have at least 1 subclass.", player);
			return;
		}
		
		if (!player.getActiveSubClass().isBase())
		{
			show("You must be on main class.", player);
			return;
		}
		
		if (player.getActiveSubClass().getLevel() < 75)
		{
			show("You must be at least level 75.", player);
			return;
		}
		
		if (param.length < 2)
		{
			show("You must specify target.", player);
			return;
		}
		
		if (getItemCount(player, Config.SERVICES_SEPARATE_SUB_ITEM) < Config.SERVICES_SEPARATE_SUB_PRICE)
		{
			if (Config.SERVICES_SEPARATE_SUB_ITEM == 57)
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
			
			return;
		}
		
		int classtomove = Integer.parseInt(param[0]);
		int newcharid = 0;
		
		for (Entry<Integer, String> e : player.getAccountChars().entrySet())
		{
			if (e.getValue().equals(param[1]))
			{
				newcharid = e.getKey();
			}
		}
		
		if (newcharid == 0)
		{
			show("Target not exists.", player);
			return;
		}
		
		if (mysql.simple_get_int("level", "character_subclasses", "char_obj_id=" + newcharid + " AND level > 1") > 1)
		{
			show("Target must be level 1.", player);
			return;
		}
		
		mysql.set("DELETE FROM character_subclasses WHERE char_obj_id=" + newcharid);
		mysql.set("DELETE FROM character_skills WHERE char_obj_id=" + newcharid);
		mysql.set("DELETE FROM character_skills_save WHERE char_obj_id=" + newcharid);
		mysql.set("DELETE FROM character_effects_save WHERE object_id=" + newcharid);
		mysql.set("DELETE FROM character_hennas WHERE char_obj_id=" + newcharid);
		mysql.set("DELETE FROM character_shortcuts WHERE char_obj_id=" + newcharid);
		mysql.set("DELETE FROM character_variables WHERE obj_id=" + newcharid);
		mysql.set("UPDATE character_subclasses SET char_obj_id=" + newcharid + ", isBase=1, certification=0 WHERE char_obj_id=" + player.getObjectId() + " AND class_id=" + classtomove);
		mysql.set("UPDATE character_skills SET char_obj_id=" + newcharid + " WHERE char_obj_id=" + player.getObjectId() + " AND class_index=" + classtomove);
		mysql.set("UPDATE character_skills_save SET char_obj_id=" + newcharid + " WHERE char_obj_id=" + player.getObjectId() + " AND class_index=" + classtomove);
		mysql.set("UPDATE character_effects_save SET object_id=" + newcharid + " WHERE object_id=" + player.getObjectId() + " AND id=" + classtomove);
		mysql.set("UPDATE character_hennas SET char_obj_id=" + newcharid + " WHERE char_obj_id=" + player.getObjectId() + " AND class_index=" + classtomove);
		mysql.set("UPDATE character_shortcuts SET char_obj_id=" + newcharid + " WHERE char_obj_id=" + player.getObjectId() + " AND class_index=" + classtomove);
		mysql.set("UPDATE character_variables SET obj_id=" + newcharid + " WHERE obj_id=" + player.getObjectId() + " AND name like 'TransferSkills%'");
		player.modifySubClass(classtomove, 0, false);
		removeItem(player, Config.SERVICES_CHANGE_BASE_ITEM, Config.SERVICES_CHANGE_BASE_PRICE);
		player.logout();
	}
	
	/**
	 * Method changebase_page.
	 */
	public void changebase_page()
	{
		Player player = getSelf();
		
		if (player == null)
		{
			return;
		}
		
		if (!player.isInPeaceZone())
		{
			show("You must be in a peace zone to use this service.", player);
			return;
		}
		
		if (player.isHero())
		{
			sendMessage("Not available for heroes.", player);
			return;
		}
		
		String append = "!Change Base Class";
		append += "<br>";
		append += "<font color=\"LEVEL\">Change character base class " + Util.formatAdena(Config.SERVICES_CHANGE_BASE_PRICE) + " " + Config.SERVICES_CHANGE_BASE_ITEM + ". Not available for some classes.</font>";
		append += "<table>";
		List<SubClass> possible = new ArrayList<>();
		
		if (player.getActiveSubClass().isBase())
		{
			possible.addAll(player.getSubClassList().values());
			possible.remove(player.getSubClassList().getByClassId(player.getBaseClassId()));
			
			for (SubClass s : player.getSubClassList().values())
			{
				for (SubClass s2 : player.getSubClassList().values())
				{
					if (((s != s2) && !SubClassTable.areClassesComportable(ClassId.VALUES[s.getClassId()], ClassId.VALUES[s2.getClassId()], false)) || (s2.getLevel() < 75))
					{
						possible.remove(s2);
					}
				}
			}
		}
		
		if (possible.isEmpty())
		{
			append += "<tr><td width=300>Not available.</td></tr>";
		}
		else
		{
			for (SubClass s : possible)
			{
				append += "<tr><td><button value=\"" + "Change to " + ClassId.VALUES[s.getClassId()].toString() + "\" action=\"bypass -h scripts_services.Rename:changebase " + s.getClassId() + "\" width=200 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";
			}
		}
		
		append += "</table>";
		show(append, player);
	}
	
	/**
	 * Method changebase.
	 * @param param String[]
	 */
	public void changebase(String[] param)
	{
		Player player = getSelf();
		
		if (player == null)
		{
			return;
		}
		
		if (!player.isInPeaceZone())
		{
			show("You must be in a peace zone to use this service.", player);
			return;
		}
		
		if (!player.getActiveSubClass().isBase())
		{
			show("You must be on your main class to use this service.", player);
			return;
		}
		
		if (player.isHero())
		{
			show("Not available for heroes.", player);
			return;
		}
		
		if (getItemCount(player, Config.SERVICES_CHANGE_BASE_ITEM) < Config.SERVICES_CHANGE_BASE_PRICE)
		{
			if (Config.SERVICES_CHANGE_BASE_ITEM == 57)
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
			
			return;
		}
		
		int target = Integer.parseInt(param[0]);
		SubClass newBase = player.getSubClassList().getByClassId(target);
		player.getActiveSubClass().setType(SubClassType.SUBCLASS);
		player.getActiveSubClass().setCertification(newBase.getCertification());
		newBase.setCertification(0);
		player.getActiveSubClass().setExp(player.getExp(), false);
		player.checkSkills();
		newBase.setType(SubClassType.BASE_CLASS);
		player.setHairColor(0);
		player.setHairStyle(0);
		player.setFace(0);
		Olympiad.unRegisterNoble(player);
		removeItem(player, Config.SERVICES_CHANGE_BASE_ITEM, Config.SERVICES_CHANGE_BASE_PRICE);
		player.logout();
	}
	
	/**
	 * Method rename.
	 * @param args String[]
	 */
	public void rename(String[] args)
	{
		Player player = getSelf();
		
		if (player == null)
		{
			return;
		}
		
		if (player.isHero())
		{
			sendMessage("Not available for heroes.", player);
			return;
		}
		
		if (args.length != 1)
		{
			show("Incorrect input.", player);
			return;
		}
		
		if (player.getEvent(SiegeEvent.class) != null)
		{
			show("Can't do that because siege in progress.", player);
			return;
		}
		
		String name = args[0];
		
		if (!Util.isMatchingRegexp(name, Config.CNAME_TEMPLATE))
		{
			show("Incorrect input.", player);
			return;
		}
		
		if (getItemCount(player, Config.SERVICES_CHANGE_NICK_ITEM) < Config.SERVICES_CHANGE_NICK_PRICE)
		{
			if (Config.SERVICES_CHANGE_NICK_ITEM == 57)
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_DO_NOT_HAVE_ENOUGH_ADENA));
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
			
			return;
		}
		
		if (CharacterDAO.getInstance().getObjectIdByName(name) > 0)
		{
			show("This name already exists.", player);
			return;
		}
		
		removeItem(player, Config.SERVICES_CHANGE_NICK_ITEM, Config.SERVICES_CHANGE_NICK_PRICE);
		String oldName = player.getName();
		player.reName(name, true);
		Log.add("Character " + oldName + " renamed to " + name, "renames");
		show("You changed name from " + oldName + " to " + name, player);
	}
	
	/**
	 * Method changesex.
	 */
	public void changesex()
	{
		Player player = getSelf();
		
		if (player == null)
		{
			return;
		}
		
		if (player.getRace() == Race.kamael)
		{
			show("Not available for Kamael.", player);
			return;
		}
		
		if (!player.isInPeaceZone())
		{
			show("You must be in a peace zone to use this service.", player);
			return;
		}
		
		if (getItemCount(player, Config.SERVICES_CHANGE_SEX_ITEM) < Config.SERVICES_CHANGE_SEX_PRICE)
		{
			if (Config.SERVICES_CHANGE_SEX_ITEM == 57)
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_DO_NOT_HAVE_ENOUGH_ADENA));
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
			
			return;
		}
		
		player.setHairColor(0);
		player.setHairStyle(0);
		player.setFace(0);
		removeItem(player, Config.SERVICES_CHANGE_SEX_ITEM, Config.SERVICES_CHANGE_SEX_PRICE);
		Log.add("Character " + player + " sex changed to " + (player.getSex() == 1 ? "male" : "female"), "renames");
		int sex = player.getSex() == 1 ? 0 : 1;
		int ObjectId = player.getObjectId();
		player.logout();
		
		try (Connection con = DatabaseFactory.getInstance().getConnection();)
		{
			
			PreparedStatement offline = con.prepareStatement("UPDATE characters SET sex = ? WHERE obj_Id = ?");
			offline.setInt(1, sex);
			offline.setInt(2, ObjectId);
			offline.executeUpdate();
			offline.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			show("Error.", player);
			return;
		}
		
	}
	
	/**
	 * Method rename_clan_page.
	 */
	public void rename_clan_page()
	{
		Player player = getSelf();
		
		if (player == null)
		{
			return;
		}
		
		if ((player.getClan() == null) || !player.isClanLeader())
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_IS_NOT_A_CLAN_LEADER).addName(player));
			return;
		}
		
		String append = "!Rename Clan";
		append += "<br>";
		append += "<font color=\"LEVEL\">Rename character for " + Util.formatAdena(Config.SERVICES_CHANGE_CLAN_NAME_PRICE) + " " + Config.SERVICES_CHANGE_CLAN_NAME_ITEM + ".</font>";
		append += "<table>";
		append += "<tr><td>New name: <edit var=\"new_name\" width=80></td></tr>";
		append += "<tr><td></td></tr>";
		append += "<tr><td><button value=\"Rename\" action=\"bypass -h scripts_services.Rename:rename_clan $new_name\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";
		append += "</table>";
		show(append, player);
	}
	
	/**
	 * Method rename_clan.
	 * @param param String[]
	 */
	public void rename_clan(String[] param)
	{
		Player player = getSelf();
		
		if ((player == null) || (param == null) || (param.length == 0))
		{
			return;
		}
		
		if ((player.getClan() == null) || !player.isClanLeader())
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_IS_NOT_A_CLAN_LEADER).addName(player));
			return;
		}
		
		if (player.getEvent(SiegeEvent.class) != null)
		{
			show("Can't do that because siege in progress.", player);
			return;
		}
		
		if (!Util.isMatchingRegexp(param[0], Config.CLAN_NAME_TEMPLATE))
		{
			player.sendPacket(new SystemMessage(SystemMessage.CLAN_NAME_IS_INCORRECT));
			return;
		}
		
		if (ClanTable.getInstance().getClanByName(param[0]) != null)
		{
			player.sendPacket(new SystemMessage(SystemMessage.THIS_NAME_ALREADY_EXISTS));
			return;
		}
		
		if (getItemCount(player, Config.SERVICES_CHANGE_CLAN_NAME_ITEM) < Config.SERVICES_CHANGE_CLAN_NAME_PRICE)
		{
			if (Config.SERVICES_CHANGE_CLAN_NAME_ITEM == 57)
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_DO_NOT_HAVE_ENOUGH_ADENA));
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
			
			return;
		}
		
		show("You changed name from " + param[0] + " to " + player.getClan().getName(), player);
		SubUnit sub = player.getClan().getSubUnit(Clan.SUBUNIT_MAIN_CLAN);
		sub.setName(param[0], true);
		removeItem(player, Config.SERVICES_CHANGE_CLAN_NAME_ITEM, Config.SERVICES_CHANGE_CLAN_NAME_PRICE);
		player.getClan().broadcastClanStatus(true, true, false);
	}
}
