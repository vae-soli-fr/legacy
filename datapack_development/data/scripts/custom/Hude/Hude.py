import sys
from java.lang import System
from com.l2jserver import L2DatabaseFactory
from com.l2jserver.gameserver.instancemanager import HellboundManager
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest

HudeNpc = 32298

class Hude (JQuest):

    def __init__(self, id, name, descr):
        JQuest.__init__(self, id, name, descr)

    def onFirstTalk (self, npc, player):
        id = npc.getNpcId()
        st = player.getQuestState("Hude")
        if not st:
            st = self.newQuestState(player)
        hellboundLevel = HellboundManager.getInstance().getLevel()
        if hellboundLevel >= 4 and st.getQuestItemsCount(9850) >= 1 and st.getQuestItemsCount(9851) < 1: return "32298.htm"
        if hellboundLevel < 7 and hellboundLevel > 3 and st.getQuestItemsCount(9851) >= 1: return "32298-1.htm"
        if hellboundLevel >= 7 and st.getQuestItemsCount(9851) >= 1: return "32298-2.htm"
        if hellboundLevel >= 7 and st.getQuestItemsCount(9852) >= 1: return "32298-3.htm"
        return "<html><body>Hude:<br>I've never seen them before, but they're dressed in really awful clothes.</body></html>"

    def onAdvEvent (self, event, npc, player):
        if player:
            st = player.getQuestState("Hude")
        if event == "scertif":
            if st.getQuestItemsCount(10012) < 60 or st.getQuestItemsCount(9676) < 30: return "<html><body>Hude:<br>I do not see them! Do not try to deceive me!</body></html>"
            st.takeItems(9676, 30)
            st.takeItems(10012, 60)
            st.takeItems(9850, 1)
            st.giveItems(9851, 1)
            return "<html><body>Hude:<br>Remarkably! You pleasantly surprised me.</body></html>"
        if event == "pcertif":
            if st.getQuestItemsCount(9681) < 56 or st.getQuestItemsCount(9682) < 14: return "<html><body>Hude:<br>I know, whatever you would begin to cheat me, but in behalf of our business relations I think that was better, if you give me proofs...</body></html>"
            st.takeItems(9681, 56)
            st.takeItems(9682, 14)
            st.takeItems(9851, 1)
            st.giveItems(9852, 1)
            st.giveItems(9994, 1)
            return "<html><body>Hude:<br>Fine! With such high-quality materials, as these, I will be able well to begin to work. Take this map which was found by a Caravan's near Hellbound. I hope, it will be useful for you.</body></html>"

QUEST = Hude(-1, "Hude", "custom")

QUEST.addStartNpc(HudeNpc)
QUEST.addFirstTalkId(HudeNpc)