package de.eidotter.bots.ts3bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.jibble.pircbot.Colors;

import de.hof.mainbot.Bot;
import de.hof.mainbot.Observer;
import de.hof.mainbot.helpers.FileIO;
import de.stefan1200.jts3serverquery.JTS3ServerQuery;
import de.stefan1200.jts3serverquery.TeamspeakActionListener;

/**
 * Stellt einen Bot zur TS3-Channelabfrage zur Verfügung. Abfrage erfolgt mit einer Channelnachricht des 
 * Formats "-ts".
 * @author Michael
 * @version 0.2.1
 *
 */
public class TeamspeakBot extends JTS3ServerQuery implements Observer, TeamspeakActionListener{
	// Konstanten
	public static final String TS3_ACTIVE_CHANNEL_QUERY_COMMAND = "-ts";
	protected static final String CONFIG_KEY_TS3_IP = "ts3_ip";
	protected static final String CONFIG_KEY_TS3_PORT = "ts3_port";
	protected static final String CONFIG_KEY_TS3_PW = "ts3_pw";
	protected static final String CONFIG_KEY_TS3_SERVERQUERY_ACCOUNT = "ts3_query_account";
	protected static final String CONFIG_KEY_TS3_SERVERQUERY_PW = "ts3_query_pw";
	protected static final String CONFIG_KEY_TS3_SERVERQUERY_PORT = "ts3_query_port";
	protected static final String CONFIG_KEY_TS3_IP_INTERNET = "ts3_ip_internet";
	protected static final String CONFIG_KEY_DEBUG_MODE = "debug_mode";
	private static final String CONFIG_FILENAME = "ts3_serverquery.properties";
	
	// Instanzvariablen
	private Bot mainBot;
	private Properties prop;
	private ConnectionProperties cnProp;
	private boolean isConfigOK = false;
	private boolean isConnectionEstablished = false;
	private boolean TS_DEBUG = false;
	private KeepAliveTask keepAlive;
	private Thread keepAliveThread;
	
	// User zu Channel
	HashMap<Integer, Integer> userToChannel;
	// Userliste
	HashMap<Integer, User> users;
	// Channelliste (nur Namen)
	HashMap<Integer, String> channels;
	
	public TeamspeakBot(Bot mainBot){
		// IRC-Bot zuweisen
		this.mainBot = mainBot;
		// DEBUG für JTS3ServerQuery
		this.DEBUG = true;
		// Register for events of main Bot
		this.mainBot.registerObserver(this);
		// HashMap initialisieren
		userToChannel = new HashMap<Integer, Integer>();
		users = new HashMap<Integer, User>();
		channels = new HashMap<Integer, String>();
		// Konfigurationsdatei einlesen
		if(!this.readConfiguration(CONFIG_FILENAME)){
			System.out.println("Fehler beim Einlesen der Konfiguration!");
		} else {
			// Konfigurationsdatei parsen
			cnProp = new ConnectionProperties();
			if(!cnProp.parseFromProperties(prop)){
				System.out.println("Fehler beim Parsen der Konfiguration!");
			} else {
				this.isConfigOK = true;
				this.TS_DEBUG = cnProp.getDebugMode();
			}
		}
		if(isConfigOK){
			// Zum Teamspeak-Server verbinden
			boolean success = this.connectAndLogin();
			if(success){
				// ActionListener registieren
				this.setTeamspeakActionListener(this);
				// Eventnotifications registrieren
				if(this.registerForTS3Events()){
					// Verbindung erfolgreich aufgebaut
					this.isConnectionEstablished = true;
					System.out.println("Verbindung zum TS3-Server erfolgreich hergestellt!");
					// Keepalive-Signale sende
					this.keepConnectionAlive();
				}
			}
		}
	}
	
	/**
	 * Schickt eine periodische Abfrage an den TS3, damit die Verbindung nicht durch timeout geschlossen wird.
	 */
	private void keepConnectionAlive() {
		keepAlive = new KeepAliveTask(this, 480, TS_DEBUG);
		keepAliveThread = new Thread(keepAlive);
		keepAliveThread.start();		
	}

	/**
	 * Registriert den Listener für alle Channel und Server-Events.
	 */
	private boolean registerForTS3Events() {
		if(!this.addEventNotify(JTS3ServerQuery.EVENT_MODE_CHANNEL, 0)){
			System.out.println("Fehler beim Hinzufügen der Notification für Channel-Events!");
			return false;
		}
		if(!this.addEventNotify(JTS3ServerQuery.EVENT_MODE_SERVER, 0)){
			System.out.println("Fehler beim Hinzufügen der Notification für Server-Events!");
			return false;
		}
		return true;
	}

	/**
	 * Stellt die Verbindung zum TS3-Server her und meldet sich an.
	 * @return
	 */
	private boolean connectAndLogin() {
		if(!this.connectTS3Query(this.cnProp.getTs3Ip(), this.cnProp.getTs3ServerQueryPort())){
			System.out.println("Fehler beim Verbindungsaufbau mit dem TS3-Server!");
			System.out.println(this.getLastError());
			return false;
		}
		if(!this.loginTS3(this.cnProp.getTs3ServerQueryName(), this.cnProp.getTs3ServerQueryPassword())){
			System.out.println("Fehler beim Anmelden am TS3-Server!");
			System.out.println(this.getLastError());
			return false;
		}
		if(!this.selectVirtualServer(1)){
			System.out.println("Fehler beim Auswählen eines virtuellen TS3-Servers!");
			System.out.println(this.getLastError());
			return false;
		}
		return true;
	}

	/**
	 * Liest die .properties-Datei ein.
	 * @param configFilename
	 * @return	boolean Succes
	 */
	private boolean readConfiguration(String configFilename) {
		this.prop = new Properties();
		try {
			prop.load(FileIO.getFileInputStream(configFilename));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return this.checkConfiguration(this.prop);
	}
	
	/**
	 * Prüft, ob alle benötigten Keys in der Properties vorhanden sind.
	 * @return boolean
	 */
	private boolean checkConfiguration(Properties prop) {
		if(!prop.containsKey(CONFIG_KEY_TS3_IP) || !prop.containsKey(CONFIG_KEY_TS3_PORT) || !prop.containsKey(CONFIG_KEY_TS3_PW)
				|| !prop.containsKey(CONFIG_KEY_TS3_SERVERQUERY_ACCOUNT) || !prop.containsKey(CONFIG_KEY_TS3_SERVERQUERY_PW)
				|| !prop.containsKey(CONFIG_KEY_TS3_SERVERQUERY_PORT) || !prop.containsKey(CONFIG_KEY_DEBUG_MODE)
				|| !prop.containsKey(CONFIG_KEY_TS3_IP_INTERNET)){
			return false;
		}
		return true;
	}

	@Override
	public void updateOnMessage(String channel, String sender, String login, String hostname, String message) {
		if(message.equals(TS3_ACTIVE_CHANNEL_QUERY_COMMAND)){
			this.incomingChannelRequest(channel);
		}
	}
	
	/**
	 * Bearbeitet die Abfrage der aktiven Channels.
	 * @param channel	String
	 */
	private void incomingChannelRequest(String channel) {
		// Verbindung prüfen
		if(!this.isConnected() || !this.isConnectionEstablished){
			this.mainBot.sendMessage(channel, Colors.RED + Colors.BOLD + "Fehler: Keine Verbindung zum TS3 vorhanden");
			if(!this.reconnect())return;
		}
		// Teamspeak IP ausgeben
		this.mainBot.sendMessage(channel, Colors.BLUE + Colors.BOLD + Colors.UNDERLINE + "TS3-IP: " + Colors.NORMAL + Colors.UNDERLINE + Colors.BLUE + cnProp.getTs3IpInternet());
		// Channelliste erstellen
		ArrayList<Channel> chans = new ArrayList<Channel>();
		Set<Entry<Integer, String>> entrySet = this.channels.entrySet();
		for (Entry<Integer, String> entry : entrySet) {
			Channel tempChan = new Channel(entry.getValue());
			// Alle User dieses Channels in Channel hinzufügen
			Set<Entry<Integer, Integer>> entrySet2 = this.userToChannel.entrySet();
			for (Entry<Integer, Integer> entry2 : entrySet2) {
				if(entry2.getValue().compareTo(entry.getKey()) == 0){
					// User ist in diesem Channel
					tempChan.addUser(this.getUserForID(entry2.getKey()));
				}
			}
			if(tempChan.getUsers().size() > 0){
				chans.add(tempChan);
			}
		}
		if(chans.size() > 0){
			// Channelliste versenden
			for (Channel channel2 : chans) {
				this.mainBot.sendMessage(channel, Colors.BLUE + Colors.BOLD + channel2.getChannelName() + ": " + Colors.NORMAL + channel2.getUserString());
			}
		} else {
			this.mainBot.sendMessage(channel, Colors.BLUE + Colors.BOLD +"Zurzeit keine User auf dem TS3.");
		}
	}

	@Override
	public void updateOnPrivateMessage(String sender, String login,	String hostname, String message) {

	}

	@Override
	public void updateOnConnect() {

	}

	@Override
	public void updateOnDisconnect() {

	}

	@Override
	public void teamspeakActionPerformed(String eventType, HashMap<String, String> eventInfo) {
		if(eventType.equals("notifyclientmoved")){
			// Client moved
			// Get Client ID
			Integer clid = Integer.parseInt(eventInfo.get("clid"));
			// Get Channel ID
			Integer ctid = Integer.parseInt(eventInfo.get("ctid"));
			if(TS_DEBUG){
				// Output Hashmap
				//this.outputHashMap(eventType, eventInfo);
				System.out.println("Client with clid = " + clid + " moved to channel with ctid = " + ctid + ".");
			}			
			// Update/Add User in HashMap
			this.clientMoved(clid, ctid);
		} else if(eventType.equals("notifycliententerview")){
			// New Client joined Server
			// Get Client ID
			Integer clid = Integer.parseInt(eventInfo.get("clid"));
			// Get Channel ID
			Integer ctid = Integer.parseInt(eventInfo.get("ctid"));
			if(TS_DEBUG){
				// Output Hashmap
				//this.outputHashMap(eventType, eventInfo);	
				System.out.println("Client with clid = " + clid + " joined Server.");
			}
			// Update/Add User in HashMap
			this.clientJoined(clid, ctid);
		} else if(eventType.equals("notifyclientleftview")){
			// Client left Server
			// Get Client ID
			Integer clid = Integer.parseInt(eventInfo.get("clid"));
			if(TS_DEBUG){
				System.out.println("Client with clid = " + clid + " has left the Server.");
			}			
			// Delete User in HashMap
			this.clientLeft(clid);
		}
	}
	
	/**
	 * Wird aufgerufen, wenn ein TS-Client den Server verlässt.
	 * @param clid	Client ID
	 */
	private void clientLeft(Integer clid) {
		// UserToChannel updaten
		this.userToChannel.remove(clid);
		// User aus Userliste entfernen
		this.users.remove(clid);
		
	}

	/**
	 * Wird aufgerufen, wenn ein TS-Client den Channel wechselt.
	 * @param clid	Client ID
	 * @param ctid	Channel ID
	 */
	private void clientMoved(Integer clid, Integer ctid) {
		// UserToChannel updaten
		this.userToChannel.put(clid, ctid);	
		// Channel updaten
		Channel tempChan = this.getChannelForID(ctid);
		if(tempChan != null){
			String chan = tempChan.getChannelName();
			this.channels.put(ctid, chan);
		}
	}

	/**
	 * Wird aufgerufen, wenn ein TS-Client den Server betreten hat.
	 * @param clid	Client ID
	 * @param ctid	Channel ID
	 */
	private void clientJoined(Integer clid, Integer ctid) {
		// UserToChannel updaten
		this.userToChannel.put(clid, ctid);
		// User in Userliste hinzufügen
		User temp = this.getUserForID(clid);
		if(temp != null){
			this.users.put(clid, temp);
		}
		// Channel updaten
		Channel tempChan = this.getChannelForID(ctid);
		if(tempChan != null){
			String chan = tempChan.getChannelName();
			this.channels.put(ctid, chan);
		}
		
	}
	
	
	/**
	 * Liefert ein User-Objekt für die entsprechende ID.
	 * @param clid Client ID
	 * @return	User oder null
	 */
	private User getUserForID(Integer clid){
		User u = null;
		if(this.isConnectionEstablished && this.isConnected()){
			HashMap<String, String> info = this.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, clid);
			if (info != null) {
				String nick = info.get("client_nickname");
				u = new User();
				u.setId(clid);
				u.setNick(nick);
			} else {
				System.out.println(this.getLastError());
				System.out.println("Keine aktive Verbindung zum TS3-Server vorhanden!");
				this.isConnectionEstablished = this.reconnect();
			}
		} else {
			System.out.println("Keine aktive Verbindung zum TS3-Server vorhanden!");
		}
		return u;
	}
	
	/**
	 * Liefert ein Channel-Objekt für die entsprechende ID.
	 * @param ctid	Channel ID
	 * @return	Channel oder null
	 */
	private Channel getChannelForID(Integer ctid){
		Channel c = null;
		if(this.isConnectionEstablished && this.isConnected()){
			HashMap<String, String> info = this.getInfo(JTS3ServerQuery.INFOMODE_CHANNELINFO, ctid);
			if (info != null) {
				String name = info.get("channel_name");
				c = new Channel(name);
			} else {
				System.out.println(this.getLastError());
				System.out.println("Keine aktive Verbindung zum TS3-Server vorhanden!");
				this.isConnectionEstablished = this.reconnect();
			}
		} else {
			System.out.println("Keine aktive Verbindung zum TS3-Server vorhanden!");
		}
		return c;
	}
	
	/**
	 * Versucht, die Serverquery-Verbindung zum TS3-Server wiederherzustellen.
	 * @return
	 */
	private boolean reconnect(){
		System.out.println("Schließe bestehende Verbindung.");
		this.closeTS3Connection();
		System.out.println("Versuche Verbindung wieder aufzubauen.");
		// HashMaps zurücksetzen
		userToChannel = new HashMap<Integer, Integer>();
		users = new HashMap<Integer, User>();
		channels = new HashMap<Integer, String>();
		// Konfigurationsdatei einlesen
		if(!this.readConfiguration(CONFIG_FILENAME)){
			System.out.println("Fehler beim Einlesen der Konfiguration!");
		} else {
			// Konfigurationsdatei parsen
			cnProp = new ConnectionProperties();
			if(!cnProp.parseFromProperties(prop)){
				System.out.println("Fehler beim Parsen der Konfiguration!");
			} else {
				this.isConfigOK = true;
				this.TS_DEBUG = cnProp.getDebugMode();
			}
		}
		if(isConfigOK){
			// Zum Teamspeak-Server verbinden
			boolean success = this.connectAndLogin();
			if(success){
				// ActionListener registieren
				this.setTeamspeakActionListener(this);
				// Eventnotifications registrieren
				if(this.registerForTS3Events()){
					// Verbindung erfolgreich aufgebaut
					this.isConnectionEstablished = true;
					System.out.println("Verbindung zum TS3-Server erfolgreich hergestellt!");
					// Keepalive-Signale sende
					this.keepConnectionAlive();
					return true;
				}
			}
		}
		return false;
	}
	
}
