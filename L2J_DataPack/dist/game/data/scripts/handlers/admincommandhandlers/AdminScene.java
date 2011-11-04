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
package handlers.admincommandhandlers;

import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.L2WorldRegion;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class AdminScene implements IAdminCommandHandler {

    private static final String[] ADMIN_COMMANDS = {
        "admin_scene"
    };

    public boolean useAdminCommand(String command, L2PcInstance activeChar) {
        StringTokenizer st = new StringTokenizer(command, " ");
        String actualCommand = st.nextToken();

        if (actualCommand.equalsIgnoreCase("admin_scene")) {
            try {
                int id = Integer.parseInt(st.nextToken());
                L2WorldRegion region = L2World.getInstance().getRegion(activeChar.getX(), activeChar.getY());
                for (L2PcInstance player : L2World.getInstance().getAllPlayersArray()) {
                    if (region == L2World.getInstance().getRegion(player.getX(), player.getY()) && (player.getInstanceId() == activeChar.getInstanceId())) {
                        player.showQuestMovie(id);
                    }
                }
            } catch ( NoSuchElementException | NumberFormatException e) {
                //Case of empty
                AdminHelpPage.showHelpPage(activeChar, "scene.htm");
            }
        }
        return true;
    }

    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }
}