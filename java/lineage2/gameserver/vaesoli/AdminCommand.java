package lineage2.gameserver.vaesoli;

import lineage2.gameserver.handlers.IAdminCommandHandler;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcSay;
import lineage2.gameserver.network.serverpackets.Say2;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.ChatType;

public class AdminCommand implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_says"
	};
	
	/**
	 * Method useAdminCommand.
	 * @param command String
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean
	 */
	@Override
	public boolean useAdminCommand(String command, String[] wordList, String fullString, Player activeChar)
	{
		if (!activeChar.getPlayerAccess().CanEditNPC)
		{
			return false;
		}
		switch (command)
		{
			case "admin_says":
				GameObject obj = activeChar.getTarget();
				if ((obj != null) && obj.isNpc())
				{
					NpcInstance target = (NpcInstance) obj;
					if (fullString.length() > 11)
					{
						String phrase = fullString.substring(11);
						target.broadcastPacket(new NpcSay(target, ChatType.NPC_SAY, phrase));
						target.broadcastPacket(new Say2(target.getNpcId(), ChatType.ALL, target.getName(), phrase));
					}
					else
					{
						activeChar.sendMessage("USAGE: //says <texte roleplay>");
					}
				}
				else
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
				}
				break;
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandList.
	 * @return String
	 */
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
