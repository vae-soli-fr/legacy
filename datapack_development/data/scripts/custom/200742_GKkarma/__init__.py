#made by Lotradas/Kirieh
import sys
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest

qn = "200742_GKkarma"
#NPC
GK = 200742

class Quest (JQuest) :

   def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

   def onTalk(self,npc,player):
      if player.getKarma() > 5000 :
         return "200742-2.htm"
      else:
         return "<html><body>Hors de ma vue, serviteur du bien.</body></html>"

QUEST       = Quest(-1,qn,"GKkarma")
QUEST.addStartNpc(200742)
QUEST.addTalkId(200742)
