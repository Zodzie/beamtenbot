package de.eidotter.bots.timer;

import java.util.HashMap;
import java.util.Timer;

import de.hof.mainbot.Bot;
import de.hof.mainbot.Observer;

/**
 * Stellt einen Timer für z.B. Pizza zur Verfügung.
 * Start des Timers erfolgt mit einer privaten Nachricht an den Bot der 
 * Form -timer ::Objekt:: ::Minuten::.
 * Beispiel:<br> -timer Pizza 13<br/>
 * Erstellt einen 13-Minuten-Timer für eine Pizza.
 * @author Michael
 * @version 0.1
 */
public class TeaTimerBot implements Observer {
	public static final String BOT_NAME = "TeaTimer";
	private Bot mainBot;
	private Timer timer = new Timer(true);
	private static final String TIMER_COMMANDO = "-timer";
	
	public TeaTimerBot(Bot mainBot){
		this.mainBot = mainBot;
		this.mainBot.registerObserver(this);
	}

	@Override
	public void updateOnMessage(String channel, String sender, String login,
			String hostname, String message) {

	}

	@Override
	public void updateOnPrivateMessage(String sender, String login,
			String hostname, String message) {
		if(message.trim().startsWith(TIMER_COMMANDO)){
			String[] elements = message.split(" ");
			if(elements.length == 3){
				String obj = elements[1];
				Integer duration = null;
				try{
					duration = Integer.parseInt(elements[2]);
				} catch (NumberFormatException e){
					wrongSyntax(sender);
					return;
				}
				if(duration != null){
					this.mainBot.sendMessage(sender, "Starte den " + duration + "-Minuten-Timer für deine " + obj + ".");
					TeaTimerTask task = new TeaTimerTask(mainBot, sender, obj);
					timer.schedule(task, duration * 60 * 1000);
				}
			} else {
				wrongSyntax(sender);
			}
		}
	}

	private void wrongSyntax(String sender) {
		this.mainBot.sendMessage(sender, "Bitte benutze folgende Syntax: + " + TIMER_COMMANDO + " <Objekt> <Minuten>");
		this.mainBot.sendMessage(sender, "Beispiel: " + TIMER_COMMANDO + " Pizza 13");
	}

	@Override
	public void updateOnConnect() {

	}

	@Override
	public void updateOnDisconnect() {

	}

	@Override
	public String helpGetFunction() {
		return BOT_NAME;
	}

	@Override
	public HashMap<String, String> helpGetCommandos() {
		return null;
	}

	@Override
	public HashMap<String, String> helpGetPrivateCommandos() {
		HashMap<String, String> commandos = new HashMap<String, String>();
		commandos.put(TIMER_COMMANDO + " <Name> <Minuten>", "Startet einen <Minuten>-Timer für <Name>. Bsp.: -timer Pizza 12");
		return commandos;
	}

	@Override
	public void updateOnJoin(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub
		
	}

}
