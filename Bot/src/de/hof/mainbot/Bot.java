package de.hof.mainbot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

import de.hof.mainbot.helpers.BotProperties;
import de.hof.mainbot.helpers.Logger;

public class Bot extends PircBot implements Observable{
	// Properties Filename
	private static final String PROPERTIES_FILENAME = "bot.properties";
	// Key-Konstanten (siehe bot.properties)
	private static final String KEY_NAME = "name";
	private static final String KEY_IRC_HOSTNAME = "hostname";
	private static final String KEY_IRC_PORT = "port";
	private static final String KEY_IRC_PASSWORD = "password";
	private static final String KEY_DEBUG = "debug";
	private static final String KEY_VERBOSE = "verbose";
	private static final String KEY_CHANNELS = "channels";
	// Logging-Prefix
	private static final String LOG_PREFIX = "MainBot";
	private static final String HELP_MESSAGE = Colors.BLUE + Colors.BOLD + "Verf�gbare Kommandos und Bots:";
	// Liste mit allen Observern
	private List<Observer> observers;
	// Singleton-Instanz
	private static Bot instance;
	// Einstellungen
	private BotProperties settings;
	// DEBUG-Modus
	private static boolean DEBUG;
	
	private Bot() throws NickAlreadyInUseException, IOException, IrcException{
		this.observers = new ArrayList<Observer>();
		this.settings = new BotProperties(PROPERTIES_FILENAME);
		// Einstellungen setzen
		this.setName(this.settings.getString(KEY_NAME));
		this.setVerbose(this.settings.getBoolean(KEY_VERBOSE));
		DEBUG = this.settings.getBoolean(KEY_DEBUG);
		// Zum IRC Server verbinden
		if(this.settings.getString(KEY_IRC_PASSWORD) == null || this.settings.getString(KEY_IRC_PASSWORD).equals("")){
			// Kein Passwort vorhanden
			this.connect(this.settings.getString(KEY_IRC_HOSTNAME), this.settings.getInteger(KEY_IRC_PORT, 6667));
		} else {
			// Passwort vorhanden
			this.connect(this.settings.getString(KEY_IRC_HOSTNAME), this.settings.getInteger(KEY_IRC_PORT, 6667), this.settings.getString(KEY_IRC_PASSWORD));
		}
		// Channelliste joinen
		String[] channelList = this.settings.getStringList(KEY_CHANNELS);
		if(channelList != null){
			for (String string : channelList) {
				if(string.contains(" ")){
					// Channel hat Passwort
					String[] chanAndPw = string.split(" ");
					this.joinChannel(chanAndPw[0], chanAndPw[1]);
				} else {
					this.joinChannel(string);
				}
			}
		}
	}
	
	public static Bot getInstance() throws NickAlreadyInUseException, IOException, IrcException{
		if(instance != null){
			return instance;
		} else {
			return new Bot();
		}
	}

	@Override
	protected void onConnect() {
		this.notifyOnConnect();
	}



	@Override
	protected void onDisconnect() {
		this.notifyOnDisconnect();
	}



	@Override
	protected void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if(message.equals("-help")){
			this.printHelp(false, channel, sender);
		} else {
			this.notifyOnMessage(channel, sender, login, hostname, message);
		}
	}


	/**
	 * Gibt die Helpnachrichten aus.
	 * @param privateMessage	boolean
	 * @param channel	String
	 * @param sender	String
	 */
	private void printHelp(boolean privateMessage, String channel, String sender) {
		if(privateMessage){
			this.sendMessage(sender, HELP_MESSAGE);
		} else {
			this.sendMessage(channel, HELP_MESSAGE);
		}
		for (Observer ob : this.observers) {
			HashMap<String, String> commandos = null;
			if(privateMessage){
				commandos = ob.helpGetPrivateCommandos();
				if(commandos != null){
					// Bot hat private Kommandos
					this.sendMessage(sender, ob.helpGetFunction());
					for (Entry<String, String> entry : commandos.entrySet()) {
						this.sendMessage(sender, "   " + entry.getKey() + " : " + entry.getValue());
					}
				}
			} else {
				commandos = ob.helpGetCommandos();
				if(commandos != null){
					// Bot hat public Kommandos
					this.sendMessage(channel, ob.helpGetFunction());
					for (Entry<String, String> entry : commandos.entrySet()) {
						this.sendMessage(channel, "   " + entry.getKey() + " : " + entry.getValue());
					}
				}
			}
		}
	}

	@Override
	protected void onPrivateMessage(String sender, String login,
			String hostname, String message) {
		if(message.equals("-help")){
			this.printHelp(true, null, sender);
		} else {
			this.notifyOnPrivateMessage(sender, login, hostname, message);
		}
	}
	
	



	@Override
	protected void onJoin(String channel, String sender, String login,
			String hostname) {
		this.notifyOnJoin(channel, sender, login, hostname);
	}

	public void notifyOnJoin(String channel, String sender, String login,
			String hostname) {
		for (Observer o : this.observers) {
			o.updateOnJoin(channel, sender, login, hostname);
		}
	}

	@Override
	public void registerObserver(Observer o) {
		if(DEBUG){
			Logger.logVerbose(LOG_PREFIX, "Neuer Observer wurde registriert: " + o.toString());
		}
		observers.add(o);
	}

	@Override
	public void unregisterObserver(Observer o) {
		if(observers.contains(o)){
			if(DEBUG) Logger.logVerbose(LOG_PREFIX, "Observer hat sich abgemeldet: " + o.toString());
			observers.remove(o);
		}
	}

	@Override
	public void notifyOnMessage(String channel, String sender, String login,
			String hostname, String message) {
		for (Observer o : this.observers){
			o.updateOnMessage(channel, sender, login, hostname, message);
		}		
	}

	@Override
	public void notifyOnPrivateMessage(String sender, String login,
			String hostname, String message) {
		for (Observer o : this.observers) {
			o.updateOnPrivateMessage(sender, login, hostname, message);
		}
	}

	@Override
	public void notifyOnConnect() {
		for (Observer o : this.observers) {
			o.updateOnConnect();
		}
	}

	@Override
	public void notifyOnDisconnect() {
		for (Observer o : this.observers) {
			o.updateOnDisconnect();
		}		
	}

}
