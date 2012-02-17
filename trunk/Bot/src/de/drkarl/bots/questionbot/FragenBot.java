package de.drkarl.bots.questionbot;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;

import de.drkarl.bots.questionbot.fragenKatalog.Antwort;
import de.drkarl.bots.questionbot.fragenKatalog.Frage;
import de.drkarl.bots.questionbot.fragenKatalog.FragenKatalog;
import de.drkarl.bots.questionbot.punkteSystem.PunkteVerwaltung;
import de.drkarl.bots.questionbot.punkteSystem.User;
import de.hof.mainbot.Bot;

public class FragenBot implements de.hof.mainbot.Observer {

	Bot bot;
	FragenKatalog fragenKatalog;
	PunkteVerwaltung punkteVerwaltung;
	Frage aktuelleFrage;
	boolean solved = true;
	
	

	public FragenBot(Bot bot) {
		super();
		this.bot = bot;
		bot.registerObserver(this);
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
	
	
	public void questionAndAnswer(String message, String channel,String sender){
		askQuestion(message, channel, false);
		checkAnswer(message, channel, sender);
		printPunkteVerwaltung(message, channel);
	}
	
	public void checkAnswer(String message, String channel, String sender){
			if(aktuelleFrage!=null){
				for (Antwort antwort : aktuelleFrage.antworten) {
					if(message.equalsIgnoreCase(antwort.getAntwort()) || message.equalsIgnoreCase(antwort.getAntwort())){
						//richtige Antwort
						punkteVerwaltung.rewardUser(sender, 1);
						bot.sendMessage(channel, Colors.BOLD + Colors.GREEN + "Richtige Antwort: " + antwort.getAntwort() + " Gelöst von: " + sender);
						solved = true;
						askQuestion(message, channel, true);
						
					}
				}
			}
	}
	
	

	public void printPunkteVerwaltung(String message, String channel){
		if(message.equalsIgnoreCase("-stats")||message.equalsIgnoreCase("-s")){
			for (User user : this.getPunkteVerwaltung().getUserList() ) {
				bot.sendMessage(channel,  Colors.BLUE + user.toString());
			}
			
		}
	}
	
	public void askQuestion(String message, String channel,boolean passVaidation){
		if(message.equalsIgnoreCase("-frage")||message.equalsIgnoreCase("-f")|| passVaidation){
			if(solved == true){
				bot.sendMessage(channel, Colors.BOLD + Colors.RED +  getRandomFrage().getFrage());
			}else{
				bot.sendMessage(channel, Colors.BOLD + Colors.RED +  aktuelleFrage.getFrage());
			}
		}
	}
	
	

	@Override
	public void updateOnMessage(String channel, String sender, String login,
			String hostname, String message) {
		questionAndAnswer(message, channel, sender	);
		
	}

	@Override
	public void updateOnPrivateMessage(String sender, String login,
			String hostname, String message) {
		this.fragenKatalog.addQuestion(message, sender, bot);
	}

	@Override
	public void updateOnConnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateOnDisconnect() {
		// TODO Auto-generated method stub
		
	}
}
