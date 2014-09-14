package lineage2.gameserver.vaesoli;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.tables.ClanTable;

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
            con = DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT account_name, charId, char_name, clanid, deletetime  FROM characters WHERE deletetime > 0 ORDER BY  account_name");
            ResultSet chardata = statement.executeQuery();
            while (chardata.next()) {
                int objectId = chardata.getInt("charId");
                // See if the char must be deleted
                long deletetime = chardata.getLong("deletetime");
                if (deletetime > 0) {
                    if (System.currentTimeMillis() > deletetime) {
                        Clan clan = ClanTable.getInstance().getClan(chardata.getInt("clanid"));
                        if (clan != null) {
                            clan.removeClanMember(objectId);
                        }
                        CharacterDAO.getInstance().deleteCharByObjId(objectId);
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