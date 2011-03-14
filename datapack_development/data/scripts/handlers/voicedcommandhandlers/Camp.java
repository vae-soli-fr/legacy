package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.datatables.NpcTable;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.idfactory.IdFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2TerrainObjectInstance;
import com.l2jserver.gameserver.util.Util;
import static com.l2jserver.gameserver.model.actor.L2Character.ZONE_TOWN;

/**
 * @author Melua
 * Cette classe implémente la commande .fire
 */
public class Camp implements IVoicedCommandHandler {

    private static final String[] _voicedCommands = {
        "camp"
    };

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String option) {
        if (command.equalsIgnoreCase("camp")) {
            if (!activeChar.isInsideZone(ZONE_TOWN)) {
                try {
                    int val = Integer.parseInt(option);
                    if (val >= 0 && val <= 6) {
                        if (val == 0) {
                            if (activeChar.getCamp1() != null) {
                                activeChar.getCamp1().deleteMe();
                                activeChar.setCamp1(null);
                            }
                            if (activeChar.getCamp2() != null) {
                                activeChar.getCamp2().deleteMe();
                                activeChar.setCamp2(null);
                            }
                            if (activeChar.getCamp3() != null) {
                                activeChar.getCamp3().deleteMe();
                                activeChar.setCamp3(null);
                            }
                            if (activeChar.getCamp4() != null) {
                                activeChar.getCamp4().deleteMe();
                                activeChar.setCamp4(null);
                            }
                        } else if (val == 1) {
                            if (activeChar.getCamp1() == null) {
                                //spawn le bois
                                activeChar.setCamp1(new L2TerrainObjectInstance(IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(80927)));
                                activeChar.getCamp1().spawnMe(activeChar.getX(), activeChar.getY(), activeChar.getZ());
                            }
                            if (activeChar.getCamp1() != null && activeChar.isInsideRadius(activeChar.getCamp1(), 500, true, false)) {
                                if (activeChar.getCamp2() != null) {
                                    activeChar.getCamp2().deleteMe();
                                    activeChar.setCamp2(null);
                                }
                                if (activeChar.getCamp3() != null) {
                                    activeChar.getCamp3().deleteMe();
                                    activeChar.setCamp3(null);
                                }
                                if (activeChar.getCamp4() != null) {
                                    activeChar.getCamp4().deleteMe();
                                    activeChar.setCamp4(null);
                                }
                            } else {
                                activeChar.sendMessage("Vous êtes trop loin du campement.");
                            }
                        } else if (val == 2 && activeChar.getCamp1() != null) {
                            if (activeChar.isInsideRadius(activeChar.getCamp1(), 500, true, false)) {
                                if (activeChar.getCamp2() == null) {
                                    //spawn les flammes
                                    activeChar.setCamp2(new L2TerrainObjectInstance(IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(80930)));
                                    activeChar.getCamp2().spawnMe(activeChar.getCamp1().getX(), activeChar.getCamp1().getY(), activeChar.getCamp1().getZ());
                                }
                                if (activeChar.getCamp3() != null) {
                                    activeChar.getCamp3().deleteMe();
                                    activeChar.setCamp3(null);
                                }
                                if (activeChar.getCamp4() != null) {
                                    activeChar.getCamp4().deleteMe();
                                    activeChar.setCamp4(null);
                                }
                            } else {
                                activeChar.sendMessage("Vous êtes trop loin du campement.");
                            }
                        } else if (val == 3 && activeChar.getCamp1() != null && activeChar.getCamp2() != null) {
                            if (activeChar.isInsideRadius(activeChar.getCamp1(), 500, true, false)) {
                                if (activeChar.getCamp3() == null) {
                                    //spawn la tente
                                    if (activeChar.isInsideRadius(activeChar.getCamp1(), 250, true, false)) {
                                        activeChar.setCamp3(new L2TerrainObjectInstance(IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(80590)));
                                        activeChar.getCamp3().setHeading(Util.calculateHeadingFrom(activeChar, activeChar.getCamp1()));
                                        activeChar.getCamp3().spawnMe(activeChar.getX(), activeChar.getY(), activeChar.getZ());
                                        
                                    } else {
                                        activeChar.sendMessage("La tente est trop loin du feu.");
                                    }
                                }
                                if (activeChar.getCamp4() != null) {
                                    activeChar.getCamp4().deleteMe();
                                    activeChar.setCamp4(null);
                                }
                            } else {
                                activeChar.sendMessage("Vous êtes trop loin du campement.");
                            }
                        } else if (val == 4 && activeChar.getCamp1() != null && activeChar.getCamp3() != null) {
                            if (activeChar.isInsideRadius(activeChar.getCamp1(), 500, true, false)) {
                                if (activeChar.getCamp2() == null) {
                                    //donc précédent val 5 a unspawn les flammes!
                                    activeChar.setCamp2(new L2TerrainObjectInstance(IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(80930)));
                                    activeChar.getCamp2().spawnMe(activeChar.getCamp1().getX(), activeChar.getCamp1().getY(), activeChar.getCamp1().getZ());
                                }
                                if (activeChar.getCamp4() == null) {
                                    //spawn la bouffe
                                    activeChar.setCamp4(new L2TerrainObjectInstance(IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(80933)));
                                    activeChar.getCamp4().spawnMe(activeChar.getCamp1().getX(), activeChar.getCamp1().getY(), activeChar.getCamp1().getZ());
                                }
                            } else {
                                activeChar.sendMessage("Vous êtes trop loin du campement.");
                            }
                        } else if (val == 5 && activeChar.getCamp1() != null && activeChar.getCamp3() != null) {
                            if (activeChar.isInsideRadius(activeChar.getCamp1(), 500, true, false)) {
                                if (activeChar.getCamp4() == null) {
                                    //donc précédent val 6 a unspawn la bouffe !
                                    activeChar.setCamp4(new L2TerrainObjectInstance(IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(80933)));
                                    activeChar.getCamp4().spawnMe(activeChar.getCamp1().getX(), activeChar.getCamp1().getY(), activeChar.getCamp1().getZ());
                                }
                                if (activeChar.getCamp2() != null) {
                                    //unspawn les flammes
                                    activeChar.getCamp2().deleteMe();
                                    activeChar.setCamp2(null);
                                }
                            } else {
                                activeChar.sendMessage("Vous êtes trop loin du campement.");
                            }
                        } else if (val == 6 && activeChar.getCamp1() != null && activeChar.getCamp3() != null && activeChar.getCamp4() != null && activeChar.getCamp2() == null) {
                            if (activeChar.isInsideRadius(activeChar.getCamp1(), 500, true, false)) {
                                //unspawn la bouffe
                                activeChar.getCamp4().deleteMe();
                                activeChar.setCamp4(null);
                            } else {
                                activeChar.sendMessage("Vous êtes trop loin du campement.");
                            }
                        }
                    } else {
                        activeChar.sendMessage("Usage: .camp <value> (0=off...6=max)");
                    }
                } catch (Exception e) {
                    activeChar.sendMessage("Usage: .camp <value> (0=off...6=max)");
                }
            } else {
                activeChar.sendMessage("Vous ne pouvez pas établir de campement en ville.");
            }

        }
        return true;

    }

    public String[] getVoicedCommandList() {
        return _voicedCommands;
    }
}