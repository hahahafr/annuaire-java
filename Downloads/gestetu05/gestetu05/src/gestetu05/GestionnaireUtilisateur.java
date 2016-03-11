package gestetu05;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ALEX-MOMO
 */

/////////////////////////////////////////////////////////////////////////////////////////////////////
public class GestionnaireUtilisateur {
//CONSTRUCTEUR
public GestionnaireUtilisateur() {
    System.out.println("Création d'une instance GestionnaireUtilisateur !");
}    
// Notre élément racine est repertoir dans le fichier XML
Element racine = new Element("repertoire") ;
Document document = new Document(racine);
// Création de la liste de STRING pour récupérer les informations du fichier xml
List<String> ListeXML = new ArrayList<>() ;

// pour la V3 nous avons besoin de créer une liste de compétences par utilisateur
// donc on va lire le fichier XML, le transformé en Document puis Element JDOM,
// faire les modifications nécessaire (ajout d'une nouvel utilisateur avec la liste),
// et re-écrire le fichier


int resultatRecherche;


synchronized void enregistreXML(String fichier)
{
	System.out.println("on rentre dans enregistreXML");
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

	System.out.println("on sort de enregistreXML");
}


synchronized void EcrireFichierXML(List<String> Liste){   
	System.out.println("on rentre dans EcrireFichierXML");
    int i=0;    
    int e = Liste.size();
    System.out.println(e);
    System.out.println(Liste);
    racine.removeChildren("utilisateur");
    while(i<e){ 
      Element utilisateur = new Element("utilisateur");
      Attribute numUtilisateur= new Attribute("NuméroUtilisateur",String.valueOf(i/3));
      Element visibilite= new Element("Visibilite");
      utilisateur.setAttribute(numUtilisateur);
      Element nom = new Element("nom");
      nom.setText(Liste.get(i));
      visibilite.setText(Liste.get(i+1));
      utilisateur.addContent(nom);
      utilisateur.addContent(visibilite);
      Element mot_de_passe = new Element("MotDePasse");
      mot_de_passe.setText(Liste.get(i+2));
      utilisateur.addContent(mot_de_passe);
      Element prof = new Element("Profession");
      prof.setText(Liste.get(i+3));
      utilisateur.addContent(prof);
      racine.addContent(utilisateur);
      i=i+4;
    };

	System.out.println("on sort de EcrireFichierXML");
    }

void ChercherInformation(String NomFichier, String nom, String mdp){
	System.out.println("on rentre dans ChercherInformation");
    
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
      //On recrée l'Element courant à chaque tour de boucle afin de
      //pouvoir utiliser les méthodes propres aux Element comme :
      //sélectionner un nœud fils, modifier du texte, etc...
      Element courant = (Element)i.next();
      //On affiche le nom de l’élément courant
      
      System.out.println(courant.getChild("nom").getText());
      System.out.println(courant.getChild("MotDePasse").getText());
      System.out.println("nom2"+nom);
      System.out.println("mdp2"+mdp);
      if ( courant.getChild("nom").getText().equals(nom) && courant.getChild("MotDePasse").getText().equals(mdp) ){
          resultatRecherche= 1;
          System.out.println("good");
          break;
      }
      else{
          resultatRecherche= 0;
      }     
      
   }
   System.out.println("resultatRecherche: "+resultatRecherche);
	System.out.println("on sort de ChercherInformation");
}

List<Element> getListeUtilisateurs(String NomFichier) {
		System.out.println("on rentre dans getListeUtilisateurs");
	    
	    //On crée une instance de SAXBuilder
	      SAXBuilder sxb = new SAXBuilder();
	      try
	      {
	         //On crée un nouveau document JDOM avec en argument le fichier XML
	         //Le parsing est terminé ;)
	         document = sxb.build(new File(NomFichier));
	      }
	      catch(JDOMException | IOException e){
	    	  System.err.println("Erreur avec la lecture du fichier XML");
	    	  System.err.println(e);
	      }

	      //On initialise un nouvel élément racine avec l'élément racine du document.
	      racine = document.getRootElement();
	   //On crée une List contenant tous les noeuds "utilisateur" de l'Element racine
	   List<Element> listUtilisateurs = racine.getChildren("utilisateur");
	   
	   return listUtilisateurs;
	 
}

void ModicationXML(String NomFichier, String NomU, String MdpU){
	System.out.println("on rentre dans ModificationXML");
    List<String> ListeUtilisateur = new ArrayList<>() ;
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
      //On recrée l'Element courant à chaque tour de boucle afin de
      //pouvoir utiliser les méthodes propres aux Element comme :
      //sélectionner un nœud fils, modifier du texte, etc...
      Element courant = (Element)i.next();
      String U;
      String M;
      U=courant.getChild("nom").getText();
      M=courant.getChild("MotDePasse").getText();
      //On affiche le nom de l’élément courant
      if (M.equals(MdpU) && U.equals(NomU)){
          
      System.out.println("Utilisateur trouvé");
      
      }
   else
   {
      ListeUtilisateur.add(courant.getChild("nom").getText());
      ListeUtilisateur.add(courant.getChild("Visibilite").getText());
      ListeUtilisateur.add(courant.getChild("MotDePasse").getText());
      ListeUtilisateur.add(courant.getChild("Profession").getText());
   }
   
}
   System.out.println("Liste modifiée: "+ListeUtilisateur);
   EcrireFichierXML(ListeUtilisateur);
  enregistreXML("Exercice.xml");
	System.out.println("on sort de ModificationXML");
}

List<String> LireXML(String NomFichier){
	System.out.println("on rentre dans LireXML");
    List<String> ListeUtilisateur = new ArrayList<>() ;
    //On crée une instance de SAXBuilder
      SAXBuilder sxb = new SAXBuilder();
      try
      {
         //On crée un nouveau document JDOM avec en argument le fichier XML
         //Le parsing est terminé ;)
         document = sxb.build(new File(NomFichier));
      }
      catch(JDOMException | IOException e){
    	  System.err.println("Erreur avec la lecture du fichier XML");
    	  System.err.println(e);
      }
      
      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
      System.out.println("lecture du fichier suivant:");
      System.out.println(sortie.outputString(document));

      //On initialise un nouvel élément racine avec l'élément racine du document.
      racine = document.getRootElement();
   //On crée une List contenant tous les noeuds "utilisateur" de l'Element racine
   List listUtilisateurs = racine.getChildren("utilisateur");
   System.out.println("listUtilisateurs: " + listUtilisateurs);
   //On crée un Iterator sur notre liste
   Iterator i = listUtilisateurs.iterator();
   
   while(i.hasNext())
   {
      //On recrée l'Element courant à chaque tour de boucle afin de
      //pouvoir utiliser les méthodes propres aux Element comme :
      //sélectionner un nœud fils, modifier du texte, etc...
      Element courant = (Element)i.next();
      //On affiche le nom de l’élément courant
      ListeUtilisateur.add(courant.getChild("nom").getText());
      ListeUtilisateur.add(courant.getChild("Visibilite").getText());
      ListeUtilisateur.add(courant.getChild("MotDePasse").getText());
      ListeUtilisateur.add(courant.getChild("Profession").getText());         
      
   }
   System.out.println("ListeUtilisateur: " + ListeUtilisateur);
	System.out.println("on sort de LireXML");
   return ListeUtilisateur;
}

int AjouterUtilisateur(List<String> AjoutUtilisateur, List<String> ListeCompetences, String NomFichier){
	System.out.println("on rentre dans AjouterUtilisateur");
  //List<String> ListeUtilisateur = new ArrayList<>();
  //ListeUtilisateur= LireXML(NomFichier);
  int resultatAjout = 0;
    
    //On crée une instance de SAXBuilder
      SAXBuilder sxb = new SAXBuilder();
      try
      {
         //On crée un nouveau document JDOM avec en argument le fichier XML
         //Le parsing est terminé ;)
         document = sxb.build(new File(NomFichier));
      }
      catch(JDOMException | IOException e){
    	  System.err.println("Erreur dans AjouterUtilisateur de GestionnaireUtilisateur, le fichier n'est pas XML ?");
    	  System.err.println(e);
      }
      XMLOutputter myXMLOutputter = new XMLOutputter();
      System.out.println("document: \n" + myXMLOutputter.outputString(document));

      //On initialise un nouvel élément racine avec l'élément racine du document.
      racine = document.getRootElement();
      System.out.println("racine: \n" + myXMLOutputter.outputString(racine));
   //On crée une List contenant tous les noeuds "utilisateur" de l'Element racine
   List listUtilisateurs = racine.getChildren("utilisateur");
   System.out.println("listUtilisateurs: " + listUtilisateurs);
   //On crée un Iterator sur notre liste
   Iterator i = listUtilisateurs.iterator(); 
   
   // si la liste est vide c'est que le fichier est vide donc on peut ajouter
   // l'utilisateur sans crainte
   if (listUtilisateurs.size() == 0)
   {
	   resultatAjout = 1;
   }
   
   while(i.hasNext())
   {
      //On recrée l'Element courant à chaque tour de boucle afin de
      //pouvoir utiliser les méthodes propres aux Element comme :
      //sélectionner un nœud fils, modifier du texte, etc...
      Element courant = (Element)i.next();
      //On affiche le nom de l’élément courant
      if (courant.getChild("nom").getText().equals(AjoutUtilisateur.get(0))){
          resultatAjout= 0;
          System.out.println("Utlisateur déjà présent");
          break;
      }
      else{
          resultatAjout= 1;
      }
   }
      
if(resultatAjout== 1){
	
	Element nouvelUtilisateur = new Element("utilisateur");
	Attribute numUtil = new Attribute("NuméroUtilisateur", Integer.toString(listUtilisateurs.size()));
	nouvelUtilisateur.setAttribute(numUtil);
	
	Element nouvelUtilisateur_nom = new Element("nom");
	nouvelUtilisateur_nom.setText(AjoutUtilisateur.get(0));
	nouvelUtilisateur.addContent(nouvelUtilisateur_nom);

	Element nouvelUtilisateur_visi = new Element("Visibilite");
	nouvelUtilisateur_visi.setText(AjoutUtilisateur.get(1));
	nouvelUtilisateur.addContent(nouvelUtilisateur_visi);

	Element nouvelUtilisateur_MotDePasse = new Element("MotDePasse");
	nouvelUtilisateur_MotDePasse.setText(AjoutUtilisateur.get(2));
	nouvelUtilisateur.addContent(nouvelUtilisateur_MotDePasse);

	Element nouvelUtilisateur_profession = new Element("Profession");
	nouvelUtilisateur_profession.setText(AjoutUtilisateur.get(3));
	nouvelUtilisateur.addContent(nouvelUtilisateur_profession);

	Element nouvelUtilisateur_competences = new Element("Compétences");
	if (ListeCompetences != null) {
		int tailleListe = ListeCompetences.size();
		int cpt = 0;
		while (cpt < tailleListe) {
			Element competence = new Element("Compétence");
			competence.setText(ListeCompetences.get(cpt));
			nouvelUtilisateur_competences.addContent(competence);
			cpt++;
		}
	}
	nouvelUtilisateur.addContent(nouvelUtilisateur_competences);
	
	racine.addContent(nouvelUtilisateur);
	
	System.out.println("nouvelUtilisateur: " + myXMLOutputter.outputString(nouvelUtilisateur));
	
  /*
  ListeUtilisateur.add(AjoutUtilisateur.get(0));
  ListeUtilisateur.add(AjoutUtilisateur.get(1));
  ListeUtilisateur.add(AjoutUtilisateur.get(2));
  ListeUtilisateur.add(AjoutUtilisateur.get(3));
  System.out.println(ListeUtilisateur);
  
	
  EcrireFichierXML(ListeUtilisateur);
  */
	enregistreXML("Exercice.xml");
}
System.out.println("on sort de AjouterUtilisateur");
return resultatAjout;
}



}
