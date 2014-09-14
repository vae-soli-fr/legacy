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
import java.util.Map;
import java.util.TreeMap;

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.templates.manor.CropProcure;

/**
 * format dd[dddc] dd[dQQc] - Gracia Final
 */
public class ExShowProcureCropDetail extends L2GameServerPacket
{
	private final int _cropId;
	private final Map<Integer, CropProcure> _castleCrops;
	
	public ExShowProcureCropDetail(int cropId)
	{
		_cropId = cropId;
		_castleCrops = new TreeMap<>();
		List<Castle> castleList = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		
		for (Castle c : castleList)
		{
			CropProcure cropItem = c.getCrop(_cropId, CastleManorManager.PERIOD_CURRENT);
			
			if ((cropItem != null) && (cropItem.getAmount() > 0))
			{
				_castleCrops.put(c.getId(), cropItem);
			}
		}
	}
	
	@Override
	public void writeImpl()
	{
		writeEx(0x79);
		writeD(_cropId); // crop id
		writeD(_castleCrops.size()); // size
		
		for (int manorId : _castleCrops.keySet())
		{
			CropProcure crop = _castleCrops.get(manorId);
			writeD(manorId); // manor name
			writeQ(crop.getAmount()); // buy residual
			writeQ(crop.getPrice()); // buy price
			writeC(crop.getReward()); // reward type
		}
	}
}