package de.eidotter.beamtenbot;

import java.util.ArrayList;
import java.util.List;

public class Channel {
	private List<String> users;
	private String channelName;
	
	public Channel(String channelName){
		this.setChannelName(channelName);
		this.setUsers(new ArrayList<String>());
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public void addUser(String user) {
		this.users.add(user);		
	}

}
