package de.hof.mainbot.helpers;

import java.io.IOException;
import java.util.Properties;

/**
 * Klasse zum Einlesen und Abfragen von .properties-Dateien.
 * @author Michael
 * @version 0.1
 */
public class BotProperties {
	private String fileName;
	private Properties properties;
	
	public BotProperties(String fileName){
		this.fileName = fileName;
		this.properties = new Properties();
		this.loadProperties();
	}

	private void loadProperties() {
		try {
			this.properties.load(FileIO.getFileInputStream(this.fileName));
		} catch (IOException e) {
			System.out.println("IO-Exception beim Lesen der Datei " + this.fileName + " (Properties).");
			e.printStackTrace();
		}
	}
	
	/**
	 * Gibt einen Boolean-Value zurück.
	 * @param key	Key
	 * @return	Boolean value oder <code>null</code>
	 */
	public Boolean getBoolean(String key){
		String property = this.properties.getProperty(key);
		if(property != null){
			Boolean b = Boolean.parseBoolean(property);
			return b;
		} else {
			return null;
		}
	}
	
	/**
	 * Gibt einen Boolean-Value zurück.
	 * @param key	Key
	 * @param defaultValue	Standard-Value
	 * @return	Boolean Value
	 */
	public Boolean getBoolean(String key, Boolean defaultValue){
		Boolean b = this.getBoolean(key);
		if(b != null){
			return b;
		} else {
			return defaultValue;
		}
	}
	
	/**
	 * Gibt den Value zum angegeben Key zurück.
	 * @param key	Key
	 * @return	String Value oder <code>null</code>
	 */
	public String getString(String key){
		return this.properties.getProperty(key);
	}
	
	/**
	 * Gibt den Value zum angegebenen Key zurück.
	 * @param key	Key
	 * @param defaultValue	Standard-Value
	 * @return	String Value
	 */
	public String getString(String key, String defaultValue){
		return this.properties.getProperty(key, defaultValue);
	}
	
	/**
	 * Gibt eine Liste mit Values zurück, die in der Properties-Datei
	 * mit ";" getrennt wurden.
	 * @param key	Key
	 * @return	String[] Values
	 */
	public String[] getStringList(String key){
		String props = this.properties.getProperty(key);
		return props.split(";");
	}

	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Liefert eine Referenz auf das innere Properties-Objekt.
	 * @return	Properties
	 */
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * Liefert einen Integer für den angegebenen Key.
	 * @param key	Key
	 * @return	Integer oder <code>null</code>
	 */
	public Integer getInteger(String key){
		String strValue = this.getString(key);
		Integer i = null;
		try{
			i = Integer.parseInt(strValue);
			return i;
		} catch (NumberFormatException e){
			System.out.println("NumberFormatException beim Parsen des Strings " + strValue + " zu einem Integer. (Properties)");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Liefert einen Integer für den angegebenen Key.
	 * @param key	Key
	 * @param defaultValue	Standard-Value
	 * @return	Integer
	 */
	public Integer getInteger(String key, int defaultValue){
		Integer i = this.getInteger(key);
		if( i == null){
			return defaultValue;
		} else {
			return i;
		}
	}
	
	
}
