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

/*
# Update by U3Games 17-03-2011 "Swg"
# Special thanks to contributors users l2jserver
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
    private static final String[] VOICED_COMMANDS = { "trust", "hellbound" };    
    @Override
    public boolean useVoicedCommand(final String command, final L2PcInstance activeChar, final String target)
    {
        if (command.startsWith("trust") || command.equalsIgnoreCase("hellbound"))
        {
            final boolean isOpen = HellboundManager.getInstance().isLocked(); //adapted by FranpiscO, thx!
            final int hellboundLevel = HellboundManager.getInstance().getLevel();
            final int hellboundTrust = HellboundManager.getInstance().getTrust();
            activeChar.sendMessage("- Hellbound Informations -");
            if (isOpen)
                activeChar.sendMessage("Status: Unlocked");
            else
                activeChar.sendMessage("Status: Locked");
            activeChar.sendMessage("Current Trust: " + hellboundTrust);
            activeChar.sendMessage("Current Level: " + hellboundLevel);
        }
        return true;
    }
    @Override
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}