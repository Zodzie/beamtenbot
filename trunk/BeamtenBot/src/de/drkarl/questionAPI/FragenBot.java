package de.drkarl.questionAPI;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;

import de.drkarl.questionAPI.fragenKatalog.Antwort;
import de.drkarl.questionAPI.fragenKatalog.Frage;
import de.drkarl.questionAPI.fragenKatalog.FragenKatalog;
import de.drkarl.questionAPI.punkteSystem.PunkteVerwaltung;
import de.drkarl.questionAPI.punkteSystem.User;

public class FragenBot {

	FragenKatalog fragenKatalog;
	PunkteVerwaltung punkteVerwaltung;
	Frage aktuelleFrage;
	boolean solved = true;
	
	

	public FragenBot() {
		super();
		this.punkteVerwaltung = new PunkteVerwaltung();
		this.fragenKatalog = new FragenKatalog();
	}

	public FragenKatalog getFragenKatalog() {
		return fragenKatalog;
	}

	public void setFragenKatalog(FragenKatalog fragenKatalog) {
		this.fragenKatalog = fragenKatalog;
	}
	
	public PunkteVerwaltung getPunkteVerwaltung() {
		return punkteVerwaltung;
	}

	public void setPunkteVerwaltung(PunkteVerwaltung punkteVerwaltung) {
		this.punkteVerwaltung = punkteVerwaltung;
	}

	public Frage getAktuelleFrage() {
		return aktuelleFrage;
	}

	public void setAktuelleFrage(Frage aktuelleFrage) {
		this.aktuelleFrage = aktuelleFrage;
	}

	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	
	/**
	 * gibt eine Zahl von 0-i zurück
	 * @param i
	 * @return
	 */
	public int getRandomNumber(int i){
		return (int)(Math.random()*i);
	}
	
	public Frage getRandomFrage(){
		int randomIndex = getRandomNumber(fragenKatalog.getFragen().size());
		aktuelleFrage = fragenKatalog.getFragen().get(randomIndex);
		solved = false;
		return aktuelleFrage;
	}
	
	
	public void questionAndAnswer(String message, String channel,String sender, PircBot myBot){
		askQuestion(message, channel, myBot,false);
		checkAnswer(message, channel, sender, myBot);
		printPunkteVerwaltung(message, channel, myBot);
	}
	
	public void checkAnswer(String message, String channel, String sender, PircBot myBot){
			if(aktuelleFrage!=null){
				for (Antwort antwort : aktuelleFrage.antworten) {
					if(message.equalsIgnoreCase(antwort.getAntwort()) || message.equalsIgnoreCase(antwort.getAntwort())){
						//richtige Antwort
						punkteVerwaltung.rewardUser(sender, 1);
						myBot.sendMessage(channel, Colors.BOLD + Colors.GREEN + "richtige Antwort");
						solved = true;
						askQuestion(message, channel, myBot, true);
						
					}
				}
			}
	}
	
	

	public void printPunkteVerwaltung(String message, String channel, PircBot myBot){
		if(message.equalsIgnoreCase("-stats")||message.equalsIgnoreCase("-s")){
			for (User user : this.getPunkteVerwaltung().getUserList() ) {
				myBot.sendMessage(channel,  Colors.BLUE + user.toString());
			}
			
		}
	}
	
	public void askQuestion(String message, String channel, PircBot myBot, boolean passVaidation){
		if(message.equalsIgnoreCase("-frage")||message.equalsIgnoreCase("-f")|| passVaidation){
			if(solved == true){
				myBot.sendMessage(channel, Colors.BOLD + Colors.RED +  getRandomFrage().getFrage());
			}else{
				myBot.sendMessage(channel, Colors.BOLD + Colors.RED +  aktuelleFrage.getFrage());
			}
		}
	}
	
	public static void main(String[] args) {
		FragenBot f = new FragenBot();
		System.out.println(f.punkteVerwaltung.toString());
	}
}
