package controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ControleurCredits extends ControleurBase
{    
    @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.changerVue(ControleurMenuPrincipal.class);
    }
}
