package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.Config;
import java.util.logging.Logger;
import javolution.util.FastMap;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jserver.gameserver.util.GMAudit;

/**
 * @author Melua
 * Mort aux GBs !
 *
 */

public class RaidBossLimiter
{
	private final static Logger _log = Logger.getLogger(RaidBossLimiter.class.getName());
	private FastMap<String, Integer> _list;

	public static final RaidBossLimiter getInstance()
	{
		return SingletonHolder._instance;
	}

	private RaidBossLimiter()
	{
		_list = new FastMap<String, Integer>();
	}

	public void addPoint(L2PcInstance player, int NpcId)
	{
        if (!Config.VAEMOD_RBJAIL || Config.VAEMOD_RBLIST.contains(NpcId)) return;
        if (_list.containsKey(player.getAccountName()))
        {
            int points = _list.get(player.getAccountName());
            _list.put(player.getAccountName(), ++points);
            if (points == 3) player.sendPacket(new ExShowScreenMessage(1, -1, 7, 0, 1, 0, 0, true, 15000, 0, "Attention ceci était votre dernier Raid Boss"));
            else if (points > 3) autoBan(player);
        }
        else _list.put(player.getAccountName(), 1);
    }


    private void autoBan(L2PcInstance player)
    {
        if (player.isFlyingMounted()) player.untransform();
        player.setPunishLevel(L2PcInstance.PunishLevel.JAIL, 1440);
        player.sendMessage("Les RBs sont limités à 3 par jour (après reboot). Vous avez dépassé le quota autorisé.");
        _log.info("Le personnage " + player.getName() + " du compte " + player.getAccountName() + " est banni 24h par le RaidBossLimiter");
        if (Config.GMAUDIT) GMAudit.auditGMAction("RaidBossLimiter", "jail 1440 minutes", player.getName() + " (" + player.getAccountName() + ")");
    }


	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final RaidBossLimiter _instance = new RaidBossLimiter();
	}
}