package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.idfactory.IdFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 * @author Melua
 * relie la description au nom et non pas à l'ID
 * support des images
 */

public class DescriptionsWithImages {

        private static Logger _log = Logger.getLogger(DescriptionsWithImages.class.getName());

        public static void showDesc(L2PcInstance target, L2PcInstance viewer)
        {
        String description = getDesc(target);
        if (description != null)  {
        NpcHtmlMessage html = new NpcHtmlMessage(1);
        html.setHtml("<html><title>" + target.getName() + "</title><body>" + description + "</body></html>");
        if (Config.VAEMOD_DESCWITHIMAGES) html.sendDDS(viewer);
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
        finally { try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace(); } }
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
        finally { try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace(); } }
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
        finally { try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace(); } }
        }

    public static void genDesc(L2PcInstance activeChar) {
        //TODO: parser à la recherche de tags d'images externes (URL)
        String description = getDesc(activeChar);
        if (description != null) {
            Pattern pattern = Pattern.compile("<img_ext>(http://[0-9a-zA-Z]+(.png|.jpg|.bmp))</img_ext>");
            Matcher matcher = pattern.matcher(description);
            while (matcher.find()) {
                try {
                    int imgId = IdFactory.getInstance().getNextId();
                    String sequence = matcher.group(0);
                    String img_ext = matcher.group(1);
                    String extension = matcher.group(2);
                    // vérifier la taille (puissance de 2)
                    ImageIcon info = new ImageIcon(img_ext); // URL
                    if ((info.getIconHeight() > 0 && (info.getIconHeight() & (info.getIconHeight() - 1)) == 0) && (info.getIconWidth() > 0 && (info.getIconWidth() & (info.getIconWidth() - 1)) == 0)) {

                        // telecharger avec l'URL trouvée
                        _log.info("Url FOUND:" + img_ext); // DEBUG
                        BufferedInputStream in = new BufferedInputStream(new URL(img_ext).openStream());
                        _log.info("File MADE:" + Config.DATAPACK_ROOT + "/images/" + activeChar.getName().toLowerCase() + "/" + imgId + extension); // DEBUG
                        FileOutputStream fos = new FileOutputStream(Config.DATAPACK_ROOT + "/images/" + activeChar.getName().toLowerCase() + "/" + imgId + extension);
                        BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                        byte[] data = new byte[1024];
                        int x = 0;
                        while ((x = in.read(data, 0, 1024)) >= 0) {
                            bout.write(data, 0, x);
                        }
                        bout.close();
                        in.close();

                        // remplacer la desc
                        description.replace(sequence, "<img_int>" + imgId + extension + "<img_int>");
                    }
                } catch (Exception e) {
                    _log.warning(e.getMessage());
                }
                activeChar.sendMessage("1 image processing...");
            }
        }
    }
}