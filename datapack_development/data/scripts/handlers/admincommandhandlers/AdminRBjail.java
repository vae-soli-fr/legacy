package handlers.admincommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Melua
 */
public class AdminRBjail implements IAdminCommandHandler {

    private static final String[] ADMIN_COMMANDS = {"admin_rbjail"};

    public boolean useAdminCommand(String command, L2PcInstance activeChar) {

        if (command.startsWith("admin_rbjail")) {

            if (!Config.VAEMOD_RBJAIL) {
                Config.VAEMOD_RBJAIL = true;
                activeChar.sendMessage("RaidBossLimiter activé.");
            } else {
                Config.VAEMOD_RBJAIL = false;
                activeChar.sendMessage("RaidBossLimiter désactivé temporairement.");
            }
        }

        return true;
    }

    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }
}
