package de.hof.mainbot;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

public class BotMain {

	public static void main(String[] args) throws NickAlreadyInUseException, IOException, IrcException {
		Bot bot = Bot.getInstance("BBot");		
		bot.setVerbose(true);
		bot.connect("h1930837.stratoserver.net", 6667, "honhon");
		bot.joinChannel("#test");
		
		
		TimeBot tBot = new TimeBot(bot);
		//bot.registerObserver(tBot);
	}

}
