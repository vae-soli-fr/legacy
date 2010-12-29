import sys
from java.lang import System
from com.l2jserver import L2DatabaseFactory
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest
from com.l2jserver.gameserver.instancemanager import HellboundManager

BuronNpc = 32345
NativeHelmet = 9669
NativeTunic = 9670
NativePants = 9671
DarionBadge = 9674
level = [3, 4]

class Buron (JQuest):

    def __init__(self, id, name, descr):
        JQuest.__init__(self, id, name, descr)

    def onFirstTalk (self, npc, player):
        npcId = npc.getNpcId()
        st = player.getQuestState("Buron")
        if not st:
            st = self.newQuestState(player)
        hellboundLevel = HellboundManager.getInstance().getLevel()
        if hellboundLevel < 2:
            return "32345-2.htm"
        if hellboundLevel >= 2 and hellboundLevel < 5:
            return "32345.htm"
        if hellboundLevel >= 5:
            return "32345-1.htm"

    def onAdvEvent (self, event, npc, player):
        if player:
            st = player.getQuestState("Buron")
        BadgesCount = st.getQuestItemsCount(DarionBadge)
        text = "<html><body>Buron:<br>It's a pity, but you do not have necessary items.</body></html>"
        if event == "Tunic":
            if BadgesCount < 10: return text
            st.takeItems(DarionBadge, 10)
            st.giveItems(NativeTunic, 1)
        if event == "Helmet":
            if BadgesCount < 10: return text
            st.takeItems(DarionBadge, 10)
            st.giveItems(NativeHelmet, 1)
        if event == "Pants":
            if BadgesCount < 10: return text
            st.takeItems(DarionBadge, 10)
            st.giveItems(NativePants, 1)
        if event == "rumor":
            hellboundLevel = HellboundManager.getInstance().getLevel()
            if hellboundLevel == 1: return "<html><body>Buron:<br>Because press is broken, we sometimes encounter travellers from the continent. You also can meet some of our brothers, whom brought down from the way the Darion's myrmidons. Please, rescue them!</body></html>"
            if hellboundLevel == 2: return "<html><body>Buron:<br>The inhabitants of continent are improbably strong - they moved to the oasis. Walks rumor, that on the southwest of oasis, in the ruins of ancient temple, the spectres of monks and soldiers from time to time appear. In details tell about this can Bernard. It must you warn that Bernard does not entrust to strangers, so that better dress as the local resident, when you go to it.</body></html>"
            if hellboundLevel == 3: return "<html><body>Buron:<br>The inhabitants of village resistances continue to suffer from the frequent attacks of devils, which they fear, that the inhabitants continent and villages will combine their forces in the fight with them. Please, save village!</body></html>"
            if hellboundLevel == 4: return "<html><body>Buron:<br>Our village still undergoes the frequent raids of demons, but now rarely it succeeds by them to break through defense, since local residents and inhabitants of continent combined their force. But we must apply decisive attack to our enemy… Possibly, Bernard knows, as this to make.</body></html>"
            if hellboundLevel == 5: return "<html><body>Buron:<br>Village resistances is rescued, and we again can name our house our! Now we should compose the plan the rescuing of slaves and prisoners of stone quarry. Belth selected this place because it is rich in rare minerals. If we save these slaves, I it is confident, caravan will be on our side. Without their aid we will starve…</body></html>"
            if hellboundLevel == 6: return "<html><body>Buron:<br>Darion's influence has grown weak in the face of the united opposition of the newcomers from the continent, the natives and the Caravan. His army has already abandoned most of Hellbound to the Resistance. Still, Darion's forces are doing everything possible to defeat the Magical Field. I wonder -- is there something important buried in there?</body></html>"
            if hellboundLevel == 7: return "<html><body>Buron:<br>I know, why even greedy to the money caravans fear to go into the covered with wounds plateau. As generally it can exist this terrible place… I myself was not there, but everyone indicates that the plateau by terrible monsters. They indicate moreover that in these monsters it is possible to obtain the magic beverages, for which strange magicians pay well. We would be very they are grateful, if you reached for us several.</body></html>"
            if hellboundLevel == 8: return "<html><body>Buron:<br>It's amazing! I heard, that the inhabitants of continent and village Resistances combined their forces and drove off devils to the external gates. Gates captain guards, but we will break through defense because of the numerical superiority!</body></html>"
            if hellboundLevel == 9: return "<html><body>Buron:<br>I heard, that the defense of external gates fall, and our forces systematically move into the Steel Citadel! Walks rumor, that the gnome, whom projected steel citadel, it still lives somewhere in the lock. Many of our brothers also live in the oasis or in the city, located in the limits of the walls lock. If you please, save them! If you have one of Darion's Badges, you will be able to travel everywhere without any limitations.</body></html>"
            if hellboundLevel == 10: return "<html><body>Buron:<br></body>Steel Citadel occupied by the monsters, which it is necessary to destroy level after the level. In order to take steel citadel, you must all kill them!</html>"
            if hellboundLevel == 11: return "<html><body>Buron:<br></body>Now, need to defeat Darion and Beleth - and return peace to the Hellbound Island.</html>"

QUEST = Buron(-1, "Buron", "custom")

QUEST.addStartNpc(BuronNpc)
QUEST.addTalkId(BuronNpc)
QUEST.addFirstTalkId(BuronNpc)