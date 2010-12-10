package handlers.admincommandhandlers;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.CameraMode;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import java.util.StringTokenizer;

/**
 * @author Melua
 * Cette classe implemente la commande vocale
 * .movie on
 * .movie off
 */
public class AdminMovie implements IAdminCommandHandler {

    private static final String[] ADMIN_COMMANDS = {"admin_movie"};

    public boolean useAdminCommand(String command, L2PcInstance activeChar) {
        StringTokenizer st = new StringTokenizer(command, " ");
        st.nextToken();
        String option = st.nextToken();
        if (option.equalsIgnoreCase("on")) {
            /* 1st person */
            activeChar.sendPacket(new CameraMode(1));
            /* invisible : consultez AdminEffect */
            activeChar.getAppearance().setInvisible();
            activeChar.broadcastUserInfo();
        } else if (option.equalsIgnoreCase("off")) {
            /* synchronisation avec le serveur : */
            activeChar.teleToLocation(activeChar.getClientX(), activeChar.getClientY(), activeChar.getClientZ());
            /* 3rd person */
            activeChar.sendPacket(new CameraMode(0));
        } else {
            activeChar.sendMessage("Usage: //movie <on|off>");
        }
        return true;
    }

    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }
}