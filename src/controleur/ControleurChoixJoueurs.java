package controleur;

import java.io.IOException;
import java.net.BindException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
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
import vue.MyTextField;

public class ControleurChoixJoueurs extends ControleurBase
{    
    
    final int BLEU = 0;
    final int JAUNE = 1;
    final int ROUGE = 2;
    final int VERT = 3;
    final String[] TYPEJOUEURHORSRESEAU = {"JOUEUR", "IA FACILE", "IA MOYENNE", "IA DIFFICILE", "AUCUN"};
    //final String[] TYPEJOUEURENRESEAU = {"JOUEUR", "IA FACILE", "IA MOYENNE", "IA DIFFICILE", "AUCUN","JOUEUR EN RESEAU"};
    String[] typeJoueur;
    final String[] COULEUR = {"VIOLET","JAUNE","ROUGE","VERT"};
    final int JOUEURREEL = 0;    
    final int IAFACILE = 1;
    final int IAMOYENNE = 2;
    final int IADIFFICILE = 3;
    final int AUCUNJOUEUR = 4;
   final  int JOUEURRESEAU = 5;
    
    
    int nombreDeJoueurs = 2;
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private Button btnCommencer;
    
    @FXML
    private Button retour;
    
    @FXML
    private Label labelMessageNbJoueurs;
    @FXML
    private Label labelMessageReseau;
    
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
    public SimpleBooleanProperty enAttente;
    public SimpleBooleanProperty boutonActifSurvole;
    
    @FXML
    public ImageView sablier;
    
    public void setJoueur(int couleur, StackPane imagesCouleur,StackPane nomCouleur,int typeJoueur)
    {
        TextField textfield = null;
        Text text = null;
        
        imagesJoueur[couleur] = imagesCouleur; 
        typesJoueur[couleur] = typeJoueur;
        nomsJoueur[couleur] = nomCouleur;
        
        setImage(imagesCouleur,typeJoueur);
        
        if(typeJoueur == JOUEURREEL)
        {
            textfield = (MyTextField) nomCouleur.getChildren().get(0);
            textfield.setVisible(true);
            textfield.setAlignment(Pos.CENTER);
            textfield.setText("Joueur " + COULEUR[couleur]);
            text = (Text) nomCouleur.getChildren().get(1);
            text.setVisible(false);
        }
        else
        {
            String message =  this.typeJoueur[ typesJoueur[couleur] ];
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
        panelSonManager.imageMusique.changerCurseur = false;
        panelSonManager.imageSon.changerCurseur = false;
        panelSonManager.screen.changerCurseur = false;
        boutonActifSurvole = new SimpleBooleanProperty(false);
        boutonActifSurvole.bind(retour.hoverProperty().or(panelSonManager.screen.hoverProperty()).or(panelSonManager.imageMusique.hoverProperty()).or(panelSonManager.imageSon.hoverProperty()));
        enAttente.addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                precedentRouge.setDisable(newValue);
                suivantRouge.setDisable(newValue);
                precedentVert.setDisable(newValue);
                suivantVert.setDisable(newValue);
                precedentBleu.setDisable(newValue);
                suivantBleu.setDisable(newValue);
                precedentJaune.setDisable(newValue);
                suivantJaune.setDisable(newValue);
                btnCommencer.setDisable(newValue);
                changerCurseur(observable, oldValue, newValue);
            }            
        });
        boutonActifSurvole.addListener(this::changerCurseur);
        enAttente.set(false);
        navigation.afficherPopupErreur = true;        
        if(navigation.enReseau)
        {
            typeJoueur = new String[TYPEJOUEURHORSRESEAU.length+1];
            for(int i = 0 ; i < TYPEJOUEURHORSRESEAU.length ; i++)
            {
                typeJoueur[i] = TYPEJOUEURHORSRESEAU[i];
            }
            typeJoueur[typesJoueur.length-1] = "JOUEUR EN RESEAU";
            setJoueur(JAUNE,imagesJaune,nomJaune,JOUEURRESEAU);
        }
        else
        {
            typeJoueur = TYPEJOUEURHORSRESEAU;
            setJoueur(JAUNE,imagesJaune,nomJaune,JOUEURREEL);
        }
        nombreDeJoueurs = 2;
        deplacements =  new HashMap<>();
        deplacements.put(precedentBleu,     new Couple<>(0,-1));
        deplacements.put(suivantBleu,       new Couple<>(0,1));
        deplacements.put(precedentJaune,    new Couple<>(1,-1));
        deplacements.put(suivantJaune,      new Couple<>(1,1));
        deplacements.put(precedentRouge,    new Couple<>(2,-1));
        deplacements.put(suivantRouge,      new Couple<>(2,1));
        deplacements.put(precedentVert,     new Couple<>(3,-1));
        deplacements.put(suivantVert,       new Couple<>(3,1));

        
        
        setJoueur(BLEU,imagesBleu,nomBleu,JOUEURREEL);
        setJoueur(ROUGE,imagesRouge,nomRouge,AUCUNJOUEUR);
        setJoueur(VERT,imagesVert,nomVert,AUCUNJOUEUR);
        estPartieConforme();
    }
    
    public void changerCurseur(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
    {
                Scene scene = anchorPane.getScene();
                if(scene != null)
                {
                    scene.setCursor(boutonActifSurvole.get() ? Cursor.HAND : (sablier.visibleProperty().get() ? Cursor.WAIT : Cursor.DEFAULT));
                }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {     
        
        enAttente = new SimpleBooleanProperty(false);
        sablier.visibleProperty().bind(enAttente);
        RotateTransition transitionSablier = new RotateTransition(Duration.millis(2000), sablier);        
        transitionSablier.setFromAngle(0);
        transitionSablier.setByAngle(359);
        transitionSablier.setCycleCount(Transition.INDEFINITE);
        transitionSablier.play();
        int maxlength = 1+longueurChaineMaxDuTableau(TYPEJOUEURHORSRESEAU)+longueurChaineMaxDuTableau(COULEUR);
        for(int i = 0 ; i < 4 ; i++)
        {
            ((MyTextField)anchorPane.lookup("#champNomJoueur"+i)).maxlength = maxlength;
        }
        //onAppearing();
       
    }
    
    private int longueurChaineMaxDuTableau(String[] chaines)
    {
        return Arrays.asList(chaines).stream().map(x -> x.length()).max(Comparator.<Integer>naturalOrder()).get();
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
        if(!enAttente.get())
        {
            //QUELLE BOUTON EST CLIQUÉ 
            Button button = (Button) event.getTarget();

            Couple<Integer,Integer> deplacement = deplacements.get(button);
            int couleur = deplacement.premier;
            int rotation = deplacement.second;

             changerAffichage(couleur,rotation);
             estPartieConforme();
        }
    }

    private void changerAffichage(int couleur, int rotation) {
        
        
        int nouveauType =  (typesJoueur[couleur] + rotation) % typeJoueur.length;
        
        if(nouveauType < 0){ nouveauType = typeJoueur.length-1;}
        
        setJoueur(couleur,imagesJoueur[couleur],nomsJoueur[couleur],nouveauType);
        
    }
    
    @FXML
    private void clicCommencer(ActionEvent event)
    {
        
        
        if(!enAttente.get())
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

                    }
                    else if(typeJoueur == JOUEURRESEAU)
                    {
                        nbJoueursReseauAAttendre++;
                        joueurs[i] = new JoueurServeur();
                    }else{
                        Text label = (Text) nomsJoueur[j].getChildren().get(1);
                        if(typeJoueur == IAFACILE)
                        {
                            joueurs[i] = new JoueurIAAleatoire();
                        }else if(typeJoueur == IAMOYENNE)
                        {
                            joueurs[i] = new JoueurIAMoyenne();
                        }
                        else if(typeJoueur == IADIFFICILE)
                        {
                            joueurs[i] = new JoueurIADifficile();
                        } 
                        joueurs[i].nom = label.getText();
                    }
                   
                    joueurs[i].couleur = j;
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
                                    joueur.connexion = navigation.gestionnaireConnexion.creerConnexionServeur(port);
                                    ((ConnexionServeur)joueur.connexion).accept();
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
                        enAttente.set(true);
                        thread.start();
                        k++;
                    }
                }
            }
            else
            {
                ControleurJeu controleurJeu = (ControleurJeu)navigation.getController(ControleurJeu.class);
                enAttente.set(false);
                navigation.changerVue(ControleurJeu.class);                
                controleurJeu.initPartie();
            }
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
                    enAttente.set(false);
                    navigation.changerVue(ControleurJeu.class);                    
                    controleurJeu.initPartie();
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
        boolean queDesJoueursReseau = true;
        for(int i=0; i < typesJoueur.length;i++ )
        {
            if(typesJoueur[i] != AUCUNJOUEUR )
            {
                nbJoueurs++;
                if(typesJoueur[i] != JOUEURRESEAU)
                {
                    queDesJoueursReseau = false;
                }
            }
        }
        nombreDeJoueurs = nbJoueurs ;
        if(nbJoueurs < 2 || queDesJoueursReseau) // pas assez de joueurs ou que des joueurs en reseau
        {
            btnCommencer.setDisable(true);
            labelMessageNbJoueurs.setVisible(true);
            if(queDesJoueursReseau)
            {                
                labelMessageReseau.setVisible(true);
            }
        }else{
            labelMessageNbJoueurs.setVisible(false);
            labelMessageReseau.setVisible(false);
            btnCommencer.setDisable(false);
        }
    }
    
    
}
