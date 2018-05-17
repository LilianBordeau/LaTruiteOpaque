package vue;

import controleur.ControleurBase;
import controleur.ControleurChargerPartie;
import controleur.ControleurChoixJoueurs;
import controleur.ControleurCredits;
import controleur.ControleurFinDePartie;
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
import modele.SonManager;

public class Navigation
{
    public static final String DEBUTNOMSCONTROLEURS = "Controleur";
    public Hashtable<String, Couple<Node,ControleurBase>> vuesEtControleurs;
    private Group noeudRacine;
    public Moteur moteur;
    public SonManager sonManager;
    public Boolean enReseau;

    
    public Navigation(Group noeudRacine)
    {
        enReseau = false;
        sonManager =  new SonManager();
        sonManager.jouerMusique();
        this.noeudRacine = noeudRacine;
        vuesEtControleurs = new Hashtable<>();
        initialiserVue(ControleurChoixJoueurs.class);
        initialiserVue(ControleurJeu.class);
        initialiserVue(ControleurFinDePartie.class);
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
        vueEtControleur.second.onAppearing();
        vueEtControleur.second.onAppearingCommun();
        
        Image image2 = new Image("Images/mouse2.gif");
        noeudRacine.getScene().setCursor(new ImageCursor(image2));
        Stage primaryStage = (Stage) noeudRacine.getScene().getWindow();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent t) {
            t.consume();
        System.out.println("hey");
        ImageView imageExit = new ImageView(new Image("Images/goodbye.gif"));
        ImageView bulle = new ImageView(new Image("Images/goodbyeBulle.png"));
        imageExit.setX(0);
        imageExit.setY(100);
        bulle.setX(300);
        bulle.setY(100);
        
        Rectangle rect = new Rectangle (0, 0, 800, 1);
        Rectangle rect2 = new Rectangle (0, 600, 800, 1);
        
        noeudRacine.getChildren().add(rect);
        noeudRacine.getChildren().add(rect2);
        noeudRacine.getChildren().add(bulle);
        noeudRacine.getChildren().add(imageExit);
        
     
     rect.setFill(Color.BLACK);
     rect2.setFill(Color.BLACK);
 
     ScaleTransition tt = new ScaleTransition(Duration.seconds(0), rect);
     tt.setByY(350f);
     
     ScaleTransition ImageExitTransition = new ScaleTransition(Duration.seconds(0), imageExit);
     TranslateTransition ImageExitTransition2 = new TranslateTransition(Duration.seconds(0), imageExit);
     ImageExitTransition.setByY(0.2f);
     ImageExitTransition.setByX(0.2f);
     
     ImageExitTransition2.setByX(30f);
     ImageExitTransition2.setByY(5f);

     ScaleTransition tt2 = new ScaleTransition(Duration.seconds(0), rect2);
     tt2.setByY(-350f);
       tt.setOnFinished(e -> Platform.exit());
     tt.play();
     tt2.play();
     ImageExitTransition.play();
     ImageExitTransition2.play();

    }
}); 
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
   
}
