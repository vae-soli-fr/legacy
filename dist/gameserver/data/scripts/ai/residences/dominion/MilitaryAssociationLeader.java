/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ai.residences.dominion;

import lineage2.gameserver.Config;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

import quests.Q00731_ProtectTheMilitaryAssociationLeader;
import ai.residences.SiegeGuardFighter;

/**
 * @author VISTALL
 * @date 4:32/23.06.2011
 */
public class MilitaryAssociationLeader extends SiegeGuardFighter
{
	private static final IntObjectMap<NpcString[]> MESSAGES = new HashIntObjectMap<>(9);
	
	static
	{
		MESSAGES.put(81, new NpcString[]
		{
			NpcString.PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_GLUDIO,
			NpcString.THE_MILITARY_ASSOCIATION_LEADER_OF_GLUDIO_IS_DEAD
		});
		MESSAGES.put(82, new NpcString[]
		{
			NpcString.PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_DION,
			NpcString.THE_MILITARY_ASSOCIATION_LEADER_OF_DION_IS_DEAD
		});
		MESSAGES.put(83, new NpcString[]
		{
			NpcString.PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_GIRAN,
			NpcString.THE_MILITARY_ASSOCIATION_LEADER_OF_GIRAN_IS_DEAD
		});
		MESSAGES.put(84, new NpcString[]
		{
			NpcString.PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_OREN,
			NpcString.THE_MILITARY_ASSOCIATION_LEADER_OF_OREN_IS_DEAD
		});
		MESSAGES.put(85, new NpcString[]
		{
			NpcString.PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_ADEN,
			NpcString.THE_MILITARY_ASSOCIATION_LEADER_OF_ADEN_IS_DEAD
		});
		MESSAGES.put(86, new NpcString[]
		{
			NpcString.PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_INNADRIL,
			NpcString.THE_MILITARY_ASSOCIATION_LEADER_OF_INNADRIL_IS_DEAD
		});
		MESSAGES.put(87, new NpcString[]
		{
			NpcString.PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_GODDARD,
			NpcString.THE_MILITARY_ASSOCIATION_LEADER_OF_GODDARD_IS_DEAD
		});
		MESSAGES.put(88, new NpcString[]
		{
			NpcString.PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_RUNE,
			NpcString.THE_MILITARY_ASSOCIATION_LEADER_OF_RUNE_IS_DEAD
		});
		MESSAGES.put(89, new NpcString[]
		{
			NpcString.PROTECT_THE_MILITARY_ASSOCIATION_LEADER_OF_SCHUTTGART,
			NpcString.THE_MILITARY_ASSOCIATION_LEADER_OF_SCHUTTGART_IS_DEAD
		});
	}
	
	private class OnPlayerEnterListenerImpl implements OnPlayerEnterListener
	{
		public OnPlayerEnterListenerImpl()
		{
		}
		
		@Override
		public void onPlayerEnter(Player player)
		{
			NpcInstance actor = getActor();
			DominionSiegeEvent siegeEvent = actor.getEvent(DominionSiegeEvent.class);
			if (siegeEvent == null)
			{
				return;
			}
			
			if (player.getEvent(DominionSiegeEvent.class) != siegeEvent)
			{
				return;
			}
			
			Quest q = QuestManager.getQuest(Q00731_ProtectTheMilitaryAssociationLeader.class);
			
			QuestState questState = q.newQuestStateAndNotSave(player, Quest.CREATED);
			questState.setCond(1, false);
			questState.setStateAndNotSave(Quest.STARTED);
		}
	}
	
	private final OnPlayerEnterListener _listener = new OnPlayerEnterListenerImpl();
	
	public MilitaryAssociationLeader(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	public void onEvtAttacked(Creature attacker, int dam)
	{
		super.onEvtAttacked(attacker, dam);
		
		NpcInstance actor = getActor();
		
		DominionSiegeEvent siegeEvent = actor.getEvent(DominionSiegeEvent.class);
		if (siegeEvent == null)
		{
			return;
		}
		
		boolean first = actor.getParameter("dominion_first_attack", true);
		if (first)
		{
			actor.setParameter("dominion_first_attack", false);
			NpcString msg = MESSAGES.get(siegeEvent.getId())[0];
			Quest q = QuestManager.getQuest(Q00731_ProtectTheMilitaryAssociationLeader.class);
			for (Player player : GameObjectsStorage.getAllPlayersForIterate())
			{
				if (player.getEvent(DominionSiegeEvent.class) == siegeEvent)
				{
					player.sendPacket(new ExShowScreenMessage(msg, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
					
					QuestState questState = q.newQuestStateAndNotSave(player, Quest.CREATED);
					questState.setCond(1, false);
					questState.setStateAndNotSave(Quest.STARTED);
				}
			}
			CharListenerList.addGlobal(_listener);
		}
	}
	
	@Override
	public void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		
		NpcInstance actor = getActor();
		
		DominionSiegeEvent siegeEvent = actor.getEvent(DominionSiegeEvent.class);
		if (siegeEvent == null)
		{
			return;
		}
		
		NpcString msg = MESSAGES.get(siegeEvent.getId())[1];
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if (player.getEvent(DominionSiegeEvent.class) == siegeEvent)
			{
				player.sendPacket(new ExShowScreenMessage(msg, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
				
				QuestState questState = player.getQuestState(Q00731_ProtectTheMilitaryAssociationLeader.class);
				if (questState != null)
				{
					questState.abortQuest();
				}
			}
		}
		
		Player player = killer.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (player.getParty() == null)
		{
			DominionSiegeEvent siegeEvent2 = player.getEvent(DominionSiegeEvent.class);
			if ((siegeEvent2 == null) || (siegeEvent2 == siegeEvent))
			{
				return;
			}
			siegeEvent2.addReward(player, DominionSiegeEvent.STATIC_BADGES, 5);
		}
		else
		{
			for (Player $member : player.getParty())
			{
				if ($member.isInRange(player, Config.ALT_PARTY_DISTRIBUTION_RANGE))
				{
					DominionSiegeEvent siegeEvent2 = $member.getEvent(DominionSiegeEvent.class);
					if ((siegeEvent2 == null) || (siegeEvent2 == siegeEvent))
					{
						continue;
					}
					siegeEvent2.addReward($member, DominionSiegeEvent.STATIC_BADGES, 5);
				}
			}
		}
	}
	
	@Override
	public void onEvtDeSpawn()
	{
		super.onEvtDeSpawn();
		CharListenerList.removeGlobal(_listener);
	}
}
