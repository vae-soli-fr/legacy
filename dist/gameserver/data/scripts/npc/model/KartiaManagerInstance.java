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
package npc.model;

import instances.KartiaLabyrinth85Party;
import instances.KartiaLabyrinth85Solo;
import instances.KartiaLabyrinth90Party;
import instances.KartiaLabyrinth90Solo;
import instances.KartiaLabyrinth95Party;
import instances.KartiaLabyrinth95Solo;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

public final class KartiaManagerInstance extends NpcInstance
{
	private static final Location TELEPORT_POSITION = new Location(-109032, -10440, -11949);
	private static final int Adolph88 = 33609;
	private static final int Barton88 = 33611;
	private static final int Hayuk88 = 33613;
	private static final int Eliyah88 = 33615;
	private static final int Elise88 = 33617;
	private static final int Adolph93 = 33620;
	private static final int Barton93 = 33622;
	private static final int Hayuk93 = 33624;
	private static final int Eliyah93 = 33626;
	private static final int Elise93 = 33628;
	private static final int Adolph98 = 33631;
	private static final int Barton98 = 33633;
	private static final int Hayuk98 = 33635;
	private static final int Eliyah98 = 33637;
	private static final int Elise98 = 33639;
	
	public KartiaManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		
		final Reflection r = player.getActiveReflection();
		
		switch (command)
		{
			case "request_zellaka_solo":
				if (r != null)
				{
					if (player.canReenterInstance(205))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(205))
				{
					ReflectionUtils.enterReflection(player, new KartiaLabyrinth85Solo(), 205);
				}
				break;
			
			case "request_zellaka_party":
				if (r != null)
				{
					if (player.canReenterInstance(208))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(208))
				{
					ReflectionUtils.enterReflection(player, new KartiaLabyrinth85Party(), 208);
				}
				break;
			
			case "request_pelline_solo":
				if (r != null)
				{
					if (player.canReenterInstance(206))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(206))
				{
					ReflectionUtils.enterReflection(player, new KartiaLabyrinth90Solo(), 206);
				}
				break;
			
			case "request_pelline_party":
				if (r != null)
				{
					if (player.canReenterInstance(209))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(209))
				{
					ReflectionUtils.enterReflection(player, new KartiaLabyrinth90Party(), 209);
				}
				break;
			
			case "request_kalios_solo":
				if (r != null)
				{
					if (player.canReenterInstance(207))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(207))
				{
					ReflectionUtils.enterReflection(player, new KartiaLabyrinth95Solo(), 207);
				}
				break;
			
			case "request_kalios_party":
				if (r != null)
				{
					if (player.canReenterInstance(210))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(210))
				{
					ReflectionUtils.enterReflection(player, new KartiaLabyrinth95Party(), 210);
				}
				break;
		}
		
		if (command.startsWith("start"))
		{
			String[] splited = command.split("\\s");
			String param = splited[1];
			
			if (r != null)
			{
				// PARTY INSTANCE
				if (r.getInstancedZoneId() > 207)
				{
					player.getParty().Teleport(TELEPORT_POSITION);
				}
				// SOLO INSTANCE
				else
				{
					player.teleToLocation(TELEPORT_POSITION);
					
					switch (r.getInstancedZoneId())
					{
						case 205:
							NpcInstance adolph88 = r.addSpawnWithoutRespawn(Adolph88, TELEPORT_POSITION, 100);
							adolph88.setFollowTarget(player);
							if (!param.equals("w"))
							{
								NpcInstance barton = r.addSpawnWithoutRespawn(Barton88, TELEPORT_POSITION, 100);
								barton.setFollowTarget(player);
							}
							if (!param.equals("a"))
							{
								NpcInstance hayuk = r.addSpawnWithoutRespawn(Hayuk88, TELEPORT_POSITION, 100);
								hayuk.setFollowTarget(player);
							}
							if (!param.equals("s"))
							{
								NpcInstance eliyah = r.addSpawnWithoutRespawn(Eliyah88, TELEPORT_POSITION, 100);
								eliyah.setFollowTarget(player);
							}
							if (!param.equals("h"))
							{
								NpcInstance elise = r.addSpawnWithoutRespawn(Elise88, TELEPORT_POSITION, 100);
								elise.setFollowTarget(player);
							}
							break;
						
						case 206:
							NpcInstance adolph93 = r.addSpawnWithoutRespawn(Adolph93, TELEPORT_POSITION, 100);
							adolph93.setFollowTarget(player);
							if (!param.equals("w"))
							{
								NpcInstance barton = r.addSpawnWithoutRespawn(Barton93, TELEPORT_POSITION, 100);
								barton.setFollowTarget(player);
							}
							if (!param.equals("a"))
							{
								NpcInstance hayuk = r.addSpawnWithoutRespawn(Hayuk93, TELEPORT_POSITION, 100);
								hayuk.setFollowTarget(player);
							}
							if (!param.equals("s"))
							{
								NpcInstance eliyah = r.addSpawnWithoutRespawn(Eliyah93, TELEPORT_POSITION, 100);
								eliyah.setFollowTarget(player);
							}
							if (!param.equals("h"))
							{
								NpcInstance elise = r.addSpawnWithoutRespawn(Elise93, TELEPORT_POSITION, 100);
								elise.setFollowTarget(player);
							}
							break;
						
						case 207:
							NpcInstance adolph98 = r.addSpawnWithoutRespawn(Adolph98, TELEPORT_POSITION, 100);
							adolph98.setFollowTarget(player);
							if (!param.equals("w"))
							{
								NpcInstance barton = r.addSpawnWithoutRespawn(Barton98, TELEPORT_POSITION, 100);
								barton.setFollowTarget(player);
							}
							if (!param.equals("a"))
							{
								NpcInstance hayuk = r.addSpawnWithoutRespawn(Hayuk98, TELEPORT_POSITION, 100);
								hayuk.setFollowTarget(player);
							}
							if (!param.equals("s"))
							{
								NpcInstance eliyah = r.addSpawnWithoutRespawn(Eliyah98, TELEPORT_POSITION, 100);
								eliyah.setFollowTarget(player);
							}
							if (!param.equals("h"))
							{
								NpcInstance elise = r.addSpawnWithoutRespawn(Elise98, TELEPORT_POSITION, 100);
								elise.setFollowTarget(player);
							}
							break;
					}
				}
			}
		}
		
		super.onBypassFeedback(player, command);
	}
}