package de.drkarl.bots.honbot;

import org.jibble.pircbot.User;

import de.hof.mainbot.Bot;
import de.hof.mainbot.Observable;
import de.hof.mainbot.Observer;

public class HonBot implements Observer {
	
	private Bot mainBot;
	
	public HonBot(Bot mainBot) {
		super();
		this.mainBot = mainBot;
		mainBot.registerObserver(this);
	}

	@Override
	public void updateOnMessage(String channel, String sender, String login,
			String hostname, String message) {
		if(message.equalsIgnoreCase("-hon")){
			User users[] = mainBot.getUsers("#lancenter");
			StringBuilder sb = new StringBuilder();
			sb.append(sender+" möchte eine HoN spielen: ");
			for (User user : users) {
				if(!user.getNick().contains("bot")&&!user.getNick().equals(sender)){
					sb.append(user.getNick() +" ");
				}
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
