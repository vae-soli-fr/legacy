package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;

/**
 * @author Melua
 * Cette classe implémente la commande pour faire parler les Summons
 */
public class PetSay implements IVoicedCommandHandler {

    private static final String[] _voicedCommands = {">>"};

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String option) {
if (command.equalsIgnoreCase(">>")) {
            if (activeChar.getPet() != null) {
                if (option != null) {
                    if (activeChar.getPet().getName() != null) {
                        activeChar.getPet().broadcastPacket(new CreatureSay(activeChar.getPet().getObjectId(), Say2.ALL, "{" + activeChar.getPet().getName() + "}", option));
                    } else {
                        activeChar.sendMessage("Votre animal ne possède pas de nom.");
                    }
                } else {
                    activeChar.sendMessage("Usage : .>> texte roleplay");
                }
            } else {
                activeChar.sendMessage("Vous n'avez pas d'animal de compagnie.");
            }
        }
        return true;
    }

    public String[] getVoicedCommandList() {
        return _voicedCommands;
    }
}