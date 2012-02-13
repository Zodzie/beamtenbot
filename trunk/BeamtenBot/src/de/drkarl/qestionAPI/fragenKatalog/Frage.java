package de.drkarl.qestionAPI.fragenKatalog;

import java.util.LinkedList;

public class Frage {

	public String frage;
	public LinkedList<Antwort> antworten = new LinkedList<Antwort>();
	
	public Frage(String frage, LinkedList<Antwort> antworten) {
		super();
		this.frage = frage;
		this.antworten = antworten;
	}

	public String getFrage() {
		return frage;
	}

	public void setFrage(String frage) {
		this.frage = frage;
	}

	public LinkedList<Antwort> getAntworten() {
		return antworten;
	}

	public void addAntwort(Antwort antwort){
		antworten.addLast(antwort);
	}
	public void setAntworten(LinkedList<Antwort> antworten) {
		this.antworten = antworten;
	}
	
	public String toXML(){
		StringBuffer sb = new StringBuffer();
		
		for (Antwort antwort : antworten) {
			sb.append(antwort.toXML()+"\n");
		}
		return "<question>\n<frage>"+getFrage()+"</frage>"+"\n"+sb.toString()+"\n</question>\n";
	}
	
	
}
