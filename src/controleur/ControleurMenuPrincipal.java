package controleur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ControleurMenuPrincipal  extends ControleurBase
{    
    @FXML
    public AnchorPane anchorPane;
    
    @FXML
    public Label labelTitre;
    
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {        
        //labelTitre.setAlignment(Pos.CENTER);
    }
    
    @FXML
    private void clicJouer(ActionEvent event)
    {
        navigation.changerVue(ControleurChoixJoueurs.class);
    }
    
    @FXML
    private void clicCreerReseau(ActionEvent event)
    {
        navigation.changerVue(ControleurChoixJoueurs.class);
    }
    
    @FXML
    private void clicRejoindreReseau(ActionEvent event)
    {
        navigation.changerVue(ControleurRejoindreReseau.class);
    }
    
    @FXML
    private void clicChargerPartie(ActionEvent event)
    {
        System.out.print("choisir fichier sauvegarde (classe FileChooser)");
    }
    
    @FXML
    private void clicTutoriel(ActionEvent event)
    {
        navigation.changerVue(ControleurTutoriel.class);
    }
    
    @FXML
    private void clicCredits(ActionEvent event)
    {
        navigation.changerVue(ControleurCredits.class);
    }
}
