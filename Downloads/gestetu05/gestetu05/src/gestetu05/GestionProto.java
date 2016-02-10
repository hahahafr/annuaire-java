package gestetu05;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.util.IteratorIterable;
import org.jdom2.input.SAXBuilder;

public class GestionProto {
	
	GestionnaireUtilisateur monGU;
	List<String> ListeInscription = new ArrayList<>() ;
        
        
	public GestionProto(GestionnaireUtilisateur startGU) {
		monGU = startGU;
	}
	
	public GestionProto()
	{
		monGU = null;
	}
	
	public String GenererMess(String type, String action, String nomUtilisateur,String mdp,String Profession, String res)
	{
		// on prépare le squelette de la réponse
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Element racine = new Element("dasProtokol");
		Element message = new Element("message");
                
		// on spécifie le type de message
		Element typeXML = new Element("type");
		typeXML.setText(type);
		message.addContent(typeXML);         
          
                
		// on ajoute le contenu
		Element actionXML = new Element("action");
		actionXML.setText(action);
		message.addContent(actionXML);
                
                // Si l' action est ajoutUtilisateur on prend les informtions d'inscription
                if("ajoutUtilisateur".equals(action)){
                    Element NomU = new Element("nom");
                    NomU.setText(nomUtilisateur);
                    message.addContent(NomU);
                    
                    Element motdepasse = new Element("MotdePasse");
                    motdepasse.setText(mdp);
                    message.addContent(motdepasse);
                    
                    Element Profes = new Element("Profession");
                    Profes.setText(Profession);
                    message.addContent(Profes);
                 }
                
                // Si l' action est connexion ou modification on prend les informtions de connexion
                if("Connexion".equals(action) || "Modification".equals(action)){
                    Element NomU = new Element("nom");
                    NomU.setText(nomUtilisateur);
                    message.addContent(NomU);
                    
                    Element motdepasse = new Element("MotdePasse");
                    motdepasse.setText(mdp);
                    message.addContent(motdepasse);
                 }
                
                //On ajoute le résultat
		Element resultat = new Element("résultat");
		resultat.setText(res);
		message.addContent(resultat);
		
		// on génère le XML
		Document document = new Document(racine);
		racine.addContent(message);
		XMLOutputter sortiePretty = new XMLOutputter(Format.getPrettyFormat());
		try {
			sortiePretty.output(document, baos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println(baos.toString("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return baos.toString();
	}
	
	public String traitementReq(String req) {
		
		// on vérifie que la requête est lisible en XML
		SAXBuilder sxb = new SAXBuilder();
		try {
			
			Document reqXML = sxb.build(new StringReader(req));		
		
		try {
		
		Element root = reqXML.getRootElement();
		
		List<Element> listeMess = root.getChildren("message");
		
		Iterator<Element> mess = listeMess.iterator();
		
		while (mess.hasNext())
		{
			System.out.println("mess::");
			
			Element ceMessage = mess.next();
			
			// on vérifie que la requête est correctement formée
			// nbType == 1
			// type == "requête" || type == "réponse"
			int nbType = ceMessage.getChildren("type").size();
			String type = ceMessage.getChild("type").getText();
			if ( nbType != 1 || (!type.equals("requête") && !type.equals("réponse")) )
				throw new JDOMException();
			if (type.equals("requête"))
			{
				System.out.println("requête reçue :");
				XMLOutputter sortiePretty = new XMLOutputter(Format.getPrettyFormat());
				try {
					sortiePretty.output(ceMessage, System.out);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				
				// case action = ?

				int nbAction = ceMessage.getChildren("action").size();
				String action = ceMessage.getChild("action").getText();
				
				if (action.equals("ajoutUtilisateur"))
				{       
                                    List<String> monUser = new ArrayList<String>();  
                                        String nomUtilisateur = ceMessage.getChild("nom").getText();
					String Mdp = ceMessage.getChild("MotdePasse").getText();
                                        String Profession = ceMessage.getChild("Profession").getText();
					monUser.add(nomUtilisateur);
					monUser.add(Mdp);
					monUser.add(Profession);
					int result;
					result=monGU.AjouterUtilisateur(monUser, "Exercice.xml");
					System.out.println("Resultat de l'ajout:" + result);
                                    if (1 == result){
                                        
                                        return GenererMess("réponse", "ajoutUtilisateur",nomUtilisateur,Mdp, "Profession", "1");
                                    }else{
                                        
                                        return "a";//GenererMess("réponse", "Connexion",nomUtilisateur,Mdp, "Profession", "0");
                                    }
				}
				else
                                    if (action.equals("Connexion"))
				{
                                    String nomUtilisateur = ceMessage.getChild("nom").getText();
                                    String Mdp = ceMessage.getChild("MotdePasse").getText();
                                    System.out.println("test nom"+nomUtilisateur);
                                    System.out.println("test mdp"+Mdp);
                                    monGU.ChercherInformation("Exercice.xml", nomUtilisateur, Mdp);
                                    int result;
                                    
                                    System.out.println("Resultat de la connexion:" + monGU.resultatRecherche);
                                    if (monGU.resultatRecherche == 1){
                                        
                                        return GenererMess("réponse", "Connexion",nomUtilisateur,Mdp, "Profession", "1");
                                    }else{
                                        
                                        return "a";//GenererMess("réponse", "Connexion",nomUtilisateur,Mdp, "Profession", "0");
                                    }
                                    
				}
                                else
                                if (action.equals("Modification"))
				{
                                    String nomUtilisateur = ceMessage.getChild("nom").getText();
                                    String Mdp = ceMessage.getChild("MotdePasse").getText();
                                    
                                    monGU.ChercherInformation("Exercice.xml", nomUtilisateur, Mdp);
                                    int result;
                                    
                                    System.out.println("Resultat de la connexion:" + monGU.resultatRecherche);
                                    if (monGU.resultatRecherche == 1){
                                        
                                        monGU.ModicationXML("Exercice.xml", nomUtilisateur, Mdp);
                                        return GenererMess("réponse", "Connexion",nomUtilisateur,Mdp, "Profession", "1");
                                    }else{
                                        
                                        return "a";
                                    }
                                    
				}
			}
			else if (type.equals("réponse")) 
			{
				System.out.println("réponse reçue :");
				XMLOutputter sortiePretty = new XMLOutputter(Format.getPrettyFormat());
				try {
					sortiePretty.output(ceMessage, System.out);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// IHM??
			} else {
				throw new JDOMException();
			}
	
			
		}
		
		} catch (NoSuchElementException nsee) {
			return GenererMess("réponse", "RequêteMalformée","NomUtilisateur" ,"MotDePasse", "Profession", "-1");
		}
		} catch (JDOMException je) {
			return GenererMess("réponse", "RequêteMalformée","NomUtilisateur" ,"MotDePasse", "Profession", "-1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return GenererMess("réponse", "Test","NomUtilisateur" ,"MotDePasse", "Profession", "1");
		
		
	}
	
}
