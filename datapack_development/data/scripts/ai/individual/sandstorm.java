/*
 * This program is free software: you can redistribute it and/or modify it under
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
# Update by U3Games 04-04-2011
# Special thanks to contributors users l2jserver
 */

package ai.individual;

import ai.group_template.L2AttackableAIScript;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Ectis
 */

public class sandstorm extends L2AttackableAIScript
{
	private static final int SANDSTORM = 32350;
	 
	public sandstorm (int questId, String name, String descr)
	{
		super(questId, name, descr);
		super.addAttackId(SANDSTORM);
	}

	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isPet)
	{
	int npcId = npc.getNpcId();

		if (npcId == SANDSTORM)
		{
			npc.setTarget(player);
			npc.doCast(SkillTable.getInstance().getInfo(5435, 1));
		}

	return super.onAggroRangeEnter(npc, player, isPet);
	}

	public static void main(String[] args)
	{
		new sandstorm (-1, "sandstorm ", "ai");
	}
}