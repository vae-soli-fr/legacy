package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Saelil
 * Cette classe implémente la commande .camp
 */
public class Camp implements IVoicedCommandHandler {

    private static final String[] _voicedCommands = {
        "camp"
    };

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String option) {
        if (command.equalsIgnoreCase("camp")) {
            activeChar.getCamp().evolve(activeChar);
        }
        return true;

    }

    public String[] getVoicedCommandList() {
        return _voicedCommands;
    }
}