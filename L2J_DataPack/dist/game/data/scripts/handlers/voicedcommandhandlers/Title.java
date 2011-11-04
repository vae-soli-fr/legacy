package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Melua
 * Cette classe implémente la commande .titre
 */
public class Title implements IVoicedCommandHandler {

    private static final String[] _voicedCommands = {
        "titre"
    };

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String option) {
        if (command.equalsIgnoreCase("titre")) {
            activeChar.setTitle(option);
            activeChar.broadcastTitleInfo();
        }
        return true;

    }

    public String[] getVoicedCommandList() {
        return _voicedCommands;
    }
}