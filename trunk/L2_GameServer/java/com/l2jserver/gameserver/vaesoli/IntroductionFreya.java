package com.l2jserver.gameserver.vaesoli;

import java.util.Map;

import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.model.L2CharPosition;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.network.serverpackets.PlaySound;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;
import com.l2jserver.gameserver.network.serverpackets.SpecialCamera;


/**
 * Introduction AI
 * @author Melua
 *
 */
public class IntroductionFreya extends Quest
{

	private int _previousx;
	private int _previousy;
	private int _previousz;
    private static final int DOCTER_CHAOS = 32033;
	private static final int STRANGE_MACHINE = 32032;
	private static final int CHAOS_GOLEM = 25512;
	private static boolean _IsGolemSpawned;

	public IntroductionFreya(int questId, String name, String descr)
	{
		super(questId, name, descr);

		addFirstTalkId(32033);
		_IsGolemSpawned = false;
	}

	public L2Npc findTemplate(int npcId)
	{
		L2Npc npcInstance = null;
		L2Spawn spawn;
		Map<Integer,L2Spawn> values = SpawnTable.getInstance().getSpawnTable();
		for(int i = 0;i<values.size();i++)
		{
			spawn = values.get(i);
			if (spawn != null && spawn.getNpcid() == npcId)
			{
				npcInstance = spawn.getLastSpawn();
				break;
			}
		}
		return npcInstance;
	}

	@Override
	public String onAdvEvent (String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("1"))
		{
			L2Npc machine_instance = findTemplate(STRANGE_MACHINE);
			if (machine_instance != null)
			{
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, machine_instance);
				machine_instance.broadcastPacket(new SpecialCamera(machine_instance.getObjectId(),1,-200,15,10000,20000,0,0,1,0));
			}
			else
				//print "Dr Chaos AI: problem finding Strange Machine (npcid = "+STRANGE_MACHINE+"). Error: not spawned!"
				startQuestTimer("2",2000,npc,player);
			startQuestTimer("3",10000,npc,player);
		}
		else if (event.equalsIgnoreCase("2"))
			npc.broadcastPacket(new SocialAction(npc.getObjectId(),3));
		else if (event.equalsIgnoreCase("3"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(),1,-150,10,3000,20000,0,0,1,0));
			startQuestTimer("4",2500,npc,player);
		}
		else if (event.equalsIgnoreCase("4"))
		{
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(96055,-110759,-3312,0));
			startQuestTimer("5",2000,npc,player);
		}
		else if (event.equalsIgnoreCase("5"))
		{
			player.teleToLocation(94832,-112624,-3304);
			npc.teleToLocation(-113091,-243942,-15536);
			if (!_IsGolemSpawned)
			{
				L2Npc golem = addSpawn(CHAOS_GOLEM,94640,-112496,-3336,0,false,0);
				_IsGolemSpawned = true;
				startQuestTimer("6",1000,golem,player);
				player.sendPacket(new PlaySound(1,"Rm03_A",0,0,0,0,0));
			}
		}
		else if (event.equalsIgnoreCase("6"))
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(),30,-200,20,6000,8000,0,0,1,0));
		return super.onAdvEvent(event, npc, player);
	}

	@Override
	public String onFirstTalk (L2Npc npc, L2PcInstance player)
	{
		if (npc.getNpcId() == DOCTER_CHAOS)
		{
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(96323,-110914,-3328,0));
			this.startQuestTimer("1",3000,npc,player);
		}
		return "";
	}

	public static void main(String[] args)
	{
		new IntroductionFreya(-1,"Doctor Chaos","ai");
	}
}