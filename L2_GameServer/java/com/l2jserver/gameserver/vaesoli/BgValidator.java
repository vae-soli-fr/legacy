package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import java.util.logging.Logger;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance.PunishLevel;
import gnu.trove.TIntArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Melua
 * Mort aux GBs !
 *
 */
public class BgValidator {

    private final static Logger _log = Logger.getLogger(BgValidator.class.getName());
    private final TIntArrayList _transforms; // liste des skills de transfo

    public static final BgValidator getInstance() {
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
            661, // Divine Transform Healer
            660, // Divine Transform Summoner
            662, // Divine Transform Enchanter
            538 // Final Form
        };
        _transforms = new TIntArrayList(skillId.length);
        _transforms.add(skillId);
        _log.info("Loaded " + _transforms.size() + " transform skills to check.");
    }

    private boolean isGuilty(L2PcInstance player) {
        Connection con = null;
        boolean guilty1 = false;
        boolean guilty2 = false;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement1 = con.prepareStatement("SELECT level, verified FROM character_subclasses WHERE charId = ?");
            PreparedStatement statement2 = con.prepareStatement("SELECT skill_id, verified FROM character_skills WHERE charId = ?");
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
        // TODO: don't check if GM
        if (!Config.VAEMOD_BGJAIL) {
            return;
        }
        // mettre au préalable PunishTimer en non UNSIGNED dans la database
        if (isGuilty(activeChar)) {
            activeChar.setPunishLevel(PunishLevel.BG, 0);
            _log.info(("Check BG: " + activeChar.getName() + " of account " + activeChar.getAccountName() + " is JAIL"));
        }
        else if (activeChar.getPunishLevel() == PunishLevel.BG) {
            activeChar.setPunishLevel(PunishLevel.NONE, 0);
             _log.info(("Check BG: " + activeChar.getName() + " of account " + activeChar.getAccountName() + " is UNJAIL"));
        }
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final BgValidator _instance = new BgValidator();
    }
}
