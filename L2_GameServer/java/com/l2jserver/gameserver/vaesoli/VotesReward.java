package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.model.L2ItemInstance;
import java.util.logging.Logger;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.StatusUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javolution.util.FastMap;

/**
 * @author Melua
 * Cette classe vérifie que les BG sont à jour et validés
 * concernant les transfos, les subs, et bientôt les BG tout court.
 *
 */
public class VotesReward {

    private final static FastMap<Integer, Integer> _rewards;

    static {
        _rewards = new FastMap<Integer, Integer>();
        // item_id, price
        _rewards.put(800, 20);
        _rewards.put(841, 30);
    }

    private static int retrievePoints(L2PcInstance player) {
        Connection con = null;
        int votes = 0;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT votes FROM accounts_digest WHERE login = ?");
            statement.setString(1, player.getAccountName());
            ResultSet rset = statement.executeQuery();

            while (rset.next()) {
                votes = rset.getInt("votes");
            }
            rset.close();
            statement.close();
        } catch (Exception e) {
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return votes;
    }

    private static String retrieveIp(L2PcInstance player) {
        Connection con = null;
        String lastIP = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT lastIP FROM accounts_digest WHERE login = ?");
            statement.setString(1, player.getAccountName());
            ResultSet rset = statement.executeQuery();

            while (rset.next()) {
                lastIP = rset.getString("lastIP");
            }
            rset.close();
            statement.close();
        } catch (Exception e) {
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lastIP;
    }

    private static void decreasePoints(L2PcInstance player, int pts) {
        Connection con = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE accounts_digest SET votes = ? WHERE lastIP = ?");
            statement.setInt(1, retrievePoints(player));
            statement.setString(2, retrieveIp(player));
            statement.executeQuery();
            statement.close();
        } catch (Exception e) {
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void show(L2PcInstance player) {
        if (retrievePoints(player) < 50) {
            return;
        }

        // Just open a Html message to inform the player
        NpcHtmlMessage htmlMsg = new NpcHtmlMessage(0);
        String rewardsInfos = HtmCache.getInstance().getHtm(player.getHtmlPrefix(), "data/html/rewards.htm");
        if (rewardsInfos != null) {
            htmlMsg.setHtml(rewardsInfos);
            player.sendPacket(htmlMsg);
        }
    }

    public static void giveReward(L2PcInstance player, int rewardId) {
        if (!_rewards.containsKey(rewardId)) {
            return;
        }

        if (retrievePoints(player) >= _rewards.get(rewardId)) {
            decreasePoints(player, _rewards.get(rewardId));
            L2ItemInstance rewardItem = player.getInventory().addItem("VotesReward", rewardId, 1, player, null);
            StatusUpdate su = new StatusUpdate(player);
            su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
            player.sendPacket(su);
            player.sendPacket(new SystemMessage(SystemMessageId.EARNED_ITEM).addItemName(rewardItem));
        } else {
            player.sendMessage("Vous n'avez pas assez de votes !");
            show(player);
        }

    }
}
