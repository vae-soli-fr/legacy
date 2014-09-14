/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;

public class EtcStatusUpdate extends L2GameServerPacket
{
	private final int IncreasedForce;
	private final int WeightPenalty;
	private final int MessageRefusal;
	private final int DangerArea;
	private final int armorExpertisePenalty;
	private final int weaponExpertisePenalty;
	private final int CharmOfCourage;
	private final int DeathPenaltyLevel;
	private final int ConsumedSouls;
	
	public EtcStatusUpdate(Player player)
	{
		IncreasedForce = player.getIncreasedForce();
		WeightPenalty = player.getWeightPenalty();
		MessageRefusal = player.getMessageRefusal() || (player.getNoChannel() != 0) || player.isBlockAll() ? 1 : 0;
		DangerArea = player.isInDangerArea() ? 1 : 0;
		armorExpertisePenalty = player.getArmorsExpertisePenalty();
		weaponExpertisePenalty = player.getWeaponsExpertisePenalty();
		CharmOfCourage = player.isCharmOfCourage() ? 1 : 0;
		DeathPenaltyLevel = player.getDeathPenalty() == null ? 0 : player.getDeathPenalty().getLevel(player);
		ConsumedSouls = player.getConsumedSouls();
	}
	
	@Override
	protected final void writeImpl()
	{
		// dddddddd
		writeC(0xf9); // Packet type
		writeD(IncreasedForce); // skill id 4271, 7 lvl
		writeD(WeightPenalty); // skill id 4270, 4 lvl
		writeD(MessageRefusal); // skill id 4269, 1 lvl
		writeD(DangerArea); // skill id 4268, 1 lvl
		writeD(weaponExpertisePenalty); // weapon grade penalty, skill 6209 in
		// epilogue
		writeD(armorExpertisePenalty); // armor grade penalty, skill 6213 in
		// epilogue
		writeD(CharmOfCourage); // Charm of Courage,
		// "Prevents experience value decreasing if killed during a siege war".
		writeD(DeathPenaltyLevel); // Death Penalty max lvl 15,
		// "Combat ability is decreased due to death."
		writeD(ConsumedSouls);
	}
}