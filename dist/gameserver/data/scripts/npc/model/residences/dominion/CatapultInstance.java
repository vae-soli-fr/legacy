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
package npc.model.residences.dominion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.AggroList;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.instances.residences.SiegeToggleNpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestEventType;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Smo
 */
public class CatapultInstance extends SiegeToggleNpcInstance
{
	
	public CatapultInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onDeathImpl(Creature lastAttacker)
	{
		DominionSiegeEvent siegeEvent = getEvent(DominionSiegeEvent.class);
		if (siegeEvent == null)
		{
			return;
		}
		
		ThreadPoolManager.getInstance().execute(new GameObjectTasks.NotifyAITask(this, CtrlEvent.EVT_DEAD, lastAttacker, null));
		
		Player killer = lastAttacker.getPlayer();
		if (killer == null)
		{
			return;
		}
		
		Map<Playable, AggroList.HateInfo> aggroMap = getAggroList().getPlayableMap();
		
		Quest[] quests = getTemplate().getEventQuests(QuestEventType.MOB_KILLED_WITH_QUEST);
		if ((quests != null) && (quests.length > 0))
		{
			List<Player> players = null;
			if (isRaid() && Config.ALT_NO_LASTHIT)
			{
				players = new ArrayList<>();
				for (Playable pl : aggroMap.keySet())
				{
					if (!pl.isDead() && (isInRangeZ(pl, Config.ALT_PARTY_DISTRIBUTION_RANGE) || killer.isInRangeZ(pl, Config.ALT_PARTY_DISTRIBUTION_RANGE)))
					{
						players.add(pl.getPlayer());
					}
				}
			}
			else if (killer.getParty() != null)
			{
				players = new ArrayList<>(killer.getParty().getMemberCount());
				for (Player pl : killer.getParty().getPartyMembers())
				{
					if (!pl.isDead() && (isInRangeZ(pl, Config.ALT_PARTY_DISTRIBUTION_RANGE) || killer.isInRangeZ(pl, Config.ALT_PARTY_DISTRIBUTION_RANGE)))
					{
						players.add(pl);
					}
				}
			}
			
			for (Quest quest : quests)
			{
				Player toReward = killer;
				if ((quest.getParty() != Quest.PARTY_NONE) && (players != null))
				{
					if (isRaid() || (quest.getParty() == Quest.PARTY_ALL))
					{
						for (Player pl : players)
						{
							QuestState qs = pl.getQuestState(quest.getName());
							if ((qs != null) && !qs.isCompleted())
							{
								quest.notifyKill(this, qs);
							}
						}
						toReward = null;
					}
					else
					{
						List<Player> interested = new ArrayList<>(players.size());
						for (Player pl : players)
						{
							QuestState qs = pl.getQuestState(quest.getName());
							if ((qs != null) && !qs.isCompleted())
							{
								interested.add(pl);
							}
						}
						
						if (interested.isEmpty())
						{
							continue;
						}
						
						toReward = interested.get(Rnd.get(interested.size()));
						if (toReward == null)
						{
							toReward = killer;
						}
					}
				}
				
				if (toReward != null)
				{
					QuestState qs = toReward.getQuestState(quest.getName());
					if ((qs != null) && !qs.isCompleted())
					{
						quest.notifyKill(this, qs);
					}
				}
			}
		}
	}
}