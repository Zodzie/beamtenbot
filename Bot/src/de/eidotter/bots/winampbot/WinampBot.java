package de.eidotter.bots.winampbot;

import java.io.IOException;
import java.util.HashMap;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import de.hof.mainbot.Bot;
import de.hof.mainbot.Observer;

/**
 * Experimental Bot. DO NOT USE!!!
 * @author Michael
 *
 */
public class WinampBot implements Observer{
	Bot mainBot;
	
	public WinampBot() throws NickAlreadyInUseException, IOException, IrcException{
		mainBot = Bot.getInstance();
		mainBot.registerObserver(this);
	}

	@Override
	public void updateOnMessage(String channel, String sender, String login,
			String hostname, String message) {
		// TODO Auto-generated method stub
		
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

	@Override
	public String helpGetFunction() {
		return null;
		
	}

	@Override
	public HashMap<String, String> helpGetCommandos() {
		return null;
		
	}

	@Override
	public HashMap<String, String> helpGetPrivateCommandos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOnJoin(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub
		
	}

}
