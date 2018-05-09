package controleur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import vue.Navigation;

public class ControleurBase implements Initializable
{
    public Navigation navigation;
    
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        
    }
    
    
    public void onAppearing()
    {
        
    }
    
}
