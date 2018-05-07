package controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ControleurRejoindreReseau extends ControleurBase
{    
    @FXML
    private void clicCommencer(ActionEvent event)
    {
        navigation.changerVue(ControleurJeu.class);
    }
}
