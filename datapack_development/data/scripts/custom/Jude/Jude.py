import sys
from java.lang import System
from com.l2jserver import L2DatabaseFactory
from com.l2jserver.gameserver.instancemanager import HellboundManager
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest

JudeNpc = 32356

class Jude (JQuest):

    def __init__(self, id, name, descr):
        JQuest.__init__(self, id, name, descr)

    def onFirstTalk (self, npc, player):
        id = npc.getNpcId()
        st = player.getQuestState("Jude")
        if not st:
            st = self.newQuestState(player)
        hellboundLevel = HellboundManager.getInstance().getLevel()
        if hellboundLevel < 3: return "<html><body>Jude:<br>Novices, yes? From where you arrived?</body></html>"
        if hellboundLevel == 3: return "32356.htm"
        if hellboundLevel == 4: return "<html><body>Jude:<br>I heard that the Native Village still exposed by the attacks?</body></html>"
        if hellboundLevel == 5: return "<html><body>Jude:<br>Oh, Natives did ask you to do it? That, I certainly feel with their situation, but we are merchants, eventually. Would you say that the purchase-sale is the course of natures! Actually I would like to ask you about the favour. I do not know, as Natives, but you, I am sure, can handle with by this task.<br>If go from here on a north, you will find a place, where minerals are obtaining for Darion. This place is named Quarry, and many representatives of my people were there turned in slaves, and compelled them to work. Please,  free them! It was yet better, if b you succeeded to free the country of Caravan, but it is the long and difficult trip. Take exempt slaves in Dolmen at the entrance in Quarry. Natives consider it as a sacred place, but we often use him for the movement. Since you you will deliver them there, we will care about them after.<br>At the guards of Quarry order to kill every slave which will try to break. You must at any cost protect our people from such fate! Avoid guards and guard our people on a way to freedom.</body></html>"
        if hellboundLevel >= 6: npc.showChatWindow(player)


    def onAdvEvent (self, event, npc, player):
        if player:
            st = player.getQuestState("Jude")
        if event == "TreasureSacks":
            if st.getQuestItemsCount(9684) < 40: return "<html><body>Jude:<br>You have unenough amount of Native Treasures.</body></html>"
            st.takeItems(9684, 40)
            HellboundManager.getInstance().changeLevel(4)
            return "<html><body>Jude:<br>Oh, treasure... Where it was?</body></html>"

QUEST = Jude(-1, "Jude", "custom")

QUEST.addStartNpc(JudeNpc)
QUEST.addTalkId(JudeNpc)
QUEST.addFirstTalkId(JudeNpc)