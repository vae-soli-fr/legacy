/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.instancemanager.HellboundManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author evill33t
 */
public class HellboundVoiced implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS = { "hellbound" };

	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.startsWith("hellbound") || command.equalsIgnoreCase("hellbound"))
		{
			HellboundManager.getInstance().checkHellboundLevel();
			boolean _isOpen = HellboundManager.getInstance().checkIsOpen();
			int hellboundLevel = HellboundManager.getInstance().getLevel();
			int hellboundTrust = HellboundManager.getInstance().getTrust();
			activeChar.sendMessage("- Hellbound Informations -");
			if (_isOpen == true)
				activeChar.sendMessage("Status: Unlocked");
			if (_isOpen == false)
				activeChar.sendMessage("Status: Locked");
			activeChar.sendMessage("Current Trust: " + hellboundTrust);
			activeChar.sendMessage("Current Level: " + hellboundLevel);
		}
		return true;
	}

	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}

	public static void main(String[] args)
	{
		new HellboundVoiced();
	}
}