package de.eidotter.beamtenbot.ts3channelquery;

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
	
	public String toString(){
		StringBuilder sb = new StringBuilder(this.getChannelName());
		sb.append(": ");
		for (int i = 0; i < this.users.size(); i++) {
			if(i == this.users.size() - 1){
				sb.append(this.users.get(i)).append("\n");
			} else {
				sb.append(this.users.get(i)).append(", ");
			}
		}
		return sb.toString();
	}

}
