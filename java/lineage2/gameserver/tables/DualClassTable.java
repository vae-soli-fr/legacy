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
package lineage2.gameserver.tables;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SubClass;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.SubClassType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class DualClassTable
{
	private static final Logger _log = LoggerFactory.getLogger(DualClassTable.class);
	private static DualClassTable _instance;
	private TIntObjectHashMap<TIntArrayList> _dualClasses;
	
	/**
	 * Constructor for SubClassTable.
	 */
	public DualClassTable()
	{
		init();
	}
	
	/**
	 * Method getInstance.
	 * @return SubClassTable
	 */
	public static DualClassTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new DualClassTable();
		}
		
		return _instance;
	}
	
	/**
	 * Method init.
	 */
	private void init()
	{
		_dualClasses = new TIntObjectHashMap<>();
		
		for (ClassId baseClassId : ClassId.VALUES)
		{
			if (!baseClassId.isOfLevel(ClassLevel.Fourth))
			{
				continue;
			}
			
			TIntArrayList availSubs = new TIntArrayList();
			
			for (ClassId subClassId : ClassId.VALUES)
			{
				if (!subClassId.isOfLevel(ClassLevel.Fourth))
				{
					continue;
				}
				
				if (subClassId == baseClassId)
				{
					continue;
				}
				
				availSubs.add(subClassId.getId());
			}
			
			availSubs.sort();
			_dualClasses.put(baseClassId.getId(), availSubs);
		}
		
		_log.info("DualClassTable: Loaded " + _dualClasses.size() + " dual-classes variations.");
	}
	
	/**
	 * Method getAvailableSubClasses.
	 * @param player Player
	 * @param classId int
	 * @return int[]
	 */
	public int[] getAvailableDualClasses(Player player, int classId)
	{
		TIntArrayList dualClassesList = _dualClasses.get(classId);
		SubClassType haveDouble = null;
		
		for (SubClass sc : player.getSubClassList().values())
		{
			if (sc.isDual())
			{
				haveDouble = sc.getType();
			}
		}
		
		if ((dualClassesList == null) || dualClassesList.isEmpty() || (haveDouble == null))
		{
			return new int[0];
		}
		
		loop:
		
		for (int clsId : dualClassesList.toArray())
		{
			int baseClassId = player.getBaseSubClass().getClassId();
			
			if (clsId == baseClassId)
			{
				dualClassesList.remove(clsId);
				continue loop;
			}
		}
		
		return dualClassesList.toArray();
	}
}
