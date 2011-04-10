# Update by U3Games 17-03-2011
# Special thanks to contributors users l2jserver
# Imported: L2jTW by pmq 10-07-2010 thx!
# Author: Psycho(killer1888) / L2jFree
# By: evill33t & vital
import sys
from java.lang                                       import System
from com.l2jserver.gameserver.ai                     import CtrlIntention
from com.l2jserver.gameserver.datatables             import DoorTable
from com.l2jserver.gameserver.datatables             import ItemTable
from com.l2jserver.gameserver.datatables             import SpawnTable
from com.l2jserver.gameserver.instancemanager        import HellboundManager
from com.l2jserver.gameserver.instancemanager        import InstanceManager
from com.l2jserver.gameserver.model                  import L2ItemInstance
from com.l2jserver.gameserver.model.actor            import L2Summon
from com.l2jserver.gameserver.model.entity           import Instance
from com.l2jserver.gameserver.model.itemcontainer    import PcInventory
from com.l2jserver.gameserver.model.quest            import State
from com.l2jserver.gameserver.model.quest            import QuestState
from com.l2jserver.gameserver.model.quest.jython     import QuestJython as JQuest
from com.l2jserver.gameserver.network                import SystemMessageId
from com.l2jserver.gameserver.network.serverpackets  import CreatureSay
from com.l2jserver.gameserver.network.serverpackets  import InventoryUpdate
from com.l2jserver.gameserver.network.serverpackets  import MagicSkillUse
from com.l2jserver.gameserver.network.serverpackets  import NpcSay
from com.l2jserver.gameserver.network.serverpackets  import SystemMessage
from com.l2jserver.gameserver.util                   import Util
from com.l2jserver.util                              import Rnd

qn = "HBTown"

debug = False

# NPCs
KANAF          = 32346
PRISONER       = 32358
STELE          = 32343

# Mobs
AMASKARI       = 22449
KEYMASTER      = 22361
GUARD          = 22359
NATIVE         = 22450
LIST = [22359, 22360]

# Items
KEY            = 9714

AMASKARI_TEXT = ["�����A���`���ݵۧA�I","�p�H�A�u���H��_","�����A�ڱ��F�A�Ӵ�����a�H�ۥ�","�D�H�ڦC�����|�ܰ���","��ӬO�A..."]

KLOCS = [
		[14264,250333,-1935,15133],
		[19961,256249,-2086,47344],
		[17271,252888,-2010,64381],
		[15784,252413,-2010,49254],
		[22029,254160,-2005,60246]
]

ReturnPort = [[16278,283633,-9709]]

dataIndex  = 0

class PyObject :
	pass

def callGuards(self,npc,player,world):
	guardList = []
	newNpc = self.addSpawn(GUARD,npc.getX()+50,npc.getY(),npc.getZ(),0,False,0,10,world.instanceId)
	guardList.append(newNpc)
	newNpc = self.addSpawn(GUARD,npc.getX()-50,npc.getY(),npc.getZ(),0,False,0,10,world.instanceId)
	guardList.append(newNpc)
	newNpc = self.addSpawn(GUARD,npc.getX(),npc.getY()+50,npc.getZ(),0,False,0,10,world.instanceId)
	guardList.append(newNpc)
	newNpc = self.addSpawn(GUARD,npc.getX(),npc.getY()-50,npc.getZ(),0,False,0,10,world.instanceId)
	guardList.append(newNpc)
	for mob in guardList:
		mob.setRunning()
		mob.addDamageHate(player, 0, 999)
		mob.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player)

def autochat(npc,text) :
	if npc: npc.broadcastPacket(NpcSay(npc.getObjectId(),0,npc.getNpcId(),text))
	return

def dropItem(player,npc,itemId,count):
	ditem = ItemTable.getInstance().createItem("Loot", itemId, count, player)
	ditem.dropMe(npc, npc.getX(), npc.getY(), npc.getZ()); 

def checkCondition(player):
	if not player.getLevel() >= 78:
		sm = SystemMessage.getSystemMessage(SystemMessageId.C1_LEVEL_REQUIREMENT_NOT_SUFFICIENT)
		sm.addCharName(player)
		player.sendPacket(sm)
		return False
	return True

def teleportPlayer(self,player,teleto):
	player.setInstanceId(teleto.instanceId)
	player.teleToLocation(teleto.x,teleto.y,teleto.z)
	pet = player.getPet()
	if pet != None :
		pet.setInstanceId(teleto.instanceId)
		pet.teleToLocation(teleto.x,teleto.y,teleto.z)
	return

def getExistingInstanceId(player):
	instanceId = 0
	party = player.getParty()
	if party == None:
		return 0
	for partyMember in party.getPartyMembers().toArray():
		if partyMember.getInstanceId()!=0:
			instanceId = partyMember.getInstanceId()
	return instanceId

def exitInstance(player,tele):
	player.setInstanceId(0)
	player.teleToLocation(tele.x, tele.y, tele.z)
	pet = player.getPet()
	if pet != None :
		pet.setInstanceId(0)
		pet.teleToLocation(tele.x, tele.y, tele.z)

class Quest(JQuest):

	def __init__(self, id, name, descr):
		JQuest.__init__(self, id, name, descr)
		self.worlds = {}
		self.world_ids = []
		self.Slaves = {}
		self.currentWorld = 0
		self.Lock = 0
		self.NATIVELock = 0
		self.hellboundLevel = 0
		self.trustp = 0
		try:
			self.trustp = int(self.loadGlobalQuestVar("trust10p"))
		except:
			pass
		self.saveGlobalQuestVar("trust10p", str(self.trustp))
		if HellboundManager.getInstance().getLevel() == 10: self.startQuestTimer("CheckTrustP", 60000, None, None, True)

	def onAdvEvent(self, event, npc, player):
		if event == "CheckTrustP":
			if self.trustp >= 500000:
				HellboundManager.getInstance().changeLevel(11)
				self.trustp = 0
				self.saveGlobalQuestVar("trust10p", str(self.trustp))
				self.cancelQuestTimers("CheckTrustP")
		elif event == "decayNpc":
			npc.decayMe()
		elif event == "NATIVESay":
			world = self.worlds[npc.getInstanceId()]
			npc.broadcastPacket(NpcSay(22450, 0, 22450, "�ڷ|...�N...����...����...�I"))
			npc.broadcastPacket(NpcSay(22450, 0, 22450, "�ڷ|...�N...����...����...�I"))
			npc.broadcastPacket(NpcSay(22450, 0, 22450, "�ڷ|...�N...����...����...�I"))
		elif event == "freeprisoner":
			world = self.worlds[npc.getInstanceId()]
			sayNpc = npc.getObjectId()
			npc.broadcastPacket(NpcSay(sayNpc, 0, npc.getNpcId(), "�h�����U�I�ݦu�̰��W�N�n�ӤF�ָ��_��..."))
			self.startQuestTimer("decayNpc", 5000, npc, None)
			chance = Rnd.get(100)
			if chance <= 30:
				if not world.guardsSpawned:
					callGuards(self,npc,player,world)
					world.guardsSpawned = True
					npc.broadcastPacket(NpcSay(22359, 0, 22359, "�o.�{.�J.�I.��...�I"))
					hellboundLevel = HellboundManager.getInstance().getLevel()
					if hellboundLevel == 10:
						HellboundManager.getInstance().increaseTrust(50)
						self.trustp += 50
						self.saveGlobalQuestVar("trust10p", str(self.trustp))
		elif event == "key":
			world = self.worlds[npc.getInstanceId()]
			if not world.instanceFinished:
				key = player.getInventory().getItemByItemId(KEY)
				if key != None:
					world.instanceFinished = True
					player.destroyItemByItemId("Moonlight Stone", KEY, 1, player, True)
					instance = InstanceManager.getInstance().getInstance(npc.getInstanceId())
					if instance != None:
						instance.setDuration(300000)
						instance.setReturnTeleport(ReturnPort[dataIndex][0],ReturnPort[dataIndex][1],ReturnPort[dataIndex][2])
				else :
					return "32343-2.htm"
			else :
				return "32343-1.htm"
		return

	def onSpawn(self,npc):
		npcId = npc.getNpcId()
		objId = npc.getObjectId()
		if npcId == AMASKARI:
			self.Prisonslaves = []
			self.Slaves[objId] = []
			self.Slaves[objId].append("noSlaves")
			xx,yy,zz = npc.getX(),npc.getY(),npc.getZ()
			self.Slaves[objId] = []
			for i in range(9):
				offsetX = xx + (50 - Rnd.get(250))
				offsetY = yy + (50 - Rnd.get(250))
				newSlave = self.addSpawn(22450,offsetX,offsetY,zz,0,False,0,False,npc.getInstanceId())
				newSlave.setRunning()
				newSlave.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, npc)
				self.Slaves[objId].append(newSlave)

	def onTalk(self, npc, player):
		npcId = npc.getNpcId()
		st = player.getQuestState(qn)
		if not st :
			st = self.newQuestState(player)
		hellboundLevel = HellboundManager.getInstance().getLevel()
		if hellboundLevel < 10: return "<html><body>How did you get inward? The gate of lock is closed... A patrol can appear at any moment. Run!</body></html>"
		if npcId == KANAF :
			party = player.getParty()
			if not party:
				return "32346-0.htm"
			if not checkCondition(player):
				return
			else :
				tele = PyObject()
				tele.x = 14205
				tele.y = 255451
				tele.z = -2025
				instanceId = getExistingInstanceId(player)
				if instanceId == 0:
					instanceId = InstanceManager.getInstance().createDynamicInstance("HBTown.xml")
					if not self.worlds.has_key(instanceId):
						world = PyObject()
						world.rewarded=[]
						world.instanceId = instanceId
						world.instanceFinished = False
						world.guardsSpawned = False
						self.worlds[instanceId] = world
						self.world_ids.append(instanceId)
						self.currentWorld = instanceId
						print "HellboundTown: started Hellbound Town Instance: " +str(instanceId) + " created by player: " + str(player.getName())
						KLOC = KLOCS[Rnd.get(len(KLOCS))]
						newKeymaster = self.addSpawn(KEYMASTER,KLOC[0],KLOC[1],KLOC[2],KLOC[3],False,0,False,world.instanceId)
						self.keymaster = newKeymaster
						self.keymasterattacked = False
						newAmaskari = self.addSpawn(AMASKARI,19496,253125,-2030,0,False,0,False,world.instanceId)
						self.amaskari = newAmaskari
						self.amaskariattacked = False
						tele.instanceId = instanceId
						teleportPlayer(self,player,tele)
						party = player.getParty()
						if party != None:
							for partyMember in party.getPartyMembers().toArray():
								teleportPlayer(self,partyMember,tele)
				else:
					for worldid in self.world_ids:
						if worldid == instanceId:
							foundworld = True
						if not worldid == instanceId:
							foundworld = False
					if not foundworld:
						player.sendPacket(SystemMessage.sendString("Your Party Members are in another Instance."))
						return
					tele.instanceId = instanceId
					teleportPlayer(self,player,tele)
		return

	def onKill(self, npc, player, isPet):
		npcId = npc.getNpcId()
		objId = npc.getObjectId()
		if npcId == KEYMASTER:
			HellboundManager.getInstance().increaseTrust(250)
			self.trustp += 250
			self.saveGlobalQuestVar("trust10p", str(self.trustp))
			chance = Rnd.get(100)
			if chance <= 75:
				npc.broadcastPacket(NpcSay(objId, 0, npc.getNpcId(), "�ڪ��ѧr�I��.��..�_...��......."))
				dropItem(player,npc,9714,1)
			else:
				npc.broadcastPacket(NpcSay(objId, 0, npc.getNpcId(), "�A�û�������o��ڪ�..�_�͡I"))
		if npcId == AMASKARI:
			if HellboundManager.getInstance().getLevel() <= 11:
				HellboundManager.getInstance().increaseTrust(500)
				self.trustp += 500
				self.saveGlobalQuestVar("trust10p", str(self.trustp))
			try:
				if self.Slaves[objId][0] != "noSlaves":
					for i in self.Slaves[objId]:
						try:
							i.setIsInvul(1)
							i.broadcastPacket(CreatureSay(i.getObjectId(), 0, i.getName(), "���§A�ϧڡI"))
							i.decayMe()
						except:
							pass
					self.Slaves[objId] = []
			except:
				pass
		if npcId == NATIVE:
			HellboundManager.getInstance().increaseTrust(-10)
			self.trustp -= 10
			self.saveGlobalQuestVar("trust10p", str(self.trustp))
			self.Lock = 0
		if npcId == PRISONER:
			HellboundManager.getInstance().increaseTrust(-10)
			self.trustp -= 10
			self.saveGlobalQuestVar("trust10p", str(self.trustp))
		if npcId in LIST:
			HellboundManager.getInstance().increaseTrust(20)
			self.trustp += 20
			self.saveGlobalQuestVar("trust10p", str(self.trustp))
		if self.worlds.has_key(npc.getInstanceId()):
			world = self.worlds[npc.getInstanceId()]
		return

	def onAttack(self, npc, player, damage, isPet, skill):
		st = player.getQuestState(qn)
		npcId = npc.getNpcId()
		objId = npc.getObjectId()
		maxHp = npc.getMaxHp()
		nowHp = npc.getStatus().getCurrentHp()
		if npcId == AMASKARI:
			if (nowHp < maxHp * 0.1):
				if self.Lock == 0:
					npc.broadcastPacket(CreatureSay(objId, 0, npc.getName(), "I will make everyone feel the same suffering as me!"))
					self.Lock = 1
		if npcId == KEYMASTER :
			if not self.keymasterattacked:
				self.keymasterattacked = True
				self.amaskari.teleToLocation(player.getX(),player.getY(),player.getZ())
				self.amaskari.setTarget(player)
				objId = self.amaskari.getObjectId()
				self.amaskari.broadcastPacket(NpcSay(objId, 0, self.amaskari.getNpcId(), AMASKARI_TEXT[Rnd.get(len(AMASKARI_TEXT))]))
				self.startQuestTimer("NATIVESay", 5000, npc, None)
		if npcId == NATIVE :
			if self.NATIVELock == 0:
				npc.broadcastPacket(CreatureSay(objId, 0, npc.getName(), "Thank you for saving me!"))
				self.NATIVELock = 1
		if self.worlds.has_key(npc.getInstanceId()):
			world = self.worlds[npc.getInstanceId()]
		return

QUEST = Quest(-1, qn, "hellbound")

for id in [32343,32346,32358] :
	QUEST.addStartNpc(id)

for id in [32343,32346,32358] :
	QUEST.addTalkId(id)

for i in LIST:
    QUEST.addKillId(i)

for mob in [22361,22449,22450] :
	QUEST.addAttackId(mob)

for mob in [22361,22449,22450] :
	QUEST.addKillId(mob)