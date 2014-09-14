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
package lineage2.gameserver.instancemanager;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KilRoy
 */
public class SoHManager
{
	private static final Logger _log = LoggerFactory.getLogger(SoHManager.class);
	private static SoHManager _instance;
	private static final String SPAWN_GROUP = "soh_all1";
	private static final String SPAWN_GROUP2 = "soh_all3";
	private static final long SOH_OPEN_TIME = 24 * 60 * 60 * 1000L;
	private static Zone _zone;
	
	public static SoHManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new SoHManager();
		}
		
		return _instance;
	}
	
	public SoHManager()
	{
		_log.info("Seed of Hellfire Manager: Loaded.");
		_zone = ReflectionUtils.getZone("[inner_hellfire01]");
		checkStageAndSpawn();
		
		if (!isSeedOpen())
		{
			openSeed(getOpenedTime());
		}
	}
	
	private static Zone getZone()
	{
		return _zone;
	}
	
	private static long getOpenedTime()
	{
		if (getCurrentStage() != 2)
		{
			return 0;
		}
		
		return (ServerVariables.getLong("SoH_opened", 0) * 1000L) - System.currentTimeMillis();
	}
	
	private static boolean isSeedOpen()
	{
		return getOpenedTime() > 0;
	}
	
	public static void setCurrentStage(int stage)
	{
		if (getCurrentStage() == stage)
		{
			return;
		}
		
		if (stage == 2)
		{
			openSeed(SOH_OPEN_TIME);
		}
		else if (isSeedOpen())
		{
			closeSeed();
		}
		
		ServerVariables.set("SoH_stage", stage);
		checkStageAndSpawn();
		_log.info("Seed of Hellfire Manager: Set to stage " + stage);
	}
	
	public static int getCurrentStage()
	{
		return ServerVariables.getInt("SoH_stage", 1);
	}
	
	private static void checkStageAndSpawn()
	{
		SpawnManager.getInstance().despawn(SPAWN_GROUP);
		SpawnManager.getInstance().despawn(SPAWN_GROUP2);
		
		switch (getCurrentStage())
		{
			case 1:
				SpawnManager.getInstance().spawn(SPAWN_GROUP);
				break;
			
			case 2:
				SpawnManager.getInstance().spawn(SPAWN_GROUP);
				break;
			
			default:
				SpawnManager.getInstance().spawn(SPAWN_GROUP);
				break;
		}
	}
	
	private static void openSeed(long timelimit)
	{
		if (timelimit <= 0)
		{
			return;
		}
		
		ServerVariables.unset("Tauti_kills");
		ServerVariables.set("SoH_opened", (System.currentTimeMillis() + timelimit) / 1000L);
		_log.info("Seed of Hellfire Manager: Opening the seed for " + Util.formatTime((int) timelimit / 1000));
		SpawnManager.getInstance().spawn(SPAWN_GROUP);
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				closeSeed();
				setCurrentStage(1);
			}
		}, timelimit);
	}
	
	static void closeSeed()
	{
		_log.info("Seed of Hellfire Manager: Closing the seed.");
		ServerVariables.unset("SoH_opened");
		SpawnManager.getInstance().despawn(SPAWN_GROUP);
		
		for (Playable p : getZone().getInsidePlayables())
		{
			p.teleToLocation(getZone().getRestartPoints().get(0));
		}
	}
}