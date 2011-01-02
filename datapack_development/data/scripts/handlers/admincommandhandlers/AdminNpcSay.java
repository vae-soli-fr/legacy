package handlers.admincommandhandlers;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;

/**
 * @author Melua
 * Cette classe implemente la commande //>>
 */
public class AdminNpcSay implements IAdminCommandHandler {

    private static final String[] ADMIN_COMMANDS = {"admin_>>"};

    public boolean useAdminCommand(String command, L2PcInstance activeChar) {

        if (activeChar.getTarget() != null) {
            if (activeChar.getTarget() instanceof L2Npc) {
                if (command.length() > 9) {
                    L2Npc npc = (L2Npc) activeChar.getTarget();
                    String vox = command.substring(9);
                    if (vox.startsWith("!")) {
                        for (L2PcInstance player : L2World.getInstance().getAllPlayers().values()) {
                            player.sendPacket(new CreatureSay(npc.getObjectId(), Say2.SHOUT, npc.getName(), vox.substring(1)));
                        }
                    } else if (vox.startsWith("^")) {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.BATTLEFIELD, npc.getName(), vox.substring(1)));
                    } else {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), vox));
                    }

                } else {
                    activeChar.sendMessage("Usage : //>> [!|^]texte roleplay");
                }
            } else if (activeChar.getTarget() instanceof L2PcInstance) {
                activeChar.sendMessage("Il est interdit de faire parler un joueur.");
            } else if (activeChar.getTarget() instanceof L2Summon) {
                activeChar.sendMessage("Il est interdit de faire parler un summon avec cette commande, utilisez .>> pour votre propre summon.");
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