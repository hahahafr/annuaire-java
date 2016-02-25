package gestetu05;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

class ThreadEcouteurMI extends Thread {
	
	private String monAddrEcoute;
	private int monNumPortEcoute;
	
	public ThreadEcouteurMI(String adresseEcoute, int numPortEcoute) {
		monAddrEcoute = adresseEcoute;
		monNumPortEcoute = numPortEcoute;
	}
	
	public void run() {
		try (ServerSocket raspberryPi = new ServerSocket())
 		{
 			
 			InetAddress adresseEcoute = InetAddress.getByName(monAddrEcoute);
 			InetSocketAddress socketEcoute = new InetSocketAddress(adresseEcoute, monNumPortEcoute);
 			raspberryPi.bind(socketEcoute);
 			System.out.println("TCP server listening: " + raspberryPi);
 			
 			Socket socketDonnees;
 			
 			while(true) {
 				socketDonnees = raspberryPi.accept();
 				System.out.println("user connected: " + socketDonnees);
 				// On lance le thread TCP pour ce client
 				ThreadMessInst leThread = new ThreadMessInst(socketDonnees);
 				leThread.start();
 			}
 		} catch (IOException e) {
 			System.err.println("Problème avec le socket.");
 			e.printStackTrace();
 		}
	}

}
