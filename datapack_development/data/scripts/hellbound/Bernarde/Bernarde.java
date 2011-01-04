package hellbound.Bernarde;

import com.l2jserver.gameserver.instancemanager.HellboundManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * @author theOne
 */
public class Bernarde extends Quest
{
	private static final int Bernarde = 32300;

	private int condition = 0;

	public Bernarde(int id, String name, String descr)
	{
		super(id, name, descr);
		addStartNpc(Bernarde);
		addTalkId(Bernarde);
		addFirstTalkId(Bernarde);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
			st = newQuestState(player);

		if (event.equalsIgnoreCase("HolyWater"))
		{
			long BadgesCount = st.getQuestItemsCount(9674);
			if (BadgesCount < 5)
				return "<html><body>Bernarde:<br>I am afraid, that you have the enough Darion's Badges.</body></html>";
			st.takeItems(9674, 5);
			st.giveItems(9673, 1);
			return "<html><body>Bernarde:<br>Here. Now, please, help our ancestors to sleep and rests in world!</body></html>";
		}
		else if (event.equalsIgnoreCase("Treasure"))
		{
			if (condition == 1)
				return "<html><body>Bernarde:<br>Seems you already gave treasure to me?</body></html>";
			long treasures = st.getQuestItemsCount(9684);
			if (treasures < 1)
				return "<html><body>Bernarde:<br>O, and where is treasure?</body></html>";
			st.takeItems(9684, 1);
			condition = 1;
			return "<html><body>Bernarde:<br>Thank you! This treasure very help for natives.</body></html>";
		}
		else if (event.equalsIgnoreCase("rumors"))
		{
			int hLevel = HellboundManager.getInstance().getLevel();
			if (hLevel == 6)
				return "<html><body>Bernarde:<br>Demons have been very active in the Magical Field lately. There must be something going on for them to only increase security in that area. Would you investigate? Perhaps you will find something that will help us reduce Beleth's power.</body></html>";
			if (hLevel == 7)
				return "<html><body>Bernarde:<br>I knew, why the Battered Lands was left, why even caravans abandoning hope are afraid to go there. Possibly, when you will be ready, you will want to find out is it independent? Speak, it is possible to use Magic Bottle, to collect Magic Souls of monsters which in same queue are very valued by oversea magicians. Why not to make you to attempt?</body></html>";
			if (hLevel == 8)
				return "<html><body>Bernarde:<br>I heard that Resistance and main habitants of continent have forced out Devils back to the external gate of Steel Citadel! A breach through gate is now only the question of time. True, one of erected hostile captains strengthening before a gate, but associations of our forces it must be it is enough, to manage with him even!</body></html>";
		}
		else if (event.equalsIgnoreCase("alreadysaid"))
			return "<html><body>Bernarde:<br>I said enough; now I must go back to work. Please, help to bring rest to those my ancestors which ferment among the ruins of Ancient Temple.</body></html>";
		else if (event.equalsIgnoreCase("abouthelp"))
			return "<html><body>Bernarde:<br>Derek is obvious, first priest, from time to time appears among the Ruins of Ancient Temple. How is tragic, that his spirit still roams there, incapable to rest! While he ferments, I, am afraid, there will never be a world between a temple and local village...</body></html>";
		else if (event.equalsIgnoreCase("quarry"))
			return "<html><body>Bernarde:<br>They speak that the slaves of caravan are forced to work on a career. If you will be able to rescue them, a caravan is rather in all to consent to enter into with local. Please, help us to free the Quarry Slaves!</body></html>";

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
			return "<html><body>Bernarde:<br>Who are you? Seems from a continent. You already took away all at us, what only it is possible! Go away from here and leave us at peace!</body></html>";
		else if (hellboundLevel == 2)
		{
			if (!player.isTransformed())
				return "<html><body>Bernarde:<br>Who are you? Seems from a continent. You already took away all at us, what only it is possible! Go away from here and leave us at peace!</body></html>";
			if (player.getTransformation().getId() != 101)
				return "<html><body>Bernarde:<br>Who are you? Seems from a continent. You already took away all at us, what only it is possible! Go away from here and leave us at peace!</body></html>";
			return "32300.htm";
		}
		else if (hellboundLevel == 3)
		{
			if (!player.isTransformed())
				return "<html><body>Bernarde:<br>He, seems, from our island. He looks very strong.</body></html>";
			if (player.getTransformation().getId() != 101)
				return "<html><body>Bernarde:<br>He, seems, from our island. He looks very strong.</body></html>";
			return "32300-2.htm";
		}
		else if (hellboundLevel == 4)
		{
			if (!player.isTransformed())
				return "32300-5.htm";
			if (player.getTransformation().getId() != 101)
				return "32300-5.htm";
			return "32300-4.htm";
		}
		else if (hellboundLevel == 5)
		{
			if (!player.isTransformed())
				return "32300-6.htm";
			if (player.getTransformation().getId() != 101)
				return "32300-6.htm";
			return "<html><body>Bernarde:<br>Ah, glad to meeting, my friend! Thank you to you, that helped the Derek's soul to attain Nirvana. Now habitants of the village under his guardianship.</body></html>";
		}
		else if (hellboundLevel >= 6 && hellboundLevel < 9)
		{
			if (!player.isTransformed())
				return "32300-3.htm";
			if (player.getTransformation().getId() != 101)
				return "32300-3.htm";
			return "32300-7.htm";
		}
		else if (hellboundLevel == 9)
			return "<html><body>Bernarde:<br>There are a lot of Darion's followers in Steel Citadel, and easier to us from it does not become. We must find a method to kill all of them!</body></html>";
		else if (hellboundLevel == 10)
			return "<html><body>Bernarde:<br>I heard that the habitants of continent and Resistance have moved up deeply in the heart of Steel Citadel. o, this  frigging earth can, finally, see sunset of a new era! At me on eyes the tears from one are piled up thoughts about it! But while it is early to celebrate - we must go and help our brothers in a castle.</body></html>";
		else if (hellboundLevel == 11)
			return "<html><body>Bernarde:<br>Now we must to defeat Beleth and Darion. And, in addition, to bring a peace on Hellbound.</body></html>";

		return null;
	}

	public static void main(String[] args)
	{
		new Bernarde(-1, "Bernarde", "hellbound");
	}
}