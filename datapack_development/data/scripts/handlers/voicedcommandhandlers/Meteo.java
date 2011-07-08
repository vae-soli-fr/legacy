package handlers.voicedcommandhandlers;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.datatables.MapRegionTable;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Inform player of weather info
 * and update database through rpc.
 * 
 * @author Saelil
 */
public class Meteo implements IVoicedCommandHandler {

    private static final String[] _voicedCommands = {
        "meteo"
    };

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String option) {
        if (command.equalsIgnoreCase("meteo")) {

            if (activeChar.getTarget() == null) {

                String bulletin = "<html><title>Météo</title><body><center><table><tr><td></td><td><center>Ville</center></td><td><center>Temp.</center></td><td><center>Ciel</center></td></tr><tr></tr>";
                Connection con = null;
                String location = getTown(activeChar);

                try {
                    // update nextchange
                    URL script = new URL("http://www.vae-soli.fr/meteo/index.php?mode=java");
                    URLConnection scriptc = script.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(scriptc.getInputStream()));
                    in.close();

                    //String town = getTown(activeChar);

                    con = L2DatabaseFactory.getInstance().getConnection();
                    PreparedStatement statement = con.prepareStatement("SELECT * FROM meteo");
                    ResultSet rset = statement.executeQuery();
                    while (rset.next()) {

                        String town = rset.getString("town").toLowerCase().replace("'", "");
                        int temp = rset.getInt("temperature");
                        int sky = rset.getInt("sky");

                        if (town.equalsIgnoreCase(location)) {
                            bulletin += "<tr><td><img src=\"l2ui.MinimapWnd.TargetPos\" width=32 height=32></td>";
                        } else {
                            bulletin += "<tr><td></td>";
                        }

                        bulletin += "<td><img_int>town_" + town + ".png</img_int></td>";
                        bulletin += "<td><img_int>temp_" + temp + ".png</img_int></td>";

                        if (sky == 0) {
                            bulletin += "<td><img_int>sky_clouds.png</img_int></td></tr><tr></tr>";

                        } else if (sky == 1) {
                            bulletin += "<td><img_int>sky_sun.png</img_int></td></tr><tr></tr>";

                        } else if (sky == 2 && temp > 2) {
                            bulletin += "<td><img_int>sky_rain.png</img_int></td></tr><tr></tr>";

                        } else if (sky == 2 && temp <= 2) {
                            bulletin += "<td><img_int>sky_snow.png</img_int></td></tr><tr></tr>";

                        }
                    }
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
                bulletin += "</table></center></body></html>";
                NpcHtmlMessage html = new NpcHtmlMessage(1, bulletin);
                activeChar.sendPacket(html);
            } else {
                activeChar.sendMessage("Vous ne devez pas avoir de cible.");
            }
        }
        return true;
    }

    private String getTown(L2PcInstance activeChar) {
        int nearestTown = MapRegionTable.getInstance().getClosestTownNumber(activeChar);
        String town;
        switch (nearestTown) {
            case 0:
                town = "Aden";
                break;
            case 1:
                town = "Cefedelen";
                break;
            case 2:
                town = "Shel'Oloth";
                break;
            case 3:
                town = "Naarg'Dum";
                break;
            case 4:
                town = "Hindemith";
                break;
            case 5:
                town = "Gludio";
                break;
            case 6:
                town = "Gludin";
                break;
            case 7:
                town = "Dion";
                break;
            case 8:
                town = "Giran";
                break;
            case 9:
                town = "Oren";
                break;
            case 10:
                town = "Aden";
                break;
            case 11:
                town = "Hunter";
                break;
            case 12:
                town = "Giran";
                break;
            case 13:
                town = "Heine";
                break;
            case 14:
                town = "Rune";
                break;
            case 15:
                town = "Goddard";
                break;
            case 16:
                town = "Schuttgart";
                break;
            case 17:
                town = "Floran";
                break;
            case 18:
                town = "Rune";
                break;
            case 19:
                town = "Gevurah";
                break;
            case 20:
                town = "Gludio";
                break;
            case 21:
                town = "Heine";
                break;
            case 22:
                town = "Gludio";
                break;
            case 23:
                town = "Aden";
                break;
            case 24:
            case 25:
                town = "Althena";
                break;
            case 26:
                town = "Aden";
                break;
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
                town = "Althena";
                break;
            default:
                town = "Aden";
        }
        return town;
    }

    public String[] getVoicedCommandList() {
        return _voicedCommands;
    }
}
