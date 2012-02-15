package de.hof.mainbot;

import java.util.ArrayList;
import java.util.List;

import org.jibble.pircbot.PircBot;

public class Bot extends PircBot implements Observable{
	private List<Observer> observers;
	private static Bot instance;
	
	private Bot(String name){
		this.observers = new ArrayList<Observer>();
		this.setName(name);
	}
	
	public static Bot getInstance(String name){
		if(instance != null){
			return instance;
		} else {
			return new Bot(name);
		}
	}

	@Override
	protected void onConnect() {
		this.notifyOnConnect();
	}



	@Override
	protected void onDisconnect() {
		this.notifyOnDisconnect();
	}



	@Override
	protected void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		this.notifyOnMessage(channel, sender, login, hostname, message);
	}



	@Override
	protected void onPrivateMessage(String sender, String login,
			String hostname, String message) {
		this.notifyOnPrivateMessage(sender, login, hostname, message);
	}



	@Override
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void unregisterObserver(Observer o) {
		if(observers.contains(o)){
			observers.remove(o);
		}
	}

	@Override
	public void notifyOnMessage(String channel, String sender, String login,
			String hostname, String message) {
		for (Observer o : this.observers){
			o.updateOnMessage(channel, sender, login, hostname, message);
		}		
	}

	@Override
	public void notifyOnPrivateMessage(String sender, String login,
			String hostname, String message) {
		for (Observer o : this.observers) {
			o.updateOnPrivateMessage(sender, login, hostname, message);
		}
	}

	@Override
	public void notifyOnConnect() {
		for (Observer o : this.observers) {
			o.updateOnConnect();
		}
	}

	@Override
	public void notifyOnDisconnect() {
		for (Observer o : this.observers) {
			o.updateOnDisconnect();
		}		
	}

}
