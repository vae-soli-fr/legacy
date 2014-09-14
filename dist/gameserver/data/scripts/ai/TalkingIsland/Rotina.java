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
package ai.TalkingIsland;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class Rotina extends RotinaSubAI
{
	/**
	 * Constructor for Rotina.
	 * @param actor NpcInstance
	 */
	public Rotina(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-116493, 257062, -1512),
			new Location(-114296, 255704, -1537)
		};
	}
}
