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
import java.util.Collections;
import java.util.List;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.entity.olympiad.OlympiadGame;
import lineage2.gameserver.model.entity.olympiad.OlympiadManager;
import lineage2.gameserver.model.entity.olympiad.TeamMember;

/**
 * @author VISTALL
 * @date 0:50/09.04.2011
 */
public abstract class ExReceiveOlympiad extends L2GameServerPacket
{
	public static class MatchList extends ExReceiveOlympiad
	{
		private List<ArenaInfo> _arenaList = Collections.emptyList();
		
		public MatchList()
		{
			super(0);
			OlympiadManager manager = Olympiad._manager;
			
			if (manager != null)
			{
				_arenaList = new ArrayList<>();
				
				for (int i = 0; i < Olympiad.STADIUMS.length; i++)
				{
					OlympiadGame game = manager.getOlympiadInstance(i);
					
					if ((game != null) && (game.getState() > 0))
					{
						_arenaList.add(new ArenaInfo(i, game.getState(), game.getType().ordinal(), game.getTeamName1(), game.getTeamName2()));
					}
				}
			}
		}
		
		@Override
		protected void writeImpl()
		{
			super.writeImpl();
			writeD(_arenaList.size());
			writeD(0x00); // unknown
			
			for (ArenaInfo arena : _arenaList)
			{
				writeD(arena._id);
				writeD(arena._matchType);
				writeD(arena._status);
				writeS(arena._name1);
				writeS(arena._name2);
			}
		}
		
		private static class ArenaInfo
		{
			final int _id;
			final int _status;
			final int _matchType;
			final String _name1;
			final String _name2;
			
			public ArenaInfo(int id, int status, int match_type, String name1, String name2)
			{
				_id = id;
				_status = status;
				_matchType = match_type;
				_name1 = name1;
				_name2 = name2;
			}
		}
	}
	
	public static class MatchResult extends ExReceiveOlympiad
	{
		@SuppressWarnings("unchecked")
		private final List<PlayerInfo>[] _players = new ArrayList[2];
		private final boolean _tie;
		private final String _name;
		
		public MatchResult(boolean tie, String name)
		{
			super(1);
			_tie = tie;
			_name = name;
		}
		
		public void addPlayer(int team, TeamMember member, int gameResultPoints)
		{
			int points = Config.OLYMPIAD_OLDSTYLE_STAT ? 0 : member.getStat().getInteger(Olympiad.POINTS, 0);
			PlayerInfo playerInfo = new PlayerInfo(member.getName(), member.getClanName(), member.getClassId(), points, gameResultPoints, (int) member.getDamage());
			
			if (_players[team] == null)
			{
				_players[team] = new ArrayList<>(2);
			}
			
			_players[team].add(playerInfo);
		}
		
		@Override
		protected void writeImpl()
		{
			super.writeImpl();
			writeD(_tie);
			writeS(_name);
			
			for (int i = 0; i < _players.length; i++)
			{
				writeD(i + 1);
				List<PlayerInfo> players = _players[i] == null ? Collections.<PlayerInfo> emptyList() : _players[i];
				writeD(players.size());
				
				for (PlayerInfo playerInfo : players)
				{
					writeS(playerInfo._name);
					writeS(playerInfo._clanName);
					writeD(0x00);
					writeD(playerInfo._classId);
					writeD(playerInfo._damage);
					writeD(playerInfo._currentPoints);
					writeD(playerInfo._gamePoints);
				}
			}
		}
		
		private static class PlayerInfo
		{
			final String _name;
			final String _clanName;
			final int _classId;
			final int _currentPoints;
			final int _gamePoints;
			final int _damage;
			
			public PlayerInfo(String name, String clanName, int classId, int currentPoints, int gamePoints, int damage)
			{
				_name = name;
				_clanName = clanName;
				_classId = classId;
				_currentPoints = currentPoints;
				_gamePoints = gamePoints;
				_damage = damage;
			}
		}
	}
	
	private final int _type;
	
	ExReceiveOlympiad(int type)
	{
		_type = type;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xD5);
		writeD(_type);
	}
}
