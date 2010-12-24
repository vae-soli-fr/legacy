package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.Config;
import java.util.logging.Logger;
import javolution.util.FastMap;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.util.GMAudit;

/**
 * @author Melua
 * Mort aux GBs !
 *
 */

public class RaidBossLimiter
{
	private final static Logger _log = Logger.getLogger(RaidBossLimiter.class.getName());
    private boolean _jail;
	private FastMap<String, Integer> _list;

	public static final RaidBossLimiter getInstance()
	{
		return SingletonHolder._instance;
	}

	private RaidBossLimiter()
	{
		_list = new FastMap<String, Integer>();
        _jail = true;
	}

    public void setJail(boolean jail) {
        _jail = jail;
    }

	public void addPoint(L2PcInstance player)
	{
        if (!_jail) return;
        if (_list.containsKey(player.getAccountName()))
        {
            int points = _list.get(player.getAccountName());
            _list.put(player.getAccountName(), ++points);
            if (points > 3) autoBan(player);
        }
        else _list.put(player.getAccountName(), 1);
    }


    private void autoBan(L2PcInstance player)
    {
        if (player.isFlyingMounted()) player.untransform();
        player.setPunishLevel(L2PcInstance.PunishLevel.JAIL, 1440);
        player.sendMessage("Les RBs sont limités à 3 par jour (après reboot). Vous avez dépassé le quota autorisé. Ce personnage est en prison pour 24 heures.");
        _log.info("Le joueur " + player.getName() + " du compte " + player.getAccountName() + " a été ban 24h par le RaidBossLimiter");
        if (Config.GMAUDIT) GMAudit.auditGMAction("[RaidBossLimiter]", "Ban de 24h", player.getName() + " du compte " + player.getAccountName());
    }


	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final RaidBossLimiter _instance = new RaidBossLimiter();
	}
}