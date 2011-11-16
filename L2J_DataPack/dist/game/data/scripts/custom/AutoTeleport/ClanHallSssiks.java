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
package custom.AutoTeleport;



import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.zone.L2ZoneType;

public class ClanHallSssiks extends Quest
{
	public ClanHallSssiks(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addEnterZoneId(88800);
                addEnterZoneId(88801);
	}
	
	@Override
	public String onEnterZone(L2Character character, L2ZoneType zone)
	{
		if (zone.getId() == 88800)
		{
			character.teleToLocation(38667, -46183, 900); // Clan Hall à Rune
		}
                else if (zone.getId() == 88801)
                {
                        character.teleToLocation(10373, 16697, -4584); // Bâtiment à DEV
                }
                
            return super.onEnterZone(character, zone);
	}
	
	public static void main(String[] args)
	{
		new ClanHallSssiks(-1,"ClanHallSssiks","custom");
	}
}