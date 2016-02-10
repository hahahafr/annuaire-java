package gestetu05;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static jdk.nashorn.tools.ShellFunctions.input;

public class ClientConnecte {


	public static void main(String[] args) throws IOException {

        GestionProto GP = new GestionProto();
		 String userInput;
        System.out.println("################################################################");
        System.out.println("################################################################");
        System.out.println("############ BIENVENUE DANS NOTRE PROJET JAVA ##################");
        System.out.println("################################################################");
        System.out.println("############ Veuillez faire votre choix : ######################");
        System.out.println("################################################################ \n");
        System.out.println("############ --> Connexion : tapez 1 ###########################");
        System.out.println("############ --> Inscription : tapez 2 ##########################");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        
        try {			
			Socket sockClient = new Socket("127.0.0.1", 57000);			
			PrintWriter outToServer = new PrintWriter(sockClient.getOutputStream(), true);
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sockClient.getInputStream()));
                        
        while ((userInput = stdIn.readLine()) != null && !userInput.equalsIgnoreCase("exit")) {
				System.out.println("input: " + userInput);
        }
	if(             "1".equals(userInput)){
            System.out.println("on rentre bien dans le 1");
        } else {
           System.out.println("on rentre bien dans le 2"); 
        }
			
			
			
			
			String srvRep;
			String xmlOut;
			while ((userInput = stdIn.readLine()) != null && !userInput.equalsIgnoreCase("exit")) {
				System.out.println("input: " + userInput);
				
                                
                                xmlOut = GP.GenererMess("requête", userInput, "??");
			        
                                outToServer.println(xmlOut + Character.toString((char)4));
			    
                                
                                
                                while ((srvRep = inFromServer.readLine()).equals("</dasProtokol>") != true)
			    	System.out.println("echo: " + srvRep);
		    	System.out.println("echo: " + srvRep);
			    
			}
			
			sockClient.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
