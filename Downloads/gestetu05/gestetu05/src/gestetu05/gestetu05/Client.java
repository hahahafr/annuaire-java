/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestetu05;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jdk.internal.org.xml.sax.SAXException;
import static jdk.nashorn.tools.ShellFunctions.input;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import static org.jdom2.filter.Filters.document;
import org.jdom2.input.SAXBuilder;


/**
 *
 * @author ALEX-MOMO
 */
public class Client {

    public Client() {
    System.out.println("Création d'une instance Client !");
    }   
    
    static void LireXML(String NomFichier){
    Element racine = new Element("repertoire") ;
    Document document = new Document(racine);

    //On crée une instance de SAXBuilder
      SAXBuilder sxb = new SAXBuilder();
      try
      {
         //On crée un nouveau document JDOM avec en argument le fichier XML
         //Le parsing est terminé ;)
         document = sxb.build(new File(NomFichier));
      }
      catch(JDOMException | IOException e){}

      //On initialise un nouvel élément racine avec l'élément racine du document.
      racine = document.getRootElement();
   //On crée une List contenant tous les noeuds "utilisateur" de l'Element racine
   List listUtilisateurs = racine.getChildren("utilisateur");
   //On crée un Iterator sur notre liste
   Iterator i = listUtilisateurs.iterator();
   System.out.println("--------------------Fil d'actualité--------------------");
   while(i.hasNext())
   {
      //On recrée l'Element courant à chaque tour de boucle afin de
      //pouvoir utiliser les méthodes propres aux Element comme :
      //sélectionner un nœud fils, modifier du texte, etc...
      Element courant = (Element)i.next();
      //On affiche le nom de l’élément courant
      System.out.println("L'utilisateur "+courant.getChild("nom").getText());
      System.out.println(" est "+courant.getChild("Profession").getText());         
      
   }
}
    static void ChercherInformation(String NomFichier, String recherche){
    Element racine = new Element("repertoire") ;
    Document document = new Document(racine);
    //On crée une instance de SAXBuilder
      SAXBuilder sxb = new SAXBuilder();
      try
      {
         //On crée un nouveau document JDOM avec en argument le fichier XML
         //Le parsing est terminé ;)
         document = sxb.build(new File(NomFichier));
      }
      catch(JDOMException | IOException e){}

      //On initialise un nouvel élément racine avec l'élément racine du document.
      racine = document.getRootElement();
   //On crée une List contenant tous les noeuds "utilisateur" de l'Element racine
   List listUtilisateurs = racine.getChildren("utilisateur");
   //On crée un Iterator sur notre liste
   Iterator i = listUtilisateurs.iterator(); 
   System.out.println("Résultat de la recherche:\n");
   while(i.hasNext())
   {
      //On recrée l'Element courant à chaque tour de boucle afin de
      //pouvoir utiliser les méthodes propres aux Element comme :
      //sélectionner un nœud fils, modifier du texte, etc...
      Element courant = (Element)i.next();
      //On affiche le nom de l’élément courant
      if (courant.getChild("nom").getText().equals(recherche) || courant.getChild("Profession").getText().equals(recherche)){
          System.out.println("Nom:"+courant.getChild("nom").getText());
          System.out.println("Profession:"+courant.getChild("Profession").getText()+"\n");
      }    
      
   }
}

        public static Document readFromString(String xmlString) throws JDOMException, IOException
    {
	    SAXBuilder builder = new SAXBuilder();
	    return builder.build(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));
    }
        public static String Menu() throws IOException{
            String userInput;
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            do{               

        System.out.println("############ Veuillez faire votre choix : ######################");
        System.out.println("################################################################ \n");
        System.out.println("############ --> Connexion : tapez 1 ###########################");
        System.out.println("############ --> Inscription : tapez 2 ##########################");
        System.out.println("############ --> Modification : tapez 3 ##########################");
        System.out.println("############ --> Rechercher : tapez 4 ##########################");
        System.out.println("############ --> Quitter : tapez 5 ##########################");
        
        if ((userInput = stdIn.readLine()) != null && !userInput.equalsIgnoreCase("exit")) {
				System.out.println("Votre choix : " + userInput);
        }
        }while(!"1".equals(userInput) && !"2".equals(userInput)&& !"3".equals(userInput)&& !"4".equals(userInput)&& !"5".equals(userInput));
            return userInput;
        }
    public static void main(String[] args) throws IOException, JDOMException {
        String choix1 = "1";
        
        GestionProto GP = new GestionProto();
        String userInput;
        String srvRep;
        String xmlOut;
        GestionnaireUtilisateur monGU = null;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        
///////////////////////Création du socket client////////////////////////////////////////
        
       			Socket sockClient = new Socket("127.0.0.1", 57000);			
			PrintWriter outToServer = new PrintWriter(sockClient.getOutputStream(), true);
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sockClient.getInputStream()));
/////////////////////////////////////////////////////////////////////////////////////////
        System.out.println("################################################################");
        System.out.println("################################################################");
        System.out.println("############ BIENVENUE DANS NOTRE PROJET JAVA ##################");
        System.out.println("################################################################");     
userInput = Menu();
     do{
     System.out.println("TEST:" + userInput);
	if("1".equals(userInput)){
            
            String utilisateur;
            String mdp;
           
        System.out.println("Connexion: \n");
            System.out.println("Entrer vos informations: \n"); 
            do{
            do{
                System.out.println("Nom d'utilisateur: \n");
                
                utilisateur = stdIn.readLine();
                if("".equals(utilisateur)){
                    System.out.println("Mauvaise entrée, recommencez!");
                } 
            }while("".equals(utilisateur));
            
            do{
                System.out.println("Mot de passe: \n");
            
                mdp = stdIn.readLine();
                if("".equals(mdp)){
                    System.out.println("Mauvaise entrée, recommencez!");
                } 
            }while("".equals(mdp));
            
            
            
            xmlOut = GP.GenererMess("requête", "Connexion",utilisateur ,mdp, "Profession","??");			        
            outToServer.println(xmlOut + Character.toString((char)4));                    
                                
              
            srvRep = inFromServer.readLine();
            System.out.println("srvRep: " + srvRep);       
                if (!srvRep.equals("a") ){
                    System.out.println("Vous êtes bien connecté!");
                    LireXML("Exercice.xml"); 
                    userInput = Menu();
                }
                else{
                    System.out.println("Nom d'utilisateur ou mot depasse erroné, recommencez!"); 
                }
        }while("a".equals(srvRep)) ;       
                    
            
                
        }  	
        else if ("2".equals(userInput)){
            String utilisateur;
            String mdp;
            String prof;
            System.out.println("Inscription: \n"); 
            System.out.println("Entrer vos informations: \n"); 
            
            do{
            do{
                System.out.println("Nom d'utilisateur: \n");
                
                utilisateur = stdIn.readLine();
                if("".equals(utilisateur)){
                    System.out.println("Mauvaise entrée, recommencez!");
                } 
            }while("".equals(utilisateur));
            
            do{
                System.out.println("Mot de passe: \n");
            
                mdp = stdIn.readLine();
                if("".equals(mdp)){
                    System.out.println("Mauvaise entrée, recommencez!");
                } 
            }while("".equals(mdp));
            
            do{
                System.out.println("Profession: \n");
            
                prof = stdIn.readLine();
                if("".equals(prof)){
                    System.out.println("Mauvaise entrée, recommencez!");
                } 
            }while("".equals(prof));
                   
            
            xmlOut = GP.GenererMess("requête", "ajoutUtilisateur",utilisateur ,mdp, prof, "??");
             
            outToServer.println(xmlOut + Character.toString((char)4)); 
            
            srvRep = inFromServer.readLine();
            System.out.println("srvRep: " + srvRep);       
                if (!srvRep.equals("a")  ){
                    System.out.println("Vous êtes bien inscrit!");
                    userInput=Menu();                  
                }
                else{
                    System.out.println("Utilisateur déja présent, Recommencez!"); 
                }
        }while("a".equals(srvRep)) ;
                                
            while ((srvRep = inFromServer.readLine()).equals("</dasProtokol>") != true)
                System.out.println("echo: " + srvRep);        
                

        } else if("3".equals(userInput)){
            
            String utilisateur;
            String mdp;
           
        System.out.println("Modification: \n");
            System.out.println("Entrer vos informations: \n"); 
            do{
            do{
                System.out.println("Nom d'utilisateur: \n");
                
                utilisateur = stdIn.readLine();
                if("".equals(utilisateur)){
                    System.out.println("Mauvaise entrée, recommencez!");
                } 
            }while("".equals(utilisateur));
            
            do{
                System.out.println("Mot de passe: \n");
            
                mdp = stdIn.readLine();
                if("".equals(mdp)){
                    System.out.println("Mauvaise entrée, recommencez!");
                } 
            }while("".equals(mdp));
            
            
            
            xmlOut = GP.GenererMess("requête", "Modification",utilisateur ,mdp, "Profession","??");			        
            outToServer.println(xmlOut + Character.toString((char)4));                    
                                
              
            srvRep = inFromServer.readLine();
            System.out.println("srvRep: " + srvRep);       
                if (!srvRep.equals("a") ){
                    System.out.println("Vous avez été trouvé!");
                    
            String uti;
            String modepa;
            String profe;
            System.out.println("Modification: \n"); 
            System.out.println("Entrer vos nouvelles informations: \n"); 
            do{
                System.out.println("Nom d'utilisateur: \n");
                
                uti = stdIn.readLine();
                if("".equals(uti)){
                    System.out.println("Mauvaise entrée, recommencez!");
                } 
            }while("".equals(uti));
            
            do{
                System.out.println("Mot de passe: \n");
            
                modepa = stdIn.readLine();
                if("".equals(modepa)){
                    System.out.println("Mauvaise entrée, recommencez!");
                } 
            }while("".equals(modepa));
            
            do{
                System.out.println("Profession: \n");
            
                profe = stdIn.readLine();
                if("".equals(profe)){
                    System.out.println("Mauvaise entrée, recommencez!");
                } 
            }while("".equals(profe));
                   
            
            xmlOut = GP.GenererMess("requête", "ajoutUtilisateur",uti ,modepa, profe, "??");
            
            userInput = Menu();
            outToServer.println(xmlOut + Character.toString((char)4));                    
                                
            while ((srvRep = inFromServer.readLine()).equals("</dasProtokol>") != true)
                System.out.println("echo: " + srvRep);        
                
                                       
                }
                else{
                    System.out.println("Nom d'utilisateur ou mot depasse erroné, recommencez!"); 
                }
        }while("a".equals(srvRep)) ;       
                    
        }
        else if("4".equals(userInput)){
            
            System.out.println("Rechercher:");
            String recherche;
            recherche = stdIn.readLine();
            ChercherInformation("Exercice.xml",recherche);
            userInput = Menu();
            
            
        
        }
        
     }while(!userInput.equals("5")); 
     sockClient.close();
}
}


