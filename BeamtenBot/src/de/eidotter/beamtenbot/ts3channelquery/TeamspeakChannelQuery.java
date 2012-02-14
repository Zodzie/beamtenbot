package de.eidotter.beamtenbot.ts3channelquery;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;
import de.stefan1200.jts3serverquery.TeamspeakActionListener;

/**
 * Die Klasse ermöglicht das Abrufen einer Channelliste eines Teamspeak 3-Servers.
 * Es werden nur Channel geliefert, in denen sich aktuell Nutzer befinden.
 * @author Michael
 * @version 0.1
 */
public class TeamspeakChannelQuery implements TeamspeakActionListener {
	private JTS3ServerQuery qry;
	private boolean isConnectionReady = false;
	// HashMap enthält User-ID -> Channel-ID
	private HashMap<Integer, Integer> userIdInChannelId;
	private static boolean DEBUG;
	
	public TeamspeakChannelQuery(String ts3Properties, boolean debug) throws ConfigurationException{
		DEBUG = debug;
		qry = new JTS3ServerQuery();
		userIdInChannelId = new HashMap<Integer, Integer>();
		PropertiesConfiguration prop;
		URL propertiesURL = this.getClass().getClassLoader().getResource(ts3Properties);
		if(propertiesURL != null){
			prop = new PropertiesConfiguration(propertiesURL);
			isConnectionReady = this.prepareConnection(qry, prop);
		} else {
			isConnectionReady = false;
			throw new ConfigurationException(ts3Properties + " konnte nicht gefunden werden!");			
		}
		
	}

	/**
	 * Baut die Serverquery-Verbindung zu einem TS3-Server auf.
	 * @param qry JTS3ServerQuery
	 * @param prop PropertiesConfiguration
	 * @return	boolean	Erfolgreicher Verbindungsaufbau.
	 */
	private boolean prepareConnection(JTS3ServerQuery qry2,	PropertiesConfiguration prop) {
		ConnectionProperties properties = new ConnectionProperties();
		properties.fillConfiguration(prop);
		if(!qry.connectTS3Query(properties.getTs3Ip(), properties.getTs3ServerQueryPort())){
			if(DEBUG){
				System.out.println("Error: connectTS3Query");
				System.out.println(qry.getLastError());
			}
			return false;
		}
		if(!qry.loginTS3(properties.getTs3ServerQueryName(), properties.getTs3ServerQueryPassword())){
			if(DEBUG){
				System.out.println("Error: loginTS3");
				System.out.println(qry.getLastError());
			}
			return false;
		}
		if(!qry.selectVirtualServer(1))
		{
			if(DEBUG){
				System.out.println("Error: selectVirtualServer");
				System.out.println(qry.getLastError());
			}
			return false;
		}
		qry.setTeamspeakActionListener(this);
		qry.addEventNotify(JTS3ServerQuery.EVENT_MODE_CHANNEL, 0);
		qry.addEventNotify(JTS3ServerQuery.EVENT_MODE_SERVER, 0);
		return true;
	}

	@Override
	public void teamspeakActionPerformed(String eventType, HashMap<String, String> eventInfo) {
		if(eventType.equals("notifyclientmoved")){
			// Client moved
			// Get Client ID
			Integer clid = Integer.parseInt(eventInfo.get("clid"));
			// Get Channel ID
			Integer ctid = Integer.parseInt(eventInfo.get("ctid"));
			if(DEBUG){
				// Output Hashmap
				//this.outputHashMap(eventType, eventInfo);
				System.out.println("Client with clid = " + clid + " moved to channel with ctid = " + ctid + ".");
			}			
			// Update/Add User in HashMap
			userIdInChannelId.put(clid, ctid);
		} else if(eventType.equals("notifycliententerview")){
			// New Client joined Server
			// Get Client ID
			Integer clid = Integer.parseInt(eventInfo.get("clid"));
			// Get Channel ID
			Integer ctid = Integer.parseInt(eventInfo.get("ctid"));
			if(DEBUG){
				// Output Hashmap
				//this.outputHashMap(eventType, eventInfo);	
				System.out.println("Client with clid = " + clid + " joined Server.");
			}
			// Update/Add User in HashMap
			userIdInChannelId.put(clid, ctid);
		} else if(eventType.equals("notifyclientleftview")){
			// Client left Server
			// Get Client ID
			Integer clid = Integer.parseInt(eventInfo.get("clid"));
			if(DEBUG){
				System.out.println("Client with clid = " + clid + " has left the Server.");
			}			
			// Delete User in HashMap
			userIdInChannelId.remove(clid);
		}
		
	}
	
	/**
	 * Liefert eine Liste mit aktiven Channels, bzw. null wenn keine Channels aktiv sind.
	 * Wirft eine Exception, wenn Verbindungsfehler.
	 * @return	List<Channel>
	 * @throws Exception 
	 */
	private List<Channel> getActiveChannels() throws Exception{
		if(!this.isConnectionReady){
			throw new Exception("Konnte keine Verbindung zum TS3-Server aufbauen.");
		} else if(this.userIdInChannelId.size() < 1){
			return null;
		} else {
			// Liste erstellen
			HashMap<String, Channel> channelMap = new HashMap<String, Channel>();
			Set<Entry<Integer, Integer>> entrySet = userIdInChannelId.entrySet();
			for (Entry<Integer, Integer> entry : entrySet) {
				String nick = this.getClientNick(entry.getKey(), qry);
				String channel = this.getChannelName(entry.getValue(), qry);
				if(channelMap.containsKey(channel)){
					channelMap.get(channel).addUser(nick);
				} else {
					Channel tempChannel = new Channel(channel);
					tempChannel.addUser(nick);
					channelMap.put(channel, tempChannel);
				}
			}
			
			Set<Entry<String, Channel>> entrySet2 = channelMap.entrySet();
			ArrayList<Channel> channelList = new ArrayList<Channel>();
			for (Entry<String, Channel> entry : entrySet2) {
				channelList.add(entry.getValue());
			}
			return channelList;
		}		
	}
	
	/**
	 * Liefert für eine ClientID den Nicknamen des Clients.
	 * @param clientId	ClientID
	 * @param qry
	 * @return Nickname
	 * @throws Exception 
	 */
	private String getClientNick(int clientId, JTS3ServerQuery qry) throws Exception{
		if(isConnectionReady){
			return qry.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, clientId).get("client_nickname");
		} else {
			throw new Exception("Konnte keine Verbindung zum TS3-Server aufbauen.");
		}
	}
	
	/**
	 * Liefert für eine ChannelID den Namen des Channels.
	 * @param channelId	ChannelID
	 * @param qry
	 * @return	Channelname
	 * @throws Exception 
	 */
	private String getChannelName(int channelId, JTS3ServerQuery qry) throws Exception{
		if(isConnectionReady){
			return qry.getInfo(JTS3ServerQuery.INFOMODE_CHANNELINFO, channelId).get("channel_name");
		} else {
			throw new Exception("Konnte keine Verbindung zum TS3-Server aufbauen.");
		}
	}
	
	
	/**
	 * Liefert einen String mit allen aktiven Channels samt Nutzer.
	 * @return	String
	 */
	public String getActiveChannelsString(){
		StringBuilder sb = new StringBuilder();
		List<Channel> activeChannels = null;
		try {
			activeChannels = this.getActiveChannels();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (activeChannels != null) {
			for (Channel chan : activeChannels) {
				sb.append(chan.toString());
			}
			return sb.toString();
		} else {
			return null;
		}
	}
	
	/**
	 * Liefert eine Liste mit aktiven Channels oder null.
	 * @return	List<Channel>
	 */
	public List<Channel> getActiveChannelList(){
		try {
			return this.getActiveChannels();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gibt eine HashMap auf der Konsole aus.
	 * @param hm	HashMap<String,String>
	 */
	private void outputHashMap(HashMap<String, String> hm){
		Set<Entry<String, String>> entrySet = hm.entrySet();
		for (Entry<String, String> entry : entrySet) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}
	
	/**
	 * Gibt eine HashMap einer Eventnotification auf der Konsole aus.
	 * @param event	String Eventtyp
	 * @param hm	HashMap Eventinfo
	 */
	@SuppressWarnings("unused")
	private void outputHashMap(String event, HashMap<String, String> hm){
		System.out.println("Event occured: " + event);
		this.outputHashMap(hm);
	}

}
