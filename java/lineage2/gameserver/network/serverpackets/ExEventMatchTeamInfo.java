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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;

public class ExEventMatchTeamInfo extends L2GameServerPacket
{
	@SuppressWarnings("unused")
	private final int leader_id, loot;
	private final List<EventMatchTeamInfo> members = new ArrayList<>();
	
	public ExEventMatchTeamInfo(List<Player> party, Player exclude)
	{
		leader_id = party.get(0).getObjectId();
		loot = party.get(0).getParty().getLootDistribution();
		
		for (Player member : party)
		{
			if (!member.equals(exclude))
			{
				members.add(new EventMatchTeamInfo(member));
			}
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x1C);
		// TODO dcd[dSdddddddddd]
	}
	
	private static class EventMatchTeamInfo
	{
		@SuppressWarnings("unused")
		public String _name;
		@SuppressWarnings("unused")
		public String pet_Name;
		@SuppressWarnings("unused")
		public int _id;
		@SuppressWarnings("unused")
		public int curCp;
		@SuppressWarnings("unused")
		public int maxCp;
		@SuppressWarnings("unused")
		public int curHp;
		@SuppressWarnings("unused")
		public int maxHp;
		@SuppressWarnings("unused")
		public int curMp;
		@SuppressWarnings("unused")
		public int maxMp;
		@SuppressWarnings("unused")
		public int level;
		@SuppressWarnings("unused")
		public int class_id;
		@SuppressWarnings("unused")
		public int race_id;
		@SuppressWarnings("unused")
		public int pet_id;
		@SuppressWarnings("unused")
		public int pet_NpcId;
		@SuppressWarnings("unused")
		public int pet_curHp;
		@SuppressWarnings("unused")
		public int pet_maxHp;
		@SuppressWarnings("unused")
		public int pet_curMp;
		@SuppressWarnings("unused")
		public int pet_maxMp;
		@SuppressWarnings("unused")
		public int pet_level;
		
		EventMatchTeamInfo(Player member)
		{
			_name = member.getName();
			_id = member.getObjectId();
			curCp = (int) member.getCurrentCp();
			maxCp = member.getMaxCp();
			curHp = (int) member.getCurrentHp();
			maxHp = member.getMaxHp();
			curMp = (int) member.getCurrentMp();
			maxMp = member.getMaxMp();
			level = member.getLevel();
			class_id = member.getClassId().getId();
			race_id = member.getRace().ordinal();
			Summon pet = member.getSummonList().getFirstServitor();
			
			if (pet != null)
			{
				pet_id = pet.getObjectId();
				pet_NpcId = pet.getId() + 1000000;
				pet_Name = pet.getName();
				pet_curHp = (int) pet.getCurrentHp();
				pet_maxHp = pet.getMaxHp();
				pet_curMp = (int) pet.getCurrentMp();
				pet_maxMp = pet.getMaxMp();
				pet_level = pet.getLevel();
			}
			else
			{
				pet_id = 0;
			}
		}
	}
}