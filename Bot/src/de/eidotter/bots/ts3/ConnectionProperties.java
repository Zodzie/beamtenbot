package de.eidotter.bots.ts3;

import java.util.Properties;

public class ConnectionProperties {
	private String ts3Ip;
	private int ts3Port;
	private String ts3Password;
	private String ts3ServerQueryName;
	private String ts3ServerQueryPassword;
	private int ts3ServerQueryPort;
	private String ts3IpInternet;
	private Boolean debugMode;
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
	public Boolean getDebugMode() {
		return debugMode;
	}
	public void setDebugMode(Boolean debugMode) {
		this.debugMode = debugMode;
	}
	
	
	/**
	 * Liest aus einer Properties alle Einstellungen ein.
	 * @param prop	Properties
	 * @return	boolean Success
	 */
	public boolean parseFromProperties(Properties prop){
		this.setTs3Ip(prop.getProperty(TeamspeakBot.CONFIG_KEY_TS3_IP));
		this.setTs3IpInternet(prop.getProperty(TeamspeakBot.CONFIG_KEY_TS3_IP_INTERNET));
		this.setTs3Password(prop.getProperty(TeamspeakBot.CONFIG_KEY_TS3_PW));
		
		// TS3-Port
		int ts3Port;
		try{
			ts3Port = Integer.parseInt(prop.getProperty(TeamspeakBot.CONFIG_KEY_TS3_PORT));
			this.setTs3Port(ts3Port);
		} catch (NumberFormatException e){
			e.printStackTrace();
			return false;
		}
		
		this.setTs3ServerQueryName(prop.getProperty(TeamspeakBot.CONFIG_KEY_TS3_SERVERQUERY_ACCOUNT));
		this.setTs3ServerQueryPassword(prop.getProperty(TeamspeakBot.CONFIG_KEY_TS3_SERVERQUERY_PW));
		
		// TS3-Serverquery-Port
		int ts3QryPort;
		try{
			ts3QryPort = Integer.parseInt(prop.getProperty(TeamspeakBot.CONFIG_KEY_TS3_SERVERQUERY_PORT));
			this.setTs3ServerQueryPort(ts3QryPort);
		} catch (NumberFormatException e){
			e.printStackTrace();
			return false;
		}
		
		Boolean debug = Boolean.parseBoolean(prop.getProperty(TeamspeakBot.CONFIG_KEY_DEBUG_MODE));
		if(debug != null){
			this.setDebugMode(debug);
		} else {
			return false;
		}
		return true;		
	}
	public String getTs3IpInternet() {
		return ts3IpInternet;
	}
	public void setTs3IpInternet(String ts3IpInternet) {
		this.ts3IpInternet = ts3IpInternet;
	}
	
	

}
