package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.actor.instance.L2GrandBossInstance;
import com.l2jserver.gameserver.model.actor.instance.L2RaidBossInstance;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.util.Rnd;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.util.FastMap;

/**
 *
 * Cette classe sert à faire parler les mobs selon leur race
 * @author Kevin
 */
public class RaidBossSpeeches {

    /**
     *  Cette interne classe permet de wrapper (et remplir) la HashMap avec
     *  des variables constantes pour éviter des calculs répétitifs
     */
    private class Speech {

        private FastMap<Integer, String> map;
        private final boolean isEmpty;
        private final int size;
        private final String path;

        public Speech(String path) {
            this.map = new FastMap<>();
            this.path = path;
            this.fillMap();
            this.isEmpty = this.map.isEmpty();
            this.size = this.map.size();
        }

        private void fillMap() {
            LineNumberReader lnr = null;
            try {
                File speechData = new File(this.path);
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
                _log.log(Level.INFO, "{0}: Loaded {1} phrases.", new Object[]{this.path, this.map.size()});
            } catch (FileNotFoundException e) {
                _log.log(Level.WARNING, "{0} is missing !", this.path);
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
    private static final Logger _log = Logger.getLogger(RaidBossSpeeches.class.getName());
    private FastMap<Integer, Speech> _speeches;

    public static RaidBossSpeeches getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {

        protected static final RaidBossSpeeches INSTANCE = new RaidBossSpeeches();
    }

    private RaidBossSpeeches() {
        reloadAll();
    }

    private void reloadAll() {
        _speeches = new FastMap<>();
        File[] files = new File(Config.DATAPACK_ROOT, "data/speeches").listFiles(new TxtFilter());
        int count = 0;
        for (File file : files) {
            if (!file.isDirectory()) {
                int id = Integer.parseInt(file.getName().substring(0, file.getName().length()-4));
                Speech speech = new Speech(file.getPath());
                _speeches.put(id, speech);
                count++;
            }
        }
        _log.log(Level.INFO, "Loaded {0} speech files.", count);
    }

    private boolean probability() {
        return Rnd.get(1, 100) <= Config.VAEMOD_SPEAKINGBOSS; // % chance
    }

    public void roleplaying(L2GrandBossInstance wb) {
        int id = wb.getNpcId();
        if (_speeches.containsKey(id) && !_speeches.get(id).getIsEmpty()&& probability()) {
            wb.broadcastPacket(new CreatureSay(wb.getObjectId(), Say2.ALL, wb.getName(), _speeches.get(id).getValue(Rnd.get(_speeches.get(id).getSize()))));
        }
        }

    public void roleplaying(L2RaidBossInstance rb) {
        int id = rb.getNpcId();
        if (_speeches.containsKey(id) && !_speeches.get(id).getIsEmpty()&& probability()) {
            rb.broadcastPacket(new CreatureSay(rb.getObjectId(), Say2.ALL, rb.getName(), _speeches.get(id).getValue(Rnd.get(_speeches.get(id).getSize()))));
        }
    }

    private static class TxtFilter implements FileFilter {
        public boolean accept(File file) {
            if (!file.isDirectory()) {
                return (file.getName().endsWith(".txt"));
            }
            return true;
        }
    }
}