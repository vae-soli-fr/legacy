package handlers.admincommandhandlers;

import java.util.ArrayList;
import java.util.Random;

import com.l2jserver.gameserver.datatables.CharNameTable;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.instancemanager.TransformationManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;

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
                	unPolyMorph(target);
                	revertName(target);
                    activeChar.sendMessage("Le joueur "+ target.getName() + " est redevenu normal.");
                    target.sendMessage("Vous redevenez normal.");
                }
                else
                {
                	polyMorph("monster", target);
                	rename("monster", target);
                    activeChar.sendMessage("Le joueur "+ target.getName() + " est devenu un monstre.");
                    target.sendMessage("Vous devenez un monstre.");
                }
                target.beMonster();
            }
            else if(activeChar.getTarget() == null)
            {
            	activeChar.sendMessage("Cible incorrecte.");
            }
        }
        else if (command.startsWith("admin_guard"))
        {
            if(activeChar.getTarget() instanceof L2PcInstance)
            {
                L2PcInstance target = (L2PcInstance) activeChar.getTarget();
                if(target.isGuard())
                {
                	unPolyMorph(target);
                	revertName(target);
                    activeChar.sendMessage("Le joueur "+ target.getName() + " est redevenu normal.");
                    target.sendMessage("Vous redevenez normal.");
                }
                else
                {
                	polyMorph("guard", target);
                	rename("guard", target);
                    activeChar.sendMessage("Le joueur "+ target.getName() + " est devenu un garde.");
                    target.sendMessage("Vous devenez un garde.");
                }
                target.beGuard();
            }
            else if(activeChar.getTarget() == null)
            {
                activeChar.sendMessage("Cible incorrecte.");
            }
        }
        
        return true;
    }

    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }
    
    private String getGuardName(L2PcInstance target)
    {
    	String name;
    	if(!target.getAppearance().getSex())
    	{
    		ArrayList<String> maleNames = new ArrayList<String>();
	    	maleNames.add("Lucas");
	    	maleNames.add("Enzo");
	    	maleNames.add("Theo");
	    	maleNames.add("Matheos");
	    	maleNames.add("Kilian");
	    	maleNames.add("Gabriel");
	    	maleNames.add("Rayan");
	    	maleNames.add("Raphael");
	    	maleNames.add("Dylan");
	    	maleNames.add("Evan");
	    	Random rnd = new Random();
	    	name = maleNames.get(rnd.nextInt(maleNames.size()));
	    	while(CharNameTable.getInstance().getIdByName(name) > 0)
	    		name = maleNames.get(rnd.nextInt(maleNames.size()));
    	}
    	else
    	{
    		ArrayList<String> femaleNames = new ArrayList<String>();
    		femaleNames.add("Lea");
    		femaleNames.add("Jade");
    		femaleNames.add("Sarah");
    		femaleNames.add("Oceane");
    		femaleNames.add("Emma");
    		femaleNames.add("Ariane");
    		femaleNames.add("Annabelle");
    		femaleNames.add("Marianne");
    		femaleNames.add("Charlotte");
    		femaleNames.add("Maika");
    		Random rnd = new Random();
	    	name = femaleNames.get(rnd.nextInt(femaleNames.size()));
	    	while(CharNameTable.getInstance().getIdByName(name) > 0)
	    		name = femaleNames.get(rnd.nextInt(femaleNames.size()));
    	}
    	
    	return name;
    }
    
    private String getMonsterName()
    {
    	ArrayList<String> monsterNames = new ArrayList<String>();
    	monsterNames.add("Shargak");
    	monsterNames.add("Kyarl");
    	monsterNames.add("Orkian");
    	monsterNames.add("Ashgard");
    	monsterNames.add("Shyanna");
    	monsterNames.add("Poltr");
    	monsterNames.add("Deshgar");
    	monsterNames.add("Madi");
    	monsterNames.add("Olia");
    	monsterNames.add("Pargas");
    	monsterNames.add("Nielgar");
    	monsterNames.add("Bwaen");
    	monsterNames.add("Taraeg");
    	monsterNames.add("Urass");
    	monsterNames.add("Venea");
    	monsterNames.add("Fernak");
    	monsterNames.add("Casdar");
    	monsterNames.add("Lergan");
    	monsterNames.add("Xasram");
    	monsterNames.add("Ardas");
    	Random rnd = new Random();
    	String name = monsterNames.get(rnd.nextInt(monsterNames.size()));
    	while(CharNameTable.getInstance().getIdByName(name) > 0)
    		name = monsterNames.get(rnd.nextInt(monsterNames.size()));
    	
    	return name;
    }
    
    private void polyMorph(String type, L2PcInstance target)
    {
    	String poly = "30008";
    	if(type.equalsIgnoreCase("guard"))
    	{
	    	if(target.getAppearance().getSex())
	    		poly = "30689";
	    	else
	    		poly = "30008";
    	}
    	else
	    	poly = "13094";
    	target.getPoly().setPolyInfo("npc", poly);
    	MagicSkillUse msk = new MagicSkillUse(target, 1008, 1, 4000, 0);
    	target.broadcastPacket(msk);
    	target.decayMe();
    	target.spawnMe(target.getX(), target.getY(), target.getZ());
    }
    
    private void unPolyMorph(L2PcInstance target)
    {
    	target.getPoly().setPolyInfo(null, "1");
    	target.decayMe();
    	target.spawnMe(target.getX(), target.getY(), target.getZ());
    }
    
    private void rename(String type, L2PcInstance target)
    {
    	L2World.getInstance().removeFromAllPlayers(target);
    	String name = "Aucun";
    	if(type.equalsIgnoreCase("guard"))
    		name = getGuardName(target);
    	else
    		name = getMonsterName();
    	target.getAppearance().setVisibleName(name);
    	target.broadcastUserInfo();
    	L2World.getInstance().addToAllPlayers(target);
    }
    
    private void revertName(L2PcInstance target)
    {
    	L2World.getInstance().removeFromAllPlayers(target);
    	String realName = target.getName();
    	target.getAppearance().setVisibleName(realName);
    	target.broadcastUserInfo();
    	L2World.getInstance().addToAllPlayers(target);
    }
}
