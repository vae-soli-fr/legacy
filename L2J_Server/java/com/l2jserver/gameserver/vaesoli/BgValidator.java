package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.cache.HtmCache;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance.PunishLevel;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import gnu.trove.TIntArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Melua
 * Cette classe vérifie que les BG sont à jour et validés
 * concernant les transfos, les subs, et bientôt les BG tout court.
 *
 */
public class BgValidator {

    private final static Logger _log = Logger.getLogger(BgValidator.class.getName());
    private final TIntArrayList _transforms; // liste des skills de transfo

    public static BgValidator getInstance() {
        return SingletonHolder._instance;
    }

    private BgValidator() {
        int skillId[] = {
            541, // Transform Grail Apostle
            542, // Transform Grail Apostle
            543, // Transform Grail Apostle
            544, // Transform Unicorn
            545, // Transform Unicorn
            546, // Transform Unicorn
            547, // Transform Lilim Knight
            548, // Transform Lilim Knight
            549, // Transform Lilim Knight
            550, // Transform Golem Guardian
            551, // Transform Golem Guardian
            552, // Transform Golem Guardian
            553, // Transform Inferno Drake
            554, // Transform Inferno Drake
            555, // Transform Inferno Drake
            556, // Transform Dragon Bomber
            557, // Transform Dragon Bomber
            558, // Transform Dragon Bomber
            617, // Transform Onyx Beast
            618, // Transform Death Blader
            663, // Transform Zaken
            664, // Transform Anakim
            665, // Transform Benom
            666, // Transform Gordon
            667, // Transform Ranku
            668, // Transform Kiyachi
            669, // Transform Demon Prince
            670, // Transform Heretic
            671, // Transform Vale Master
            672, // Transform Saber Tooth Tiger
            673, // Transform Ol Mahum
            674, // Transform Doll Blader
            656, // Divine Transform Warrior
            657, // Divine Transform Knight
            658, // Divine Transform Rogue
            659, // Divine Transform Wizard
            660, // Divine Transform Summoner
            661, // Divine Transform Healer
            662, // Divine Transform Enchanter
            538 // Final Form
        };
        _transforms = new TIntArrayList(skillId.length);
        _transforms.add(skillId);
        _log.log(Level.INFO, "Loaded {0} transform skills to check.", _transforms.size());
    }

    private boolean isGuilty(L2PcInstance player) {
        Connection con = null;
        boolean guilty1 = false;
        boolean guilty2 = false;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement1 = con.prepareStatement("SELECT level, verified FROM character_subclasses WHERE charId = ?");
            PreparedStatement statement2 = con.prepareStatement("SELECT skill_id, SUM(verified) as verified FROM character_skills WHERE charId = ? GROUP BY skill_id");
            statement1.setInt(1, player.getObjectId());
            statement2.setInt(1, player.getObjectId());
            ResultSet rset1 = statement1.executeQuery();
            ResultSet rset2 = statement2.executeQuery();

            while (rset1.next()) {
                if (rset1.getInt("verified") == 0 && rset1.getInt("level") > 40) {
                    guilty1 = true;
                    continue;
                }
            }

            while (rset2.next()) {
                if (_transforms.contains(rset2.getInt("skill_id")) && rset2.getInt("verified") == 0) {
                    guilty2 = true;
                    continue;
                }
            }

            rset1.close();
            rset2.close();
            statement1.close();
            statement2.close();
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

        return (guilty1 || guilty2);
    }

    public void check(L2PcInstance activeChar) {
        if (activeChar.isGM() || !Config.VAEMOD_BGJAIL) {
            return;
        }
        if (isGuilty(activeChar) && activeChar.getPunishLevel() == PunishLevel.BG)
        {
            // Just open a Html message to inform the player
            NpcHtmlMessage htmlMsg = new NpcHtmlMessage(0);
            String jailInfos = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/jail_in.htm");
            if (jailInfos != null) {
                htmlMsg.setHtml(jailInfos);
            } else {
                htmlMsg.setHtml("<html><body>You have been put in jail by an admin.</body></html>");
            }
            activeChar.sendPacket(htmlMsg);
            _log.info(("Check BG: " + activeChar.getName() + " of account " + activeChar.getAccountName() + " is JAIL"));
        } else if (isGuilty(activeChar) && activeChar.getPunishLevel() != PunishLevel.BG) {
            // Jail the player
            activeChar.setPunishLevel(PunishLevel.BG, 0);
            _log.info(("Check BG: " + activeChar.getName() + " of account " + activeChar.getAccountName() + " is JAIL"));
        } else if (!isGuilty(activeChar) && activeChar.getPunishLevel() == PunishLevel.BG) {
            // Unjail the player
            activeChar.setPunishLevel(PunishLevel.NONE, 0);
            _log.info(("Check BG: " + activeChar.getName() + " of account " + activeChar.getAccountName() + " is UNJAIL"));
        }
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final BgValidator _instance = new BgValidator();
    }
}
