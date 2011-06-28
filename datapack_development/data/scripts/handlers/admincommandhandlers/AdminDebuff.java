package handlers.admincommandhandlers;

import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.L2Effect;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.skills.Env;
import com.l2jserver.gameserver.templates.effects.EffectTemplate;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Saelil
 */
public class AdminDebuff implements IAdminCommandHandler {

    private static final String[] ADMIN_COMMANDS = {"admin_debuff"};

    public boolean useAdminCommand(String command, L2PcInstance activeChar) {
        StringTokenizer st = new StringTokenizer(command, " ");
        st.nextToken();

        if (command.startsWith("admin_debuff")) 
        {
            try 
            {
                String name = st.nextToken();
                int skillId;
                if(name.equals("stun"))
                    skillId = 5168;
                else if(name.equals("slow"))
                    skillId = 5166;
                else if(name.equals("root"))
                    skillId = 5169;
                else if(name.equals("sleep"))
                    skillId = 5170;
                else if(name.equals("winter"))
                    skillId = 5167;
                else if(name.equals("fear"))
                    skillId = 5173;
                else if(name.equals("poison"))
                    skillId = 5174;
                else if(name.equals("bleed"))
                    skillId = 5175;
                else if(name.equals("silence"))
                    skillId = 5176;
                else
                    return false;
                
                boolean sendMessage = activeChar.getFirstEffect(skillId) != null;
                if (activeChar.getTarget() instanceof L2PcInstance) 
                {
                    L2PcInstance player = (L2PcInstance) activeChar.getTarget();
                    player.stopSkillEffects(skillId);
                    if (sendMessage) 
                        player.sendPacket(new SystemMessage(SystemMessageId.EFFECT_S1_DISAPPEARED).addSkillName(skillId));
                    else
                    {
                        L2Skill debuff = SkillTable.getInstance().getInfo(skillId, 1);
                        ArrayList<L2Effect> effects = new ArrayList<L2Effect>(debuff.getEffectTemplates().length);
                        EffectTemplate[] effectTemplates = debuff.getEffectTemplates();
                        Env env = new Env();
                        env.player = activeChar;
                        env.target = player;
                        env.skill = debuff;
                        for (EffectTemplate et : effectTemplates)
                        {
                            L2Effect e = et.getEffect(env);
                            if(e != null)
                            {
                                  e.scheduleEffect();
                                  effects.add(e);
                            }
                        }
                        debuff.getEffects(player, player);
                    }
                }
            }
            catch (Exception e) 
            {
                activeChar.sendMessage("Usage: //debuff <stun|slow|root|sleep|winter|fear|bleed|poison}silence>");
            }
        }
        return true;
    }

    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }
}
