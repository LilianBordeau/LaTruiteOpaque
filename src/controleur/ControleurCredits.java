package controleur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import modele.Constantes;
import vue.PanelSonManager;

public class ControleurCredits extends ControleurBase
{    
   
   
    
    @FXML
    public Label labelTitre;
    
    boolean fullscreen;
  
    
    
    
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {        
       
    
    }
    
    @Override
    public void onAppearing()
    {         
        
        
       

    
    }
    
    @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.sonManager.jouerSon(Constantes.CLIQUEBOUTON);
        navigation.changerVue(ControleurMenuPrincipal.class);
    }
    
     
    
     
   
}
