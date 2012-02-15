package de.hof.mainbot;


public interface Observer {
	
	public void updateOnMessage(String channel, String sender, String login,
			String hostname, String message);
	public void updateOnPrivateMessage(String sender, String login,
			String hostname, String message);
	public void updateOnConnect();
	public void updateOnDisconnect();

}
