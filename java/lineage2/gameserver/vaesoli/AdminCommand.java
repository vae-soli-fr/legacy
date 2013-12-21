package lineage2.gameserver.vaesoli;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcSay;
import lineage2.gameserver.network.serverpackets.Say2;
import lineage2.gameserver.network.serverpackets.components.ChatType;

public class AdminCommand implements IAdminCommandHandler
{
	private static enum Commands
	{
		/**
		 * Field admin_delete.
		 */
		admin_says,
	}
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar) {
		Commands command = (Commands) comm;
		if (!activeChar.getPlayerAccess().CanEditNPC)
		{
			return false;
		}
		switch (command) {
		case admin_says:
			GameObject obj = activeChar.getTarget();
			if ((obj != null) && obj.isNpc()) {
				NpcInstance target = (NpcInstance) obj;
				if (fullString.length() > 11) {
					String phrase = fullString.substring(11);
					target.broadcastPacket(new NpcSay(target, ChatType.NPC_SAY, phrase));
					target.broadcastPacket(new Say2(target.getNpcId(), ChatType.ALL, target.getName(), phrase));
				} else {
					activeChar.sendMessage("USAGE: //says <texte roleplay>");
				}
			} else {
				activeChar.sendPacket(Msg.INVALID_TARGET);
			}
			break;
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[] * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
}
