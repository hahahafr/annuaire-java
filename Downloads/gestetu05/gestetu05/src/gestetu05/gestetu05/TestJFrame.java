package gestetu05;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

class TestJFrame extends JFrame implements ActionListener {
		
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
		private static JTextField champ_user = new JTextField(10);
		private static JPasswordField champ_password= new JPasswordField(10);
		private static JLabel label_info = new JLabel("Info: Le LABEL");
		private static JLabel label_etat = new JLabel("Etat: Déconnecté");
		

		private static JButton bouton_1 = new JButton("Connexion");
		private static JButton bouton_2 = new JButton("Deconnexion");
		private static JButton bouton_3 = new JButton("Mon bouton");
		private static JButton bouton_4 = new JButton("Mon bouton");
		
		static GestionProto GP = new GestionProto();
        static String srvRep;
        static String xmlOut;

        static Socket sockClient;
        static PrintWriter outToServer;
        static BufferedReader inFromServer;
        

		static JFrame fenetre;
		
		public TestJFrame() {

			this.setTitle("Client Annuaire JAVA");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			
			
			JPanel pan = new JPanel();
			
			
			Font police_info = new Font("Tahoma", Font.BOLD, 16);
			label_info.setFont(police_info);
			label_info.setForeground(Color.black);
			
			Font police_etat = new Font("Tahoma", Font.BOLD, 16);
			label_etat.setFont(police_etat);
			label_etat.setForeground(Color.black);
			
			
			bouton_1.addActionListener(this);
			bouton_2.addActionListener(this);
			bouton_3.addActionListener(this);
			bouton_4.addActionListener(this);
			
			pan.add(champ_user);
			pan.add(champ_password);
			pan.add(label_info);
			pan.add(label_etat);
			
			pan.add(bouton_1);
			pan.add(bouton_2);
			bouton_2.setEnabled(false);
			pan.add(bouton_3);
			pan.add(bouton_4);
			this.setContentPane(pan);
			
			this.pack();
			//this.setLocationRelativeTo(null);
			this.setVisible(true);
			
		}
		
		public void actionPerformed(ActionEvent arg0) {
			
			System.out.println(arg0.toString());
			if (arg0.getSource() == bouton_1) {
				//System.out.println(arg0.getSource());
				//JOptionPane.showMessageDialog(null, "test bouton_1 connexion");
			
			
				try {
					String utilisateur;
					String mdp;
					
		
					
					    utilisateur = champ_user.getText();
					    if("".equals(utilisateur)){
					    	JOptionPane.showOptionDialog(
						             null, "Erreur : votre nom d'utilisateur ne peut pas être vide!", 
						             "Erreur nom d'utilisateur", JOptionPane.DEFAULT_OPTION, 
						             JOptionPane.ERROR_MESSAGE, null, null, null);
					    }
		
					    mdp =  champ_password.getText();
					    if("".equals(mdp)){
					    	JOptionPane.showOptionDialog(
						             null, "Erreur : votre mot de passe ne peut pas être vide!", 
						             "Erreur mot de passe", JOptionPane.DEFAULT_OPTION, 
						             JOptionPane.ERROR_MESSAGE, null, null, null);
					    }
		
		
					if ( !"".equals(mdp) && !"".equals(utilisateur)) {
						
						xmlOut = GP.GenererMess("requête", "Connexion",utilisateur ,mdp, "Profession","??");			        
						outToServer.println(xmlOut + Character.toString((char)4));                    
						                    
						String srvRep;
						srvRep = GP.RecevoirMessRzo(inFromServer);
						try {
							Document xmlRep = GP.LireMess(srvRep);
						
						
						System.out.println("srvRep: " + srvRep);       
						    if (xmlRep.getRootElement().getChild("message").getChild("résultat").getText().equals("1") ){
						    	
						        label_etat.setText("Connecté!");
								label_info.setText(champ_user.getText());
								
								bouton_1.setEnabled(false);
								champ_user.setEnabled(false);
								champ_password.setEnabled(false);
								bouton_2.setEnabled(true);
								
						        fenetre.pack();
						        
						        LireXML("Exercice.xml");
						        
						        /* Création du socket ecoute de la messagerie instantannée */
						        
						        //ThreadEcouteurMI leThreadEcouteurMI = new ThreadEcouteurMI("127.0.0.1", 58000);
						        //leThreadEcouteurMI.start();
						        
						    }
						    else{
						    	label_info.setText("Nom d'utilisateur ou mot depasse erroné, recommencez!"); 
						    	champ_user.setText("");
						    	champ_password.setText("");
						    	fenetre.pack();
						    }
						} catch (JDOMException e) {
							JOptionPane.showOptionDialog(
						             null, "Erreur : le message du serveur est invalide. Essayez de vous reconnecter, et aussi avec d'autres identifiants.", 
						             "Erreur du serveur", JOptionPane.DEFAULT_OPTION, 
						             JOptionPane.ERROR_MESSAGE, null, null, null);
							label_info.setText("Nom d'utilisateur ou mot depasse erroné, recommencez!"); 
					    	champ_user.setText("");
					    	champ_password.setText("");
					    	fenetre.pack();
						}
					}
					
					} catch (Exception e) {
						label_info.setText(e.toString());
						fenetre.pack();
						e.printStackTrace();
					}
				
			}
			
			else if (arg0.getSource() == bouton_2) {

				/*
			    JOptionPane.showOptionDialog(
			             null, "bouton_2",
			             "debug", JOptionPane.DEFAULT_OPTION, 
			             JOptionPane.INFORMATION_MESSAGE, null, null, null);
				*/
				try {
					String utilisateur;
					String mdp;
					
		
					
					    utilisateur = champ_user.getText();
					    mdp =  champ_password.getText();
					    
					    /*
					    JOptionPane.showOptionDialog(
					             null, "mdp: " + mdp + ", utilisateur: " + utilisateur,
					             "debug", JOptionPane.DEFAULT_OPTION, 
					             JOptionPane.INFORMATION_MESSAGE, null, null, null);
					     */
						
						xmlOut = GP.GenererMess("requête", "Deconnexion",utilisateur ,mdp, "Profession","??");			        
						outToServer.println(xmlOut + Character.toString((char)4));                    
						                    
						String srvRep;
						System.out.println("avant");
						srvRep = GP.RecevoirMessRzo(inFromServer);
						System.out.println("avant2");
						try {
						Document xmlRep = GP.LireMess(srvRep);
						System.out.println("avant3");
						
						System.out.println("srvRep: " + srvRep);       
						    if (xmlRep.getRootElement().getChild("message").getChild("résultat").getText().equals("1") ){
						    	
						        label_etat.setText("Déonnecté!");
								label_info.setText("");
								
								bouton_1.setEnabled(true);
								champ_user.setEnabled(true);
								champ_password.setEnabled(true);
								bouton_2.setEnabled(false);
								
						        fenetre.pack();
						        
						        LireXML("Exercice.xml");
						        
						        
						    }
						    else{
						    	label_info.setText("Nom d'utilisateur ou mot depasse erroné, recommencez!"); 
						    	champ_user.setText("");
						    	champ_password.setText("");
						    	fenetre.pack();
						    }
						    
				} catch (JDOMException e) {
					JOptionPane.showOptionDialog(
				             null, "Erreur : le message du serveur est invalide. Essayez de vous reconnecter, et de changer les identifiants", 
				             "Erreur du serveur", JOptionPane.DEFAULT_OPTION, 
				             JOptionPane.ERROR_MESSAGE, null, null, null);
					champ_user.setText("");
			    	champ_password.setText("");
			    	fenetre.pack();
				}
					
					
					} catch (Exception e) {
						label_info.setText(e.toString());
						fenetre.pack();
						e.printStackTrace();
					}
			}
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
	    
	    
		

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		fenetre = new TestJFrame();
		
		WindowListener exitListener = new WindowAdapter() {

		    @Override
		    public void windowClosing(WindowEvent e) {
		        
		        try {
					sockClient.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        System.exit(0);
		        
		    }
		};
		fenetre.addWindowListener(exitListener);
        
        
///////////////////////  Création du socket client<->serveur /////////////////////////////
        
try {
	sockClient = new Socket("127.0.0.1", 57000);
	outToServer = new PrintWriter(sockClient.getOutputStream(), true);
	inFromServer = new BufferedReader(new InputStreamReader(sockClient.getInputStream()));
} catch (UnknownHostException e) {
	// TODO Auto-generated catch block
	label_info.setText(e.toString());
	fenetre.pack();
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	label_info.setText(e.toString());
	fenetre.pack();
	e.printStackTrace();
}			

label_info.setText("Communication avec le serveur OK");
fenetre.pack();


/////////////////////////////////////////////////////////////////////////////////////////



    
    // FIN MAIN //
    }

}
