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
package handlers.voicedcommands;

import static lineage2.gameserver.model.Zone.ZoneType.NoRestart;
import static lineage2.gameserver.model.Zone.ZoneType.NoSummon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.handlers.IVoicedCommandHandler;
import lineage2.gameserver.handlers.VoicedCommandHandler;
import lineage2.gameserver.instancemanager.CoupleManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.actor.player.OnAnswerListener;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Couple;
import lineage2.gameserver.network.serverpackets.ConfirmDlg;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SetupGauge;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.skills.AbnormalEffect;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Wedding implements IVoicedCommandHandler, ScriptFile
{
	private static class CoupleAnswerListener implements OnAnswerListener
	{
		private final HardReference<Player> _playerRef1;
		private final HardReference<Player> _playerRef2;
		
		/**
		 * Constructor for CoupleAnswerListener.
		 * @param player1 Player
		 * @param player2 Player
		 */
		public CoupleAnswerListener(Player player1, Player player2)
		{
			_playerRef1 = player1.getRef();
			_playerRef2 = player2.getRef();
		}
		
		/**
		 * Method sayYes.
		 * @see lineage2.gameserver.listener.actor.player.OnAnswerListener#sayYes()
		 */
		@Override
		public void sayYes()
		{
			Player player1, player2;
			
			if (((player1 = _playerRef1.get()) == null) || ((player2 = _playerRef2.get()) == null))
			{
				return;
			}
			
			CoupleManager.getInstance().createCouple(player1, player2);
			player1.sendMessage("Engage accepted.");
			player2.sendMessage("Engage accepted.");
		}
		
		/**
		 * Method sayNo.
		 * @see lineage2.gameserver.listener.actor.player.OnAnswerListener#sayNo()
		 */
		@Override
		public void sayNo()
		{
			Player player1, player2;
			
			if (((player1 = _playerRef1.get()) == null) || ((player2 = _playerRef2.get()) == null))
			{
				return;
			}
			
			player1.sendMessage("Engage declined.");
			player2.sendMessage("Engage declined.");
		}
	}
	
	private static final Logger _log = LoggerFactory.getLogger(Wedding.class);
	private static final String[] _voicedCommands =
	{
		"divorce",
		"engage",
		"gotolove"
	};
	
	/**
	 * Method useVoicedCommand.
	 * @param command String
	 * @param activeChar Player
	 * @param target String
	 * @return boolean
	 * @see lineage2.gameserver.handlers.IVoicedCommandHandler#useVoicedCommand(String, Player, String)
	 */
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		if (!Config.ALLOW_WEDDING)
		{
			return false;
		}
		
		if (command.startsWith("engage"))
		{
			return engage(activeChar);
		}
		else if (command.startsWith("divorce"))
		{
			return divorce(activeChar);
		}
		else if (command.startsWith("gotolove"))
		{
			return goToLove(activeChar);
		}
		
		return false;
	}
	
	/**
	 * Method divorce.
	 * @param activeChar Player
	 * @return boolean
	 */
	private boolean divorce(Player activeChar)
	{
		if (activeChar.getPartnerId() == 0)
		{
			return false;
		}
		
		int _partnerId = activeChar.getPartnerId();
		long AdenaAmount = 0;
		
		if (activeChar.isMaried())
		{
			activeChar.sendMessage("You are divorced now.");
			AdenaAmount = Math.abs(((activeChar.getAdena() / 100) * Config.WEDDING_DIVORCE_COSTS) - 10);
			activeChar.reduceAdena(AdenaAmount, true);
		}
		else
		{
			activeChar.sendMessage("You are disengaged now.");
		}
		
		activeChar.setMaried(false);
		activeChar.setPartnerId(0);
		Couple couple = CoupleManager.getInstance().getCouple(activeChar.getCoupleId());
		couple.divorce();
		couple = null;
		Player partner = GameObjectsStorage.getPlayer(_partnerId);
		
		if (partner != null)
		{
			partner.setPartnerId(0);
			
			if (partner.isMaried())
			{
				partner.sendMessage("Your Partner has decided to divorce from you.");
			}
			else
			{
				partner.sendMessage("Your Partner has decided to disengage.");
			}
			
			partner.setMaried(false);
			
			if (AdenaAmount > 0)
			{
				partner.addAdena(AdenaAmount);
			}
		}
		
		return true;
	}
	
	/**
	 * Method engage.
	 * @param activeChar Player
	 * @return boolean
	 */
	private boolean engage(final Player activeChar)
	{
		if (activeChar.getTarget() == null)
		{
			activeChar.sendMessage("You have none targeted.");
			return false;
		}
		
		if (!activeChar.getTarget().isPlayer())
		{
			activeChar.sendMessage("You can only ask another Player for partnership.");
			return false;
		}
		
		if (activeChar.getPartnerId() != 0)
		{
			activeChar.sendMessage("You are already engaged.");
			
			if (Config.WEDDING_PUNISH_INFIDELITY)
			{
				activeChar.startAbnormalEffect(AbnormalEffect.BIG_HEAD);
				int skillId;
				int skillLevel = 1;
				
				if (activeChar.getLevel() > 40)
				{
					skillLevel = 2;
				}
				
				if (activeChar.isMageClass())
				{
					skillId = 4361;
				}
				else
				{
					skillId = 4362;
				}
				
				Skill skill = SkillTable.getInstance().getInfo(skillId, skillLevel);
				
				if (activeChar.getEffectList().getEffectsBySkill(skill) == null)
				{
					skill.getEffects(activeChar, activeChar, false, false);
					activeChar.sendPacket(new SystemMessage(SystemMessage.S1_S2S_EFFECT_CAN_BE_FELT).addSkillName(skillId, skillLevel));
				}
			}
			
			return false;
		}
		
		final Player ptarget = (Player) activeChar.getTarget();
		
		if (ptarget.getObjectId() == activeChar.getObjectId())
		{
			activeChar.sendMessage("Engaging with yourself?");
			return false;
		}
		
		if (ptarget.isMaried())
		{
			activeChar.sendMessage("Already married.");
			return false;
		}
		
		if (ptarget.getPartnerId() != 0)
		{
			activeChar.sendMessage("Is already engaged with someone else.");
			return false;
		}
		
		Pair<Integer, OnAnswerListener> entry = ptarget.getAskListener(false);
		
		if ((entry != null) && (entry.getValue() instanceof CoupleAnswerListener))
		{
			activeChar.sendMessage("Already asked by someone else.");
			return false;
		}
		
		if (ptarget.getPartnerId() != 0)
		{
			activeChar.sendMessage("Is already engaged with someone else.");
			return false;
		}
		
		if ((ptarget.getSex() == activeChar.getSex()) && !Config.WEDDING_SAMESEX)
		{
			activeChar.sendMessage("You can't ask partners of same sex.");
			return false;
		}
		
		boolean FoundOnFriendList = false;
		int objectId;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT friend_id FROM character_friends WHERE char_id=?");
			statement.setInt(1, ptarget.getObjectId());
			rset = statement.executeQuery();
			
			while (rset.next())
			{
				objectId = rset.getInt("friend_id");
				
				if (objectId == activeChar.getObjectId())
				{
					FoundOnFriendList = true;
					break;
				}
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		
		if (!FoundOnFriendList)
		{
			activeChar.sendMessage("The person you want to ask hasn't added you on the friendlist.");
			return false;
		}
		
		ConfirmDlg packet = new ConfirmDlg(SystemMsg.S1, 60000).addString("Player " + activeChar.getName() + " asking you to engage. Do you want to start new relationship?");
		ptarget.ask(packet, new CoupleAnswerListener(activeChar, ptarget));
		return true;
	}
	
	/**
	 * Method goToLove.
	 * @param activeChar Player
	 * @return boolean
	 */
	private boolean goToLove(Player activeChar)
	{
		if (!activeChar.isMaried())
		{
			activeChar.sendMessage("You are not married.");
			return false;
		}
		
		if (activeChar.getPartnerId() == 0)
		{
			activeChar.sendMessage("Couldn't find your Partner in Database - Inform a Gamemaster.");
			return false;
		}
		
		Player partner = GameObjectsStorage.getPlayer(activeChar.getPartnerId());
		
		if (partner == null)
		{
			activeChar.sendMessage("Your Partner is not online.");
			return false;
		}
		
		if (partner.isInOlympiadMode() || activeChar.isMovementDisabled() || activeChar.isMuted(null) || activeChar.isInOlympiadMode() || activeChar.isInDuel() || activeChar.getPlayer().isTerritoryFlagEquipped() || partner.isInZone(NoSummon))
		{
			activeChar.sendMessage("Try later.");
			return false;
		}
		
		if ((activeChar.getTeleMode() != 0) || (activeChar.getReflection() != ReflectionManager.DEFAULT))
		{
			activeChar.sendMessage("Try later.");
			return false;
		}
		
		if (partner.isInZoneBattle() || partner.isInZone(Zone.ZoneType.Siege) || partner.isInZone(NoRestart) || partner.isInOlympiadMode() || activeChar.isInZoneBattle() || activeChar.isInZone(Zone.ZoneType.Siege) || activeChar.isInZone(NoRestart) || activeChar.isInOlympiadMode() || (partner.getReflection() != ReflectionManager.DEFAULT) || partner.isInZone(NoSummon) || activeChar.isInObserverMode() || partner.isInObserverMode())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.YOUR_TARGET_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING));
			return false;
		}
		
		if (!activeChar.reduceAdena(Config.WEDDING_TELEPORT_PRICE, true))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_DO_NOT_HAVE_ENOUGH_ADENA));
			return false;
		}
		
		int teleportTimer = Config.WEDDING_TELEPORT_INTERVAL;
		activeChar.abortAttack(true, true);
		activeChar.abortCast(true, true);
		activeChar.sendActionFailed();
		activeChar.stopMove();
		activeChar.startParalyzed();
		activeChar.sendMessage("After " + (teleportTimer / 60) + " min. you will be teleported to your Love.");
		activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, 1050, 1, teleportTimer, 0));
		activeChar.sendPacket(new SetupGauge(activeChar, SetupGauge.BLUE_DUAL, teleportTimer));
		ThreadPoolManager.getInstance().schedule(new EscapeFinalizer(activeChar, partner.getLoc()), teleportTimer * 1000L);
		return true;
	}
	
	private static class EscapeFinalizer extends RunnableImpl
	{
		private final Player _activeChar;
		private final Location _loc;
		
		/**
		 * Constructor for EscapeFinalizer.
		 * @param activeChar Player
		 * @param loc Location
		 */
		EscapeFinalizer(Player activeChar, Location loc)
		{
			_activeChar = activeChar;
			_loc = loc;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_activeChar.stopParalyzed();
			
			if (_activeChar.isDead())
			{
				return;
			}
			
			_activeChar.teleToLocation(_loc);
		}
	}
	
	/**
	 * Method getVoicedCommandList.
	 * @return String[]
	 * @see lineage2.gameserver.handlers.IVoicedCommandHandler#getVoicedCommandList()
	 */
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
}
