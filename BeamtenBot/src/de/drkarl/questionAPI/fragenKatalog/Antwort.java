package de.drkarl.questionAPI.fragenKatalog;

public class Antwort {

	public String antwort;

	public Antwort(String antwort) {
		super();
		this.antwort = antwort;
	}

	public String getAntwort() {
		return antwort;
	}

	public void setAntwort(String antwort) {
		this.antwort = antwort;
	}
	
	public String toXML(){
		return "<antwort>"+getAntwort()+"</antwort>";
	}
	
}
