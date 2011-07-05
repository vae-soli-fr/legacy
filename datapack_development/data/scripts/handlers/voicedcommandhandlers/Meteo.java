package handlers.voicedcommandhandlers;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.datatables.MapRegionTable;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
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
            try
            {
                URL script = new URL("http://www.vae-soli.fr/meteo/index.php?mode=java");
                URLConnection scriptc = script.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(scriptc.getInputStream()));
                in.close();
                
                String town = getTown(activeChar);
                Connection con = null;
                try
                {
                    con = L2DatabaseFactory.getInstance().getConnection();
                    PreparedStatement statement = con.prepareStatement("SELECT temperature, sky FROM meteo WHERE town = ?");
                    statement.setString(1, town);
                    ResultSet rset = statement.executeQuery();
                    if(rset.next())
                    {
                        int temperature = rset.getInt("temperature");
                        int sky = rset.getInt("temperature");
                        String temp1;
                        temp1 = " il fait " + temperature + " degrés, et ";
                        String temp2;
                        if(sky == 0)
                            temp2 = "le ciel est nuageux.";
                        else if(sky == 1)
                            temp2 = "le ciel est ensoleillé.";
                        else
                        {
                            if(temperature > 2)
                                temp2 = "il pleut.";
                            else
                                temp2 = "il neige.";
                        }
                        activeChar.sendMessage("À " + town + temp1 + temp2);
                    }
                }
                catch (Exception e)
                {
                    activeChar.sendMessage("Erreur niveau 2 : "+ e.getLocalizedMessage() + e.getMessage());
                }
            }
            catch (Exception e)
            {
                activeChar.sendMessage("Erreur niveau 1 : "+ e.getLocalizedMessage() + e.getMessage());
            }
        }
        return true;
    }
    
    private String getTown(L2PcInstance activeChar)
    {
        int nearestTown = MapRegionTable.getInstance().getClosestTownNumber(activeChar);
        String town;
        switch (nearestTown)
        {
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
