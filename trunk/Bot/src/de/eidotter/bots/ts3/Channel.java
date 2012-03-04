package de.eidotter.bots.ts3;

import java.util.ArrayList;
import java.util.List;

public class Channel {
	private List<User> users;
	private String channelName;
	
	public Channel(String channelName){
		this.setChannelName(channelName);
		this.setUsers(new ArrayList<User>());
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public void addUser(User user) {
		this.users.add(user);		
	}
	
	public Integer indexOfUser(Integer id){
		for (int i = 0; i < this.users.size(); i++) {
			if(this.users.get(i).getId().compareTo(id) == 0){
				return i;
			}
		}
		return -1;
	}
	
	public void removeUser(Integer id){
		this.users.remove(this.indexOfUser(id));
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
	
	public String getUserString(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.getUsers().size(); i++) {
			if(i == this.getUsers().size() - 1){
				sb.append(this.getUsers().get(i));
			} else {
				sb.append(this.getUsers().get(i)).append(", ");
			}
		}
		return sb.toString();
	}

}
