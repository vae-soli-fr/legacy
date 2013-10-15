/*
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
package lineage2.gameserver.vaesoli;

import java.util.Collections;
import java.util.Comparator;

import javolution.util.FastList;
import javolution.util.FastMap;
import lineage2.gameserver.Config;
import lineage2.gameserver.GameServer;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;

public class CommunityManager {
	// private static final Logger _log =
	// LoggerFactory.getLogger(CommunityManager.class);
	private static CommunityManager _instance;
	private int _onlineCount = 0;
	private int _onlineCountGm = 0;
	private static FastList<Player> _onlinePlayers = new FastList<Player>().shared();
	private static FastMap<String, String> _communityPages = new FastMap<String, String>().shared();
	private static int NAME_PER_ROW_COMMUNITYBOARD = 3;
	private static String CBCOLOR_HUMAN = "CC6666";
	private static String CBCOLOR_ELF = "FFCC99";
	private static String CBCOLOR_DARKELF = "669999";
	private static String CBCOLOR_DWARF = "CC9966";
	private static String CBCOLOR_ORC = "99CC99";
	private static String CBCOLOR_KAMAEL = "FFBFFF";
	private static String CBCOLOR_GM = "FF0000";
	private static String CBCOLOR_OFFLINE = "808080";

	private CommunityManager() {

	}

	public static CommunityManager getInstance() {
		if (_instance == null)
			_instance = new CommunityManager();
		return _instance;
	}

	/**
	 * @param activeChar
	 */
	public void showOldCommunity(Player activeChar) {
		NpcHtmlMessage htmlMsg = new NpcHtmlMessage(0);
		htmlMsg.setHtml(_communityPages.get(activeChar.isGM() ? "gm" : "pl"));
		activeChar.sendPacket(htmlMsg);
	}

	public void refreshCommunityBoard() {
		FastList<Player> sortedPlayers = new FastList<Player>();
		sortedPlayers.addAll(GameObjectsStorage.getAllPlayers());
		Collections.sort(sortedPlayers, new Comparator<Player>() {
			@Override
			public int compare(Player p1, Player p2) {
				return p1.getName().compareToIgnoreCase(p2.getName());
			}
		});

		_onlinePlayers.clear();
		_onlineCount = 0;
		_onlineCountGm = 0;

		for (Player player : sortedPlayers) {
			addOnlinePlayer(player);
		}

		_communityPages.clear();
		writeCommunityPages();
	}

	private void addOnlinePlayer(Player player) {
		if (!_onlinePlayers.contains(player)) {
			_onlinePlayers.add(player);
			if (!player.isInvisible() && !player.isInOfflineMode())
				_onlineCount++;
			if (!player.isInOfflineMode())
				_onlineCountGm++;
		}
	}

	private void writeCommunityPages() {
		final StringBuilder htmlCode = new StringBuilder(2000);
		final String tdClose = "</td>";
		final String tdOpen = "<td align=left valign=top>";
		final String trClose = "</tr>";
		final String trOpen = "<tr>";
		final String colSpacer = "<td FIXWIDTH=15></td>";

		htmlCode.setLength(0);
		htmlCode.append("<html><body><br>" + "<table>" + trOpen + "<td align=left valign=top>Serveur redémarré le "
				+ String.valueOf(GameServer.getInstance().dateTimeServerStarted.getTime()) + tdClose + trClose + "</table>" + "<table>" + trOpen + tdOpen
				+ "XP x" + String.valueOf(Config.RATE_XP) + tdClose + colSpacer + tdOpen + "SP x" + String.valueOf(Config.RATE_SP) + tdClose + trClose + trOpen
				+ tdOpen + "Drop x" + String.valueOf(Config.RATE_DROP_ITEMS) + tdClose + colSpacer + tdOpen + "Spoil x"
				+ String.valueOf(Config.RATE_DROP_SPOIL) + tdClose + colSpacer + tdOpen + "Adena x" + String.valueOf(Config.RATE_DROP_ADENA) + tdClose
				+ trClose + "</table>" + "<table>" + trOpen + "<td><img src=\"sek.cbui355\" width=600 height=1><br></td>" + trClose + trOpen + tdOpen
				+ String.valueOf(GameObjectsStorage.getAllObjectsCount()) + " Objets instanciés</td>" + trClose + trOpen + tdOpen
				+ String.valueOf(getOnlineCount("gm")) + " joueurs en ligne " + "<br1>(<font color=\"" + CBCOLOR_HUMAN + "\">Humain</font>, " + "<font color=\""
				+ CBCOLOR_ELF + "\">Elfe</font>, " + "<font color=\"" + CBCOLOR_DARKELF + "\">Sombre</font>, " + "<font color=\"" + CBCOLOR_DWARF
				+ "\">Nain</font>, " + "<font color=\"" + CBCOLOR_ORC + "\">Orc</font>, " + "<font color=\"" + CBCOLOR_KAMAEL + "\">Kamael</font>, "
				+ "<font color=\"" + CBCOLOR_GM + "\">GM</font>, " + "<font color=\"" + CBCOLOR_OFFLINE + "\">Offline</font>)</td>" + trClose + "</table>");

		int cell = 0;
		htmlCode.append("<table border=0><tr><td><table border=0>");

		for (Player player : _onlinePlayers) {
			cell++;

			if (cell == 1) {
				htmlCode.append(trOpen);
			}

			htmlCode.append("<td align=left valign=top FIXWIDTH=110>");

			if (player.isGM()) {
				htmlCode.append("<font color=\"" + CBCOLOR_GM + "\">" + player.getName() + "</font>");
			} else if (player.isInOfflineMode()) {
				htmlCode.append("<font color=\"" + CBCOLOR_OFFLINE + "\">" + player.getName() + "</font>");
			} else {
				switch (player.getRace()) {
				case darkelf:
					htmlCode.append("<font color=\"").append(CBCOLOR_DARKELF).append("\">").append(player.getName()).append("</font>");
					break;
				case dwarf:
					htmlCode.append("<font color=\"").append(CBCOLOR_DWARF).append("\">").append(player.getName()).append("</font>");
					break;
				case elf:
					htmlCode.append("<font color=\"").append(CBCOLOR_ELF).append("\">").append(player.getName()).append("</font>");
					break;
				case human:
					htmlCode.append("<font color=\"").append(CBCOLOR_HUMAN).append("\">").append(player.getName()).append("</font>");
					break;
				case kamael:
					htmlCode.append("<font color=\"").append(CBCOLOR_KAMAEL).append("\">").append(player.getName()).append("</font>");
					break;
				case orc:
					htmlCode.append("<font color=\"").append(CBCOLOR_ORC).append("\">").append(player.getName()).append("</font>");
					break;
				default:
					htmlCode.append(player.getName());
					break;
				}
			}

			htmlCode.append("</a></td>");

			if (cell < NAME_PER_ROW_COMMUNITYBOARD)
				htmlCode.append(colSpacer);

			if (cell == NAME_PER_ROW_COMMUNITYBOARD) {
				cell = 0;
				htmlCode.append(trClose);
			}
		}
		if (cell > 0 && cell < NAME_PER_ROW_COMMUNITYBOARD) {
			htmlCode.append(trClose);
		}

		htmlCode.append("</table><br></td></tr>" + trOpen + "<td><img src=\"sek.cbui355\" width=600 height=1><br></td>" + trClose + "</table></body></html>");

		_communityPages.put("gm", htmlCode.toString());

		htmlCode.setLength(0);
		htmlCode.append("<html><title>Communauté</title><body><br>" + "<table>" + trOpen + "<td align=left valign=top>Serveur redémarré le "
				+ String.valueOf(GameServer.getInstance().dateTimeServerStarted.getTime()) + tdClose + trClose + "</table>" + "<table>" + trOpen + tdOpen
				+ "XP x" + String.valueOf(Config.RATE_XP) + tdClose + colSpacer + tdOpen + "SP x" + String.valueOf(Config.RATE_SP) + tdClose + trClose + trOpen
				+ tdOpen + "Drop x" + String.valueOf(Config.RATE_DROP_ITEMS) + tdClose + colSpacer + tdOpen + "Spoil x"
				+ String.valueOf(Config.RATE_DROP_SPOIL) + tdClose + colSpacer + tdOpen + "Adena x" + String.valueOf(Config.RATE_DROP_ADENA) + tdClose
				+ trClose + "</table>" + "<table>" + trOpen + "<td><img src=\"sek.cbui355\" width=600 height=1><br></td>" + trClose + trOpen + tdOpen
				+ String.valueOf(getOnlineCount("pl")) + " joueur(s) en ligne " + "<br1>(<font color=\"" + CBCOLOR_HUMAN + "\">Humain</font>, " + "<font color=\""
				+ CBCOLOR_ELF + "\">Elfe</font>, " + "<font color=\"" + CBCOLOR_DARKELF + "\">Sombre</font>, " + "<font color=\"" + CBCOLOR_DWARF
				+ "\">Nain</font>, " + "<font color=\"" + CBCOLOR_ORC + "\">Orc</font>, " + "<font color=\"" + CBCOLOR_KAMAEL + "\">Kamael</font>, "
				+ "<font color=\"" + CBCOLOR_GM + "\">GM</font>)</td>" + trClose + "</table>");

		htmlCode.append("<table border=0><tr><td><table border=0>");

		cell = 0;
		for (Player player : _onlinePlayers) {
			if (player == null || player.isInvisible() || player.isInOfflineMode())
				continue; // Go to next

			cell++;

			if (cell == 1) {
				htmlCode.append(trOpen);
			}

			htmlCode.append("<td align=left valign=top FIXWIDTH=110>");

			if (player.isGM()) {
				htmlCode.append("<font color=\"" + CBCOLOR_GM + "\">" + player.getName() + "</font>");
			} else {
				switch (player.getRace()) {
				case darkelf:
					htmlCode.append("<font color=\"").append(CBCOLOR_DARKELF).append("\">").append(player.getName()).append("</font>");
					break;
				case dwarf:
					htmlCode.append("<font color=\"").append(CBCOLOR_DWARF).append("\">").append(player.getName()).append("</font>");
					break;
				case elf:
					htmlCode.append("<font color=\"").append(CBCOLOR_ELF).append("\">").append(player.getName()).append("</font>");
					break;
				case human:
					htmlCode.append("<font color=\"").append(CBCOLOR_HUMAN).append("\">").append(player.getName()).append("</font>");
					break;
				case kamael:
					htmlCode.append("<font color=\"").append(CBCOLOR_KAMAEL).append("\">").append(player.getName()).append("</font>");
					break;
				case orc:
					htmlCode.append("<font color=\"").append(CBCOLOR_ORC).append("\">").append(player.getName()).append("</font>");
					break;
				default:
					htmlCode.append(player.getName());
					break;
				}
			}

			htmlCode.append("</a></td>");

			if (cell < NAME_PER_ROW_COMMUNITYBOARD)
				htmlCode.append(colSpacer);

			if (cell == NAME_PER_ROW_COMMUNITYBOARD) {
				cell = 0;
				htmlCode.append(trClose);
			}
		}
		if (cell > 0 && cell < NAME_PER_ROW_COMMUNITYBOARD)
			htmlCode.append(trClose);

		htmlCode.append("</table><br></td></tr>" + trOpen + "<td><img src=\"sek.cbui355\" width=600 height=1><br></td>" + trClose + "</table></body></html>");

		_communityPages.put("pl", htmlCode.toString());
	}

	private int getOnlineCount(String type) {
		if (type.equalsIgnoreCase("gm"))
			return _onlineCountGm;
		return _onlineCount;
	}
}