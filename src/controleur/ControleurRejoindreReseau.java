package controleur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modele.DonneesDebutPartie;
import modele.Joueur;
import modele.JoueurClient;
import modele.JoueurHumain;
import modele.JoueurReseau;
import modele.Moteur;
import reseau.Connexion;
import reseau.ConnexionClient;
import reseau.ConnexionServeur;
import java.io.IOException;
import java.net.BindException;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class ControleurRejoindreReseau extends ControleurBase
{    
    @FXML
    public Label labelTitre;
    
    @FXML
    public TextField champIp;
    
    public ConnexionClient connexion;
    public DonneesDebutPartie donneesDebutPartie;
    
    @FXML
    public TextField champNom;
    
    private int nbEchecsConnexion;
    
    public SimpleBooleanProperty enAttente;
    
    @FXML
    public ImageView sablier;
    
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {        
        enAttente = new SimpleBooleanProperty(false);
        sablier.visibleProperty().bind(enAttente);
        RotateTransition transitionSablier = new RotateTransition(Duration.millis(1000), sablier);        
        transitionSablier.setFromAngle(0);
        transitionSablier.setByAngle(359);
        transitionSablier.setCycleCount(Transition.INDEFINITE);
        transitionSablier.play();
        labelTitre.setAlignment(Pos.CENTER);
    }
    
    @Override
    public void onAppearing()
    {
        enAttente.set(false);
        navigation.afficherPopupErreur = true;
    }
    
    @FXML
    private void clicCommencer(ActionEvent event)
    {
        if(!enAttente.get())
        {            
            enAttente.set(true);
            nbEchecsConnexion = 0;
            Thread thread = new Thread()
            {
                @Override
                public void run()
                {
                    for(int j = 0 ; j <= 2 ; j++)
                    {
                        final int port = Connexion.PORT+j;
                        try
                        {
                            connexion = navigation.gestionnaireConnexion.creerConnexionClient();
                            ((ConnexionClient)connexion).connect(champIp.getText(), port, 500);
                            System.out.println("connecte");
                            connexion.writeObject(champNom.getText());
                            donneesDebutPartie = (DonneesDebutPartie)connexion.readObject();
                            if(donneesDebutPartie != null)
                            {
                                Platform.runLater(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if(donneesDebutPartie != null)
                                        {        
                                            enAttente.set(false);
                                            Joueur[] joueurs = new Joueur[donneesDebutPartie.joueurs.length];
                                            for(int i = 0 ; i < joueurs.length ; i++)
                                            {
                                                joueurs[i] = (i == donneesDebutPartie.numJoueur) ? new JoueurHumain() : new JoueurClient(connexion);
                                                joueurs[i].couleur = donneesDebutPartie.joueurs[i].couleur;
                                                joueurs[i].nom = donneesDebutPartie.joueurs[i].nom;
                                            }
                                            navigation.moteur = new Moteur(joueurs);
                                            navigation.moteur.plateau = donneesDebutPartie.plateau;
                                            ControleurJeu controleurJeu = (ControleurJeu)navigation.getController(ControleurJeu.class);
                                            navigation.changerVue(ControleurJeu.class);                                            
                                            controleurJeu.initPartie();
                                        }
                                    }
                                });
                                break;
                            }
                        }
                        catch(IOException e)
                        {
                            System.out.println("impossible de se connecter");
                            if(e instanceof BindException)
                            {
                                System.out.println("BIND EXCEPTION : le port "+port+" est deja utilise");
                            }
                            echecConnexion();
                            //throw(e);
                        }
                    }
                    
                }                       
            };
            thread.start();  
        }            
    }
    
    @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.changerVue(ControleurMenuPrincipal.class);
    }
    
    private synchronized void echecConnexion()
    {
        nbEchecsConnexion++;
        if(nbEchecsConnexion == 3)
        {
            enAttente.set(false);
            Platform.runLater(ControleurRejoindreReseau.this::erreurReseau); 
        }
    }
}
