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
package lineage2.gameserver.model.entity.olympiad.tasks;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.entity.olympiad.OlympiadDatabase;
import lineage2.gameserver.model.entity.olympiad.OlympiadManager;
import lineage2.gameserver.network.serverpackets.SystemMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CompEndTask extends RunnableImpl
{
	private static final Logger _log = LoggerFactory.getLogger(CompEndTask.class);
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		if (Olympiad.isOlympiadEnd())
		{
			return;
		}
		
		Olympiad._inCompPeriod = false;
		
		try
		{
			OlympiadManager manager = Olympiad._manager;
			
			if ((manager != null) && !manager.getOlympiadGames().isEmpty())
			{
				ThreadPoolManager.getInstance().schedule(new CompEndTask(), 60000);
				return;
			}
			
			Announcements.getInstance().announceToAll(new SystemMessage(SystemMessage.THE_OLYMPIAD_GAME_HAS_ENDED));
			_log.info("Olympiad System: Olympiad Game Ended");
			OlympiadDatabase.save();
		}
		catch (Exception e)
		{
			_log.warn("Olympiad System: Failed to save Olympiad configuration:");
			_log.error("", e);
		}
		
		Olympiad.init();
	}
}