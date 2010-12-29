import sys
from java.lang import System
from com.l2jserver import L2DatabaseFactory
from com.l2jserver.gameserver.instancemanager import HellboundManager
from com.l2jserver.gameserver.model.actor.instance import L2PcInstance
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest

BernardeNpc = 32300

class Bernarde (JQuest):

    def __init__(self, id, name, descr):
        JQuest.__init__(self, id, name, descr)
        self.condition = 0

    def onFirstTalk (self, npc, player):
        id = npc.getNpcId()
        st = player.getQuestState("Bernarde")
        if not st:
            st = self.newQuestState(player)
        hellboundLevel = HellboundManager.getInstance().getLevel()
        if hellboundLevel < 2: return "<html><body>Bernarde:<br>Who are you? Seems from a continent. You already took away all at us, what only it is possible! Go away from here and leave us at peace!</body></html>"
        if hellboundLevel == 2:
            if not player.isTransformed(): return "<html><body>Bernarde:<br>Who are you? Seems from a continent. You already took away all at us, what only it is possible! Go away from here and leave us at peace!</body></html>"
            if not player.getTransformation().getId() == 101: return "<html><body>Bernarde:<br>Who are you? Seems from a continent. You already took away all at us, what only it is possible! Go away from here and leave us at peace!</body></html>"
            return "32300.htm"
        if hellboundLevel == 3:
            if not player.isTransformed(): return "<html><body>Bernarde:<br>He, seems, from our island. He looks very strong.</body></html>"
            if not player.getTransformation().getId() == 101: return "<html><body>Bernarde:<br>He, seems, from our island. He looks very strong.</body></html>"
            return "32300-2.htm"
        if hellboundLevel == 4:
            if not player.isTransformed(): return "32300-5.htm"
            if not player.getTransformation().getId() == 101: return "32300-5.htm"
            return "32300-4.htm"
        if hellboundLevel == 5:
            if not player.isTransformed(): return "32300-6.htm"
            if not player.getTransformation().getId() == 101: return "32300-6.htm"
            return "<html><body>Bernarde:<br>Ah, glad to meeting, my friend! Thank you to you, that helped the Derek's soul to attain Nirvana. Now habitants of the village under his guardianship.</body></html>"
        if hellboundLevel >= 6 and hellboundLevel < 9:
            if not player.isTransformed(): return "32300-3.htm"
            if not player.getTransformId() == 101: return "32300-3.htm"
            return "32300-7.htm"
        if hellboundLevel == 9: return "<html><body>Bernarde:<br>There are a lot of Darion's followers in Steel Citadel, and easier to us from it does not become. We must find a method to kill all of them!</body></html>"
        if hellboundLevel == 10: return "<html><body>Bernarde:<br>I heard that the habitants of continent and Resistance have moved up deeply in the heart of Steel Citadel. o, this  frigging earth can, finally, see sunset of a new era! At me on eyes the tears from one are piled up thoughts about it! But while it is early to celebrate - we must go and help our brothers in a castle.</body></html>"
        if hellboundLevel == 11: return "<html><body>Bernarde:<br>Now we must to defeat Beleth and Darion. And, in addition, to bring a peace on Hellbound.</body></html>"

    def onAdvEvent (self, event, npc, player):
        if player:
            st = player.getQuestState("Bernarde")
        if event == "HolyWater":
            BadgesCount = st.getQuestItemsCount(9674)
            if BadgesCount < 5: return "<html><body>Bernarde:<br>I am afraid, that you have the unenough Darion's Badges.</body></html>"
            st.takeItems(9674, 5)
            st.giveItems(9673, 1)
            return "<html><body>Bernarde:<br>Here. Now, please, help our ancestors to sleep and rests in world!</body></html>"
        if event == "Treasure":
            if self.condition == 1: return "<html><body>Bernarde:<br>Seems you already gave treasure to me?</body></html>"
            treasures = st.getQuestItemsCount(9684)
            if treasures < 1: return "<html><body>Bernarde:<br>O, and where is treasure?</body></html>"
            st.takeItems(9684, 1)
            self.condition = 1
            return "<html><body>Bernarde:<br>Thank you! This treasure very help for natives.</body></html>"
        if event == "rumors":
            hLevel = HellboundManager.getInstance().getLevel()
            if hLevel == 6: return "<html><body>Bernarde:<br>Demons have been very active in the Magical Field lately. There must be something going on for them to only increase security in that area. Would you investigate? Perhaps you will find something that will help us reduce Beleth's power.</body></html>"
            if hLevel == 7: return "<html><body>Bernarde:<br>I knew, why the Battered Lands was left, why even caravans abandoning hope are afraid to go there. Possibly, when you will be ready, you will want to find out is it independent? Speak, it is possible to use Magic Bottle, to collect Magic Souls of monsters which in same queue are very valued by oversea magicians. Why not to make you to attempt?</body></html>"
            if hLevel == 8: return "<html><body>Bernarde:<br>I heard that Resistance and main habitants of continent have forced out Devils back to the external gate of Steel Citadel! A breach through gate is now only the question of time. True, one of erected hostile captains strengthening before a gate, but associations of our forces it must be it is enough, to manage with him even!</body></html>"
        if event == "alreadysaid": return "<html><body>Bernarde:<br>I said enough; now I must go back to work. Please, help to bring rest to those my ancestors which ferment among the ruins of Ancient Temple.</body></html>"
        if event == "abouthelp": return "<html><body>Bernarde:<br>Derek is obvious, first priest, from time to time appears among the Ruins of Ancient Temple. How is tragic, that his spirit still roams there, incapable to rest! While he ferments, I, am afraid, there will never be a world between a temple and local village...</body></html>"
        if event == "quarry": return "<html><body>Bernarde:<br>They speak that the slaves of caravan are forced to work on a career. If you will be able to rescue them, a caravan is rather in all to consent to enter into with local. Please, help us to free the Quarry Slaves!</body></html>"

QUEST = Bernarde(-1, "Bernarde", "custom")

QUEST.addStartNpc(BernardeNpc)
QUEST.addFirstTalkId(BernardeNpc)
QUEST.addTalkId(BernardeNpc)