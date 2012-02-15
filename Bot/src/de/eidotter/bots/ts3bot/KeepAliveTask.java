package de.eidotter.bots.ts3bot;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class KeepAliveTask implements Runnable {
	JTS3ServerQuery ts3query;
	int period;
	boolean debug;
	
	/**
	 * Schickt alle period Sekunden eine Anfrage an den TS3, um die Verbindung aufrecht zu erhalten.
	 * @param ts3query	JTS3ServerQuery
	 * @param period	Intervall in Sekunden
	 * @param tS_DEBUG 	Debugging-Meldungen
	 */
	public KeepAliveTask(JTS3ServerQuery ts3query, int period, boolean tS_DEBUG){
		this.ts3query = ts3query;
		this.period = period;	
		this.debug = tS_DEBUG;
	}

	@Override
	public void run() {
		while(true){
			ts3query.doCommand("version");
			if(debug)	System.out.println("Keep-Alive Anfrage an TS3 gesendet. Intervall beträgt " + period + " Sekunden.");
			try {
				Thread.sleep(this.period * 1000);
			} catch (InterruptedException e) {
				if(debug)	System.out.println("Sleep wurde interrupted!");
				e.printStackTrace();
			}
		}

	}

}
