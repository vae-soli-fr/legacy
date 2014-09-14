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

import java.util.List;

import lineage2.gameserver.data.xml.holder.ManorDataHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.templates.manor.SeedProduction;

/**
 * format dd[ddc[d]c[d]dddddddd] dd[ddc[d]c[d]ddddQQQQ] - Gracia Final
 */
public class ExShowSeedSetting extends L2GameServerPacket
{
	private final int _manorId;
	private final int _count;
	private final long[] _seedData; // data to send, size:_count*12
	
	public ExShowSeedSetting(int manorId)
	{
		_manorId = manorId;
		Castle c = ResidenceHolder.getInstance().getResidence(Castle.class, _manorId);
		List<Integer> seeds = ManorDataHolder.getInstance().getSeedsForCastle(_manorId);
		_count = seeds.size();
		_seedData = new long[_count * 12];
		int i = 0;
		
		for (int s : seeds)
		{
			_seedData[(i * 12) + 0] = s;
			_seedData[(i * 12) + 1] = ManorDataHolder.getInstance().getSeedLevel(s);
			_seedData[(i * 12) + 2] = ManorDataHolder.getInstance().getRewardItemBySeed(s, 1);
			_seedData[(i * 12) + 3] = ManorDataHolder.getInstance().getRewardItemBySeed(s, 2);
			_seedData[(i * 12) + 4] = ManorDataHolder.getInstance().getSeedSaleLimit(s);
			_seedData[(i * 12) + 5] = ManorDataHolder.getInstance().getSeedBuyPrice(s);
			_seedData[(i * 12) + 6] = (ManorDataHolder.getInstance().getSeedBasicPrice(s) * 60) / 100;
			_seedData[(i * 12) + 7] = ManorDataHolder.getInstance().getSeedBasicPrice(s) * 10;
			SeedProduction seedPr = c.getSeed(s, CastleManorManager.PERIOD_CURRENT);
			
			if (seedPr != null)
			{
				_seedData[(i * 12) + 8] = seedPr.getStartProduce();
				_seedData[(i * 12) + 9] = seedPr.getPrice();
			}
			else
			{
				_seedData[(i * 12) + 8] = 0;
				_seedData[(i * 12) + 9] = 0;
			}
			
			seedPr = c.getSeed(s, CastleManorManager.PERIOD_NEXT);
			
			if (seedPr != null)
			{
				_seedData[(i * 12) + 10] = seedPr.getStartProduce();
				_seedData[(i * 12) + 11] = seedPr.getPrice();
			}
			else
			{
				_seedData[(i * 12) + 10] = 0;
				_seedData[(i * 12) + 11] = 0;
			}
			
			i++;
		}
	}
	
	@Override
	public void writeImpl()
	{
		writeEx(0x26); // SubId
		writeD(_manorId); // manor id
		writeD(_count); // size
		
		for (int i = 0; i < _count; i++)
		{
			writeD((int) _seedData[(i * 12) + 0]); // seed id
			writeD((int) _seedData[(i * 12) + 1]); // level
			writeC(1);
			writeD((int) _seedData[(i * 12) + 2]); // reward 1 id
			writeC(1);
			writeD((int) _seedData[(i * 12) + 3]); // reward 2 id
			writeD((int) _seedData[(i * 12) + 4]); // next sale limit
			writeD((int) _seedData[(i * 12) + 5]); // price for castle to produce
			// 1
			writeD((int) _seedData[(i * 12) + 6]); // min seed price
			writeD((int) _seedData[(i * 12) + 7]); // max seed price
			writeQ(_seedData[(i * 12) + 8]); // today sales
			writeQ(_seedData[(i * 12) + 9]); // today price
			writeQ(_seedData[(i * 12) + 10]); // next sales
			writeQ(_seedData[(i * 12) + 11]); // next price
		}
	}
}