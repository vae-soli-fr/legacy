package custom.CertificationEx;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.util.Util;
import java.util.StringTokenizer;

/**
 * @author Melua
 */
public class CertificationEx extends Quest {

    private static final int[] SKILLITEMS = {10280, 10281, 10282, 10283, 10284, 10285, 10286, 10287, 10288, 10289, 10290, 10291, 10292, 10293, 10294, 10612};
    private static final String[] QUESTVARSITEMS = {"EmergentAbility65-", "EmergentAbility70-", "ClassAbility75-", "ClassAbility80-"};
    private static final int[] KNIGHTCLASSES = {5, 90, 6, 91, 20, 99, 33, 106};
    private static final int[] SUMMONERCLASSES = {14, 96, 28, 104, 41, 111};

    public CertificationEx(int id, String name, String descr) {
        super(id, name, descr);
    }

    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {

        // doit être en main class
        if (player.getClassIndex() != 0) {
            return "main.htm";
        }

        // quelle deuxième page afficher
        else if (event.startsWith("ask")) {
            if (Util.contains(KNIGHTCLASSES, player.getClassId().getId()))
                return "knightlist.htm";
            else if (Util.contains(KNIGHTCLASSES, player.getClassId().getId()))
                return "summonlist.htm";
            else
                return "defaultlist.htm";
        }

        // effectuer l'échange
        else if (event.startsWith("change")) {

            StringTokenizer st = new StringTokenizer(event);
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
            for (int i = QUESTVARSITEMS.length; --i >= 0;) {
                for (int j = Config.MAX_SUBCLASS; j > 0; j--) {
                    qName = QUESTVARSITEMS[i] + String.valueOf(j);
                    qValue = qt.getGlobalQuestVar(qName);

                    if (!qValue.endsWith(";")) // not a skill
                    {
                        objectId = Integer.parseInt(qValue);
                        certItem = player.getInventory().getItemByObjectId(objectId);
                        if (certItem != null) {
                            certId = certItem.getItemId();
                            if (certId == itemId && Util.contains(SKILLITEMS, certId)) {
                                qt.takeItems(57, 2000000);
                                qt.takeItems(certId, 1);
                                rewardItem = player.getInventory().addItem("CertificationEx", rewardId, 1, player, player.getTarget());
                                qt.saveGlobalQuestVar(qName, Integer.toString(rewardItem.getObjectId()));
                                return "done.htm";
                            }
                        }
                    }

                }

            }
        }
        return "defaultlist.html";

    }

    public static final void main(String[] args) {
        new CertificationEx(-1, "CertificationEx", "custom");
    }
}