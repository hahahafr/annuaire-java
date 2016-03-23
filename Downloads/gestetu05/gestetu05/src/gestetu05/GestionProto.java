package gestetu05;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

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
	
	public String GenMessReqAjUt(
			String nomUtilisateur,
			String visi,
			String mdp,
			String profession,
			List<String> competences)
	{
		System.out.println("dans GenMessReqAjUt au tout début");
		System.out.println(competences);
		return GenererMess("requête",
				"ajoutUtilisateur",
				nomUtilisateur,
				visi,
				mdp,
				profession,
				"pas de résultat, c'est une requête pas une réponse...",
				competences);
	}
	
	public Document LireMess(String mess) throws JDOMException, IOException {
		
		SAXBuilder sxb = new SAXBuilder();
		Document reqXML = sxb.build(new StringReader(mess));		
		
		return reqXML;
		
	}
	
	public String GenererMess(String type,
			String action,
			String nomUtilisateur,
			String visi,
			String mdp,
			String Profession,
			String res,
			List<String> competences)
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
                    Element visibilite = new Element("Visibilite");
                    visibilite.setText(visi);
                    message.addContent(NomU);
                    message.addContent(visibilite);
                    
                    Element motdepasse = new Element("MotdePasse");
                    motdepasse.setText(mdp);
                    message.addContent(motdepasse);
                    
                    Element Profes = new Element("Profession");
                    Profes.setText(Profession);
                    message.addContent(Profes);
                    
                    // on traite la liste des competences
                    if (competences != null) {
	                    int tailleCompetences = competences.size();
	                    System.out.println("dans GenererMess : tailleCompetences = " + tailleCompetences);
	                    int cpt = 0;
	                    Element XMLcompetences = new Element("Compétences");
	                    while (cpt < tailleCompetences) {
	                    	System.out.println("cpt = " + cpt);
	                    	Element XMLcompetence = new Element("Compétence");
	                    	XMLcompetence.setText(competences.get(cpt));
	                    	XMLcompetences.addContent(XMLcompetence);
	                    	cpt++;
	                    }
	                    message.addContent(XMLcompetences);
                    }
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
			e.printStackTrace();
		}
		
		try {
			System.out.println(baos.toString("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return baos.toString();
	}
	
	public String RecevoirMessRzo(BufferedReader in) throws IndexOutOfBoundsException, IOException {
		
		String clientInput;
		String buf = "";
		
		char[] monBuffer;
		char[] charArrayOfclientInput;
		
		int tailleBuffer = 99999;
		int positionDansBuffer = 0;
		int tailleDeLaLigneRecue = 0;
		
		monBuffer = new char[tailleBuffer];
		
		
		
		
		System.out.println("rzo1");
		clientInput = in.readLine();
		System.out.println("in.readLine()=" + clientInput);
		
		
		if (clientInput.equals("a") || clientInput.equals("a"+Character.toString((char)4))) {
			System.out.println("\"a\" recu !");
			return "a";
		}
		
		System.out.println("rzo2");
		while(clientInput != null
				&& !clientInput.endsWith("</dasProtokol>" + Character.toString((char)4))
				&& !clientInput.endsWith(Character.toString((char)4))) {

			System.out.println("rzo3: " + new String(monBuffer, 0, positionDansBuffer));
			System.out.println("rzo3: " + buf);
			
				// on ajoute la ligne recue au message que l'on reconstruit
				
				charArrayOfclientInput = clientInput.toCharArray();
				tailleDeLaLigneRecue = charArrayOfclientInput.length;
				
				if (tailleDeLaLigneRecue + positionDansBuffer > tailleBuffer) {
					throw new IndexOutOfBoundsException("Message trop gros, debordement de la memoire tampon");
				}
				
				System.arraycopy(charArrayOfclientInput,
						0,
						monBuffer,
						positionDansBuffer,
						tailleDeLaLigneRecue);
				
				charArrayOfclientInput = null;
				
				positionDansBuffer = positionDansBuffer + tailleDeLaLigneRecue;
				
				buf = buf + clientInput + "\n";
				System.out.println("rzo4: "  + new String(monBuffer, 0, positionDansBuffer));
				System.out.println("rzo4: "  + buf);
				
				// on recupere la ligne suivante
				clientInput = in.readLine();
				System.out.println("rzo5");
		}

		System.out.println("fin while");
		System.out.println("rzo6: " + new String(monBuffer, 0, positionDansBuffer));
		System.out.println("rzo6: " + buf);
			// la requete est entierement sur une ligne
			if (clientInput.endsWith("</dasProtokol>" + Character.toString((char)4)))
			{

				System.out.println("rzo7: " + new String(monBuffer, 0, positionDansBuffer));
				System.out.println("rzo7: " + buf);
				// on enleve le caractere \4 (EOT) de la chaine recue
				clientInput = (String) clientInput.subSequence(0, clientInput.length() - 1);
				
				// on rajoute la derniere ligne dans le buffer et on le renvoi
				
				charArrayOfclientInput = clientInput.toCharArray();
				tailleDeLaLigneRecue = charArrayOfclientInput.length;
				
				if (tailleDeLaLigneRecue + positionDansBuffer > tailleBuffer) {
					throw new IndexOutOfBoundsException("Message trop gros, debordement de la memoire tampon");
				}
				
				System.arraycopy(charArrayOfclientInput,
						0,
						monBuffer,
						positionDansBuffer,
						tailleDeLaLigneRecue);
				
				charArrayOfclientInput = null;
				
				positionDansBuffer = positionDansBuffer + tailleDeLaLigneRecue;
				
				buf = buf + clientInput + "\n";
				System.out.println("got (1 ligne):");
				System.out.println("--- debut buffer ---");
				System.out.println(new String(monBuffer, 0, positionDansBuffer));
				System.out.println("--- fin buffer ---");
				System.out.println("got (1 ligne):");
				System.out.println("--- debut buffer ---");
				System.out.println(buf);
				System.out.println("--- fin buffer ---");
				
				return new String(monBuffer, 0, positionDansBuffer);
			}
			else if (clientInput.endsWith(Character.toString((char)4)))
			{
				System.out.println("rzo8: " + new String(monBuffer, 0, positionDansBuffer));
				System.out.println("rzo8: " + buf);
				// on ne rajoute pas la derniere ligne et on renvoi le buffer
				System.out.println("got (plus de 1 ligne):");
				System.out.println("--- debut buffer ---");
				System.out.println(new String(monBuffer, 0, positionDansBuffer));
				System.out.println("--- fin buffer ---");
				System.out.println("got (plus de 1 ligne):");
				System.out.println("--- debut buffer ---");
				System.out.println(buf);
				System.out.println("--- fin buffer ---");
				
				return new String(monBuffer, 0, positionDansBuffer);
			}
			else // clientInput == null
			{
				// on renvoi ce qu'on a déjà
				return new String(monBuffer, 0, positionDansBuffer);
			}
								
		
	}
	
	public String traitementReq(String req) {
		
		try {

		// on vérifie que la requête est lisible en XML
		Document docReqXML = LireMess(req);
		
		Element root = docReqXML.getRootElement();
		
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
					e.printStackTrace();
				}				
				
				// case action = ?

				int nbAction = ceMessage.getChildren("action").size();
				String action = ceMessage.getChild("action").getText();
				
				if (action.equals("ajoutUtilisateur"))
				{       
					// infos personnelles
					List<String> monUser = new ArrayList<String>();
					
					      
					String nomUtilisateur = ceMessage.getChild("nom").getText();                                        
					String visi = ceMessage.getChild("Visibilite").getText();
					System.out.println();
					System.out.println("visi: " + visi);
					String Mdp = ceMessage.getChild("MotdePasse").getText();
					String Profession = ceMessage.getChild("Profession").getText();
					monUser.add(nomUtilisateur);
					monUser.add(visi);
					monUser.add(Mdp);
					monUser.add(Profession);
					System.out.println("liste monUser: " + monUser);
                                        
					// liste compétences
					Element competences = ceMessage.getChild("Compétences");
					List<Element> listeCompetencesXMLElems = competences.getChildren();
					List<String> listeCompetencesString = new ArrayList<String>();
					if (listeCompetencesXMLElems != null) {
					    int tailleListeCompetencesXML = listeCompetencesXMLElems.size();
					    int cpt = 0;
					    while (cpt < tailleListeCompetencesXML) {
					    	listeCompetencesString.add(listeCompetencesXMLElems.get(cpt).getText());
					    	cpt++;
					    }
					}
					System.out.println("liste listeCompetencesString: " + listeCompetencesString);
                                        
					int result;
					result=monGU.AjouterUtilisateur(monUser, listeCompetencesString, "Exercice.xml");
					System.out.println("Resultat de l'ajout:" + result);
                                    if (1 == result){
                                        
                                        return GenererMess("réponse",
                                        		"ajoutUtilisateur",
                                        		nomUtilisateur,
                                        		visi,
                                        		Mdp,
                                        		Profession,
                                        		"1",
                                        		null);
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
                                        
                                        return GenererMess("réponse",
                                        		"Connexion",
                                        		nomUtilisateur,
                                        		"visi",
                                        		Mdp,
                                        		"Profession",
                                        		"1",
                                        		null);
                                    }else{
                                        
                                    	return GenererMess("réponse",
                                        		"Connexion",
                                        		nomUtilisateur,
                                        		"visi",
                                        		Mdp,
                                        		"Profession",
                                        		"0",
                                        		null);
                                    }
                                    
				}
                                else
                                if (action.equals("Modification"))
				{
                                    String nomUtilisateur = ceMessage.getChild("nom").getText();
                                    String Mdp = ceMessage.getChild("MotdePasse").getText();
                                    
                                    monGU.ChercherInformation("Exercice.xml", nomUtilisateur, Mdp);
                                    
                                    
                                    System.out.println("Resultat de la connexion:" + monGU.resultatRecherche);
                                    if (monGU.resultatRecherche == 1){
                                        
                                        monGU.ModicationXML("Exercice.xml", nomUtilisateur, Mdp);
                                        return GenererMess("réponse",
                                        		"Connexion",
                                        		nomUtilisateur,
                                        		"visi",
                                        		Mdp,
                                        		"Profession",
                                        		"1",
                                        		null);
                                    }else{
                                        
                                        return "a";
                                    }
                                    
				}
                                else if (action.equals("afficherListeUtilisateurs")) {
                                	
                                	Document maRepXML = new Document();
                                	Element repRoot = new Element("dasProtokol");
                                	Element repMess = new Element("message");
                                	Element repType = new Element("réponse");
                                	Element repAction = new Element("action");
                                	Element donnees = new Element("donnees");
                                	
                                	List<Element> listeUsers = monGU.getListeUtilisateurs("Exercice.xml");
                                	
                                	Iterator<Element> iterateurListeUsers = listeUsers.iterator();
                                	
                                	while (iterateurListeUsers.hasNext()) {
                                		Element ElementActuel = iterateurListeUsers.next();
                                		Element ElementNouveau = new Element("utilisateur");
                                		ElementNouveau.addContent((new Element("nom")).setText(ElementActuel.getChild("nom").getText()));
                                		ElementNouveau.addContent((new Element("Profession")).setText(ElementActuel.getChild("Profession").getText()));
                                		ElementNouveau.addContent((new Element("Visibilite")).setText(ElementActuel.getChild("Visibilite").getText()));
                                		donnees.addContent(ElementNouveau);
                                	}
                                	
                                	repMess.addContent(repType);
                                	repMess.addContent(repAction);
                                	repMess.addContent(donnees);
                                	repRoot.addContent(repMess);
                                	maRepXML.addContent(repRoot);
                                	
                                	XMLOutputter monXMLOutputter = new XMLOutputter(Format.getPrettyFormat());
                                	
                                	return monXMLOutputter.outputString(maRepXML);
                                	
                                }
			}
			else if (type.equals("réponse")) 
			{
				System.out.println("réponse reçue :");
				XMLOutputter sortiePretty = new XMLOutputter(Format.getPrettyFormat());
				try {
					sortiePretty.output(ceMessage, System.out);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// IHM??
			} else {
				throw new JDOMException();
			}
	
			
		}
		
		} catch (NoSuchElementException nsee) {
			return GenererMess("réponse", "RequêteMalformée","NomUtilisateur", "visi" ,"MotDePasse", "Profession", "-1", null);
		} catch (JDOMException je) {
			return GenererMess("réponse", "RequêteMalformée","NomUtilisateur" ,"visi","MotDePasse", "Profession", "-1", null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return GenererMess("réponse", "Test","NomUtilisateur", "visi" ,"MotDePasse", "Profession", "1", null);
		
		
	}
	
}
