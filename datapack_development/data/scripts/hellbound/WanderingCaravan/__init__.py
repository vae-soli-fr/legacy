import sys
from com.l2jserver.gameserver.model.quest           import State
from com.l2jserver.gameserver.model.quest           import QuestState
from com.l2jserver.gameserver.model.quest.jython    import QuestJython as JQuest
from com.l2jserver.gameserver.model.itemcontainer   import PcInventory
from com.l2jserver.gameserver.model                 import L2ItemInstance
from com.l2jserver.gameserver.network.serverpackets import InventoryUpdate
from com.l2jserver.gameserver.network.serverpackets import SystemMessage
from com.l2jserver.gameserver.network               import SystemMessageId

qn = "WanderingCaravan"

SAND_SCORPION           = 22334
DESERT_SCORPION         = 22335
WANDERING_CARAVAN       = 22339
BASIC_CERTIFICATE       = 9850
STANDARD_CERTIFICATE    = 9851
MARK_BETRAYAL           = 9676
SCORPION_POISON_STINGER = 10012

class Quest (JQuest):
	def __init__(self,id,name,descr):
		JQuest.__init__(self,id,name,descr)

	def onKill (self,npc,player,isPet):
		npcId = npc.getNpcId()
		if npcId == WANDERING_CARAVAN:
			bcertificate = player.getInventory().getItemByItemId(BASIC_CERTIFICATE)
			scertificate = player.getInventory().getItemByItemId(STANDARD_CERTIFICATE)
			if bcertificate and not scertificate:
				item = player.getInventory().addItem("Quest", MARK_BETRAYAL, 1, player, None)
				iu = InventoryUpdate()
				iu.addItem(item)
				player.sendPacket(iu);
				sm = SystemMessage(SystemMessageId.YOU_PICKED_UP_S1_S2)
				sm.addItemName(item)
				sm.addNumber(1)
				player.sendPacket(sm)
		if npcId in [22334,22335]:
			bcertificate = player.getInventory().getItemByItemId(BASIC_CERTIFICATE)
			scertificate = player.getInventory().getItemByItemId(STANDARD_CERTIFICATE)
			if bcertificate and not scertificate:
				item = player.getInventory().addItem("Quest", SCORPION_POISON_STINGER, 1, player, None)
				iu = InventoryUpdate()
				iu.addItem(item)
				player.sendPacket(iu);
				sm = SystemMessage(SystemMessageId.YOU_PICKED_UP_S1_S2)
				sm.addItemName(item)
				sm.addNumber(1)
				player.sendPacket(sm)
		return

QUEST = Quest(-1, qn, "hellbound")

QUEST.addKillId(22339)
QUEST.addKillId(22334)
QUEST.addKillId(22335)