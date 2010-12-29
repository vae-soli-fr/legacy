import sys
from java.lang import System
from com.l2jserver import L2DatabaseFactory
from com.l2jserver.gameserver.instancemanager import HellboundManager
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest

BudenkaNpc = 32294

class Budenka (JQuest):

    def __init__(self, id, name, descr):
        JQuest.__init__(self, id, name, descr)

    def onFirstTalk (self, npc, player):
        id = npc.getNpcId()
        st = player.getQuestState("Budenka")
        if not st:
            st = self.newQuestState(player)
        hellboundLevel = HellboundManager.getInstance().getLevel()
        standartcert = st.getQuestItemsCount(9851)
        premiumcert = st.getQuestItemsCount(9852)
        if standartcert >= 1 and premiumcert < 1: return "<html><body>Budenka:<br>Oh, it's you?! I heard about you from Guild. Did you analyse that, Bernard is engaged in what, to give the estimation?</body></html>"
        if premiumcert >= 1: return "<html><body>Budenka:<br>Well, you already have enough high reputation in Guild. How are you?</body></html>"
        npc.showChatWindow(player)

QUEST = Budenka(-1, "Budenka", "custom")

QUEST.addStartNpc(BudenkaNpc)
QUEST.addFirstTalkId(BudenkaNpc)
QUEST.addTalkId(BudenkaNpc)