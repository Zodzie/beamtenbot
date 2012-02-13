package de.drkarl.questionAPI.fragenKatalog;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;
import org.xml.sax.InputSource;

public class FragenKatalog {
	
	
	
	public FragenKatalog() {
		super();
		this.initParse();
	}


	//Liste mit allen Fragen
	public LinkedList<Frage> fragen = new LinkedList<Frage>();

	public LinkedList<Frage> getFragen() {
		return fragen;
	}
	
	public void addFrage(Frage frage){
		fragen.addLast(frage);
	}

	public void setFragen(LinkedList<Frage> fragen) {
		this.fragen = fragen;
	}
	 public String toXML(){
		 
		 StringBuffer sb = new StringBuffer();
		 for (Frage frage : fragen) {
			sb.append(frage.toXML());
		}
		 return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<fragenKatalog>\n"+sb.toString()+"</fragenKatalog>";
	 }
	 
	 /**
	  * Schreibt die aktuelle Instanz in die this.xml
	  */
	 public void writeToXML(){
		 try {
				BufferedWriter out = new BufferedWriter(new FileWriter("FragenKatalog.xml"));
				out.write(this.toXML());
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	 
	 /**
	  * befüllt die Instanz der Klasse aus this.xml
	  */
	 public void initParse(){
		 try {
			 	this.fragen = new LinkedList<Frage>();
				InputSource in = new InputSource(new FileInputStream("FragenKatalog.xml"));
				Document doc = new SAXBuilder().build(in);
				Element root = doc.getRootElement();
				List<Element> elementList = root.getChildren("question");
				
				for (Element question : elementList) {
					//erstelle LinkedList mit antworten
					List<Element> antwortenList = question.getChildren("antwort");
					LinkedList<Antwort> antworten = new LinkedList<Antwort>();
					for(Element antwort: antwortenList){
						antworten.addLast(new Antwort(antwort.getTextTrim()));
					}
					//erstelle Frage
					Frage frage = new Frage(question.getChildTextTrim("frage"), antworten);
					//hänge Frage in aktuellen this
					this.addFrage(frage);
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	 }
	 
	 public void addQuestion(String message,String sender,PircBot myBot) {
			//überprüfen des message Pattern
			Pattern p = Pattern.compile("addQuestion \"[\\wäÄöÖüÜß\\\\?\\s\\\\.\\\\-\\\\*\\\\']+\" \"[\\wäÄöÖüÜß\\\\?\\s\\\\.\\\\-\\\\*\\\\']+\"");
			Matcher m = p.matcher(message);
			boolean meetRegEx = m.matches();
			if(meetRegEx){
				//String der message aufteilen und in richtige Objekte packen
				String[] st = message.split("\"");
				Frage frage = null;
				LinkedList<Antwort> antworten= new LinkedList<Antwort>();
				int counter = 0;
				for (String string : st) {
					if(counter==1){
						frage = new Frage(string,antworten);
					}else if (counter > 1 && counter%2 != 0){
						antworten.addLast(new Antwort(string));
					}
					counter++;
				}
				//überprüfen ob frage schon im Katalog --> wenn ja nur Antwoten anfügen
				int flag = 0;
				for (Frage frageOutOfAll : this.getFragen()) {
					if(frageOutOfAll.getFrage().equalsIgnoreCase(frage.getFrage())){
						for (Antwort antwort : antworten) {
							frageOutOfAll.getAntworten().addLast(antwort);
						}
						flag = 1;
					}
				}
				if(flag == 0){//diese frage ist noch nicht im Katalog
				this.addFrage(frage);
				}
				this.writeToXML();
				myBot.sendMessage(sender, Colors.GREEN +  "Frage Hinzugefügt");
				System.out.println(this.toXML());
			}else{
				myBot.sendMessage(sender, Colors.BOLD + Colors.RED +  "Falsche Syntax --> addQuestion \"xx\" \"yy\"");
			}
		}
	 	
	 	
		public static void main(String[] args) {
			FragenKatalog f = new FragenKatalog();
			System.out.println(f.toXML());
			
		}
}
