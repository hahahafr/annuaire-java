/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestetu05;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author ALEX-MOMO
 */
public class ProjetJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String Fichier ="Exercice.xml";
        List<String> ListeAdmin = new ArrayList<>() ;
        GestionnaireUtilisateur GU = new GestionnaireUtilisateur();
        GestionProto GP = new GestionProto(GU);
        
        try{
            BufferedReader In = new BufferedReader(new FileReader(Fichier));
            //si le fichier existe...
                //GU.LireXML("Exercice.xml");
                System.out.println(GU.ListeXML);
                GU.EcrireFichierXML(GU.LireXML("Exercice.xml"));
                System.out.println(GU.ListeXML);
                GU.enregistreXML("Exercice.xml");
                System.out.println("Fichier trouvé!");
            
            } 
         catch (FileNotFoundException fnfe) {
           //si le fichier n'existe pas ...     
                ListeAdmin.add("Admin");
                ListeAdmin.add("non");
                ListeAdmin.add("12345678");
                ListeAdmin.add("Informaticien");
                GU.EcrireFichierXML(ListeAdmin);
                GU.enregistreXML("Exercice.xml");
                System.out.println("Création d'un fichier!");
            }
              
        // TODO code application logic here
        GU.ListeXML.clear();
        // On ecoute sur le socket TCP serveur
 		try (ServerSocket raspberryPi = new ServerSocket())
 		{
                     
 			InetAddress adresseEcoute = InetAddress.getByName("127.0.0.1");
 			InetSocketAddress socketEcoute = new InetSocketAddress(adresseEcoute, 2009);
 			raspberryPi.bind(socketEcoute);
 			System.out.println("TCP server listening: " + raspberryPi);
 			
 			Socket socketDonnees;
 			
 			while(true) {
 				socketDonnees = raspberryPi.accept();
 				System.out.println("user connected: " + socketDonnees);
 				// On lance le thread TCP pour ce client
 				ThreadClient leThread = new ThreadClient(socketDonnees, GP);
 				leThread.start();
 			}
 		} catch (IOException e) {
 			System.err.println("Problème avec le socket.");
 			e.printStackTrace();
 		}
        
    }
    
}
