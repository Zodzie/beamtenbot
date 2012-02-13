package QestionAPI;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;

public class FragenBot {

	FragenKatalog fragenKatalog;
	Frage aktuelleFrage;
	boolean solved = true;
	
	

	public FragenBot() {
		super();
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
		aktuelleFrage = fragenKatalog.fragen.get(randomIndex);
		solved = false;
		return aktuelleFrage;
	}
	
	
	public void questionAndAnswer(String message, String channel, PircBot myBot){
		askQuestion(message, channel, myBot);
		checkAnswer(message, channel, myBot);
	}
	
	public void checkAnswer(String message, String channel, PircBot myBot){
				for (Antwort antwort : aktuelleFrage.antworten) {
					if(message.equalsIgnoreCase(antwort.getAntwort()) || message.equalsIgnoreCase(antwort.getAntwort())){
						//richtige Antwort
						myBot.sendMessage(channel, Colors.BOLD + Colors.RED + "richtige Antwort");
						solved = true;
					}
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
