package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.util.Rnd;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin
 */
public class SpeakingMob {

    private static final Logger _log = Logger.getLogger(SpeakingMob.class.getName());
    private HashMap<Integer, String> _ANGEL = new HashMap<Integer, String>();
    private HashMap<Integer, String> _ANIMAL = new HashMap<Integer, String>();
    private HashMap<Integer, String> _BEAST = new HashMap<Integer, String>();
    private HashMap<Integer, String> _BUG = new HashMap<Integer, String>();
    private HashMap<Integer, String> _DARKELVE = new HashMap<Integer, String>();
    private HashMap<Integer, String> _DEFENDINGARMY = new HashMap<Integer, String>();
    private HashMap<Integer, String> _DEMON = new HashMap<Integer, String>();
    private HashMap<Integer, String> _DRAGON = new HashMap<Integer, String>();
    private HashMap<Integer, String> _DWARVE = new HashMap<Integer, String>();
    private HashMap<Integer, String> _ELVE = new HashMap<Integer, String>();
    private HashMap<Integer, String> _FAIRIE = new HashMap<Integer, String>();
    private HashMap<Integer, String> _GIANT = new HashMap<Integer, String>();
    private HashMap<Integer, String> _HUMAN = new HashMap<Integer, String>();
    private HashMap<Integer, String> _HUMANOID = new HashMap<Integer, String>();
    private HashMap<Integer, String> _KAMAEL = new HashMap<Integer, String>();
    private HashMap<Integer, String> _MAGICCREATURE = new HashMap<Integer, String>();
    private HashMap<Integer, String> _MERCENAIRE = new HashMap<Integer, String>();
    private HashMap<Integer, String> _NONE = new HashMap<Integer, String>();
    private HashMap<Integer, String> _NONLIVING = new HashMap<Integer, String>();
    private HashMap<Integer, String> _ORC = new HashMap<Integer, String>();
    private HashMap<Integer, String> _OTHER = new HashMap<Integer, String>();
    private HashMap<Integer, String> _PLANT = new HashMap<Integer, String>();
    private HashMap<Integer, String> _SIEGEWEAPON = new HashMap<Integer, String>();
    private HashMap<Integer, String> _SPIRIT = new HashMap<Integer, String>();
    private HashMap<Integer, String> _UNDEAD = new HashMap<Integer, String>();
    private HashMap<Integer, String> _UNKNOWN = new HashMap<Integer, String>();

    private boolean _angelIsEmpty;
    private boolean _animalIsEmpty;
    private boolean _beastIsEmpty;
    private boolean _dugIsEmpty;
    private boolean _darkelveIsEmpty;
    private boolean _defendingarmyIsEmpty;


    private SpeakingMob() {
        load();
        }

    public void roleplaying(L2Npc npc) {
        if (probability() && npc instanceof L2MonsterInstance) {
            switch (npc.getTemplate().getRace()) {
                case ANGEL:
                    if (!_ANGEL.isEmpty()) {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _ANGEL.get(Rnd.get(_ANGEL.size()))));
                    }
                    break;
                case ANIMAL:
                    if (!_ANIMAL.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _ANIMAL.get(Rnd.get(_ANIMAL.size()))));
                    }
                    break;
                case BEAST:
                    if (!_BEAST.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _BEAST.get(Rnd.get(_BEAST.size()))));
                    }
                    break;
                case BUG:
                    if (!_BUG.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _BUG.get(Rnd.get(_BUG.size()))));
                    }
                    break;
                case DARKELVE:
                    if (!_DARKELVE.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _DARKELVE.get(Rnd.get(_DARKELVE.size()))));
                    }
                    break;
                case DEFENDINGARMY:
                    if (!_DEFENDINGARMY.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _DEFENDINGARMY.get(Rnd.get(_DEFENDINGARMY.size()))));
                    }
                    break;
                case DEMON:
                    if (!_DEMON.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _DEMON.get(Rnd.get(_DEMON.size()))));
                    }
                    break;
                case DRAGON:
                    if (!_DRAGON.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _DRAGON.get(Rnd.get(_DRAGON.size()))));
                    }
                    break;
                case DWARVE:
                    if (!_DWARVE.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _DWARVE.get(Rnd.get(_DWARVE.size()))));
                    }
                    break;
                case ELVE:
                    if (!_ELVE.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _ELVE.get(Rnd.get(_ELVE.size()))));
                    }
                    break;
                case FAIRIE:
                    if (!_FAIRIE.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _FAIRIE.get(Rnd.get(_FAIRIE.size()))));
                    }
                    break;
                case GIANT:
                    if (!_GIANT.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _GIANT.get(Rnd.get(_GIANT.size()))));
                    }
                    break;
                case HUMAN:
                    if (!_HUMAN.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _HUMAN.get(Rnd.get(_HUMAN.size()))));
                    }
                    break;
                case HUMANOID:
                    if (!_HUMANOID.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _HUMANOID.get(Rnd.get(_HUMANOID.size()))));
                    }
                    break;
                case KAMAEL:
                    if (!_KAMAEL.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _KAMAEL.get(Rnd.get(_KAMAEL.size()))));
                    }
                    break;
                case MAGICCREATURE:
                    if (!_MAGICCREATURE.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _MAGICCREATURE.get(Rnd.get(_MAGICCREATURE.size()))));
                    }
                    break;
                case MERCENARIE:
                    if (!_MERCENAIRE.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _MERCENAIRE.get(Rnd.get(_MERCENAIRE.size()))));
                    }
                    break;
                default:

                case NONE:
                    if (!_NONE.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _NONE.get(Rnd.get(_NONE.size()))));
                    }
                    break;
                case NONLIVING:
                    if (!_NONLIVING.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _NONLIVING.get(Rnd.get(_NONLIVING.size()))));
                    }
                    break;
                case ORC:
                    if (!_ORC.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _ORC.get(Rnd.get(_ORC.size()))));
                    }
                    break;
                case OTHER:
                    if (!_OTHER.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _OTHER.get(Rnd.get(_OTHER.size()))));
                    }
                    break;
                case PLANT:
                    if (!_PLANT.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _PLANT.get(Rnd.get(_PLANT.size()))));
                    }
                    break;
                case SIEGEWEAPON:
                    if (!_SIEGEWEAPON.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _SIEGEWEAPON.get(Rnd.get(_SIEGEWEAPON.size()))));
                    }
                    break;
                case SPIRIT:
                    if (!_SPIRIT.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _SPIRIT.get(Rnd.get(_SPIRIT.size()))));
                    }
                    break;
                case UNDEAD:
                    if (!_UNDEAD.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _UNDEAD.get(Rnd.get(_UNDEAD.size()))));
                    }
                    break;
                case UNKNOWN:
                    if (!_UNKNOWN.isEmpty())  {
                        npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), _UNKNOWN.get(Rnd.get(_UNKNOWN.size()))));
                    }
                    break;
            }
        }
    }

    public static SpeakingMob getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {

        protected static final SpeakingMob INSTANCE = new SpeakingMob();
    }

    private void load() {
        loadPhrases(_ANGEL, "data/speech/angel.csv");
        loadPhrases(_ANIMAL, "data/speech/animal.csv");
        loadPhrases(_BEAST, "data/speech/beast.csv");
        loadPhrases(_BUG, "data/speech/bug.csv");
        loadPhrases(_DARKELVE, "data/speech/darkelve.csv");
        loadPhrases(_DEFENDINGARMY, "data/speech/defendingarmy.csv");
        loadPhrases(_DEMON, "data/speech/demon.csv");
        loadPhrases(_DRAGON, "data/speech/dragon.csv");
        loadPhrases(_DWARVE, "data/speech/dwarve.csv");
        loadPhrases(_ELVE, "data/speech/elve.csv");
        loadPhrases(_FAIRIE, "data/speech/fairie.csv");
        loadPhrases(_GIANT, "data/speech/giant.csv");
        loadPhrases(_HUMAN, "data/speech/human.csv");
        loadPhrases(_HUMANOID, "data/speech/humanoid.csv");
        loadPhrases(_KAMAEL, "data/speech/kamael.csv");
        loadPhrases(_MAGICCREATURE, "data/speech/magiccreature");
        loadPhrases(_MERCENAIRE, "data/speech/mercenaire.csv");
        loadPhrases(_NONE, "data/speech/none.csv");
        loadPhrases(_NONLIVING, "data/speech/nonliving.csv");
        loadPhrases(_ORC, "data/speech/orc.csv");
        loadPhrases(_OTHER, "data/speech/other.csv");
        loadPhrases(_PLANT, "data/speech/plant.csv");
        loadPhrases(_SIEGEWEAPON, "data/speech/siegeweapon.csv");
        loadPhrases(_SPIRIT, "data/speech/spirit.csv");
        loadPhrases(_UNDEAD, "data/speech/undead.csv");
        loadPhrases(_UNKNOWN, "data/speech/unknown.csv");
    }

    private boolean probability() {
        return Rnd.get(1, 100) <= Config.VAEMOD_VOTESCHECK; //20% chance
    }

    private void loadPhrases(HashMap map, String path) {
        LineNumberReader lnr = null;
        try {
            File speechData = new File(Config.DATAPACK_ROOT, path);
            lnr = new LineNumberReader(new BufferedReader(new FileReader(speechData)));

            String line = null;
            int cle = 0;

            while ((line = lnr.readLine()) != null) {
                if (line.trim().length() == 0 || line.startsWith("#")) {
                    continue;
                }
                map.put(cle, line);
                cle++;
            }
            _log.info(path + " Loaded : " + map.size() + " phrases.");
        } catch (FileNotFoundException e) {
            _log.warning(path + " is missing");
        } catch (IOException e) {
            _log.log(Level.WARNING, "Error while loading speech " + e.getMessage(), e);
        } finally {
            try {
                lnr.close();
            } catch (Exception e) {
            }
        }
    }

    public void reloadAll() {
    _ANGEL.clear();
    _ANIMAL.clear();
    _BEAST.clear();
    _BUG.clear();
    _DARKELVE.clear();
    _DEFENDINGARMY.clear();
    _DEMON.clear();
    _DRAGON.clear();
    _DWARVE.clear();
    _ELVE.clear();
    _FAIRIE.clear();
    _GIANT.clear();
    _HUMAN.clear();
    _HUMANOID.clear();
    _KAMAEL.clear();
    _MAGICCREATURE.clear();
    _MERCENAIRE.clear();
    _NONE.clear();
    _NONLIVING.clear();
    _ORC.clear();
    _OTHER.clear();
    _PLANT.clear();
    _SIEGEWEAPON.clear();
    _SPIRIT.clear();
    _UNDEAD.clear();
    _UNKNOWN.clear();
    load();
    }
}
