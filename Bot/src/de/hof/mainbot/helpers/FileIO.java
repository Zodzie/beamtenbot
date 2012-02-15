package de.hof.mainbot.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;


/**
 * Hilfsklasse zum Einlesen von Dateien innerhalb und au�erhalb der JAR.
 * Zuerst wird immer versucht die Datei au�erhalb der JAR zu �ffnen, ist dies nicht erfolgreich, wird versucht
 * die Datei innerhalb der JAR zu �ffnen.
 * @author Michael
 * @version 0.1
 *
 */
public class FileIO {
	private static FileIO instance = new FileIO();
	
	/**
	 * Gibt ein File-Objekt f�r die angegebene Datei zur�ck. Es wird zuerst au�erhalb der JAR gesucht, danach
	 * wird intern nach der Datei gesucht. Gibt null zur�ck, wenn Datei nicht gefunden wurde.
	 * @param filename	Dateiname
	 * @return	File oder null
	 */
	public static File getFile(String filename){
		File f = null;
		// Au�erhalb �ffnen
		URL resourceURL = instance.getClass().getClassLoader().getResource(filename);
		if(resourceURL != null){
			String path = resourceURL.getFile();
			if(path != null){
				f = new File(path);
			}
		} else {
			// Versuchen innerhalb zu �ffnen
			f = new File(filename);
		}
		return f;		
	}
	
	/**
	 * Gibt einen FileInputStream f�r die angegebene Datei zur�ck. Es wird zuerst au�erhalb der JAR gesucht, danach
	 * wird intern nach der Datei gesucht. Gibt null zur�ck, wenn Datei nicht gefunden wurde.
	 * @param filename	Dateiname
	 * @return	File oder null
	 */
	public static FileInputStream getFileInputStream(String filename){
		FileInputStream fis = null;
		File f = getFile(filename);
		if(f != null){
			try {
				fis = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				fis = null;
			}
		}
		return fis;
	}
	
	/**
	 * Gibt einen FileWriter f�r die angegebene Datei zur�ck. Es wird zuerst au�erhalb der JAR gesucht, danach
	 * wird intern nach der Datei gesucht. Gibt null zur�ck, wenn die Datei nicht gefunden wurde.
	 * @param filename Dateiname
	 * @return FileWriter oder null
	 */
	public static FileWriter getFileWriter(String filename){
		FileWriter fw = null;
		File f = getFile(filename);
		if(f != null){
			if(f.exists()){
				try {
					fw = new FileWriter(f);
				} catch (IOException e) {
					fw = null;
				}
			}
		}
		return fw;
	}

}
