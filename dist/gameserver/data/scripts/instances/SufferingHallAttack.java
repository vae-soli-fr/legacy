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
package instances;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExSendUIEvent;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;

/**
 * @author pchayka
 */
public final class SufferingHallAttack extends Reflection
{
	private static final int AliveTumor = 18704;
	private static final int DeadTumor = 32531;
	private static final int Yehan = 25665;
	public int timeSpent;
	long _savedTime = 0;
	private final DeathListener _deathListener = new DeathListener();
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		_savedTime = System.currentTimeMillis();
		timeSpent = 0;
		spawnRoom(1);
	}
	
	private class DeathListener implements OnDeathListener
	{
		public DeathListener()
		{
		}
		
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (!self.isNpc())
			{
				return;
			}
			
			if (self.getId() == AliveTumor)
			{
				if (self.isInZone("[soi_hos_attack_1]"))
				{
					addSpawnWithoutRespawn(DeadTumor, self.getLoc(), 0);
					self.deleteMe();
					getZone("[soi_hos_attack_defenceup_1]").setActive(false);
					getZone("[soi_hos_attack_attackup_1]").setActive(false);
					spawnRoom(2);
				}
				else if (self.isInZone("[soi_hos_attack_2]"))
				{
					addSpawnWithoutRespawn(DeadTumor, self.getLoc(), 0);
					self.deleteMe();
					getZone("[soi_hos_attack_defenceup_2]").setActive(false);
					getZone("[soi_hos_attack_attackup_2]").setActive(false);
					spawnRoom(3);
				}
				else if (self.isInZone("[soi_hos_attack_3]"))
				{
					addSpawnWithoutRespawn(DeadTumor, self.getLoc(), 0);
					self.deleteMe();
					getZone("[soi_hos_attack_defenceup_3]").setActive(false);
					getZone("[soi_hos_attack_attackup_3]").setActive(false);
					spawnRoom(4);
				}
				else if (self.isInZone("[soi_hos_attack_4]"))
				{
					addSpawnWithoutRespawn(DeadTumor, self.getLoc(), 0);
					self.deleteMe();
					getZone("[soi_hos_attack_defenceup_4]").setActive(false);
					getZone("[soi_hos_attack_attackup_4]").setActive(false);
					spawnRoom(5);
				}
				else if (self.isInZone("[soi_hos_attack_5]"))
				{
					addSpawnWithoutRespawn(DeadTumor, self.getLoc(), 0);
					self.deleteMe();
					getZone("[soi_hos_attack_defenceup_5]").setActive(false);
					getZone("[soi_hos_attack_attackup_5]").setActive(false);
					spawnRoom(6);
				}
			}
			else if (self.getId() == Yehan)
			{
				ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						spawnRoom(7);
						setReenterTime(System.currentTimeMillis());
						
						for (Player p : getPlayers())
						{
							p.sendPacket(new ExSendUIEvent(p, 1, 1, 0, 0));
							p.sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(5));
						}
						
						startCollapseTimer(5 * 60 * 1000L);
						timeSpent = (int) (System.currentTimeMillis() - _savedTime) / 1000;
					}
				}, 10000L);
			}
		}
	}
	
	private void invokeDeathListener()
	{
		for (NpcInstance npc : getNpcs())
		{
			npc.addListener(_deathListener);
		}
	}
	
	void spawnRoom(int id)
	{
		switch (id)
		{
			case 1:
				spawnByGroup("soi_hos_attack_1");
				getZone("[soi_hos_attack_attackup_1]").setActive(true);
				getZone("[soi_hos_attack_defenceup_1]").setActive(true);
				break;
			
			case 2:
				spawnByGroup("soi_hos_attack_2");
				getZone("[soi_hos_attack_attackup_2]").setActive(true);
				getZone("[soi_hos_attack_defenceup_2]").setActive(true);
				break;
			
			case 3:
				spawnByGroup("soi_hos_attack_3");
				getZone("[soi_hos_attack_attackup_3]").setActive(true);
				getZone("[soi_hos_attack_defenceup_3]").setActive(true);
				break;
			
			case 4:
				spawnByGroup("soi_hos_attack_4");
				getZone("[soi_hos_attack_attackup_4]").setActive(true);
				getZone("[soi_hos_attack_defenceup_4]").setActive(true);
				break;
			
			case 5:
				spawnByGroup("soi_hos_attack_5");
				getZone("[soi_hos_attack_attackup_5]").setActive(true);
				getZone("[soi_hos_attack_defenceup_5]").setActive(true);
				break;
			
			case 6:
				spawnByGroup("soi_hos_attack_6");
				getZone("[soi_hos_attack_pcbuff_6]").setActive(true);
				break;
			
			case 7:
				spawnByGroup("soi_hos_attack_7");
				getZone("[soi_hos_attack_pcbuff_6]").setActive(false);
				break;
			
			default:
				break;
		}
		
		invokeDeathListener();
	}
	
	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
		player.sendPacket(new ExSendUIEvent(player, 0, 1, (int) (System.currentTimeMillis() - _savedTime) / 1000, 0, NpcString.NONE));
	}
	
	@Override
	public void onPlayerExit(Player player)
	{
		super.onPlayerExit(player);
		player.sendPacket(new ExSendUIEvent(player, 1, 1, 0, 0));
	}
}