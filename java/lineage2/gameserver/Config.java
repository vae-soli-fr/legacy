/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.commons.configuration.ExProperties;
import lineage2.commons.net.nio.impl.SelectorConfig;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.model.actor.instances.player.Bonus;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.base.PlayerAccess;
import lineage2.gameserver.network.loginservercon.ServerType;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Config
{
	private static final Logger _log = LoggerFactory.getLogger(Config.class);
	private static final int NCPUS = Runtime.getRuntime().availableProcessors();
	private static final String OTHER_CONFIG_FILE = "config/Other.ini";
	private static final String RESIDENCE_CONFIG_FILE = "config/Residence.ini";
	private static final String SPOIL_CONFIG_FILE = "config/Spoil.ini";
	private static final String GENERAL_CONFIG_FILE = "config/General.ini";
	private static final String FORMULAS_CONFIG_FILE = "config/Formulas.ini";
	private static final String PVP_CONFIG_FILE = "config/PvP.ini";
	private static final String SERVER_CONFIG_FILE = "config/Server.ini";
	private static final String RATES_CONFIG_FILE = "config/Rates.ini";
	private static final String NPC_CONFIG_FILE = "config/Npc.ini";
	private static final String GEODATA_CONFIG_FILE = "config/Geodata.ini";
	private static final String EVENTS_CONFIG_FILE = "config/Events.ini";
	private static final String SERVICES_CONFIG_FILE = "config/Services.ini";
	public static final String OLYMPIAD_CONFIG_FILE = "config/Olympiad.ini";
	private static final String DEVELOP_CONFIG_FILE = "config/Develop.ini";
	private static final String EXT_CONFIG_FILE = "config/ext.ini";
	private static final String VOTE_REWARD_CONFIG_FILE = "config/VoteReward.ini";
	private static final String DONATE_CONFIG_FILE = "config/Donate.ini";
	private static final String COMMUNITY_CONFIG_FILE = "config/CommunityBoard.ini";
	private static final String ABUSEWORDS_CONFIG_FILE = "config/Abusewords.txt";
	public static final String FAKE_PLAYERS_LIST = "config/FakePlayers.txt";
	public static final String PLAYER_ACCESS_FILES_DIR = "config/xml/AccessLevels/";
	public static int HTM_CACHE_MODE;
	public static boolean HTM_DEBUG_MODE;
	public static int[] PORTS_GAME;
	static String GAMESERVER_HOSTNAME;
	public static String DATABASE_DRIVER;
	public static int DATABASE_MAX_CONNECTIONS;
	public static int DATABASE_MAX_IDLE_TIMEOUT;
	public static int DATABASE_IDLE_TEST_PERIOD;
	public static String DATABASE_URL;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;
	public static boolean AUTOSAVE;
	public static int EFFECT_TASK_MANAGER_COUNT;
	public static int MAXIMUM_ONLINE_USERS;
	public static boolean DONTLOADSPAWN;
	public static boolean DONTLOADQUEST;
	public static int MAX_REFLECTIONS_COUNT;
	public static int SHIFT_BY;
	public static int SHIFT_BY_Z;
	public static int MAP_MIN_Z;
	public static int MAP_MAX_Z;
	public static boolean SHOW_NPC_LVL;
	public static boolean ALLOW_PACKET_FAIL;
	public static boolean RWHO_LOG;
	// public static int RWHO_FORCE_INC;
	// public static int RWHO_KEEP_STAT;
	// public static int RWHO_MAX_ONLINE;
	// public static boolean RWHO_SEND_TRASH;
	// static int RWHO_ONLINE_INCREMENT;
	// public static float RWHO_PRIV_STORE_FACTOR;
	private static final int RWHO_ARRAY[] = new int[13];
	public static int CHAT_MESSAGE_MAX_LEN;
	public static boolean ABUSEWORD_BANCHAT;
	public static final int[] BAN_CHANNEL_LIST = new int[18];
	public static boolean ABUSEWORD_REPLACE;
	public static String ABUSEWORD_REPLACE_STRING;
	public static int ABUSEWORD_BANTIME;
	private static Pattern[] ABUSEWORD_LIST = {};
	public static boolean BANCHAT_ANNOUNCE;
	public static boolean BANCHAT_ANNOUNCE_FOR_ALL_WORLD;
	public static boolean BANCHAT_ANNOUNCE_NICK;
	public static final int[] CHATFILTER_CHANNELS = new int[18];
	public static int CHATFILTER_MIN_LEVEL = 0;
	public static int CHATFILTER_WORK_TYPE = 1;
	public static boolean SAVING_SPS;
	public static boolean MANAHEAL_SPS_BONUS;
	public static int ALT_ADD_RECIPES;
	public static int ALT_MAX_ALLY_SIZE;
	public static int ALT_PARTY_DISTRIBUTION_RANGE;
	public static double[] ALT_PARTY_BONUS;
	public static double ALT_ABSORB_DAMAGE_MODIFIER;
	public static boolean ALT_ABSORB_DAMAGE_ONLY_MEELE;
	// public static boolean ALT_ALL_PHYS_SKILLS_OVERHIT;
	public static double ALT_POLE_DAMAGE_MODIFIER;
	public static boolean ALT_REMOVE_SKILLS_ON_DELEVEL;
	public static boolean ALT_VITALITY_ENABLED;
	public static double ALT_VITALITY_RATE;
	public static double ALT_VITALITY_CONSUME_RATE;
	public static final int MAX_VITALITY = 140000;
	public static Calendar CASTLE_VALIDATION_DATE;
	public static int[] CASTLE_SELECT_HOURS;
	public static boolean ALT_PCBANG_POINTS_ENABLED;
	public static double ALT_PCBANG_POINTS_BONUS_DOUBLE_CHANCE;
	public static int ALT_PCBANG_POINTS_BONUS;
	public static int ALT_PCBANG_POINTS_DELAY;
	public static int ALT_PCBANG_POINTS_MIN_LVL;
	public static boolean ALT_DEBUG_ENABLED;
	public static boolean ALT_DEBUG_PVP_ENABLED;
	public static boolean ALT_DEBUG_PVP_DUEL_ONLY;
	public static boolean ALT_DEBUG_PVE_ENABLED;
	// public static double CRAFT_MASTERWORK_CHANCE;
	// public static double CRAFT_DOUBLECRAFT_CHANCE;
	static int SCHEDULED_THREAD_POOL_SIZE;
	static int EXECUTOR_THREAD_POOL_SIZE;
	// public static int THREAD_P_MOVE;
	// public static int NPC_AI_MAX_THREAD;
	// public static int PLAYER_AI_MAX_THREAD;
	// public static int THREAD_P_PATHFIND;
	static boolean ENABLE_RUNNABLE_STATS;
	public static boolean L2_TOP_MANAGER_ENABLED;
	public static int L2_TOP_MANAGER_INTERVAL;
	public static String L2_TOP_WEB_ADDRESS;
	public static String L2_TOP_SMS_ADDRESS;
	public static String L2_TOP_SERVER_ADDRESS;
	public static int L2_TOP_SAVE_DAYS;
	public static int[] L2_TOP_REWARD;
	// public static String L2_TOP_SERVER_PREFIX;
	public static int[] L2_TOP_REWARD_NO_CLAN;
	public static boolean MMO_TOP_MANAGER_ENABLED;
	public static int MMO_TOP_MANAGER_INTERVAL;
	public static String MMO_TOP_WEB_ADDRESS;
	// public static String MMO_TOP_SERVER_ADDRESS;
	public static int MMO_TOP_SAVE_DAYS;
	public static int[] MMO_TOP_REWARD;
	public static int[] MMO_TOP_REWARD_NO_CLAN;
	public static boolean SMS_PAYMENT_MANAGER_ENABLED;
	public static String SMS_PAYMENT_WEB_ADDRESS;
	public static int SMS_PAYMENT_MANAGER_INTERVAL;
	public static int SMS_PAYMENT_SAVE_DAYS;
	public static String SMS_PAYMENT_SERVER_ADDRESS;
	public static int[] SMS_PAYMENT_REWARD;
	public static int[] SMS_PAYMENT_REWARD_NO_CLAN;
	public static boolean SMS_PAYMENT_TYPE;
	// public static String SMS_PAYMENT_PREFIX;
	// public static boolean AUTH_SERVER_GM_ONLY;
	// public static boolean AUTH_SERVER_BRACKETS;
	// public static boolean AUTH_SERVER_IS_PVP;
	// public static int AUTH_SERVER_AGE_LIMIT;
	// public static int AUTH_SERVER_SERVER_TYPE;
	static final SelectorConfig SELECTOR_CONFIG = new SelectorConfig();
	public static boolean AUTO_LOOT;
	public static boolean AUTO_LOOT_HERBS;
	public static boolean AUTO_LOOT_INDIVIDUAL;
	public static boolean AUTO_LOOT_FROM_RAIDS;
	public static boolean AUTO_LOOT_PK;
	public static String CNAME_TEMPLATE;
	public static final int CNAME_MAXLEN = 32;
	public static String CLAN_NAME_TEMPLATE;
	public static String CLAN_TITLE_TEMPLATE;
	public static String ALLY_NAME_TEMPLATE;
	public static boolean GLOBAL_SHOUT;
	public static boolean GLOBAL_TRADE_CHAT;
	public static int CHAT_RANGE;
	public static int SHOUT_OFFSET;
	public static boolean PREMIUM_HEROCHAT;
	public static boolean EVERYONE_HAS_ADMIN_RIGHTS;
	public static int DEFAULT_ACCESS_FOR_EVERYONE;
	public static boolean SECOND_AUTH_ENABLED;
	public static int SECOND_AUTH_MAX_ATTEMPTS;
	public static int SECOND_AUTH_BAN_TIME;
	public static boolean DEBUG_SPAWN_MANAGER;
	public static boolean DEBUG_EVENT_SCHEDULES;
	public static double ALT_RAID_RESPAWN_MULTIPLIER;
	public static boolean ALT_ALLOW_AUGMENT_ALL;
	public static boolean ALT_ALLOW_DROP_AUGMENTED;
	public static boolean ALT_GAME_UNREGISTER_RECIPE;
	// public static int SS_ANNOUNCE_PERIOD;
	public static boolean PETITIONING_ALLOWED;
	public static int MAX_PETITIONS_PER_PLAYER;
	public static int MAX_PETITIONS_PENDING;
	public static boolean ALT_GAME_SHOW_DROPLIST;
	public static boolean ALT_FULL_NPC_STATS_PAGE;
	public static boolean ALLOW_NPC_SHIFTCLICK;
	public static boolean ALT_ALLOW_SELL_COMMON;
	public static boolean ALT_ALLOW_SHADOW_WEAPONS;
	public static int[] ALT_DISABLED_MULTISELL;
	public static int[] ALT_SHOP_PRICE_LIMITS;
	public static int[] ALT_SHOP_UNALLOWED_ITEMS;
	public static int[] ALT_ALLOWED_PET_POTIONS;
	public static double SKILLS_CHANCE_MOD;
	public static double SKILLS_CHANCE_MIN;
	public static double SKILLS_CHANCE_POW;
	public static double SKILLS_CHANCE_CAP;
	public static boolean ALT_SAVE_UNSAVEABLE;
	public static int ALT_SAVE_EFFECTS_REMAINING_TIME;
	public static boolean ALT_SHOW_REUSE_MSG;
	public static boolean ALT_DELETE_SA_BUFFS;
	public static int SKILLS_CAST_TIME_MIN;
	public static boolean CHAR_TITLE;
	public static String ADD_CHAR_TITLE;
	public static boolean ALT_SOCIAL_ACTION_REUSE;
	public static boolean ALT_DISABLE_SPELLBOOKS;
	public static boolean ALT_DELETE_SKILL_PROF;
	public static boolean ALT_DELETE_SKILL_RELATION;
	public static boolean ALT_DELETE_AWAKEN_SKILL_FROM_DB;
	public static boolean ALT_CHECK_SKILLS_AWAKENING;
	public static boolean ALT_GAME_EXP_LOST;
	public static boolean ALT_ARENA_EXP;
	public static int ALT_GAME_LEVEL_TO_GET_SUBCLASS;
	public static int ALT_MAX_LEVEL;
	public static int ALT_MAX_SUB_LEVEL;
	public static int ALT_MAX_DUAL_SUB_LEVEL;
	public static boolean ALT_GAME_SUB_BOOK;
	public static double[] ALT_GAME_DUALCLASS_REAWAKENING_COST;
	public static int ALT_GAME_RESET_CERTIFICATION_COST;
	public static int ALT_GAME_RESET_DUALCERTIFICATION_COST;
	public static boolean ALT_GAME_REMOVE_PREVIOUS_CERTIFICATES;
	public static boolean ALT_NO_LASTHIT;
	public static boolean ALT_KAMALOKA_NIGHTMARES_PREMIUM_ONLY;
	// public static boolean ALT_KAMALOKA_NIGHTMARE_REENTER;
	// public static boolean ALT_KAMALOKA_ABYSS_REENTER;
	// public static boolean ALT_KAMALOKA_LAB_REENTER;
	public static boolean ALT_PET_HEAL_BATTLE_ONLY;
	public static boolean ALT_SIMPLE_SIGNS;
	public static boolean ALT_TELE_TO_CATACOMBS;
	public static boolean ALT_BS_CRYSTALLIZE;
	// public static int ALT_MAMMON_EXCHANGE;
	// public static int ALT_MAMMON_UPGRADE;
	public static boolean ALT_ALLOW_TATTOO;
	public static int ALT_BUFF_LIMIT;
	public static int MULTISELL_SIZE;
	// public static boolean SERVICES_CHANGE_NICK_ENABLED;
	public static int SERVICES_CHANGE_NICK_PRICE;
	public static int SERVICES_CHANGE_NICK_ITEM;
	// public static boolean SERVICES_CHANGE_CLAN_NAME_ENABLED;
	public static int SERVICES_CHANGE_CLAN_NAME_PRICE;
	public static int SERVICES_CHANGE_CLAN_NAME_ITEM;
	public static boolean SERVICES_CHANGE_PET_NAME_ENABLED;
	public static int SERVICES_CHANGE_PET_NAME_PRICE;
	public static int SERVICES_CHANGE_PET_NAME_ITEM;
	public static boolean SERVICES_EXCHANGE_BABY_PET_ENABLED;
	public static int SERVICES_EXCHANGE_BABY_PET_PRICE;
	public static int SERVICES_EXCHANGE_BABY_PET_ITEM;
	// public static boolean SERVICES_CHANGE_SEX_ENABLED;
	public static int SERVICES_CHANGE_SEX_PRICE;
	public static int SERVICES_CHANGE_SEX_ITEM;
	// public static boolean SERVICES_CHANGE_BASE_ENABLED;
	public static int SERVICES_CHANGE_BASE_PRICE;
	public static int SERVICES_CHANGE_BASE_ITEM;
	// public static boolean SERVICES_SEPARATE_SUB_ENABLED;
	public static int SERVICES_SEPARATE_SUB_PRICE;
	public static int SERVICES_SEPARATE_SUB_ITEM;
	// public static boolean SERVICES_CHANGE_NICK_COLOR_ENABLED;
	public static int SERVICES_CHANGE_NICK_COLOR_PRICE;
	public static int SERVICES_CHANGE_NICK_COLOR_ITEM;
	public static String[] SERVICES_CHANGE_NICK_COLOR_LIST;
	public static boolean SERVICES_BASH_ENABLED;
	public static boolean SERVICES_BASH_SKIP_DOWNLOAD;
	public static int SERVICES_BASH_RELOAD_TIME;
	public static int SERVICES_RATE_TYPE;
	public static int[] SERVICES_RATE_BONUS_PRICE;
	public static int[] SERVICES_RATE_BONUS_ITEM;
	public static double[] SERVICES_RATE_BONUS_VALUE;
	public static int[] SERVICES_RATE_BONUS_DAYS;
	// public static boolean SERVICES_NOBLESS_SELL_ENABLED;
	public static int SERVICES_NOBLESS_SELL_PRICE;
	public static int SERVICES_NOBLESS_SELL_ITEM;
	public static boolean SERVICES_HERO_SELL_ENABLED;
	public static int[] SERVICES_HERO_SELL_DAY;
	public static int[] SERVICES_HERO_SELL_PRICE;
	public static int[] SERVICES_HERO_SELL_ITEM;
	public static boolean SERVICES_EXPAND_INVENTORY_ENABLED;
	public static int SERVICES_EXPAND_INVENTORY_PRICE;
	public static int SERVICES_EXPAND_INVENTORY_ITEM;
	public static int SERVICES_EXPAND_INVENTORY_MAX;
	public static boolean SERVICES_EXPAND_WAREHOUSE_ENABLED;
	public static int SERVICES_EXPAND_WAREHOUSE_PRICE;
	public static int SERVICES_EXPAND_WAREHOUSE_ITEM;
	public static boolean SERVICES_EXPAND_CWH_ENABLED;
	public static int SERVICES_EXPAND_CWH_PRICE;
	public static int SERVICES_EXPAND_CWH_ITEM;
	// public static boolean SERVICES_DELEVEL_ENABLED;
	// public static int SERVICES_DELEVEL_PRICE;
	// public static int SERVICES_DELEVEL_ITEM;
	public static String SERVICES_SELLPETS;
	public static boolean SERVICES_OFFLINE_TRADE_ALLOW;
	public static boolean SERVICES_OFFLINE_TRADE_ALLOW_OFFSHORE;
	public static int SERVICES_OFFLINE_TRADE_MIN_LEVEL;
	public static int SERVICES_OFFLINE_TRADE_NAME_COLOR;
	public static int SERVICES_OFFLINE_TRADE_PRICE;
	public static int SERVICES_OFFLINE_TRADE_PRICE_ITEM;
	public static long SERVICES_OFFLINE_TRADE_SECONDS_TO_KICK;
	public static boolean SERVICES_OFFLINE_TRADE_RESTORE_AFTER_RESTART;
	public static boolean SERVICES_GIRAN_HARBOR_ENABLED;
	public static boolean SERVICES_PARNASSUS_ENABLED;
	public static boolean SERVICES_PARNASSUS_NOTAX;
	public static long SERVICES_PARNASSUS_PRICE;
	public static boolean SERVICES_ALLOW_LOTTERY;
	public static int SERVICES_LOTTERY_PRIZE;
	public static int SERVICES_ALT_LOTTERY_PRICE;
	public static int SERVICES_LOTTERY_TICKET_PRICE;
	public static double SERVICES_LOTTERY_5_NUMBER_RATE;
	public static double SERVICES_LOTTERY_4_NUMBER_RATE;
	public static double SERVICES_LOTTERY_3_NUMBER_RATE;
	public static int SERVICES_LOTTERY_2_AND_1_NUMBER_PRIZE;
	public static boolean SERVICES_ALLOW_ROULETTE;
	public static long SERVICES_ROULETTE_MIN_BET;
	public static long SERVICES_ROULETTE_MAX_BET;
	public static boolean ALT_ALLOW_OTHERS_WITHDRAW_FROM_CLAN_WAREHOUSE;
	public static boolean ALT_ALLOW_CLAN_COMMAND_ONLY_FOR_CLAN_LEADER;
	// public static boolean ALT_GAME_REQUIRE_CLAN_CASTLE;
	// public static boolean ALT_GAME_REQUIRE_CASTLE_DAWN;
	// public static boolean ALT_GAME_ALLOW_ADENA_DAWN;
	public static int ALT_CLAN_PLAYER_COUNT_6LVL;
	public static int ALT_CLAN_REP_COUNT_6LVL;
	public static int ALT_CLAN_PLAYER_COUNT_7LVL;
	public static int ALT_CLAN_REP_COUNT_7LVL;
	public static int ALT_CLAN_PLAYER_COUNT_8LVL;
	public static int ALT_CLAN_REP_COUNT_8LVL;
	public static int ALT_CLAN_PLAYER_COUNT_9LVL;
	public static int ALT_CLAN_REP_COUNT_9LVL;
	public static int ALT_CLAN_PLAYER_COUNT_10LVL;
	public static int ALT_CLAN_REP_COUNT_10LVL;
	public static int ALT_CLAN_PLAYER_COUNT_11LVL;
	public static int ALT_CLAN_REP_COUNT_11LVL;
	public static int ALT_OLY_START_TIME;
	public static int ALT_OLY_MIN;
	public static long ALT_OLY_CPERIOD;
	public static long ALT_OLY_WPERIOD;
	public static long ALT_OLY_VPERIOD;
	public static boolean ENABLE_OLYMPIAD;
	public static boolean ENABLE_OLYMPIAD_SPECTATING;
	public static boolean ALT_OLY_DAYS;
	public static int CLASS_GAME_MIN;
	public static int NONCLASS_GAME_MIN;
	// public static int TEAM_GAME_MIN;
	public static int GAME_MAX_LIMIT;
	public static int GAME_CLASSES_COUNT_LIMIT;
	public static int GAME_NOCLASSES_COUNT_LIMIT;
	// public static int GAME_TEAM_COUNT_LIMIT;
	// public static int ALT_OLY_REG_DISPLAY;
	public static int ALT_OLY_BATTLE_REWARD_ITEM;
	public static int ALT_OLY_CLASSED_RITEM_C;
	public static int ALT_OLY_NONCLASSED_RITEM_C;
	public static int ALT_OLY_TEAM_RITEM_C;
	public static int ALT_OLY_COMP_RITEM;
	public static int ALT_OLY_GP_PER_POINT;
	public static int ALT_OLY_HERO_POINTS;
	public static int ALT_OLY_RANK1_POINTS;
	public static int ALT_OLY_RANK2_POINTS;
	public static int ALT_OLY_RANK3_POINTS;
	public static int ALT_OLY_RANK4_POINTS;
	public static int ALT_OLY_RANK5_POINTS;
	public static int OLYMPIAD_STADIAS_COUNT;
	public static int OLYMPIAD_BATTLES_FOR_REWARD;
	public static int OLYMPIAD_POINTS_DEFAULT;
	public static int OLYMPIAD_POINTS_WEEKLY;
	public static boolean OLYMPIAD_OLDSTYLE_STAT;
	public static long NONOWNER_ITEM_PICKUP_DELAY;
	public static boolean LOG_CHAT;
	public static final Map<Integer, PlayerAccess> PLAYER_ACCESS = new HashMap<>();
	public static double RATE_XP;
	public static double RATE_SP;
	public static double RATE_QUESTS_REWARD;
	public static double RATE_QUESTS_DROP;
	public static double RATE_CLAN_REP_SCORE;
	public static int RATE_CLAN_REP_SCORE_MAX_AFFECTED;
	public static double RATE_DROP_ADENA;
	public static double RATE_DROP_ITEMS;
	public static double RATE_DROP_COMMON_ITEMS;
	public static double RATE_DROP_RAIDBOSS;
	public static double RATE_DROP_SPOIL;
	public static int[] NO_RATE_ITEMS;
	public static boolean NO_RATE_EQUIPMENT;
	public static boolean NO_RATE_KEY_MATERIAL;
	public static boolean NO_RATE_RECIPES;
	public static double RATE_DROP_SIEGE_GUARD;
	public static double RATE_MANOR;
	public static boolean RATE_PARTY_MIN;
	public static int RATE_MOB_SPAWN;
	public static int RATE_MOB_SPAWN_MIN_LEVEL;
	public static int RATE_MOB_SPAWN_MAX_LEVEL;
	public static boolean KARMA_DROP_GM;
	public static boolean KARMA_NEEDED_TO_DROP;
	public static int KARMA_DROP_ITEM_LIMIT;
	public static int KARMA_RANDOM_DROP_LOCATION_LIMIT;
	public static double KARMA_DROPCHANCE_BASE;
	public static double KARMA_DROPCHANCE_MOD;
	public static double NORMAL_DROPCHANCE_BASE;
	public static int DROPCHANCE_EQUIPMENT;
	public static int DROPCHANCE_EQUIPPED_WEAPON;
	public static int DROPCHANCE_ITEM;
	public static boolean ENABLE_DYNAMIC_RATES;
	public static double DYN_RATE_XP9;
	public static double DYN_RATE_SP9;
	public static double DYN_RATE_XP19;
	public static double DYN_RATE_SP19;
	public static double DYN_RATE_XP29;
	public static double DYN_RATE_SP29;
	public static double DYN_RATE_XP39;
	public static double DYN_RATE_SP39;
	public static double DYN_RATE_XP49;
	public static double DYN_RATE_SP49;
	public static double DYN_RATE_XP59;
	public static double DYN_RATE_SP59;
	public static double DYN_RATE_XP69;
	public static double DYN_RATE_SP69;
	public static double DYN_RATE_XP79;
	public static double DYN_RATE_SP79;
	public static double DYN_RATE_XP89;
	public static double DYN_RATE_SP89;
	public static double DYN_RATE_XP99;
	public static double DYN_RATE_SP99;
	public static int AUTODESTROY_ITEM_AFTER;
	// public static int AUTODESTROY_PLAYER_ITEM_AFTER;
	public static int DELETE_DAYS;
	// public static int PURGE_BYPASS_TASK_FREQUENCY;
	public static File DATAPACK_ROOT;
	public static double CLANHALL_BUFFTIME_MODIFIER;
	public static double SONGDANCETIME_MODIFIER;
	public static double MAXLOAD_MODIFIER;
	public static double GATEKEEPER_MODIFIER;
	public static boolean ALT_IMPROVED_PETS_LIMITED_USE;
	public static int GATEKEEPER_FREE;
	public static int CRUMA_GATEKEEPER_LVL;
	public static double ALT_CHAMPION_CHANCE1;
	public static double ALT_CHAMPION_CHANCE2;
	public static boolean ALT_CHAMPION_CAN_BE_AGGRO;
	public static boolean ALT_CHAMPION_CAN_BE_SOCIAL;
	public static int ALT_CHAMPION_TOP_LEVEL;
	public static boolean ALLOW_DISCARDITEM;
	public static boolean ALLOW_MAIL;
	public static boolean ALLOW_WAREHOUSE;
	public static boolean ALLOW_WATER;
	public static boolean ALLOW_CURSED_WEAPONS;
	public static boolean DROP_CURSED_WEAPONS_ON_KICK;
	public static boolean ALLOW_NOBLE_TP_TO_ALL;
	public static boolean ALLOW_FAKE_PLAYERS;
	public static boolean ALLOW_TOTAL_ONLINE;
	public static int FAKE_PLAYERS_PERCENT;
	public static int SWIMING_SPEED;
	public static int MIN_PROTOCOL_REVISION;
	public static int MAX_PROTOCOL_REVISION;
	public static int MIN_NPC_ANIMATION;
	public static int MAX_NPC_ANIMATION;
	static String RESTART_AT_TIME;
	public static int GAME_SERVER_LOGIN_PORT;
	// public static boolean GAME_SERVER_LOGIN_CRYPT;
	public static String GAME_SERVER_LOGIN_HOST;
	public static String INTERNAL_HOSTNAME;
	public static String EXTERNAL_HOSTNAME;
	public static boolean SERVER_SIDE_NPC_NAME;
	public static boolean SERVER_SIDE_NPC_TITLE;
	private static String CLASS_MASTERS_PRICE;
	private static String CLASS_MASTERS_PRICE_ITEM;
	public static final int[] CLASS_MASTERS_PRICE_ITEM_LIST = new int[5];
	public static final List<Integer> ALLOW_CLASS_MASTERS_LIST = new ArrayList<>();
	public static final int[] CLASS_MASTERS_PRICE_LIST = new int[5];
	public static boolean ALLOW_EVENT_GATEKEEPER;
	public static boolean ITEM_BROKER_ITEM_SEARCH;
	public static int INVENTORY_MAXIMUM_NO_DWARF;
	public static int INVENTORY_MAXIMUM_DWARF;
	public static int INVENTORY_MAXIMUM_GM;
	public static int QUEST_INVENTORY_MAXIMUM;
	public static int WAREHOUSE_SLOTS_NO_DWARF;
	public static int WAREHOUSE_SLOTS_DWARF;
	public static int WAREHOUSE_SLOTS_CLAN;
	public static int FREIGHT_SLOTS;
	public static double BASE_SPOIL_RATE;
	public static double MINIMUM_SPOIL_RATE;
	public static boolean ALT_SPOIL_FORMULA;
	public static double MANOR_SOWING_BASIC_SUCCESS;
	public static double MANOR_SOWING_ALT_BASIC_SUCCESS;
	public static double MANOR_HARVESTING_BASIC_SUCCESS;
	public static int MANOR_DIFF_PLAYER_TARGET;
	public static double MANOR_DIFF_PLAYER_TARGET_PENALTY;
	public static int MANOR_DIFF_SEED_TARGET;
	public static double MANOR_DIFF_SEED_TARGET_PENALTY;
	public static int KARMA_MIN_KARMA;
	public static int KARMA_SP_DIVIDER;
	public static int KARMA_LOST_BASE;
	public static int MIN_PK_TO_ITEMS_DROP;
	public static boolean DROP_ITEMS_ON_DIE;
	public static boolean DROP_ITEMS_AUGMENTED;
	public static List<Integer> KARMA_LIST_NONDROPPABLE_ITEMS = new ArrayList<>();
	public static int PVP_TIME;
	public static int REPUTATION_COUNT;
	public static int PK_KILLER_NAME_COLOR;
	public static boolean ALT_GAME_KARMA_PLAYER_CAN_SHOP;
	public static int ENCHANT_CHANCE_WEAPON;
	public static int ENCHANT_CHANCE_ARMOR;
	public static int ENCHANT_CHANCE_ACCESSORY;
	public static int ENCHANT_CHANCE_BLESSED_WEAPON;
	public static int ENCHANT_CHANCE_BLESSED_ARMOR;
	public static int ENCHANT_CHANCE_BLESSED_ACCESSORY;
	public static int ENCHANT_CHANCE_CRYSTAL_WEAPON;
	public static int ENCHANT_CHANCE_CRYSTAL_ARMOR;
	public static int ENCHANT_CHANCE_CRYSTAL_ACCESSORY;
	public static int ENCHANT_CHANCE_GIANT_WEAPON;
	public static int ENCHANT_CHANCE_GIANT_ARMOR;
	public static int ENCHANT_CHANCE_GIANT_ACCESSORY;
	public static int ENCHANT_MAX;
	public static int ENCHANT_ATTRIBUTE_STONE_CHANCE;
	public static int ENCHANT_ATTRIBUTE_CRYSTAL_CHANCE;
	public static int ARMOR_OVERENCHANT_HPBONUS_LIMIT;
	public static int SAFE_ENCHANT_COMMON;
	public static int SAFE_ENCHANT_FULL_BODY;
	public static boolean SHOW_ENCHANT_EFFECT_RESULT;
	public static boolean REGEN_SIT_WAIT;
	public static double RATE_RAID_REGEN;
	public static double RATE_RAID_DEFENSE;
	public static double RATE_RAID_ATTACK;
	public static double RATE_EPIC_DEFENSE;
	public static double RATE_EPIC_ATTACK;
	public static int RAID_MAX_LEVEL_DIFF;
	public static boolean PARALIZE_ON_RAID_DIFF;
	public static double ALT_PK_DEATH_RATE;
	public static boolean AWAKING_FREE;
	public static boolean FREE_JUMPS_FOR_ALL;
	public static boolean DEEPBLUE_DROP_RULES;
	public static int DEEPBLUE_DROP_MAXDIFF;
	public static int DEEPBLUE_DROP_RAID_MAXDIFF;
	public static boolean UNSTUCK_SKILL;
	// public static boolean CB_PLAYER_IN_COMBAT;
	// public static boolean CB_PLAYER_ON_EVENT;
	// public static boolean CB_PLAYER_ON_OLYMPIAD;
	// public static boolean ALLOW_CB_SERVICES;
	// public static boolean ALLOW_CB_BUFFER;
	// public static boolean ALLOW_CB_BUFFER_IN_INSTANCE;
	// public static boolean ALLOW_CB_BUFFER_ON_SIEGE;
	// public static int CB_BUFFER_PRICE_ITEM;
	// public static int CB_BUFFER_PRICE;
	// public static int CB_BUFFER_BUFF_TIME;
	// public static boolean ALLOW_CB_SHOP;
	public static double RESPAWN_RESTORE_CP;
	public static double RESPAWN_RESTORE_HP;
	public static double RESPAWN_RESTORE_MP;
	public static int MAX_PVTSTORE_SLOTS_DWARF;
	public static int MAX_PVTSTORE_SLOTS_OTHER;
	public static int MAX_PVTCRAFT_SLOTS;
	public static boolean SENDSTATUS_TRADE_JUST_OFFLINE;
	public static double SENDSTATUS_TRADE_MOD;
	// public static boolean ALLOW_CH_DOOR_OPEN_ON_CLICK;
	public static boolean ALT_CH_ALL_BUFFS;
	public static boolean ALT_CH_ALLOW_1H_BUFFS;
	// public static boolean ALT_CH_SIMPLE_DIALOG;
	// public static int CH_BID_GRADE1_MINCLANLEVEL;
	// public static int CH_BID_GRADE1_MINCLANMEMBERS;
	// public static int CH_BID_GRADE1_MINCLANMEMBERSLEVEL;
	// public static int CH_BID_GRADE2_MINCLANLEVEL;
	// public static int CH_BID_GRADE2_MINCLANMEMBERS;
	// public static int CH_BID_GRADE2_MINCLANMEMBERSLEVEL;
	// public static int CH_BID_GRADE3_MINCLANLEVEL;
	// public static int CH_BID_GRADE3_MINCLANMEMBERS;
	// public static int CH_BID_GRADE3_MINCLANMEMBERSLEVEL;
	public static double RESIDENCE_LEASE_FUNC_MULTIPLIER;
	// public static double RESIDENCE_LEASE_MULTIPLIER;
	public static boolean ACCEPT_ALTERNATE_ID;
	public static int REQUEST_ID;
	public static int GM_NAME_COLOR;
	public static boolean GM_HERO_AURA;
	public static int NORMAL_NAME_COLOR;
	public static int CLANLEADER_NAME_COLOR;
	public static int AI_TASK_MANAGER_COUNT;
	public static long AI_TASK_ATTACK_DELAY;
	public static long AI_TASK_ACTIVE_DELAY;
	public static boolean BLOCK_ACTIVE_TASKS;
	public static boolean ALWAYS_TELEPORT_HOME;
	public static boolean RND_WALK;
	public static int RND_WALK_RATE;
	public static int RND_ANIMATION_RATE;
	public static int AGGRO_CHECK_INTERVAL;
	public static long NONAGGRO_TIME_ONTELEPORT;
	public static int MAX_DRIFT_RANGE;
	public static int MAX_PURSUE_RANGE;
	public static int MAX_PURSUE_UNDERGROUND_RANGE;
	public static int MAX_PURSUE_RANGE_RAID;
	public static boolean ALT_DEATH_PENALTY;
	public static boolean ALLOW_DEATH_PENALTY_C5;
	public static int ALT_DEATH_PENALTY_C5_CHANCE;
	public static int ALT_DEATH_PENALTY_C5_EXPERIENCE_PENALTY;
	public static int ALT_DEATH_PENALTY_C5_KARMA_PENALTY;
	// public static boolean HIDE_GM_STATUS;
	public static boolean SHOW_GM_LOGIN;
	public static boolean SAVE_GM_EFFECTS;
	public static boolean AUTO_LEARN_SKILLS;
	public static boolean AUTO_LEARN_FORGOTTEN_SKILLS;
	public static boolean ENCHANT_SKILLSID_RETAIL;
	public static int MOVE_PACKET_DELAY;
	public static int ATTACK_PACKET_DELAY;
	public static boolean DAMAGE_FROM_FALLING;
	public static boolean USE_BBS_BUFER_IS_COMBAT;
	// public static boolean USE_BBS_BUFER_IS_EVENTS;
	public static boolean USE_BBS_TELEPORT_IS_COMBAT;
	// public static boolean USE_BBS_TELEPORT_IS_EVENTS;
	// public static boolean USE_BBS_PROF_IS_COMBAT;
	// public static boolean USE_BBS_PROF_IS_EVENTS;
	// public static boolean SAVE_BBS_TELEPORT_IS_EPIC;
	// public static boolean SAVE_BBS_TELEPORT_IS_BZ;
	// public static boolean BUFFER_SUMMON_ENABLE;
	public static boolean COMMUNITYBOARD_ENABLED;
	public static boolean ALLOW_COMMUNITYBOARD_IN_COMBAT;
	public static boolean COMMUNITYBOARD_BUFFER_ENABLED;
	public static boolean COMMUNITYBOARD_SHOP_ENABLED;
	// public static boolean COMMUNITYBOARD_BUFFER_PET_ENABLED;
	// public static boolean COMMUNITYBOARD_BUFFER_SAVE_ENABLED;
	// public static boolean COMMUNITYBOARD_ABNORMAL_ENABLED;
	public static boolean COMMUNITYBOARD_INSTANCE_ENABLED;
	public static boolean COMMUNITYBOARD_EVENTS_ENABLED;
	public static int COMMUNITYBOARD_BUFF_TIME;
	public static int COMMUNITYBOARD_BUFF_PICE;
	public static int COMMUNITYBOARD_BUFF_SAVE_PICE;
	public static final List<Integer> COMMUNITYBOARD_BUFF_ALLOW = new ArrayList<>();
	public static final List<Integer> COMMUNITI_LIST_MAGE_SUPPORT = new ArrayList<>();
	public static final List<Integer> COMMUNITI_LIST_FIGHTER_SUPPORT = new ArrayList<>();
	public static final List<Integer> COMMUNITYBOARD_MULTISELL_ALLOW = new ArrayList<>();
	public static String BBS_DEFAULT;
	public static String BBS_HOME_DIR;
	public static boolean COMMUNITYBOARD_TELEPORT_ENABLED;
	public static int COMMUNITYBOARD_TELE_PICE;
	public static int COMMUNITYBOARD_SAVE_TELE_PICE;
	public static boolean ENCHANT_ENABLED;
	public static int ENCHANTER_ITEM_ID;
	public static int BEAUTY_SHOP_COIN_ITEM_ID;
	public static int MAX_ENCHANT;
	public static int[] ENCHANT_LEVELS;
	public static int[] ENCHANT_PRICE_WPN;
	public static int[] ENCHANT_PRICE_ARM;
	public static int[] ENCHANT_ATTRIBUTE_LEVELS;
	public static int[] ENCHANT_ATTRIBUTE_LEVELS_ARM;
	public static int[] ATTRIBUTE_PRICE_WPN;
	public static int[] ATTRIBUTE_PRICE_ARM;
	public static boolean ENCHANT_ATT_PVP;
	public static boolean ALLOW_WEDDING;
	public static int WEDDING_PRICE;
	public static boolean WEDDING_PUNISH_INFIDELITY;
	// public static boolean WEDDING_TELEPORT;
	public static int WEDDING_TELEPORT_PRICE;
	public static int WEDDING_TELEPORT_INTERVAL;
	public static boolean WEDDING_SAMESEX;
	public static boolean WEDDING_FORMALWEAR;
	public static int WEDDING_DIVORCE_COSTS;
	public static int AUGMENTATION_NG_SKILL_CHANCE;
	public static int AUGMENTATION_NG_GLOW_CHANCE;
	public static int AUGMENTATION_MID_SKILL_CHANCE;
	public static int AUGMENTATION_MID_GLOW_CHANCE;
	public static int AUGMENTATION_HIGH_SKILL_CHANCE;
	public static int AUGMENTATION_HIGH_GLOW_CHANCE;
	public static int AUGMENTATION_TOP_SKILL_CHANCE;
	public static int AUGMENTATION_TOP_GLOW_CHANCE;
	public static int AUGMENTATION_BASESTAT_CHANCE;
	public static int AUGMENTATION_ACC_SKILL_CHANCE;
	public static int FOLLOW_RANGE;
	public static boolean ALT_ITEM_AUCTION_ENABLED;
	public static boolean ALT_ITEM_AUCTION_CAN_REBID;
	public static boolean ALT_ITEM_AUCTION_START_ANNOUNCE;
	public static int ALT_ITEM_AUCTION_BID_ITEM_ID;
	public static long ALT_ITEM_AUCTION_MAX_BID;
	public static int ALT_ITEM_AUCTION_MAX_CANCEL_TIME_IN_MILLIS;
	public static boolean ALT_FISH_CHAMPIONSHIP_ENABLED;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_ITEM;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_1;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_2;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_3;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_4;
	public static int ALT_FISH_CHAMPIONSHIP_REWARD_5;
	public static boolean ALT_ENABLE_BLOCK_CHECKER_EVENT;
	public static int ALT_MIN_BLOCK_CHECKER_TEAM_MEMBERS;
	public static double ALT_RATE_COINS_REWARD_BLOCK_CHECKER;
	public static boolean ALT_HBCE_FAIR_PLAY;
	public static int ALT_PET_INVENTORY_LIMIT;
	public static boolean SAVE_GM_SPAWN_CUSTOM;
	public static boolean LOAD_GM_SPAWN_CUSTOM;
	public static int STARTING_ADENA;
	public static int STARTING_LEVEL;
	public static int SUB_START_LEVEL;
	public static int BOW_REUSE;
	public static int CROSSBOW_REUSE;
	public static int LIM_PATK;
	public static int LIM_MATK;
	public static int LIM_PDEF;
	public static int LIM_MDEF;
	public static int LIM_MATK_SPD;
	public static int LIM_PATK_SPD;
	public static int LIM_CRIT_DAM;
	public static int LIM_CRIT;
	public static int LIM_MCRIT;
	public static int LIM_ACCURACY;
	public static int LIM_MACCURACY;
	public static int LIM_EVASION;
	public static int LIM_MEVASION;
	public static int LIM_MOVE;
	public static int GM_LIM_MOVE;
	public static int LIM_FAME;
	public static double ALT_NPC_PATK_MODIFIER;
	public static double ALT_NPC_MATK_MODIFIER;
	public static double ALT_NPC_MAXHP_MODIFIER;
	public static double ALT_NPC_MAXMP_MODIFIER;
	public static boolean ALLOW_TALK_WHILE_SITTING;
	public static boolean PARTY_LEADER_ONLY_CAN_INVITE;
	public static boolean PARTY_MATCHMAKING_ON_ENTERWORLD;
	// public static boolean ALLOW_CLANSKILLS;
	public static boolean ALLOW_LEARN_TRANS_SKILLS_WO_QUEST;
	public static boolean ALLOW_MANOR;
	public static int MANOR_REFRESH_TIME;
	public static int MANOR_REFRESH_MIN;
	public static int MANOR_APPROVE_TIME;
	public static int MANOR_APPROVE_MIN;
	public static int MANOR_MAINTENANCE_PERIOD;
	public static double EVENT_CofferOfShadowsPriceRate;
	// public static double EVENT_CofferOfShadowsRewardRate;
	public static double EVENT_APRIL_FOOLS_DROP_CHANCE;
	// public static int ENCHANT_CHANCE_MASTER_YOGI_STAFF;
	// public static int ENCHANT_MAX_MASTER_YOGI_STAFF;
	// public static int SAFE_ENCHANT_MASTER_YOGI_STAFF;
	public static int EVENT_LastHeroItemID;
	public static double EVENT_LastHeroItemCOUNT;
	public static boolean EVENT_LastHeroRate;
	public static double EVENT_LastHeroItemCOUNTFinal;
	public static boolean EVENT_LastHeroRateFinal;
	public static int EVENT_LHTime;
	public static String[] EVENT_LHStartTime;
	public static boolean EVENT_LHCategories;
	public static boolean EVENT_LHAllowSummons;
	public static boolean EVENT_LHAllowBuffs;
	public static boolean EVENT_LHAllowMultiReg;
	public static String EVENT_LHCheckWindowMethod;
	public static int EVENT_LHEventRunningTime;
	public static String[] EVENT_LHFighterBuffs;
	public static String[] EVENT_LHMageBuffs;
	public static boolean EVENT_LHBuffPlayers;
	public static boolean ALLOW_HEROES_LASTHERO;
	public static String[] EVENT_TvTRewards;
	// public static String[] EVENTS_DISALLOWED_SKILLS;
	public static int EVENT_TvTTime;
	public static boolean EVENT_TvT_rate;
	public static String[] EVENT_TvTStartTime;
	public static boolean EVENT_TvTCategories;
	public static int EVENT_TvTMaxPlayerInTeam;
	public static int EVENT_TvTMinPlayerInTeam;
	public static boolean EVENT_TvTAllowSummons;
	public static boolean EVENT_TvTAllowBuffs;
	public static boolean EVENT_TvTAllowMultiReg;
	public static String EVENT_TvTCheckWindowMethod;
	public static int EVENT_TvTEventRunningTime;
	public static String[] EVENT_TvTFighterBuffs;
	public static String[] EVENT_TvTMageBuffs;
	public static boolean EVENT_TvTBuffPlayers;
	public static boolean EVENT_TvTrate;
	public static int EVENT_CtfTime;
	public static boolean EVENT_CtFrate;
	public static String[] EVENT_CtFStartTime;
	public static boolean EVENT_CtFCategories;
	public static int EVENT_CtFMaxPlayerInTeam;
	public static int EVENT_CtFMinPlayerInTeam;
	public static boolean EVENT_CtFAllowSummons;
	public static boolean EVENT_CtFAllowBuffs;
	public static boolean EVENT_CtFAllowMultiReg;
	public static String EVENT_CtFCheckWindowMethod;
	public static String[] EVENT_CtFFighterBuffs;
	public static String[] EVENT_CtFMageBuffs;
	public static boolean EVENT_CtFBuffPlayers;
	public static String[] EVENT_CtFRewards;
	public static double EVENT_TFH_POLLEN_CHANCE;
	public static double EVENT_GLITTMEDAL_NORMAL_CHANCE;
	public static double EVENT_GLITTMEDAL_GLIT_CHANCE;
	public static double EVENT_L2DAY_LETTER_CHANCE;
	public static double EVENT_CHANGE_OF_HEART_CHANCE;
	// public static int TMEVENTINTERVAL;
	// public static int TMTIME1;
	// public static int TMWAVE1COUNT;
	// public static int TMWAVE2;
	public static double EVENT_TRICK_OF_TRANS_CHANCE;
	public static double EVENT_MARCH8_DROP_CHANCE;
	public static double EVENT_MARCH8_PRICE_RATE;
	public static boolean EVENT_BOUNTY_HUNTERS_ENABLED;
	public static long EVENT_SAVING_SNOWMAN_LOTERY_PRICE;
	public static int EVENT_SAVING_SNOWMAN_REWARDER_CHANCE;
	public static boolean SERVICES_NO_TRADE_ONLY_OFFLINE;
	public static double SERVICES_TRADE_TAX;
	public static double SERVICES_OFFSHORE_TRADE_TAX;
	public static boolean SERVICES_OFFSHORE_NO_CASTLE_TAX;
	public static boolean SERVICES_TRADE_TAX_ONLY_OFFLINE;
	public static boolean SERVICES_TRADE_ONLY_FAR;
	public static int SERVICES_TRADE_RADIUS;
	public static int SERVICES_TRADE_MIN_LEVEL;
	public static boolean SERVICES_ENABLE_NO_CARRIER;
	public static int SERVICES_NO_CARRIER_DEFAULT_TIME;
	// public static int SERVICES_NO_CARRIER_MAX_TIME;
	// public static int SERVICES_NO_CARRIER_MIN_TIME;
	public static boolean ALT_OPEN_CLOAK_SLOT;
	public static boolean ALT_SHOW_SERVER_TIME;
	public static int GEO_X_FIRST, GEO_Y_FIRST, GEO_X_LAST, GEO_Y_LAST;
	public static String GEOFILES_PATTERN;
	public static boolean ALLOW_GEODATA;
	public static boolean ALLOW_FALL_FROM_WALLS;
	// public static boolean ALLOW_KEYBOARD_MOVE;
	public static boolean COMPACT_GEO;
	public static int CLIENT_Z_SHIFT;
	public static int MAX_Z_DIFF;
	public static int MIN_LAYER_HEIGHT;
	public static int PATHFIND_BOOST;
	public static boolean PATHFIND_DIAGONAL;
	public static boolean PATH_CLEAN;
	public static int PATHFIND_MAX_Z_DIFF;
	public static long PATHFIND_MAX_TIME;
	public static String PATHFIND_BUFFERS;
	public static boolean DEBUG;
	public static int GAME_POINT_ITEM_ID;
	public static int WEAR_DELAY;
	public static int GARBAGE_COLLECTOR_INTERVAL;
	public static final boolean GOODS_INVENTORY_ENABLED = false;
	public static boolean EX_NEW_PETITION_SYSTEM;
	public static boolean EX_JAPAN_MINIGAME;
	public static boolean EX_LECTURE_MARK;
	public static boolean LOGIN_SERVER_GM_ONLY;
	public static boolean LOGIN_SERVER_BRACKETS;
	public static boolean LOGIN_SERVER_IS_PVP;
	public static int LOGIN_SERVER_AGE_LIMIT;
	public static int LOGIN_SERVER_SERVER_TYPE;
	public static boolean REMOVE_UNKNOWN_QUEST;
	public static boolean ALLOW_MENTOR_BUFFS_IN_OFFLINE_MODE;
	public static boolean STARTING_LOC;
	public static int STARTING_LOC_X;
	public static int STARTING_LOC_Y;
	public static int STARTING_LOC_Z;
	public static boolean STARTING_ITEMS;
	public static List<int[]> STARTING_ITEMS_ID_QTY;
	
	/**
	 * Method loadServerConfig.
	 */
	private static void loadServerConfig()
	{
		ExProperties serverSettings = load(SERVER_CONFIG_FILE);
		GAME_SERVER_LOGIN_HOST = serverSettings.getProperty("LoginHost", "127.0.0.1");
		GAME_SERVER_LOGIN_PORT = serverSettings.getProperty("LoginPort", 9013);
		// GAME_SERVER_LOGIN_CRYPT = serverSettings.getProperty("LoginUseCrypt", true);
		LOGIN_SERVER_AGE_LIMIT = serverSettings.getProperty("ServerAgeLimit", 0);
		LOGIN_SERVER_GM_ONLY = serverSettings.getProperty("ServerGMOnly", false);
		LOGIN_SERVER_BRACKETS = serverSettings.getProperty("ServerBrackets", false);
		LOGIN_SERVER_IS_PVP = serverSettings.getProperty("PvPServer", false);
		
		for (String a : serverSettings.getProperty("ServerType", ArrayUtils.EMPTY_STRING_ARRAY))
		{
			if (a.trim().isEmpty())
			{
				continue;
			}
			
			ServerType t = ServerType.valueOf(a.toUpperCase());
			LOGIN_SERVER_SERVER_TYPE |= t.getMask();
		}
		
		INTERNAL_HOSTNAME = serverSettings.getProperty("InternalHostname", "*");
		EXTERNAL_HOSTNAME = serverSettings.getProperty("ExternalHostname", "*");
		REQUEST_ID = serverSettings.getProperty("RequestServerID", 0);
		ACCEPT_ALTERNATE_ID = serverSettings.getProperty("AcceptAlternateID", true);
		GAMESERVER_HOSTNAME = serverSettings.getProperty("GameserverHostname");
		PORTS_GAME = serverSettings.getProperty("GameserverPort", new int[]
		{
			7777
		});
		EVERYONE_HAS_ADMIN_RIGHTS = serverSettings.getProperty("EveryoneHasAdminRights", false);
		DEFAULT_ACCESS_FOR_EVERYONE = serverSettings.getProperty("DefaultAccessLevelForEveryone", 100);
		SECOND_AUTH_ENABLED = serverSettings.getProperty("SecondAuth", false);
		SECOND_AUTH_MAX_ATTEMPTS = serverSettings.getProperty("SecondAuthMaxEnter", 5);
		SECOND_AUTH_BAN_TIME = serverSettings.getProperty("SecondAuthBanTime", 480);
		DEBUG_SPAWN_MANAGER = serverSettings.getProperty("DebugSpawnManager", false);
		DEBUG_EVENT_SCHEDULES = serverSettings.getProperty("DebugEventSchedules", false);
		// HIDE_GM_STATUS = serverSettings.getProperty("HideGMStatus", false);
		SHOW_GM_LOGIN = serverSettings.getProperty("ShowGMLogin", true);
		SAVE_GM_EFFECTS = serverSettings.getProperty("SaveGMEffects", false);
		CNAME_TEMPLATE = serverSettings.getProperty("CnameTemplate", "[A-Za-z0-9\u0410-\u042f\u0430-\u044f]{2,16}");
		CLAN_NAME_TEMPLATE = serverSettings.getProperty("ClanNameTemplate", "[A-Za-z0-9\u0410-\u042f\u0430-\u044f]{3,16}");
		CLAN_TITLE_TEMPLATE = serverSettings.getProperty("ClanTitleTemplate", "[A-Za-z0-9\u0410-\u042f\u0430-\u044f \\p{Punct}]{1,16}");
		ALLY_NAME_TEMPLATE = serverSettings.getProperty("AllyNameTemplate", "[A-Za-z0-9\u0410-\u042f\u0430-\u044f]{3,16}");
		GLOBAL_SHOUT = serverSettings.getProperty("GlobalShout", false);
		GLOBAL_TRADE_CHAT = serverSettings.getProperty("GlobalTradeChat", false);
		CHAT_RANGE = serverSettings.getProperty("ChatRange", 1250);
		SHOUT_OFFSET = serverSettings.getProperty("ShoutOffset", 0);
		PREMIUM_HEROCHAT = serverSettings.getProperty("PremiumHeroChat", true);
		LOG_CHAT = serverSettings.getProperty("LogChat", false);
		AUTODESTROY_ITEM_AFTER = serverSettings.getProperty("AutoDestroyDroppedItemAfter", 0);
		// AUTODESTROY_PLAYER_ITEM_AFTER = serverSettings.getProperty("AutoDestroyPlayerDroppedItemAfter", 0);
		DELETE_DAYS = serverSettings.getProperty("DeleteCharAfterDays", 7);
		// PURGE_BYPASS_TASK_FREQUENCY = serverSettings.getProperty("PurgeTaskFrequency", 60);
		
		try
		{
			DATAPACK_ROOT = new File(serverSettings.getProperty("DatapackRoot", ".")).getCanonicalFile();
		}
		catch (IOException e)
		{
			_log.error("", e);
		}
		
		ALLOW_DISCARDITEM = serverSettings.getProperty("AllowDiscardItem", true);
		ALLOW_MAIL = serverSettings.getProperty("AllowMail", true);
		ALLOW_WAREHOUSE = serverSettings.getProperty("AllowWarehouse", true);
		ALLOW_WATER = serverSettings.getProperty("AllowWater", true);
		ALLOW_CURSED_WEAPONS = serverSettings.getProperty("AllowCursedWeapons", false);
		DROP_CURSED_WEAPONS_ON_KICK = serverSettings.getProperty("DropCursedWeaponsOnKick", false);
		MIN_PROTOCOL_REVISION = serverSettings.getProperty("MinProtocolRevision", 415);
		MAX_PROTOCOL_REVISION = serverSettings.getProperty("MaxProtocolRevision", 448);
		MIN_NPC_ANIMATION = serverSettings.getProperty("MinNPCAnimation", 5);
		MAX_NPC_ANIMATION = serverSettings.getProperty("MaxNPCAnimation", 90);
		SERVER_SIDE_NPC_NAME = serverSettings.getProperty("ServerSideNpcName", false);
		SERVER_SIDE_NPC_TITLE = serverSettings.getProperty("ServerSideNpcTitle", false);
		AUTOSAVE = serverSettings.getProperty("Autosave", true);
		MAXIMUM_ONLINE_USERS = serverSettings.getProperty("MaximumOnlineUsers", 3000);
		DATABASE_DRIVER = serverSettings.getProperty("Driver", "com.mysql.jdbc.Driver");
		DATABASE_MAX_CONNECTIONS = serverSettings.getProperty("MaximumDbConnections", 10);
		DATABASE_MAX_IDLE_TIMEOUT = serverSettings.getProperty("MaxIdleConnectionTimeout", 600);
		DATABASE_IDLE_TEST_PERIOD = serverSettings.getProperty("IdleConnectionTestPeriod", 60);
		DATABASE_URL = serverSettings.getProperty("URL", "jdbc:mysql://localhost/l2sdb");
		DATABASE_LOGIN = serverSettings.getProperty("Login", "root");
		DATABASE_PASSWORD = serverSettings.getProperty("Password", "");
		EFFECT_TASK_MANAGER_COUNT = serverSettings.getProperty("EffectTaskManagers", 2);
		SCHEDULED_THREAD_POOL_SIZE = serverSettings.getProperty("ScheduledThreadPoolSize", NCPUS * 4);
		EXECUTOR_THREAD_POOL_SIZE = serverSettings.getProperty("ExecutorThreadPoolSize", NCPUS * 2);
		// THREAD_P_MOVE = serverSettings.getProperty("ThreadPoolSizeMove", 25);
		// THREAD_P_PATHFIND = serverSettings.getProperty("ThreadPoolSizePathfind", 10);
		// NPC_AI_MAX_THREAD = serverSettings.getProperty("NpcAiMaxThread", 10);
		// PLAYER_AI_MAX_THREAD = serverSettings.getProperty("PlayerAiMaxThread", 20);
		ENABLE_RUNNABLE_STATS = serverSettings.getProperty("EnableRunnableStats", false);
		SELECTOR_CONFIG.SLEEP_TIME = serverSettings.getProperty("SelectorSleepTime", 10L);
		SELECTOR_CONFIG.INTEREST_DELAY = serverSettings.getProperty("InterestDelay", 30L);
		SELECTOR_CONFIG.MAX_SEND_PER_PASS = serverSettings.getProperty("MaxSendPerPass", 32);
		SELECTOR_CONFIG.READ_BUFFER_SIZE = serverSettings.getProperty("ReadBufferSize", 65536);
		SELECTOR_CONFIG.WRITE_BUFFER_SIZE = serverSettings.getProperty("WriteBufferSize", 131072);
		SELECTOR_CONFIG.HELPER_BUFFER_COUNT = serverSettings.getProperty("BufferPoolSize", 64);
		CHAT_MESSAGE_MAX_LEN = serverSettings.getProperty("ChatMessageLimit", 1000);
		ABUSEWORD_BANCHAT = serverSettings.getProperty("ABUSEWORD_BANCHAT", false);
		int counter = 0;
		
		for (int id : serverSettings.getProperty("ABUSEWORD_BAN_CHANNEL", new int[]
		{
			0
		}))
		{
			BAN_CHANNEL_LIST[counter] = id;
			counter++;
		}
		ABUSEWORD_REPLACE = serverSettings.getProperty("ABUSEWORD_REPLACE", false);
		ABUSEWORD_REPLACE_STRING = serverSettings.getProperty("ABUSEWORD_REPLACE_STRING", "[censored]");
		BANCHAT_ANNOUNCE = serverSettings.getProperty("BANCHAT_ANNOUNCE", true);
		BANCHAT_ANNOUNCE_FOR_ALL_WORLD = serverSettings.getProperty("BANCHAT_ANNOUNCE_FOR_ALL_WORLD", true);
		BANCHAT_ANNOUNCE_NICK = serverSettings.getProperty("BANCHAT_ANNOUNCE_NICK", true);
		ABUSEWORD_BANTIME = serverSettings.getProperty("ABUSEWORD_UNBAN_TIMER", 30);
		CHATFILTER_MIN_LEVEL = serverSettings.getProperty("ChatFilterMinLevel", 0);
		counter = 0;
		
		for (int id : serverSettings.getProperty("ChatFilterChannels", new int[]
		{
			1,
			8
		}))
		{
			CHATFILTER_CHANNELS[counter] = id;
			counter++;
		}
		CHATFILTER_WORK_TYPE = serverSettings.getProperty("ChatFilterWorkType", 1);
		RESTART_AT_TIME = serverSettings.getProperty("AutoRestartAt", "0 5 * * *");
		SHIFT_BY = serverSettings.getProperty("HShift", 12);
		SHIFT_BY_Z = serverSettings.getProperty("VShift", 11);
		MAP_MIN_Z = serverSettings.getProperty("MapMinZ", -32768);
		MAP_MAX_Z = serverSettings.getProperty("MapMaxZ", 32767);
		MOVE_PACKET_DELAY = serverSettings.getProperty("MovePacketDelay", 100);
		ATTACK_PACKET_DELAY = serverSettings.getProperty("AttackPacketDelay", 500);
		DAMAGE_FROM_FALLING = serverSettings.getProperty("DamageFromFalling", true);
		ALLOW_WEDDING = serverSettings.getProperty("AllowWedding", false);
		WEDDING_PRICE = serverSettings.getProperty("WeddingPrice", 500000);
		WEDDING_PUNISH_INFIDELITY = serverSettings.getProperty("WeddingPunishInfidelity", true);
		// WEDDING_TELEPORT = serverSettings.getProperty("WeddingTeleport", true);
		WEDDING_TELEPORT_PRICE = serverSettings.getProperty("WeddingTeleportPrice", 500000);
		WEDDING_TELEPORT_INTERVAL = serverSettings.getProperty("WeddingTeleportInterval", 120);
		WEDDING_SAMESEX = serverSettings.getProperty("WeddingAllowSameSex", true);
		WEDDING_FORMALWEAR = serverSettings.getProperty("WeddingFormalWear", true);
		WEDDING_DIVORCE_COSTS = serverSettings.getProperty("WeddingDivorceCosts", 20);
		DONTLOADSPAWN = serverSettings.getProperty("StartWithoutSpawn", false);
		DONTLOADQUEST = serverSettings.getProperty("StartWithoutQuest", false);
		MAX_REFLECTIONS_COUNT = serverSettings.getProperty("MaxReflectionsCount", 300);
		WEAR_DELAY = serverSettings.getProperty("WearDelay", 5);
		GARBAGE_COLLECTOR_INTERVAL = serverSettings.getProperty("GarbageCollectorInterval", 30) * 60000;
		HTM_CACHE_MODE = serverSettings.getProperty("HtmCacheMode", HtmCache.LAZY);
		HTM_DEBUG_MODE = serverSettings.getProperty("DebugHtmlMessage", false);
		SHOW_NPC_LVL = serverSettings.getProperty("ShowNpcLevel", true);
		ALLOW_PACKET_FAIL = serverSettings.getProperty("AllowPacketFail", false);
		Random ppc = new Random();
		int z = ppc.nextInt(6);
		
		if (z == 0)
		{
			z += 2;
		}
		
		for (int x = 0; x < 8; x++)
		{
			if (x == 4)
			{
				RWHO_ARRAY[x] = 44;
			}
			else
			{
				RWHO_ARRAY[x] = 51 + ppc.nextInt(z);
			}
		}
		
		RWHO_ARRAY[11] = 37265 + ppc.nextInt((z * 2) + 3);
		RWHO_ARRAY[8] = 51 + ppc.nextInt(z);
		z = 36224 + ppc.nextInt(z * 2);
		RWHO_ARRAY[9] = z;
		RWHO_ARRAY[10] = z;
		RWHO_ARRAY[12] = 1;
		RWHO_LOG = Boolean.parseBoolean(serverSettings.getProperty("RemoteWhoLog", "False"));
		// RWHO_SEND_TRASH = Boolean.parseBoolean(serverSettings.getProperty("RemoteWhoSendTrash", "False"));
		// RWHO_MAX_ONLINE = Integer.parseInt(serverSettings.getProperty("RemoteWhoMaxOnline", "0"));
		// RWHO_KEEP_STAT = Integer.parseInt(serverSettings.getProperty("RemoteOnlineKeepStat", "5"));
		// RWHO_ONLINE_INCREMENT = Integer.parseInt(serverSettings.getProperty("RemoteOnlineIncrement", "0"));
		// RWHO_PRIV_STORE_FACTOR = Float.parseFloat(serverSettings.getProperty("RemotePrivStoreFactor", "0"));
		// RWHO_FORCE_INC = Integer.parseInt(serverSettings.getProperty("RemoteWhoForceInc", "0"));
	}
	
	/**
	 * Method loadRatesConfig.
	 */
	private static void loadRatesConfig()
	{
		ExProperties rateSettings = load(RATES_CONFIG_FILE);
		
		RATE_XP = rateSettings.getProperty("RateXp", 1.);
		RATE_SP = rateSettings.getProperty("RateSp", 1.);
		RATE_DROP_ADENA = rateSettings.getProperty("RateDropAdena", 1.);
		RATE_DROP_ITEMS = rateSettings.getProperty("RateDropItems", 1.);
		RATE_DROP_COMMON_ITEMS = rateSettings.getProperty("RateDropCommonItems", 1.);
		RATE_DROP_SPOIL = rateSettings.getProperty("RateDropSpoil", 1.);
		RATE_QUESTS_DROP = rateSettings.getProperty("RateQuestsDrop", 1.);
		RATE_QUESTS_REWARD = rateSettings.getProperty("RateQuestsReward", 1.);
		RATE_DROP_RAIDBOSS = rateSettings.getProperty("RateRaidBoss", 1.);
		NO_RATE_ITEMS = rateSettings.getProperty("NoRateItemIds", new int[]
		{
			6660,
			6662,
			6661,
			6659,
			6656,
			6658,
			8191,
			6657,
			10170,
			10314,
			16025,
			16026
		});
		RATE_MANOR = rateSettings.getProperty("RateManor", 1.);
		RATE_CLAN_REP_SCORE = rateSettings.getProperty("RateClanRepScore", 1.);
		RATE_CLAN_REP_SCORE_MAX_AFFECTED = rateSettings.getProperty("RateClanRepScoreMaxAffected", 2);
		RATE_DROP_SIEGE_GUARD = rateSettings.getProperty("RateSiegeGuard", 1.);
		NO_RATE_EQUIPMENT = rateSettings.getProperty("NoRateEquipment", true);
		NO_RATE_KEY_MATERIAL = rateSettings.getProperty("NoRateKeyMaterial", true);
		NO_RATE_RECIPES = rateSettings.getProperty("NoRateRecipes", true);
		RATE_PARTY_MIN = rateSettings.getProperty("RatePartyMin", false);
		RATE_MOB_SPAWN = rateSettings.getProperty("RateMobSpawn", 1);
		RATE_MOB_SPAWN_MIN_LEVEL = rateSettings.getProperty("RateMobMinLevel", 1);
		RATE_MOB_SPAWN_MAX_LEVEL = rateSettings.getProperty("RateMobMaxLevel", 100);
		RATE_RAID_REGEN = rateSettings.getProperty("RateRaidRegen", 1.);
		RATE_RAID_DEFENSE = rateSettings.getProperty("RateRaidDefense", 1.);
		RATE_RAID_ATTACK = rateSettings.getProperty("RateRaidAttack", 1.);
		RATE_EPIC_DEFENSE = rateSettings.getProperty("RateEpicDefense", RATE_RAID_DEFENSE);
		RATE_EPIC_ATTACK = rateSettings.getProperty("RateEpicAttack", RATE_RAID_ATTACK);
		RAID_MAX_LEVEL_DIFF = rateSettings.getProperty("RaidMaxLevelDiff", 8);
		PARALIZE_ON_RAID_DIFF = rateSettings.getProperty("ParalizeOnRaidLevelDiff", true);
		ENABLE_DYNAMIC_RATES = rateSettings.getProperty("EnableDynamicRates", false);
		DYN_RATE_XP9 = rateSettings.getProperty("DynRateXp9", 1.);
		DYN_RATE_SP9 = rateSettings.getProperty("DynRateSp9", 1.);
		DYN_RATE_XP19 = rateSettings.getProperty("DynRateXp19", 1.);
		DYN_RATE_SP19 = rateSettings.getProperty("DynRateSp19", 1.);
		DYN_RATE_XP29 = rateSettings.getProperty("DynRateXp29", 1.);
		DYN_RATE_SP29 = rateSettings.getProperty("DynRateSp29", 1.);
		DYN_RATE_XP39 = rateSettings.getProperty("DynRateXp39", 1.);
		DYN_RATE_SP39 = rateSettings.getProperty("DynRateSp39", 1.);
		DYN_RATE_XP49 = rateSettings.getProperty("DynRateXp49", 1.);
		DYN_RATE_SP49 = rateSettings.getProperty("DynRateSp49", 1.);
		DYN_RATE_XP59 = rateSettings.getProperty("DynRateXp59", 1.);
		DYN_RATE_SP59 = rateSettings.getProperty("DynRateSp59", 1.);
		DYN_RATE_XP69 = rateSettings.getProperty("DynRateXp69", 1.);
		DYN_RATE_SP69 = rateSettings.getProperty("DynRateSp69", 1.);
		DYN_RATE_XP79 = rateSettings.getProperty("DynRateXp79", 1.);
		DYN_RATE_SP79 = rateSettings.getProperty("DynRateSp79", 1.);
		DYN_RATE_XP89 = rateSettings.getProperty("DynRateXp89", 1.);
		DYN_RATE_SP89 = rateSettings.getProperty("DynRateSp89", 1.);
		DYN_RATE_XP99 = rateSettings.getProperty("DynRateXp99", 1.);
		DYN_RATE_SP99 = rateSettings.getProperty("DynRateSp99", 1.);
	}
	
	/**
	 * Method loadCommunityConfig.
	 */
	private static void loadCommunityConfig()
	{
		ExProperties communitySettings = load(COMMUNITY_CONFIG_FILE);
		COMMUNITYBOARD_ENABLED = communitySettings.getProperty("CommunityBoardEnable", true);
		
		if (COMMUNITYBOARD_ENABLED)
		{
			ALLOW_COMMUNITYBOARD_IN_COMBAT = communitySettings.getProperty("AllowInCombat", false);
			// COMMUNITYBOARD_ABNORMAL_ENABLED = communitySettings.getProperty("AllowAbnormalState", false);
			BBS_DEFAULT = communitySettings.getProperty("BBSStartPage", "_bbshome");
			BBS_HOME_DIR = communitySettings.getProperty("BBSHomeDir", "scripts/services/community/");
			COMMUNITYBOARD_SHOP_ENABLED = communitySettings.getProperty("CommunityShopEnable", false);
			
			for (int id : communitySettings.getProperty("AllowMultisell", new int[] {}))
			{
				COMMUNITYBOARD_MULTISELL_ALLOW.add(Integer.valueOf(id));
			}
			
			COMMUNITYBOARD_BUFFER_ENABLED = communitySettings.getProperty("CommunityBufferEnable", false);
			// COMMUNITYBOARD_BUFFER_PET_ENABLED = communitySettings.getProperty("CommunityBufferPetEnable", false);
			// COMMUNITYBOARD_BUFFER_SAVE_ENABLED = communitySettings.getProperty("CommunityBufferSaveEnable", false);
			COMMUNITYBOARD_INSTANCE_ENABLED = communitySettings.getProperty("CommunityBufferInstancesEnable", false);
			COMMUNITYBOARD_EVENTS_ENABLED = communitySettings.getProperty("CommunityBufferEventsEnable", false);
			COMMUNITYBOARD_BUFF_TIME = communitySettings.getProperty("CommunityBuffTime", 20) * 60000;
			COMMUNITYBOARD_BUFF_PICE = communitySettings.getProperty("CommunityBuffPice", 5000);
			COMMUNITYBOARD_BUFF_SAVE_PICE = communitySettings.getProperty("CommunityBuffSavePice", 50000);
			
			for (int id : communitySettings.getProperty("AllowEffect", new int[]
			{
				1085,
				1048,
				1045
			}))
			{
				COMMUNITYBOARD_BUFF_ALLOW.add(Integer.valueOf(id));
			}
			
			for (int id : communitySettings.getProperty("MageScheme", new int[]
			{
				1085
			}))
			{
				COMMUNITI_LIST_MAGE_SUPPORT.add(Integer.valueOf(id));
			}
			
			for (int id : communitySettings.getProperty("FighterScheme", new int[]
			{
				1085
			}))
			{
				COMMUNITI_LIST_FIGHTER_SUPPORT.add(Integer.valueOf(id));
			}
			COMMUNITYBOARD_TELEPORT_ENABLED = communitySettings.getProperty("CommunityTeleportEnable", false);
			COMMUNITYBOARD_TELE_PICE = communitySettings.getProperty("CommunityTeleportPice", 10000);
			COMMUNITYBOARD_SAVE_TELE_PICE = communitySettings.getProperty("CommunitySaveTeleportPice", 50000);
			USE_BBS_BUFER_IS_COMBAT = communitySettings.getProperty("UseBBSBuferIsCombat", false);
			// USE_BBS_BUFER_IS_EVENTS = communitySettings.getProperty("UseBBSBuferIsEvents", false);
			USE_BBS_TELEPORT_IS_COMBAT = communitySettings.getProperty("UseBBSTeleportIsCombat", false);
			// USE_BBS_TELEPORT_IS_EVENTS = communitySettings.getProperty("UseBBSTeleportIsEvents", false);
			// USE_BBS_PROF_IS_COMBAT = communitySettings.getProperty("UseBBSProfIsCombat", false);
			// USE_BBS_PROF_IS_EVENTS = communitySettings.getProperty("UseBBSProfIsEvents", false);
			// SAVE_BBS_TELEPORT_IS_EPIC = communitySettings.getProperty("SaveBBSTeleportIsEpic", false);
			// SAVE_BBS_TELEPORT_IS_BZ = communitySettings.getProperty("SaveBBSTeleportIsBZ", false);
			// BUFFER_SUMMON_ENABLE = communitySettings.getProperty("CommunityBufferSummon", false);
			ENCHANT_ENABLED = communitySettings.getProperty("Enchant_enabled", false);
			ENCHANTER_ITEM_ID = communitySettings.getProperty("CBEnchantItem", 4037);
			MAX_ENCHANT = communitySettings.getProperty("CBEnchantItem", 20);
			ENCHANT_LEVELS = communitySettings.getProperty("CBEnchantLvl", new int[]
			{
				1
			});
			ENCHANT_PRICE_WPN = communitySettings.getProperty("CBEnchantPriceWeapon", new int[]
			{
				1
			});
			ENCHANT_PRICE_ARM = communitySettings.getProperty("CBEnchantPriceArmor", new int[]
			{
				1
			});
			ENCHANT_ATTRIBUTE_LEVELS = communitySettings.getProperty("CBEnchantAtributeLvlWeapon", new int[]
			{
				1
			});
			ENCHANT_ATTRIBUTE_LEVELS_ARM = communitySettings.getProperty("CBEnchantAtributeLvlArmor", new int[]
			{
				1
			});
			ATTRIBUTE_PRICE_WPN = communitySettings.getProperty("CBEnchantAtributePriceWeapon", new int[]
			{
				1
			});
			ATTRIBUTE_PRICE_ARM = communitySettings.getProperty("CBEnchantAtributePriceArmor", new int[]
			{
				1
			});
			ENCHANT_ATT_PVP = communitySettings.getProperty("CBEnchantAtributePvP", false);
		}
	}
	
	/**
	 * Method loadResidenceConfig.
	 */
	private static void loadResidenceConfig()
	{
		ExProperties residenceSettings = load(RESIDENCE_CONFIG_FILE);
		// CH_BID_GRADE1_MINCLANLEVEL = residenceSettings.getProperty("ClanHallBid_Grade1_MinClanLevel", 2);
		// CH_BID_GRADE1_MINCLANMEMBERS = residenceSettings.getProperty("ClanHallBid_Grade1_MinClanMembers", 1);
		// CH_BID_GRADE1_MINCLANMEMBERSLEVEL = residenceSettings.getProperty("ClanHallBid_Grade1_MinClanMembersAvgLevel", 1);
		// CH_BID_GRADE2_MINCLANLEVEL = residenceSettings.getProperty("ClanHallBid_Grade2_MinClanLevel", 2);
		// CH_BID_GRADE2_MINCLANMEMBERS = residenceSettings.getProperty("ClanHallBid_Grade2_MinClanMembers", 1);
		// CH_BID_GRADE2_MINCLANMEMBERSLEVEL = residenceSettings.getProperty("ClanHallBid_Grade2_MinClanMembersAvgLevel", 1);
		// CH_BID_GRADE3_MINCLANLEVEL = residenceSettings.getProperty("ClanHallBid_Grade3_MinClanLevel", 2);
		// CH_BID_GRADE3_MINCLANMEMBERS = residenceSettings.getProperty("ClanHallBid_Grade3_MinClanMembers", 1);
		// CH_BID_GRADE3_MINCLANMEMBERSLEVEL = residenceSettings.getProperty("ClanHallBid_Grade3_MinClanMembersAvgLevel", 1);
		RESIDENCE_LEASE_FUNC_MULTIPLIER = residenceSettings.getProperty("ResidenceLeaseFuncMultiplier", 1.);
		// RESIDENCE_LEASE_MULTIPLIER = residenceSettings.getProperty("ResidenceLeaseMultiplier", 1.);
		CASTLE_SELECT_HOURS = residenceSettings.getProperty("CastleSelectHours", new int[]
		{
			16,
			20
		});
		int[] tempCastleValidatonTime = residenceSettings.getProperty("CastleValidationDate", new int[]
		{
			2,
			4,
			2003
		});
		CASTLE_VALIDATION_DATE = Calendar.getInstance();
		CASTLE_VALIDATION_DATE.set(Calendar.DAY_OF_MONTH, tempCastleValidatonTime[0]);
		CASTLE_VALIDATION_DATE.set(Calendar.MONTH, tempCastleValidatonTime[1] - 1);
		CASTLE_VALIDATION_DATE.set(Calendar.YEAR, tempCastleValidatonTime[2]);
		CASTLE_VALIDATION_DATE.set(Calendar.HOUR_OF_DAY, 0);
		CASTLE_VALIDATION_DATE.set(Calendar.MINUTE, 0);
		CASTLE_VALIDATION_DATE.set(Calendar.SECOND, 0);
		CASTLE_VALIDATION_DATE.set(Calendar.MILLISECOND, 0);
	}
	
	/**
	 * Method loadVoteRewardSettings.
	 */
	private static void loadVoteRewardSettings()
	{
		ExProperties voteRewardSettings = load(VOTE_REWARD_CONFIG_FILE);
		L2_TOP_MANAGER_ENABLED = voteRewardSettings.getProperty("L2TopManagerEnabled", false);
		L2_TOP_MANAGER_INTERVAL = voteRewardSettings.getProperty("L2TopManagerInterval", 300000);
		L2_TOP_WEB_ADDRESS = voteRewardSettings.getProperty("L2TopWebAddress", "");
		L2_TOP_SMS_ADDRESS = voteRewardSettings.getProperty("L2TopSmsAddress", "");
		L2_TOP_SERVER_ADDRESS = voteRewardSettings.getProperty("L2TopServerAddress", "Lineage2GoD.com");
		L2_TOP_SAVE_DAYS = voteRewardSettings.getProperty("L2TopSaveDays", 30);
		L2_TOP_REWARD = voteRewardSettings.getProperty("L2TopReward", new int[0]);
		// L2_TOP_SERVER_PREFIX = voteRewardSettings.getProperty("L2TopServerPrefix", "");
		L2_TOP_REWARD_NO_CLAN = voteRewardSettings.getProperty("L2TopRewardNoClan", new int[0]);
		MMO_TOP_MANAGER_ENABLED = voteRewardSettings.getProperty("MMOTopEnable", false);
		MMO_TOP_MANAGER_INTERVAL = voteRewardSettings.getProperty("MMOTopManagerInterval", 300000);
		MMO_TOP_WEB_ADDRESS = voteRewardSettings.getProperty("MMOTopUrl", "");
		// MMO_TOP_SERVER_ADDRESS = voteRewardSettings.getProperty("MMOTopServerAddress", "Lineage2GoD.com");
		MMO_TOP_SAVE_DAYS = voteRewardSettings.getProperty("MMOTopSaveDays", 30);
		MMO_TOP_REWARD = voteRewardSettings.getProperty("MMOTopReward", new int[0]);
		MMO_TOP_REWARD_NO_CLAN = voteRewardSettings.getProperty("MMOTopRewardNoClan", new int[0]);
	}
	
	/**
	 * Method loadDonateConfig.
	 */
	private static void loadDonateConfig()
	{
		ExProperties donateSetting = load(DONATE_CONFIG_FILE);
		SMS_PAYMENT_MANAGER_ENABLED = donateSetting.getProperty("SMSPaymentEnabled", false);
		SMS_PAYMENT_WEB_ADDRESS = donateSetting.getProperty("SMSPaymentWebAddress", "");
		SMS_PAYMENT_MANAGER_INTERVAL = donateSetting.getProperty("SMSPaymentManagerInterval", 300000);
		SMS_PAYMENT_SAVE_DAYS = donateSetting.getProperty("SMSPaymentSaveDays", 30);
		SMS_PAYMENT_SERVER_ADDRESS = donateSetting.getProperty("SMSPaymentServerAddress", "Lineage2GoD.com");
		SMS_PAYMENT_REWARD = donateSetting.getProperty("SMSPaymentReward", new int[0]);
		SMS_PAYMENT_REWARD_NO_CLAN = donateSetting.getProperty("SMSPaymentRewardNoClan", new int[0]);
		SMS_PAYMENT_TYPE = donateSetting.getProperty("SMSPaymentProfitOrSum", true);
		// SMS_PAYMENT_PREFIX = donateSetting.getProperty("SMSPaymentPrefix", "");
	}
	
	/**
	 * Method loadOtherConfig.
	 */
	private static void loadOtherConfig()
	{
		ExProperties otherSettings = load(OTHER_CONFIG_FILE);
		AWAKING_FREE = otherSettings.getProperty("AwakingFree", false);
		FREE_JUMPS_FOR_ALL = otherSettings.getProperty("FreeJumpsForAll", false);
		DEEPBLUE_DROP_RULES = otherSettings.getProperty("UseDeepBlueDropRules", true);
		DEEPBLUE_DROP_MAXDIFF = otherSettings.getProperty("DeepBlueDropMaxDiff", 8);
		DEEPBLUE_DROP_RAID_MAXDIFF = otherSettings.getProperty("DeepBlueDropRaidMaxDiff", 2);
		SWIMING_SPEED = otherSettings.getProperty("SwimingSpeedTemplate", 50);
		INVENTORY_MAXIMUM_NO_DWARF = otherSettings.getProperty("MaximumSlotsForNoDwarf", 80);
		INVENTORY_MAXIMUM_DWARF = otherSettings.getProperty("MaximumSlotsForDwarf", 100);
		INVENTORY_MAXIMUM_GM = otherSettings.getProperty("MaximumSlotsForGMPlayer", 250);
		QUEST_INVENTORY_MAXIMUM = otherSettings.getProperty("MaximumSlotsForQuests", 100);
		MULTISELL_SIZE = otherSettings.getProperty("MultisellPageSize", 10);
		WAREHOUSE_SLOTS_NO_DWARF = otherSettings.getProperty("BaseWarehouseSlotsForNoDwarf", 100);
		WAREHOUSE_SLOTS_DWARF = otherSettings.getProperty("BaseWarehouseSlotsForDwarf", 120);
		WAREHOUSE_SLOTS_CLAN = otherSettings.getProperty("MaximumWarehouseSlotsForClan", 200);
		FREIGHT_SLOTS = otherSettings.getProperty("MaximumFreightSlots", 10);
		ENCHANT_CHANCE_WEAPON = otherSettings.getProperty("EnchantChanceWeapon", 66);
		ENCHANT_CHANCE_ARMOR = otherSettings.getProperty("EnchantChanceArmor", ENCHANT_CHANCE_WEAPON);
		ENCHANT_CHANCE_ACCESSORY = otherSettings.getProperty("EnchantChanceAccessory", ENCHANT_CHANCE_ARMOR);
		ENCHANT_CHANCE_BLESSED_WEAPON = otherSettings.getProperty("EnchantChanceBlessedWeapon", 66);
		ENCHANT_CHANCE_BLESSED_ARMOR = otherSettings.getProperty("EnchantChanceBlessedArmor", ENCHANT_CHANCE_BLESSED_WEAPON);
		ENCHANT_CHANCE_BLESSED_ACCESSORY = otherSettings.getProperty("EnchantChanceBlessedAccessory", ENCHANT_CHANCE_BLESSED_ARMOR);
		ENCHANT_CHANCE_CRYSTAL_WEAPON = otherSettings.getProperty("EnchantChanceCrystalWeapon", 66);
		ENCHANT_CHANCE_CRYSTAL_ARMOR = otherSettings.getProperty("EnchantChanceCrystalArmor", ENCHANT_CHANCE_CRYSTAL_WEAPON);
		ENCHANT_CHANCE_CRYSTAL_ACCESSORY = otherSettings.getProperty("EnchantChanceCrystalAccessory", ENCHANT_CHANCE_CRYSTAL_ARMOR);
		ENCHANT_CHANCE_GIANT_WEAPON = otherSettings.getProperty("EnchantChanceGiantWeapon", 66);
		ENCHANT_CHANCE_GIANT_ARMOR = otherSettings.getProperty("EnchantChanceGiantArmor", ENCHANT_CHANCE_GIANT_WEAPON);
		ENCHANT_CHANCE_GIANT_ACCESSORY = otherSettings.getProperty("EnchantChanceGiantAccessory", ENCHANT_CHANCE_GIANT_ARMOR);
		SAFE_ENCHANT_COMMON = otherSettings.getProperty("SafeEnchantCommon", 3);
		SAFE_ENCHANT_FULL_BODY = otherSettings.getProperty("SafeEnchantFullBody", 4);
		ENCHANT_MAX = otherSettings.getProperty("EnchantMax", 20);
		ARMOR_OVERENCHANT_HPBONUS_LIMIT = otherSettings.getProperty("ArmorOverEnchantHPBonusLimit", 10) - 3;
		SHOW_ENCHANT_EFFECT_RESULT = otherSettings.getProperty("ShowEnchantEffectResult", false);
		ENCHANT_ATTRIBUTE_STONE_CHANCE = otherSettings.getProperty("EnchantAttributeChance", 50);
		ENCHANT_ATTRIBUTE_CRYSTAL_CHANCE = otherSettings.getProperty("EnchantAttributeCrystalChance", 30);
		REGEN_SIT_WAIT = otherSettings.getProperty("RegenSitWait", false);
		REGEN_SIT_WAIT = otherSettings.getProperty("RegenSitWait", false);
		STARTING_ADENA = otherSettings.getProperty("StartingAdena", 0);
		STARTING_LEVEL = otherSettings.getProperty("StartingLevel", 1);
		SUB_START_LEVEL = otherSettings.getProperty("SubClassStartLevel", 40);
		UNSTUCK_SKILL = otherSettings.getProperty("UnstuckSkill", true);
		RESPAWN_RESTORE_CP = otherSettings.getProperty("RespawnRestoreCP", 0.) / 100;
		RESPAWN_RESTORE_HP = otherSettings.getProperty("RespawnRestoreHP", 65.) / 100;
		RESPAWN_RESTORE_MP = otherSettings.getProperty("RespawnRestoreMP", 0.) / 100;
		MAX_PVTSTORE_SLOTS_DWARF = otherSettings.getProperty("MaxPvtStoreSlotsDwarf", 5);
		MAX_PVTSTORE_SLOTS_OTHER = otherSettings.getProperty("MaxPvtStoreSlotsOther", 4);
		MAX_PVTCRAFT_SLOTS = otherSettings.getProperty("MaxPvtManufactureSlots", 20);
		SENDSTATUS_TRADE_JUST_OFFLINE = otherSettings.getProperty("SendStatusTradeJustOffline", false);
		SENDSTATUS_TRADE_MOD = otherSettings.getProperty("SendStatusTradeMod", 1.);
		GM_NAME_COLOR = Integer.decode("0x" + otherSettings.getProperty("GMNameColor", "00CCFF"));
		GM_HERO_AURA = otherSettings.getProperty("GMHeroAura", false);
		NORMAL_NAME_COLOR = Integer.decode("0x" + otherSettings.getProperty("NormalNameColor", "FFFFFF"));
		CLANLEADER_NAME_COLOR = Integer.decode("0x" + otherSettings.getProperty("ClanleaderNameColor", "FFFFFF"));
		GAME_POINT_ITEM_ID = otherSettings.getProperty("GamePointItemId", -1);
		REMOVE_UNKNOWN_QUEST = otherSettings.getProperty("RemoveUnknownQuest", false);
		ALLOW_MENTOR_BUFFS_IN_OFFLINE_MODE = otherSettings.getProperty("AllowMentorBuffsInOfflineMode", false);
		STARTING_LOC = otherSettings.getProperty("StartingLoc", false);
		STARTING_LOC_X = otherSettings.getProperty("StartingLocX", -114536);
		STARTING_LOC_Y = otherSettings.getProperty("StartingLocY", 259928);
		STARTING_LOC_Z = otherSettings.getProperty("StartingLocZ", -1224);
		STARTING_ITEMS = otherSettings.getProperty("StartingItems", false);
		STARTING_ITEMS_ID_QTY = new ArrayList<>();
		String[] propertySplit = otherSettings.getProperty("StartingItemsIdQty", "20635,1;20638,1").split(";");
		
		for (String reward : propertySplit)
		{
			String[] rewardSplit = reward.split(",");
			
			if (rewardSplit.length != 2)
			{
			}
			else
			{
				try
				{
					STARTING_ITEMS_ID_QTY.add(new int[]
					{
						Integer.parseInt(rewardSplit[0]),
						Integer.parseInt(rewardSplit[1])
					});
				}
				catch (NumberFormatException nfe)
				{
					// empty catch clause
				}
			}
		}
	}
	
	/**
	 * Method loadSpoilConfig.
	 */
	private static void loadSpoilConfig()
	{
		ExProperties spoilSettings = load(SPOIL_CONFIG_FILE);
		BASE_SPOIL_RATE = spoilSettings.getProperty("BasePercentChanceOfSpoilSuccess", 78.);
		MINIMUM_SPOIL_RATE = spoilSettings.getProperty("MinimumPercentChanceOfSpoilSuccess", 1.);
		ALT_SPOIL_FORMULA = spoilSettings.getProperty("AltFormula", false);
		MANOR_SOWING_BASIC_SUCCESS = spoilSettings.getProperty("BasePercentChanceOfSowingSuccess", 100.);
		MANOR_SOWING_ALT_BASIC_SUCCESS = spoilSettings.getProperty("BasePercentChanceOfSowingAltSuccess", 10.);
		MANOR_HARVESTING_BASIC_SUCCESS = spoilSettings.getProperty("BasePercentChanceOfHarvestingSuccess", 90.);
		MANOR_DIFF_PLAYER_TARGET = spoilSettings.getProperty("MinDiffPlayerMob", 5);
		MANOR_DIFF_PLAYER_TARGET_PENALTY = spoilSettings.getProperty("DiffPlayerMobPenalty", 5.);
		MANOR_DIFF_SEED_TARGET = spoilSettings.getProperty("MinDiffSeedMob", 5);
		MANOR_DIFF_SEED_TARGET_PENALTY = spoilSettings.getProperty("DiffSeedMobPenalty", 5.);
		ALLOW_MANOR = spoilSettings.getProperty("AllowManor", true);
		MANOR_REFRESH_TIME = spoilSettings.getProperty("AltManorRefreshTime", 20);
		MANOR_REFRESH_MIN = spoilSettings.getProperty("AltManorRefreshMin", 00);
		MANOR_APPROVE_TIME = spoilSettings.getProperty("AltManorApproveTime", 6);
		MANOR_APPROVE_MIN = spoilSettings.getProperty("AltManorApproveMin", 00);
		MANOR_MAINTENANCE_PERIOD = spoilSettings.getProperty("AltManorMaintenancePeriod", 360000);
	}
	
	/**
	 * Method loadFormulasConfig.
	 */
	private static void loadFormulasConfig()
	{
		ExProperties formulasSettings = load(FORMULAS_CONFIG_FILE);
		SKILLS_CHANCE_MOD = formulasSettings.getProperty("SkillsChanceMod", 11.);
		SKILLS_CHANCE_POW = formulasSettings.getProperty("SkillsChancePow", 0.5);
		SKILLS_CHANCE_MIN = formulasSettings.getProperty("SkillsChanceMin", 5.);
		SKILLS_CHANCE_CAP = formulasSettings.getProperty("SkillsChanceCap", 95.);
		SKILLS_CAST_TIME_MIN = formulasSettings.getProperty("SkillsCastTimeMin", 333);
		ALT_ABSORB_DAMAGE_MODIFIER = formulasSettings.getProperty("AbsorbDamageModifier", 1.0);
		ALT_ABSORB_DAMAGE_ONLY_MEELE = formulasSettings.getProperty("AbsorbDamageOnlyMeele", true);
		LIM_PATK = formulasSettings.getProperty("LimitPatk", 20000);
		LIM_MATK = formulasSettings.getProperty("LimitMAtk", 25000);
		LIM_PDEF = formulasSettings.getProperty("LimitPDef", 15000);
		LIM_MDEF = formulasSettings.getProperty("LimitMDef", 15000);
		LIM_PATK_SPD = formulasSettings.getProperty("LimitPatkSpd", 1500);
		LIM_MATK_SPD = formulasSettings.getProperty("LimitMatkSpd", 1999);
		LIM_CRIT_DAM = formulasSettings.getProperty("LimitCriticalDamage", 2000);
		BOW_REUSE = formulasSettings.getProperty("bowReuse", 1500);
		CROSSBOW_REUSE = formulasSettings.getProperty("crossbowReuse", 850);
		LIM_CRIT = formulasSettings.getProperty("LimitCritical", 500);
		LIM_MCRIT = formulasSettings.getProperty("LimitMCritical", 200);
		LIM_ACCURACY = formulasSettings.getProperty("LimitAccuracy", 300);
		LIM_MACCURACY = formulasSettings.getProperty("LimitMAccuracy", 300);
		LIM_MEVASION = formulasSettings.getProperty("LimitMEvasion", 300);
		LIM_EVASION = formulasSettings.getProperty("LimitEvasion", 300);
		LIM_MOVE = formulasSettings.getProperty("LimitMove", 250);
		GM_LIM_MOVE = formulasSettings.getProperty("GmLimitMove", 1500);
		LIM_FAME = formulasSettings.getProperty("LimitFame", 3000000);
		ALT_NPC_PATK_MODIFIER = formulasSettings.getProperty("NpcPAtkModifier", 1.0);
		ALT_NPC_MATK_MODIFIER = formulasSettings.getProperty("NpcMAtkModifier", 1.0);
		ALT_NPC_MAXHP_MODIFIER = formulasSettings.getProperty("NpcMaxHpModifier", 1.58);
		ALT_NPC_MAXMP_MODIFIER = formulasSettings.getProperty("NpcMapMpModifier", 1.11);
		ALT_POLE_DAMAGE_MODIFIER = formulasSettings.getProperty("PoleDamageModifier", 1.0);
	}
	
	/**
	 * Method loadDevelopSettings.
	 */
	private static void loadDevelopSettings()
	{
		load(DEVELOP_CONFIG_FILE);
	}
	
	/**
	 * Method loadExtSettings.
	 */
	private static void loadExtSettings()
	{
		ExProperties properties = load(EXT_CONFIG_FILE);
		EX_NEW_PETITION_SYSTEM = properties.getProperty("NewPetitionSystem", false);
		EX_JAPAN_MINIGAME = properties.getProperty("JapanMinigame", false);
		EX_LECTURE_MARK = properties.getProperty("LectureMark", false);
	}
	
	/**
	 * Method loadGeneralSettings.
	 */
	private static void loadGeneralSettings()
	{
		ExProperties generalSettings = load(GENERAL_CONFIG_FILE);
		ALT_ARENA_EXP = generalSettings.getProperty("ArenaExp", true);
		ALT_GAME_EXP_LOST = generalSettings.getProperty("AltGameExpLost", false);
		ALT_SAVE_UNSAVEABLE = generalSettings.getProperty("AltSaveUnsaveable", false);
		ALT_SAVE_EFFECTS_REMAINING_TIME = generalSettings.getProperty("AltSaveEffectsRemainingTime", 5);
		ALT_SHOW_REUSE_MSG = generalSettings.getProperty("AltShowSkillReuseMessage", true);
		ALT_DELETE_SA_BUFFS = generalSettings.getProperty("AltDeleteSABuffs", false);
		AUTO_LOOT = generalSettings.getProperty("AutoLoot", false);
		AUTO_LOOT_HERBS = generalSettings.getProperty("AutoLootHerbs", false);
		AUTO_LOOT_INDIVIDUAL = generalSettings.getProperty("AutoLootIndividual", false);
		AUTO_LOOT_FROM_RAIDS = generalSettings.getProperty("AutoLootFromRaids", false);
		AUTO_LOOT_PK = generalSettings.getProperty("AutoLootPK", false);
		ALT_GAME_KARMA_PLAYER_CAN_SHOP = generalSettings.getProperty("AltKarmaPlayerCanShop", false);
		SAVING_SPS = generalSettings.getProperty("SavingSpS", false);
		MANAHEAL_SPS_BONUS = generalSettings.getProperty("ManahealSpSBonus", false);
		// CRAFT_MASTERWORK_CHANCE = generalSettings.getProperty("CraftMasterworkChance", 3.);
		// CRAFT_DOUBLECRAFT_CHANCE = generalSettings.getProperty("CraftDoubleCraftChance", 3.);
		ALT_RAID_RESPAWN_MULTIPLIER = generalSettings.getProperty("AltRaidRespawnMultiplier", 1.0);
		ALT_ALLOW_AUGMENT_ALL = generalSettings.getProperty("AugmentAll", false);
		ALT_ALLOW_DROP_AUGMENTED = generalSettings.getProperty("AlowDropAugmented", false);
		ALT_GAME_UNREGISTER_RECIPE = generalSettings.getProperty("AltUnregisterRecipe", true);
		ALT_GAME_SHOW_DROPLIST = generalSettings.getProperty("AltShowDroplist", true);
		ALLOW_NPC_SHIFTCLICK = generalSettings.getProperty("AllowShiftClick", false);
		ALT_FULL_NPC_STATS_PAGE = generalSettings.getProperty("AltFullStatsPage", false);
		ALT_GAME_LEVEL_TO_GET_SUBCLASS = generalSettings.getProperty("AltLevelToGetSubclass", 75);
		ALT_GAME_SUB_BOOK = generalSettings.getProperty("AltSubBook", false);
		ALT_GAME_RESET_CERTIFICATION_COST = generalSettings.getProperty("AltResetCertificationCost", 10000000);
		ALT_GAME_RESET_DUALCERTIFICATION_COST = generalSettings.getProperty("AltResetDualCertificationCost", 20000000);
		ALT_GAME_REMOVE_PREVIOUS_CERTIFICATES = generalSettings.getProperty("AltRemovePreviousCertificates", false);
		ALT_GAME_DUALCLASS_REAWAKENING_COST = generalSettings.getProperty("AltGameDualClassReawakeningCost", new double[]
		{
			100,
			90,
			80,
			70,
			60,
			50,
			40,
			30,
			20,
			10
		});
		
		if (ALT_GAME_DUALCLASS_REAWAKENING_COST.length != 10)
		{
			double[] DefaultValues = new double[]
			{
				100,
				90,
				80,
				70,
				60,
				50,
				40,
				30,
				20,
				10
			};
			ALT_GAME_DUALCLASS_REAWAKENING_COST = DefaultValues;
			_log.warn("altGameReawakeningCost - Incorrect values for corresponding levels, loaded default values.");
		}
		
		ALT_MAX_LEVEL = Math.min(generalSettings.getProperty("AltMaxLevel", 99), Experience.LEVEL.length - 1);
		ALT_MAX_SUB_LEVEL = Math.min(generalSettings.getProperty("AltMaxSubLevel", 80), Experience.LEVEL.length - 1);
		ALT_MAX_DUAL_SUB_LEVEL = Math.min(generalSettings.getProperty("AltMaxDualSubLevel", 99), Experience.LEVEL.length - 1);
		ALT_ALLOW_OTHERS_WITHDRAW_FROM_CLAN_WAREHOUSE = generalSettings.getProperty("AltAllowOthersWithdrawFromClanWarehouse", false);
		ALT_ALLOW_CLAN_COMMAND_ONLY_FOR_CLAN_LEADER = generalSettings.getProperty("AltAllowClanCommandOnlyForClanLeader", true);
		// ALT_GAME_REQUIRE_CLAN_CASTLE = generalSettings.getProperty("AltRequireClanCastle", false);
		// ALT_GAME_REQUIRE_CASTLE_DAWN = generalSettings.getProperty("AltRequireCastleDawn", true);
		// ALT_GAME_ALLOW_ADENA_DAWN = generalSettings.getProperty("AltAllowAdenaDawn", true);
		ALT_CLAN_PLAYER_COUNT_6LVL = generalSettings.getProperty("AltClanPlayer_6", 30);
		ALT_CLAN_REP_COUNT_6LVL = generalSettings.getProperty("AltClanRep_6", 5000);
		ALT_CLAN_PLAYER_COUNT_7LVL = generalSettings.getProperty("AltClanPlayer_7", 50);
		ALT_CLAN_REP_COUNT_7LVL = generalSettings.getProperty("AltClanRep_7", 10000);
		ALT_CLAN_PLAYER_COUNT_8LVL = generalSettings.getProperty("AltClanPlayer_8", 80);
		ALT_CLAN_REP_COUNT_8LVL = generalSettings.getProperty("AltClanRep_8", 20000);
		ALT_CLAN_PLAYER_COUNT_9LVL = generalSettings.getProperty("AltClanPlayer_9", 120);
		ALT_CLAN_REP_COUNT_9LVL = generalSettings.getProperty("AltClanRep_9", 40000);
		ALT_CLAN_PLAYER_COUNT_10LVL = generalSettings.getProperty("AltClanPlayer_10", 140);
		ALT_CLAN_REP_COUNT_10LVL = generalSettings.getProperty("AltClanRep_10", 75000);
		ALT_CLAN_PLAYER_COUNT_11LVL = generalSettings.getProperty("AltClanPlayer_11", 170);
		ALT_CLAN_REP_COUNT_11LVL = generalSettings.getProperty("AltClanRep_11", 75000);
		ALT_ADD_RECIPES = generalSettings.getProperty("AltAddRecipes", 0);
		// SS_ANNOUNCE_PERIOD = generalSettings.getProperty("SSAnnouncePeriod", 0);
		PETITIONING_ALLOWED = generalSettings.getProperty("PetitioningAllowed", true);
		MAX_PETITIONS_PER_PLAYER = generalSettings.getProperty("MaxPetitionsPerPlayer", 5);
		MAX_PETITIONS_PENDING = generalSettings.getProperty("MaxPetitionsPending", 25);
		AUTO_LEARN_SKILLS = generalSettings.getProperty("AutoLearnSkills", false);
		AUTO_LEARN_FORGOTTEN_SKILLS = generalSettings.getProperty("AutoLearnForgottenSkills", false);
		ENCHANT_SKILLSID_RETAIL = generalSettings.getProperty("EnchantSkillsIdRetail", false);
		ALT_SOCIAL_ACTION_REUSE = generalSettings.getProperty("AltSocialActionReuse", false);
		ALT_DISABLE_SPELLBOOKS = generalSettings.getProperty("AltDisableSpellbooks", false);
		ALT_DELETE_SKILL_PROF = generalSettings.getProperty("AltDeleteSkillProf", false);
		ALT_DELETE_SKILL_RELATION = generalSettings.getProperty("AltDeleteSkillRelation", false);
		ALT_DELETE_AWAKEN_SKILL_FROM_DB = generalSettings.getProperty("AltDeleteAwakenSkillFromDB", true);
		ALT_CHECK_SKILLS_AWAKENING = generalSettings.getProperty("AltCheckSkillsPostAwakening", false);
		ALT_SIMPLE_SIGNS = generalSettings.getProperty("PushkinSignsOptions", false);
		ALT_TELE_TO_CATACOMBS = generalSettings.getProperty("TeleToCatacombs", false);
		ALT_BS_CRYSTALLIZE = generalSettings.getProperty("BSCrystallize", false);
		// ALT_MAMMON_UPGRADE = generalSettings.getProperty("MammonUpgrade", 6680500);
		// ALT_MAMMON_EXCHANGE = generalSettings.getProperty("MammonExchange", 10091400);
		ALT_ALLOW_TATTOO = generalSettings.getProperty("AllowTattoo", false);
		ALT_BUFF_LIMIT = generalSettings.getProperty("BuffLimit", 20);
		ALT_DEATH_PENALTY = generalSettings.getProperty("EnableAltDeathPenalty", false);
		ALLOW_DEATH_PENALTY_C5 = generalSettings.getProperty("EnableDeathPenaltyC5", true);
		ALT_DEATH_PENALTY_C5_CHANCE = generalSettings.getProperty("DeathPenaltyC5Chance", 10);
		ALT_DEATH_PENALTY_C5_EXPERIENCE_PENALTY = generalSettings.getProperty("DeathPenaltyC5RateExpPenalty", 1);
		ALT_DEATH_PENALTY_C5_KARMA_PENALTY = generalSettings.getProperty("DeathPenaltyC5RateKarma", 1);
		ALT_PK_DEATH_RATE = generalSettings.getProperty("AltPKDeathRate", 0.);
		NONOWNER_ITEM_PICKUP_DELAY = generalSettings.getProperty("NonOwnerItemPickupDelay", 15L) * 1000L;
		ALT_NO_LASTHIT = generalSettings.getProperty("NoLasthitOnRaid", false);
		ALT_KAMALOKA_NIGHTMARES_PREMIUM_ONLY = generalSettings.getProperty("KamalokaNightmaresPremiumOnly", false);
		// ALT_KAMALOKA_NIGHTMARE_REENTER = generalSettings.getProperty("SellReenterNightmaresTicket", true);
		// ALT_KAMALOKA_ABYSS_REENTER = generalSettings.getProperty("SellReenterAbyssTicket", true);
		// ALT_KAMALOKA_LAB_REENTER = generalSettings.getProperty("SellReenterLabyrinthTicket", true);
		ALT_PET_HEAL_BATTLE_ONLY = generalSettings.getProperty("PetsHealOnlyInBattle", true);
		CHAR_TITLE = generalSettings.getProperty("CharTitle", false);
		ADD_CHAR_TITLE = generalSettings.getProperty("CharAddTitle", "");
		ALT_ALLOW_SELL_COMMON = generalSettings.getProperty("AllowSellCommon", true);
		ALT_ALLOW_SHADOW_WEAPONS = generalSettings.getProperty("AllowShadowWeapons", true);
		ALT_DISABLED_MULTISELL = generalSettings.getProperty("DisabledMultisells", ArrayUtils.EMPTY_INT_ARRAY);
		ALT_SHOP_PRICE_LIMITS = generalSettings.getProperty("ShopPriceLimits", ArrayUtils.EMPTY_INT_ARRAY);
		ALT_SHOP_UNALLOWED_ITEMS = generalSettings.getProperty("ShopUnallowedItems", ArrayUtils.EMPTY_INT_ARRAY);
		ALT_ALLOWED_PET_POTIONS = generalSettings.getProperty("AllowedPetPotions", new int[]
		{
			735,
			1060,
			1061,
			1062,
			1374,
			1375,
			1539,
			1540,
			6035,
			6036
		});
		// ALLOW_CLANSKILLS = generalSettings.getProperty("AllowClanSkills", true);
		ALLOW_LEARN_TRANS_SKILLS_WO_QUEST = generalSettings.getProperty("AllowLearnTransSkillsWOQuest", false);
		PARTY_LEADER_ONLY_CAN_INVITE = generalSettings.getProperty("PartyLeaderOnlyCanInvite", true);
		PARTY_MATCHMAKING_ON_ENTERWORLD = generalSettings.getProperty("PartyMatchmakingOnEnterWorld", false);
		ALLOW_TALK_WHILE_SITTING = generalSettings.getProperty("AllowTalkWhileSitting", true);
		ALLOW_NOBLE_TP_TO_ALL = generalSettings.getProperty("AllowNobleTPToAll", false);
		ALLOW_FAKE_PLAYERS = generalSettings.getProperty("AllowFakePlayers", false);
		ALLOW_TOTAL_ONLINE = generalSettings.getProperty("AllowVoiceCommandOnline", false);
		FAKE_PLAYERS_PERCENT = generalSettings.getProperty("FakePlayersPercent", 100);
		CLANHALL_BUFFTIME_MODIFIER = generalSettings.getProperty("ClanHallBuffTimeModifier", 1.0);
		SONGDANCETIME_MODIFIER = generalSettings.getProperty("SongDanceTimeModifier", 1.0);
		MAXLOAD_MODIFIER = generalSettings.getProperty("MaxLoadModifier", 1.0);
		GATEKEEPER_MODIFIER = generalSettings.getProperty("GkCostMultiplier", 1.0);
		GATEKEEPER_FREE = generalSettings.getProperty("GkFree", 40);
		CRUMA_GATEKEEPER_LVL = generalSettings.getProperty("GkCruma", 65);
		ALT_IMPROVED_PETS_LIMITED_USE = generalSettings.getProperty("ImprovedPetsLimitedUse", false);
		ALT_CHAMPION_CHANCE1 = generalSettings.getProperty("AltChampionChance1", 0.);
		ALT_CHAMPION_CHANCE2 = generalSettings.getProperty("AltChampionChance2", 0.);
		ALT_CHAMPION_CAN_BE_AGGRO = generalSettings.getProperty("AltChampionAggro", false);
		ALT_CHAMPION_CAN_BE_SOCIAL = generalSettings.getProperty("AltChampionSocial", false);
		ALT_CHAMPION_TOP_LEVEL = generalSettings.getProperty("AltChampionTopLevel", 75);
		ALT_VITALITY_ENABLED = generalSettings.getProperty("AltVitalityEnabled", true);
		ALT_VITALITY_RATE = generalSettings.getProperty("AltVitalityRate", 2.);
		ALT_VITALITY_CONSUME_RATE = generalSettings.getProperty("AltVitalityConsumeRate", 1.);
		ALT_PCBANG_POINTS_ENABLED = generalSettings.getProperty("AltPcBangPointsEnabled", false);
		ALT_PCBANG_POINTS_BONUS_DOUBLE_CHANCE = generalSettings.getProperty("AltPcBangPointsDoubleChance", 10.);
		ALT_PCBANG_POINTS_BONUS = generalSettings.getProperty("AltPcBangPointsBonus", 0);
		ALT_PCBANG_POINTS_DELAY = generalSettings.getProperty("AltPcBangPointsDelay", 20);
		ALT_PCBANG_POINTS_MIN_LVL = generalSettings.getProperty("AltPcBangPointsMinLvl", 1);
		ALT_DEBUG_ENABLED = generalSettings.getProperty("AltDebugEnabled", false);
		ALT_DEBUG_PVP_ENABLED = generalSettings.getProperty("AltDebugPvPEnabled", false);
		ALT_DEBUG_PVP_DUEL_ONLY = generalSettings.getProperty("AltDebugPvPDuelOnly", true);
		ALT_DEBUG_PVE_ENABLED = generalSettings.getProperty("AltDebugPvEEnabled", false);
		ALT_MAX_ALLY_SIZE = generalSettings.getProperty("AltMaxAllySize", 3);
		ALT_PARTY_DISTRIBUTION_RANGE = generalSettings.getProperty("AltPartyDistributionRange", 1500);
		ALT_PARTY_BONUS = generalSettings.getProperty("AltPartyBonus", new double[]
		{
			1.00,
			1.10,
			1.20,
			1.30,
			1.40,
			1.50,
			2.00,
			2.10,
			2.20
		});
		// ALT_ALL_PHYS_SKILLS_OVERHIT = generalSettings.getProperty("AltAllPhysSkillsOverhit", true);
		ALT_REMOVE_SKILLS_ON_DELEVEL = generalSettings.getProperty("AltRemoveSkillsOnDelevel", true);
		// ALLOW_CH_DOOR_OPEN_ON_CLICK = generalSettings.getProperty("AllowChDoorOpenOnClick", true);
		ALT_CH_ALL_BUFFS = generalSettings.getProperty("AltChAllBuffs", false);
		ALT_CH_ALLOW_1H_BUFFS = generalSettings.getProperty("AltChAllowHourBuff", false);
		// ALT_CH_SIMPLE_DIALOG = generalSettings.getProperty("AltChSimpleDialog", false);
		BEAUTY_SHOP_COIN_ITEM_ID = generalSettings.getProperty("CoinForBeautyShop", 36308);
		AUGMENTATION_NG_SKILL_CHANCE = generalSettings.getProperty("AugmentationNGSkillChance", 15);
		AUGMENTATION_NG_GLOW_CHANCE = generalSettings.getProperty("AugmentationNGGlowChance", 0);
		AUGMENTATION_MID_SKILL_CHANCE = generalSettings.getProperty("AugmentationMidSkillChance", 30);
		AUGMENTATION_MID_GLOW_CHANCE = generalSettings.getProperty("AugmentationMidGlowChance", 40);
		AUGMENTATION_HIGH_SKILL_CHANCE = generalSettings.getProperty("AugmentationHighSkillChance", 45);
		AUGMENTATION_HIGH_GLOW_CHANCE = generalSettings.getProperty("AugmentationHighGlowChance", 70);
		AUGMENTATION_TOP_SKILL_CHANCE = generalSettings.getProperty("AugmentationTopSkillChance", 60);
		AUGMENTATION_TOP_GLOW_CHANCE = generalSettings.getProperty("AugmentationTopGlowChance", 100);
		AUGMENTATION_BASESTAT_CHANCE = generalSettings.getProperty("AugmentationBaseStatChance", 1);
		AUGMENTATION_ACC_SKILL_CHANCE = generalSettings.getProperty("AugmentationAccSkillChance", 10);
		ALT_OPEN_CLOAK_SLOT = generalSettings.getProperty("OpenCloakSlot", false);
		ALT_SHOW_SERVER_TIME = generalSettings.getProperty("ShowServerTime", false);
		FOLLOW_RANGE = generalSettings.getProperty("FollowRange", 100);
		ALT_ITEM_AUCTION_ENABLED = generalSettings.getProperty("AltItemAuctionEnabled", true);
		ALT_ITEM_AUCTION_CAN_REBID = generalSettings.getProperty("AltItemAuctionCanRebid", false);
		ALT_ITEM_AUCTION_START_ANNOUNCE = generalSettings.getProperty("AltItemAuctionAnnounce", true);
		ALT_ITEM_AUCTION_BID_ITEM_ID = generalSettings.getProperty("AltItemAuctionBidItemId", 57);
		ALT_ITEM_AUCTION_MAX_BID = generalSettings.getProperty("AltItemAuctionMaxBid", 1000000L);
		ALT_ITEM_AUCTION_MAX_CANCEL_TIME_IN_MILLIS = generalSettings.getProperty("AltItemAuctionMaxCancelTimeInMillis", 604800000);
		ALT_FISH_CHAMPIONSHIP_ENABLED = generalSettings.getProperty("AltFishChampionshipEnabled", true);
		ALT_FISH_CHAMPIONSHIP_REWARD_ITEM = generalSettings.getProperty("AltFishChampionshipRewardItemId", 57);
		ALT_FISH_CHAMPIONSHIP_REWARD_1 = generalSettings.getProperty("AltFishChampionshipReward1", 800000);
		ALT_FISH_CHAMPIONSHIP_REWARD_2 = generalSettings.getProperty("AltFishChampionshipReward2", 500000);
		ALT_FISH_CHAMPIONSHIP_REWARD_3 = generalSettings.getProperty("AltFishChampionshipReward3", 300000);
		ALT_FISH_CHAMPIONSHIP_REWARD_4 = generalSettings.getProperty("AltFishChampionshipReward4", 200000);
		ALT_FISH_CHAMPIONSHIP_REWARD_5 = generalSettings.getProperty("AltFishChampionshipReward5", 100000);
		ALT_ENABLE_BLOCK_CHECKER_EVENT = generalSettings.getProperty("EnableBlockCheckerEvent", true);
		ALT_MIN_BLOCK_CHECKER_TEAM_MEMBERS = Math.min(Math.max(generalSettings.getProperty("BlockCheckerMinTeamMembers", 1), 1), 6);
		ALT_RATE_COINS_REWARD_BLOCK_CHECKER = generalSettings.getProperty("BlockCheckerRateCoinReward", 1.);
		ALT_HBCE_FAIR_PLAY = generalSettings.getProperty("HBCEFairPlay", false);
		ALT_PET_INVENTORY_LIMIT = generalSettings.getProperty("AltPetInventoryLimit", 12);
		SAVE_GM_SPAWN_CUSTOM = generalSettings.getProperty("SaveGmSpawnCustom", false);
		LOAD_GM_SPAWN_CUSTOM = generalSettings.getProperty("LoadGmSpawnCustom", false);
	}
	
	/**
	 * Method loadServicesSettings.
	 */
	private static void loadServicesSettings()
	{
		ExProperties servicesSettings = load(SERVICES_CONFIG_FILE);
		
		for (int id : servicesSettings.getProperty("AllowClassMasters", ArrayUtils.EMPTY_INT_ARRAY))
		{
			if (id != 0)
			{
				ALLOW_CLASS_MASTERS_LIST.add(id);
			}
		}
		
		CLASS_MASTERS_PRICE = servicesSettings.getProperty("ClassMastersPrice", "0,0,0,0");
		
		if (CLASS_MASTERS_PRICE.length() >= 7)
		{
			int level = 1;
			
			for (String id : CLASS_MASTERS_PRICE.split(","))
			{
				CLASS_MASTERS_PRICE_LIST[level] = Integer.parseInt(id);
				level++;
			}
		}
		
		CLASS_MASTERS_PRICE_ITEM = servicesSettings.getProperty("ClassMastersPriceItem", "0,0,0,0");
		
		if (CLASS_MASTERS_PRICE_ITEM.length() >= 7)
		{
			int level = 1;
			
			for (String id : CLASS_MASTERS_PRICE_ITEM.split(","))
			{
				CLASS_MASTERS_PRICE_ITEM_LIST[level] = Integer.parseInt(id);
				level++;
			}
		}
		
		// SERVICES_CHANGE_NICK_ENABLED = servicesSettings.getProperty("NickChangeEnabled", false);
		SERVICES_CHANGE_NICK_PRICE = servicesSettings.getProperty("NickChangePrice", 100);
		SERVICES_CHANGE_NICK_ITEM = servicesSettings.getProperty("NickChangeItem", 4037);
		// SERVICES_CHANGE_CLAN_NAME_ENABLED = servicesSettings.getProperty("ClanNameChangeEnabled", false);
		SERVICES_CHANGE_CLAN_NAME_PRICE = servicesSettings.getProperty("ClanNameChangePrice", 100);
		SERVICES_CHANGE_CLAN_NAME_ITEM = servicesSettings.getProperty("ClanNameChangeItem", 4037);
		SERVICES_CHANGE_PET_NAME_ENABLED = servicesSettings.getProperty("PetNameChangeEnabled", false);
		SERVICES_CHANGE_PET_NAME_PRICE = servicesSettings.getProperty("PetNameChangePrice", 100);
		SERVICES_CHANGE_PET_NAME_ITEM = servicesSettings.getProperty("PetNameChangeItem", 4037);
		SERVICES_EXCHANGE_BABY_PET_ENABLED = servicesSettings.getProperty("BabyPetExchangeEnabled", false);
		SERVICES_EXCHANGE_BABY_PET_PRICE = servicesSettings.getProperty("BabyPetExchangePrice", 100);
		SERVICES_EXCHANGE_BABY_PET_ITEM = servicesSettings.getProperty("BabyPetExchangeItem", 4037);
		// SERVICES_CHANGE_SEX_ENABLED = servicesSettings.getProperty("SexChangeEnabled", false);
		SERVICES_CHANGE_SEX_PRICE = servicesSettings.getProperty("SexChangePrice", 100);
		SERVICES_CHANGE_SEX_ITEM = servicesSettings.getProperty("SexChangeItem", 4037);
		// SERVICES_CHANGE_BASE_ENABLED = servicesSettings.getProperty("BaseChangeEnabled", false);
		SERVICES_CHANGE_BASE_PRICE = servicesSettings.getProperty("BaseChangePrice", 100);
		SERVICES_CHANGE_BASE_ITEM = servicesSettings.getProperty("BaseChangeItem", 4037);
		// SERVICES_SEPARATE_SUB_ENABLED = servicesSettings.getProperty("SeparateSubEnabled", false);
		SERVICES_SEPARATE_SUB_PRICE = servicesSettings.getProperty("SeparateSubPrice", 100);
		SERVICES_SEPARATE_SUB_ITEM = servicesSettings.getProperty("SeparateSubItem", 4037);
		// SERVICES_CHANGE_NICK_COLOR_ENABLED = servicesSettings.getProperty("NickColorChangeEnabled", false);
		SERVICES_CHANGE_NICK_COLOR_PRICE = servicesSettings.getProperty("NickColorChangePrice", 100);
		SERVICES_CHANGE_NICK_COLOR_ITEM = servicesSettings.getProperty("NickColorChangeItem", 4037);
		SERVICES_CHANGE_NICK_COLOR_LIST = servicesSettings.getProperty("NickColorChangeList", new String[]
		{
			"00FF00"
		});
		SERVICES_BASH_ENABLED = servicesSettings.getProperty("BashEnabled", false);
		SERVICES_BASH_SKIP_DOWNLOAD = servicesSettings.getProperty("BashSkipDownload", false);
		SERVICES_BASH_RELOAD_TIME = servicesSettings.getProperty("BashReloadTime", 24);
		SERVICES_RATE_TYPE = servicesSettings.getProperty("RateBonusType", Bonus.NO_BONUS);
		SERVICES_RATE_BONUS_PRICE = servicesSettings.getProperty("RateBonusPrice", new int[]
		{
			1500
		});
		SERVICES_RATE_BONUS_ITEM = servicesSettings.getProperty("RateBonusItem", new int[]
		{
			4037
		});
		SERVICES_RATE_BONUS_VALUE = servicesSettings.getProperty("RateBonusValue", new double[]
		{
			1.25
		});
		SERVICES_RATE_BONUS_DAYS = servicesSettings.getProperty("RateBonusTime", new int[]
		{
			30
		});
		// SERVICES_NOBLESS_SELL_ENABLED = servicesSettings.getProperty("NoblessSellEnabled", false);
		SERVICES_NOBLESS_SELL_PRICE = servicesSettings.getProperty("NoblessSellPrice", 1000);
		SERVICES_NOBLESS_SELL_ITEM = servicesSettings.getProperty("NoblessSellItem", 4037);
		SERVICES_HERO_SELL_ENABLED = servicesSettings.getProperty("HeroSellEnabled", false);
		SERVICES_HERO_SELL_DAY = servicesSettings.getProperty("HeroSellDay", new int[]
		{
			30
		});
		SERVICES_HERO_SELL_PRICE = servicesSettings.getProperty("HeroSellPrice", new int[]
		{
			30
		});
		SERVICES_HERO_SELL_ITEM = servicesSettings.getProperty("HeroSellItem", new int[]
		{
			4037
		});
		SERVICES_EXPAND_INVENTORY_ENABLED = servicesSettings.getProperty("ExpandInventoryEnabled", false);
		SERVICES_EXPAND_INVENTORY_PRICE = servicesSettings.getProperty("ExpandInventoryPrice", 1000);
		SERVICES_EXPAND_INVENTORY_ITEM = servicesSettings.getProperty("ExpandInventoryItem", 4037);
		SERVICES_EXPAND_INVENTORY_MAX = servicesSettings.getProperty("ExpandInventoryMax", 250);
		SERVICES_EXPAND_WAREHOUSE_ENABLED = servicesSettings.getProperty("ExpandWarehouseEnabled", false);
		SERVICES_EXPAND_WAREHOUSE_PRICE = servicesSettings.getProperty("ExpandWarehousePrice", 1000);
		SERVICES_EXPAND_WAREHOUSE_ITEM = servicesSettings.getProperty("ExpandWarehouseItem", 4037);
		SERVICES_EXPAND_CWH_ENABLED = servicesSettings.getProperty("ExpandCWHEnabled", false);
		SERVICES_EXPAND_CWH_PRICE = servicesSettings.getProperty("ExpandCWHPrice", 1000);
		SERVICES_EXPAND_CWH_ITEM = servicesSettings.getProperty("ExpandCWHItem", 4037);
		// SERVICES_DELEVEL_ENABLED = servicesSettings.getProperty("DelevelEnabled", false);
		// SERVICES_DELEVEL_PRICE = servicesSettings.getProperty("DelevelPrice", 1);
		// SERVICES_DELEVEL_ITEM = servicesSettings.getProperty("DelevelItem", 4037);
		SERVICES_SELLPETS = servicesSettings.getProperty("SellPets", "");
		SERVICES_OFFLINE_TRADE_ALLOW = servicesSettings.getProperty("AllowOfflineTrade", false);
		SERVICES_OFFLINE_TRADE_ALLOW_OFFSHORE = servicesSettings.getProperty("AllowOfflineTradeOnlyOffshore", true);
		SERVICES_OFFLINE_TRADE_MIN_LEVEL = servicesSettings.getProperty("OfflineMinLevel", 0);
		SERVICES_OFFLINE_TRADE_NAME_COLOR = Integer.decode("0x" + servicesSettings.getProperty("OfflineTradeNameColor", "B0FFFF"));
		SERVICES_OFFLINE_TRADE_PRICE_ITEM = servicesSettings.getProperty("OfflineTradePriceItem", 0);
		SERVICES_OFFLINE_TRADE_PRICE = servicesSettings.getProperty("OfflineTradePrice", 0);
		SERVICES_OFFLINE_TRADE_SECONDS_TO_KICK = servicesSettings.getProperty("OfflineTradeDaysToKick", 14) * 86400L;
		SERVICES_OFFLINE_TRADE_RESTORE_AFTER_RESTART = servicesSettings.getProperty("OfflineRestoreAfterRestart", true);
		SERVICES_NO_TRADE_ONLY_OFFLINE = servicesSettings.getProperty("NoTradeOnlyOffline", false);
		SERVICES_TRADE_TAX = servicesSettings.getProperty("TradeTax", 0.0);
		SERVICES_OFFSHORE_TRADE_TAX = servicesSettings.getProperty("OffshoreTradeTax", 0.0);
		SERVICES_TRADE_TAX_ONLY_OFFLINE = servicesSettings.getProperty("TradeTaxOnlyOffline", false);
		SERVICES_OFFSHORE_NO_CASTLE_TAX = servicesSettings.getProperty("NoCastleTaxInOffshore", false);
		SERVICES_TRADE_ONLY_FAR = servicesSettings.getProperty("TradeOnlyFar", false);
		SERVICES_TRADE_MIN_LEVEL = servicesSettings.getProperty("MinLevelForTrade", 0);
		SERVICES_TRADE_RADIUS = servicesSettings.getProperty("TradeRadius", 30);
		SERVICES_GIRAN_HARBOR_ENABLED = servicesSettings.getProperty("GiranHarborZone", false);
		SERVICES_PARNASSUS_ENABLED = servicesSettings.getProperty("ParnassusZone", false);
		SERVICES_PARNASSUS_NOTAX = servicesSettings.getProperty("ParnassusNoTax", false);
		SERVICES_PARNASSUS_PRICE = servicesSettings.getProperty("ParnassusPrice", 500000);
		SERVICES_ALLOW_LOTTERY = servicesSettings.getProperty("AllowLottery", false);
		SERVICES_LOTTERY_PRIZE = servicesSettings.getProperty("LotteryPrize", 50000);
		SERVICES_ALT_LOTTERY_PRICE = servicesSettings.getProperty("AltLotteryPrice", 2000);
		SERVICES_LOTTERY_TICKET_PRICE = servicesSettings.getProperty("LotteryTicketPrice", 2000);
		SERVICES_LOTTERY_5_NUMBER_RATE = servicesSettings.getProperty("Lottery5NumberRate", 0.6);
		SERVICES_LOTTERY_4_NUMBER_RATE = servicesSettings.getProperty("Lottery4NumberRate", 0.4);
		SERVICES_LOTTERY_3_NUMBER_RATE = servicesSettings.getProperty("Lottery3NumberRate", 0.2);
		SERVICES_LOTTERY_2_AND_1_NUMBER_PRIZE = servicesSettings.getProperty("Lottery2and1NumberPrize", 200);
		SERVICES_ALLOW_ROULETTE = servicesSettings.getProperty("AllowRoulette", false);
		SERVICES_ROULETTE_MIN_BET = servicesSettings.getProperty("RouletteMinBet", 1L);
		SERVICES_ROULETTE_MAX_BET = servicesSettings.getProperty("RouletteMaxBet", Long.MAX_VALUE);
		SERVICES_ENABLE_NO_CARRIER = servicesSettings.getProperty("EnableNoCarrier", false);
		// SERVICES_NO_CARRIER_MIN_TIME = servicesSettings.getProperty("NoCarrierMinTime", 0);
		// SERVICES_NO_CARRIER_MAX_TIME = servicesSettings.getProperty("NoCarrierMaxTime", 90);
		SERVICES_NO_CARRIER_DEFAULT_TIME = servicesSettings.getProperty("NoCarrierDefaultTime", 60);
		ITEM_BROKER_ITEM_SEARCH = servicesSettings.getProperty("UseItemBrokerItemSearch", false);
		ALLOW_EVENT_GATEKEEPER = servicesSettings.getProperty("AllowEventGatekeeper", false);
	}
	
	/**
	 * Method loadPvPSettings.
	 */
	private static void loadPvPSettings()
	{
		ExProperties pvpSettings = load(PVP_CONFIG_FILE);
		KARMA_MIN_KARMA = pvpSettings.getProperty("MinKarma", 240);
		KARMA_SP_DIVIDER = pvpSettings.getProperty("SPDivider", 7);
		KARMA_LOST_BASE = pvpSettings.getProperty("BaseKarmaLost", 0);
		KARMA_DROP_GM = pvpSettings.getProperty("CanGMDropEquipment", false);
		KARMA_NEEDED_TO_DROP = pvpSettings.getProperty("KarmaNeededToDrop", true);
		DROP_ITEMS_ON_DIE = pvpSettings.getProperty("DropOnDie", false);
		DROP_ITEMS_AUGMENTED = pvpSettings.getProperty("DropAugmented", false);
		KARMA_DROP_ITEM_LIMIT = pvpSettings.getProperty("MaxItemsDroppable", 10);
		MIN_PK_TO_ITEMS_DROP = pvpSettings.getProperty("MinPKToDropItems", 5);
		KARMA_RANDOM_DROP_LOCATION_LIMIT = pvpSettings.getProperty("MaxDropThrowDistance", 70);
		KARMA_DROPCHANCE_BASE = pvpSettings.getProperty("ChanceOfPKDropBase", 20.);
		KARMA_DROPCHANCE_MOD = pvpSettings.getProperty("ChanceOfPKsDropMod", 1.);
		NORMAL_DROPCHANCE_BASE = pvpSettings.getProperty("ChanceOfNormalDropBase", 1.);
		DROPCHANCE_EQUIPPED_WEAPON = pvpSettings.getProperty("ChanceOfDropWeapon", 3);
		DROPCHANCE_EQUIPMENT = pvpSettings.getProperty("ChanceOfDropEquippment", 17);
		DROPCHANCE_ITEM = pvpSettings.getProperty("ChanceOfDropOther", 80);
		KARMA_LIST_NONDROPPABLE_ITEMS = new ArrayList<>();
		
		for (int id : pvpSettings.getProperty("ListOfNonDroppableItems", new int[]
		{
			57,
			1147,
			425,
			1146,
			461,
			10,
			2368,
			7,
			6,
			2370,
			2369,
			3500,
			3501,
			3502,
			4422,
			4423,
			4424,
			2375,
			6648,
			6649,
			6650,
			6842,
			6834,
			6835,
			6836,
			6837,
			6838,
			6839,
			6840,
			5575,
			7694,
			6841,
			8181
		}))
		{
			KARMA_LIST_NONDROPPABLE_ITEMS.add(id);
		}
		PVP_TIME = pvpSettings.getProperty("PvPTime", 40000);
		REPUTATION_COUNT = pvpSettings.getProperty("CountReputation", 360);
		PK_KILLER_NAME_COLOR = Integer.decode("0x" + pvpSettings.getProperty("PKKillerNameColor", "00FF00"));
	}
	
	/**
	 * Method loadNpcSettings.
	 */
	private static void loadNpcSettings()
	{
		ExProperties npcSettings = load(NPC_CONFIG_FILE);
		AI_TASK_MANAGER_COUNT = npcSettings.getProperty("AiTaskManagers", 1);
		AI_TASK_ATTACK_DELAY = npcSettings.getProperty("AiTaskDelay", 1000);
		AI_TASK_ACTIVE_DELAY = npcSettings.getProperty("AiTaskActiveDelay", 1000);
		BLOCK_ACTIVE_TASKS = npcSettings.getProperty("BlockActiveTasks", false);
		ALWAYS_TELEPORT_HOME = npcSettings.getProperty("AlwaysTeleportHome", false);
		RND_WALK = npcSettings.getProperty("RndWalk", true);
		RND_WALK_RATE = npcSettings.getProperty("RndWalkRate", 1);
		RND_ANIMATION_RATE = npcSettings.getProperty("RndAnimationRate", 2);
		AGGRO_CHECK_INTERVAL = npcSettings.getProperty("AggroCheckInterval", 250);
		NONAGGRO_TIME_ONTELEPORT = npcSettings.getProperty("NonAggroTimeOnTeleport", 15000);
		MAX_DRIFT_RANGE = npcSettings.getProperty("MaxDriftRange", 100);
		MAX_PURSUE_RANGE = npcSettings.getProperty("MaxPursueRange", 4000);
		MAX_PURSUE_UNDERGROUND_RANGE = npcSettings.getProperty("MaxPursueUndergoundRange", 2000);
		MAX_PURSUE_RANGE_RAID = npcSettings.getProperty("MaxPursueRangeRaid", 5000);
	}
	
	/**
	 * Method loadGeodataSettings.
	 */
	private static void loadGeodataSettings()
	{
		ExProperties geodataSettings = load(GEODATA_CONFIG_FILE);
		GEO_X_FIRST = geodataSettings.getProperty("GeoFirstX", 11);
		GEO_Y_FIRST = geodataSettings.getProperty("GeoFirstY", 10);
		GEO_X_LAST = geodataSettings.getProperty("GeoLastX", 26);
		GEO_Y_LAST = geodataSettings.getProperty("GeoLastY", 26);
		GEOFILES_PATTERN = geodataSettings.getProperty("GeoFilesPattern", "(\\d{2}_\\d{2})\\.l2j");
		ALLOW_GEODATA = geodataSettings.getProperty("AllowGeodata", true);
		ALLOW_FALL_FROM_WALLS = geodataSettings.getProperty("AllowFallFromWalls", false);
		// ALLOW_KEYBOARD_MOVE = geodataSettings.getProperty("AllowMoveWithKeyboard", true);
		COMPACT_GEO = geodataSettings.getProperty("CompactGeoData", false);
		CLIENT_Z_SHIFT = geodataSettings.getProperty("ClientZShift", 16);
		PATHFIND_BOOST = geodataSettings.getProperty("PathFindBoost", 2);
		PATHFIND_DIAGONAL = geodataSettings.getProperty("PathFindDiagonal", true);
		PATH_CLEAN = geodataSettings.getProperty("PathClean", true);
		PATHFIND_MAX_Z_DIFF = geodataSettings.getProperty("PathFindMaxZDiff", 32);
		MAX_Z_DIFF = geodataSettings.getProperty("MaxZDiff", 64);
		MIN_LAYER_HEIGHT = geodataSettings.getProperty("MinLayerHeight", 64);
		PATHFIND_MAX_TIME = geodataSettings.getProperty("PathFindMaxTime", 10000000);
		PATHFIND_BUFFERS = geodataSettings.getProperty("PathFindBuffers", "8x96;8x128;8x160;8x192;4x224;4x256;4x288;2x320;2x384;2x352;1x512");
	}
	
	/**
	 * Method loadEventsSettings.
	 */
	private static void loadEventsSettings()
	{
		ExProperties eventSettings = load(EVENTS_CONFIG_FILE);
		EVENT_CofferOfShadowsPriceRate = eventSettings.getProperty("CofferOfShadowsPriceRate", 1.);
		// EVENT_CofferOfShadowsRewardRate = eventSettings.getProperty("CofferOfShadowsRewardRate", 1.);
		EVENT_LastHeroItemID = eventSettings.getProperty("LastHero_bonus_id", 57);
		EVENT_LastHeroItemCOUNT = eventSettings.getProperty("LastHero_bonus_count", 5000.0);
		EVENT_LastHeroRate = eventSettings.getProperty("LastHero_rate", true);
		EVENT_LastHeroItemCOUNTFinal = eventSettings.getProperty("LastHero_bonus_count_final", 10000.0);
		EVENT_LastHeroRateFinal = eventSettings.getProperty("LastHero_rate_final", true);
		EVENT_LHTime = eventSettings.getProperty("LH_time", 3);
		EVENT_LHStartTime = eventSettings.getProperty("LH_StartTime", "20:00").trim().replaceAll(" ", "").split(",");
		EVENT_LHCategories = eventSettings.getProperty("LH_Categories", false);
		EVENT_LHAllowSummons = eventSettings.getProperty("LH_AllowSummons", false);
		EVENT_LHAllowBuffs = eventSettings.getProperty("LH_AllowBuffs", false);
		EVENT_LHAllowMultiReg = eventSettings.getProperty("LH_AllowMultiReg", false);
		EVENT_LHCheckWindowMethod = eventSettings.getProperty("LH_CheckWindowMethod", "IP");
		EVENT_LHEventRunningTime = eventSettings.getProperty("LH_EventRunningTime", 20);
		EVENT_LHFighterBuffs = eventSettings.getProperty("LH_FighterBuffs", "").trim().replaceAll(" ", "").split(";");
		EVENT_LHMageBuffs = eventSettings.getProperty("LH_MageBuffs", "").trim().replaceAll(" ", "").split(";");
		EVENT_LHBuffPlayers = eventSettings.getProperty("LH_BuffPlayers", false);
		ALLOW_HEROES_LASTHERO = eventSettings.getProperty("LH_AllowHeroes", true);
		// EVENTS_DISALLOWED_SKILLS = eventSettings.getProperty("DisallowedSkills", "").trim().replaceAll(" ", "").split(";");
		EVENT_TvTRewards = eventSettings.getProperty("TvT_Rewards", "").trim().replaceAll(" ", "").split(";");
		EVENT_TvTTime = eventSettings.getProperty("TvT_time", 3);
		EVENT_TvTStartTime = eventSettings.getProperty("TvT_StartTime", "20:00").trim().replaceAll(" ", "").split(",");
		EVENT_TvTCategories = eventSettings.getProperty("TvT_Categories", false);
		EVENT_TvTMaxPlayerInTeam = eventSettings.getProperty("TvT_MaxPlayerInTeam", 20);
		EVENT_TvTMinPlayerInTeam = eventSettings.getProperty("TvT_MinPlayerInTeam", 2);
		EVENT_TvTAllowSummons = eventSettings.getProperty("TvT_AllowSummons", false);
		EVENT_TvTAllowBuffs = eventSettings.getProperty("TvT_AllowBuffs", false);
		EVENT_TvTAllowMultiReg = eventSettings.getProperty("TvT_AllowMultiReg", false);
		EVENT_TvTCheckWindowMethod = eventSettings.getProperty("TvT_CheckWindowMethod", "IP");
		EVENT_TvTEventRunningTime = eventSettings.getProperty("TvT_EventRunningTime", 20);
		EVENT_TvTFighterBuffs = eventSettings.getProperty("TvT_FighterBuffs", "").trim().replaceAll(" ", "").split(";");
		EVENT_TvTMageBuffs = eventSettings.getProperty("TvT_MageBuffs", "").trim().replaceAll(" ", "").split(";");
		EVENT_TvTBuffPlayers = eventSettings.getProperty("TvT_BuffPlayers", false);
		EVENT_TvTrate = eventSettings.getProperty("TvT_rate", true);
		EVENT_CtFRewards = eventSettings.getProperty("CtF_Rewards", "").trim().replaceAll(" ", "").split(";");
		EVENT_CtfTime = eventSettings.getProperty("CtF_time", 3);
		EVENT_CtFrate = eventSettings.getProperty("CtF_rate", true);
		EVENT_CtFStartTime = eventSettings.getProperty("CtF_StartTime", "20:00").trim().replaceAll(" ", "").split(",");
		EVENT_CtFCategories = eventSettings.getProperty("CtF_Categories", false);
		EVENT_CtFMaxPlayerInTeam = eventSettings.getProperty("CtF_MaxPlayerInTeam", 20);
		EVENT_CtFMinPlayerInTeam = eventSettings.getProperty("CtF_MinPlayerInTeam", 2);
		EVENT_CtFAllowSummons = eventSettings.getProperty("CtF_AllowSummons", false);
		EVENT_CtFAllowBuffs = eventSettings.getProperty("CtF_AllowBuffs", false);
		EVENT_CtFAllowMultiReg = eventSettings.getProperty("CtF_AllowMultiReg", false);
		EVENT_CtFCheckWindowMethod = eventSettings.getProperty("CtF_CheckWindowMethod", "IP");
		EVENT_CtFFighterBuffs = eventSettings.getProperty("CtF_FighterBuffs", "").trim().replaceAll(" ", "").split(";");
		EVENT_CtFMageBuffs = eventSettings.getProperty("CtF_MageBuffs", "").trim().replaceAll(" ", "").split(";");
		EVENT_CtFBuffPlayers = eventSettings.getProperty("CtF_BuffPlayers", false);
		EVENT_TFH_POLLEN_CHANCE = eventSettings.getProperty("TFH_POLLEN_CHANCE", 5.);
		EVENT_GLITTMEDAL_NORMAL_CHANCE = eventSettings.getProperty("MEDAL_CHANCE", 10.);
		EVENT_GLITTMEDAL_GLIT_CHANCE = eventSettings.getProperty("GLITTMEDAL_CHANCE", 0.1);
		EVENT_L2DAY_LETTER_CHANCE = eventSettings.getProperty("L2DAY_LETTER_CHANCE", 1.);
		EVENT_CHANGE_OF_HEART_CHANCE = eventSettings.getProperty("EVENT_CHANGE_OF_HEART_CHANCE", 5.);
		EVENT_APRIL_FOOLS_DROP_CHANCE = eventSettings.getProperty("AprilFoolsDropChance", 50.);
		EVENT_BOUNTY_HUNTERS_ENABLED = eventSettings.getProperty("BountyHuntersEnabled", true);
		EVENT_SAVING_SNOWMAN_LOTERY_PRICE = eventSettings.getProperty("SavingSnowmanLoteryPrice", 50000);
		EVENT_SAVING_SNOWMAN_REWARDER_CHANCE = eventSettings.getProperty("SavingSnowmanRewarderChance", 2);
		EVENT_TRICK_OF_TRANS_CHANCE = eventSettings.getProperty("TRICK_OF_TRANS_CHANCE", 10.);
		EVENT_MARCH8_DROP_CHANCE = eventSettings.getProperty("March8DropChance", 10.);
		EVENT_MARCH8_PRICE_RATE = eventSettings.getProperty("March8PriceRate", 1.);
		// ENCHANT_CHANCE_MASTER_YOGI_STAFF = eventSettings.getProperty("MasterYogiEnchantChance", 66);
		// ENCHANT_MAX_MASTER_YOGI_STAFF = eventSettings.getProperty("MasterYogiEnchantMaxWeapon", 28);
		// SAFE_ENCHANT_MASTER_YOGI_STAFF = eventSettings.getProperty("MasterYogiSafeEnchant", 3);
		// TMEVENTINTERVAL = eventSettings.getProperty("TMEventInterval", 0);
		// TMTIME1 = eventSettings.getProperty("TMTime1", 120000);
		// TMWAVE1COUNT = eventSettings.getProperty("TMWave1Count", 2);
		// TMWAVE2 = eventSettings.getProperty("TMWave2", 18855);
	}
	
	/**
	 * Method loadOlympiadSettings.
	 */
	private static void loadOlympiadSettings()
	{
		ExProperties olympSettings = load(OLYMPIAD_CONFIG_FILE);
		ENABLE_OLYMPIAD = olympSettings.getProperty("EnableOlympiad", true);
		ENABLE_OLYMPIAD_SPECTATING = olympSettings.getProperty("EnableOlympiadSpectating", true);
		ALT_OLY_DAYS = olympSettings.getProperty("AltOlyDays", false);
		ALT_OLY_START_TIME = olympSettings.getProperty("AltOlyStartTime", 18);
		ALT_OLY_MIN = olympSettings.getProperty("AltOlyMin", 0);
		ALT_OLY_CPERIOD = olympSettings.getProperty("AltOlyCPeriod", 21600000);
		ALT_OLY_WPERIOD = olympSettings.getProperty("AltOlyWPeriod", 604800000);
		ALT_OLY_VPERIOD = olympSettings.getProperty("AltOlyVPeriod", 43200000);
		CLASS_GAME_MIN = olympSettings.getProperty("ClassGameMin", 5);
		NONCLASS_GAME_MIN = olympSettings.getProperty("NonClassGameMin", 9);
		// TEAM_GAME_MIN = olympSettings.getProperty("TeamGameMin", 4);
		GAME_MAX_LIMIT = olympSettings.getProperty("GameMaxLimit", 50);
		GAME_CLASSES_COUNT_LIMIT = olympSettings.getProperty("GameClassesCountLimit", 20);
		GAME_NOCLASSES_COUNT_LIMIT = olympSettings.getProperty("GameNoClassesCountLimit", 40);
		// ALT_OLY_REG_DISPLAY = olympSettings.getProperty("AltOlyRegistrationDisplayNumber", 100);
		ALT_OLY_BATTLE_REWARD_ITEM = olympSettings.getProperty("AltOlyBattleRewItem", 13722);
		ALT_OLY_CLASSED_RITEM_C = olympSettings.getProperty("AltOlyClassedRewItemCount", 50);
		ALT_OLY_NONCLASSED_RITEM_C = olympSettings.getProperty("AltOlyNonClassedRewItemCount", 40);
		ALT_OLY_TEAM_RITEM_C = olympSettings.getProperty("AltOlyTeamRewItemCount", 50);
		ALT_OLY_COMP_RITEM = olympSettings.getProperty("AltOlyCompRewItem", 13722);
		ALT_OLY_GP_PER_POINT = olympSettings.getProperty("AltOlyGPPerPoint", 1000);
		ALT_OLY_HERO_POINTS = olympSettings.getProperty("AltOlyHeroPoints", 180);
		ALT_OLY_RANK1_POINTS = olympSettings.getProperty("AltOlyRank1Points", 120);
		ALT_OLY_RANK2_POINTS = olympSettings.getProperty("AltOlyRank2Points", 80);
		ALT_OLY_RANK3_POINTS = olympSettings.getProperty("AltOlyRank3Points", 55);
		ALT_OLY_RANK4_POINTS = olympSettings.getProperty("AltOlyRank4Points", 35);
		ALT_OLY_RANK5_POINTS = olympSettings.getProperty("AltOlyRank5Points", 20);
		OLYMPIAD_STADIAS_COUNT = olympSettings.getProperty("OlympiadStadiasCount", 160);
		OLYMPIAD_BATTLES_FOR_REWARD = olympSettings.getProperty("OlympiadBattlesForReward", 15);
		OLYMPIAD_POINTS_DEFAULT = olympSettings.getProperty("OlympiadPointsDefault", 50);
		OLYMPIAD_POINTS_WEEKLY = olympSettings.getProperty("OlympiadPointsWeekly", 10);
		OLYMPIAD_OLDSTYLE_STAT = olympSettings.getProperty("OlympiadOldStyleStat", false);
	}
	
	/**
	 * Method load.
	 */
	public static void load()
	{
		loadServerConfig();
		loadRatesConfig();
		loadResidenceConfig();
		loadOtherConfig();
		loadVoteRewardSettings();
		loadDonateConfig();
		loadSpoilConfig();
		loadFormulasConfig();
		loadGeneralSettings();
		loadServicesSettings();
		loadPvPSettings();
		loadNpcSettings();
		loadGeodataSettings();
		loadEventsSettings();
		loadOlympiadSettings();
		loadDevelopSettings();
		loadExtSettings();
		loadCommunityConfig();
		abuseLoad();
		loadPlayerAccess();
	}
	
	/**
	 * Constructor for Config.
	 */
	private Config()
	{
	}
	
	/**
	 * Method abuseLoad.
	 */
	public static void abuseLoad()
	{
		List<Pattern> tmp = new ArrayList<>();
		LineNumberReader lnr = null;
		
		try
		{
			String line;
			lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(ABUSEWORDS_CONFIG_FILE), "UTF-8"));
			
			while ((line = lnr.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line, "\n\r");
				
				if (st.hasMoreTokens())
				{
					tmp.add(Pattern.compile(".*" + st.nextToken() + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
				}
			}
			
			ABUSEWORD_LIST = tmp.toArray(new Pattern[tmp.size()]);
			tmp.clear();
			if (DEBUG)
			{
				_log.info("Abuse: Loaded " + ABUSEWORD_LIST.length + " abuse words.");
			}
		}
		catch (IOException e1)
		{
			_log.warn("Error reading abuse: " + e1);
		}
		finally
		{
			try
			{
				if (lnr != null)
				{
					lnr.close();
				}
			}
			catch (Exception e2)
			{
				// empty catch clause
			}
		}
	}
	
	/**
	 * Method loadPlayerAccess.
	 */
	public static void loadPlayerAccess()
	{
		PLAYER_ACCESS.clear();
		File dir = new File(PLAYER_ACCESS_FILES_DIR);
		
		if (!dir.exists() || !dir.isDirectory())
		{
			_log.info("Dir " + dir.getAbsolutePath() + " not exists.");
			return;
		}
		
		for (File file : dir.listFiles())
		{
			if (!file.isDirectory() && file.getName().endsWith(".xml"))
			{
				loadPlayerAccess(file);
			}
		}
	}
	
	/**
	 * Method loadPlayerAccess.
	 * @param file File
	 */
	public static void loadPlayerAccess(File file)
	{
		try
		{
			Field field;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			Document doc = factory.newDocumentBuilder().parse(file);
			
			for (Node nod = doc.getFirstChild(); nod != null; nod = nod.getNextSibling())
			{
				for (Node n = nod.getFirstChild(); n != null; n = n.getNextSibling())
				{
					if (!n.getNodeName().equals("access"))
					{
						continue;
					}
					
					PlayerAccess access = new PlayerAccess();
					
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						Class<?> cls = access.getClass();
						String node = d.getNodeName();
						
						if (node.equals("#text"))
						{
							continue;
						}
						
						try
						{
							field = cls.getField(node);
						}
						catch (NoSuchFieldException e)
						{
							_log.info("Not found desclarate Access: " + node + " in XML Player access Object");
							continue;
						}
						
						if (field.getType().getName().equals("boolean"))
						{
							field.setBoolean(access, Boolean.parseBoolean(d.getAttributes().getNamedItem("set").getNodeValue()));
						}
						else if (field.getType().getName().equals("int"))
						{
							field.setInt(access, Integer.valueOf(d.getAttributes().getNamedItem("set").getNodeValue()));
						}
					}
					
					PLAYER_ACCESS.put(access.AccessLevel, access);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Method getField.
	 * @param fieldName String
	 * @return String
	 */
	public static String getField(String fieldName)
	{
		Field field = FieldUtils.getField(Config.class, fieldName);
		
		if (field == null)
		{
			return null;
		}
		
		try
		{
			return String.valueOf(field.get(null));
		}
		catch (IllegalArgumentException e)
		{
			// empty catch clause
		}
		catch (IllegalAccessException e)
		{
			// empty catch clause
		}
		
		return null;
	}
	
	/**
	 * Method setField.
	 * @param fieldName String
	 * @param value String
	 * @return boolean
	 */
	public static boolean setField(String fieldName, String value)
	{
		Field field = FieldUtils.getField(Config.class, fieldName);
		
		if (field == null)
		{
			return false;
		}
		
		try
		{
			if (field.getType() == boolean.class)
			{
				field.setBoolean(null, BooleanUtils.toBoolean(value));
			}
			else if (field.getType() == int.class)
			{
				field.setInt(null, Integer.valueOf(value));
			}
			else if (field.getType() == long.class)
			{
				field.setLong(null, Long.valueOf(value));
			}
			else if (field.getType() == double.class)
			{
				field.setDouble(null, Double.valueOf(value));
			}
			else if (field.getType() == String.class)
			{
				field.set(null, value);
			}
			else
			{
				return false;
			}
		}
		catch (IllegalArgumentException e)
		{
			return false;
		}
		catch (IllegalAccessException e)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Method load.
	 * @param filename String
	 * @return ExProperties
	 */
	public static ExProperties load(String filename)
	{
		return load(new File(filename));
	}
	
	/**
	 * Method load.
	 * @param file File
	 * @return ExProperties
	 */
	private static ExProperties load(File file)
	{
		ExProperties result = new ExProperties();
		
		try
		{
			result.load(file);
		}
		catch (IOException e)
		{
			_log.error("Error loading config : " + file.getName() + "!");
		}
		
		return result;
	}
	
	/**
	 * Method containsAbuseWord.
	 * @param s String
	 * @return boolean
	 */
	public static boolean containsAbuseWord(String s)
	{
		for (Pattern pattern : ABUSEWORD_LIST)
		{
			if (pattern.matcher(s).matches())
			{
				return true;
			}
		}
		
		return false;
	}
}
