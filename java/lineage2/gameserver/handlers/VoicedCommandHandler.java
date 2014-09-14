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
package lineage2.gameserver.handlers;

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.data.xml.AbstractHolder;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class VoicedCommandHandler extends AbstractHolder
{
	private static final VoicedCommandHandler _instance = new VoicedCommandHandler();
	
	/**
	 * Method getInstance.
	 * @return VoicedCommandHandler
	 */
	public static VoicedCommandHandler getInstance()
	{
		return _instance;
	}
	
	private final Map<String, IVoicedCommandHandler> _datatable = new HashMap<>();
	
	/**
	 * Constructor for VoicedCommandHandler.
	 */
	private VoicedCommandHandler()
	{
	}
	
	/**
	 * Method registerVoicedCommandHandler.
	 * @param handler IVoicedCommandHandler
	 */
	public void registerVoicedCommandHandler(IVoicedCommandHandler handler)
	{
		String[] ids = handler.getVoicedCommandList();
		
		for (String element : ids)
		{
			_datatable.put(element, handler);
		}
	}
	
	/**
	 * Method getVoicedCommandHandler.
	 * @param voicedCommand String
	 * @return IVoicedCommandHandler
	 */
	public IVoicedCommandHandler getVoicedCommandHandler(String voicedCommand)
	{
		String command = voicedCommand;
		
		if (voicedCommand.contains(" "))
		{
			command = voicedCommand.substring(0, voicedCommand.indexOf(" "));
		}
		
		return _datatable.get(command);
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _datatable.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_datatable.clear();
	}
}
