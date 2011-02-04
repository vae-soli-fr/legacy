package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.L2DatabaseFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 *
 * @author Melua
 */
public class OptimizeTables {

    private final static Logger _log = Logger.getLogger(OptimizeTables.class.getName());

    public static void justDoIt() {
        Connection con = null;
        try {
            String sql = "OPTIMIZE TABLE ";
            int i = 0;
            con = L2DatabaseFactory.getInstance().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLE STATUS");
            while (rs.next()) {
                if (rs.getInt("Data_free") > 0) {
                    sql = sql + rs.getString("Name") + ", ";
                    i++;
                }
            }
            if (i > 0) {
                sql = sql.substring(0, sql.length() - 2);
                stmt.executeQuery(sql);
                _log.info(sql);
            } else {
                _log.info("NO TABLE TO OPTIMIZE");
            }
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
    }
}
