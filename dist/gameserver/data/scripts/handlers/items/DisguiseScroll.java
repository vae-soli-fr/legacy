/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.items;

import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeRunnerEvent;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author VISTALL
 * @date 13:42/07.04.2011
 */
public class DisguiseScroll extends ScriptItemHandler
{
	private final int[] ITEM_IDS =
	{
		13677, // Gludio Disguise Scroll
		13678, // Dion Disguise Scroll
		13679, // Giran Disguise Scroll
		13680, // Oren Disguise Scroll
		13681, // Aden Disguise Scroll
		13682, // Innadril Disguise Scroll
		13683, // Goddard Disguise Scroll
		13684, // Rune Disguise Scroll
		13685, // Schuttgart Disguise Scroll
	};
	private final int[] DOMINION_IDS =
	{
		81,
		82,
		83,
		84,
		85,
		86,
		87,
		88,
		89
	};
	
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if (!playable.isPlayer())
		{
			return false;
		}
		
		Player player = (Player) playable;
		
		DominionSiegeRunnerEvent runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 1);
		if (!runnerEvent.isBattlefieldChatActive())
		{
			player.sendPacket(SystemMsg.THE_TERRITORY_WAR_EXCLUSIVE_DISGUISE_AND_TRANSFORMATION_CAN_BE_USED_20_MINUTES_BEFORE_THE_START_OF_THE_TERRITORY_WAR_TO_10_MINUTES_AFTER_ITS_END);
			return false;
		}
		int index = org.apache.commons.lang3.ArrayUtils.indexOf(ITEM_IDS, item.getId());
		DominionSiegeEvent siegeEvent = player.getEvent(DominionSiegeEvent.class);
		if (siegeEvent == null)
		{
			player.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addName(item));
			return false;
		}
		if (siegeEvent.getId() != DOMINION_IDS[index])
		{
			player.sendPacket(SystemMsg.THE_DISGUISE_SCROLL_CANNOT_BE_USED_BECAUSE_IT_IS_MEANT_FOR_USE_IN_A_DIFFERENT_TERRITORY);
			return false;
		}
		if (player.isCursedWeaponEquipped())
		{
			player.sendPacket(SystemMsg.A_DISGUISE_CANNOT_BE_USED_WHEN_YOU_ARE_IN_A_CHAOTIC_STATE);
			return false;
		}
		if (player.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
		{
			player.sendPacket(SystemMsg.THE_DISGUISE_SCROLL_CANNOT_BE_USED_WHILE_YOU_ARE_ENGAGED_IN_A_PRIVATE_STORE_OR_MANUFACTURE_WORKSHOP);
			return false;
		}
		if (siegeEvent.getResidence().getOwner() == player.getClan())
		{
			player.sendPacket(SystemMsg.A_TERRITORY_OWNING_CLAN_MEMBER_CANNOT_USE_A_DISGUISE_SCROLL);
			return false;
		}
		if (player.consumeItem(item.getId(), 1))
		{
			siegeEvent.addObject(DominionSiegeEvent.DISGUISE_PLAYERS, player.getObjectId());
			player.broadcastCharInfo();
		}
		return true;
	}
	
	@Override
	public int[] getItemIds()
	{
		return ITEM_IDS;
	}
}