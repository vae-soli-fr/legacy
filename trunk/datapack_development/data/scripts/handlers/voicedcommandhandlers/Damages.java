package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;



/**
 * @author Melua
 * Cette classe implemente la commande damages
 * .desc on
 * .desc off
 */
public class Damages implements IVoicedCommandHandler {

    private static final String[] VOICED_COMMANDS = {
        "damages"
    };

    @Override
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String option) {

        if (command.equalsIgnoreCase("damages")) {

            if (option.equalsIgnoreCase("on")) {
                activeChar.setFsDamages(true);
                activeChar.sendMessage("Dégats plein écran activés.");

            } else if (option.equalsIgnoreCase("off")) {
                activeChar.setFsDamages(false);
                activeChar.sendMessage("Dégats plein écran désactivés.");

            } else {
                activeChar.sendMessage("Usage: damages <on|off>");
                }
            }
        return true;
    }

    @Override
    public String[] getVoicedCommandList() {
        return VOICED_COMMANDS;
    }
}