import sys
from java.lang import System
from com.l2jserver.gameserver.instancemanager import HellboundManager
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest

NativesNpc = 32357

class InnerOasis (JQuest):

    def __init__(self, id, name, descr):
        JQuest.__init__(self, id, name, descr)
        self.badgesamount = 0

    def onAdvEvent (self, event, npc, player) :
        if event == "FreeSlaves":
            st = player.getQuestState("InnerOasis")
            badges = st.getQuestItemsCount(9674)
            if badges < 5:
                return "<html><body>You do not have enough Darions Badges. You need at least 5 such Badges to left this place.</body></html>"
            st.takeItems(9674, 5)
            self.badgesamount += 1
            if self.badgesamount == 6:
                HellboundManager.getInstance().changeLevel(10)
                print "---- Hellbound achieved Level 10"
                announce = "Hellbound now has the level of trust 10"
            return"<html><body>Thank you!<br>Now I can left this place!</body></html>"

    def onFirstTalk (self, npc, player):
        npcId = npc.getNpcId()
        st = player.getQuestState("InnerOasis")
        if not st:
            st = self.newQuestState(player)
        hellboundLevel = HellboundManager.getInstance().getLevel()
        xx, yy = npc.getX(), npc.getY()
        if hellboundLevel < 9: return "<html><body>Native Slave:<br>How did you get inward? The gate of lock is closed... A patrol can appear at any moment. Run!</body></html>"
        if hellboundLevel == 9 and xx in range(4475, 10960) and yy in range(247925, 254415):
            return "32357.htm"
        if hellboundLevel > 9: return "<html><body>Native Slave:<br>Thank you!<br>To my mind, Hellbound island has earned its freedom. Many thanks!</body></html>"


QUEST = InnerOasis(-1, "InnerOasis", "custom")

QUEST.addFirstTalkId(NativesNpc)
QUEST.addTalkId(NativesNpc)
QUEST.addStartNpc(NativesNpc)