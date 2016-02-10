package gestetu05;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import static org.jdom2.filter.Filters.document;
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
int resultatRecherche;


synchronized void enregistreXML(String fichier)
{
   try
   {
      //On utilise ici un affichage classique avec getPrettyFormat()
      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
      //Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
      //avec en argument le nom du fichier pour effectuer la sérialisation.
      sortie.output(document, new FileOutputStream(fichier));
   }
   catch (java.io.IOException e){}
}


synchronized void EcrireFichierXML(List<String> Liste){   
    int i=0;    
    int e = Liste.size();
    System.out.println(e);
    System.out.println(Liste);
    racine.removeChildren("utilisateur");
    while(i<e){ 
      Element utilisateur = new Element("utilisateur");
      Attribute numUtilisateur= new Attribute("NuméroUtilisateur",String.valueOf(i/3));
      utilisateur.setAttribute(numUtilisateur);
      Element nom = new Element("nom");
      nom.setText(Liste.get(i));
      utilisateur.addContent(nom);
      Element mot_de_passe = new Element("MotDePasse");
      mot_de_passe.setText(Liste.get(i+1));
      utilisateur.addContent(mot_de_passe);
      Element prof = new Element("Profession");
      prof.setText(Liste.get(i+2));
      utilisateur.addContent(prof);
      racine.addContent(utilisateur);
      i=i+3;
    };
    
    }

void ChercherInformation(String NomFichier, String nom, String mdp){
    
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
   System.out.println("_-_"+resultatRecherche);
}

void ModicationXML(String NomFichier, String NomU, String MdpU){
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
      ListeUtilisateur.add(courant.getChild("MotDePasse").getText());
      ListeUtilisateur.add(courant.getChild("Profession").getText());
   }
   
}
   System.out.println("Liste modifiée:"+ListeUtilisateur);
   EcrireFichierXML(ListeUtilisateur);
  enregistreXML("Exercice.xml");
}

List<String> LireXML(String NomFichier){
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
      //On affiche le nom de l’élément courant
      ListeUtilisateur.add(courant.getChild("nom").getText());
      ListeUtilisateur.add(courant.getChild("MotDePasse").getText());
      ListeUtilisateur.add(courant.getChild("Profession").getText());         
      
   }
   System.out.println(ListeUtilisateur);
   return ListeUtilisateur;
}

int AjouterUtilisateur(List<String> AjoutUtilisateur,String NomFichier){
  List<String> ListeUtilisateur = new ArrayList<>();
  ListeUtilisateur= LireXML(NomFichier);
  int resultatAjout = 0;
    
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
  
  ListeUtilisateur.add(AjoutUtilisateur.get(0));
  ListeUtilisateur.add(AjoutUtilisateur.get(1));
  ListeUtilisateur.add(AjoutUtilisateur.get(2));
  System.out.println(ListeUtilisateur);
  EcrireFichierXML(ListeUtilisateur);
  enregistreXML("Exercice.xml");
}
return resultatAjout;
}
}
