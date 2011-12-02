package custom.CertificationEx;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.StatusUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.Util;
import java.util.StringTokenizer;

/**
 * @author Melua
 */
public class CertificationEx extends Quest {

    private static final int[] SKILLITEMS = {
        10281, // Certificate - Warrior Ability
        10282, // Certificate - Knight Ability
        10283, // Certificate - Rogue Ability
        10284, // Certificate - Wizard Ability
        10285, // Certificate - Healer Ability
        10286, // Certificate - Summoner Ability
        10287, // Certificate - Enchanter Ability
    };
    private static final int[] KNIGHTCLASSES = {5, 90, 6, 91, 20, 99, 33, 106};
    private static final int[] SUMMONERCLASSES = {14, 96, 28, 104, 41, 111};
    private static final int _npc = 513002;

    public CertificationEx(int id, String name, String descr) {
        super(id, name, descr);
        addFirstTalkId(_npc); // means that once you double-click on the NPC, it will run the onFirsTalk part of the quest.
        addStartNpc(_npc); // will run the onTalk part.
        addTalkId(_npc); // will run the onEvent and onAdvEvent parts.

    }

    @Override
    public String onFirstTalk(L2Npc npc, L2PcInstance player) {
        return "main.htm";
    }

    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {

        // doit être en main class
        if (player.getClassIndex() != 0) {
            return "class.htm";
        } // quelle deuxième page afficher
        else if (event.startsWith("ask")) {
            if (Util.contains(KNIGHTCLASSES, player.getClassId().getId())) {
                return "knightlist.htm";
            } else if (Util.contains(SUMMONERCLASSES, player.getClassId().getId())) {
                return "summonlist.htm";
            } else {
                return "defaultlist.htm";
            }
        } // effectuer l'échange
        else if (event.startsWith("change")) {

            StringTokenizer st = new StringTokenizer(event, " ");
            st.nextToken();
            int itemId = Integer.parseInt(st.nextToken());
            int rewardId = Integer.parseInt(st.nextToken());

            // security check
            if (rewardId == 10282 && Util.contains(KNIGHTCLASSES, player.getClassId().getId())) {
                return "knightlist.htm";
            } else if (rewardId == 10286 && Util.contains(SUMMONERCLASSES, player.getClassId().getId())) {
                return "summonlist.htm";
            }

            L2ItemInstance certItem, rewardItem;
            QuestState qt = player.getQuestState(CertificationEx.class.getSimpleName());
            String qName, qValue;
            int objectId, certId;
            for (int j = Config.MAX_SUBCLASS; j > 0; j--) {
                qName = "ClassAbility75-" + String.valueOf(j);
                qValue = qt.getGlobalQuestVar(qName);
                if (qValue == null || qValue.isEmpty()) {
                    continue;
                }

                if (!qValue.endsWith(";")) // not a skill
                {
                    objectId = Integer.parseInt(qValue);
                    if (objectId == 0) // canceled skill, no item
                    {
                        continue;
                    }

                    if (player.getInventory().getInventoryItemCount(5575, 0) < 2000000) // not enough AA
                    {
                        continue;
                    }

                    certItem = player.getInventory().getItemByObjectId(objectId);
                    if (certItem != null) {
                        certId = certItem.getItemId();
                        if (certId == itemId && Util.contains(SKILLITEMS, certId)) {
                            qt.takeItems(5575, 2000000);
                            qt.takeItems(certId, 1);
                            rewardItem = player.getInventory().addItem("CertificationEx", rewardId, 1, player, player.getTarget());
                            qt.saveGlobalQuestVar(qName, Integer.toString(rewardItem.getObjectId()));
                            StatusUpdate su = new StatusUpdate(player);
                            su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
                            player.sendPacket(su);
                            player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1).addItemName(rewardItem));
                            return null;
                        }
                    }
                }

            }
            return "error.htm";
        }
        return "main.htm";

    }

    public static final void main(String[] args) {
        new CertificationEx(-1, "CertificationEx", "custom");
    }
}