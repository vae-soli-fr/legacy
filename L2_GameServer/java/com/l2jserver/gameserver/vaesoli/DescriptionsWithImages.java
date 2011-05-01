package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.idfactory.IdFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.PledgeCrest;
import com.sun.opengl.util.texture.spi.DDSImage.ImageInfo;
import gov.nasa.worldwind.formats.dds.DDSConverter;
import java.io.File;
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
        Pattern pattern = Pattern.compile("<img_int>[0-9a-zA-Z]+</img_int>"); //TODO: isoler le nom du reste !!!
        Matcher matcher = pattern.matcher(description);
        while(matcher.find())
            {
                try
                {
                String img_int = matcher.group();
                int imgId = IdFactory.getInstance().getNextId();
                File image = new File("data/images/" + target.getName() + "/" + img_int);
                ImageIcon info = new ImageIcon("data/images/" + target.getName() + "/" + img_int);
                PledgeCrest packet = new PledgeCrest(imgId, DDSConverter.convertToDDS(image).array());
                description.replace(img_int, "<img src=\"Crest.crest_" + Config.SERVER_ID + "_" + imgId + "\" width=" + info.getIconWidth() + " height=" + info.getIconHeight() + ">");
                // envoyer le DDS au client
                viewer.sendPacket(packet);
                }
                catch (Exception e)
                {
                    _log.warning(e.getMessage());
                }
        }
        // finalement envoyer le html et le tour est joué !
        if (description != null)
        {
        NpcHtmlMessage html = new NpcHtmlMessage(1);
        html.setHtml("<html><title>" + target.getName() + "</title><body>" + description + "</body></html>");
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
        // récupérer l'URL et télécharger
        // vérifier la taille de l'image (puissance de 2)
        // donner un ID à l'image et màj la desc
        String description = getDesc(activeChar);
        Pattern pattern = Pattern.compile("<img_ext>http://[0-9a-zA-Z]+(.png|.jpg|.bmp)</img_ext>"); // toto : isoler l'url du reste !! plus loin
        Matcher matcher = pattern.matcher(description);
        while (matcher.find()) {
            try {
                String img_ext = matcher.group();
                // vérifier la taille (puissance de 2)
                ImageIcon info = new ImageIcon(img_ext); // URL
                if ((info.getIconHeight() > 0 && (info.getIconHeight() & (info.getIconHeight() - 1)) == 0) && (info.getIconWidth() > 0 && (info.getIconWidth() & (info.getIconWidth() - 1)) == 0)) {
                    //TODO: telecharger avec l'URL trouvée
                    //TODO: enregistrer l'image (créer le dossier le cas échéant)
                    // remplacer la desc
                    description.replace(img_ext, "<img_int>" + "nom" + "<img_int>"); // mettre le nom
                }
            } catch (Exception e) {
                _log.warning(e.getMessage());
            }
        }
    }
}