package vue;

import controleur.ControleurBase;
import controleur.ControleurChoixJoueurs;
import controleur.ControleurCredits;
import controleur.ControleurJeu;
import controleur.ControleurMenuPrincipal;
import controleur.ControleurRejoindreReseau;
import controleur.ControleurTutoriel;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.stage.Screen;
import javafx.stage.Stage;
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
    public Navigation(Group noeudRacine)
    {
        this.noeudRacine = noeudRacine;
        Joueur[] joueurs = new Joueur[]{new JoueurHumain(), new JoueurHumain(), new JoueurHumain(), new JoueurHumain()};
        moteur = new Moteur(joueurs);
        vuesEtControleurs = new Hashtable<>();
        initialiserVue(ControleurChoixJoueurs.class);
        initialiserVue(ControleurJeu.class);
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
    
    public static String nomFichierFXML(Class<?> classeControleur)
    {
        String nomControleur = classeControleur.getSimpleName();
        String resultat = /*"vue/"+*/nomControleur.substring(DEBUTNOMSCONTROLEURS.length(), nomControleur.length())+".fxml";
        //System.out.println(resultat);
        return resultat;
    }
    
    public void changerVue(Class<? extends ControleurBase> classeControleur)
    {
        
        Couple<Node,ControleurBase> vueEtControleur = vuesEtControleurs.get(classeControleur.getSimpleName()); 
        vueEtControleur.second.onAppearing();
        noeudRacine.getChildren().clear();
        noeudRacine.getChildren().add(vueEtControleur.premier);
       
        
    }
    
   public void setFullScreen()
   {
              Stage stage = (Stage) noeudRacine.getScene().getWindow();
              stage.setFullScreen(!stage.isFullScreen());
   }
}
