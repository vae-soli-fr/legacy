import sys
from java.lang import System
from com.l2jserver.gameserver.ai import CtrlIntention
from com.l2jserver.gameserver.datatables import DoorTable
from com.l2jserver.gameserver.instancemanager import HellboundManager
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest
from com.l2jserver.gameserver.network.serverpackets import CreatureSay
from com.l2jserver.util import Rnd


Leodas = 22448
LeodasMinions = 22451
Traitor = 32364
Doors = [19250003, 19250004]
LeodasLoc = [-27807, 252740, -3520]


class Hellbound_leodas (JQuest):

    # init function.  Add in here variables that you'd like to be inherited by subclasses (if any)
    def __init__(self, id, name, descr):
        JQuest.__init__(self, id, name, descr)
        self.Leodas = 0

    def onAttack(self, npc, player, damage, isPet, skill):
        id = npc.getNpcId()
        for i in Doors:
            DoorTable.getInstance().getDoor(i).closeMe()

    def onKill(self, npc, player, isPet):
        id = npc.getNpcId()
        for i in Doors:
            DoorTable.getInstance().getDoor(i).openMe()
            DoorTable.getInstance().getDoor(i).onOpen()
        HellboundManager.getInstance().increaseTrust(-1000) #value needs to be updated
        self.Leodas = 0

    def onFirstTalk (self, npc, player):
        id = npc.getNpcId()
        st = player.getQuestState("Hellbound_leodas")
        hellboundLevel = HellboundManager.getInstance().getLevel()
        if self.Leodas == 1: return "<html><body>Hellbound Traitor:<br>Leodas already attacked!</body></html>"
        if not hellboundLevel in (5, 6): return
        npc.showChatWindow(player)

    def onAdvEvent (self, event, npc, player):
        if event == "meetLeodas":
            st = player.getQuestState("Hellbound_leodas")
            MarksCount = st.getQuestItemsCount(9676)
            if MarksCount >= 1 and MarksCount < 10: return "<html><body>Hellbound Traitor:<br>Yeah! You have a <font color=""LEVEL"">Mark of Betrayal</font>. Unfortunately, I can in no way help you, if you do not bring to me 10 Marks. Bring all Marks to me, and I will immediately open door.</body></html>"
            if MarksCount < 1: return "<html><body>Hellbound Traitor:<br>I need <font color=""LEVEL"">10 Mark of Betrayal</font>. But you did not bring one! Do not attempt to deceive me - bring to me that I was requested!</body></html>"
            st.takeItems(9676, 10)
            npc.broadcastPacket(CreatureSay(npc.getObjectId(), 1, npc.getName(), "Brothers! This stranger wants to kill our Commander!!!"))
            self.startQuestTimer("Leodas", 3000, npc, None, False)
            self.Leodas = 1
            for i in Doors:
                DoorTable.getInstance().getDoor(i).openMe()
        if event == "Leodas":
            xx, yy, zz = LeodasLoc
            newLeodas = HellboundManager.getInstance().addSpawn(Leodas, xx, yy, zz, 0, 0)

# Quest class and state definition
QUEST = Hellbound_leodas(-1, "Hellbound_leodas", "ai")

QUEST.addAttackId(Leodas)
QUEST.addKillId(Leodas)
QUEST.addTalkId(Traitor)
QUEST.addFirstTalkId(Traitor)
QUEST.addStartNpc(Traitor)