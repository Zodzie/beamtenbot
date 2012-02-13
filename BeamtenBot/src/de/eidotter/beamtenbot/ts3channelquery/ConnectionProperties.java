package de.eidotter.beamtenbot.ts3channelquery;

import org.apache.commons.configuration.PropertiesConfiguration;

public class ConnectionProperties {
	private String ircHostname;
	private String ircPassword;
	private int ircPort;
	private String ts3Ip;
	private int ts3Port;
	private String ts3Password;
	private String ts3ServerQueryName;
	private String ts3ServerQueryPassword;
	private int ts3ServerQueryPort;
	public String getIrcHostname() {
		return ircHostname;
	}
	public void setIrcHostname(String ircHostname) {
		this.ircHostname = ircHostname;
	}
	public String getIrcPassword() {
		return ircPassword;
	}
	public void setIrcPassword(String ircPassword) {
		this.ircPassword = ircPassword;
	}
	public int getIrcPort() {
		return ircPort;
	}
	public void setIrcPort(int ircPort) {
		this.ircPort = ircPort;
	}
	public String getTs3Ip() {
		return ts3Ip;
	}
	public void setTs3Ip(String ts3Ip) {
		this.ts3Ip = ts3Ip;
	}
	public String getTs3ServerQueryName() {
		return ts3ServerQueryName;
	}
	public void setTs3ServerQueryName(String ts3ServerQueryName) {
		this.ts3ServerQueryName = ts3ServerQueryName;
	}
	public String getTs3ServerQueryPassword() {
		return ts3ServerQueryPassword;
	}
	public void setTs3ServerQueryPassword(String ts3ServerQueryPassword) {
		this.ts3ServerQueryPassword = ts3ServerQueryPassword;
	}
	public int getTs3ServerQueryPort() {
		return ts3ServerQueryPort;
	}
	public void setTs3ServerQueryPort(int ts3ServerQueryPort) {
		this.ts3ServerQueryPort = ts3ServerQueryPort;
	}
	public int getTs3Port() {
		return ts3Port;
	}
	public void setTs3Port(int ts3Port) {
		this.ts3Port = ts3Port;
	}
	public String getTs3Password() {
		return ts3Password;
	}
	public void setTs3Password(String ts3Password) {
		this.ts3Password = ts3Password;
	}
	public void fillConfiguration(PropertiesConfiguration prop) {
		this.setIrcHostname(prop.getString("irc_hostname"));
		this.setIrcPassword(prop.getString("irc_password"));
		this.setIrcPort(prop.getInt("irc_port", 6667));
		
		this.setTs3Ip(prop.getString("ts3_ip"));
		this.setTs3Port(prop.getInt("ts3_port"));
		this.setTs3Password(prop.getString("ts3_pw", null));
		this.setTs3ServerQueryName(prop.getString("ts3_serverquery_name"));
		this.setTs3ServerQueryPassword(prop.getString("ts3_serverquery_password"));
		this.setTs3ServerQueryPort(prop.getInt("ts3_serverquery_port"));
	}
	
	

}
