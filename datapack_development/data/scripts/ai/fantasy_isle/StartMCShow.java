package ai.fantasy_isle;

import com.l2jserver.gameserver.instancemanager.QuestManager;

public class StartMCShow implements Runnable {
	@Override
	public void run() {
		try {
			QuestManager.getInstance().getQuest("MC_Show").notifyEvent("Start", null, null);
		} catch (Exception e){}
	}
}
