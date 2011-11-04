package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 *
 * @author Melua
 * Cette classe vérifie le timer de vote
 */

public class VotesChecker {

    public static long CURRENTTIME = 0;
    private final int TIMER = Config.VAEMOD_VOTESCHECK * 60000;

    private static final class SingletonHolder {

        private static final VotesChecker INSTANCE = new VotesChecker();
    }

    public static VotesChecker getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
            /**
     * Etat du vote
     * @return vrai(true) si le joueur à voté ou faux(false) dans le cas contraire
     * @author melua
     */

        public static boolean hasVoted(L2PcInstance player)
        {
        if ( VotesChecker.CURRENTTIME < player.getLastVote()+(2*3600)) return true;
        else {
        Connection con = null;
        long newtime = 0;
        try
        { con = L2DatabaseFactory.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement("SELECT lastVote FROM accounts_digest WHERE login = ?");
        statement.setString(1, player.getAccountName());
        ResultSet rset = statement.executeQuery();
        while (rset.next()) newtime = rset.getLong("lastVote");
        rset.close();
        statement.close();
        }
        catch (Exception e) { }
        finally { try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace(); } }
        player.setLastVote(newtime);
        if (VotesChecker.CURRENTTIME < player.getLastVote()+(2*3600)) return true;
        else return false;
        }
        }

    private VotesChecker() {
        ThreadPoolManager.getInstance().scheduleGeneral(new Rappel(), TIMER);
    }

    private class Rappel implements Runnable {

        @Override
        public void run() {
            CURRENTTIME = System.currentTimeMillis();
            for (L2PcInstance player : L2World.getInstance().getAllPlayersArray()) {
                if (player.isVoting() && !hasVoted(player)) {
                    player.sendPacket(new ExShowScreenMessage("Vous n'avez pas voté depuis plus de 120 minutes", 15000));
                }
            }
            ThreadPoolManager.getInstance().scheduleGeneral(new Rappel(), TIMER);
        }
    }
}
