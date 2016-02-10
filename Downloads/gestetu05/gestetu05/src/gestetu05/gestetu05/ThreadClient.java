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
			
			/* On lit ligne par ligne les donnees envoyes par le client
			 * 
			 * Tant que le bloc de donnees n'est pas termine par
			 * </dasProtokol>\4 ou \4 (EOT)
			 * on continue à lire les donnees
			 * 
			 * Sinon on traite la requete et on renvoie le resultat
			 */ 
			while ((clientInput = inFromClient.readLine()) != null) {
				
				// la requete est entierement sur une ligne
				if (clientInput.endsWith("</dasProtokol>" + Character.toString((char)4)))
				{
					// on enleve le caractere \4 (EOT) de la chaine recue
					clientInput = (String) clientInput.subSequence(0, clientInput.length() - 1);
					
					// on rajoute la derniere ligne dans le buffer et on traite la requete
					buf = buf + clientInput + "\n";
					System.out.println("got (1 ligne):");
					System.out.println("--- debut buffer ---");
					System.out.println(buf);
					System.out.println("--- fin buffer ---");
					dataSent = notreGestionProto.traitementReq(buf);
					outToClient.println(dataSent);
					System.out.println("sent:");
					System.out.println("--- debut dataSent ---");
					System.out.println(dataSent);
					System.out.println("--- fin dataSent ---");
					buf = "";
				} 
				else if (clientInput.endsWith(Character.toString((char)4)))
				{
					// on ne rajoute pas la derniere ligne et on traite directement la requete
					System.out.println("got (plus de 1 ligne):");
					System.out.println("--- debut buffer ---");
					System.out.println(buf);
					System.out.println("--- fin buffer ---");
					dataSent = notreGestionProto.traitementReq(buf);
					outToClient.println(dataSent);
					System.out.println("sent:");
					System.out.println("--- debut dataSent ---");
					System.out.println(dataSent);
					System.out.println("--- fin dataSent ---");
					buf = "";
				}
				else
				{
					// on ajoute la ligne recue a la requete
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
