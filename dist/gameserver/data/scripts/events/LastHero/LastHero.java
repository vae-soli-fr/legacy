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
package events.LastHero;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerExitListener;
import lineage2.gameserver.listener.actor.player.OnTeleportListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.impl.DuelEvent;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.network.serverpackets.Revive;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.skills.AbnormalEffect;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.DoorTemplate;
import lineage2.gameserver.templates.ZoneTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;
import lineage2.gameserver.utils.ReflectionUtils;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LastHero extends Functions implements ScriptFile, OnDeathListener, OnTeleportListener, OnPlayerExitListener
{
	private static final Logger _log = LoggerFactory.getLogger(LastHero.class);
	
	private static final int[] doors = new int[]
	{
		24190001,
		24190002,
		24190003,
		24190004
	};
	
	private static ScheduledFuture<?> _startTask;
	
	private static List<Long> players_list = new CopyOnWriteArrayList<>();
	static List<Long> live_list = new CopyOnWriteArrayList<>();
	private static int[][] mage_buffs = new int[Config.EVENT_LHMageBuffs.length][2];
	private static int[][] fighter_buffs = new int[Config.EVENT_LHFighterBuffs.length][2];
	
	private static Map<Long, Location> playerRestoreCoord = new LinkedHashMap<>();
	
	private static Map<Long, String> boxes = new LinkedHashMap<>();
	private static boolean _isRegistrationActive = false;
	static int _status = 0;
	private static int _time_to_start;
	private static int _category;
	private static int _minLevel;
	private static int _maxLevel;
	private static int _autoContinue = 0;
	static boolean _active = false;
	private static Skill buff;
	private static ScheduledFuture<?> _endTask;
	
	static Reflection reflection = ReflectionManager.LAST_HERO;
	private static Map<String, ZoneTemplate> _zones = new HashMap<>();
	private static IntObjectMap<DoorTemplate> _doors = new HashIntObjectMap<>();
	
	private static Zone _zone;
	private static ZoneListener _zoneListener = new ZoneListener();
	
	private static final Location _enter = new Location(149505, 46719, -3417);
	
	private static boolean isActive()
	{
		return _active;
	}
	
	public void activateEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		
		if (!isActive())
		{
			if (_startTask == null)
			{
				scheduleEventStart();
			}
			ServerVariables.set("LastHero", "on");
			_log.info("Event 'Last Hero' activated.");
			Announcements.getInstance().announceToAll("Event 'Last Hero' activated.");
		}
		else
		{
			player.sendMessage("Event 'Last Hero' already active.");
		}
		
		_active = true;
		
		show("admin/events.htm", player);
	}
	
	public void teleportPlayers()
	{
		for (Player player : getPlayers(players_list))
		{
			if ((player == null) || !playerRestoreCoord.containsKey(player.getStoredId()))
			{
				continue;
			}
			player.teleToLocation(playerRestoreCoord.get(player.getStoredId()), ReflectionManager.DEFAULT);
			player.setRegisteredInEvent(false);
		}
		playerRestoreCoord.clear();
	}
	
	public void deactivateEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		
		if (isActive())
		{
			if (_startTask != null)
			{
				_startTask.cancel(false);
				_startTask = null;
			}
			ServerVariables.unset("LastHero");
			_log.info("Event 'Last Hero' deactivated.");
			Announcements.getInstance().announceToAll("Event 'Last Hero' deactivated.");
		}
		else
		{
			player.sendMessage("Event 'LastHero' not active.");
		}
		
		_active = false;
		
		show("admin/events.htm", player);
	}
	
	public static boolean isRunned()
	{
		return _isRegistrationActive || (_status > 0);
	}
	
	public static int getMinLevelForCategory(int category)
	{
		switch (category)
		{
			case 1:
				return 20;
			case 2:
				return 30;
			case 3:
				return 40;
			case 4:
				return 52;
			case 5:
				return 62;
			case 6:
				return 76;
		}
		return 0;
	}
	
	public static int getMaxLevelForCategory(int category)
	{
		switch (category)
		{
			case 1:
				return 29;
			case 2:
				return 39;
			case 3:
				return 51;
			case 4:
				return 61;
			case 5:
				return 75;
			case 6:
				return 99;
		}
		return 0;
	}
	
	public static int getCategory(int level)
	{
		if ((level >= 20) && (level <= 29))
		{
			return 1;
		}
		else if ((level >= 30) && (level <= 39))
		{
			return 2;
		}
		else if ((level >= 40) && (level <= 51))
		{
			return 3;
		}
		else if ((level >= 52) && (level <= 61))
		{
			return 4;
		}
		else if ((level >= 62) && (level <= 75))
		{
			return 5;
		}
		else if (level >= 76)
		{
			return 6;
		}
		return 0;
	}
	
	public void start(String[] var)
	{
		Player player = getSelf();
		if (var.length != 2)
		{
			show("Error.", player);
			return;
		}
		Integer category;
		Integer autoContinue;
		try
		{
			category = Integer.valueOf(var[0]);
			autoContinue = Integer.valueOf(var[1]);
		}
		catch (Exception e)
		{
			show("Error.", player);
			return;
		}
		
		_category = category;
		_autoContinue = autoContinue;
		
		if (_category == -1)
		{
			_minLevel = 1;
			_maxLevel = 99;
		}
		else
		{
			_minLevel = getMinLevelForCategory(_category);
			_maxLevel = getMaxLevelForCategory(_category);
		}
		
		if (_endTask != null)
		{
			show("Try later.", player);
			return;
		}
		
		_status = 0;
		_isRegistrationActive = true;
		_time_to_start = Config.EVENT_LHTime;
		
		players_list = new CopyOnWriteArrayList<>();
		live_list = new CopyOnWriteArrayList<>();
		playerRestoreCoord = new LinkedHashMap<>();
		
		sayToAll("Last Hero: Start in " + String.valueOf(_time_to_start) + " min. for levels " + String.valueOf(_minLevel) + "-" + String.valueOf(_maxLevel) + ". Information in the community board (alt+b).");
		
		executeTask("events.LastHero.LastHero", "question", new Object[0], 10000);
		executeTask("events.LastHero.LastHero", "announce", new Object[0], 60000);
	}
	
	public static void sayToAll(String address)
	{
		Announcements.getInstance().announceToAll(address, ChatType.CRITICAL_ANNOUNCE);
	}
	
	public static void question()
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if ((player != null) && !player.isDead() && (player.getLevel() >= _minLevel) && (player.getLevel() <= _maxLevel) && player.getReflection().isDefault() && !player.isInOlympiadMode() && !player.isInObserverMode())
			{
				player.scriptRequest("Do you want to participate in event 'Last Hero'?", "events.LastHero.LastHero:addPlayer", new Object[0]);
			}
		}
	}
	
	public static void announce()
	{
		if (players_list.size() < 2)
		{
			sayToAll("Last Hero: Event cancelled, not enough players.");
			_isRegistrationActive = false;
			_status = 0;
			executeTask("events.LastHero.LastHero", "autoContinue", new Object[0], 10000);
			return;
		}
		
		if (_time_to_start > 1)
		{
			_time_to_start--;
			sayToAll("Last Hero: Start in " + String.valueOf(_time_to_start) + " min. for levels " + String.valueOf(_minLevel) + "-" + String.valueOf(_maxLevel) + ". Information in the community board (alt+b).");
			executeTask("events.LastHero.LastHero", "announce", new Object[0], 60000);
		}
		else
		{
			_status = 1;
			_isRegistrationActive = false;
			sayToAll("Last Hero: Registration ended, teleporting players...");
			executeTask("events.LastHero.LastHero", "prepare", new Object[0], 5000);
		}
	}
	
	public void addPlayer()
	{
		Player player = getSelf();
		if ((player == null) || !checkPlayer(player, true) || !checkDualBox(player))
		{
			return;
		}
		
		players_list.add(player.getStoredId());
		live_list.add(player.getStoredId());
		
		show("You have been registered in the Last Hero event. Please, do not register in other events and avoid duels until countdown end.", player);
		player.setRegisteredInEvent(true);
	}
	
	public static boolean checkPlayer(Player player, boolean first)
	{
		if (first && (!_isRegistrationActive || player.isDead()))
		{
			show("Event is already running, registration closed.", player);
			return false;
		}
		
		if (first && players_list.contains(player.getStoredId()))
		{
			player.setRegisteredInEvent(false);
			show("Registration cancelled.", player);
			if (players_list.contains(player.getStoredId()))
			{
				players_list.remove(player.getStoredId());
			}
			if (live_list.contains(player.getStoredId()))
			{
				live_list.remove(player.getStoredId());
			}
			if (boxes.containsKey(player.getStoredId()))
			{
				boxes.remove(player.getStoredId());
			}
			return false;
		}
		
		if ((player.getLevel() < _minLevel) || (player.getLevel() > _maxLevel))
		{
			show("Registration cancelled. Inconsistent level.", player);
			return false;
		}
		
		if (player.isMounted())
		{
			show("Registration cancelled.", player);
			return false;
		}
		
		if (player.isCursedWeaponEquipped())
		{
			show("Registration cancelled.", player);
			return false;
		}
		
		if (player.isInDuel())
		{
			show("Registration cancelled. You can't participate while in a duel.", player);
			return false;
		}
		
		if (player.getTeam() != TeamType.NONE)
		{
			show("Registration cancelled. You are already participating other event.", player);
			return false;
		}
		
		if ((player.getOlympiadGame() != null) || (first && Olympiad.isRegistered(player)))
		{
			show("Registration cancelled. You are in the olympiad zone.", player);
			return false;
		}
		
		if (player.isTeleporting())
		{
			show("Registration cancelled. You are teleporting.", player);
			return false;
		}
		
		if (player.isInObserverMode())
		{
			show("Registration cancelled. You are in observer mode.", player);
			return false;
		}
		if (!Config.ALLOW_HEROES_LASTHERO && player.isHero())
		{
			show("Registration cancelled. Heroes are now allowed.", player);
			return false;
		}
		
		return true;
	}
	
	public static void prepare()
	{
		
		for (DoorInstance door : reflection.getDoors())
		{
			door.openMe();
		}
		
		for (Zone z : reflection.getZones())
		{
			z.setType(ZoneType.Peace);
		}
		
		cleanPlayers();
		clearArena();
		
		executeTask("events.LastHero.LastHero", "ressurectPlayers", new Object[0], 1000);
		executeTask("events.LastHero.LastHero", "healPlayers", new Object[0], 2000);
		executeTask("events.LastHero.LastHero", "paralyzePlayers", new Object[0], 4000);
		executeTask("events.LastHero.LastHero", "teleportPlayersToColiseum", new Object[0], 3000);
		executeTask("events.LastHero.LastHero", "buffPlayers", new Object[0], 5000);
		executeTask("events.LastHero.LastHero", "go", new Object[0], 60000);
		
		sayToAll("Last Hero: 1 minute to start.");
	}
	
	public static void go()
	{
		_status = 2;
		upParalyzePlayers();
		checkLive();
		clearArena();
		sayToAll("Last Hero: >>> FIGHT!!! <<<");
		for (Zone z : reflection.getZones())
		{
			z.setType(ZoneType.Battle);
		}
		_endTask = executeTask("events.LastHero.LastHero", "endBattle", new Object[0], 300000);
	}
	
	public static void endBattle()
	{
		_status = 0;
		removeAura();
		
		for (Zone z : reflection.getZones())
		{
			z.setType(ZoneType.Peace);
		}
		boxes.clear();
		if (live_list.size() == 1)
		{
			for (Player player : getPlayers(live_list))
			{
				sayToAll("Last Hero: " + player.getName() + " wins.");
				addItem(player, Config.EVENT_LastHeroItemID, Math.round(Config.EVENT_LastHeroRateFinal ? player.getLevel() * Config.EVENT_LastHeroItemCOUNTFinal : 1 * Config.EVENT_LastHeroItemCOUNTFinal));
				player.setHero(true);
				break;
			}
		}
		sayToAll("Last Hero: Event ended. 30 sec countdown before teleporting players back.");
		executeTask("events.LastHero.LastHero", "end", new Object[0], 30000);
		_isRegistrationActive = false;
		if (_endTask != null)
		{
			_endTask.cancel(false);
			_endTask = null;
		}
	}
	
	public static void end()
	{
		executeTask("events.LastHero.LastHero", "ressurectPlayers", new Object[0], 1000);
		executeTask("events.LastHero.LastHero", "healPlayers", new Object[0], 2000);
		executeTask("events.LastHero.LastHero", "teleportPlayers", new Object[0], 3000);
		executeTask("events.LastHero.LastHero", "autoContinue", new Object[0], 10000);
	}
	
	public void autoContinue()
	{
		if (_autoContinue > 0)
		{
			if (_autoContinue >= 6)
			{
				_autoContinue = 0;
				return;
			}
			start(new String[]
			{
				"" + (_autoContinue + 1),
				"" + (_autoContinue + 1)
			});
		}
		else
		{
			scheduleEventStart();
		}
	}
	
	public static void teleportPlayersToColiseum()
	{
		for (Player player : getPlayers(players_list))
		{
			
			unRide(player);
			if (!Config.EVENT_LHAllowSummons)
			{
				unSummonPet(player, true);
			}
			
			DuelEvent duel = player.getEvent(DuelEvent.class);
			if (duel != null)
			{
				duel.abortDuel(player);
			}
			
			playerRestoreCoord.put(player.getStoredId(), new Location(player.getX(), player.getY(), player.getZ()));
			player.teleToLocation(Location.findPointToStay(_enter, 150, 500, ReflectionManager.DEFAULT.getGeoIndex()), reflection);
			player.setIsInLastHero(true);
			if (!Config.EVENT_LHAllowBuffs)
			{
				player.getEffectList().stopAllEffects();
			}
		}
	}
	
	public static void paralyzePlayers()
	{
		for (Player player : getPlayers(players_list))
		{
			if (player == null)
			{
				continue;
			}
			
			if (!player.isRooted())
			{
				player.startRooted();
				player.startAbnormalEffect(AbnormalEffect.ROOT);
			}
		}
	}
	
	public static void upParalyzePlayers()
	{
		for (Player player : getPlayers(players_list))
		{
			if (player.isRooted())
			{
				player.stopRooted();
				player.stopAbnormalEffect(AbnormalEffect.ROOT);
			}
		}
	}
	
	public static void ressurectPlayers()
	{
		for (Player player : getPlayers(players_list))
		{
			if (player.isDead())
			{
				player.restoreExp();
				player.setCurrentCp(player.getMaxCp());
				player.setCurrentHp(player.getMaxHp(), true);
				player.setCurrentMp(player.getMaxMp());
				player.broadcastPacket(new Revive(player));
			}
		}
	}
	
	public static void healPlayers()
	{
		for (Player player : getPlayers(players_list))
		{
			player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
			player.setCurrentCp(player.getMaxCp());
		}
	}
	
	public static void cleanPlayers()
	{
		for (Player player : getPlayers(players_list))
		{
			if (!checkPlayer(player, false))
			{
				removePlayer(player);
			}
		}
	}
	
	public static void checkLive()
	{
		List<Long> new_live_list = new CopyOnWriteArrayList<>();
		
		for (Long storeId : live_list)
		{
			Player player = GameObjectsStorage.getAsPlayer(storeId);
			if (player != null)
			{
				new_live_list.add(storeId);
			}
		}
		
		live_list = new_live_list;
		
		for (Player player : getPlayers(live_list))
		{
			if (player.isInZone(_zone) && !player.isDead() && !player.isLogoutStarted())
			{
				player.setTeam(TeamType.RED);
			}
			else
			{
				loosePlayer(player);
			}
		}
		
		if (live_list.size() <= 1)
		{
			endBattle();
		}
	}
	
	public static void removeAura()
	{
		for (Player player : getPlayers(live_list))
		{
			player.setTeam(TeamType.NONE);
			player.setIsInLastHero(false);
		}
	}
	
	public static void clearArena()
	{
		for (GameObject obj : _zone.getObjects())
		{
			if (obj != null)
			{
				Player player = obj.getPlayer();
				if ((player != null) && !live_list.contains(player.getStoredId()))
				{
					player.teleToLocation(147451, 46728, -3410, ReflectionManager.DEFAULT);
				}
			}
		}
	}
	
	@Override
	public void onDeath(Creature self, Creature killer)
	{
		if ((_status > 1) && self.isPlayer() && (self.getTeam() != TeamType.NONE) && live_list.contains(self.getStoredId()))
		{
			Player player = (Player) self;
			loosePlayer(player);
			checkLive();
			if ((killer != null) && killer.isPlayer() && ((killer.getPlayer().expertiseIndex - player.expertiseIndex) > 2) && !killer.getPlayer().getIP().equals(player.getIP()))
			{
				addItem((Player) killer, Config.EVENT_LastHeroItemID, Math.round(Config.EVENT_LastHeroRate ? player.getLevel() * Config.EVENT_LastHeroItemCOUNT : 1 * Config.EVENT_LastHeroItemCOUNT));
			}
			self.getPlayer().setIsInLastHero(false);
		}
	}
	
	@Override
	public void onTeleport(Player player, int x, int y, int z, Reflection reflection)
	{
		if (_zone.checkIfInZone(x, y, z, reflection))
		{
			return;
		}
		
		if ((_status > 1) && (player.getTeam() != TeamType.NONE) && live_list.contains(player.getStoredId()))
		{
			removePlayer(player);
			checkLive();
		}
	}
	
	@Override
	public void onPlayerExit(Player player)
	{
		if (player.getTeam() == TeamType.NONE)
		{
			return;
		}
		
		if ((_status == 0) && _isRegistrationActive && live_list.contains(player.getStoredId()))
		{
			removePlayer(player);
			return;
		}
		
		if ((_status == 1) && live_list.contains(player.getStoredId()))
		{
			player.teleToLocation(playerRestoreCoord.get(player.getStoredId()), ReflectionManager.DEFAULT);
			removePlayer(player);
			
			return;
		}
		
		if ((_status > 1) && (player.getTeam() != TeamType.NONE) && live_list.contains(player.getStoredId()))
		{
			removePlayer(player);
			checkLive();
		}
	}
	
	private static class ZoneListener implements OnZoneEnterLeaveListener
	{
		public ZoneListener()
		{
		}
		
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (cha == null)
			{
				return;
			}
			Player player = cha.getPlayer();
			if ((_status > 0) && (player != null) && !live_list.contains(player.getStoredId()))
			{
				player.teleToLocation(147451, 46728, -3410, ReflectionManager.DEFAULT);
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
			if (cha == null)
			{
				return;
			}
			Player player = cha.getPlayer();
			if ((_status > 1) && (player != null) && (player.getTeam() != TeamType.NONE) && live_list.contains(player.getStoredId()))
			{
				double angle = PositionUtils.convertHeadingToDegree(cha.getHeading()); // ???? ? ????????
				double radian = Math.toRadians(angle - 90); // ???? ? ????????
				int x = (int) (cha.getX() + (250 * Math.sin(radian)));
				int y = (int) (cha.getY() - (250 * Math.cos(radian)));
				int z = cha.getZ();
				player.teleToLocation(x, y, z, reflection);
			}
		}
	}
	
	private static void loosePlayer(Player player)
	{
		if (player != null)
		{
			live_list.remove(player.getStoredId());
			player.setTeam(TeamType.NONE);
			show("You lose! Please wait event end.", player);
		}
	}
	
	private static void removePlayer(Player player)
	{
		if (player != null)
		{
			live_list.remove(player.getStoredId());
			players_list.remove(player.getStoredId());
			playerRestoreCoord.remove(player.getStoredId());
			player.setIsInLastHero(false);
			
			if (!Config.EVENT_LHAllowMultiReg)
			{
				boxes.remove(player.getStoredId());
			}
			player.setTeam(TeamType.NONE);
		}
	}
	
	private static List<Player> getPlayers(List<Long> list)
	{
		List<Player> result = new ArrayList<>(list.size());
		for (Long storeId : list)
		{
			Player player = GameObjectsStorage.getAsPlayer(storeId);
			if (player != null)
			{
				result.add(player);
			}
		}
		return result;
	}
	
	public static void buffPlayers()
	{
		
		for (Player player : getPlayers(players_list))
		{
			if (player.isMageClass())
			{
				mageBuff(player);
			}
			else
			{
				fighterBuff(player);
			}
		}
		
		for (Player player : getPlayers(live_list))
		{
			if (player.isMageClass())
			{
				mageBuff(player);
			}
			else
			{
				fighterBuff(player);
			}
		}
	}
	
	public void scheduleEventStart()
	{
		try
		{
			Calendar currentTime = Calendar.getInstance();
			Calendar nextStartTime = null;
			Calendar testStartTime = null;
			
			for (String timeOfDay : Config.EVENT_LHStartTime)
			{
				testStartTime = Calendar.getInstance();
				testStartTime.setLenient(true);
				
				String[] splitTimeOfDay = timeOfDay.split(":");
				
				testStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTimeOfDay[0]));
				testStartTime.set(Calendar.MINUTE, Integer.parseInt(splitTimeOfDay[1]));
				
				if (testStartTime.getTimeInMillis() < currentTime.getTimeInMillis())
				{
					testStartTime.add(Calendar.DAY_OF_MONTH, 1);
				}
				
				if ((nextStartTime == null) || (testStartTime.getTimeInMillis() < nextStartTime.getTimeInMillis()))
				{
					nextStartTime = testStartTime;
				}
				
				if (_startTask != null)
				{
					_startTask.cancel(false);
					_startTask = null;
				}
				_startTask = ThreadPoolManager.getInstance().schedule(new StartTask(), nextStartTime.getTimeInMillis() - System.currentTimeMillis());
				
			}
			
			currentTime = null;
			nextStartTime = null;
			testStartTime = null;
			
		}
		catch (Exception e)
		{
			_log.warn("LH: Error figuring out a start time. Check TvTEventInterval in config file.");
		}
	}
	
	public static void mageBuff(Player player)
	{
		for (int[] mage_buff : mage_buffs)
		{
			buff = SkillTable.getInstance().getInfo(mage_buff[0], mage_buff[1]);
			/*
			 * for(EffectTemplate et : buff.getEffectTemplates()) { Env env = new Env(player, player, buff); Effect effect = et.getEffect(env); effect.setPeriod(1200000); //20 ????? player. getEffectList ().addEffect(effect); }
			 */
			if ((player != null) && (buff != null))
			{
				buff.getEffects(player, player, false, false);
			}
		}
	}
	
	public static void fighterBuff(Player player)
	{
		for (int[] fighter_buff : fighter_buffs)
		{
			buff = SkillTable.getInstance().getInfo(fighter_buff[0], fighter_buff[1]);
			/*
			 * for(EffectTemplate et : buff.getEffectTemplates()) { Env env = new Env(player, player, buff); Effect effect = et.getEffect(env); effect.setPeriod(1200000); //20 ????? player. getEffectList ().addEffect(effect); }
			 */
			if ((player != null) && (buff != null))
			{
				buff.getEffects(player, player, false, false);
			}
		}
	}
	
	private static boolean checkDualBox(Player player)
	{
		if (!Config.EVENT_LHAllowMultiReg)
		{
			if ("IP".equals(Config.EVENT_LHCheckWindowMethod))
			{
				if (boxes.containsValue(player.getIP()))
				{
					show("Multibox is not allowed.", player);
					return false;
				}
			}
			
			// else if ("HWid".equals(Config.EVENT_LHCheckWindowMethod)) {
			// if (boxes.containsValue(player.getNetConnection().getHWID())) {
			// show(new CustomMessage("scripts.events.TvT.CancelledBox",
			// player), player);
			// return false;
			// }
			// }
		}
		return true;
	}
	
	public class StartTask extends RunnableImpl
	{
		
		@Override
		public void runImpl()
		{
			if (!_active)
			{
				return;
			}
			
			if (isPvPEventStarted())
			{
				_log.info("LH not started: another event is already running");
				return;
			}
			
			for (Residence c : ResidenceHolder.getInstance().getResidenceList(Castle.class))
			{
				if ((c.getSiegeEvent() != null) && c.getSiegeEvent().isInProgress())
				{
					_log.debug("LH not started: CastleSiege in progress");
					return;
				}
			}
			
			/*
			 * if(TerritorySiege.isInProgress()) { _log.debug("TvT not started: TerritorySiege in progress"); return; }
			 */
			
			if (Config.EVENT_LHCategories)
			{
				start(new String[]
				{
					"1",
					"1"
				});
			}
			else
			{
				start(new String[]
				{
					"-1",
					"-1"
				});
			}
		}
	}
	
	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
		
		_zones.put("[colosseum_battle]", ReflectionUtils.getZone("[colosseum_battle]").getTemplate());
		for (final int doorId : doors)
		{
			_doors.put(doorId, ReflectionUtils.getDoor(doorId).getTemplate());
		}
		reflection.init(_doors, _zones);
		_zone = reflection.getZone("[colosseum_battle]");
		_zone.addListener(_zoneListener);
		
		_active = ServerVariables.getString("LastHero", "off").equals("on");
		
		if (isActive())
		{
			scheduleEventStart();
		}
		
		int i = 0;
		
		if (Config.EVENT_LHBuffPlayers && (Config.EVENT_LHMageBuffs.length != 0))
		{
			for (String skill : Config.EVENT_LHMageBuffs)
			{
				String[] splitSkill = skill.split(",");
				mage_buffs[i][0] = Integer.parseInt(splitSkill[0]);
				mage_buffs[i][1] = Integer.parseInt(splitSkill[1]);
				i++;
			}
		}
		
		i = 0;
		
		if (Config.EVENT_LHBuffPlayers && (Config.EVENT_LHFighterBuffs.length != 0))
		{
			for (String skill : Config.EVENT_LHFighterBuffs)
			{
				String[] splitSkill = skill.split(",");
				fighter_buffs[i][0] = Integer.parseInt(splitSkill[0]);
				fighter_buffs[i][1] = Integer.parseInt(splitSkill[1]);
				i++;
			}
		}
		
		_log.info("Loaded Event: Last Hero");
	}
	
	@Override
	public void onReload()
	{
		_zone.removeListener(_zoneListener);
		if (_startTask != null)
		{
			_startTask.cancel(false);
			_startTask = null;
		}
	}
	
	@Override
	public void onShutdown()
	{
		onReload();
	}
}