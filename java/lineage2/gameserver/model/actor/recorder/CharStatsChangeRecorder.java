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
package lineage2.gameserver.model.actor.recorder;

import javolution.util.FastList;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.base.TeamType;
import gnu.trove.list.array.TIntArrayList;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 * @param <T>
 */
public class CharStatsChangeRecorder<T extends Creature>
{
	static final int BROADCAST_CHAR_INFO = 1 << 0;
	static final int SEND_CHAR_INFO = 1 << 1;
	static final int SEND_STATUS_INFO = 1 << 2;
	protected final T _activeChar;
	private int _level;
	private int _accuracy;
	private int _attackSpeed;
	private int _castSpeed;
	private int _criticalHit;
	private int _evasion;
	private int _magicAttack;
	private int _magicDefence;
	private int _maxHp;
	private int _maxMp;
	private int _physicAttack;
	private int _physicDefence;
	private int _runSpeed;
	protected TeamType _team;
	protected int _changes;
	protected final TIntArrayList _abnormalEffects = new TIntArrayList();
	protected final FastList<Integer> _aveList = new FastList<>();
	
	/**
	 * Constructor for CharStatsChangeRecorder.
	 * @param actor T
	 */
	public CharStatsChangeRecorder(T actor)
	{
		this._activeChar = actor;
	}
	
	/**
	 * Method set.
	 * @param flag int
	 * @param oldValue int
	 * @param newValue int
	 * @return int
	 */
	protected int set(int flag, int oldValue, int newValue)
	{
		if (oldValue != newValue)
		{
			_changes |= flag;
		}
		
		return newValue;
	}
	
	/**
	 * Method set.
	 * @param flag int
	 * @param oldValue long
	 * @param newValue long
	 * @return long
	 */
	protected long set(int flag, long oldValue, long newValue)
	{
		if (oldValue != newValue)
		{
			_changes |= flag;
		}
		
		return newValue;
	}
	
	/**
	 * Method set.
	 * @param flag int
	 * @param oldValue String
	 * @param newValue String
	 * @return String
	 */
	protected String set(int flag, String oldValue, String newValue)
	{
		if (!oldValue.equals(newValue))
		{
			_changes |= flag;
		}
		
		return newValue;
	}
	
	/**
	 * Method set.
	 * @param <E>
	 * @param flag int
	 * @param oldValue E
	 * @param newValue E
	 * @return E
	 */
	protected <E extends Enum<E>> E set(int flag, E oldValue, E newValue)
	{
		if (oldValue != newValue)
		{
			_changes |= flag;
		}
		
		return newValue;
	}
	
	/**
	 * Method set.
	 * @param flag int
	 * @param oldValue TIntArrayList
	 * @param newValue TIntArrayList
	 * @return TIntArrayList
	 */
	protected TIntArrayList set(int flag, TIntArrayList oldValue, TIntArrayList newValue)
	{
		if ((oldValue.size() != newValue.size()) || !newValue.containsAll(oldValue))
		{
			_changes |= flag;
			oldValue.clear();
			oldValue.addAll(newValue);
		}
		
		return oldValue;
	}
	
	/**
	 * Method refreshStats.
	 */
	protected void refreshStats()
	{
		_accuracy = set(SEND_CHAR_INFO, _accuracy, _activeChar.getAccuracy());
		_attackSpeed = set(BROADCAST_CHAR_INFO, _attackSpeed, _activeChar.getPAtkSpd());
		_castSpeed = set(BROADCAST_CHAR_INFO, _castSpeed, _activeChar.getMAtkSpd());
		_criticalHit = set(SEND_CHAR_INFO, _criticalHit, _activeChar.getCriticalHit(null, null));
		_evasion = set(SEND_CHAR_INFO, _evasion, _activeChar.getEvasionRate(null));
		_runSpeed = set(BROADCAST_CHAR_INFO, _runSpeed, _activeChar.getRunSpeed());
		_physicAttack = set(SEND_CHAR_INFO, _physicAttack, _activeChar.getPAtk(null));
		_physicDefence = set(SEND_CHAR_INFO, _physicDefence, _activeChar.getPDef(null));
		_magicAttack = set(SEND_CHAR_INFO, _magicAttack, _activeChar.getMAtk(null, null));
		_magicDefence = set(SEND_CHAR_INFO, _magicDefence, _activeChar.getMDef(null, null));
		_maxHp = set(SEND_STATUS_INFO, _maxHp, _activeChar.getMaxHp());
		_maxMp = set(SEND_STATUS_INFO, _maxMp, _activeChar.getMaxMp());
		_level = set(SEND_CHAR_INFO, _level, _activeChar.getLevel());
		_team = set(BROADCAST_CHAR_INFO, _team, _activeChar.getTeam());
	}
	
	/**
	 * Method sendChanges.
	 */
	public final void sendChanges()
	{
		refreshStats();
		onSendChanges();
		_changes = 0;
	}
	
	/**
	 * Method onSendChanges.
	 */
	protected void onSendChanges()
	{
		if ((_changes & SEND_STATUS_INFO) == SEND_STATUS_INFO)
		{
			_activeChar.broadcastStatusUpdate();
		}
	}
}
