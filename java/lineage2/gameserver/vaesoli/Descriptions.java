package lineage2.gameserver.vaesoli;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Melua
 * relie la description au nom
 * support des images
 */

public class Descriptions {

	private static final String DESC_VAR = "playerDesc";
	private static final String NO_DESC = "Ce personnage ne possède pas de description.";
	private static final int DESC_MAX_LENGTH = 600;

	public static void showDesc(Player target, Player viewer) {
		String description = getDesc(target);
		if (description != null) {
			NpcHtmlMessage html = new NpcHtmlMessage(1);
			html.setHtml("<html><title>" + target.getName() + "</title><body>" + description + "</body></html>");
			/**
			 * for DDS
			 * 
			 * @see L2PcInstance#sendPacket(L2GameServerPacket packet)
			 */
			viewer.sendPacket(html);
		} else {
			viewer.sendMessage(NO_DESC);
		}
	}

	public static String getDesc(Player target) // BY NAME
	{
		return target.getVar(DESC_VAR);
	}

	public static void setDesc(Player activeChar, String description) // BY NAME
	{
		delDesc(activeChar);
		if (description.length() > DESC_MAX_LENGTH)
			description = description.substring(0, DESC_MAX_LENGTH - 1);
		activeChar.setVar(DESC_VAR, description, -1);
	}

	public static void delDesc(Player activeChar) // BY NAME
	{
		activeChar.unsetVar(DESC_VAR);
	}
}