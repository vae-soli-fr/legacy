import sys
from java.lang import System
from com.l2jserver import L2DatabaseFactory
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest
from com.l2jserver.gameserver.instancemanager import HellboundManager

solomon = 32355

class Solomon(JQuest):

    def __init__(self, id, name, descr):
        JQuest.__init__(self, id, name, descr)

    def onFirstTalk (self, npc, player):
        npcId = npc.getNpcId()
        st = player.getQuestState("Solomon")
        if not st:
            st = self.newQuestState(player)
        hellboundLevel = HellboundManager.getInstance().getLevel()
        if hellboundLevel < 5:
            htmltext = "<html><body>Solomon:<br>Our resistance will intend to establish good relations with Caravans, but for this to us are necessary many forces; therefore if you want to help us, go to the Caravan Encampment and speak with Jude to obtain a Caravan Certificate.</body></html>"
        if hellboundLevel == 6 or hellboundLevel == 7:
            htmltext = "<html><body>Solomon:<br>With your help, we have finally begun to establish friendly relations with Caravan. The food we receive from them is a great help to us, and I am confident that sooner or later we will reach the outer gate of Steel Citadel -- after passing the Battered Lands, of course.</body></html>"
        if hellboundLevel >= 9:
            htmltext = "<html><body>Solomon:<br>With your help, we finally approached our main goal - Steel Ciradel. Seems, it will be a great battle. I no longer doubt in our victory. You are our heros...</body></html>"
        return htmltext

QUEST = Solomon(-1, "Solomon", "custom")

QUEST.addStartNpc(solomon)
QUEST.addFirstTalkId(solomon)