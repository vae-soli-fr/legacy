package hellbound.Solomon;

import com.l2jserver.gameserver.instancemanager.HellboundManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;

/**
 * @author theOne
 */
public class Solomon extends Quest
{
	private static final int Solomon = 32355;

	public Solomon(int id, String name, String descr)
	{
		super(id, name, descr);
		addStartNpc(Solomon);
		addTalkId(Solomon);
		addFirstTalkId(Solomon);
	}

	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";

		int hellboundLevel = HellboundManager.getInstance().getLevel();
		if (hellboundLevel == 5)
			htmltext = "32355.htm";
		else if (hellboundLevel == 6 || hellboundLevel == 7)
			htmltext = "32355-1.htm";
		else if (hellboundLevel >= 9)
			htmltext = "32355-2.htm";

		return htmltext;
	}

	public static void main(String[] args)
	{
		new Solomon(-1, "Solomon", "hellbound");
	}
}