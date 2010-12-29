import sys
from java.lang import System
from com.l2jserver import L2DatabaseFactory
from com.l2jserver.gameserver.datatables import ItemTable
from com.l2jserver.gameserver.datatables import SpawnTable
from com.l2jserver.gameserver.instancemanager import HellboundManager
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest
from com.l2jserver.util import Rnd


Chimera = [22349, 22350, 22351, 22352]
Celtus = 22353
LOCS = [
          [4276, 237245, -3310]
          , [11437, 236788, -1949]
          , [7647, 235672, -1977]
          , [1882, 233520, -3315]
]

def dropItem(player, npc, itemId, count):
    ditem = ItemTable.getInstance().createItem("Forces", itemId, count, player)
    ditem.dropMe(player, npc.getX(), npc.getY(), npc.getZ());

class Chimeras (JQuest):

    # init function.  Add in here variables that you'd like to be inherited by subclasses (if any)
    def __init__(self, id, name, descr):
        JQuest.__init__(self, id, name, descr)
        if HellboundManager.getInstance().getLevel() >= 7:
          LOC = LOCS[Rnd.get(len(LOCS))]
          respTime = (18 + Rnd.get(36)) * 100 #between 30m and 1h
          newCeltus = HellboundManager.getInstance().addSpawn(Celtus, LOC[0], LOC[1], LOC[2], 0, respTime)


    def onSkillSee (self, npc, player, skill, targets, isPet):
        if not npc in targets: return
        id = npc.getNpcId()
        maxHp = npc.getMaxHp()
        nowHp = npc.getStatus().getCurrentHp()
        if (nowHp < maxHp * 0.1):
            if skill.getId() == 2359:
              if HellboundManager.getInstance().getLevel() >= 7:
                if id in Chimera:
                  if Rnd.get(100) < 10:
                    dropItem(player, npc, 9681, 1)
                  dropItem(player, npc, 9680, 1)
                  npc.onDecay()
                  return
                if id == Celtus:
                  dropItem(player, npc, 9682, 1)
                  npc.onDecay()
                  return


QUEST = Chimeras(-1, "Chimeras", "ai")

for i in Chimera:
    QUEST.addSkillSeeId(i)
QUEST.addSkillSeeId(Celtus)