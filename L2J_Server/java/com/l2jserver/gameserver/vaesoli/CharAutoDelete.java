package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.datatables.ClanTable;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.network.L2GameClient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Melua
 * Cette classe supprime les personnages dont le compteur de delete est dépassé
 * en effet d'ordinaire cela n'est delete qu'a la prochaine connexion (qui peut ne jamais venir)
 */
public class CharAutoDelete {

    private static final Logger _log = Logger.getLogger(CharAutoDelete.class.getName());

    public static void clean() {
        Connection con = null;
        int count = 0;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT account_name, charId, char_name, clanid, deletetime  FROM characters WHERE deletetime > 0 ORDER BY  account_name");
            ResultSet chardata = statement.executeQuery();
            while (chardata.next()) {
                int objectId = chardata.getInt("charId");
                // See if the char must be deleted
                long deletetime = chardata.getLong("deletetime");
                if (deletetime > 0) {
                    if (System.currentTimeMillis() > deletetime) {
                        L2Clan clan = ClanTable.getInstance().getClan(chardata.getInt("clanid"));
                        if (clan != null) {
                            clan.removeClanMember(objectId, 0, true);
                        }
                        L2GameClient.deleteCharByObjId(objectId);
                        _log.info("Character " + chardata.getString("char_name") + " of account " + chardata.getString("account_name") + " has been deleted.");
                        count++;
                    }
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
            }
        }
        _log.info("Deleted " + count + " characters from database.");
    }
}