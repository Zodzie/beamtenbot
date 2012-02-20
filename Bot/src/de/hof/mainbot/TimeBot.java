package de.hof.mainbot;

import java.util.Date;
import java.util.HashMap;

/**
 * Beispielhafte Implementierung eines Bots, der in den MainBot
 * eingeh�ngt werden kann.
 * @author Michael
 *
 */
public class TimeBot implements Observer {
	public static final String BOT_NAME = "TimeBot";
	Bot mainBot;
	
	public TimeBot(Bot mainBot){
		this.mainBot = mainBot;
		// Diesen Bot am Mainbot einh�ngen.
		mainBot.registerObserver(this);
	}

	@Override
	public void updateOnMessage(String channel, String sender, String login,
			String hostname, String message) {
		// Aktuelle Zeit senden
		if(message.equals("-time")){
			mainBot.sendMessage(channel, new Date().toString());
		}
	}

	@Override
	public void updateOnPrivateMessage(String sender, String login,
			String hostname, String message) {

	}

	@Override
	public void updateOnConnect() {

	}

	@Override
	public void updateOnDisconnect() {

	}

	@Override
	public String helpGetFunction() {
		// Botnamen zur�ckgeben
		return BOT_NAME;
		
	}

	@Override
	public HashMap<String, String> helpGetCommandos() {
		HashMap<String, String> commandos = new HashMap<String, String>();
		commandos.put("-time", "Gibt die aktuelle Uhrzeit zur�ck.");
		return commandos;
		
	}

	@Override
	public HashMap<String, String> helpGetPrivateCommandos() {
		// Null zur�ckgeben, da Bot keine Kommandos bei privaten Nachrichten hat.
		return null;
	}

}
