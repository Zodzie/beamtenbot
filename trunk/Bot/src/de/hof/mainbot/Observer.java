package de.hof.mainbot;

import java.util.HashMap;


public interface Observer {
	
	public void updateOnMessage(String channel, String sender, String login,
			String hostname, String message);
	public void updateOnPrivateMessage(String sender, String login,
			String hostname, String message);
	public void updateOnConnect();
	public void updateOnDisconnect();
	public void updateOnJoin(String channel, String sender, String login, String hostname);
	/**
	 * Gibt den Namen des Bots zur�ck (z.B. HonBot, TimerBot, TSBot).
	 * @return	String
	 */
	public String helpGetFunction();
	
	/**
	 * Gibt eine HashMap mit Kommandas zur�ck, die per Channelnachricht ausgef�hrt werden k�nnen.
	 * Beispiel f�r TimeBot: HashMap mit einem Eintrag: key="-time" value="Gibt die aktuelle Uhrzeit an."
	 * @return	HashMap<String,String> commandos
	 */
	public HashMap<String, String> helpGetCommandos();
	
	/**
	 * Gibt eine HashMap mit Kommandos zur�ck, die per privater Nachricht an den Bot ausgef�hrt werden 
	 * k�nnen.
	 * @return	HashMap<String,String> commandos
	 */
	public HashMap<String, String> helpGetPrivateCommandos();

}
