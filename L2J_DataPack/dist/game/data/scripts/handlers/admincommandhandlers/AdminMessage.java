package handlers.admincommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.L2WorldRegion;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

/**
 * @author Melua
 * Cette classe implemente la commande message
 */
public class AdminMessage implements IAdminCommandHandler {

    private static final String[] ADMIN_COMMANDS = {"admin_message"};
    private String message;

    public boolean useAdminCommand(String command, L2PcInstance activeChar) {

        if (command.startsWith("admin_message")) {

            if (Config.VAEMOD_ADMINMESSAGEAREA.equalsIgnoreCase("GLOBAL")) {

                try {
                    message = command.substring(14);
                    for (L2PcInstance player : L2World.getInstance().getAllPlayersArray()) {
                        player.sendPacket(new ExShowScreenMessage(message, 6000)); // durée
                    }
                } catch (Exception e) {
                }
            } else if (Config.VAEMOD_ADMINMESSAGEAREA.equalsIgnoreCase("REGION")) {
                try {
                    message = command.substring(14);
                    L2WorldRegion region = L2World.getInstance().getRegion(activeChar.getX(), activeChar.getY());
                    for (L2PcInstance player : L2World.getInstance().getAllPlayersArray())
                    {
                        if (region == L2World.getInstance().getRegion(player.getX(), player.getY()) && (player.getInstanceId() == activeChar.getInstanceId()))
                        {
                            player.sendPacket(new ExShowScreenMessage(message, 6000)); // durée
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return true;
    }

    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }
}