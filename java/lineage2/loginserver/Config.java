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
package lineage2.loginserver;

import java.io.File;
import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lineage2.commons.configuration.ExProperties;
import lineage2.commons.util.Rnd;
import lineage2.loginserver.crypt.PasswordHash;
import lineage2.loginserver.crypt.ScrambledKeyPair;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Config
{
	private final static Logger _log = LoggerFactory.getLogger(Config.class);
	private static final String LOGIN_CONFIG_FILE = "config/LoginServer.ini";
	private static final String SERVER_NAMES_FILE = "config/xml/servername.xml";
	public static String LOGIN_HOST;
	public static int PORT_LOGIN;
	public static String GAME_SERVER_LOGIN_HOST;
	public static int GAME_SERVER_LOGIN_PORT;
	public static long GAME_SERVER_PING_DELAY;
	public static int GAME_SERVER_PING_RETRY;
	public static String DATABASE_DRIVER;
	public static int DATABASE_MAX_CONNECTIONS;
	public static int DATABASE_MAX_IDLE_TIMEOUT;
	public static int DATABASE_IDLE_TEST_PERIOD;
	public static String DATABASE_URL;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;
	public static String DEFAULT_PASSWORD_HASH;
	public static String LEGACY_PASSWORD_HASH;
	public static int LOGIN_BLOWFISH_KEYS;
	public static int LOGIN_RSA_KEYPAIRS;
	public static boolean ACCEPT_NEW_GAMESERVER;
	public static boolean AUTO_CREATE_ACCOUNTS;
	public static String ANAME_TEMPLATE;
	public static String APASSWD_TEMPLATE;
	public static final Map<Integer, String> SERVER_NAMES = new HashMap<>();
	public final static long LOGIN_TIMEOUT = 60 * 1000L;
	public static int LOGIN_TRY_BEFORE_BAN;
	public static long LOGIN_TRY_TIMEOUT;
	public static long IP_BAN_TIME;
	private static ScrambledKeyPair[] _keyPairs;
	private static byte[][] _blowfishKeys;
	public static PasswordHash DEFAULT_CRYPT;
	public static PasswordHash[] LEGACY_CRYPT;
	public static boolean LOGIN_LOG;
	
	/**
	 * Constructor for Config.
	 */
	private Config()
	{
	}
	
	/**
	 * Method load.
	 */
	static void load()
	{
		loadConfiguration();
		loadServerNames();
	}
	
	/**
	 * Method initCrypt.
	 * @throws Throwable
	 */
	static void initCrypt() throws Throwable
	{
		DEFAULT_CRYPT = new PasswordHash(Config.DEFAULT_PASSWORD_HASH);
		List<PasswordHash> legacy = new ArrayList<>();
		
		for (String method : Config.LEGACY_PASSWORD_HASH.split(";"))
		{
			if (!method.equals(Config.DEFAULT_PASSWORD_HASH))
			{
				legacy.add(new PasswordHash(method));
			}
		}
		
		LEGACY_CRYPT = legacy.toArray(new PasswordHash[legacy.size()]);
		_log.info("Loaded " + Config.DEFAULT_PASSWORD_HASH + " as default crypt.");
		_keyPairs = new ScrambledKeyPair[Config.LOGIN_RSA_KEYPAIRS];
		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
		RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4);
		keygen.initialize(spec);
		
		for (int i = 0; i < _keyPairs.length; i++)
		{
			_keyPairs[i] = new ScrambledKeyPair(keygen.generateKeyPair());
		}
		
		_log.info("Cached " + _keyPairs.length + " KeyPairs for RSA communication.");
		_blowfishKeys = new byte[Config.LOGIN_BLOWFISH_KEYS][16];
		
		for (int i = 0; i < _blowfishKeys.length; i++)
		{
			for (int j = 0; j < _blowfishKeys[i].length; j++)
			{
				_blowfishKeys[i][j] = (byte) (Rnd.get(255) + 1);
			}
		}
		
		_log.info("Stored " + _blowfishKeys.length + " keys for Blowfish communication.");
	}
	
	/**
	 * Method loadServerNames.
	 */
	private final static void loadServerNames()
	{
		SERVER_NAMES.clear();
		
		try
		{
			SAXReader reader = new SAXReader(true);
			Document document = reader.read(new File(SERVER_NAMES_FILE));
			Element root = document.getRootElement();
			
			for (Iterator<?> itr = root.elementIterator(); itr.hasNext();)
			{
				Element node = (Element) itr.next();
				
				if (node.getName().equals("server"))
				{
					Integer id = Integer.valueOf(node.attributeValue("id"));
					String name = node.attributeValue("name");
					SERVER_NAMES.put(id, name);
				}
			}
			
			_log.info("Loaded " + SERVER_NAMES.size() + " server names.");
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
	}
	
	/**
	 * Method loadConfiguration.
	 */
	private static void loadConfiguration()
	{
		ExProperties serverSettings = load(LOGIN_CONFIG_FILE);
		LOGIN_HOST = serverSettings.getProperty("LoginserverHostname", "127.0.0.1");
		PORT_LOGIN = serverSettings.getProperty("LoginserverPort", 2106);
		GAME_SERVER_LOGIN_HOST = serverSettings.getProperty("LoginHost", "127.0.0.1");
		GAME_SERVER_LOGIN_PORT = serverSettings.getProperty("LoginPort", 9014);
		DATABASE_DRIVER = serverSettings.getProperty("Driver", "com.mysql.jdbc.Driver");
		DATABASE_MAX_CONNECTIONS = serverSettings.getProperty("MaximumDbConnections", 3);
		DATABASE_MAX_IDLE_TIMEOUT = serverSettings.getProperty("MaxIdleConnectionTimeout", 600);
		DATABASE_IDLE_TEST_PERIOD = serverSettings.getProperty("IdleConnectionTestPeriod", 60);
		DATABASE_URL = serverSettings.getProperty("URL", "jdbc:mysql://localhost/l2sdb");
		DATABASE_LOGIN = serverSettings.getProperty("Login", "root");
		DATABASE_PASSWORD = serverSettings.getProperty("Password", "");
		LOGIN_BLOWFISH_KEYS = serverSettings.getProperty("BlowFishKeys", 20);
		LOGIN_RSA_KEYPAIRS = serverSettings.getProperty("RSAKeyPairs", 10);
		ACCEPT_NEW_GAMESERVER = serverSettings.getProperty("AcceptNewGameServer", true);
		DEFAULT_PASSWORD_HASH = serverSettings.getProperty("PasswordHash", "whirlpool2");
		LEGACY_PASSWORD_HASH = serverSettings.getProperty("LegacyPasswordHash", "sha1");
		AUTO_CREATE_ACCOUNTS = serverSettings.getProperty("AutoCreateAccounts", true);
		ANAME_TEMPLATE = serverSettings.getProperty("AccountTemplate", "[A-Za-z0-9]{4,14}");
		APASSWD_TEMPLATE = serverSettings.getProperty("PasswordTemplate", "[A-Za-z0-9]{4,16}");
		LOGIN_TRY_BEFORE_BAN = serverSettings.getProperty("LoginTryBeforeBan", 10);
		LOGIN_TRY_TIMEOUT = serverSettings.getProperty("LoginTryTimeout", 5) * 1000L;
		IP_BAN_TIME = serverSettings.getProperty("IpBanTime", 300) * 1000L;
		GAME_SERVER_PING_DELAY = serverSettings.getProperty("GameServerPingDelay", 30) * 1000L;
		GAME_SERVER_PING_RETRY = serverSettings.getProperty("GameServerPingRetry", 4);
		LOGIN_LOG = serverSettings.getProperty("LoginLog", true);
	}
	
	/**
	 * Method load.
	 * @param filename String
	 * @return ExProperties
	 */
	private static ExProperties load(String filename)
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
			_log.error("", e);
		}
		
		return result;
	}
	
	/**
	 * Method getScrambledRSAKeyPair.
	 * @return ScrambledKeyPair
	 */
	static ScrambledKeyPair getScrambledRSAKeyPair()
	{
		return _keyPairs[Rnd.get(_keyPairs.length)];
	}
	
	/**
	 * Method getBlowfishKey.
	 * @return byte[]
	 */
	static byte[] getBlowfishKey()
	{
		return _blowfishKeys[Rnd.get(_blowfishKeys.length)];
	}
}
