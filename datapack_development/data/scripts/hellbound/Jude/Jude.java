package hellbound.Jude;

import com.l2jserver.gameserver.instancemanager.HellboundManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * @author theOne
 */
public class Jude extends Quest
{
	private static final int Jude = 32356;

	public Jude(int id, String name, String descr)
	{
		super(id, name, descr);
		addStartNpc(Jude);
		addTalkId(Jude);
		addFirstTalkId(Jude);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(getName());

		if (st == null)
			st = newQuestState(player);

		if (event.equalsIgnoreCase("TreasureSacks"))
		{
			if (st.getQuestItemsCount(9684) >= 40)
			{
				st.takeItems(9684, 40);
				htmltext = "32356-4.htm";
			}
			else
				htmltext = "32356-5.htm";
		}

		return htmltext;
	}

	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(getName());
		if (st == null)
			st = newQuestState(player);

		int hellboundLevel = HellboundManager.getInstance().getLevel();
		if (hellboundLevel < 3)
			htmltext = "32356-1.htm";
		else if (hellboundLevel == 3)
			htmltext = "32356.htm";
		else if (hellboundLevel == 4)
			htmltext = "32356-2.htm";
		else if (hellboundLevel == 5)
			htmltext = "32356-3.htm";
		else if (hellboundLevel >= 6)
			npc.showChatWindow(player);

		return htmltext;
	}

	public static void main(String[] args)
	{
		new Jude(-1, "Jude", "hellbound");
	}
}