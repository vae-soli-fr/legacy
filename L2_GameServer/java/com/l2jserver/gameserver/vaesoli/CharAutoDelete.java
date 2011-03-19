package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.datatables.ClanTable;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.network.L2GameClient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

/**
 *
 * @author Melua
 */
public class CharAutoDelete {

    private static Logger _log = Logger.getLogger(CharAutoDelete.class.getName());

    public static void clean() {
        Connection con = null;
        int count = 0;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT account_name, charId, char_name, clanid, deletetime  FROM characters WHERE deletetime > 0");
            ResultSet chardata = statement.executeQuery();
            while (chardata.next()) {
                String account = chardata.getString("account_name");
                int objectId = chardata.getInt("charId");
                String name = chardata.getString("char_name");
                int clanId = chardata.getInt("clanid");
                long deletetime = chardata.getLong("deletetime");
                if (System.currentTimeMillis() > deletetime) {
                    L2Clan clan = ClanTable.getInstance().getClan(clanId);
                    if (clan != null) {
                        clan.removeClanMember(objectId, 0, true);
                    }
                    L2GameClient.deleteCharByObjId(objectId);
                    _log.info("Character " + name + " of account " + account + " has been deleted.");
                    count++;
                }
            }
            chardata.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        _log.info("Deleted " + count + " characters from database.");
    }
}