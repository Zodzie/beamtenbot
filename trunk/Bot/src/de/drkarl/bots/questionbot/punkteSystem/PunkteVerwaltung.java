package de.drkarl.bots.questionbot.punkteSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;


public class PunkteVerwaltung {

	LinkedList<User> userList;
	
	public PunkteVerwaltung() {
		super();
		userList = new LinkedList<User>();
		initParse();
	}

	public LinkedList<User> getUserList() {
		return userList;
	}

	public void setUserList(LinkedList<User> userList) {
		this.userList = userList;
	}
	
	public String toXML(){
		StringBuilder sb = new StringBuilder();
		for (User user : userList) {
			sb.append(user.toXML()+"\n");
		}
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<punkteVerwaltung>\n"+sb.toString()+"</punkteVerwaltung>";
	}
	
	 public void writeToXML(){
		 try {
			 BufferedWriter out = new BufferedWriter(de.hof.mainbot.helpers.FileIO.getFileWriter(("PunkteVerwaltung.xml")));
			 	//BufferedWriter out = new BufferedWriter(new FileWriter(new File(this.getClass().getClassLoader().getResource("PunkteVerwaltung.xml").getFile())));
				out.write(this.toXML());
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	 
	 public void initParse(){
		 	this.userList = new LinkedList<User>();
		 	try{
		 		InputSource in = new InputSource(de.hof.mainbot.helpers.FileIO.getFileInputStream(("PunkteVerwaltung.xml")));
		 	//InputSource in = new InputSource(this.getClass().getClassLoader().getResourceAsStream("PunkteVerwaltung.xml"));
			Document doc = new SAXBuilder().build(in);
			Element root = doc.getRootElement();
			List<Element> elementList = root.getChildren("user");
			for (Element element : elementList) {
				User user = new User(element.getChildTextTrim("name"));
				Punkte punkte = new Punkte(Integer.parseInt(element.getChildTextTrim("punkte")));
				user.setPunkte(punkte);
				this.getUserList().addLast(user);
			}
		 	}catch(Exception e){
		 		System.out.println(e);
		 	}
	 }

	public void rewardUser(String sender,int punkte){
		//falls user vorhanden rewarde ihn
		boolean flag = false;
		for (User user : this.userList) {
			if(user.getName().equalsIgnoreCase(sender)){
				user.getPunkte().setPunkte(user.getPunkte().getPunkte()+punkte);
				flag= true;
			}
		}
		//lege neuen User an und rewarde ihn
		if(flag == false){
			createNewUser(sender);
		}
		this.writeToXML();
	}

	private void createNewUser(String sender) {
		User user = new User(sender);
		Punkte punkte = new Punkte(1);
		user.setPunkte(punkte);
		this.getUserList().addLast(user);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (User user : getUserList()) {
			sb.append(user.toString()+"\n");
		}
		return sb.toString();
	}
	
}

