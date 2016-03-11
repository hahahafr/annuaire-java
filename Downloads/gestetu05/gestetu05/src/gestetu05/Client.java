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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javafx.scene.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jdk.internal.org.xml.sax.SAXException;
import static jdk.nashorn.tools.ShellFunctions.input;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import static org.jdom2.filter.Filters.document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


/**
 *
 * @author ALEX-MOMO
 */
public class Client {
	
	private static String userConnecte = null;

        static List<String> LireMail(String NomFichier){
    Element racine = new Element("mail") ;
    Document document = new Document(racine);
    List<String> Liste = new ArrayList<>() ; ;
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
   List listMail = racine.getChildren("liste");
   //On crée un Iterator sur notre liste
   Iterator i = listMail.iterator(); 
   System.out.println("mail::");
   while(i.hasNext())
   {
       
      //On recrée l'Element courant à chaque tour de boucle afin de
      //pouvoir utiliser les méthodes propres aux Element comme :
      //sélectionner un nœud fils, modifier du texte, etc...
      Element courant = (Element)i.next();
      //On affiche le nom de l’élément courant
      System.out.println("mail::");
      Liste.add(courant.getChild("destinataire").getAttributeValue("emetteur"));
      Liste.add(courant.getChild("destinataire").getText());
      System.out.println("mail---"+courant.getChild("destinataire").getText());
      Liste.add(courant.getChild("message").getText());
      System.out.println("mail--"+courant.getChild("message").getText());
      
   }
   System.out.println("mail:::::"+ Liste);
   return Liste;
}

    public Client() {
    System.out.println("Création d'une instance Client !");
    }   
    static ServerSocket connexion(int port, String uti){
       ServerSocket ss = null;
       Thread t;
        try {
            ss = new ServerSocket(port);           
            System.out.println("Le serveur est à l'écoute du port "+ss.getLocalPort());
            t = new Thread(new Accepter_connexion(ss,uti));
            t.start();
        } catch (IOException e) {
            System.err.println("Le port "+ss.getLocalPort()+" est déjà utilisé !");
        }
        return ss;
    }
    static void Mess(String uti) throws IOException{
        String userInput;
        System.out.println("Envoyé message à un autre utilisateur, taper mess, \n"
                + "sinon n'importe quoi d'autre:");
        
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader stdIn2 = new BufferedReader(new InputStreamReader(System.in));
        String mail;
        
         if ((userInput = stdIn.readLine()) != null && !userInput.equalsIgnoreCase("exit")) {
				System.out.println("Votre choix : " + userInput);
        }
         if(userInput.equals("mess")){
             System.out.println("Son nom:");
              if ((userInput = stdIn2.readLine()) != null && !userInput.equalsIgnoreCase("exit")) {
				System.out.println("Utilisateur : " + userInput);
        }            
             int Num;
             Num =recupererNumUti("Exercice.xml",userInput);
             System.out.println(Num);
             Socket socket = null;
             Thread t1;
             try {                
		System.out.println("Demande de connexion");
		socket = new Socket("127.0.0.1",50000+Num);
		System.out.println("Le lien a été établi"); // Si le message s'affiche c'est que je suis connecté
                t1 = new Thread(new Chat_ClientServeurC(socket, uti));
		t1.start();
             }catch (IOException e) {
            System.err.println("Utilisateur non connecté");
            System.out.println("message pour le mail: ");
            if ((mail = stdIn2.readLine()) != null && !mail.equalsIgnoreCase("exit")) {
				
                    int i=0;
                    List<String> Liste;
                    Element racine = new Element("mail") ;           
                    Document document = new Document(racine);
                    try{
                        BufferedReader In = new BufferedReader(new FileReader("mail.xml"));
                    }
                    catch (FileNotFoundException fnfe) {
           //si le fichier n'existe pas ...  
           System.err.println("erreur!");
           XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
      //Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
      //avec en argument le nom du fichier pour effectuer la sérialisation.
      sortie.output(document,new FileOutputStream("mail.xml"));
                
                    }
                    Liste=LireMail("mail.xml");
                    System.out.println("listeMail: "+Liste);
                    System.out.println("userInput: "+userInput);
                    Liste.add(uti);
                    Liste.add(userInput);
                    Liste.add(mail);
                    int j = Liste.size();
                    
                    
                    while(i<j){
                        System.out.println("debut boucle, i=" + i + " ,j=" + j);
                        
                        Element liste = new Element("liste");
                        Attribute emetteur= new Attribute("emetteur",Liste.get(i));
                        
                        Element destinataire = new Element("destinataire");
                        Element message = new Element("message");
                        destinataire.setText(Liste.get(i+1));
                        destinataire.setAttribute(emetteur);
                        message.setText(Liste.get(i+2));
                        liste.addContent(destinataire);
                        liste.addContent(message);
                        racine.addContent(liste);
                                i=i+3;
                                System.out.println("fin boucle, i=" + i + " ,j=" + j);
                    }
                    
                    try
   {
      //On utilise ici un affichage classique avec getPrettyFormat()
      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
      //Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
      //avec en argument le nom du fichier pour effectuer la sérialisation.
      sortie.output(document, new FileOutputStream("mail.xml"));
   }
   catch (java.io.IOException l){}
            }
            }
             
        }
    }
    
    static int recupererNumUti(String NomFichier, String uti){
        Element racine = new Element("repertoire") ;
    Document document = new Document(racine);
    int np = 0;
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
   while(i.hasNext())
   {
      Element courant = (Element)i.next();
      if (courant.getChild("nom").getText().equals(uti)){        
          np = Integer.parseInt(courant.getAttribute("NuméroUtilisateur").getValue());
          System.out.println(np); 
          break;
      }
      else{
          System.out.println("Cet utilisateur n'existe pas");
          np = -1;
      }
    }
   return np;
    }
        
    
    static int LireXML(String NomFichier, String uti){
    Element racine = new Element("repertoire") ;
    Document document = new Document(racine);
    int np = 0;

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
      if (courant.getChild("nom").getText().equals(uti)){        
          np = Integer.parseInt(courant.getAttribute("NuméroUtilisateur").getValue());
          System.out.println(np); 
      }
      //On affiche le nom de l’élément courant
      if(courant.getChild("Visibilite").getText().equals("oui")){
      System.out.println("L'utilisateur "+courant.getChild("nom").getText());
      System.out.println(" est "+courant.getChild("Profession").getText()); 
      }
      
   }
   return np;
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
      if (courant.getChild("nom").getText().equals(recherche) && courant.getChild("Visibilite").getText().equals("oui") || courant.getChild("Profession").getText().equals(recherche)&& courant.getChild("Visibilite").getText().equals("oui")){
          System.out.println("Nom:"+courant.getChild("nom").getText());
          System.out.println("Profession:"+courant.getChild("Profession").getText()+"\n");
      }    
      
   }
}
    
    /*
     * Methode inspiree de ChercherInformation :
     * - On cherche l'utilisateur dans le fichier XML
     * - On affiche ses compétences
     * 
     * On retourne si l'utilisateur a été trouvé ou non
     */
    static Boolean afficherCompetences(String fichier, String utilisateur) {

        Element racine = new Element("repertoire") ;
        Document document = new Document(racine);
        //On crée une instance de SAXBuilder
          SAXBuilder sxb = new SAXBuilder();
          try
          {
             //On crée un nouveau document JDOM avec en argument le fichier XML
             //Le parsing est terminé ;)
             document = sxb.build(new File(fichier));
          }
          catch(JDOMException | IOException e){
        	  System.err.println(e);
        	  System.err.println("Fichier XML invalide");
          }

          //On initialise un nouvel élément racine avec l'élément racine du document.
          racine = document.getRootElement();
       //On crée une List contenant tous les noeuds "utilisateur" de l'Element racine
       List listUtilisateurs = racine.getChildren("utilisateur");
       //On crée un Iterator sur notre liste
       Iterator i = listUtilisateurs.iterator(); 
       Boolean userTrouve = false;
       while(i.hasNext()) {
    	      //On recrée l'Element courant à chaque tour de boucle afin de
    	      //pouvoir utiliser les méthodes propres aux Element comme :
    	      //sélectionner un nœud fils, modifier du texte, etc...
    	      Element courant = (Element)i.next();
    	      if (courant.getChild("nom").getText().equalsIgnoreCase(utilisateur)
    	    		  && courant.getChild("Visibilite").getText().equalsIgnoreCase("oui")) {
    	    	  userTrouve = true;
    	    	  //System.out.println(courant);
    	    	  System.out.println("Utilisateur :" + utilisateur);
    	    	  System.out.println("Competences : ");
    	    	  int cpt = 0;
    	    	  List<Element> listeElementsCompetences = courant.getChild("Compétences").getChildren("Compétence");
    	    	  if (listeElementsCompetences != null) {
	    	    	  while (cpt < listeElementsCompetences.size()) {
	    	    		  System.out.println(listeElementsCompetences.get(cpt).getText());
	    	    		  cpt++;
	    	    	  }
    	    	  }
    	      }
    	   
       }
       return userTrouve;
    }
    
    /*
     * Ajoute ou supprime une recommandation
     * 
     * retourne si oui ou non l'action a été effectué
     */
    static Boolean recommandation(String action, String nomRecommandeur, String nomRecommande, String competence, String fichier) {
    	Document document = null;
    	Element racine = null;
    	Boolean actionEffectuee = false;

    	// on fait sxb xml :
    	//On crée une instance de SAXBuilder
    	SAXBuilder sxb = new SAXBuilder();
    	try
    	{
    		//On crée un nouveau document JDOM avec en argument le fichier XML
    		//Le parsing est terminé ;)
    		document = sxb.build(new File(fichier));
    	}
    	catch(JDOMException | IOException e){
    		System.err.println(e);
    		System.err.println("Fichier XML invalide");
    		e.printStackTrace();
    	}

    	//On initialise un nouvel élément racine avec l'élément racine du document.
    	racine = document.getRootElement();
    	//On crée une List contenant tous les noeuds "utilisateur" de l'Element racine
    	List<Element> listUtilisateurs = racine.getChildren("utilisateur");
    	//On crée un Iterator sur notre liste
    	Iterator<Element> i = listUtilisateurs.iterator(); 

    	Boolean userRecommandeTrouve = false;
    	Boolean userRecommandeurTrouve = false;
    	Boolean competenceTrouve = false;

    	// on cherche user nomRecommandeur :
    	while(i.hasNext() && userRecommandeurTrouve == false) {

    		//On recrée l'Element courant à chaque tour de boucle afin de
    		//pouvoir utiliser les méthodes propres aux Element comme :
    		//sélectionner un nœud fils, modifier du texte, etc...
    		Element courant = (Element)i.next();
    		if (courant.getChild("nom").getText().equalsIgnoreCase(nomRecommandeur)) {
    			userRecommandeurTrouve = true;
    		}
    	}

    	if (userRecommandeurTrouve == true) {
    		// on cherche user nomRecommande :
    		i = listUtilisateurs.iterator();
    		while(i.hasNext() && userRecommandeTrouve == false) {

    			//On recrée l'Element courant à chaque tour de boucle afin de
    			//pouvoir utiliser les méthodes propres aux Element comme :
    			//sélectionner un nœud fils, modifier du texte, etc...
    			Element courant = (Element)i.next();
    			if (courant.getChild("nom").getText().equalsIgnoreCase(nomRecommande)) {
    				userRecommandeTrouve = true;
    				List<Element> listeCompetences = courant.getChild("Compétences").getChildren("Compétence");
    				Iterator<Element> iterateurListeCompetences = listeCompetences.iterator();
    				while (iterateurListeCompetences.hasNext() && competenceTrouve == false && listeCompetences.size() > 0) {
    					//On recrée l'Element courant à chaque tour de boucle afin de
    					//pouvoir utiliser les méthodes propres aux Element comme :
    					//sélectionner un nœud fils, modifier du texte, etc...
    					Element ElementCompetenceCourant = (Element)iterateurListeCompetences.next();
    					if (ElementCompetenceCourant.getText().equalsIgnoreCase(competence)) {
    						competenceTrouve = true;

    						// on ajoute ou supprime la recommandation :

    						Element recommandation = new Element("Recommandation");
    						Attribute provenance = new Attribute("Provenance", nomRecommandeur);
    						recommandation.setAttribute(provenance);
    						recommandation.setText(competence);

    						if (action.equals("ajout")) {

    							// s'il n'y a pas de recommandations du tout
    							if (courant.getChild("Recommandations") == null) {
    								Element recommandations = new Element("Recommandations");
    								recommandations.addContent(recommandation);
    								courant.addContent(recommandations);
    								actionEffectuee = true;
    							} else {
    								Boolean recommandationTrouve = false;
    								int tailleListeRecommandations = 0;
    								List<Element> listeRecommandations = null;
    								listeRecommandations = courant.getChild("Recommandations").getChildren();
    								tailleListeRecommandations = listeRecommandations.size();

    								// s'il y a des recommandations
    								if (tailleListeRecommandations > 0) {
    									int cpt = 0;
    									do {
    										if ( recommandation.getText().equals(listeRecommandations.get(cpt).getText())
    												&& recommandation.getAttribute("Provenance").getValue().equals(listeRecommandations.get(cpt).getAttribute("Provenance").getValue()) ) {
    											recommandationTrouve = true;
    										}
    										cpt++;
    									} while (cpt < tailleListeRecommandations && !recommandationTrouve);

    									// mais que celle-ci n'a pas été trouvé
    									if (!recommandationTrouve) {
    										courant.getChild("Recommandations").addContent(recommandation);
    										actionEffectuee = true;
    									}
    									// on en déduit qu'elle existe déjà
    									else {
    										System.err.print("Erreur ajout recommandation : de \""+nomRecommandeur+"\" vers \""+nomRecommande+"\" pour \""+competence+"\", ");
    										System.err.println("recommandation déjà existante !");
    										

    									}
    								} else {
    									courant.getChild("Recommandations").addContent(recommandation);
    									actionEffectuee = true;
    								}

    							}
    						} else if (action.equals("suppression")) {
    							if (courant.getChild("Recommandations") == null) {
    								System.err.println("Erreur suppression recommandation : pas de recommandations pour \"" + nomRecommande + "\" !");
    								    							
    							} else {

    								Boolean recommandationTrouve = false;
    								int tailleListeRecommandations = 0;
    								List<Element> listeRecommandations = null;
    								listeRecommandations = courant.getChild("Recommandations").getChildren();
    								tailleListeRecommandations = listeRecommandations.size();

    								if (tailleListeRecommandations > 0) {
    									int cpt = 0;
    									do {
    										if ( recommandation.getText().equals(listeRecommandations.get(cpt).getText())
    												&& recommandation.getAttribute("Provenance").getValue().equals(listeRecommandations.get(cpt).getAttribute("Provenance").getValue()) ) {
    											recommandationTrouve = true;
    										}
    										cpt++;
    									} while (cpt < tailleListeRecommandations && !recommandationTrouve);
    								}
    								
    								if (recommandationTrouve) {
    									Element ElementASupprimer = null;
    									Boolean ElementTrouve = false;
    									List<Element> ListDesElementsRecommandation = courant.getChild("Recommandations").getChildren();
    									int tailleListe = ListDesElementsRecommandation.size();
    									int cpt = 0;
    									if (tailleListe > 0) {
    										while (cpt < tailleListe && !ElementTrouve) {
	    										ElementASupprimer = ListDesElementsRecommandation.get(cpt);
	    										if (ElementASupprimer.getText().equals(recommandation.getText())
	    												&& ElementASupprimer.getAttribute("Provenance").getValue().equals(recommandation.getAttribute("Provenance").getValue())) {
	    											ElementTrouve = true;
	    										}
	    										cpt++;
    										}
    									}
    									if (courant.getChild("Recommandations").removeContent(ElementASupprimer) == false) {
        									System.err.print("Erreur suppresson recommandation : de \""+nomRecommandeur+"\" vers \""+nomRecommande+"\" pour \""+competence+"\", ");
        									System.err.println("suppression non réalisée !");
        									
        								} else {
        									actionEffectuee = true;
        								}
    								} else {
    									System.err.print("Erreur suppresson recommandation : de \""+nomRecommandeur+"\" vers \""+nomRecommande+"\" pour \""+competence+"\", ");
    									System.err.println("recommandation inexistante !");
    								}
    								
    								
    							}

    						} else {
    							System.err.println("Erreur recommandation : action autre que \"suppression\" ou \"ajout\" !");
    							
    						}
    					}

    				}
    				if (competenceTrouve == false) {
    					System.err.println("La compétence \"" + competence + "\" est introuvable chez l'utilisateur \"" + nomRecommande + "\" !");
    					
    				}

    			}

    			// on re-ecrit le xml
    			try
    			{
    				//On utilise ici un affichage classique avec getPrettyFormat()
    				XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
    				//Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
    				//avec en argument le nom du fichier pour effectuer la sérialisation.
    				System.out.println("enregistrement du fichier suivant:");
    				System.out.println(sortie.outputString(document));
    				sortie.output(document, new FileOutputStream(fichier));
    			}
    			catch (java.io.IOException e){
    				System.err.println("erreur avec l'enregistrement du fichier XML");
    				System.err.println(e);
    			}
    		}

    		if (userRecommandeTrouve == false) {
    			System.err.println("L'utilisateur \""+nomRecommande+"\" qui devait recevoir la recommandation n'a pas été trouvé !");
    			
    		}
    	}
    	else {
    		System.err.println("L'utilisateur \""+nomRecommandeur+"\" (qui veut recommander \""+nomRecommande+"\") n'a pas été trouvé !");
    		
    	}
    	return actionEffectuee;
    }
    
   static void LireMailCo(String NomFichier, String uti){
    Element racine = new Element("mail") ;
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
   List listUtilisateurs = racine.getChildren("liste");
   //On crée un Iterator sur notre liste
   Iterator i = listUtilisateurs.iterator(); 
   System.out.println("Vos mails:\n");
   while(i.hasNext())
   {
      //On recrée l'Element courant à chaque tour de boucle afin de
      //pouvoir utiliser les méthodes propres aux Element comme :
      //sélectionner un nœud fils, modifier du texte, etc...
      Element courant = (Element)i.next();
      //On affiche le nom de l’élément courant
      if (courant.getChild("destinataire").getText().equals(uti)){
          
          System.out.println("mail de "+courant.getChild("destinataire").getAttributeValue("emetteur")+" : " +courant.getChild("message").getText());
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
        System.out.println("############ --> Recommandations : tapez 6 ##########################");
        System.out.println("############ --> Afficher Liste Utilisateurs : tapez 7 ############");
        
        if ((userInput = stdIn.readLine()) != null && !userInput.equalsIgnoreCase("exit")) {
				System.out.println("Votre choix : " + userInput);
        }
        }while(!"1".equals(userInput)
        		&& !"2".equals(userInput)
        		&& !"3".equals(userInput)
        		&& !"4".equals(userInput)
        		&& !"5".equals(userInput)
        		&& !"6".equals(userInput)
        		&& !"7".equals(userInput));
            return userInput;
        }
    public static void main(String[] args) throws IOException, JDOMException {
        String choix1 = "1";
        
        GestionProto GP = new GestionProto();
        String userInput;
        String srvRep;
        Document docXMLRep;
        String xmlOut;
        GestionnaireUtilisateur monGU = null;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        
        try (///////////////////////Création du socket client////////////////////////////////////////
            Socket sockClient = new Socket("127.0.0.1",2009) ){
            PrintWriter outToServer = new PrintWriter(sockClient.getOutputStream(), true);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sockClient.getInputStream()));
/////////////////////////////////////////////////////////////////////////////////////////
System.out.println("################################################################");
System.out.println("################################################################");
System.out.println("############ BIENVENUE DANS NOTRE PROJET JAVA ##################");
System.out.println("################################################################");   
do{  
	userInput = Menu();
    System.out.println("userInput:" + userInput);
    if("1".equals(userInput)){
        
        String utilisateur;
        String mdp;
        
        System.out.println("Connexion: \n");
        System.out.println("Entrer vos informations: \n"); 
        try {
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
	            
	            
	            
	            xmlOut = GP.GenererMess("requête", "Connexion",utilisateur, "visi" ,mdp, "Profession","??",null);
	            outToServer.println(xmlOut + Character.toString((char)4));
	            
	            
	            //srvRep = inFromServer.readLine();
	            
	            //penser a catch OutOfMemoryError et INdexOUutOFBunds
	        	srvRep = GP.RecevoirMessRzo(inFromServer);
	            System.out.println("srvRep: " + srvRep);
	            docXMLRep = GP.LireMess(srvRep);
	            if (docXMLRep.getRootElement().getChild("message").getChild("action").getText().equals("Connexion")
	            		&& docXMLRep.getRootElement().getChild("message").getChild("résultat").getText().equals("1")){
	                System.out.println("Vous êtes bien connecté!");
	                
	                userConnecte = utilisateur;
	                
	                /*
	                ServerSocket ss = null;
	                int h;
	                h = 50000+LireXML("Exercice.xml",utilisateur);
	                LireMailCo("mail.xml",utilisateur);
	               ss = connexion(h, utilisateur);
	               System.out.println(utilisateur + "est l'utilisateur");
	               System.out.println("le port est "+h);
	               Mess(utilisateur);
	               */
	               
	               //userInput = Menu();
	            }
	            else{
	                System.out.println("Nom d'utilisateur ou mot depasse erroné, recommencez!");
	            }
	        }while(!docXMLRep.getRootElement().getChild("message").getChild("action").getText().equals("Connexion")
            		|| !docXMLRep.getRootElement().getChild("message").getChild("résultat").getText().equals("1")) ;       

        } catch (OutOfMemoryError | IndexOutOfBoundsException e) {
            System.err.println("Message du serveur trop gros, invalide");
            System.err.println(e);
        }
    } 
    else if ("2".equals(userInput)){
        String utilisateur;
        String visi;
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
                System.out.println("Souhaitez vous être visible?: \n Entrez oui ou non\n");
                
                visi = stdIn.readLine();
                System.out.println("votre choix : "+visi);
                if(!visi.equals("oui") && !visi.equals("non")){
                    System.out.println("Mauvaise entrée, recommencez!");
                } 
            }while(!visi.equals("oui") && !visi.equals("non"));
            
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
            
            // saisie des compétences
            List<String> listeCompetencesSaisies = new ArrayList<String>();
            String competenceSaisie;

            System.out.println("Rentrez vos compétences");
            System.out.println("une par ligne, finissez par \"fin\" (sans les guillemets) :");
        	Boolean saisieListeFinie = false;
        	Boolean saisieValide = false;
            do {
            	saisieValide = false;
            	competenceSaisie = stdIn.readLine();
            	if (competenceSaisie.equals("fin")) {
            		saisieListeFinie = true;
            		saisieValide = true;
            	} else if (competenceSaisie.equals("")) {
            		System.out.println("La dernière compétence saisie est vide, veuillez recommencer :");
            	} else if (listeCompetencesSaisies.contains(competenceSaisie)) {
            		System.out.println("La compétence saisie existe déjà, veuillez recommencer :");
            	} else {
                	saisieValide = true;
            		listeCompetencesSaisies.add(competenceSaisie);
            		System.out.println("Compétence : \"" + competenceSaisie + "\" ajoutée, veuillez continuer :");
            	}
            } while(!saisieValide || !saisieListeFinie);
            System.out.println(listeCompetencesSaisies.size() + " compétences saisies :");
            System.out.println("listeCompetencesSaisies: " + listeCompetencesSaisies);
            
            

            System.out.println("avant le GenMessReqAjUt");
            xmlOut = GP.GenMessReqAjUt(utilisateur,visi ,mdp, prof, listeCompetencesSaisies);
            System.out.println("aprés le GenMessReqAjUt");
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
            
            
            
            xmlOut = GP.GenererMess("requête", "Modification",utilisateur,"visu" ,mdp, "Profession","??",null);
            outToServer.println(xmlOut + Character.toString((char)4));
            
            
            srvRep = inFromServer.readLine();
            System.out.println("srvRep: " + srvRep);
            if (!srvRep.equals("a") ){
                System.out.println("Vous avez été trouvé!");
                
                String uti;
                String modepa;
                String profe;
                String visi;
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
                    System.out.println("Souhaitez vous être visible?: \n Entrez oui ou non");
                    
                    visi = stdIn.readLine();
                    System.out.print("votre choix : "+visi);
                    if(!visi.equals("oui") && !visi.equals("non")){
                        System.out.println("Mauvaise entrée, recommencez!");
                    }
                }while(!visi.equals("oui") && !visi.equals("non"));
                
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
                
                
                xmlOut = GP.GenererMess("requête", "ajoutUtilisateur",uti, visi ,modepa, profe, "??",null);
                
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
    else if("6".equals(userInput)){
        

        // saisie de l'utilisateur a recommander

        System.out.println("Rentrez le nom d'utilisateur a recommander :");
    	Boolean saisieValide = false;
        String user;
        do {
        	user = stdIn.readLine();
        	if (user.equals("") || afficherCompetences("Exercice.xml",user) == false) {
        		System.err.println("Nom d'utilisateur invalide, recommencez :");
        	} else {
        		saisieValide = true;
        	}
        } while(!saisieValide);
        
        System.out.println("Rentrez \"suppression\" ou \"ajout\" :");
    	saisieValide = false;
        String action;
        do {
        	action = stdIn.readLine();
        	if (!action.equals("ajout") && !action.equals("suppression")) {
        		System.err.println("Action invalide, recommencez :");
        	} else {
        		saisieValide = true;
        	}
        } while(!saisieValide);

        System.out.println("Rentrez la compétence :");
        String competence;
        saisieValide = false;
        do {
        	competence = stdIn.readLine();
        	if (competence.equals("")) {
        		System.err.println("Compétence vide, recommencez :");
        	} else {
        		saisieValide = true;
        	}
        } while(!saisieValide);
        
        Boolean retRecommandation = recommandation(action,userConnecte,user,competence,"Exercice.xml");
        if (retRecommandation) {
        	System.out.println("action effecutée !");
        } else {
        	System.err.println("action pas effecutée !");
        }
        
        
        
        
    } 
    else if("7".equals(userInput)){
    	
    	Document laRequeteXML = new Document();
    	Element dasProtokol = new Element("dasProtokol");
    	laRequeteXML.setRootElement(dasProtokol);
    	Element message = new Element("message");
    	dasProtokol.addContent(message);
    	Element type = new Element("type");
    	type.setText("requête");
    	message.addContent(type);
    	Element action = new Element("action");
    	action.setText("afficherListeUtilisateurs");
    	message.addContent(action);
    	
    	XMLOutputter monXMLOutputter = new XMLOutputter();
    	String laRequeteXMLEnCaracteres = monXMLOutputter.outputString(laRequeteXML);
    	
    	outToServer.println(laRequeteXMLEnCaracteres + Character.toString((char)4));
    	srvRep = GP.RecevoirMessRzo(inFromServer);
    	
    	Document repDocXML = GP.LireMess(srvRep);
    	List<Element> listeUsers = repDocXML.getRootElement().getChild("message").getChild("donnees").getChildren();
    	Iterator<Element> iterateurListeUsers = listeUsers.iterator();
    	
    	while (iterateurListeUsers.hasNext()) {
    		Element ElementActuel = iterateurListeUsers.next();
    		System.out.println("nom: "+ElementActuel.getChild("nom").getText()+" | profession: "+ElementActuel.getChild("Profession").getText());
    	}
    	
    }
    
}while(true);
        } catch (IOException e) {
        	System.err.println("Problème avec le socket de connexion au serveur !");
        	e.printStackTrace();
        }
}
    
}


