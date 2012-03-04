package de.eidotter.bots.weather;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jibble.pircbot.Colors;
import org.xml.sax.InputSource;

import de.hof.mainbot.Bot;
import de.hof.mainbot.Observer;
import de.hof.mainbot.helpers.Logger;

/**
 * Abfrage des Wetters für einen Ort. Holt sich die Wetter-Informationen von der Google
 * Weather API (unoffiziell, nicht von Google supported).
 * 
 * @author Michael
 * @version 0.1
 */
public class WeatherBot implements Observer {
	public static final String BOT_NAME = "WetterBot";
	public static final String BOT_CMD = "-wetter";
	public static final String HELP_SYNTAX = BOT_CMD + " <Ort>";
	public static final String HELP_DESCRIPTION = "Liefert das Wetter für den angegebenen Ort.";
	public static final String GOOGLE_API_URL = "http://www.google.com/ig/api?weather=";
	
	private Bot mainBot;
	
	public WeatherBot(Bot mainBot){
		this.mainBot = mainBot;
		mainBot.registerObserver(this);
	}

	@Override
	public void updateOnMessage(String channel, String sender, String login,
			String hostname, String message) {
		if(message.startsWith(BOT_CMD)){
			// Incoming Weather-Request
			this.onIncomingWeatherRequest(channel, sender, login, hostname, message);
		}

	}
	
	/**
	 * Wird aufgerufen, wenn der Bot eine Nachricht erhält, die mit dem BOT_CMD startet.
	 * @param channel	Channel oder null (PN)
	 * @param sender	Sender
	 * @param login		Login
	 * @param hostname	Hostname
	 * @param message	Ganze Nachricht
	 */
	private void onIncomingWeatherRequest(String channel, String sender,
			String login, String hostname, String message) {
		boolean isPn = (channel == null) ? true : false;
		if(message.split(" ").length > 1){
			// Abfrage an Google Weather API
			String ort = message.substring(BOT_CMD.length() + 1);
			URLConnection con = null;
			try {
				URL weatherURL = new URL(GOOGLE_API_URL + URLEncoder.encode(ort, "UTF-8"));
				Logger.logVerbose(BOT_NAME, "Wetter-URL: " + weatherURL.toString());
				con = weatherURL.openConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(con != null){
				Document weatherDoc = this.getXMLDocument(con);
				Element root = weatherDoc.getRootElement();
				if(root != null){
					this.sendWeatherInfo(root, (isPn) ? sender : channel, ort);
				} else {
					Logger.logError(BOT_NAME, "XML von Google konnte nicht geparst werden.");
				}
			}
		} else {
			// Wrong Syntax
			this.mainBot.sendMessage(isPn ? sender : channel, "Falsche Syntax: " + HELP_SYNTAX);
		}
		
	}

	private void sendWeatherInfo(Element root, String target, String ort) {
		if(!root.getAttributeValue("version").equals("1")){
			// Neue Google Weather API Version
			Logger.logVerbose(BOT_NAME, "xml_api_reply-Version der Google Weather API hat sich geändert!");
		}
		if(root.getChild("weather").getChild("problem_cause") != null){
			// Fehler bei Abfrage, Ort existiert nicht??
			this.mainBot.sendMessage(target, "Fehler bei Wetter-Abfrage. Existiert dieser Ort wirklich?");
			return;
		}
		root = root.getChild("weather");
		//Element forecastInformation = root.getChild("forecast_information");
		//String nOrt = forecastInformation.getChild("postal_code").getAttributeValue("data");
		Element currentConditions = root.getChild("current_conditions");
		String bedingungen = currentConditions.getChild("condition").getAttributeValue("data");
		String temperatur = currentConditions.getChild("temp_c").getAttributeValue("data");
		String feuchtigkeit = currentConditions.getChild("humidity").getAttributeValue("data");
		String wind = currentConditions.getChild("wind_condition").getAttributeValue("data");
		
		// Akt. Bedingungen senden
		StringBuilder sb = new StringBuilder("Aktuelles Wetter für ").append(Colors.BLUE);
		sb.append(ort).append(Colors.NORMAL).append(":");
		this.mainBot.sendMessage(target, sb.toString());
		sb = new StringBuilder();
		sb.append(bedingungen).append(" - ").append(temperatur).append(" °C - ").append(feuchtigkeit)
			.append(" - ").append(wind);
		this.mainBot.sendMessage(target, sb.toString());
		
	}

	private Document getXMLDocument(URLConnection con){
		SAXBuilder saxB = new SAXBuilder();
		Document doc;
		try {
			InputStream is = con.getInputStream();
			Reader reader = new InputStreamReader(is, "UTF-8");
			InputSource ips = new InputSource(reader);
			ips.setEncoding("UTF-8");			
			doc = saxB.build(ips);
		} catch (Exception e) {
			doc = null;
			e.printStackTrace();
		}
		return doc;
	}

	@Override
	public void updateOnPrivateMessage(String sender, String login,
			String hostname, String message) {
		if(message.startsWith(BOT_CMD)){
			// Incoming Weather-Request
			this.onIncomingWeatherRequest(null, sender, login, hostname, message);
		}
	}

	@Override
	public void updateOnConnect() {
		// Do Nothing
	}

	@Override
	public void updateOnDisconnect() {
		// Do Nothing
	}

	@Override
	public void updateOnJoin(String channel, String sender, String login,
			String hostname) {
		// Do nothing
	}

	@Override
	public String helpGetFunction() {
		return BOT_NAME;
	}

	@Override
	public HashMap<String, String> helpGetCommandos() {
		HashMap<String, String> cmds = new HashMap<String, String>();
		cmds.put(HELP_SYNTAX, HELP_DESCRIPTION);
		return cmds;
	}

	@Override
	public HashMap<String, String> helpGetPrivateCommandos() {
		return this.helpGetCommandos();
	}

}
