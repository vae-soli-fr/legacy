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
package ai.individual;

import ai.group_template.L2AttackableAIScript;

import com.l2jserver.gameserver.datatables.DoorTable;
import com.l2jserver.gameserver.instancemanager.HellboundManager;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;

/**
 * @author theOne
 */
public class Leodas extends L2AttackableAIScript
{
	private static final int leodas = 22448;
	private static final int traitor = 32364;

	private static final int[] doors = {
			19250003, 19250004
	};

	private boolean leodasOnAttack = false;

	public Leodas(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addAttackId(leodas);
		addKillId(leodas);
		addTalkId(traitor);
		addFirstTalkId(traitor);
		addStartNpc(traitor);
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

		HellboundManager.getInstance().increaseTrust(-1000); //value needs to be updated
		leodasOnAttack = false;

		return super.onKill(npc, player, isPet);
	}

	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		if (leodasOnAttack)
			return "<html><body>Hellbound Traitor:<br>Leodas already attacked!</body></html>";

		int hellboundLevel = HellboundManager.getInstance().getLevel();
		if (hellboundLevel < 5 && hellboundLevel > 6)
			return null;

		npc.showChatWindow(player);

		return super.onFirstTalk(npc, player);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		if (event.equalsIgnoreCase("meetLeodas"))
		{
			long marksCount = player.getInventory().getItemByItemId(9676).getCount();
			if (marksCount == 0)
				htmltext = "<html><body>Hellbound Traitor:<br>I need <font color=\"LEVEL\">10 Mark of Betrayal</font>. But you did not bring one! Do not attempt to deceive me - bring to me that I was requested!</body></html>";
			else if (marksCount >= 1 && marksCount < 10)
				htmltext = "<html><body>Hellbound Traitor:<br>Yeah! You have a <font color=\"LEVEL\">Mark of Betrayal</font>. Unfortunately, I can in no way help you, if you do not bring to me 10 Marks. Bring all Marks to me, and I will immediately open door.</body></html>";
			else if (marksCount >= 10)
			{
				player.destroyItemByItemId("item", 9676, 10, player, true);
				npc.broadcastPacket(new CreatureSay(npc.getObjectId(), 1, npc.getName(), "Brothers! This stranger wants to kill our Commander!!!"));
				startQuestTimer("Leodas", 3000, npc, null, false);
				leodasOnAttack = true;
				for (int i : doors)
					DoorTable.getInstance().getDoor(i).openMe();
			}
		}
		else if (event.equalsIgnoreCase("Leodas"))
			HellboundManager.getInstance().addSpawn(leodas, -27807, 252740, -3520, 0, 0);

		return htmltext;
	}

	public static void main(String[] args)
	{
		new Leodas(-1, "Leodas", "ai");
	}
}