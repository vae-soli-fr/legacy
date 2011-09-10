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
        this._state = this._state.EMPTY;
        this._spawns = new ArrayList<L2Spawn>();
        this._npcs = new ArrayList<L2Npc>();
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
                this.addSpawn(activeChar, 80927);
                activeChar.sendMessage("Vous disposez du bois sec au sol.");
                this._state = this._state.TIMBER;
                break;
            case TIMBER:
                this.addSpawn(activeChar, 80930);
                activeChar.sendMessage("Vous allumez un feu.");
                this._state = this._state.FIRE;
                break;
            case FIRE:
                this.addSpawn(activeChar, 80933);
                activeChar.sendMessage("Vous faites cuire le repas.");
                this._state = this._state.FOOD;
                break;
            case FOOD:
                this.addSpawn(activeChar, 80590);
                activeChar.sendMessage("Vous montez une tente pour vous abriter.");
                this._state = this._state.TENT;
                break;    
            case TENT:
                this.removeNpc(3);
                activeChar.sendMessage("Vous démontez la tente.");
                this._state = this._state.PACKED;
                break;
            case PACKED:
                this.removeNpc(2);
                activeChar.sendMessage("Vous rangez les ustensiles de cuisine.");
                this._state = this._state.EATEN;
                break;
            case EATEN:
                this.removeNpc(1);
                activeChar.sendMessage("Vous étouffez le feu.");
                this._state = this._state.EXTINCT;
                break;    
            case EXTINCT:
                this.removeNpc(0);
                this._npcs.clear();
                this._spawns.clear();
                activeChar.sendMessage("Vous dispersez les morceaux de bois calcinés.");
                this._state = this._state.EMPTY;
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
            
            if(this._spawns.isEmpty())
            {
                x = activeChar.getX();
                y = activeChar.getY();
                z = activeChar.getZ();
                head = activeChar.getHeading();
            }
            else if(this._spawns.size() > 2 && activeChar.isInsideRadius(this._npcs.get(0), 500, true, false))
            {
                x = activeChar.getX();
                y = activeChar.getY();
                z = activeChar.getZ();
                head = activeChar.getHeading();
            }
            else
            {
                x = this._spawns.get(0).getLocx();
                y = this._spawns.get(0).getLocy();
                z = this._spawns.get(0).getLocz();
                head = this._spawns.get(0).getHeading();
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
            this._spawns.add(spawn);
            this._npcs.add(spawn.getLastSpawn());
        }
        catch (Exception e)
        {
            activeChar.sendMessage("Erreur à la création.");
        }
    }
    
   /**
    * Remove camp elements.
    */
    private void removeNpc(int index)
    {
        this._npcs.get(index).deleteMe();
        SpawnTable.getInstance().deleteSpawn(this._spawns.get(index), false);
    }
    
    /**
     * Delete all elements.
     */
    public void clear()
    {
        for(L2Npc n : this._npcs)
            n.deleteMe();
        for(L2Spawn s : this._spawns)
            SpawnTable.getInstance().deleteSpawn(s, false);
        this._npcs.clear();
        this._spawns.clear();
    }
}
