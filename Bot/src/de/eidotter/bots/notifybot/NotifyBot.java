package de.eidotter.bots.notifybot;

import java.util.ArrayList;
import java.util.HashMap;

import de.hof.mainbot.Bot;
import de.hof.mainbot.Observer;
import de.hof.mainbot.helpers.BotProperties;

public class NotifyBot implements Observer {
	public static final String BOT_NAME = "NotifyBot";
	public static final String BOT_COMMAND = "-tell ";
	public static final String BOT_HELP_SYNTAX = BOT_COMMAND + " <Nick> <Nachricht>";
	public static final String BOT_HELP = "Schickt eine Nachricht an den angegebenen Nick, sobald dieser wieder auf den Server kommt.";
	private static final String SETTINGS_FILE_NAME = "notifybot.properties";
	private HashMap<String, ArrayList<String>> messages;
	private BotProperties settings;
	private int maxMessages;
	private Bot mainBot;
	
	public NotifyBot(Bot mainBot){
		this.mainBot = mainBot;
		this.messages = new HashMap<String, ArrayList<String>>();
		this.settings = new BotProperties(SETTINGS_FILE_NAME);
		
		// Settings lesen
		this.maxMessages = this.settings.getInteger("max_messages", 10);
		
		// Für Updates registrieren
		this.mainBot.registerObserver(this);
	}
	
	
	@Override
	public void updateOnMessage(String channel, String sender, String login,
			String hostname, String message) {
	}

	@Override
	public void updateOnPrivateMessage(String sender, String login,
			String hostname, String message) {
		if(message.toLowerCase().startsWith("-tell ")){
			String[] parts = message.split("\\s+");
			if(parts.length > 2){
				String recipient = parts[1];
				String rec_message = message.substring(message.indexOf(recipient) + recipient.length() + 1);
				this.storeMessage(recipient, rec_message, sender);
			} else {
				// Falsches Format
			}
		}
	}

	private void storeMessage(String recipient, String rec_message, String sender) {
		String toPut = recipient + ", " + sender + " lässt dir ausrichten: " + rec_message;
		if(this.messages.containsKey(recipient.toLowerCase())){
			// Bereits gespeicherte Nachrichten prüfen
			if(this.messages.get(recipient.toLowerCase()).size() < this.maxMessages){
				// Platz vorhanden
				this.messages.get(recipient.toLowerCase()).add(toPut);
				this.mainBot.sendMessage(sender, "Nachricht an " + recipient + " wurde gespeichert und wird ihm zugestellt, sobald dieser wieder online ist.");
			} else {
				// Maxmimalzahl an gespeicherten Nachrichten erreicht
				this.mainBot.sendMessage(sender, "Postfach von " + recipient + " ist leider voll!");
			}
		} else {
			ArrayList<String> ar = new ArrayList<String>();
			ar.add(toPut);
			this.messages.put(recipient.toLowerCase(), ar);
			this.mainBot.sendMessage(sender, "Nachricht an " + recipient + " wurde gespeichert und wird ihm zugestellt, sobald dieser wieder online ist.");
		}
		
	}


	@Override
	public void updateOnConnect() {
	}

	@Override
	public void updateOnDisconnect() {
	}

	@Override
	public void updateOnJoin(String channel, String sender, String login,
			String hostname) {
		if(this.messages.containsKey(sender.toLowerCase())){
			// Nick Nachrichten zukommen lassen
			for (String mess : this.messages.get(sender.toLowerCase())) {
				this.mainBot.sendMessage(sender, mess);
			}
			// Nachrichten löschen
			this.messages.remove(sender.toLowerCase());
		}
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
		commandos.put(BOT_HELP_SYNTAX, BOT_HELP);
		return commandos;
	}

}
