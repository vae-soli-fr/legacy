import sys
from com.l2jserver.gameserver.model.quest            import State
from com.l2jserver.gameserver.model.quest            import QuestState
from com.l2jserver.gameserver.model.quest.jython	    import QuestJython as JQuest
from com.l2jserver.gameserver.model.actor.instance	import L2PcInstance

qn = "HB_Deltuva"

DELTUVA = 32313

class Quest (JQuest) :

	def __init__(self,id,name,descr):
		JQuest.__init__(self,id,name,descr)

	def onTalk (self,npc,player):
		st = player.getQuestState(qn)
		if not st: return

		npcId = npc.getNpcId()
		st1 = player.getQuestState("132_MatrasCuriosity") # TODO rework QuestState 132_MatrasCuriosity
		htmltext = "<html><body>McDowell Palestinian territories:<br>It appears you have not been into the city of steel Oh. Too dangerous because it can not let you go.</body></html>"
		if st1 :
			if npcId == DELTUVA and st1.getState() == State.COMPLETED :
				player.teleToLocation(17957, 283361, -9705)
				htmltext = "<html><body>McDowell Palestinian territories:<br><font color=\"FF0000\">(This is the test transmission is not yet loaded!)</font></body></html>"
				st.exitQuest(1)
		st.exitQuest(1)
		return htmltext

QUEST = Quest(-1, qn, "hellbound")

QUEST.addStartNpc(DELTUVA)
QUEST.addTalkId(DELTUVA)