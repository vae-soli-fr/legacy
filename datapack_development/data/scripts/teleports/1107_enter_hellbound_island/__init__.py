# Created by L2Emu Team
import sys
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest
from com.l2jserver import L2DatabaseFactory

qn = "1107_enter_hellbound_island"

WARPGATES = [32314, 32315, 32316, 32317, 32318, 32319]

class Quest (JQuest) :

 def __init__(self, id, name, descr): JQuest.__init__(self, id, name, descr)

 def onTalk (self, npc, player):
   st = player.getQuestState(qn)
   if not st: return

   con = L2DatabaseFactory.getInstance().getConnection()
   trigger = con.prepareStatement("SELECT unlocked FROM hellbound WHERE name=8000")
   trigger1 = trigger.executeQuery()
   while (trigger1.next()):
       HellboundLock = trigger1.getInt("unlocked")
   con.close()
   npcId = npc.getNpcId()
   st1 = st.getPlayer().getQuestState("130_PathToHellbound")
   if HellboundLock == 0:
       if st1 :
         if npcId in WARPGATES and st1.getState() == State.COMPLETED and player.getLevel() >= 78 :
           player.teleToLocation(-11095, 236440, -3232)
           htmltext = ""
         else :
           htmltext = "cant-port.htm"
       else :
         htmltext = "cant-port.htm"
   elif HellboundLock == 1:
       if st1 :
         if npcId in WARPGATES and st1.getState() == State.COMPLETED :
           player.teleToLocation(-11095, 236440, -3232)
           htmltext = ""
           con = L2DatabaseFactory.getInstance().getConnection()
           insertion = con.prepareStatement("DELETE FROM hellbound WHERE name=8000")
           insertion.executeUpdate()
           insertion.execute()
           insertion.close();
           insertion = con.prepareStatement("INSERT INTO hellbound (name,trustLevel,zonesLevel,unlocked,dummy) VALUES (?,?,?,?,?)")
           insertion.setInt(1, 8000)
           insertion.setInt(2, 0)
           insertion.setInt(3, 1)
           insertion.setInt(4, 1)
           insertion.setInt(5, 0)
           insertion.executeUpdate()
           insertion.close();
           con.close()

         else :
           htmltext = "cant-port.htm"
       else :
         htmltext = "cant-port.htm"
   else :
       htmltext = "cant-port.htm"
   st.exitQuest(1)
   return htmltext

QUEST = Quest(1107, qn, "teleports")

for npcId in WARPGATES :
    QUEST.addStartNpc(npcId)
    QUEST.addTalkId(npcId)