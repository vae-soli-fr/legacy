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
import lineage2.gameserver.templates.manor.CropProcure;

/**
 * Format: cddd[ddddcdc[d]c[d]] cddd[dQQQcdc[d]c[d]] - Gracia Final
 */
public class ExShowCropInfo extends L2GameServerPacket
{
	private final List<CropProcure> _crops;
	private final int _manorId;
	
	public ExShowCropInfo(int manorId, List<CropProcure> crops)
	{
		_manorId = manorId;
		_crops = crops;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x24); // SubId
		writeC(0);
		writeD(_manorId); // Manor ID
		writeD(0);
		writeD(_crops.size());
		
		for (CropProcure crop : _crops)
		{
			writeD(crop.getId()); // Crop id
			writeQ(crop.getAmount()); // Buy residual
			writeQ(crop.getStartAmount()); // Buy
			writeQ(crop.getPrice()); // Buy price
			writeC(crop.getReward()); // Reward
			writeD(ManorDataHolder.getInstance().getSeedLevelByCrop(crop.getId())); // Seed
			// Level
			writeC(1); // rewrad 1 Type
			writeD(ManorDataHolder.getInstance().getRewardItem(crop.getId(), 1));
			writeC(1); // rewrad 2 Type
			writeD(ManorDataHolder.getInstance().getRewardItem(crop.getId(), 2));
		}
	}
}