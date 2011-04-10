/* This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

/*
# Update by U3Games 17-03-2011 || Update by goncafa 31-03-2011 
# Special thanks to contributors users l2jserver
# Imported: L2jTW, thx!
 */

package ai.individual;

import ai.group_template.L2AttackableAIScript;

import com.l2jserver.gameserver.datatables.DoorTable;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.instancemanager.HellboundManager;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.util.Rnd;

/**
 * @author theOne
 */

public class Leodas extends L2AttackableAIScript
{
	private static final int leodas  = 22448;
	private static final int traitor = 32364;

	private static final int[] doors = {
			19250003, 19250004
	};

	private boolean leodasOnAttack = false;

	public Leodas(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(traitor);
		addTalkId(traitor);
		addFirstTalkId(traitor);
		addAttackId(leodas);
		addKillId(leodas);
	}

	private void dropItem(L2PcInstance player, L2Npc npc, int itemId, int count)
	{
		L2ItemInstance item = ItemTable.getInstance().createItem("Betrayal", itemId, count, player);
		item.dropMe(player, npc.getX(), npc.getY(), npc.getZ());
	}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet, L2Skill skill)
	{
		for (int i : doors)
			DoorTable.getInstance().getDoor(i).closeMe();

		return super.onAttack(npc, player, damage, isPet);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		for (int i : doors)
		{
			DoorTable.getInstance().getDoor(i).openMe();
			DoorTable.getInstance().getDoor(i).onOpen();
		}

		if (HellboundManager.getInstance().getLevel() >= 5)
		{
			if (Rnd.get(100) < 10)
				dropItem(player, npc, 9676, 30);

			dropItem(player, npc, 9676, 15);
			npc.onDecay();
		}

		HellboundManager.getInstance().increaseTrust(-1000); //value needs to be updated
		leodasOnAttack = false;

		return super.onKill(npc, player, isPet);
	}

	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = "";
		if (leodasOnAttack)
		{
			htmltext = "<html><body>Hellbound Traitor:<br>Leodas already attacked!</body></html>";
		}
		else if (player.getInventory().getItemByItemId(9676).getCount() == 0)
		{
			htmltext = "<html><body>原住民變節份子：<br>我需要的是<font color=\"LEVEL\">背叛者的憑證</font>10個，而且你沒有攜帶任何一個。<br>我能猜出你要騙我的理由，但無論如何先帶來再說吧。</body></html>";
		}
		else
		{
			htmltext = "<html><body>原住民變節份子：<br>";
			htmltext += "準備好要挑戰雷歐達斯嗎？<br><center><font color=\"FF0000\">（這是測試用的！）</font><br>";
			htmltext += "<a action=\"bypass -h Quest Leodas meetLeodas\">說準備好</a></center></body></html>";
		}

		int hellboundLevel = HellboundManager.getInstance().getLevel();
		if (hellboundLevel < 5)
			return null;

		return htmltext;
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		if (event.equalsIgnoreCase("meetLeodas"))
		{
			long marksCount = player.getInventory().getItemByItemId(9676).getCount();

			if (marksCount == 0)
			{
				htmltext = "<html><body>Hellbound Traitor:<br>I need <font color=\"LEVEL\">10 Mark of Betrayal</font>. But you did not bring one! Do not attempt to deceive me - bring to me that I was requested!</body></html>";
			}
			else if (marksCount >= 1 && marksCount < 10)
			{
				htmltext = "<html><body>Hellbound Traitor:<br>Yeah! You have a <font color=\"LEVEL\">Mark of Betrayal</font>. Unfortunately, I can in no way help you, if you do not bring to me 10 Marks. Bring all Marks to me, and I will immediately open door.</body></html>";
			}
			else if (marksCount >= 10)
			{
				player.destroyItemByItemId("item", 9676, 10, player, true);
				npc.broadcastPacket(new CreatureSay(npc.getObjectId(), 0, npc.getName(), "Brothers! This stranger wants to kill our Commander!!!"));
				startQuestTimer("Leodas", 3000, npc, null, false);
				leodasOnAttack = true;
				for (int i : doors)
					DoorTable.getInstance().getDoor(i).openMe();
			}
		}
		//else if (event.equalsIgnoreCase("Leodas"))
		//	HellboundManager.getInstance().addSpawn(leodas, -27807, 252740, -3520, 0, 0);

		return htmltext;
	}

	public static void main(String[] args)
	{
		new Leodas(-1, "Leodas", "ai");
	}
}