package de.hof.mainbot;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import de.drkarl.bots.honbot.HonBot;
import de.drkarl.bots.questionbot.FragenBot;
import de.eidotter.bots.notify.NotifyBot;
import de.eidotter.bots.timer.TeaTimerBot;
import de.eidotter.bots.ts3.TeamspeakBot;
import de.eidotter.bots.weather.WeatherBot;

public class BotMain {

	public static void main(String[] args) throws NickAlreadyInUseException, IOException, IrcException {
		Bot bot = Bot.getInstance();		
		
		// Einhängen der Unterbots
		FragenBot fBot = new FragenBot(bot);
		HonBot honBot = new HonBot(bot);
		TeamspeakBot tsBot = new TeamspeakBot(bot);
		TeaTimerBot teaBot = new TeaTimerBot(bot);
		TimeBot tBot = new TimeBot(bot);
		NotifyBot nBot = new NotifyBot(bot);
		WeatherBot wBot = new WeatherBot(bot);
		
	}

}
