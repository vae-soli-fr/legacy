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

/**
 * Format (ch)dddcccd d: cahacter oid d: time left d: fish hp c: c: c: 00 if fish gets damage 02 if fish regens d:
 */
public class ExFishingHpRegen extends L2GameServerPacket
{
	private final int _time;
	private final int _fishHP;
	private final int _HPmode;
	private final int _Anim;
	private final int _GoodUse;
	private final int _Penalty;
	private final int _hpBarColor;
	private final int char_obj_id;
	
	public ExFishingHpRegen(Creature character, int time, int fishHP, int HPmode, int GoodUse, int anim, int penalty, int hpBarColor)
	{
		char_obj_id = character.getObjectId();
		_time = time;
		_fishHP = fishHP;
		_HPmode = HPmode;
		_GoodUse = GoodUse;
		_Anim = anim;
		_Penalty = penalty;
		_hpBarColor = hpBarColor;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x28);
		writeD(char_obj_id);
		writeD(_time);
		writeD(_fishHP);
		writeC(_HPmode); // 0 = HP stop, 1 = HP raise
		writeC(_GoodUse); // 0 = none, 1 = success, 2 = failed
		writeC(_Anim); // Anim: 0 = none, 1 = reeling, 2 = pumping
		writeD(_Penalty); // Penalty
		writeC(_hpBarColor); // 0 = normal hp bar, 1 = purple hp bar
	}
}