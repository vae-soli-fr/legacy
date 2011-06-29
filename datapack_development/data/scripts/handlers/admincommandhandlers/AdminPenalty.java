package handlers.admincommandhandlers;

import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.model.L2Skill;
import java.util.StringTokenizer;

/**
 * @author Melua
 */
public class AdminPenalty implements IAdminCommandHandler {

    private static final String[] ADMIN_COMMANDS = {"admin_penalty"};

    public boolean useAdminCommand(String command, L2PcInstance activeChar) {
        StringTokenizer st = new StringTokenizer(command, " ");
        st.nextToken();

        if (command.startsWith("admin_penalty")) {
            try {
                int val = Integer.parseInt(st.nextToken());
                boolean sendMessage = activeChar.getFirstEffect(7029) != null;
                if (activeChar.getTarget() instanceof L2PcInstance) {
                L2PcInstance player = (L2PcInstance) activeChar.getTarget();
                player.stopSkillEffects(5660);
                if (val == 0 && sendMessage) {
                    player.sendPacket(new SystemMessage(SystemMessageId.EFFECT_S1_DISAPPEARED).addSkillName(5660));
                } else if ((val >= 1) && (val <= 5)) {
                    L2Skill deathPenaltySkill = SkillTable.getInstance().getInfo(5660, val);
                    deathPenaltySkill.getEffects(player, player);
                } else {
                    activeChar.sendMessage("Usage: //penalty <value> (0=off...5=max)");
                }
                }
            } catch (Exception e) {
            }
        }
        return true;
    }

    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }
}
