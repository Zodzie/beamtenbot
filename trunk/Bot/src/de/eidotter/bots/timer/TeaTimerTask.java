package de.eidotter.bots.timer;

import java.util.TimerTask;

import de.hof.mainbot.Bot;

public class TeaTimerTask extends TimerTask {
	
	private Bot mainBot;
	private String nick;
	private String object;
	
	public TeaTimerTask(Bot mainBot, String nick, String object){
		this.mainBot = mainBot;
		this.nick = nick;
		this.object = object;
	}

	@Override
	public void run() {
		this.mainBot.sendMessage(nick, "Deine " + object + " ist fertig!");
	}

}
