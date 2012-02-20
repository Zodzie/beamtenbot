package de.drkarl.bots.questionbot;

import java.util.HashMap;
import java.util.LinkedList;

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
	LinkedList<User> voteForNextQuestion;
	
	

	public FragenBot(Bot bot) {
		super();
		this.bot = bot;
		bot.registerObserver(this);
		this.punkteVerwaltung = new PunkteVerwaltung();
		this.fragenKatalog = new FragenKatalog();
		this.voteForNextQuestion = new LinkedList<User>();
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
	
	public void voteForNextQuestion(String message, String channel,String sender){
		if(message.equalsIgnoreCase("-fn")||message.equalsIgnoreCase("-FrageNext")){
			boolean userNotInList = true;
			for (User user : voteForNextQuestion) {
				if(user.getName().equalsIgnoreCase(sender)){
					userNotInList = false;
					System.out.println("Bereits abgestimmt");
				}
			}
			//füge user in Liste der User die für eine neue Frage voten.. bei 3 mache neue Frage
			if(userNotInList){
				if(voteForNextQuestion.size()+1==3){
					voteForNextQuestion = new LinkedList<User>();
					aktuelleFrage = getRandomFrage();
					askQuestion(message, channel, true);
				}else{
					voteForNextQuestion.addLast(new User(sender));
					bot.sendMessage(channel,  Colors.RED + voteForNextQuestion.size()+" Votes für eine neue Frage: "+ (3-voteForNextQuestion.size())+" Votes verbleiben");
					
				}
			}
		}
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
	
	/**
	 * Sendet einen Hinweis auf die Richtige Antwort zur aktuellen Frage
	 * @param message
	 * @param channel
	 * @param sender
	 */
	
	public void getTip(String message, String channel, String sender){
		if(message.equalsIgnoreCase("-fh")||message.equalsIgnoreCase("-FrageHelp")){
			StringBuilder sb = new StringBuilder();
			sb.append(aktuelleFrage.antworten.get(0).getAntwort().charAt(0));
			for (int i = 1; i < aktuelleFrage.antworten.get(0).getAntwort().length()-1; i++) {
				if((aktuelleFrage.antworten.get(0).getAntwort().charAt(i))==' '){
					sb.append(" ");
				}else{
					sb.append("*");
				}
			}
			sb.append(aktuelleFrage.antworten.get(0).getAntwort().charAt(aktuelleFrage.antworten.get(0).getAntwort().length()-1));
			bot.sendMessage(channel,  Colors.RED + sb.toString());			
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
		questionAndAnswer(message, channel, sender);
		getTip(message, channel, sender);
		voteForNextQuestion(message, channel, sender);
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

	@Override
	public String helpGetFunction() {
		return null;
		
	}

	@Override
	public HashMap<String, String> helpGetCommandos() {
		return null;
		
	}

	@Override
	public HashMap<String, String> helpGetPrivateCommandos() {
		// TODO Auto-generated method stub
		return null;
	}
}
