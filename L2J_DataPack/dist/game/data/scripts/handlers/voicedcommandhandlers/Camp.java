package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
/*import com.l2jserver.gameserver.idfactory.IdFactory;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.actor.instance.L2TerrainObjectInstance;
import com.l2jserver.gameserver.datatables.NpcTable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.util.Util;
import static com.l2jserver.gameserver.model.actor.L2Character.ZONE_TOWN;
import com.l2jserver.gameserver.templates.chars.L2NpcTemplate;*/

/**
 * @author Saelil
 * Cette classe implémente la commande .camp
 */
public class Camp implements IVoicedCommandHandler {

    private static final String[] _voicedCommands = {
        "camp"
    };

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String option) {
        if (command.equalsIgnoreCase("camp")) {
            activeChar.evolveCamp();
        }
        return true;

    }

    public String[] getVoicedCommandList() {
        return _voicedCommands;
    }
}