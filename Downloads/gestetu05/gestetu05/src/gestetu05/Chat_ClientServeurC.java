package gestetu05;
import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Chat_ClientServeurC implements Runnable {

	private Socket socket;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private Scanner sc;
	private Thread t3, t4;
        String login;
	
	public Chat_ClientServeurC(Socket s, String log){
		socket = s;
                login = log;
	}
	
	public void run() {
		try {
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			sc = new Scanner(System.in);
			
			Thread t4 = new Thread(new EmissionC(out,login));
			t4.start();
			Thread t3 = new Thread(new ReceptionC((in),login));
			t3.start();
		
		   
		    
		} catch (IOException e) {
			System.err.println("Le serveur distant s'est déconnecté !");
		}
	}

}
