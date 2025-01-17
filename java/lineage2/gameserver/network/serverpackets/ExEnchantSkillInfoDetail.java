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

import lineage2.gameserver.tables.SkillTreeTable;

/**
 * Дамп с оффа, 828 протокол: 0000: fe 5e 00 01 00 00 00 46 00 00 00 65 00 00 00 7e 0010: cc 12 00 fe fc bb 00 00 00 00 00 61 00 00 00 01 0020: 00 00 00 05 00 00 00 9b 25 00 00 00 00 00 00
 * <p/>
 * Скилл: Drain Health (id: 70) Левел скила: 53 Нужен предмет: Giant's Secret Codex of Mastery - 1 штука Точим на +1 Power (lvl: 101) Требуется SP: 1231998 Требуется exp: 12319998 Шанс успеха: 97%
 * <p/>
 * Еще дампы для примера: 0000: fe 5e 00 01 00 00 00 7a 00 00 00 6b 00 00 00 60 0010: 81 1e 00 c0 0d 31 01 00 00 00 00 50 00 00 00 01 0020: 00 00 00 05 00 00 00 9b 25 00 00 00 00 00 00
 * <p/>
 * 0000: fe 5e 00 03 00 00 00 9a 01 00 00 d0 00 00 00 20 0010: a9 03 00 40 9b 24 00 00 00 00 00 64 00 00 00 01 0020: 00 00 00 05 00 00 00 9a 25 00 00 00 00 00 00
 * <p/>
 * 0000: fe 5e 00 00 00 00 00 6f 00 00 00 65 00 00 00 d5 0010: 79 04 00 55 c2 2c 00 00 00 00 00 61 00 00 00 01 0020: 00 00 00 05 00 00 00 de 19 00 00 00 00 00 00
 */
public class ExEnchantSkillInfoDetail extends L2GameServerPacket
{
	private final int _unk = 0;
	private final int _skillId;
	private final int _skillLvl;
	private final int _sp;
	private final int _chance;
	private final int _bookId, _adenaCount;
	
	public ExEnchantSkillInfoDetail(int skillId, int skillLvl, int sp, int chance, int bookId, int adenaCount)
	{
		_skillId = skillId;
		_skillLvl = skillLvl;
		_sp = sp;
		_chance = chance;
		_bookId = bookId;
		_adenaCount = adenaCount;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x5F);
		// FIXME GraciaEpilogue ddddd dx[dd]
		writeD(_unk); // ?
		writeD(_skillId);
		writeD(_skillLvl);
		writeD(_sp);
		writeD(_chance);
		writeD(2);
		writeD(57); // adena
		writeD(_adenaCount); // adena count ?
		
		if (_bookId > 0)
		{
			writeD(_bookId); // book
			writeD(1); // book count
		}
		else
		{
			writeD(SkillTreeTable.NORMAL_ENCHANT_BOOK); // book
			writeD(0); // book count
		}
	}
}