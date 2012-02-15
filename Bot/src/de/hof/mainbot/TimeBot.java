package de.hof.mainbot;

import java.util.Date;

public class TimeBot implements Observer {
	Bot mainBot;
	
	public TimeBot(Bot mainBot){
		this.mainBot = mainBot;
		mainBot.registerObserver(this);
	}

	@Override
	public void updateOnMessage(String channel, String sender, String login,
			String hostname, String message) {
		mainBot.sendMessage(channel, new Date().toString());
	}

	@Override
	public void updateOnPrivateMessage(String sender, String login,
			String hostname, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateOnConnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateOnDisconnect() {
		// TODO Auto-generated method stub

	}

}
