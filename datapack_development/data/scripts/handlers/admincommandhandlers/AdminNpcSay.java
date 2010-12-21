package handlers.admincommandhandlers;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import java.util.StringTokenizer;

/**
 * @author Melua
 * Cette classe implemente la commande //>>
 */
public class AdminNpcSay implements IAdminCommandHandler {

    private static final String[] ADMIN_COMMANDS = {"admin_>>"};

    public boolean useAdminCommand(String command, L2PcInstance activeChar) {

        if (activeChar.getTarget() != null) {
            if (activeChar.getTarget() instanceof L2PcInstance) {
                activeChar.sendMessage("Il n'est pas permis de faire parler un joueur.");
            } else if (activeChar.getTarget() instanceof L2Npc) {
                StringTokenizer st = new StringTokenizer(command, " ");
                st.nextToken();
                if (st.hasMoreTokens()) {
                    L2Npc npc = (L2Npc) activeChar.getTarget();
                    npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), st.nextToken()));
                } else {
                    activeChar.sendMessage("Usage : //>> texte roleplay");
                }
            }
        } else {
            activeChar.sendMessage("Vous n'avez pas de cible à faire parler.");
        }
        return true;
    }

    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }
}