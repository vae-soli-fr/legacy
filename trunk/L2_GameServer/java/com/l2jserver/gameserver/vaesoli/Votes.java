package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.Config;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

public class Votes {

    private final int INTERVAL = Config.VAEMOD_VOTESDELAY;
    private final int TIMER = Config.VAEMOD_VOTESCHECK * 60000;

    private static final class SingletonHolder {

        private static final Votes INSTANCE = new Votes();
    }

    public static Votes getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Votes() {
        ThreadPoolManager.getInstance().scheduleGeneral(new Rappel(), TIMER);
    }

    private class Rappel implements Runnable {

        @Override
        public void run() {
            for (L2PcInstance player : L2World.getInstance().getAllPlayers().values()) {
                if (player.isVoting() && !player.hasVoted()) {
                    player.sendPacket(new ExShowScreenMessage(1, -1, 7, 0, 1, 0, 0, true, 10000, 0, "Vous n'avez pas voté depuis plus de " + INTERVAL + " minutes"));
                }
            }
            ThreadPoolManager.getInstance().scheduleGeneral(new Rappel(), TIMER);
        }
    }
}
