package gestetu05;

import java.io.*;
import java.net.*;


public class Accepter_connexion implements Runnable{

	private ServerSocket socketserver = null;
	private Socket socket = null;
        private Socket socket2 = null;
        String login;

	public Thread t2;
	public Accepter_connexion(ServerSocket ss, String log){
	 socketserver = ss;
         login=log;
         
	}
	
	public void run() {
		
		try {
			while(true){
				
			socket = socketserver.accept();
			System.out.println("Un Utilisateur veut se connecter  ");
                        System.out.println("voila:"+socket.getPort());
			
			t2 = new Thread(new Chat_ClientServeur(socket,login));
			t2.start();
                        
                      /*  Thread t1;
             try {                
		System.out.println("Demande de connexion");
		socket2 = new Socket("127.0.0.1",50004);
		System.out.println("Le lien a été établi"); // Si le message s'affiche c'est que je suis connecté
                t1 = new Thread(new Chat_ClientServeurC(socket2));
		t1.start();
             }catch (IOException e) {
            System.err.println("Aucun serveur à l'écoute du port "+socket2.getLocalPort());
            }*/
			
			}
		} catch (IOException e) {
			
			System.err.println("Erreur serveur");
		}
		
	}
}
