package com.l2jserver.gameserver.vaesoli;

import java.util.ArrayList;
import com.l2jserver.gameserver.datatables.NpcTable;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.templates.chars.L2NpcTemplate;

/**
 * This class completely manages camps,
 * from spawn to erase.
 * @author Saelil
 */

public class Camp {
    public static enum State {
        EMPTY, TIMBER, FIRE, FOOD, TENT,
        PACKED, EATEN, EXTINCT
    }
    private State _state;
    private ArrayList<L2Spawn> _spawns;
    private ArrayList<L2Npc> _npcs;
    
    public Camp()
    {
        _state = State.EMPTY;
        _spawns = new ArrayList<>();
        _npcs = new ArrayList<>();
    }
    
    public State getState()
    {
        return _state;
    }
    
    /**
     * Based on the state of active char's camp,
     * figures out which element to spawn, remove,
     * etc.
     * Called through .camp.
     */
    public void evolve(L2PcInstance activeChar)
    {
        switch(_state)
        {
            case EMPTY:
                addSpawn(activeChar, 80927);
                activeChar.sendMessage("Vous disposez du bois sec au sol.");
                _state = State.TIMBER;
                break;
            case TIMBER:
                addSpawn(activeChar, 80930);
                activeChar.sendMessage("Vous allumez un feu.");
                _state = State.FIRE;
                break;
            case FIRE:
                addSpawn(activeChar, 80933);
                activeChar.sendMessage("Vous faites cuire le repas.");
                _state = State.FOOD;
                break;
            case FOOD:
                addSpawn(activeChar, 80590);
                activeChar.sendMessage("Vous montez une tente pour vous abriter.");
                _state = State.TENT;
                break;    
            case TENT:
                removeNpc(3);
                activeChar.sendMessage("Vous démontez la tente.");
                _state = State.PACKED;
                break;
            case PACKED:
                removeNpc(2);
                activeChar.sendMessage("Vous rangez les ustensiles de cuisine.");
                _state = State.EATEN;
                break;
            case EATEN:
                removeNpc(1);
                activeChar.sendMessage("Vous étouffez le feu.");
                _state = State.EXTINCT;
                break;    
            case EXTINCT:
                removeNpc(0);
                _npcs.clear();
                _spawns.clear();
                activeChar.sendMessage("Vous dispersez les morceaux de bois calcinés.");
                _state = State.EMPTY;
                break;   
        }
    }

    /**
     * Spawn camp elements.
     */
   private void addSpawn(L2PcInstance activeChar, int id)
    {
        L2NpcTemplate template;
        int x, y, z, head;
        template = NpcTable.getInstance().getTemplate(id);

        try
        {
            L2Spawn spawn = new L2Spawn(template);
            
            if(_spawns.isEmpty())
            {
                x = activeChar.getX();
                y = activeChar.getY();
                z = activeChar.getZ();
                head = activeChar.getHeading();
            }
            else if(_spawns.size() > 2 && activeChar.isInsideRadius(_npcs.get(0), 500, true, false))
            {
                x = activeChar.getX();
                y = activeChar.getY();
                z = activeChar.getZ();
                head = activeChar.getHeading();
            }
            else
            {
                x = _spawns.get(0).getLocx();
                y = _spawns.get(0).getLocy();
                z = _spawns.get(0).getLocz();
                head = _spawns.get(0).getHeading();
            }
            
            spawn.setLocx(x);
            spawn.setLocy(y);
            spawn.setLocz(z);
            spawn.setHeading(head);
            spawn.setAmount(1);
            spawn.setRespawnDelay(60);

            if (activeChar.getInstanceId() > 0)
                spawn.setInstanceId(activeChar.getInstanceId());
            else
                spawn.setInstanceId(0);

            SpawnTable.getInstance().addNewSpawn(spawn, false);
            spawn.init();
            spawn.stopRespawn();
            _spawns.add(spawn);
            _npcs.add(spawn.getLastSpawn());
        }
        catch (SecurityException | ClassNotFoundException | NoSuchMethodException e)
        {
            activeChar.sendMessage("Erreur à la création.");
        }
    }
    
   /**
    * Remove camp elements.
    */
    private void removeNpc(int index)
    {
        _npcs.get(index).deleteMe();
        SpawnTable.getInstance().deleteSpawn(_spawns.get(index), false);
    }
    
    /**
     * Delete all elements.
     */
    public void clear()
    {
        for(L2Npc n : _npcs)
            n.deleteMe();
        for(L2Spawn s : _spawns)
            SpawnTable.getInstance().deleteSpawn(s, false);
        _npcs.clear();
        _spawns.clear();
    }
}
