package de.eidotter.beamtenbot.ts3channelquery;

import java.util.HashMap;
import java.util.List;

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
	
	public TeamspeakChannelQuery(String ts3Properties) throws ConfigurationException{
		qry = new JTS3ServerQuery();
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
		return false;
	}

	@Override
	public void teamspeakActionPerformed(String eventType,
			HashMap<String, String> eventInfo) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Liefert eine Liste mit aktiven Channels, bzw. null wenn keine Channels aktiv sind.
	 * Wirft eine Exception, wenn Verbindungsfehler.
	 * @return	List<Channel>
	 */
	public List<Channel> getActiveChannels(){
		return null;
		
	}

}
