package de.eidotter.beamtenbot;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration.ConfigurationException;
import org.jibble.pircbot.PircBot;

import de.drkarl.qestionAPI.FragenBot;
import de.eidotter.beamtenbot.ts3channelquery.ConnectionProperties;
import de.eidotter.beamtenbot.ts3channelquery.TeamspeakChannelQuery;


public class BeamtenBot extends PircBot{
	private static boolean DEBUG = false;
	private ConnectionProperties properties;
	public static final SimpleDateFormat SDF_GERMAN = new SimpleDateFormat("HH:mm' Uhr, 'dd.MM.yyyy");
	private FragenBot fragenBot;
	private TeamspeakChannelQuery tsQuery;
	
	public BeamtenBot(boolean debug, ConnectionProperties properties){
		this.properties = properties;
		DEBUG = debug;
		this.setName("BeamtenBot");
	}

	
	protected void onConnect() {
		super.onConnect();
		fragenBot = new FragenBot();
		try {
			tsQuery = new TeamspeakChannelQuery("connection.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		
		//prüfe Fragen und Antworten------------------------------------------
		fragenBot.questionAndAnswer(message, channel, this);
		//--------------------------------------------------------------------
		
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
		if(tsQuery != null){
			String active = tsQuery.getActiveChannelsString();
			if(active != null){
				this.sendMessage(sender, active);
			} else {
				this.sendMessage(sender, "Zurzeit nix los aufm TS3!");
			}
		}
		
		
	}

	@Override
	protected void onPrivateMessage(String sender, String login,
			String hostname, String message) {
		
		//füge mit Befehl Fragen hinzu---------------------------------------------
				fragenBot.getFragenKatalog().addQuestion(message,sender,this);
		//-------------------------------------------------------------------------
		
		// Teamspeak-Channelabfrage
		if(message.equals("ts3")){
			this.channelQuery(sender);
		}
	}

}
