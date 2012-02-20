package de.drkarl.bots.honbot;

import java.util.HashMap;

import org.jibble.pircbot.User;

import de.hof.mainbot.Bot;
import de.hof.mainbot.Observer;

public class HonBot implements Observer {
	
	private Bot mainBot;
	
	public HonBot(Bot mainBot) {
		super();
		this.mainBot = mainBot;
		mainBot.registerObserver(this);
	}

	/**
	 * sendet eine HoN-Benachrichtigung an alle User im #lancenter
	 */
	public void updateOnMessage(String channel, String sender, String login,
			String hostname, String message) {
		if(message.equalsIgnoreCase("-hon")){
			User users[] = mainBot.getUsers("#lancenter");
			StringBuilder sb = new StringBuilder();
			sb.append(sender+" möchte eine HoN spielen: ");
			//liste der User in string adden
			for (User user : users) {
				if(!user.getNick().contains("bot")&&!user.getNick().equals(sender)){
					if(user.getNick().startsWith("~")){
						sb.append(user.getNick().substring(1) +" ");
					}else{
						sb.append(user.getNick() +" ");
					}
				}
				//basti extrabenachtichtigung
				if(user.getNick().equals("basti")&&!sender.equals("basti")){
					mainBot.sendMessage("basti", sender+" möchte eine HoN spielen");
				}
			}
			mainBot.sendMessage("#lancenter", sb.toString());
		}
		
	}

	@Override
	public void updateOnPrivateMessage(String sender, String login,
			String hostname, String message) {
		
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

	

}
