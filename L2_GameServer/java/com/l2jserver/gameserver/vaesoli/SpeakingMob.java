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
 * Cette classe sert à faire parler les mobs selon leur race
 * @author Kevin
 */
public class SpeakingMob {

        /**
         *  Cette interne classe permet de wrapper (et remplir) la HashMap avec
         *  des variables constantes pour éviter des calculs répétitifs
         */
        private class Speech {
        private HashMap<Integer, String> map;
        private final boolean isEmpty;
        private final int size;
        private final String path;
        public Speech(String path) {
            this.map = new HashMap<Integer, String>();
            this.path = path;
            this.fillMap();
            this.isEmpty = this.map.isEmpty();
            this.size = this.map.size();
        }
        private void fillMap() {
        LineNumberReader lnr = null;
        try {
            File speechData = new File(Config.DATAPACK_ROOT, this.path);
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
            _log.info(this.path + ": Loaded " + this.map.size() + " phrases.");
        } catch (FileNotFoundException e) {
            _log.warning(path + " is missing !");
        } catch (IOException e) {
            _log.log(Level.WARNING, "Error while loading file " + e.getMessage(), e);
        } finally {
            try {
                lnr.close();
            } catch (Exception e) {
            }
        }
        }
        public boolean getIsEmpty() {
            return this.isEmpty;
        }
        public int getSize() {
            return this.size;
        }
        public String getValue(int key) {
            return this.map.get(key);
        }
        }

    private static final Logger _log = Logger.getLogger(SpeakingMob.class.getName());
    Speech _ANGEL;
    Speech _ANIMAL;
    Speech _BEAST;
    Speech _BUG;
    Speech _DARKELVE;
    Speech _DEFENDINGARMY;
    Speech _DEMON;
    Speech _DRAGON;
    Speech _DWARVE;
    Speech _ELVE;
    Speech _FAIRIE;
    Speech _GIANT;
    Speech _HUMAN;
    Speech _HUMANOID;
    Speech _KAMAEL;
    Speech _MAGICCREATURE;
    Speech _MERCENARIE;
    Speech _NONE;
    Speech _NONLIVING;
    Speech _ORC;
    Speech _OTHER;
    Speech _PLANT;
    Speech _SIEGEWEAPON;
    Speech _SPIRIT;
    Speech _UNDEAD;
    Speech _UNKNOWN;
    public static SpeakingMob getInstance() {
    return SingletonHolder.INSTANCE;
    }
    private static class SingletonHolder {
    protected static final SpeakingMob INSTANCE = new SpeakingMob();
    }
    private SpeakingMob() {
        reloadAll();
        }
    public void reloadAll() {
    _ANGEL = new Speech("data/speechs/angel.csv");
    _ANIMAL = new Speech("data/speechs/animal.csv");
    _BEAST = new Speech("data/speechs/beast.csv");
    _BUG = new Speech("data/speechs/bug.csv");
    _DARKELVE = new Speech("data/speechs/darkelve.csv");
    _DEFENDINGARMY = new Speech("data/speechs/defendingarmy.csv");
    _DEMON = new Speech("data/speechs/demon.csv");
    _DRAGON = new Speech("data/speechs/dragon.csv");
    _DWARVE = new Speech("data/speechs/dwarve.csv");
    _ELVE = new Speech("data/speechs/elve.csv");
    _FAIRIE = new Speech("data/speechs/fairie.csv");
    _GIANT = new Speech("data/speechs/giant.csv");
    _HUMAN = new Speech("data/speechs/human.csv");
    _HUMANOID = new Speech("data/speechs/humanoid.csv");
    _KAMAEL = new Speech("data/speechs/kamael.csv");
    _MAGICCREATURE = new Speech("data/speechs/magiccreature.csv");
    _MERCENARIE = new Speech("data/speechs/mercenarie.csv");
    _NONE = new Speech("data/speechs/none.csv");
    _NONLIVING = new Speech("data/speechs/nonliving.csv");
    _ORC = new Speech("data/speechs/orc.csv");
    _OTHER = new Speech("data/speechs/other.csv");
    _PLANT = new Speech("data/speechs/plant.csv");
    _SIEGEWEAPON = new Speech("data/speechs/siegeweapon.csv");
    _SPIRIT = new Speech("data/speechs/spirit.csv");
    _UNDEAD = new Speech("data/speechs/undead.csv");
    _UNKNOWN = new Speech("data/speechs/unknown.csv");
    }
    private boolean probability() {
    return Rnd.get(1, 100) <= Config.VAEMOD_VOTESCHECK; //20% chance
    }
    private void doSpeak(L2Npc npc, Speech speech) {
       if (!speech.getIsEmpty()) npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.ALL, npc.getName(), speech.getValue(Rnd.get(speech.getSize()))));
    }
    public void roleplaying(L2Npc npc) {
        if (Config.VAEMOD_SPEAKINGMOB > 0 && probability() && npc instanceof L2MonsterInstance) {
            switch (npc.getTemplate().getRace()) {
                case ANGEL: doSpeak(npc, _ANGEL); break;
                case ANIMAL: doSpeak(npc, _ANIMAL); break;
                case BEAST: doSpeak(npc, _BEAST); break;
                case BUG: doSpeak(npc, _BUG); break;
                case DARKELVE: doSpeak(npc, _DARKELVE); break;
                case DEFENDINGARMY: doSpeak(npc, _DEFENDINGARMY); break;
                case DEMON: doSpeak(npc, _DEMON); break;
                case DRAGON: doSpeak(npc, _DRAGON); break;
                case DWARVE: doSpeak(npc, _DWARVE); break;
                case ELVE: doSpeak(npc, _ELVE); break;
                case FAIRIE: doSpeak(npc, _FAIRIE); break;
                case GIANT: doSpeak(npc, _GIANT); break;
                case HUMAN: doSpeak(npc, _HUMAN); break;
                case HUMANOID: doSpeak(npc, _HUMANOID); break;
                case KAMAEL: doSpeak(npc, _KAMAEL); break;
                case MAGICCREATURE: doSpeak(npc, _MAGICCREATURE); break;
                case MERCENARIE: doSpeak(npc, _MERCENARIE); break;
                default:
                case NONE: doSpeak(npc, _NONE); break;
                case NONLIVING: doSpeak(npc, _NONLIVING); break;
                case ORC: doSpeak(npc, _ORC); break;
                case OTHER: doSpeak(npc, _OTHER); break;
                case PLANT: doSpeak(npc, _PLANT); break;
                case SIEGEWEAPON: doSpeak(npc, _SIEGEWEAPON); break;
                case SPIRIT: doSpeak(npc, _SPIRIT); break;
                case UNDEAD: doSpeak(npc, _UNDEAD); break;
                case UNKNOWN: doSpeak(npc, _UNKNOWN); break;
            }
        }
    }
}