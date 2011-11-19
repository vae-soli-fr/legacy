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

public class PrefectureRune extends Quest
{
	public PrefectureRune(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addEnterZoneId(77700);
                addEnterZoneId(77701);
                addEnterZoneId(77702);
                addEnterZoneId(77703);
                addEnterZoneId(77704);
	}
	
	@Override
	public String onEnterZone(L2Character character, L2ZoneType zone)
	{
		if (zone.getId() == 77700 || zone.getId() == 77701)
		{
			character.teleToLocation(39221, -48241, 896); // Rune CH
		}
                else if (zone.getId() == 77702)
                {
                        character.teleToLocation(-114972, 44933, 518); // Kamael Village WH
                }
                else if (zone.getId() == 77703)
                {
                        character.teleToLocation(-114684, 44933, 552); // WH up
                }
                else if (zone.getId() == 77704)
                {
                        character.teleToLocation(-114780, 45168, 518); // WH down
                }
                
            return super.onEnterZone(character, zone);
	}
	
	public static void main(String[] args)
	{
		new PrefectureRune(-1,"PrefectureRune","custom");
	}
}