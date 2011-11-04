package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Kirieh, updated by Melua
 * Cette classe implémente la commande .time
 */
public class RPTime implements IVoicedCommandHandler {

    private static final String[] VOICED_COMMANDS = {
        "time"
    };

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target) {
        if (command.startsWith("time")) {
            long basetime = 1167606000; // 1.1.2007
            long actualtime = System.currentTimeMillis() / 1000;
            long elapsed = actualtime - basetime; // Time elapsed
            long elapsed_ig = elapsed * 4; // Temps écoulé IG

            int year = 31 + (int) Math.floor(elapsed_ig / (12 * 30 * 24 * 3600)); // Year, $basetime being 31.

            int month = (int) Math.floor(elapsed_ig / (30 * 24 * 3600)) % 12; // Month from 0 to 11
            int day = 1 + (int) Math.floor(elapsed_ig / (24 * 3600)) % 30; // Day from from 1 to 30
            int dayow = (int) Math.floor(elapsed_ig / (24 * 3600)) % 6; // Day of week from 0 to 5

            String[] months = {
                "Tombeglace",
                "Blancheterre",
                "Fondgivre",
                "L'Astrée",
                "Vertefeuille",
                "Brilleblé",
                "Brûleblé",
                "Astredoux",
                "Tourneterre",
                "Rougefeuille",
                "Tombefeuille",
                "Souffleglace"};
            String[] days = {"Lunem", "Marka", "Metri", "Jeriel", "Verdel", "Sumbra"};
            String message = days[(int) dayow] + ", le " + (int) day + " " + months[(int) month] + " de l'an " + (int) year;
            activeChar.sendMessage(message);
        }
        return true;
    }

    public String[] getVoicedCommandList() {
        return VOICED_COMMANDS;
    }
}
