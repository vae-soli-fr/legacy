package ai.individual;

import ai.group_template.L2AttackableAIScript;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.instancemanager.HellboundManager;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class Remnant_ghosts extends L2AttackableAIScript
{
	int npcId = 0;

	L2Npc curNpc;

	private static final int[] remnants =
	{
		18463, 18464
	};

	private static final int Derek = 18465;

	private static final int[][] RemnantSpawns =
	{
		{18464, -28681, 255110, -2160, 17318, 120},
		{18464, -26114, 254708, -2139, 6976, 120},
		{18463, -28457, 256584, -1926, 59926, 120},
		{18463, -26482, 257663, -1925, 35352, 120},
		{18464, -26453, 256745, -1930, 50259, 120},
		{18463, -27362, 256282, -1935, 47936, 120},
		{18464, -25441, 256441, -2147, 48011, 120}
	};

	public Remnant_ghosts(int questId, String name, String descr)
	{
		super(questId, name, descr);
		for (int i : remnants)
		{
			addAttackId(i);
			addSkillSeeId(i);
			addKillId(i);
		}
		addSkillSeeId(Derek);
		addAttackId(Derek);
		addKillId(22330);
		addKillId(Derek);

		int hblvl = HellboundManager.getInstance().getLevel();

		if (hblvl <= 4)
		{
			for (int i = 0; i < RemnantSpawns.length; i++)
			{
				int[] loc = RemnantSpawns[i];
				HellboundManager.getInstance().addSpawn(loc[0], loc[1], loc[2], loc[3], loc[4], loc[5]);
			}
		}

		if (hblvl > 4)
		{
			for (L2Spawn spawn : SpawnTable.getInstance().getSpawnTable().values())
			{
				npcId = spawn.getNpcid();
				for (int i : remnants)
				{
					if (i == npcId)
					{
						curNpc = spawn.getLastSpawn();
						curNpc.deleteMe();
					}
				}
			}
		}
	}

	public String onSkillSee(L2Npc npc, L2PcInstance player, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		for (L2Object obj : targets)
		{
			if (npc != obj)
				return null;
		}

		if (skill.getId() == 2358)
		{
			npcId = npc.getNpcId();
			int maxHp = npc.getMaxHp();
			double currentHp = npc.getStatus().getCurrentHp();
			int triggerHp = (maxHp * 4) / 100;

			if (currentHp <= triggerHp)
			{
				npc.setIsInvul(false);
				L2Character dead = (L2Character) npc;
				dead.doDie(player);
			}
		}

		return super.onSkillSee(npc, player, skill, targets, isPet);
	}

	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet)
	{
		npcId = npc.getNpcId();

		for (int i : remnants)
		{
			if (i == npcId)
			{
				int maxHp = npc.getMaxHp();
				double currentHp = npc.getStatus().getCurrentHp();
				int triggerHp = (maxHp * 4) / 100;

				if (currentHp <= triggerHp)
					npc.setIsInvul(true);
			}
		}

		if (npcId == Derek)
		{
			int maxHp = npc.getMaxHp();
				double currentHp = npc.getStatus().getCurrentHp();
				int triggerHp = (maxHp * 5) / 100;

				if (currentHp <= triggerHp)
					npc.setIsInvul(true);
		}

		return super.onAttack(npc, player, damage, isPet);
	}

	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		npcId = npc.getNpcId();
		int level = HellboundManager.getInstance().getLevel();

		if (npcId == 22330)
		{
			if (level >= 4) return null;
            HellboundManager.getInstance().increaseTrust(5);
		}
		else if (npcId == Derek)
		{
			HellboundManager.getInstance().increaseTrust(10000);
            HellboundManager.getInstance().changeLevel(5);
            player.sendMessage("The holy water affects Derek. You have freed his soul.");
		}
		else
		{
			for (int i : remnants)
			{
				if (i == npcId)
				{
					if (level < 4)
					{
						HellboundManager.getInstance().increaseTrust(5);
						player.sendMessage("The holy water affects Remnants Ghost. You have freed his soul.");
					}
					else
						player.sendMessage("The holy water affects Remnants Ghost. You have freed his soul.");
				}
			}
		}

		return super.onKill(npc, player, isPet);
	}

	public static void main(String[] args)
	{
		new Remnant_ghosts(-1, "Remnant_ghosts", "ai");
	}
}