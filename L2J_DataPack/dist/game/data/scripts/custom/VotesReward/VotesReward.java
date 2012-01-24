package custom.VotesReward;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.model.item.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.network.L2GameClient;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.StatusUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.GMAudit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import javolution.util.FastMap;

/**
 * @author Melua
 */
public class VotesReward extends Quest {

    private static final FastMap<Integer, Integer> _rewards; // item_id, price
    private static final int _npc = 55000;

    static {
        _rewards = new FastMap<>();
        _rewards.put(55001, 50); // keltir
        _rewards.put(55002, 50); // renard
        _rewards.put(55003, 50); // lapin
        _rewards.put(55004, 50); // elpy
        _rewards.put(55005, 50); // larve
        _rewards.put(55006, 50); // salamandre
        _rewards.put(55007, 50); // crapaud
        _rewards.put(55008, 100); // licorne
        _rewards.put(55009, 100); // mandragore
        _rewards.put(55010, 100); // abeille
        _rewards.put(55011, 100); // chauve souris
        _rewards.put(55012, 150); // panthere
        _rewards.put(55013, 150); // cougar
        _rewards.put(55014, 150); // bete
        _rewards.put(55015, 200); // phoenix
        _rewards.put(55016, 200); // onyx
        _rewards.put(55017, 200); // spectre   
    }

    public VotesReward(int id, String name, String descr) {
        super(id, name, descr);
        addFirstTalkId(_npc); // means that once you double-click on the NPC, it will run the onFirsTalk part of the quest.
        addStartNpc(_npc); // will run the onTalk part.
        addTalkId(_npc); // will run the onEvent and onAdvEvent parts.
    }

    @Override
    public String onFirstTalk(L2Npc npc, L2PcInstance player) {
        show(player);
        return null;
    }

    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
        if (event.startsWith("give")) {
            StringTokenizer st = new StringTokenizer(event, " ");
            st.nextToken();
            int rewardId = Integer.parseInt(st.nextToken());
            giveReward(player, rewardId);
        }
        return null;
    }

    private static void show(L2PcInstance player) {
        NpcHtmlMessage html = new NpcHtmlMessage(1);
        html.setFile(player.getHtmlPrefix(), "data/html/rewards.htm");
        html.replace("%playername%", player.getName());
        html.replace(("%votes%"), String.valueOf(retrievePoints(player)));
        html.replace(("%accountname%"), player.getAccountName());
        html.replace(("%ip%"), retrieveIp(player));
        player.sendPacket(html);
    }

    private static int retrievePoints(L2PcInstance player) {
        Connection con = null;
        int votes = 0;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT votes FROM account_digest WHERE lastIP = ? ORDER BY votes ASC");
            statement.setString(1, retrieveIp(player));
            ResultSet rset = statement.executeQuery();

            while (rset.next()) {
                // get the max for this IP
                votes = rset.getInt("votes");
            }
            rset.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
        return votes;
    }

    private static String retrieveIp(L2PcInstance player) {
        L2GameClient client = player.getClient();
        return client.getConnection().getInetAddress().getHostAddress();
    }

    private static void decreasePoints(L2PcInstance player, int pts) {
        Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE account_digest SET votes = ? WHERE lastIP = ?");
            statement.setInt(1, retrievePoints(player) - pts);
            statement.setString(2, retrieveIp(player));
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
        player.sendMessage("Vous avez dépensé " + pts + " points de vote.");
    }

    private static void giveReward(L2PcInstance player, int rewardId) {
        if (!_rewards.containsKey(rewardId)) {
            return;
        }

        if (retrievePoints(player) >= _rewards.get(rewardId)) {
            decreasePoints(player, _rewards.get(rewardId));
            L2ItemInstance rewardItem = player.getInventory().addItem("VotesReward", rewardId, 1, player, null);
            StatusUpdate su = new StatusUpdate(player);
            su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
            player.sendPacket(su);
            player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1).addItemName(rewardItem));
            if (Config.GMAUDIT) GMAudit.auditGMAction("VotesReward", "Has bought a pet for " + _rewards.get(rewardId) + " points", player.getName() + " (" + player.getAccountName() + ")", rewardItem.getItemName());
        } else {
            player.sendMessage("Vous n'avez pas assez de votes !");
            show(player);
        }

    }

    public static final void main(String[] args) {
        new VotesReward(-1, "VotesReward", "custom");
    }
}
