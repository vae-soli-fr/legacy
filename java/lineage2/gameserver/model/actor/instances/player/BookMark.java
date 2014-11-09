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
package lineage2.gameserver.model.actor.instances.player;

import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BookMark
{
	public final int x, y, z;
	private int icon;
	private String name, acronym;
	
	/**
	 * Constructor for BookMark.
	 * @param loc Location
	 * @param aicon int
	 * @param aname String
	 * @param aacronym String
	 */
	BookMark(Location loc, int aicon, String aname, String aacronym)
	{
		this(loc.getX(), loc.getY(), loc.getZ(), aicon, aname, aacronym);
	}
	
	/**
	 * Constructor for BookMark.
	 * @param _x int
	 * @param _y int
	 * @param _z int
	 * @param aicon int
	 * @param aname String
	 * @param aacronym String
	 */
	BookMark(int _x, int _y, int _z, int aicon, String aname, String aacronym)
	{
		x = _x;
		y = _y;
		z = _z;
		setIcon(aicon);
		setName(aname);
		setAcronym(aacronym);
	}
	
	/**
	 * Method setIcon.
	 * @param val int
	 * @return BookMark
	 */
	public BookMark setIcon(int val)
	{
		icon = val;
		return this;
	}
	
	/**
	 * Method getIcon.
	 * @return int
	 */
	public int getIcon()
	{
		return icon;
	}
	
	/**
	 * Method setName.
	 * @param val String
	 * @return BookMark
	 */
	public BookMark setName(String val)
	{
		name = val.length() > 32 ? val.substring(0, 32) : val;
		return this;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Method setAcronym.
	 * @param val String
	 * @return BookMark
	 */
	public BookMark setAcronym(String val)
	{
		acronym = val.length() > 4 ? val.substring(0, 4) : val;
		return this;
	}
	
	/**
	 * Method getAcronym.
	 * @return String
	 */
	public String getAcronym()
	{
		return acronym;
	}
	
	/**
	 * Method getX.
	 * @return x int
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * Method getY.
	 * @return y int
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * Method getY.
	 * @return z int
	 */
	public int getZ()
	{
		return z;
	}
}
