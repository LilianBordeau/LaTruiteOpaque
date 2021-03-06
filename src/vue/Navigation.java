package vue;

import controleur.ControleurBase;
import controleur.ControleurChargerPartie;
import controleur.ControleurChoixJoueurs;
import controleur.ControleurCredits;
import controleur.ControleurJeu;
import controleur.ControleurMenuPrincipal;
import controleur.ControleurRejoindreReseau;
import controleur.ControleurSauvegarderPartie;
import controleur.ControleurTutoriel;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import util.Couple;
import modele.Joueur;
import modele.JoueurHumain;
import modele.Moteur;
import controleur.SonManager;
import reseau.GestionnaireConnexion;

public class Navigation
{
    public static final String DEBUTNOMSCONTROLEURS = "Controleur";
    public Hashtable<String, Couple<Node,ControleurBase>> vuesEtControleurs;
    private Group noeudRacine;
    public Moteur moteur;
    public SonManager sonManager;
    public Boolean enReseau;
    public GestionnaireConnexion gestionnaireConnexion;
    public boolean afficherPopupErreur;
    public static boolean estEnModeDebug = false;
    
    public Navigation(Group noeudRacine)
    {        
        gestionnaireConnexion = new GestionnaireConnexion();
        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) -> {
            e.printStackTrace();
            fermerToutesLesConnexions();
            Platform.exit();
        });
        afficherPopupErreur = true;
        enReseau = false;
        sonManager =  new SonManager();
        sonManager.jouerMusique();
        this.noeudRacine = noeudRacine;
        vuesEtControleurs = new Hashtable<>();
        initialiserVue(ControleurChoixJoueurs.class);
        initialiserVue(ControleurJeu.class);
        initialiserVue(ControleurChargerPartie.class);
        initialiserVue(ControleurSauvegarderPartie.class);
        initialiserVue(ControleurMenuPrincipal.class);
        initialiserVue(ControleurRejoindreReseau.class);
        initialiserVue(ControleurTutoriel.class);
        initialiserVue(ControleurCredits.class);
    }
    
    private void initialiserVue(Class<? extends ControleurBase> classeControleur) 
    {
        URL fxmlURL = getClass().getResource(nomFichierFXML(classeControleur));  
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
        try {      
            ControleurBase controleur = classeControleur.newInstance();
            controleur.navigation = this;
            fxmlLoader.setController(controleur);
            Node vue = fxmlLoader.load(); 
            vuesEtControleurs.put(classeControleur.getSimpleName(), new Couple<>(vue, fxmlLoader.getController()));
        } catch (InstantiationException|IllegalAccessException|IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static String nomFichierFXML(Class<? extends ControleurBase> classeControleur)
    {
        String nomControleur = classeControleur.getSimpleName();
        String resultat = /*"vue/"+*/nomControleur.substring(DEBUTNOMSCONTROLEURS.length(), nomControleur.length())+".fxml";
        //System.out.println(resultat);
        return resultat;
    }
    
    public void changerVue(Class<? extends ControleurBase> classeControleur)
    {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(750));
        Couple<Node,ControleurBase> vueEtControleur = vuesEtControleurs.get(classeControleur.getSimpleName());
        fadeTransition.setNode(noeudRacine);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        noeudRacine.getChildren().clear();
        noeudRacine.getChildren().add(vueEtControleur.premier);        
        vueEtControleur.second.onAppearingCommun();  
        vueEtControleur.second.onAppearing();     
    }
    
    public void setFullScreen()
    {
               Stage stage = (Stage) noeudRacine.getScene().getWindow();
               stage.setFullScreen(!stage.isFullScreen());
    }
   
   
   public boolean isFullScreen()
   {
        Stage stage = (Stage) noeudRacine.getScene().getWindow();
        return stage.isFullScreen();
   }
   
   public ControleurBase getController(Class<? extends ControleurBase> classeControleur)
   {
       return vuesEtControleurs.get(classeControleur.getSimpleName()).second;
       
   }
   
   public void fermerToutesLesConnexions()
   {
       afficherPopupErreur = false;
       gestionnaireConnexion.fermerToutesLesConnexions();
   }
   
}
