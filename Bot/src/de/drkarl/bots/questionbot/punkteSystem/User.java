package de.drkarl.bots.questionbot.punkteSystem;

public class User {

	private String name;
	private Punkte punkte;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User(String name, Punkte punkte) {
		super();
		this.name = name;
		this.punkte = punkte;
	}

	public Punkte getPunkte() {
		return punkte;
	}

	public void setPunkte(Punkte punkte) {
		this.punkte = punkte;
	}

	public User(String name) {
		super();
		this.name = name;
	}
	
	public String toXML(){
		return "<user>\n<name>"+getName()+"</name>\n"+punkte.toXML()+"</user>";
	}
	
	public String toString() {
		return getName()+": "+getPunkte().toString();
	}
	
	
}
