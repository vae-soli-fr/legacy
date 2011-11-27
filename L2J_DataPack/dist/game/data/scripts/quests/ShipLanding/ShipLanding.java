package quests.ShipLanding;

import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.network.serverpackets.ExStartScenePlayer;

/**
 * @author Melua
 */
public class ShipLanding extends Quest {

    private static final int[] _liste = {32782, 32604, 32605, 32779};

    public ShipLanding(int id, String name, String descr) {

        super(id, name, descr);

        for (int npc : _liste) {
            addStartNpc(npc);
            addTalkId(npc);
        }
    }

    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {

        if (event.equalsIgnoreCase("infinity")) {
            player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
            player.teleToLocation(-213676, 210672, 4402);
            player.showQuestMovie(ExStartScenePlayer.LANDING_INFINITY);
        } else if (event.equalsIgnoreCase("destruction")) {
            player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
            player.teleToLocation(-246955, 251857, 4341);
            player.showQuestMovie(ExStartScenePlayer.LANDING_DESTRUCTION);
        } else if (event.equalsIgnoreCase("annihilation")) {
            player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
            player.teleToLocation(-181299, 154542, 2710);
            player.showQuestMovie(ExStartScenePlayer.LANDING_ANNIHILATION);
        } else if (event.equalsIgnoreCase("keucereus_south")) {
            player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
            player.teleToLocation(-185896, 246365, 1296);
            player.showQuestMovie(ExStartScenePlayer.LANDING_KSERTH_A);
        } else if (event.equalsIgnoreCase("keucereus_north")) {
            player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
            player.teleToLocation(-183772, 239402, 1294);
            player.showQuestMovie(ExStartScenePlayer.LANDING_KSERTH_B);
        }
        return null;
    }

    public static final void main(String[] args) {
        new ShipLanding(-1, "ShipLanding", "ship landing on seeds");
    }
}