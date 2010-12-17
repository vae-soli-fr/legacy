package handlers.voicedcommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Kirieh, updated by Melua
 * Cette classe implémente l'option de rappel de votes
 */
public class VotesReminder implements IVoicedCommandHandler {

    private static final String[] VOICED_COMMANDS = {"votes"};

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String option) {

        if (command.equalsIgnoreCase("votes")) {

            if (option.equalsIgnoreCase("on")) {
                activeChar.setVoting(true);
                activeChar.sendMessage("Vous allez être averti pour les votes, le serveur vérifiera toutes les " + Config.VAEMOD_VOTESCHECK + " minutes si vous n'avez pas voté depuis plus de " + Config.VAEMOD_VOTESDELAY + " minutes. Utilisez un des liens de vote du forum pour comptabiliser votre vote. Merci de votre participation.");

            } else if (option.equalsIgnoreCase("off")) {
                activeChar.setVoting(false);
                activeChar.sendMessage("Vous ne serez plus averti pour les votes.");

            } else {
                activeChar.sendMessage("Usage: votes <on|off>");
            }
        }
        return true;
    }

    public String[] getVoicedCommandList() {
        return VOICED_COMMANDS;
    }
}
