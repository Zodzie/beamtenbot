package de.hof.mainbot.helpers;

import java.util.Date;

/**
 * Hilfsklasse zum Loggen von Meldungen und Fehlern.
 * @author Michael
 *
 */
public class Logger {

	public static void logError(String prefix, String message){
		System.err.println(new Date().getTime() + " - " + prefix + " : " + message);
	}
	
	public static void logVerbose(String prefix, String message){
		System.out.println(new Date().getTime() + " - " + prefix + " : " + message);
	}

}
