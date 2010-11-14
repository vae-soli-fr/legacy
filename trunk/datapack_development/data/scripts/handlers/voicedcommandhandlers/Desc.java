/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package handlers.voicedcommandhandlers;

import javolution.text.TextBuilder;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

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

    @Override
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String option) {

        if (command.equalsIgnoreCase("desc")) {

            if (option.equalsIgnoreCase("delete")) {
                activeChar.delDesc();
                activeChar.sendMessage("Votre description a été supprimée.");

            } else if (option.equalsIgnoreCase("add")) {
                NpcHtmlMessage descWindow = new NpcHtmlMessage(1);
                TextBuilder replyMSG = new TextBuilder("<html><title>" + activeChar.getName() + "</title><body>");
                replyMSG.append("<center>Ecrivez la description de votre personnage ici<br></center>");
                replyMSG.append("<center><multiedit var=\"new_desc\" width=240 height=255><br>");
                replyMSG.append("<button value=\"Enregistrer\" action=\"bypass -h char_desc $new_desc\" width=110 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
                replyMSG.append("<table width=300><tr><td align=\"right\"><font color=\"444444\">.desc by Melua</font></td></tr></table>");
                replyMSG.append("</body></html>");
                descWindow.setHtml(replyMSG.toString());
                activeChar.sendPacket(descWindow);

            } else {
                if (activeChar.getTarget() instanceof L2PcInstance) {
                    L2PcInstance target = (L2PcInstance) activeChar.getTarget();
                    target.showDesc(activeChar);
                }
            }
        }
        return true;
    }

    @Override
    public String[] getVoicedCommandList() {
        return VOICED_COMMANDS;
    }
}

