package controleur;

import java.io.IOException;
import java.net.BindException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import modele.DonneesDebutPartie;
import modele.Joueur;
import modele.JoueurHumain;
import modele.JoueurIAAleatoire;
import modele.JoueurIADifficile;
import modele.JoueurIAMoyenne;
import modele.JoueurReseau;
import modele.JoueurServeur;
import modele.Moteur;
import reseau.Connexion;
import reseau.ConnexionServeur;
import util.Couple;

public class ControleurChoixJoueurs extends ControleurBase
{    
    
    int BLEU = 0;
    int JAUNE = 1;
    int ROUGE = 2;
    int VERT = 3;
    String[] TYPEJOUEURHORSRESEAU = {"JOUEUR", "IA FACILE", "IA MOYENNE", "IA DIFFICILE", "AUCUN"};
    String[] TYPEJOUEURENRESEAU = {"JOUEUR", "IA FACILE", "IA MOYENNE", "IA DIFFICILE", "AUCUN","JOUEUR EN RESEAU"};
    String[] TYPEJOUEUR;
    String[] COULEUR = {"BLEU","JAUNE","ROUGE","VERT"};
    int JOUEURREEL = 0;    
    int IAFACILE = 1;
    int IAMOYENNE = 2;
    int IADIFFICILE = 3;
    int AUCUNJOUEUR = 4;
    int JOUEURRESEAU = 5;
    
    
    int nombreDeJoueurs = 2;
    
    @FXML
    private Button btnCommencer;
    @FXML
    private Label labelMessage;
    
    @FXML
    private Button precedentRouge,suivantRouge,precedentVert,suivantVert,precedentBleu,suivantBleu,precedentJaune,suivantJaune;
    
    @FXML
    private StackPane imagesRouge ,nomRouge,imagesVert,nomVert,imagesBleu,nomBleu,imagesJaune,nomJaune;
    
    HashMap<Button,Couple<Integer,Integer>> deplacements;
    
    StackPane[] imagesJoueur = new StackPane[4];
    
    StackPane[] nomsJoueur = new StackPane[4];
    int[] typesJoueur = new int[4];
   
    int nbJoueursReseauAAttendre;
    int nbJoueursReseau;
    
    public void setJoueur(int couleur, StackPane imagesCouleur,StackPane nomCouleur,int typeJoueur)
    {
        TextField textfield = null;
        Text text = null;
        
        imagesJoueur[couleur] = imagesCouleur; 
        typesJoueur[couleur] = typeJoueur;
        nomsJoueur[couleur] = nomCouleur;
        
        setImage(imagesCouleur,typeJoueur);
        
        if(typeJoueur == JOUEURREEL){
            textfield = (TextField) nomCouleur.getChildren().get(0); 
            textfield.setVisible(true);
            textfield.setAlignment(Pos.CENTER);
            textfield.setText("Joueur " + " " + COULEUR[couleur]);  

            text = (Text) nomCouleur.getChildren().get(1); 
            text.setVisible(false);
        }
        else{
            String message =  TYPEJOUEUR[ typesJoueur[couleur] ];
            nomCouleur.getChildren().get(0).setVisible(false);
            if(typesJoueur[couleur] == IAFACILE || typesJoueur[couleur] == IAMOYENNE || typesJoueur[couleur] == IADIFFICILE )
            { 
                message += " " +  COULEUR[couleur]  ; 
            }
            
            text = (Text) nomCouleur.getChildren().get(1); 
            text.setText(message);
            text.setVisible(true);
        }
     
    }    
    @Override
    public void onAppearing()
    {  
        
        if(navigation.enReseau)
        {
            TYPEJOUEUR = TYPEJOUEURENRESEAU;
            setJoueur(JAUNE,imagesJaune,nomJaune,JOUEURRESEAU);
        }
        else
        {
            TYPEJOUEUR = TYPEJOUEURHORSRESEAU;
            setJoueur(JAUNE,imagesJaune,nomJaune,JOUEURREEL);
        }
        nombreDeJoueurs = 2;
        deplacements =  new HashMap<>();
        deplacements.put(precedentBleu,     new Couple(0,-1));
        deplacements.put(suivantBleu,       new Couple(0,1));
        deplacements.put(precedentJaune,    new Couple(1,-1));
        deplacements.put(suivantJaune,      new Couple(1,1));
        deplacements.put(precedentRouge,    new Couple(2,-1));
        deplacements.put(suivantRouge,      new Couple(2,1));
        deplacements.put(precedentVert,     new Couple(3,-1));
        deplacements.put(suivantVert,       new Couple(3,1));

        
        
        setJoueur(BLEU,imagesBleu,nomBleu,JOUEURREEL);
        setJoueur(ROUGE,imagesRouge,nomRouge,AUCUNJOUEUR);
        setJoueur(VERT,imagesVert,nomVert,AUCUNJOUEUR);
        estPartieConforme();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {     
        
        
        onAppearing();
       
    }
    
    
    public void setImage(StackPane imagesJoueurCouleur, int typeJoueur)
    {
        for(int i =0;i<imagesJoueurCouleur.getChildren().size();i++)
        {
            if( i == typeJoueur)
            {
                imagesJoueurCouleur.getChildren().get(i).setVisible(true);
            }         
            else
            {
                imagesJoueurCouleur.getChildren().get(i).setVisible(false);
            }
        }
    }
    
    
    public void changerJoueur(Event event)
    {
       //QUELLE BOUTON EST CLIQUÉ 
       Button button = (Button) event.getTarget();
     
       Couple<Integer,Integer> deplacement = deplacements.get(button);
       int couleur = deplacement.premier;
       int rotation = deplacement.second;

        changerAffichage(couleur,rotation);
        estPartieConforme();
     
      
    }

    private void changerAffichage(int couleur, int rotation) {
        
        
        int nouveauType =  (typesJoueur[couleur] + rotation) % TYPEJOUEUR.length;
        
        if(nouveauType < 0){ nouveauType = TYPEJOUEUR.length-1;}
        
        setJoueur(couleur,imagesJoueur[couleur],nomsJoueur[couleur],nouveauType);
        
    }
    
    @FXML
    private void clicCommencer(ActionEvent event)
    {
        
        
        
        Joueur[] joueurs = new Joueur[nombreDeJoueurs];
        int i = 0;
        nbJoueursReseauAAttendre = 0;
        for(int j = 0; j < typesJoueur.length;j++)
        {
            
            int typeJoueur  = typesJoueur[j];
            if(typeJoueur != AUCUNJOUEUR)
            {

                
                if(typeJoueur == JOUEURREEL)
                {
                    joueurs[i] = new JoueurHumain();
                    TextField tf = (TextField) nomsJoueur[j].getChildren().get(0);
                    joueurs[i].nom = tf.getText();
                    joueurs[i].couleur = j;

                }
                else if(typeJoueur == IAFACILE)
                {
                   joueurs[i] = new JoueurIAAleatoire();
                    joueurs[i].couleur = j;
                }
                else if(typeJoueur == IAMOYENNE)
                {
                    joueurs[i] = new JoueurIAMoyenne();
                     joueurs[i].couleur = j;
                }
                else if(typeJoueur == IADIFFICILE)
                {
                    joueurs[i] = new JoueurIADifficile();
                     joueurs[i].couleur = j;
                }else if(typeJoueur == JOUEURRESEAU)
                {
                    nbJoueursReseauAAttendre++;
                    joueurs[i] = new JoueurServeur();
                    joueurs[i].couleur = j;
                }
                
                i++;
            }
        }
       
        navigation.moteur = new Moteur(joueurs);
        nbJoueursReseau = nbJoueursReseauAAttendre;
        navigation.moteur.setJoueurs(joueurs);
        if(nbJoueursReseauAAttendre >= 1)
        {            
            int k = 0;
            for(int j = 0 ; j < navigation.moteur.joueurs.length ; j++)
            {
                Joueur joueur = navigation.moteur.joueurs[j];
                if(joueur instanceof JoueurReseau)
                {         
                    final int port = Connexion.PORT+k;
                    Thread thread = new Thread()
                    {
                        
                        @Override
                        public void run()
                        {
                            try
                            {
                                joueur.connexion = new ConnexionServeur(port);
                                joueur.nom = (String)joueur.connexion.readObject();
                                nouveauJoueurConnecte();
                            }                               
                            catch(BindException e)
                            {           
                                Platform.runLater(ControleurChoixJoueurs.this::erreurAdresseDejaUtilisee);
                                /*System.out.println("impossible de se connecter");
                                throw(e);*/
                            }
                            catch(IOException e)
                            {           
                                Platform.runLater(ControleurChoixJoueurs.this::erreurReseau);
                                /*System.out.println("impossible de se connecter");
                                throw(e);*/
                            }
                        }                       
                    };
                    thread.start();
                    k++;
                }
            }
        }
        else
        {
            ControleurJeu controleurJeu = (ControleurJeu)navigation.getController(ControleurJeu.class);
            controleurJeu.lineFantome.setVisible(false);
            navigation.changerVue(ControleurJeu.class);
        }
    }
    
    private synchronized void nouveauJoueurConnecte()
    {
        nbJoueursReseauAAttendre--;
        if(nbJoueursReseauAAttendre == 0)
        {            
            nbJoueursReseauAAttendre = nbJoueursReseau;
            Platform.runLater(new Runnable(){
                @Override
                public void run()
                {
                    for(int j = 0 ; j < navigation.moteur.joueurs.length ; j++)
                    {
                        Joueur joueur = navigation.moteur.joueurs[j];
                        if(joueur instanceof JoueurReseau)
                        {         
                            Thread thread = new Thread()
                            {
                                @Override
                                public void run()
                                {
                                    try
                                    {
                                        joueur.connexion.writeObject(new DonneesDebutPartie(navigation.moteur.plateau, navigation.moteur.joueurs, joueur.numero));
                                        nouveauJoueurPret();
                                    }
                                    catch(IOException e)
                                    {
                                        Platform.runLater(ControleurChoixJoueurs.this::erreurReseau);
                                        /*System.out.println("impossible de se connecter");
                                        throw(e);*/
                                    }
                                }                         
                            };
                            thread.start();
                        }
                    }
                }
            });
        }
    }
    
    private synchronized void nouveauJoueurPret()
    {
        nbJoueursReseauAAttendre--;
        if(nbJoueursReseauAAttendre == 0)
        {  
            Platform.runLater(new Runnable(){
                @Override
                public void run()
                {         
                    ControleurJeu controleurJeu = (ControleurJeu)navigation.getController(ControleurJeu.class);
                    controleurJeu.lineFantome.setVisible(false);
                    navigation.changerVue(ControleurJeu.class);
                }
            });
        }
    }
    
    @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.changerVue(ControleurMenuPrincipal.class);
    }

    private void estPartieConforme() {
        int nbJoueurs = 0;
        for(int i=0; i < typesJoueur.length;i++ )
        {
            if(typesJoueur[i] != AUCUNJOUEUR )
            {
                nbJoueurs++;
            }
        }
        nombreDeJoueurs = nbJoueurs ;
        if(nbJoueurs < 2 ) //nombre de joueur minimum non ok 
        {
            btnCommencer.setDisable(true);
            labelMessage.setVisible(true);
        }else{
            labelMessage.setVisible(false);
            btnCommencer.setDisable(false);
        }
    }
    
    
}
