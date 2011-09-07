/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ai.group_template;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.instancemanager.ZoneManager;
import com.l2jserver.gameserver.model.L2CharPosition;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.NpcSay;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;
import com.l2jserver.gameserver.util.Util;
import com.l2jserver.util.Rnd;

import gnu.trove.TIntHashSet;
import java.util.List;
import javolution.util.FastList;

/**
 * Sel Mahum Training Ground AI. Now controls only "tribune-based" Mahums
 * @author GKR
 */

public class SelMahums extends L2AttackableAIScript
{

	//Sel Mahum Drill Sergeant, Sel Mahum Training Officer, Sel Mahum Drill Sergeant respectively
	private static final int[] MAHUM_CHIEFS = { 22775, 22776, 22778 };

	//Sel Mahum Recruit, Sel Mahum Recruit, Sel Mahum Soldier, Sel Mahum Recruit, Sel Mahum Soldier respectively 
	private static final int[] MAHUM_SOLDIERS = { 22780, 22782, 22783, 22784, 22785 };
	
	private static final int[] CHIEF_SOCIAL_ACTIONS = { 1, 4, 5, 7 };
	private static final int[] SOLDIER_SOCIAL_ACTIONS = { 1, 5, 6, 7 };
	
	/**
	 * 1801112 - Who is mucking with my recruits!?!
	 * 1801113 - You are entering a world of hurt!
	 */	 	 	
	//I get crash of client, if use "int" constructor, so I use "String" constructor here
	//private static final int[] CHIEF_FSTRINGS = { 1801112, 1801113 };
	private static final String[] CHIEF_FSTRINGS = { "Who is mucking with my recruits!?!", "You are entering a world of hurt!" };

	/**
	 * 1801114 - They done killed da Sarge... Run!!
	 * 1801115 - Don't Panic... Okay, Panic!
	 */	 	 	
	//I get crash of client, if use "int" constructor, so I use "String" constructor here
	//private static final int[] SOLDIER_FSTRINGS = { 1801114, 1801115 };
	private static final String[] SOLDIER_FSTRINGS = { "They done killed da Sarge... Run!!", "Don't Panic... Okay, Panic!" };
	
	private static List<L2Spawn> _spawns = new FastList<L2Spawn>(); //all Mahum's spawns are stored here
	private static TIntHashSet _scheduledReturnTasks = new TIntHashSet(); //Used to track scheduled Return Tasks

	public SelMahums (int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		for (int i : MAHUM_CHIEFS)
			{
				addAttackId(i);
				addKillId(i);
				addSpawnId(i);
			}

		for (int i : MAHUM_SOLDIERS)
			addSpawnId(i);
	
		//Send event to monsters, that was spawned through SpawnTable at server start (it is impossible to track first spawn)
    for (L2Spawn npcSpawn : SpawnTable.getInstance().getSpawnTable().values())
    {
      if (Util.contains(MAHUM_CHIEFS, npcSpawn.getNpcid()) || Util.contains(MAHUM_SOLDIERS, npcSpawn.getNpcid()))
          onSpawn(npcSpawn.getLastSpawn());
    }
	}

	@Override
	public String onAdvEvent (String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("do_social_action"))
		{
			if (npc != null && !npc.isDead()) 
			{
				if (!npc.isBusy() && npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_ACTIVE && 
						npc.getX() == npc.getSpawn().getLocx() && npc.getY() == npc.getSpawn().getLocy()) 
				{
					int idx = Rnd.get(6);
					if (idx <= CHIEF_SOCIAL_ACTIONS.length - 1)
					{
						npc.broadcastPacket(new SocialAction(npc.getObjectId(), CHIEF_SOCIAL_ACTIONS[idx]));

						L2ZoneType zone = getZone(npc);
					
						if (zone != null )
						for (L2Character ch : zone.getCharactersInside().values())
						{
							if (ch != null && !ch.isDead() && ch instanceof L2MonsterInstance && !((L2MonsterInstance) ch).isBusy() && 
									Util.contains(MAHUM_SOLDIERS, ((L2MonsterInstance) ch).getNpcId()) && ch.getAI().getIntention() == CtrlIntention.AI_INTENTION_ACTIVE && 
									ch.getX() == ((L2MonsterInstance) ch).getSpawn().getLocx() && ch.getY() == ((L2MonsterInstance) ch).getSpawn().getLocy())
								ch.broadcastPacket(new SocialAction(ch.getObjectId(), SOLDIER_SOCIAL_ACTIONS[idx]));
						}
					}
				}	

				startQuestTimer("do_social_action", 15000, npc, null);
			}
		}
		
		else if (event.equalsIgnoreCase("reset_busy_state"))
		{
			if (npc != null)
			{
				npc.setBusy(false);
				npc.disableCoreAI(false);
			}
   	}
   	
		return null;
	}

	@Override
	public String onAttack (L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (!npc.isDead() && !npc.isBusy())
		{
			if (Rnd.get(10) < 1)
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.ALL, npc.getNpcId(), CHIEF_FSTRINGS[Rnd.get(2)]));

			npc.setBusy(true);
			startQuestTimer("reset_busy_state", 60000, npc, null);
		}

		return super.onAttack(npc,attacker,damage,isPet);
	}

	@Override
	public String onKill (L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		L2ZoneType leaderZone = getZone(npc);
		
		if (leaderZone != null)	
		{
			for (L2Spawn sp : _spawns)
			{
				L2MonsterInstance soldier = (L2MonsterInstance) sp.getLastSpawn();
				if (soldier != null && !soldier.isDead())
				{
					L2ZoneType soldierZone = getZone(soldier);
					if (soldierZone != null && leaderZone.getId() == soldierZone.getId())
					{
						if (Rnd.get(4) < 1)
							soldier.broadcastPacket(new NpcSay(soldier.getObjectId(), Say2.ALL, soldier.getNpcId(), SOLDIER_FSTRINGS[Rnd.get(2)]));
				
						soldier.setBusy(true);
						soldier.setIsRunning(true);
						soldier.clearAggroList();
						soldier.disableCoreAI(true);
						soldier.getAI().setIntention( CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition((soldier.getX() + Rnd.get(-800, 800)), (soldier.getY()+ Rnd.get(-800, 800)), soldier.getZ(), soldier.getHeading()));
						startQuestTimer("reset_busy_state", 5000, soldier, null);
					}
				}
			}
			//Soldiers should return into spawn location, if they have "NO_DESIRE" state. It looks like AI_INTENTION_ACTIVE in L2J terms,
			//but we have no possibility to track AI intention change, so timer is used here. Time can be ajusted, if needed.
			if (!_scheduledReturnTasks.contains(leaderZone.getId())) //Check for shceduled task presence for this zone
			{
				_scheduledReturnTasks.add(leaderZone.getId()); //register scheduled task for zone
				ThreadPoolManager.getInstance().scheduleGeneral(new ReturnTask(leaderZone.getId()), 120000); //schedule task
			}
		}

		return super.onKill(npc,killer,isPet);
	}

	@Override
	public String onSpawn(L2Npc npc)
	{
		if (!npc.isTeleporting())
		{
			if (Util.contains(MAHUM_CHIEFS, npc.getNpcId()))
				startQuestTimer("do_social_action", 15000, npc, null);
			
			npc.disableCoreAI(false);
			npc.setBusy(false);
			npc.setIsNoRndWalk(true);
			npc.setRandomAnimationEnabled(false);
			_spawns.add(npc.getSpawn());
		}

		return super.onSpawn(npc);
	}
	
	private L2ZoneType getZone(L2Npc npc)
	{
		L2ZoneType zone = null;
					
		try
		{
			L2Spawn spawn = npc.getSpawn();
			zone = ZoneManager.getInstance().getZones(spawn.getLocx(), spawn.getLocy(), spawn.getLocz()).get(0);
		}

		catch(NullPointerException e)
		{
		}

		catch(IndexOutOfBoundsException e)
		{
		}
		
		return zone;
	}
	
	/**
	 * Returns monsters in their spawn location
	 */	 	
	private class ReturnTask implements Runnable
	{
		private final int _zoneId;
		private boolean _runAgain;

		public ReturnTask(int zoneId)
		{
			_zoneId = zoneId;
			_runAgain = false;
		}

		@Override
		public void run()
		{
		 	for (L2Spawn sp: _spawns)
		 	{
				L2MonsterInstance monster = (L2MonsterInstance) sp.getLastSpawn();
				
				if (monster != null && !monster.isDead())
				{
					L2ZoneType zone = getZone(monster);
					if (zone != null && zone.getId() == _zoneId)
					{
						if (monster.getX() != sp.getLocx() && monster.getY() != sp.getLocy()) //Check if there is monster not in spawn location
						{
							//Teleport him if not engaged in battle / not flee
							if (monster.getAI().getIntention() == CtrlIntention.AI_INTENTION_ACTIVE || monster.getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE) 
							{
								monster.setHeading(sp.getHeading());
								monster.teleToLocation(sp.getLocx(), sp.getLocy(), sp.getLocz());
							}
							else //There is monster('s) not in spawn location, but engaged in battle / flee. Set flag to repeat Return Task for this zone 
								_runAgain = true;
						}
					}
				}   
			}
			if (_runAgain) //repeat task
				ThreadPoolManager.getInstance().scheduleGeneral(new ReturnTask(_zoneId), 120000);
			else // Task is not sheduled ahain for this zone, unregister it
				_scheduledReturnTasks.remove(_zoneId);
		}
	}
	
	
	public static void main(String[] args)
	{
		// now call the constructor (starts up the ai)
		new SelMahums(-1,"sel_mahums","ai");
	}
}
