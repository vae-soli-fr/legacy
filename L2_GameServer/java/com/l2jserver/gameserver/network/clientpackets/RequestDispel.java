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
package com.l2jserver.gameserver.network.clientpackets;

import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.model.L2Effect;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.templates.skills.L2EffectType;

/**
 *
 * @author  KenM
 */
public class RequestDispel extends L2GameClientPacket
{
	private int _skillId;
	private int _skillLevel;
	
	/**
	 * @see com.l2jserver.gameserver.network.clientpackets.L2GameClientPacket#readImpl()
	 */
	@Override
	protected void readImpl()
	{
		readD(); // player objid, not needed
		_skillId = readD();
		_skillLevel = readD();
	}
	
	/**
	 * @see com.l2jserver.gameserver.network.clientpackets.L2GameClientPacket#runImpl()
	 */
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		
		L2Skill skill = SkillTable.getInstance().getInfo(_skillId, _skillLevel);
		if (skill != null && (!skill.isDance() || Config.DANCE_CANCEL_BUFF) && !skill.isDebuff() && skill.canBeDispeled())
		{
			for (L2Effect e : activeChar.getAllEffects())
			{
				if (e != null && e.getSkill() == skill && e.getEffectType() != L2EffectType.TRANSFORMATION)
					e.exit();
			}
		}
	}
	
	/**
	 * @see com.l2jserver.gameserver.network.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return "[C] D0:4E RequestDispel";
	}
}