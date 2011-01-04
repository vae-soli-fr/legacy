package hellbound.Buron;

import com.l2jserver.gameserver.instancemanager.HellboundManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * @author theOne
 */
public class Buron extends Quest
{
	private static final int Buron = 32345;

	private static final int DarionBadge = 9674;

	public Buron(int id, String name, String descr)
	{
		super(id, name, descr);
		addStartNpc(Buron);
		addTalkId(Buron);
		addFirstTalkId(Buron);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());

		if (st == null)
			st = newQuestState(player);

		long BadgesCount = st.getQuestItemsCount(DarionBadge);
		String text = "<html><body>Buron:<br>It's a pity, but you do not have the necessary items.</body></html>";

		if (event.equalsIgnoreCase("Tunic"))
		{
			if (BadgesCount < 10)
				return text;
			st.takeItems(DarionBadge, 10);
			st.giveItems(9670, 1);
		}
		else if (event.equalsIgnoreCase("Helmet"))
		{
			if (BadgesCount < 10)
				return text;
			st.takeItems(DarionBadge, 10);
			st.giveItems(9669, 1);
		}
		else if (event.equalsIgnoreCase("Pants"))
		{
			if (BadgesCount < 10)
				return text;
			st.takeItems(DarionBadge, 10);
			st.giveItems(9671, 1);
		}
		else if (event.equalsIgnoreCase("rumor"))
		{
			int hellboundLevel = HellboundManager.getInstance().getLevel();
			return "hellboundLevel-" + hellboundLevel + ".htm";
		}

		return null;
	}

	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
			st = newQuestState(player);

		int hellboundLevel = HellboundManager.getInstance().getLevel();
		if (hellboundLevel < 2)
			return "32345-2.htm";
		else if (hellboundLevel >= 2 && hellboundLevel < 5)
			return "32345.htm";
		else if (hellboundLevel >= 5)
			return "32345-1.htm";

		return null;
	}

	public static void main(String[] args)
	{
		new Buron(-1, "Buron", "hellbound");
	}
}