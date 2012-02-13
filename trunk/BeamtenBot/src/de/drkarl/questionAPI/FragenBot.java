package de.drkarl.questionAPI;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;

import de.drkarl.questionAPI.fragenKatalog.Antwort;
import de.drkarl.questionAPI.fragenKatalog.Frage;
import de.drkarl.questionAPI.fragenKatalog.FragenKatalog;
import de.drkarl.questionAPI.punkteSystem.PunkteVerwaltung;

public class FragenBot {

	FragenKatalog fragenKatalog;
	PunkteVerwaltung punkteVerwaltung;
	Frage aktuelleFrage;
	boolean solved = true;
	
	

	public FragenBot() {
		super();
		punkteVerwaltung = new PunkteVerwaltung();
		this.fragenKatalog = new FragenKatalog();
		fragenKatalog.initParse();
	}

	public FragenKatalog getFragenKatalog() {
		return fragenKatalog;
	}

	public void setFragenKatalog(FragenKatalog fragenKatalog) {
		this.fragenKatalog = fragenKatalog;
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
		askQuestion(message, channel, myBot);
		checkAnswer(message, channel, sender, myBot);
	}
	
	public void checkAnswer(String message, String channel, String sender, PircBot myBot){
				for (Antwort antwort : aktuelleFrage.antworten) {
					if(message.equalsIgnoreCase(antwort.getAntwort()) || message.equalsIgnoreCase(antwort.getAntwort())){
						//richtige Antwort
						punkteVerwaltung.rewardUser(sender, 1);
						myBot.sendMessage(channel, Colors.BOLD + Colors.RED + "richtige Antwort");
						solved = true;
					}
				}
	}
	
	public void printPunkteVerwaltung(String message, String channel, PircBot myBot){
		if(message.equalsIgnoreCase("-stats")||message.equalsIgnoreCase("-s")){
			myBot.sendMessage(channel, Colors.BOLD + Colors.RED + punkteVerwaltung.toString());
		}
	}
	
	public void askQuestion(String message, String channel, PircBot myBot){
		if(message.equalsIgnoreCase("-frage")||message.equalsIgnoreCase("-f")){
			if(solved == true){
				myBot.sendMessage(channel, Colors.BOLD + Colors.RED +  getRandomFrage().getFrage());
			}else{
				myBot.sendMessage(channel, Colors.BOLD + Colors.RED +  aktuelleFrage.getFrage());
			}
		}
	}
	
	
}
