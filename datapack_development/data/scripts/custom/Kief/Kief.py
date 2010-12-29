import sys
from java.lang import System
from com.l2jserver import L2DatabaseFactory
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest
from com.l2jserver.gameserver.instancemanager import HellboundManager

kief = 32354

class Kief (JQuest):

    def __init__(self, id, name, descr):
        JQuest.__init__(self, id, name, descr)
        self.checked = 0
        self.points = 0
        try:
            self.points = int(self.loadGlobalQuestVar("life_points"))
        except:
            pass
        self.saveGlobalQuestVar("life_points", str(self.points))
        self.hellboundLevel = HellboundManager.getInstance().getLevel()
        if self.hellboundLevel == 7: self.startQuestTimer("CheckPoints", 180000, None, None, True)

    def onFirstTalk (self, npc, player):
        npcId = npc.getNpcId()
        st = player.getQuestState("Kief")
        if not st:
            st = self.newQuestState(player)
        hellboundLevel = HellboundManager.getInstance().getLevel()
        if hellboundLevel < 3: return "<html><body>Kief:<br>Who are you? Do not approach to me!!!!</body></html>"
        if hellboundLevel == 3 :
            return "32354.htm"
        if hellboundLevel == 4: return "<html><body>Kief:<br>Walk rumours, that Derek appeared in Ruins of Ancient Temple. Did not I see him by the eyes, as do you think, is it a true?</body></html>"
        if hellboundLevel == 5: return "<html><body>Kief:<br>We, finally, came home. I am so happy.</body></html>"
        if hellboundLevel == 6: return "<html><body>Kief:<br>Due to the newcomers from the continent, Darion's military power has been weakened. But it is rumored that they are attempting to after the Magic Fields.</body></html>"
        if hellboundLevel == 7: return "32354-1.htm"
        if hellboundLevel >= 8: return "32354-2.htm"

    def onAdvEvent (self, event, npc, player):
        if player:
            st = player.getQuestState("Kief")
        if event == "Badges":
            badges = st.getQuestItemsCount(9674)
            if badges < 1: return "<html><body>Kief:<br>I can not find the Darion's Badges.</body></html>"
            if badges > 1:
                trustam = 10 * badges
                HellboundManager.getInstance().increaseTrust(trustam)
                return "<html><body>Kief:<br>Thank you. Your help in the Beleth's banishment from this place.</body></html>"
        if event == "bottle":
            return "32354-2.htm"
        if event == "getbottle":
            stinger = st.getQuestItemsCount(10012)
            if stinger < 20: return "<html><body>Kief:<br>Excuse me, but that you brought it is not sufficient.</body></html>"
            st.takeItems(10012, 20)
            st.giveItems(9672, 1)
            return "<html><body>Kief:<br>Take.</body></html>"
        if event == "dlf":
            dimlf = st.getQuestItemsCount(9680)
            if dimlf < 1: return "<html><body>Kief:<br>Excuse me, but you brought nothing.</body></html>"
            trustam = 10 * dimlf
            st.takeItems(9680, dimlf)
            HellboundManager.getInstance().increaseTrust(trustam)
            self.points += trustam
            self.saveGlobalQuestVar("life_points", str(self.points))
            return "<html><body>Kief:<br>Thank you. Your help in the Beleth's banishment from this place.</body></html>"
        if event == "lf":
            lifef = st.getQuestItemsCount(9681)
            if lifef < 1: return "<html><body>Kief:<br>Excuse me, but you brought nothing.</body></html>"
            trustam = 20 * lifef
            st.takeItems(9681, lifef)
            HellboundManager.getInstance().increaseTrust(trustam)
            self.points += trustam
            self.saveGlobalQuestVar("life_points", str(self.points))
            return "<html><body>Kief:<br>Thank you. Your help in the Beleth's banishment from this place.</body></html>"
        if event == "clf":
            conlf = st.getQuestItemsCount(9682)
            if conlf < 1: return "<html><body>Kief:<br>Excuse me, but you brought nothing.</body></html>"
            trustam = 50 * conlf
            st.takeItems(9682, conlf)
            HellboundManager.getInstance().increaseTrust(trustam)
            self.points += trustam
            self.saveGlobalQuestVar("life_points", str(self.points))
            return "<html><body>Kief:<br>Thank you. Your help in the Beleth's banishment from this place.</body></html>"
        if event == "CheckPoints":
            if self.checked == 0 and self.points >= 1000000:
                self.checked = 1
                HellboundManager.getInstance().changeLevel(8)
                self.points = 0
                self.saveGlobalQuestVar("life_points", str(self.points))
                self.cancelQuestTimers("CheckPoints")

QUEST = Kief(-1, "Kief", "custom")

QUEST.addStartNpc(kief)
QUEST.addTalkId(kief)
QUEST.addFirstTalkId(kief)