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

import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;

import org.apache.commons.lang3.StringUtils;

public class PledgeShowInfoUpdate extends L2GameServerPacket
{
	private final int clan_id;
	private final int clan_level;
	private final int clan_rank;
	private final int clan_rep;
	private final int crest_id;
	private final int ally_id;
	private int ally_crest;
	private final int _territorySide;
	private final int atwar;
	private String ally_name = StringUtils.EMPTY;
	private final int HasCastle;
	private final int HasHideout;
	private final int HasFortress;
	
	public PledgeShowInfoUpdate(final Clan clan)
	{
		clan_id = clan.getClanId();
		clan_level = clan.getLevel();
		HasCastle = clan.getCastle();
		HasHideout = clan.getHasHideout();
		HasFortress = clan.getHasFortress();
		clan_rank = clan.getRank();
		clan_rep = clan.getReputationScore();
		crest_id = clan.getCrestId();
		ally_id = clan.getAllyId();
		atwar = clan.isAtWar();
		_territorySide = clan.getWarDominion();
		Alliance ally = clan.getAlliance();
		
		if (ally != null)
		{
			ally_name = ally.getAllyName();
			ally_crest = ally.getAllyCrestId();
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		// dddddddddddd Sdddd
		writeC(0x8e);
		// sending empty data so client will ask all the info in response ;)
		writeD(clan_id);
		writeD(crest_id);
		writeD(clan_level);
		writeD(HasCastle);
		writeD(0);
		writeD(HasHideout);
		writeD(HasFortress);
		writeD(clan_rank);// displayed in the "tree" view (with the clan skills)
		writeD(clan_rep);
		writeD(0);
		writeD(0);
		writeD(ally_id); // c5
		writeS(ally_name); // c5
		writeD(ally_crest); // c5
		writeD(atwar); // c5
		writeD(0x00);
		writeD(_territorySide);
	}
}