package handlers.admincommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.Earthquake;
import com.l2jserver.gameserver.network.serverpackets.ExRedSky;
import com.l2jserver.gameserver.network.serverpackets.PlaySound;
import com.l2jserver.gameserver.network.serverpackets.SSQInfo;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;
import com.l2jserver.gameserver.network.serverpackets.SunRise;
import com.l2jserver.gameserver.skills.AbnormalEffect;
import com.l2jserver.gameserver.vaesoli.RedEnterWorld;
import java.util.StringTokenizer;

/**
 * @author Melua
 * Cette classe implemente la commande redsky
 */
public class AdminRedSky implements IAdminCommandHandler {

    private static final String[] ADMIN_COMMANDS = {"admin_redsky"};
    private static final int HERO = 16;
    private static final int AIR_ROOT = AbnormalEffect.S_AIR_ROOT.getMask();

    public boolean useAdminCommand(String command, L2PcInstance activeChar) {
        StringTokenizer st = new StringTokenizer(command);
        st.nextToken();

        if (command.startsWith("admin_redsky")) {

            if (!Config.VAEMOD_SKYISRED) {
                RedStart();
            } else if (Config.VAEMOD_SKYISRED) {
                RedStop();
            }
        }

        return true;
    }

    private void RedStart() {

        for (L2PcInstance player : L2World.getInstance().getAllPlayers().values()) {
            /* Tremblement de terre */
            player.sendPacket(new Earthquake(player.getX(), player.getY(), player.getZ(), 14, 12)); // intensity, duration
            RedEnterWorld.RedEffects(player);
        }
        /* SUPERGLOBALE */
        Config.VAEMOD_SKYISRED = true;
    }

    private void RedStop() {
        /* for each player IG */
       for (L2PcInstance player : L2World.getInstance().getAllPlayers().values()) {
            /* Stop vitality si besoin est */
            if (player.getAbnormalEffect() == AIR_ROOT) {
                player.stopAbnormalEffect(AIR_ROOT);
            }
            /* Effet Hero */
            player.broadcastPacket(new SocialAction(player.getObjectId(), HERO));
            /* Son neutral rise */
            player.sendPacket(new PlaySound("ssq_neutral_01"));
            /* fin du ciel rouge */
            player.sendPacket(new ExRedSky(0));
            /* Tremblement de terre */
            player.sendPacket(new Earthquake(player.getX(), player.getY(), player.getZ(), 14, 6)); // intensity, duration
            /* Jour */
            player.sendPacket(new SunRise());
            /* Neutralite seven signs */
            player.sendPacket(new SSQInfo(0));
        }
        /* SUPERGLOBALE */
        Config.VAEMOD_SKYISRED = false;
    }

    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }
}
