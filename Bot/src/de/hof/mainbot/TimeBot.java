package de.hof.mainbot;

import java.util.Date;

/**
 * Beispielhafte Implementierung eines Bots, der in den MainBot
 * eingehängt werden kann.
 * @author Michael
 *
 */
public class TimeBot implements Observer {
	Bot mainBot;
	
	public TimeBot(Bot mainBot){
		this.mainBot = mainBot;
		// Diesen Bot am Mainbot einhängen.
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

}
