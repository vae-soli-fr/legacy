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
package quests;

import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Util;

/**
 * @author pchayka
 */
public class Q10295_SevenSignsSolinasTomb extends Quest implements ScriptFile
{
	private static final int ErisEvilThoughts = 32792;
	private static final int ElcardiaInzone1 = 32787;
	private static final int TeleportControlDevice = 32820;
	private static final int PowerfulDeviceStaff = 32838;
	private static final int PowerfulDeviceBook = 32839;
	private static final int PowerfulDeviceSword = 32840;
	private static final int PowerfulDeviceShield = 32841;
	private static final int AltarofHallowsStaff = 32857;
	private static final int AltarofHallowsSword = 32858;
	private static final int AltarofHallowsBook = 32859;
	private static final int AltarofHallowsShield = 32860;
	private static final int TeleportControlDevice2 = 32837;
	private static final int TeleportControlDevice3 = 32842;
	private static final int TomboftheSaintess = 32843;
	private static final int ScrollofAbstinence = 17228;
	private static final int ShieldofSacrifice = 17229;
	private static final int SwordofHolySpirit = 17230;
	private static final int StaffofBlessing = 17231;
	private static final int Solina = 32793;
	private static final int[] SolinaGuardians =
	{
		18952,
		18953,
		18954,
		18955
	};
	private static final int[] TombGuardians =
	{
		18956,
		18957,
		18958,
		18959
	};
	static
	{
		new Location(55672, -252120, -6760);
		new Location(55752, -252120, -6760);
		new Location(55656, -252216, -6760);
		new Location(55736, -252216, -6760);
		new Location(55672, -252728, -6760);
		new Location(55752, -252840, -6760);
		new Location(55768, -252840, -6760);
		new Location(55752, -252712, -6760);
		new Location(56504, -252840, -6760);
		new Location(56504, -252728, -6760);
		new Location(56392, -252728, -6760);
		new Location(56408, -252840, -6760);
		new Location(56520, -252232, -6760);
		new Location(56520, -252104, -6760);
		new Location(56424, -252104, -6760);
		new Location(56440, -252216, -6760);
	}
	
	public Q10295_SevenSignsSolinasTomb()
	{
		super(false);
		addStartNpc(ErisEvilThoughts);
		addTalkId(ElcardiaInzone1, TeleportControlDevice, PowerfulDeviceStaff, PowerfulDeviceBook, PowerfulDeviceSword, PowerfulDeviceShield);
		addTalkId(AltarofHallowsStaff, AltarofHallowsSword, AltarofHallowsBook, AltarofHallowsShield);
		addTalkId(TeleportControlDevice2, TomboftheSaintess, TeleportControlDevice3, Solina);
		addQuestItem(ScrollofAbstinence, ShieldofSacrifice, SwordofHolySpirit, StaffofBlessing);
		addKillId(SolinaGuardians);
		addKillId(TombGuardians);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "eris_q10295_5.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "teleport_in":
				player.teleToLocation(new Location(45512, -249832, -6760));
				teleportElcardia(player);
				return null;
				
			case "teleport_out":
				player.teleToLocation(new Location(120664, -86968, -3392));
				teleportElcardia(player);
				return null;
				
			case "use_staff":
				if (qs.getQuestItemsCount(StaffofBlessing) > 0)
				{
					qs.takeAllItems(StaffofBlessing);
					// TODO: remove glow from NPC
					removeInvincibility(player, 18953);
					return null;
				}
				htmltext = "powerful_q10295_0.htm";
				break;
			
			case "use_book":
				if (qs.getQuestItemsCount(ScrollofAbstinence) > 0)
				{
					qs.takeAllItems(ScrollofAbstinence);
					// TODO: remove glow from NPC
					removeInvincibility(player, 18954);
					return null;
				}
				htmltext = "powerful_q10295_0.htm";
				break;
			
			case "use_sword":
				if (qs.getQuestItemsCount(SwordofHolySpirit) > 0)
				{
					qs.takeAllItems(SwordofHolySpirit);
					// TODO: remove glow from NPC
					removeInvincibility(player, 18955);
					return null;
				}
				htmltext = "powerful_q10295_0.htm";
				break;
			
			case "use_shield":
				if (qs.getQuestItemsCount(ShieldofSacrifice) > 0)
				{
					qs.takeAllItems(ShieldofSacrifice);
					// TODO: remove glow from NPC
					removeInvincibility(player, 18952);
					return null;
				}
				htmltext = "powerful_q10295_0.htm";
				break;
			
			case "altarstaff_q10295_2.htm":
				if (qs.getQuestItemsCount(StaffofBlessing) == 0)
				{
					qs.giveItems(StaffofBlessing, 1);
				}
				else
				{
					htmltext = "atlar_q10295_0.htm";
				}
				break;
			
			case "altarbook_q10295_2.htm":
				if (qs.getQuestItemsCount(ScrollofAbstinence) == 0)
				{
					qs.giveItems(ScrollofAbstinence, 1);
				}
				else
				{
					htmltext = "atlar_q10295_0.htm";
				}
				break;
			
			case "altarsword_q10295_2.htm":
				if (qs.getQuestItemsCount(SwordofHolySpirit) == 0)
				{
					qs.giveItems(SwordofHolySpirit, 1);
				}
				else
				{
					htmltext = "atlar_q10295_0.htm";
				}
				break;
			
			case "altarshield_q10295_2.htm":
				if (qs.getQuestItemsCount(ShieldofSacrifice) == 0)
				{
					qs.giveItems(ShieldofSacrifice, 1);
				}
				else
				{
					htmltext = "atlar_q10295_0.htm";
				}
				break;
			
			case "teleport_solina":
				player.teleToLocation(new Location(56033, -252944, -6760));
				teleportElcardia(player);
				return null;
				
			case "tombsaintess_q10295_2.htm":
				if (!player.getReflection().getDoor(21100101).isOpen())
				{
					activateTombGuards(player);
				}
				else
				{
					htmltext = "tombsaintess_q10295_3.htm";
				}
				break;
			
			case "teleport_realtomb":
				player.teleToLocation(new Location(56081, -250391, -6760));
				teleportElcardia(player);
				player.showQuestMovie(ExStartScenePlayer.SCENE_SSQ2_ELYSS_NARRATION);
				return null;
				
			case "solina_q10295_4.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "solina_q10295_8.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		
		if (!player.isBaseClassActive())
		{
			return "no_subclass_allowed.htm";
		}
		
		switch (npc.getId())
		{
			case ErisEvilThoughts:
				if (cond == 0)
				{
					final QuestState state = player.getQuestState(Q10294_SevenSignsToTheMonasteryOfSilence.class);
					
					if ((player.getLevel() >= 81) && (state != null) && state.isCompleted())
					{
						htmltext = "eris_q10295_1.htm";
					}
					else
					{
						htmltext = "eris_q10295_0a.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond == 1)
				{
					htmltext = "eris_q10295_6.htm";
				}
				else if (cond == 2)
				{
					htmltext = "eris_q10295_7.htm";
				}
				else if (cond == 3)
				{
					if (player.getLevel() >= 81)
					{
						htmltext = "eris_q10295_8.htm";
						qs.addExpAndSp(125000000, 12500000);
						qs.setState(COMPLETED);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(false);
					}
					else
					{
						htmltext = "eris_q10295_0.htm";
					}
				}
				break;
			
			case ElcardiaInzone1:
				htmltext = "elcardia_q10295_1.htm";
				break;
			
			case TeleportControlDevice:
				if (!checkGuardians(player, SolinaGuardians))
				{
					htmltext = "teleport_device_q10295_1.htm";
				}
				else
				{
					htmltext = "teleport_device_q10295_2.htm";
				}
				break;
			
			case PowerfulDeviceStaff:
				htmltext = "powerfulstaff_q10295_1.htm";
				break;
			
			case PowerfulDeviceBook:
				htmltext = "powerfulbook_q10295_1.htm";
				break;
			
			case PowerfulDeviceSword:
				htmltext = "powerfulsword_q10295_1.htm";
				break;
			
			case PowerfulDeviceShield:
				htmltext = "powerfulsheild_q10295_1.htm";
				break;
			
			case AltarofHallowsStaff:
				htmltext = "altarstaff_q10295_1.htm";
				break;
			
			case AltarofHallowsSword:
				htmltext = "altarsword_q10295_1.htm";
				break;
			
			case AltarofHallowsBook:
				htmltext = "altarbook_q10295_1.htm";
				break;
			
			case AltarofHallowsShield:
				htmltext = "altarshield_q10295_1.htm";
				break;
			
			case TeleportControlDevice2:
				htmltext = "teleportdevice2_q10295_1.htm";
				break;
			
			case TomboftheSaintess:
				htmltext = "tombsaintess_q10295_1.htm";
				break;
			
			case TeleportControlDevice3:
				htmltext = "teleportdevice3_q10295_1.htm";
				break;
			
			case Solina:
				if (cond == 1)
				{
					htmltext = "solina_q10295_1.htm";
				}
				else if (cond == 2)
				{
					htmltext = "solina_q10295_4.htm";
				}
				else if (cond == 3)
				{
					htmltext = "solina_q10295_8.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final Player player = qs.getPlayer();
		
		if (Util.contains(SolinaGuardians, npcId) && checkGuardians(player, SolinaGuardians))
		{
			player.broadcastPacket(new EventTrigger(21100100, false));
			player.showQuestMovie(ExStartScenePlayer.SCENE_SSQ2_SOLINA_TOMB_CLOSING);
			player.broadcastPacket(new EventTrigger(21100102, true));
		}
		
		if (Util.contains(TombGuardians, npcId))
		{
			if (checkGuardians(player, TombGuardians))
			{
				player.getReflection().openDoor(21100018);
			}
			
			switch (npcId)
			{
				case 18956:
					player.getReflection().despawnByGroup("tombguards3");
					break;
				
				case 18957:
					player.getReflection().despawnByGroup("tombguards2");
					break;
				
				case 18958:
					player.getReflection().despawnByGroup("tombguards1");
					break;
				
				case 18959:
					player.getReflection().despawnByGroup("tombguards4");
					break;
			}
		}
		
		return null;
	}
	
	private void teleportElcardia(Player player)
	{
		for (NpcInstance n : player.getReflection().getNpcs())
		{
			if (n.getId() == ElcardiaInzone1)
			{
				n.teleToLocation(Location.findPointToStay(player, 100));
			}
		}
	}
	
	private void removeInvincibility(Player player, int mobId)
	{
		for (NpcInstance n : player.getReflection().getNpcs())
		{
			if (n.getId() == mobId)
			{
				for (Effect e : n.getEffectList().getAllEffects())
				{
					if (e.getSkill().getId() == 6371)
					{
						e.exit();
					}
				}
			}
		}
	}
	
	private boolean checkGuardians(Player player, int[] npcIds)
	{
		for (NpcInstance n : player.getReflection().getNpcs())
		{
			if (Util.contains(npcIds, n.getId()) && !n.isDead())
			{
				return false;
			}
		}
		
		return true;
	}
	
	private void activateTombGuards(Player player)
	{
		Reflection r = player.getReflection();
		
		if ((r == null) || r.isDefault())
		{
			return;
		}
		
		r.openDoor(21100101);
		r.openDoor(21100102);
		r.openDoor(21100103);
		r.openDoor(21100104);
		r.spawnByGroup("tombguards1");
		r.spawnByGroup("tombguards2");
		r.spawnByGroup("tombguards3");
		r.spawnByGroup("tombguards4");
	}
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
}