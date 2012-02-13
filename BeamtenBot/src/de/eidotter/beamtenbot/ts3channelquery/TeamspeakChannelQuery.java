package de.eidotter.beamtenbot.ts3channelquery;

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
	// HashMap enthält Username -> Channelname
	private HashMap<String, String> userInChannel;
	
	public TeamspeakChannelQuery(String ts3Properties) throws ConfigurationException{
		qry = new JTS3ServerQuery();
		userInChannel = new HashMap<String, String>();
		PropertiesConfiguration prop = new PropertiesConfiguration(ts3Properties);
		isConnectionReady = this.prepareConnection(qry, prop);
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
			System.out.println("Error: connectTS3Query");
			System.out.println(qry.getLastError());
			return false;
		}
		if(!qry.loginTS3(properties.getTs3ServerQueryName(), properties.getTs3ServerQueryPassword())){
			System.out.println("Error: loginTS3");
			System.out.println(qry.getLastError());
			return false;
		}
		if(!qry.selectVirtualServer(1))
		{
			System.out.println("Error: selectVirtualServer");
			System.out.println(qry.getLastError());
			return false;
		}
		qry.setTeamspeakActionListener(this);
		qry.addEventNotify(JTS3ServerQuery.EVENT_MODE_CHANNEL, 0);
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
			// Get Username for Client ID
			HashMap<String, String> hminfo = qry.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, clid);
			String nick = hminfo.get("client_nickname");
			// Get Channelname for Channel ID
			hminfo = qry.getInfo(JTS3ServerQuery.INFOMODE_CHANNELINFO, ctid);
			String channel = hminfo.get("channel_name");			
			
			// Update/Add User in HashMap
			userInChannel.put(nick, channel);	
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
		} else if(this.userInChannel.size() < 1){
			return null;
		} else {
			// Liste erstellen
			HashMap<String, Channel> channelMap = new HashMap<String, Channel>();
			Set<Entry<String, String>> entrySet = userInChannel.entrySet();
			for (Entry<String, String> entry : entrySet) {
				if(channelMap.containsKey(entry.getValue())){
					channelMap.get(entry.getValue()).addUser(entry.getKey());
				} else {
					Channel tempChannel = new Channel(entry.getValue());
					tempChannel.addUser(entry.getKey());
					channelMap.put(entry.getValue(), tempChannel);
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

}
