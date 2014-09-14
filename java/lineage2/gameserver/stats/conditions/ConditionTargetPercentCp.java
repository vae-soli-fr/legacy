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
package lineage2.gameserver.stats.conditions;

import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ConditionTargetPercentCp extends Condition
{
	private final double _cp;
	
	/**
	 * Constructor for ConditionTargetPercentCp.
	 * @param cp int
	 */
	public ConditionTargetPercentCp(int cp)
	{
		_cp = cp / 100.;
	}
	
	/**
	 * Method testImpl.
	 * @param env Env
	 * @return boolean
	 */
	@Override
	protected boolean testImpl(Env env)
	{
		return (env.target != null) && (env.target.getCurrentCpRatio() <= _cp);
	}
}
