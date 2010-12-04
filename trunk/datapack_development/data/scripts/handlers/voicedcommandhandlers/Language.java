package scripts.handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Kirieh, updated by Melua
 * Cette classe implémente les langages RP
 */
public class Language implements IVoicedCommandHandler {

    private static final String[] VOICED_COMMANDS = {"elfe", "sombre", "nain", "orc", "kamael"};

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target) {
        if (command.startsWith("elfe")) {
            if (activeChar.getRPlanguage().equals(" *elfique* ")) {
                activeChar.setRPlanguage("");
                activeChar.sendMessage("Vous parlez en commun.");
            } else {
                activeChar.setRPlanguage(" *elfique* ");
                activeChar.sendMessage("Vous parlez en elfique.");
            }
        }

        if (command.startsWith("sombre")) {
            if (activeChar.getRPlanguage().equals(" *sombre* ")) {
                activeChar.setRPlanguage("");
                activeChar.sendMessage("Vous parlez en commun.");
            } else {
                activeChar.setRPlanguage(" *sombre* ");
                activeChar.sendMessage("Vous parlez en sombre.");
            }
        }

        if (command.startsWith("nain")) {
            if (activeChar.getRPlanguage().equals(" *nain* ")) {
                activeChar.setRPlanguage("");
                activeChar.sendMessage("Vous parlez en commun.");
            } else {
                activeChar.setRPlanguage(" *nain* ");
                activeChar.sendMessage("Vous parlez en nain.");
            }
        }

        if (command.startsWith("orc")) {
            if (activeChar.getRPlanguage().equals(" *orc* ")) {
                activeChar.setRPlanguage("");
                activeChar.sendMessage("Vous parlez en commun.");
            } else {
                activeChar.setRPlanguage(" *orc* ");
                activeChar.sendMessage("Vous parlez en orc.");
            }
        }

        if (command.startsWith("kamael")) {
            if (activeChar.getRPlanguage().equals(" *kamael* ")) {
                activeChar.setRPlanguage("");
                activeChar.sendMessage("Vous parlez en commun.");
            } else {
                activeChar.setRPlanguage(" *kamael* ");
                activeChar.sendMessage("Vous parlez en kamael.");
            }
        }

        return true;
    }

    public String[] getVoicedCommandList() {
        return VOICED_COMMANDS;
    }
}