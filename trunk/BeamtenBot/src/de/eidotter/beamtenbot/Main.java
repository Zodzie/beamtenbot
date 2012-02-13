package de.eidotter.beamtenbot;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import de.eidotter.beamtenbot.ts3channelquery.ConnectionProperties;

public class Main {

	/**
	 * @param args
	 * @throws IrcException 
	 * @throws IOException 
	 * @throws NickAlreadyInUseException 
	 * @throws ConfigurationException 
	 */
	public static void main(String[] args) throws NickAlreadyInUseException, IOException, IrcException, ConfigurationException {
		PropertiesConfiguration prop2 = new PropertiesConfiguration("connection.properties");
		prop2.setListDelimiter(';');
		ConnectionProperties prop = new ConnectionProperties();
		prop.setIrcHostname(prop2.getString("irc_hostname"));
		prop.setIrcPassword(prop2.getString("irc_password"));
		prop.setIrcPort(prop2.getInt("irc_port", 6667));
		prop.setBotNick(prop2.getString("bot_nick", "bot"));
		prop.setChannelList(prop2.getString("channel_list"));
		
		BeamtenBot bot = new BeamtenBot(true, prop.getBotNick());
		bot.setVerbose(true);
		bot.connect(prop.getIrcHostname(), prop.getIrcPort(), prop.getIrcPassword());
		
		// Join Channels
		String[] channels = prop.getChannelList().split(";");
		for (int i = 0; i < channels.length; i++) {
			if(channels[i].contains(" ")){
				String[] channelAndKey = channels[i].split(" ");
				bot.joinChannel(channelAndKey[0], channelAndKey[1]);
			} else {
				bot.joinChannel(channels[i]);
			}
		}
	}

}
