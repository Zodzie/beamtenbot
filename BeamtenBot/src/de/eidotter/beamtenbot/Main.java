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
		ConnectionProperties prop = new ConnectionProperties();
		prop.setIrcHostname(prop2.getString("irc_hostname"));
		prop.setIrcPassword(prop2.getString("irc_password"));
		prop.setIrcPort(prop2.getInt("irc_port", 6667));
		
		prop.setTs3Ip(prop2.getString("ts3_ip"));
		prop.setTs3Port(prop2.getInt("ts3_port"));
		prop.setTs3Password(prop2.getString("ts3_pw", null));
		prop.setTs3ServerQueryName(prop2.getString("ts3_serverquery_name"));
		prop.setTs3ServerQueryPassword(prop2.getString("ts3_serverquery_password"));
		prop.setTs3ServerQueryPort(prop2.getInt("ts3_serverquery_port"));
		
		BeamtenBot bot = new BeamtenBot(true, prop);
		bot.setVerbose(true);
		bot.connect(prop.getIrcHostname(), prop.getIrcPort(), prop.getIrcPassword());
		//bot.joinChannel("#beamtendisco", "hwu4711");
		bot.joinChannel("#lancenter");
	}

}
