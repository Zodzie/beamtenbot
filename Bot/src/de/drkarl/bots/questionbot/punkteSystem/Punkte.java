package de.drkarl.bots.questionbot.punkteSystem;

public class Punkte {
	private int punkte;

	public int getPunkte() {
		return punkte;
	}

	public void setPunkte(int punkte) {
		this.punkte = punkte;
	}

	public Punkte(int punkte) {
		super();
		this.punkte = punkte;
	}
	
	public String toXML(){
		return "<punkte>"+getPunkte()+"</punkte>\n";
	}
	
	public String toString() {
		return Integer.toString(getPunkte());
	}
}
