package de.eidotter.bots.ts3bot;

public class User {
	
	private String nick;
	private Integer id;
	
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String toString(){
		return nick;
	}

}
