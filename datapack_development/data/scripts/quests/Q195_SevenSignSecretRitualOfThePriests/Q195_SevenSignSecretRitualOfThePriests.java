package quests.Q195_SevenSignSecretRitualOfThePriests;

import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * @author Plim
 * Update by pmq High Five 12-06-2011
 */
public class Q195_SevenSignSecretRitualOfThePriests extends Quest {

    private static final String qn = "195_SevenSignSecretRitualOfThePriests";
    //NPCs
    private static final int CLAUDIA = 31001;
    private static final int JOHN = 32576;
    private static final int RAYMOND = 30289;
    private static final int IASON_HEINE = 30969;
    private static final int SHELF = 32580;
    //ITEMS
    private static final int SHUNAIMAN_CONTRACT = 13823;
    private static final int IDENTITY_CARD = 13822;
    //SKILLS
    private static final int GUARD_DAWN = 6204;

    @Override
    public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
        String htmltext = event;
        QuestState st = player.getQuestState(qn);

        if (st == null) {
            return htmltext;
        }

        final int cond = st.getInt("cond");
        if (event.equalsIgnoreCase("31001-05.htm")) {
            if (cond == 0) {
                st.set("cond", "1");
                st.setState(State.STARTED);
                st.playSound("ItemSound.quest_accept");
            }
        } else if (event.equalsIgnoreCase("32576-02.htm")) {
            if (cond == 1) {
                st.giveItems(IDENTITY_CARD, 1);
                st.set("cond", "2");
                st.playSound("ItemSound.quest_middle");
            }
        } else if (event.equalsIgnoreCase("30289-04.htm")) {
            if (cond == 2) {
                st.set("cond", "3");
            }
            player.stopAllEffects();
            SkillTable.getInstance().getInfo(GUARD_DAWN, 1).getEffects(player, player);

        } else if (event.equalsIgnoreCase("30289-07.htm")) {
            if (cond == 3) {
                player.stopAllEffects();
            }
        } else if (event.equalsIgnoreCase("30289-08.htm")) {
            if (cond == 3) {
                st.set("cond", "4");
                st.playSound("ItemSound.quest_middle");
                player.stopAllEffects();
            }
        } else if (event.equalsIgnoreCase("30969-03.htm")) {
            if (cond == 4) {
                st.addExpAndSp(25000000, 2500000);
                st.unset("cond");
                st.exitQuest(false);
                st.playSound("ItemSound.quest_finish");
            }
        } else if (event.equalsIgnoreCase("32580-02.htm")) {
            if (cond == 3 && !st.hasQuestItems(SHUNAIMAN_CONTRACT)) {
                st.giveItems(SHUNAIMAN_CONTRACT, 1);
                st.playSound("ItemSound.quest_middle");
            }
        }

        return event;
    }

    @Override
    public final String onTalk(L2Npc npc, L2PcInstance player) {
        String htmltext = getNoQuestMsg(player);
        final QuestState st = player.getQuestState(qn);
        final QuestState contractOfMammon = player.getQuestState("194_SevenSignContractOfMammon");

        if (st == null) {
            return htmltext;
        }

        final int cond = st.getInt("cond");
        switch (npc.getNpcId()) {
            case CLAUDIA:
                switch (st.getState()) {
                    case State.CREATED:
                        if (cond == 0 && player.getLevel() >= 79 && contractOfMammon.getState() == State.COMPLETED) {
                            htmltext = "31001-01.htm";
                        } else {
                            st.exitQuest(true);
                            htmltext = "31001-0a.htm";
                        }
                        break;
                    case State.STARTED:
                        if (cond == 1) {
                            htmltext = "31001-06.htm";
                        }
                        break;
                    case State.COMPLETED:
                        htmltext = getAlreadyCompletedMsg(player);
                        break;
                }
                break;
            case JOHN:
                switch (st.getInt("cond")) {
                    case 1:
                        htmltext = "32576-01.htm";
                        break;
                    case 2:
                        htmltext = "32576-03.htm";
                        break;
                }
                break;
            case RAYMOND:
                switch (st.getInt("cond")) {
                    case 1:
                    case 2:
                        htmltext = "30289-01.htm";
                        break;
                    case 3:
                        if (st.hasQuestItems(SHUNAIMAN_CONTRACT)) {
                            htmltext = "30289-08.htm";
                        } else {
                            htmltext = "30289-06.htm";
                        }
                        break;
                }
                break;
            case IASON_HEINE:
                if (cond == 4) {
                    htmltext = "30969-01.htm";
                }
                break;
            case SHELF:
                if (cond == 3) {
                    htmltext = "32580-01.htm";
                }
                break;
        }
        return htmltext;
    }

    public Q195_SevenSignSecretRitualOfThePriests(int questId, String name, String descr) {
        super(questId, name, descr);

        addStartNpc(CLAUDIA);
        addTalkId(CLAUDIA);
        addTalkId(JOHN);
        addTalkId(RAYMOND);
        addTalkId(IASON_HEINE);
        addTalkId(SHELF);

        questItemIds = new int[]{SHUNAIMAN_CONTRACT, IDENTITY_CARD};
    }

    public static void main(String[] args) {
        new Q195_SevenSignSecretRitualOfThePriests(195, qn, "Seven Sign Secret Ritual Of The Priests");
    }
}