import sys
from java.lang import System
from com.l2jserver import L2DatabaseFactory
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest
from com.l2jserver.gameserver.instancemanager import HellboundManager

falk = 32297
CaravanCertificates = [9850, 9851, 9852]

class Falk (JQuest):

    def __init__(self, id, name, descr):
        JQuest.__init__(self, id, name, descr)

    def onTalk (self, npc, player):
        st = player.getQuestState("Falk")
        if not st:
            st = self.newQuestState(player)
        have = 0
        for i in CaravanCertificates:
            if st.getQuestItemsCount(i) >= 1:
                have = 1
        if have == 1: return "<html><body>Falk:<br>Did you already give me the Darion's Badges, remember? Now you must deal with somebody other.</body></html>"
        if st.getQuestItemsCount(9674) < 20: return "<html><body>Falk:<br>I am afraid, it is not enough of Darion's Badges. Please, find yet and come back.</body></html>"
        st.takeItems(9674, 20)
        st.giveItems(9850, 1)
        return "<html><body>Jude:<br>Thank you! Take it. Now you will be able to trade with other Caravan's groups.</body></html>"

QUEST = Falk(-1, "Falk", "custom")

QUEST.addStartNpc(falk)
QUEST.addTalkId(falk)