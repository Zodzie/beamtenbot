package de.hof.mainbot;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import de.drkarl.bots.honbot.HonBot;
import de.drkarl.bots.questionbot.FragenBot;
import de.eidotter.bots.teatimerbot.TeaTimerBot;
import de.eidotter.bots.ts3bot.TeamspeakBot;

public class BotMain {

	public static void main(String[] args) throws NickAlreadyInUseException, IOException, IrcException {
		Bot bot = Bot.getInstance();		
		
		// Einhängen der Unterbots
		FragenBot fBot = new FragenBot(bot);
		HonBot honBot = new HonBot(bot);
		TeamspeakBot tsBot = new TeamspeakBot(bot);
		TeaTimerBot teaBot = new TeaTimerBot(bot);
		TimeBot tBot = new TimeBot(bot);
		
	}

}
