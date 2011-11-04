package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.vaesoli.Descriptions;

/**
 * @author Melua
 * Cette classe implemente la commande desc
 * .desc
 * .desc add
 * .desc delete
 */
public class Desc implements IVoicedCommandHandler {

    private static final String[] VOICED_COMMANDS = {
        "desc"
    };

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String option) {

        if (command.equalsIgnoreCase("desc")) {

            if (option == null) {
                if (activeChar.getTarget() != null && activeChar.getTarget() instanceof L2PcInstance)
                {
                L2PcInstance target = (L2PcInstance) activeChar.getTarget();
                Descriptions.showDesc(target, activeChar);
                }
                else activeChar.sendMessage("Sélectionnez un joueur pour voir sa description.");
            } else {
                if (option.equalsIgnoreCase("delete")) {
                    Descriptions.delDesc(activeChar);
                    activeChar.sendMessage("Votre description a été supprimée.");
                } else {
                    activeChar.sendMessage("Usage: .desc [delete]");
                }
            }
        }
        return true;
    }

    public String[] getVoicedCommandList() {
        return VOICED_COMMANDS;
    }
}

