package controleur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class ControleurRejoindreReseau extends ControleurBase
{    
    @FXML
    public Label labelTitre;
    
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {        
        labelTitre.setAlignment(Pos.CENTER);
    }
    
    @FXML
    private void clicCommencer(ActionEvent event)
    {
        navigation.changerVue(ControleurJeu.class);
    }
    
    @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.changerVue(ControleurMenuPrincipal.class);
    }
}
