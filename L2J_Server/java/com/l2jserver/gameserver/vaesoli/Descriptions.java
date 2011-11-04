package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Melua
 * relie la description au nom et non pas à l'ID
 * support des images
 */

public class Descriptions {

        public static void showDesc(L2PcInstance target, L2PcInstance viewer)
        {
        String description = getDesc(target);
        if (description != null)  {
        NpcHtmlMessage html = new NpcHtmlMessage(1);
        html.setHtml("<html><title>" + target.getName() + "</title><body>" + description + "</body></html>");
        /**
         * for DDS
         * @see L2PcInstance#sendPacket(L2GameServerPacket packet)
         */
        viewer.sendPacket(html);
        } else {
        viewer.sendMessage("Ce personnage ne possède pas de description.");
        }
        }

        public static String getDesc(L2PcInstance target) // BY NAME
        {
        Connection con = null;
        String description = null;
        try
        {
        con = L2DatabaseFactory.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement("SELECT char_desc FROM descriptions WHERE charId = ? AND char_name = ?");
        statement.setInt(1, target.getObjectId());
        statement.setString(2, target.getName());
        ResultSet rset = statement.executeQuery();
        while (rset.next()) description = rset.getString("char_desc");
        rset.close();
        statement.close();
        }
        catch (Exception e) { }
        finally { try { if (con != null) con.close(); } catch (Exception e) {} }
        return description;
        }

        public static void setDesc(L2PcInstance activeChar, String description) // BY NAME
        {
        delDesc(activeChar);
        if (description.length() > 600) description = description.substring(0, 599);
        Connection con = null;
        try
        {
        con = L2DatabaseFactory.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement("INSERT INTO descriptions VALUES (?,?,'" + description + "');");
        statement.setInt(1, activeChar.getObjectId());
        statement.setString(2, activeChar.getName());
        statement.execute();
        statement.close();
        }
        catch (Exception e) { }
        finally { try { if (con != null) con.close(); } catch (Exception e) {} }
        }

        public static void delDesc(L2PcInstance activeChar) // BY NAME
        {
        Connection con = null;
        try
        {
        con = L2DatabaseFactory.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement("DELETE FROM descriptions WHERE charId = ? AND char_name = ?");
        statement.setInt(1, activeChar.getObjectId());
        statement.setString(2, activeChar.getName());
        statement.execute();
        statement.close();
        }
        catch (Exception e) { }
        finally { try { if (con != null) con.close(); } catch (Exception e) {} }
        }
}