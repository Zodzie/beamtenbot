package de.eidotter.beamtenbot;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.jibble.pircbot.PircBot;

import de.eidotter.beamtenbot.ts3channelquery.Channel;
import de.eidotter.beamtenbot.ts3channelquery.ConnectionProperties;
import de.stefan1200.jts3serverquery.JTS3ServerQuery;
import de.stefan1200.jts3serverquery.TeamspeakActionListener;


public class BeamtenBot extends PircBot implements TeamspeakActionListener {
	private static boolean DEBUG = false;
	private ConnectionProperties properties;
	public static final SimpleDateFormat SDF_GERMAN = new SimpleDateFormat("HH:mm' Uhr, 'dd.MM.yyyy");
	private JTS3ServerQuery qry;
	private HashMap<String, Integer> userInChannel;
	public BeamtenBot(boolean debug, ConnectionProperties properties){
		this.properties = properties;
		userInChannel = new HashMap<String, Integer>();
		DEBUG = debug;
		this.setName("BeamtenBot");
		qry = new JTS3ServerQuery();
		if(!qry.connectTS3Query(properties.getTs3Ip(), properties.getTs3ServerQueryPort())){
			System.out.println("Error: connectTS3Query");
			System.out.println(qry.getLastError());
		}
		if(!qry.loginTS3(properties.getTs3ServerQueryName(), properties.getTs3ServerQueryPassword())){
			System.out.println("Error: loginTS3");
			System.out.println(qry.getLastError());
		}
		if(!qry.selectVirtualServer(1))
		{
			System.out.println("Error: selectVirtualServer");
			System.out.println(qry.getLastError());
		}
		qry.setTeamspeakActionListener(this);
		qry.addEventNotify(JTS3ServerQuery.EVENT_MODE_CHANNEL, 0);
	}

	@Override
	protected void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if(DEBUG)	System.out.printf("Incoming Message in Channel %s from %s: %s\n",channel, sender, message);
		if(message.equals("thetime")){
			this.sendMessage(channel, SDF_GERMAN.format(new Date()));
		} else if(message.equals("hwu")){
			this.sendMessage(channel, "HA");
			this.sendMessage(channel, "WE");
			this.sendMessage(channel, "UU");
			this.sendMessage(channel, "HAWEUUUUUH!!!!!!!!!");
		} else if(message.equals("wer ist der godfather of c++?")){
			this.sendMessage(channel, "MICHI!!!");
		}
		
	}
	
	/**
	 * Schickt eine PN an den Sender mit allen bevölkerten Channels.
	 * @param sender
	 */
	private void channelQuery(String sender) {
		if(userInChannel.size() > 0){
//			Collection<Integer> cValue = userInChannel.values();
//		    Collection<String> cKey = userInChannel.keySet();
//		    Iterator<Integer> itrValue = cValue.iterator();
//		    Iterator<String> itrKey = cKey.iterator();
//		    HashMap<String, List<String>> hm = new HashMap<String, List<String>>();
//			while (itrValue.hasNext() && itrKey.hasNext())
//			{
//				//System.out.println(itrKey.next() + ": " + itrValue.next());
//				// Get Channel Name
//				HashMap<String, String> info = qry.getInfo(JTS3ServerQuery.INFOMODE_CHANNELINFO, itrValue.next());
//				String channelName = info.get("channel_name");
//				String userName = itrKey.next();
//				
//				if(hm.containsKey(channelName)){
//					List<String> users = hm.get(channelName);
//					users.add(userName);
//				} else {
//					ArrayList<String> users = new ArrayList<String>();
//					users.add(userName);
//					hm.put(channelName, users);					
//				}				
//				// Send Message with User In Channel
//				//this.sendMessage(sender, itrKey.next() + " - " + channelName);
//			}
			HashMap<String, Channel> channelList = new HashMap<String, Channel>();
			Set<Entry<String, Integer>> entrySet = userInChannel.entrySet();
			for (Entry<String, Integer> entry : entrySet) {
				HashMap<String, String> info = qry.getInfo(JTS3ServerQuery.INFOMODE_CHANNELINFO, entry.getValue());
				String channelName = info.get("channel_name");
				if(channelList.containsKey(channelName)){
					channelList.get(channelName).addUser(entry.getKey());
				} else {
					Channel tempChannel = new Channel(channelName);
					tempChannel.addUser(entry.getKey());
					channelList.put(channelName, tempChannel);
				}
			}
			
			Set<Entry<String, Channel>> entrySet2 = channelList.entrySet();
			for (Entry<String, Channel> entry : entrySet2) {
				StringBuilder sb = new StringBuilder();
				sb.append(entry.getKey() + ": ");
				for (String user : entry.getValue().getUsers()) {
					sb.append(user + "  ");
				}
				sb.append(" - ").append(this.produceTS3Link(entry.getKey(), sender));
				this.sendMessage(sender, sb.toString());
			}			
			
		} else {
			// Keine bevölkerten Channels vorhanden
			this.sendMessage(sender, "Zurzeit nix los aufm TS!");
		}
		
		
	}

	@Override
	protected void onPrivateMessage(String sender, String login,
			String hostname, String message) {
		//if(message.equals("ts3")){
		//	this.channelQuery(sender);
		//}
	}

	@Override
	public void teamspeakActionPerformed(String eventType,
			HashMap<String, String> eventInfo) {
		if(eventType.equals("notifyclientmoved")){
			// Client moved
			outputHashMap(eventInfo);
			// Get Client ID
			Integer clid = Integer.parseInt(eventInfo.get("clid"));
			System.out.println("CLID: " + clid);
			// Get Channel ID
			Integer ctid = Integer.parseInt(eventInfo.get("ctid"));
			System.out.println("CTID: " + ctid);
			// Get Username for Client ID
			HashMap<String, String> hminfo = qry.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, clid);
			String nick = hminfo.get("client_nickname");
			
			// Output Userinfo
			//System.out.println("CLIENTINFO FOR " + clid);
			//outputHashMap(hminfo);
			
			// Update/Add User in HashMap
			userInChannel.put(nick, ctid);	
		}
		
	}
	
	/*
	 * Just output all key / value pairs
	 */
	private void outputHashMap(HashMap<String, String> hm){		
	    Collection<String> cValue = hm.values();
	    Collection<String> cKey = hm.keySet();
	    Iterator<String> itrValue = cValue.iterator();
	    Iterator<String> itrKey = cKey.iterator();		
		while (itrValue.hasNext() && itrKey.hasNext())
		{
			System.out.println(itrKey.next() + ": " + itrValue.next());
		}
	}
	// ts3server://ts3.hoster.com?port=9987&nickname=UserNickname&password=serverPassword&channel=MyDefaultChannel &channelpassword=defaultChannelPassword&token=TokenKey&addbookmark=1
	/**
	 * Produziert einen Hyperlink, der das direkte Joinen in den angegeben Channel ermöglicht.
	 * @param channelName
	 * @param userName
	 * @return Hyperlink
	 */
	private String produceTS3Link(String channelName, String userName){
		StringBuilder sb = new StringBuilder("ts3server://");
		sb.append(properties.getTs3Ip()).append("?port=").append(properties.getTs3Port()).append("&nickname=").append(userName)
			.append("&channel=").append(channelName);
		if(properties.getTs3Password() != null){
			sb.append("&password=").append(properties.getTs3Password());
		}
		return sb.toString();
		
	}
}
