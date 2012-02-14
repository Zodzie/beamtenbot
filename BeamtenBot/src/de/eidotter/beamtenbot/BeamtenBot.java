package de.eidotter.beamtenbot;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;

import de.drkarl.questionAPI.FragenBot;
import de.eidotter.beamtenbot.ts3channelquery.Channel;
import de.eidotter.beamtenbot.ts3channelquery.TeamspeakChannelQuery;


public class BeamtenBot extends PircBot{
	private static boolean DEBUG = false;
	private FragenBot fragenBot;
	private TeamspeakChannelQuery tsQuery;
	
	public BeamtenBot(boolean debug, String nick){
		DEBUG = debug;
		this.setName(nick);
	}

	
	protected void onConnect() {
		super.onConnect();
		fragenBot = new FragenBot();
		try {
			tsQuery = new TeamspeakChannelQuery("connection.properties", DEBUG);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		// Teamspeak-Channelabfrage
		if(message.equals("ts3")){
			this.channelQuery(channel);
		} else {
			//prüfe Fragen und Antworten------------------------------------------
			fragenBot.questionAndAnswer(message, channel,sender, this);
			//--------------------------------------------------------------------
		}		
		if(DEBUG)	System.out.printf("Incoming Message in Channel %s from %s: %s\n",channel, sender, message);
	}
	
	/**
	 * Schickt eine PN an den Sender mit allen bevölkerten Channels.
	 * @param sender
	 */
	private void channelQuery(String sender) {
		if(tsQuery != null){
//			String active = tsQuery.getActiveChannelsString();
//			if(active != null){
//				this.sendMessage(sender, active);
//			} else {
//				this.sendMessage(sender, "Zurzeit nix los aufm TS3!");
//			}
			List<Channel> channelList = tsQuery.getActiveChannelList();
			if(channelList != null){
				for (Channel channel : channelList) {
					this.sendMessage(sender, Colors.BOLD + Colors.BLUE + channel.getChannelName() + ": " + Colors.NORMAL + channel.getUserString());
				}
			} else {
				this.sendMessage(sender, Colors.BOLD + Colors.BLUE + "Zurzeit keine User auf dem TS");
			}
		}		
	}

	@Override
	protected void onPrivateMessage(String sender, String login,
			String hostname, String message) {
		
		
		// Teamspeak-Channelabfrage
		if(message.equals("ts3")){
			this.channelQuery(sender);
		} else {
			//füge mit Befehl Fragen hinzu---------------------------------------------
			fragenBot.getFragenKatalog().addQuestion(message,sender,this);
			//-------------------------------------------------------------------------
		}
	}

}
