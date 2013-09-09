package lineage2.gameserver.vaesoli;

import java.util.ArrayList;

import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.CustomSpawnTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * This class completely manages camps, from spawn to erase.
 * 
 * @author Saelil, updated by Melua
 */

public class Camp {
	public static enum State {
		EMPTY, TIMBER, FIRE, FOOD, TENT, PACKED, EATEN, EXTINCT
	}

	private State _state;
	private ArrayList<SimpleSpawner> _spawns;
	private ArrayList<NpcInstance> _npcs;

	public Camp() {
		_state = State.EMPTY;
		_spawns = new ArrayList<>();
		_npcs = new ArrayList<>();
	}

	public State getState() {
		return _state;
	}

	/**
	 * Based on the state of active char's camp, figures out which element to
	 * spawn, remove, etc. Called through .camp.
	 */
	public void evolve(Player activeChar) {
		switch (_state) {
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
	private void addSpawn(Player activeChar, int id) {
		NpcTemplate template;
		int x, y, z, head;
		template = NpcHolder.getInstance().getTemplate(id);

		SimpleSpawner spawn = new SimpleSpawner(template);

		if (_spawns.isEmpty()) {
			x = activeChar.getX();
			y = activeChar.getY();
			z = activeChar.getZ();
			head = activeChar.getHeading();
		} else if (_spawns.size() > 2 && (activeChar.getDistance(_npcs.get(0)) <= 500)) {
			x = activeChar.getX();
			y = activeChar.getY();
			z = activeChar.getZ();
			head = activeChar.getHeading();
		} else {
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

		if (activeChar.getReflection() != ReflectionManager.DEFAULT)
			spawn.setReflection(activeChar.getReflection());
		else
			spawn.setReflection(ReflectionManager.DEFAULT);

		CustomSpawnTable.getInstance().addNewSpawn(spawn);
		spawn.init();
		spawn.stopRespawn();
		_spawns.add(spawn);
		_npcs.add(spawn.getLastSpawn());
	}

	/**
	 * Remove camp elements.
	 */
	private void removeNpc(int index) {
		_npcs.get(index).deleteMe();
		CustomSpawnTable.getInstance().deleteSpawn(_npcs.get(index));
	}

	/**
	 * Delete all elements.
	 */
	public void clear() {
		for (NpcInstance n : _npcs) {
			n.deleteMe();
			CustomSpawnTable.getInstance().deleteSpawn(n);
		}
		_npcs.clear();
		_spawns.clear();
	}
}
