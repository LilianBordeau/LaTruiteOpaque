package controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ControleurChoixJoueurs extends ControleurBase
{    
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
