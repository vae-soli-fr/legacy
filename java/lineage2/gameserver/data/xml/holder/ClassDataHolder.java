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
package lineage2.gameserver.data.xml.holder;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.templates.player.ClassData;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Smo
 */
public class ClassDataHolder extends AbstractHolder
{
	private static final ClassDataHolder _instance = new ClassDataHolder();
	private final TIntObjectHashMap<ClassData> _classDataList = new TIntObjectHashMap<>();
	
	public static ClassDataHolder getInstance()
	{
		return _instance;
	}
	
	public void addClassData(ClassData classData)
	{
		_classDataList.put(classData.getClassId(), classData);
	}
	
	public ClassData getClassData(int classId)
	{
		return _classDataList.get(classId);
	}
	
	@Override
	public int size()
	{
		return _classDataList.size();
	}
	
	@Override
	public void clear()
	{
		_classDataList.clear();
	}
}