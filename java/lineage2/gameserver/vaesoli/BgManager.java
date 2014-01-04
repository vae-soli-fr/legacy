package lineage2.gameserver.vaesoli;

import gnu.trove.list.array.TIntArrayList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.utils.AdminFunctions;
import lineage2.gameserver.utils.Location;

/**
 * @author Melua Cette classe vérifie que les BG sont à jour et validés
 *         concernant les transfos, les subs, et bientot les BG tout court.
 * 
 */
public class BgManager {

	private static final Logger _log = LoggerFactory.getLogger(BgManager.class);
	private static BgManager _instance;
	private final TIntArrayList _transforms; // liste des skills de transfo
	private final static String JAIL_IN = "<html><title>Prison</title><body><br>Vous avez été mis en prison :<br>Vérifiez que vos BG sont à jours concernant<br1>les subs et les transformations !!<br>Faites votre mise à jour et contactez gentillement un Conseiller ;)</body></html>";
	private final static String JAIL_OUT = "<html><title>Prison</title><body><br>Vous êtes libre.<br>Vos mises à jours BG ont été validées.<br>Bravo.</body></html>";
	private final static int JAIL_PERIOD = 604800;
	private final static String JAILED_VAR = "BgJailed";
	private final static String JAILEDFROM_VAR = "BgJailedFrom";

	public static BgManager getInstance() {
		if (_instance == null)
			_instance = new BgManager();
		return _instance;
	}

	private BgManager() {
		int skillId[] = {
		/*
		 * Interdites
		 */
		663, // Transform Zaken
		665, // Transform Benom
		666, // Transform Gordon
		664, // Transform Anakim
		668, // Transform Kiyachi
		667 // Transform Ranku
		};
		_transforms = new TIntArrayList(skillId.length);
		_transforms.add(skillId);
		_log.info("Backgrounds Manager: Loaded. " + _transforms.size() + " transform skills to check.");
	}

	private boolean isGuilty(Player player) {
		Connection con = null;
		boolean guilty1 = false;
		boolean guilty2 = false;
		try {
			con = DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement1 = con.prepareStatement("SELECT level, verified FROM character_subclasses WHERE charId = ?");
			PreparedStatement statement2 = con
					.prepareStatement("SELECT skill_id, SUM(verified) as verified FROM character_skills WHERE charId = ? GROUP BY skill_id");
			statement1.setInt(1, player.getObjectId());
			statement2.setInt(1, player.getObjectId());
			ResultSet rset1 = statement1.executeQuery();
			ResultSet rset2 = statement2.executeQuery();

			while (rset1.next()) {
				if (rset1.getInt("verified") == 0 && rset1.getInt("level") > 40) {
					guilty1 = true;
					continue;
				}
			}

			while (rset2.next()) {
				if (_transforms.contains(rset2.getInt("skill_id")) && rset2.getInt("verified") == 0) {
					guilty2 = true;
					continue;
				}
			}

			rset1.close();
			rset2.close();
			statement1.close();
			statement2.close();
		} catch (Exception e) {
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return (guilty1 || guilty2);
	}

	public void check(Player activeChar) {
		if (activeChar.isGM()) {
			return;
		}
		if (isGuilty(activeChar)) {
			if (activeChar.getVar(JAILED_VAR) != null) {
				// Just open a Html message to inform the player
				NpcHtmlMessage htmlMsg = new NpcHtmlMessage(0);
				htmlMsg.setHtml(JAIL_IN);
				activeChar.sendPacket(htmlMsg);
			} else {
				// Jail the player
				activeChar.setVar(JAILEDFROM_VAR, activeChar.getX() + ";" + activeChar.getY() + ";" + activeChar.getZ() + ";" + activeChar.getReflectionId(),
						-1);
				activeChar.setVar(JAILED_VAR, JAIL_PERIOD, -1);
				activeChar.startUnjailTask(activeChar, JAIL_PERIOD);
				activeChar.teleToLocation(Location.findPointToStay(activeChar, AdminFunctions.JAIL_SPAWN, 50, 200), ReflectionManager.JAIL);
				if (activeChar.isInStoreMode()) {
					activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
				}
				activeChar.sitDown(null);
				activeChar.block();
				NpcHtmlMessage htmlMsg = new NpcHtmlMessage(0);
				htmlMsg.setHtml(JAIL_IN);
				activeChar.sendPacket(htmlMsg);
				_log.info(("Backgrounds Manager: " + activeChar.getName() + " of account " + activeChar.getAccountName() + " has been JAILED"));
			}
		} else if (activeChar.getVar(JAILED_VAR) != null) {
			// Unjail the player
			String[] re = activeChar.getVar(JAILEDFROM_VAR).split(";");
			activeChar.teleToLocation(Integer.parseInt(re[0]), Integer.parseInt(re[1]), Integer.parseInt(re[2]));
			activeChar.setReflection(re.length > 3 ? Integer.parseInt(re[3]) : 0);
			activeChar.stopUnjailTask();
			activeChar.unsetVar(JAILEDFROM_VAR);
			activeChar.unsetVar(JAILED_VAR);
			activeChar.unblock();
			activeChar.standUp();
			NpcHtmlMessage htmlMsg = new NpcHtmlMessage(0);
			htmlMsg.setHtml(JAIL_OUT);
			activeChar.sendPacket(htmlMsg);
			_log.info(("Backgrounds Manager: " + activeChar.getName() + " of account " + activeChar.getAccountName() + " has been UNJAILED"));
		}
	}
}
