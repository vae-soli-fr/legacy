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
package lineage2.gameserver.model.quest.dynamic;

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.time.cron.SchedulingPattern;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @date 27.10.12 23:54
 */
public class DynamicQuestController
{
	private static final Logger log = LoggerFactory.getLogger(DynamicQuestController.class);
	private static final DynamicQuestController ourInstance = new DynamicQuestController();
	final Map<Integer, DynamicQuest> dynamicQuestsMap;
	private final Map<String, DynamicQuest> dynamicQuestsMapByName;
	
	private DynamicQuestController()
	{
		dynamicQuestsMap = new HashMap<>();
		dynamicQuestsMapByName = new HashMap<>();
	}
	
	public static DynamicQuestController getInstance()
	{
		return ourInstance;
	}
	
	void registerDynamicQuest(DynamicQuest dynamicQuest)
	{
		dynamicQuestsMap.put(dynamicQuest.getQuestId(), dynamicQuest);
		dynamicQuestsMapByName.put(dynamicQuest.getClass().getSimpleName(), dynamicQuest);
	}
	
	/**
	 * @param questId
	 */
	public void startQuest(int questId)
	{
		startQuest(questId, 1);
	}
	
	/**
	 * @param questId
	 * @param step
	 */
	public void startQuest(int questId, int step)
	{
		DynamicQuest quest = dynamicQuestsMap.get(questId);
		quest.setCurrentStep(step);
		
		if (quest.isStartCondition())
		{
			quest.start(new QuestEnder(questId));
		}
		
		// System.out.println("Condition == " + quest.isStartCondition());
	}
	
	/**
	 * @param questId
	 * @param success
	 */
	public void endQuest(int questId, boolean success)
	{
		DynamicQuest quest = dynamicQuestsMap.get(questId);
		quest.stop(success, new QuestFinalizer(questId));
	}
	
	/**
	 * @param questId
	 */
	void finalizeQuest(int questId)
	{
		DynamicQuest quest = dynamicQuestsMap.get(questId);
		quest.finish();
	}
	
	/**
	 * @param questId
	 * @param pattern
	 */
	void initSchedulingPattern(int questId, SchedulingPattern pattern)
	{
		if (!dynamicQuestsMap.containsKey(questId))
		{
			log.warn("DynamicQuestController#initSchedulingPattern(int, SchedulingPattern): Not found quest with id: " + questId);
			return;
		}
		
		long nextLaunchTime = pattern.next(System.currentTimeMillis());
		ThreadPoolManager.getInstance().schedule(new QuestStarter(questId, pattern), nextLaunchTime - System.currentTimeMillis());
	}
	
	public void requestDynamicQuestHtml(int id, int step, Player player)
	{
		if (dynamicQuestsMap.containsKey(id))
		{
			DynamicQuest quest = dynamicQuestsMap.get(id);
			quest.requestHtml(step, player);
		}
	}
	
	public void requestQuestProgressInfo(int id, int step, Player player)
	{
		if (dynamicQuestsMap.containsKey(id))
		{
			DynamicQuest quest = dynamicQuestsMap.get(id);
			quest.requestProgressInfo(step, player);
		}
	}
	
	public void requestScoreBoard(int id, int step, Player player)
	{
		if (dynamicQuestsMap.containsKey(id))
		{
			DynamicQuest quest = dynamicQuestsMap.get(id);
			quest.requestScoreBoard(step, player);
		}
	}
	
	public void taskCompleted(int questId, int taskId)
	{
		if (dynamicQuestsMap.containsKey(questId))
		{
			DynamicQuest quest = dynamicQuestsMap.get(questId);
			quest.taskCompleted(taskId);
		}
	}
	
	public void processDialogEvent(String questName, String event, Player player)
	{
		if (dynamicQuestsMapByName.containsKey(questName))
		{
			DynamicQuest quest = dynamicQuestsMapByName.get(questName);
			quest.processDialogEvent(event, player);
		}
	}
	
	public void StartCondition(int questId)
	{
		if (dynamicQuestsMap.containsKey(questId))
		{
			DynamicQuest quest = dynamicQuestsMap.get(questId);
			quest.isStartCondition();
		}
	}
	
	private final class QuestStarter extends RunnableImpl
	{
		private final int questId;
		private final SchedulingPattern pattern;
		
		public QuestStarter(int questId, SchedulingPattern pattern)
		{
			this.questId = questId;
			this.pattern = pattern;
		}
		
		@Override
		public void runImpl()
		{
			DynamicQuest quest = dynamicQuestsMap.get(questId);
			startQuest(questId);
			long nextLaunchTime = pattern.next(System.currentTimeMillis() + (quest.getDuration() * 1000));
			ThreadPoolManager.getInstance().schedule(this, nextLaunchTime - System.currentTimeMillis());
		}
	}
	
	private final class QuestEnder extends RunnableImpl
	{
		private final int questId;
		
		public QuestEnder(int questId)
		{
			this.questId = questId;
		}
		
		@Override
		public void runImpl()
		{
			endQuest(questId, false);
		}
	}
	
	private final class QuestFinalizer extends RunnableImpl
	{
		private final int questId;
		
		public QuestFinalizer(int questId)
		{
			this.questId = questId;
		}
		
		@Override
		public void runImpl()
		{
			finalizeQuest(questId);
		}
	}
}