/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.l2jserver.gameserver.vaesoli;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExRedSky;
import com.l2jserver.gameserver.network.serverpackets.PlaySound;
import com.l2jserver.gameserver.network.serverpackets.SSQInfo;
import com.l2jserver.gameserver.network.serverpackets.SunSet;
import com.l2jserver.gameserver.skills.AbnormalEffect;

/**
 *
 * @author Melua
 */

public class RedEnterWorld {

    public static final void RedEffects(L2PcInstance player) {
        /* Son dusk rise */
        player.sendPacket(new PlaySound("ssq_dusk_01"));
        /* Nuit */
        player.sendPacket(new SunSet());
        /* Effet Air Root */
        player.startSpecialEffect(AbnormalEffect.S_AIR_ROOT.getMask());
        /* Dusk */
        player.sendPacket(new SSQInfo(1));
        /* Ciel rouge 24h */
        player.sendPacket(new ExRedSky(86400));
    }
}
