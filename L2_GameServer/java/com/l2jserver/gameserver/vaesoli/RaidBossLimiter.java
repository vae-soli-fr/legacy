package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.NpcTable;
import java.util.logging.Logger;
import javolution.util.FastMap;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jserver.gameserver.util.GMAudit;

/**
 * @author Melua
 * Mort aux GBs !
 * Cette classe jail au bout de plus de 3 RBs par compte
 */

public class RaidBossLimiter
{
	private final static Logger _log = Logger.getLogger(RaidBossLimiter.class.getName());
	private FastMap<String, Integer> _list; // liste des points par compte
    private FastMap<String, String> _done; // liste des noms des RB par compte

	public static final RaidBossLimiter getInstance()
	{
		return SingletonHolder._instance;
	}

	private RaidBossLimiter()
	{
		_list = new FastMap<String, Integer>();
                _done = new FastMap<String, String>();
	}

	public void addPoint(L2PcInstance player, String raidboss)
	{
        if (!Config.VAEMOD_RBJAIL || player.getInstanceId() != 0) return;
        if (_list.containsKey(player.getAccountName())
                && _done.containsKey(player.getAccountName()))
        {
            int points = _list.get(player.getAccountName());
            String names = _done.get(player.getAccountName());
            _list.put(player.getAccountName(), ++points);
            _done.put(player.getAccountName(), names + ", " + raidboss);
            if (points == 3)
            {
                player.sendPacket(new ExShowScreenMessage(1, -1, 7, 0, 1, 0, 0, true, 15000, 0, "Attention ceci était votre dernier Raid Boss"));
                player.sendMessage("---------------------------------------------");
                player.sendMessage("Attention ceci était votre dernier Raid Boss.");
                player.sendMessage("---------------------------------------------");
            }
            else if (points > 3) autoBan(player);
        }
        else
        {
            _list.put(player.getAccountName(), 1);
            _done.put(player.getAccountName(), raidboss);
        }
    }


    private void autoBan(L2PcInstance player)
    {
        if (player.isFlyingMounted()) player.untransform();
        player.setPunishLevel(L2PcInstance.PunishLevel.JAIL, 1440);
        player.sendMessage("Les RBs sont limités à 3 par jour par joueur (après reboot). Vous avez dépassé le quota autorisé.");
        _log.info("Le personnage " + player.getName() + " du compte " + player.getAccountName() + " est banni 24h par le RaidBossLimiter");
        if (Config.GMAUDIT) GMAudit.auditGMAction("RaidBossLimiter", "jail 24h", player.getName() + " (" + player.getAccountName() + ")", _done.get(player.getAccountName()));
    }


	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final RaidBossLimiter _instance = new RaidBossLimiter();
	}
}