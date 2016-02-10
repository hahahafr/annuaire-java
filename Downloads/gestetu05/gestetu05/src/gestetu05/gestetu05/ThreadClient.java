package gestetu05;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ThreadClient extends Thread {
	
	private Socket socketDonnees;
	private GestionProto notreGestionProto;
	
	public ThreadClient(Socket sock, GestionProto gest) {
		socketDonnees = sock;
		notreGestionProto = gest;
	}
	
	public void run() {
		
		try {
	
			PrintWriter outToClient = new PrintWriter(socketDonnees.getOutputStream(), true);
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socketDonnees.getInputStream()));
			
			String clientInput;
			String dataSent;
			String buf = "";
			
			while ((clientInput = inFromClient.readLine()) != null) {
				
				if (clientInput.endsWith("</dasProtokol>" + Character.toString((char)4)))
				{
					clientInput = (String) clientInput.subSequence(0, clientInput.length() - 1);
					buf = buf + clientInput + "\n";
					System.out.println("got:\n" + buf);
					dataSent = notreGestionProto.traitementReq(buf);
					outToClient.println(dataSent);
					System.out.println("sent:\n" + dataSent);
					buf = "";
				} 
				else if (clientInput.endsWith(Character.toString((char)4)))
				{
					System.out.println("got:\n" + buf);
					dataSent = notreGestionProto.traitementReq(buf);
					outToClient.println(dataSent);
					System.out.println("sent:\n" + dataSent);
					buf = "";
				}
				else
				{
					buf = buf + clientInput + "\n";
				}
									
			}
			socketDonnees.close();
			System.out.println("user disc: " + socketDonnees);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
