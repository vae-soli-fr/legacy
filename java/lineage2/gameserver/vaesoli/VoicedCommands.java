package lineage2.gameserver.vaesoli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.Say2;
import lineage2.gameserver.network.serverpackets.components.ChatType;

public class VoicedCommands implements IVoicedCommandHandler {

    private static final String[] _voicedCommands = {
        "camp",
    	"chuchote",
    	"cri",
    	"desc",
    	"elfe",
    	"sombre",
    	"nain",
    	"orc",
    	"kamael",
    	"meteo",
    	">>",
    	"time",
    	"titre"
    };

	public boolean useVoicedCommand(String command, Player activeChar, String option) {
		switch (command) {
		case "camp":
			camp(activeChar);
			break;
		case "chuchote":
			volume(activeChar, RpVolume.WHISPER);
			break;
		case "cri":
			volume(activeChar, RpVolume.SHOUT);
			break;
		case "desc":
			desc(activeChar, option);
			break;
		case "elfe":
			language(activeChar, RpLanguage.ELVEN);
			break;
		case "sombre":
			language(activeChar, RpLanguage.DROW);
			break;
		case "nain":
			language(activeChar, RpLanguage.DWARVEN);
			break;
		case "orc":
			language(activeChar, RpLanguage.ORCISH);
			break;
		case "kamael":
			language(activeChar, RpLanguage.KAMAEL);
			break;
		case "meteo":
			meteo(activeChar);
			break;
		case ">>":
			petsays(activeChar, option);
			break;
		case "time":
			time(activeChar);
			break;
		case "titre":
			titre(activeChar, option);
			break;
		}
		return true;
	}
    
    private void titre(Player player, String titre) {
    	player.setTitle(titre);
    	player.sendChanges();
	}

	private void time(Player player) {
    	long basetime = 1167606000; // 1.1.2007
        long actualtime = System.currentTimeMillis() / 1000;
        long elapsed = actualtime - basetime; // Time elapsed
        long elapsed_ig = elapsed * 4; // Temps écoulé IG

        int year = 31 + (int) Math.floor(elapsed_ig / (12 * 30 * 24 * 3600)); // Year, basetime being 31.

        int month = (int) Math.floor(elapsed_ig / (30 * 24 * 3600)) % 12; // Month from 0 to 11
        int day = 1 + (int) Math.floor(elapsed_ig / (24 * 3600)) % 30; // Day from from 1 to 30
        int dayow = (int) Math.floor(elapsed_ig / (24 * 3600)) % 6; // Day of week from 0 to 5

        String[] months = {
            "Tombeglace",
            "Blancheterre",
            "Fondgivre",
            "L'Astrée",
            "Vertefeuille",
            "Brilleblé",
            "Brûleblé",
            "Astredoux",
            "Tourneterre",
            "Rougefeuille",
            "Tombefeuille",
            "Souffleglace"};
        String[] days = {"Lunem", "Marka", "Metri", "Jeriel", "Verdel", "Sumbra"};
        String message = days[dayow] + ", le " + day + " " + months[month] + " de l'an " + year;
        player.sendMessage(message);
	}

	private void petsays(Player player, String phrase) {
		Summon pet = player.getSummonList().getPet();
		if (pet != null) {
			if (phrase != null) {
				if (pet.getName() != null) {
					pet.broadcastPacket(new Say2(pet.getNpcId(), ChatType.ALL, "[" + pet.getName() + "]", phrase));
				} else {
					player.sendMessage("Votre animal ne possède pas de nom.");
				}
			} else {
				player.sendMessage("Usage : .>> texte roleplay");
			}
		} else {
			player.sendMessage("Vous n'avez pas d'animal de compagnie.");
		}
	}

	private void meteo(Player activeChar) {
        if (activeChar.getTarget() == null) {
            String bulletin = "<html><title>Météo</title><body><center><table><tr><td></td><td><center>Ville</center></td><td><center>Temp.</center></td><td><center>Ciel</center></td></tr><tr></tr>";
            Connection con = null;
            try {
                URL script = new URL("http://www.vae-soli.fr/meteo/index.php?mode=java");
                URLConnection scriptc = script.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(scriptc.getInputStream()));
                in.close();

                con = DatabaseFactory.getInstance().getConnection();
                PreparedStatement statement = con.prepareStatement("SELECT * FROM meteo");
                ResultSet rset = statement.executeQuery();
                while (rset.next()) {

                    String town = rset.getString("town").toLowerCase().replace("'", "");
                    int temp = rset.getInt("temperature");
                    int sky = rset.getInt("sky");

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
            NpcHtmlMessage htmlMsg = new NpcHtmlMessage(0);
			htmlMsg.setHtml(bulletin);
            activeChar.sendPacket(htmlMsg);
        } else {
            activeChar.sendMessage("Vous ne devez pas avoir de cible.");
        }
	}

	private void language(Player player, RpLanguage langue) {
		if (player.getRpLanguage() == langue)
		{
			player.setRpLanguage(RpLanguage.COMMON);
			player.sendMessage("Vous parlez en commun.");
		} else {
			player.setRpLanguage(langue);
			player.sendMessage("Vous parlez en" + langue + ".");
		}
	}

	private void volume(Player player, RpVolume volume) {
		if (player.getRpVolume() == volume)
		{
			player.setRpVolume(RpVolume.DEFAULT);
			player.sendMessage("Vous parlez normalement.");
		} else {
			player.setRpVolume(volume);
			player.sendMessage("Vous parlez en" + volume + ".");
		}
    }
       
	private void desc(Player player, String option) {
		switch (option) {
		case "add":
			//TODO
			// nothing for now
			break;
		case "delete":
			Descriptions.delDesc(player);
			player.sendMessage("Votre description a été supprimée.");
			break;
		case "show":
			if (player.getTarget() != null && player.getTarget() instanceof Player)
				Descriptions.showDesc((Player) player.getTarget(), player);
			else
				player.sendMessage("Sélectionnez un joueur pour voir sa description.");
			break;
		default:
			player.sendMessage("Usage: .desc [delete]");
		}
	}
    
    private void camp(Player player) {
    	player.getCamp().evolve(player);
    }

    public String[] getVoicedCommandList() {
        return _voicedCommands;
    }
}