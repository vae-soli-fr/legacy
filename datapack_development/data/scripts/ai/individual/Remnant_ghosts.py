import sys
from java.lang import System
from com.l2jserver import L2DatabaseFactory
from com.l2jserver.gameserver.ai import CtrlIntention
from com.l2jserver.gameserver.datatables import SpawnTable
from com.l2jserver.gameserver.instancemanager import HellboundManager
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest


remnants = [18463, 18464]
Derek = 18435

RemnantSpawns = {
0: [18464, -28681, 255110, -2160, 17318, 120],
1: [18464, -26114, 254708, -2139, 6976, 120],
2: [18463, -28457, 256584, -1926, 59926, 120],
3: [18463, -26482, 257663, -1925, 35352, 120],
4: [18464, -26453, 256745, -1930, 50259, 120],
5: [18463, -27362, 256282, -1935, 47936, 120],
6: [18464, -25441, 256441, -2147, 48011, 120]
                }

class Hellbound_remnants (JQuest):

    # init function.  Add in here variables that you'd like to be inherited by subclasses (if any)
    def __init__(self, id, name, descr):
        JQuest.__init__(self, id, name, descr)
        hellboundLevel = HellboundManager.getInstance().getLevel()
        # I am leaving the remnants up until Derek gets killed at level 4
        # I know it's not exactly like on retail, but I think it makes more sense =)
        if hellboundLevel <= 4:
          for i in range(7):
            npcId, xx, yy, zz, head, resp = RemnantSpawns[i]
            newRemnant = HellboundManager.getInstance().addSpawn(npcId, xx, yy, zz, head, resp)
        worldObjects = SpawnTable.getInstance().getSpawnTable().values()
        if hellboundLevel > 4:
            for i in worldObjects:
                npcId = i.getNpcid()
                if npcId in remnants:
                    curNpc = i.getLastSpawn()
                    curNpc.deleteMe()

    def onSkillSee (self, npc, player, skill, targets, isPet):
        if not npc in targets: return
        id = npc.getNpcId()
        if skill.getId() == 2358:
            npc.setIsInvul(0)
            npc.onDecay()
            if id in remnants:
                if HellboundManager.getInstance().getLevel() < 4:
                  if id == 18463:
                    HellboundManager.getInstance().increaseTrust(5)
                    player.sendMessage("The holy water affects Remnants Ghost. You have freed his soul.")
                  if id == 18464:
                    HellboundManager.getInstance().increaseTrust(5)
                    player.sendMessage("The holy water affects Remnants Ghost. You have freed his soul.")
                elif HellboundManager.getInstance().getLevel() >= 4:
                    player.sendMessage("The holy water affects Remnants Ghost. You have freed his soul.")
            if id == Derek:
                HellboundManager.getInstance().increaseTrust(10000)
                HellboundManager.getInstance().changeLevel(5)
                player.sendMessage("The holy water affects Derek. You have freed his soul.")
            return

    def onAttack (self, npc, player, damage, isPet, skill):
        CurrentLevel = HellboundManager.getInstance().getLevel()
        id = npc.getNpcId()
        st = player.getQuestState("Hellbound_remnants")
        if not st:
            st = self.newQuestState(player)
        if npc:
            npcHp = npc.getCurrentHp()
            npcMaxHp = npc.getMaxHp()
            triggerHp = int((npcMaxHp * 3) / 100)
            if npcHp <= triggerHp:
                npc.setIsInvul(1)


    def onKill(self, npc, player, isPet):
        id = npc.getNpcId()
        st = player.getQuestState("Hellbound_remnants")
        if not st:
            st = self.newQuestState(player)
        if npc:
            if id == 22330:
                if HellboundManager.getInstance().getLevel() >= 4: return
                HellboundManager.getInstance().increaseTrust(5)

# Quest class and state definition
QUEST = Hellbound_remnants(-1, "Hellbound_remnants", "ai")

for i in remnants :
    QUEST.addAttackId(i)
    QUEST.addSkillSeeId(i)
QUEST.addSkillSeeId(Derek)
QUEST.addAttackId(Derek)
QUEST.addKillId(22330)
