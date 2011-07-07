package handlers.admincommandhandlers;

import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Saelil
 */
public class AdminMonster implements IAdminCommandHandler {

    private static final String[] ADMIN_COMMANDS = {"admin_monster", "admin_guard"};

    public boolean useAdminCommand(String command, L2PcInstance activeChar) {
        if (command.startsWith("admin_monster"))
        {
            if(activeChar.getTarget() instanceof L2PcInstance)
            {
                L2PcInstance target = (L2PcInstance) activeChar.getTarget();
                if(target.isMonster())
                {
                    activeChar.sendMessage("Le joueur "+ target.getName() + " est redevenu normal.");
                    target.sendMessage("Vous redevenez normal.");
                }
                else
                {
                    activeChar.sendMessage("Le joueur "+ target.getName() + " est devenu un monstre.");
                    target.sendMessage("Vous devenez un monstre.");
                }
                target.beMonster();
            }
            else if(activeChar.getTarget() == null)
            {
                if(activeChar.isMonster())
                    activeChar.sendMessage("Vous redevenez normal.");
                else
                    activeChar.sendMessage("Vous devenez un monstre.");
                activeChar.beMonster();
            }
        }
        else if (command.startsWith("admin_guard"))
        {
            if(activeChar.getTarget() instanceof L2PcInstance)
            {
                L2PcInstance target = (L2PcInstance) activeChar.getTarget();
                if(target.isGuard())
                {
                    activeChar.sendMessage("Le joueur "+ target.getName() + " est redevenu normal.");
                    target.sendMessage("Vous redevenez normal.");
                }
                else
                {
                    activeChar.sendMessage("Le joueur "+ target.getName() + " est devenu un garde.");
                    target.sendMessage("Vous devenez un garde.");
                }
                target.beGuard();
            }
            else if(activeChar.getTarget() == null)
            {
                if(activeChar.isGuard())
                    activeChar.sendMessage("Vous redevenez normal.");
                else
                    activeChar.sendMessage("Vous devenez un garde.");
                activeChar.beGuard();
            }
        }
        
        return true;
    }

    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }
}
