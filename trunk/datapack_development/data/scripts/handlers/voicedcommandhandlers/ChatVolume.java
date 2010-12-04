package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Kirieh, updated by Melua
 * Cette classe implémente les volumes de voix
 */
public class ChatVolume implements IVoicedCommandHandler {

    private static final String[] VOICED_COMMANDS = {"chuchote", "cri"};

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target) {
        if (command.startsWith("chuchote")) {
            if (activeChar.getRPvolume().equals(" *chuchote* ")) {
                activeChar.setRPvolume("");
                activeChar.sendMessage("Vous parlez normalement.");
            } else {
                activeChar.setRPvolume(" *chuchote* ");
                activeChar.sendMessage("Vous murmurez.");
            }
        }
        if (command.startsWith("cri")) {
            if (activeChar.getRPvolume().equals(" *crie* ")) {
                activeChar.setRPvolume("");
                activeChar.sendMessage("Vous parlez normalement.");
            } else {
                activeChar.setRPvolume(" *crie* ");
                activeChar.sendMessage("Vous criez.");
            }
        }

        return true;
    }

    public String[] getVoicedCommandList() {
        return VOICED_COMMANDS;
    }
}

