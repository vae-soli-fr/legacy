package lineage2.gameserver.vaesoli;

import java.util.logging.Logger;

import javolution.util.FastMap;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.utils.AdminFunctions;
import lineage2.gameserver.utils.Location;

/**
 * @author Melua Mort aux GBs ! Cette classe jail au bout de plus de 3 RBs par
 *         compte
 */

public class RaidLimiter {
	private final static int period = 86400;
	private final static String JAIL_IN = "<html><title>Prison</title><body>Les RBs sont limités à 3 par jour et par compte, après reboot.<br1>Vous avez dépassé le quota autorisé, vous êtes en prison pour 24h.</body></html>";
	private final static Logger _log = Logger.getLogger(RaidLimiter.class.getName());
	private FastMap<String, Integer> _list; // liste des points par compte
	private FastMap<String, String> _done; // liste des noms des RB par compte

	public static RaidLimiter getInstance() {
		return SingletonHolder._instance;
	}

	private RaidLimiter() {
		_list = new FastMap<>();
		_done = new FastMap<>();
	}

	public void addPoint(Player player, String raidboss) {
		if (player.getReflection() != ReflectionManager.DEFAULT)
			return;
		if (_list.containsKey(player.getAccountName()) && _done.containsKey(player.getAccountName())) {
			int points = _list.get(player.getAccountName());
			String names = _done.get(player.getAccountName());
			_list.put(player.getAccountName(), ++points);
			_done.put(player.getAccountName(), names + ", " + raidboss);
			if (points == 3) {
				player.sendPacket(new ExShowScreenMessage("Attention ceci était votre dernier Raid Boss", 15000, ScreenMessageAlign.TOP_CENTER, true));
			} else if (points > 3)
				autoBan(player);
		} else {
			_list.put(player.getAccountName(), 1);
			_done.put(player.getAccountName(), raidboss);
		}
	}

	private void autoBan(Player player) {
		player.setVar("jailedFrom", player.getX() + ";" + player.getY() + ";" + player.getZ() + ";" + player.getReflectionId(), -1);
		player.setVar("jailed", period, -1);
		player.startUnjailTask(player, period);
		player.teleToLocation(Location.findPointToStay(player, AdminFunctions.JAIL_SPAWN, 50, 200), ReflectionManager.JAIL);
		player.sitDown(null);
		player.block();
		NpcHtmlMessage htmlMsg = new NpcHtmlMessage(0);
		htmlMsg.setHtml(JAIL_IN);
		player.sendPacket(htmlMsg);
		_log.info("RaidBoss Limiter: character " + player.getName() + " of account " + player.getAccountName() + " is jailed for 24 hours.");
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final RaidLimiter _instance = new RaidLimiter();
	}
}