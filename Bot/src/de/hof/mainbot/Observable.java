package de.hof.mainbot;


public interface Observable {
//	public List<Observer> observers = null;
	
	public void registerObserver(Observer o);
	public void unregisterObserver(Observer o);
	public void notifyOnMessage(String channel, String sender, String login,
			String hostname, String message);
	public void notifyOnPrivateMessage(String sender, String login,
			String hostname, String message);
	public void notifyOnConnect();
	public void notifyOnDisconnect();
}
