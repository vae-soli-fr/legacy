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
import lineage2.gameserver.templates.manor.CropProcure;

/**
 * format dd[ddc[d]c[d]ddddddcddc] dd[ddc[d]c[d]ddddQQcQQc] - Gracia Final
 */
public class ExShowCropSetting extends L2GameServerPacket
{
	private final int _manorId;
	private final int _count;
	private final long[] _cropData; // data to send, size:_count*14
	
	public ExShowCropSetting(int manorId)
	{
		_manorId = manorId;
		Castle c = ResidenceHolder.getInstance().getResidence(Castle.class, _manorId);
		List<Integer> crops = ManorDataHolder.getInstance().getCropsForCastle(_manorId);
		_count = crops.size();
		_cropData = new long[_count * 14];
		int i = 0;
		
		for (int cr : crops)
		{
			_cropData[(i * 14) + 0] = cr;
			_cropData[(i * 14) + 1] = ManorDataHolder.getInstance().getSeedLevelByCrop(cr);
			_cropData[(i * 14) + 2] = ManorDataHolder.getInstance().getRewardItem(cr, 1);
			_cropData[(i * 14) + 3] = ManorDataHolder.getInstance().getRewardItem(cr, 2);
			_cropData[(i * 14) + 4] = ManorDataHolder.getInstance().getCropPuchaseLimit(cr);
			_cropData[(i * 14) + 5] = 0; // Looks like not used
			_cropData[(i * 14) + 6] = (ManorDataHolder.getInstance().getCropBasicPrice(cr) * 60) / 100;
			_cropData[(i * 14) + 7] = ManorDataHolder.getInstance().getCropBasicPrice(cr) * 10;
			CropProcure cropPr = c.getCrop(cr, CastleManorManager.PERIOD_CURRENT);
			
			if (cropPr != null)
			{
				_cropData[(i * 14) + 8] = cropPr.getStartAmount();
				_cropData[(i * 14) + 9] = cropPr.getPrice();
				_cropData[(i * 14) + 10] = cropPr.getReward();
			}
			else
			{
				_cropData[(i * 14) + 8] = 0;
				_cropData[(i * 14) + 9] = 0;
				_cropData[(i * 14) + 10] = 0;
			}
			
			cropPr = c.getCrop(cr, CastleManorManager.PERIOD_NEXT);
			
			if (cropPr != null)
			{
				_cropData[(i * 14) + 11] = cropPr.getStartAmount();
				_cropData[(i * 14) + 12] = cropPr.getPrice();
				_cropData[(i * 14) + 13] = cropPr.getReward();
			}
			else
			{
				_cropData[(i * 14) + 11] = 0;
				_cropData[(i * 14) + 12] = 0;
				_cropData[(i * 14) + 13] = 0;
			}
			
			i++;
		}
	}
	
	@Override
	public void writeImpl()
	{
		writeEx(0x2b); // SubId
		writeD(_manorId); // manor id
		writeD(_count); // size
		
		for (int i = 0; i < _count; i++)
		{
			writeD((int) _cropData[(i * 14) + 0]); // crop id
			writeD((int) _cropData[(i * 14) + 1]); // seed level
			writeC(1);
			writeD((int) _cropData[(i * 14) + 2]); // reward 1 id
			writeC(1);
			writeD((int) _cropData[(i * 14) + 3]); // reward 2 id
			writeD((int) _cropData[(i * 14) + 4]); // next sale limit
			writeD((int) _cropData[(i * 14) + 5]); // ???
			writeD((int) _cropData[(i * 14) + 6]); // min crop price
			writeD((int) _cropData[(i * 14) + 7]); // max crop price
			writeQ(_cropData[(i * 14) + 8]); // today buy
			writeQ(_cropData[(i * 14) + 9]); // today price
			writeC((int) _cropData[(i * 14) + 10]); // today reward
			writeQ(_cropData[(i * 14) + 11]); // next buy
			writeQ(_cropData[(i * 14) + 12]); // next price
			writeC((int) _cropData[(i * 14) + 13]); // next reward
		}
	}
}