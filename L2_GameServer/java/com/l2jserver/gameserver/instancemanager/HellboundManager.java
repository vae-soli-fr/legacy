/**
 * Mod Hellbound pour L2J
 */

package com.l2jserver.gameserver.instancemanager;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.datatables.NpcTable;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.templates.chars.L2NpcTemplate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;



public class HellboundManager
{
	private boolean _isOpen = false;
	private boolean _megalithsCompleted = false;
	private int _level = 0;
	private int _currentTrust = 0;
	private int _tempTrust = 0;
	private int tempLevel = 0;
	private static final Logger _log = Logger.getLogger(HellboundManager.class.getName());

	private HellboundManager()
	{
		_log.info("Initializing HellboundManager");
		init();
	}

	private void init()
	{
		checkHellboundLevel();
		_log.info(getClass().getSimpleName()+": Current Level - "+_level);
		_log.info(getClass().getSimpleName()+": Current Trust - "+_currentTrust);
	}

	public static final HellboundManager getInstance()
	{
		return SingletonHolder._instance;
	}

	public boolean checkMegalithsCompleted()
	{
		return _megalithsCompleted;
	}

	public void setMegalithsCompleted(boolean value)
	{
		_megalithsCompleted = value;
	}

	public boolean checkIsOpen()
	{
		return _isOpen;
	}

	public int getLevel()
	{
		return _level;
	}

	public int getTrust()
	{
		return _currentTrust;
	}

	public L2Spawn addSpawn(int mobId, int xx, int yy, int zz, int headg, int respTime)
	{
		L2NpcTemplate template1;
		template1 = NpcTable.getInstance().getTemplate(mobId);
		L2Spawn spawn = null;
		try
		{
			spawn = new L2Spawn(template1);
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		spawn.setLocx(xx);
		spawn.setLocy(yy);
		spawn.setLocz(zz);
		spawn.setAmount(1);
		spawn.setHeading(headg);
		spawn.setRespawnDelay(respTime);
		spawn.setInstanceId(0);
		spawn.setOnKillDelay(0);
		SpawnTable.getInstance().addNewSpawn(spawn, false);
		spawn.init();
		spawn.startRespawn();
		if (respTime == 0)
			spawn.stopRespawn();
		return spawn;
	}

	public void increaseTrust(int points)
	{
		_currentTrust += points;
		updateTrust(_currentTrust);
	}

	public void updateTrust(int newTrust)
	{
		_currentTrust = newTrust;
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE hellbound SET trustLevel=? WHERE name=8000");
			statement.setInt(1, _currentTrust);
			statement.execute();
			statement.close();
		}
		catch (SQLException e)
		{
			_log.warning("HellboundManager: Could not save Hellbound trust points");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void changeLevel(int newLevel)
	{
		_level = newLevel;
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE hellbound SET zonesLevel=? WHERE name=8000");
			statement.setInt(1, _level);
			statement.execute();
			statement.close();
		}
		catch (SQLException e)
		{
			_log.warning("HellboundManager: Could not save Hellbound level");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void checkHellboundLevel()
	{
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT trustLevel, zonesLevel FROM hellbound");
			ResultSet rset = statement.executeQuery();
			while (rset.next())
			{
				// Read all info from DB, and store it for AI to read and decide what to do
				// faster than accessing DB in real time
				_tempTrust = rset.getInt("trustLevel");
				tempLevel = rset.getInt("zonesLevel");
				// TODO: Fix it properly
				_currentTrust = rset.getInt("trustLevel");
				if (_tempTrust >= 0 && _currentTrust < 300000)
					_level = 1;
				if (_tempTrust >= 300000 && _currentTrust < 600000)
					_level = 2;
				if (_tempTrust >= 600000 && _currentTrust < 1000000)
					_level = 3;
				if (tempLevel == 4)
					_level = 4;
				if (tempLevel == 5)
					_level = 5;
				if (tempLevel == 6)
					_level = 6;
				if (tempLevel == 7)
					_level = 7;
				if (tempLevel == 8)
					_level = 8;
				if (tempLevel == 9)
					_level = 9;
				if (tempLevel == 10)
					_level = 10;
				if (_tempTrust >= 2000000 && tempLevel >= 10)
					_level = 11;
			}
			rset.close();
			statement.close();
		}
		catch (SQLException e)
		{
			_log.warning("HellboundManager: Could not load the hellbound table");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if (tempLevel != _level && tempLevel != 5)
			changeLevel(_level);
	}

	/**
	 * Unlock hellbound zone
	 */
	public void unlock()
	{
		if (!checkIsOpen())
			changeLevel(1);
	}



	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final HellboundManager _instance = new HellboundManager();
	}
}