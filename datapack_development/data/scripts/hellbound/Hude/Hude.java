package hellbound.Hude;

import com.l2jserver.gameserver.instancemanager.HellboundManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * @author theOne
 */
public class Hude extends Quest
{
	private static final int Hude = 32298;

	public Hude(int id, String name, String descr)
	{
		super(id, name, descr);
		addStartNpc(Hude);
		addTalkId(Hude);
		addFirstTalkId(Hude);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(getName());

		if (st == null)
			st = newQuestState(player);

		if (event.equalsIgnoreCase("scertif"))
		{
			if (st.getQuestItemsCount(10012) >= 60 && st.getQuestItemsCount(9676) >= 30)
			{
				st.takeItems(9676, 30);
				st.takeItems(10012, 60);
				st.takeItems(9850, 1);
				st.giveItems(9851, 1);
				htmltext = "32298-6.htm";
			}
			else
				htmltext = "32298-5.htm";
		}
		if (event.equalsIgnoreCase("pcertif"))
		{
			if (st.getQuestItemsCount(9681) >= 56 && st.getQuestItemsCount(9682) >= 14)
			{
				st.takeItems(9681, 56);
				st.takeItems(9682, 14);
				st.takeItems(9851, 1);
				st.giveItems(9852, 1);
				st.giveItems(9994, 1);
				htmltext = "32298-8.htm";
			}
			else
				htmltext = "32298-7.htm";
		}

		return htmltext;
	}

	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
			st = newQuestState(player);

		int hellboundLevel = HellboundManager.getInstance().getLevel();
		if (hellboundLevel >= 4 && st.getQuestItemsCount(9850) >= 1 && st.getQuestItemsCount(9851) < 1)
			return "32298.htm";
		else if (hellboundLevel < 7 && hellboundLevel > 3 && st.getQuestItemsCount(9851) >= 1)
			return "32298-1.htm";
		else if (hellboundLevel >= 7 && st.getQuestItemsCount(9851) >= 1)
			return "32298-2.htm";
		else if (hellboundLevel >= 7 && st.getQuestItemsCount(9852) >= 1)
			return "32298-3.htm";

		return "32298-4.htm";
	}

	public static void main(String[] args)
	{
		new Hude(-1, "Hude", "hellbound");
	}
}