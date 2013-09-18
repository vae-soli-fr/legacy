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
package handlers.chathandlers;

import java.util.Collection;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IChatHandler;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.handler.VoicedCommandHandler;
import com.l2jserver.gameserver.model.BlockList;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.skills.Stats;
import com.l2jserver.gameserver.util.Util;


/**
 * A chat handler
 *
 * @author  durgus
 */
public class ChatAll implements IChatHandler
{
	private static final int[] COMMAND_IDS =
	{
		0
	};
	
	private static Logger _log = Logger.getLogger(ChatAll.class.getName());
	
	private static final Pattern THREE_LETTER_WORD_PATTERN = Pattern.compile("[A-ZÀ-ÿa-z]{3,}");
	
	private static final Pattern EX_ITEM_LINK_PATTERN = Pattern.compile("[\b]\tType=[0-9]+[\\s]+\tID=([0-9]+)[\\s]+\tColor=[0-9]+[\\s]+\tUnderline=[0-9]+[\\s]+\tTitle=\u001B(.[^\u001B]*)[^\b]");
	
	/**
	 * Handle chat type 'all'
	 * @see com.l2jserver.gameserver.handler.IChatHandler#handleChat(int, com.l2jserver.gameserver.model.actor.instance.L2PcInstance, java.lang.String, java.lang.String)
	 */
	@Override
	public void handleChat(int type, L2PcInstance activeChar, String params, String text)
	{
		boolean vcd_used= false;

		if (text.startsWith("."))
		{
			StringTokenizer st = new StringTokenizer(text);
			IVoicedCommandHandler vch;
			String command = "";
			
			if (st.countTokens() > 1)
			{
				command = st.nextToken().substring(1);
				params = text.substring(command.length() + 2);
				vch = VoicedCommandHandler.getInstance().getHandler(command);
			}
			else
			{
				command = text.substring(1);
				if (Config.DEBUG)
					_log.info("Command: " + command);
				vch = VoicedCommandHandler.getInstance().getHandler(command);
			}
			if (vch != null)
			{
				vch.useVoicedCommand(command, activeChar, params);
				vcd_used = true;
			}
			else
			{
				if (Config.DEBUG)
					_log.warning("No handler registered for bypass '" + command + "'");
				vcd_used = false;
			}
		}
		if (!vcd_used)
		{
			if (activeChar.isChatBanned() && Util.contains(Config.BAN_CHAT_CHANNELS, type))
			{
				activeChar.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED);
				return;
			}
		
			/**
			 * Match the character "." literally (Exactly 1 time)
			 * Match any character that is NOT a . character. Between one and unlimited times as possible, giving back as needed (greedy)
			 */
			if (text.matches("\\.{1}[^\\.]+"))
				activeChar.sendPacket(SystemMessageId.INCORRECT_SYNTAX);
			else
			{	
                boolean is_action = false;
	            if(text.startsWith(" *")) is_action = true;
	            
				CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getAppearance().getVisibleName(),
											is_action ? text : activeChar.getRPvolume() + activeChar.getRPlanguage() + text);
				
				Collection<L2PcInstance> plrs = activeChar.getKnownList().getKnownPlayers().values();
				for (L2PcInstance player : plrs)
				{
					if (player != null && !player.isInOfflineMode() && !BlockList.isBlocked(player, activeChar))
                    {
                        if((activeChar.getRPvolume().equals("") || is_action) && activeChar.isInsideRadius(player, 1250, false, true))
                            player.sendPacket(cs);
                        
                        else if(activeChar.getRPvolume().equals(" *chuchote* ") && activeChar.isInsideRadius(player, 100, false, true) && !is_action)
                            player.sendPacket(cs);

                        else if(activeChar.getRPvolume().equals(" *crie* ") && activeChar.isInsideRadius(player, 2900, false, true) && !is_action)
                            player.sendPacket(cs);
                        
                        else
                        	continue;                         
                        
                        if (text.startsWith("(") && text.endsWith(")"))
                        	continue;
                        
                        if (EX_ITEM_LINK_PATTERN.matcher(text).find())
                        	continue;
                        
						int rolepex = 0;
						Matcher matcher = THREE_LETTER_WORD_PATTERN.matcher(text);
						while (matcher.find()) rolepex++;
						rolepex *= player.getLevel();
						if (rolepex > 0)
							player.addExpAndSp(Math.round(rolepex * Config.RATE_XP), Math.round(rolepex/10 * Config.RATE_SP));
                    }
				}
				
				activeChar.sendPacket(cs);
			}
		}
	}
	
	/**
	 * Returns the chat types registered to this handler
	 * @see com.l2jserver.gameserver.handler.IChatHandler#getChatTypeList()
	 */
	@Override
	public int[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
}