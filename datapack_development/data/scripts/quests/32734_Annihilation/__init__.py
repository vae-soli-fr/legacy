#made by Melua
import sys
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest

qn = "32734_Annihilation"

class Quest (JQuest) :

   def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

   def onTalk(self,npc,player):
      if player.getLevel() < 80 :
         return "32734-no.htm"
      else:
         return "32734-yes.htm"

QUEST = Quest(-1,qn,"Seed of Annihilation")
QUEST.addStartNpc(32734)
QUEST.addTalkId(32734)
