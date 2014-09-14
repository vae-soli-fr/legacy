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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;

/**
 * sample 06 8f19904b 2522d04b 00000000 80 950c0000 4af50000 08f2ffff 0000 - 0 damage (missed 0x80) 06 85071048 bc0e504b 32000000 10 fc41ffff fd240200 a6f5ffff 0100 bc0e504b 33000000 10 3....
 * <p/>
 * format dddc dddh (ddc)
 */
public class Attack extends L2GameServerPacket
{
	private static final int FLAG = 0x00; // Usual kick unprinted.
	private static final int FLAG_MISS = 0x01; // Dodged the blow.
	private static final int FLAG_CRIT = 0x04; // Crit.
	private static final int FLAG_SHIELD = 0x06; // Block Crit.
	private static final int FLAG_SOULSHOT = 0x08; // Beat with a pacifier.
	
	private class Hit
	{
		int _targetId, _damage, _flags;
		
		Hit(GameObject target, int damage, boolean miss, boolean crit, boolean shld)
		{
			_targetId = target.getObjectId();
			_damage = damage;
			_flags = FLAG;
			
			if (miss)
			{
				_flags = FLAG_MISS;
			}
			else if (shld)
			{
				_flags = FLAG_SHIELD;
			}
			else if (crit)
			{
				_flags = FLAG_CRIT;
			}
			
			if (_soulshot)
			{
				_flags |= FLAG_SOULSHOT;
			}
		}
	}
	
	private final int _attackerId;
	public final boolean _soulshot;
	private final int _grade;
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _tx;
	private final int _ty;
	private final int _tz;
	private Hit[] hits;
	
	public Attack(Creature attacker, Creature target, boolean ss, int grade)
	{
		_attackerId = attacker.getObjectId();
		_soulshot = ss;
		_grade = grade;
		_x = attacker.getX();
		_y = attacker.getY();
		_z = attacker.getZ();
		_tx = target.getX();
		_ty = target.getY();
		_tz = target.getZ();
		hits = new Hit[0];
	}
	
	/**
	 * Add this hit (target, damage, miss, critical, shield) to the Server-Client packet Attack.<BR>
	 * <BR>
	 * @param target
	 * @param damage
	 * @param miss
	 * @param crit
	 * @param shld
	 */
	public void addHit(GameObject target, int damage, boolean miss, boolean crit, boolean shld)
	{
		// Get the last position in the hits table
		int pos = hits.length;
		// Create a new Hit object
		Hit[] tmp = new Hit[pos + 1];
		// Add the new Hit object to hits table
		System.arraycopy(hits, 0, tmp, 0, hits.length);
		tmp[pos] = new Hit(target, damage, miss, crit, shld);
		hits = tmp;
	}
	
	/**
	 * Return True if the Server-Client packet Attack conatins at least 1 hit.<BR>
	 * <BR>
	 * @return
	 */
	public boolean hasHits()
	{
		return hits.length > 0;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x33);
		writeD(_attackerId);
		writeD(hits[0]._targetId);
		writeC(0x00);
		writeD(hits[0]._damage);
		writeD(hits[0]._flags);
		writeD(_grade);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeH(hits.length - 1);
		
		for (int i = 1; i < hits.length; i++)
		{
			writeD(hits[i]._targetId);
			writeD(hits[i]._damage);
			writeD(hits[i]._flags);
			writeD(_grade);
		}
		
		writeD(_tx);
		writeD(_ty);
		writeD(_tz);
	}
}