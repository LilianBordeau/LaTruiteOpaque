package controleur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import modele.Constantes;

public class ControleurCredits extends ControleurBase
{    
    
     
    @FXML
    public Slider sliderMusique,sliderSon;
    
    @FXML
    public ImageView imageMusique,imageSon,screenProportion;

    
    @FXML
    public Label labelTitre;
    
    
    
    
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {        
        labelTitre.setAlignment(Pos.CENTER);
        
        
        sliderMusique.setMin(0);
        sliderMusique.setMax(100);
        sliderMusique.setBlockIncrement(10);
        imageMusique.setImage( new Image("Images/Sons/music_on.png"));

          sliderMusique.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
          //      System.out.println( String.valueOf((int) sliderMusique.getValue()));
                if(sliderMusique.getValue() == 0 )
                {
                    imageMusique.setImage( new Image("Images/Sons/music_off.png"));
                }
                else
                {
                    imageMusique.setImage( new Image("Images/Sons/music_on.png"));
                }
                navigation.sonManager.setVolumeMusique(sliderMusique.getValue());
            }
        });
        
        sliderSon.setMin(0);
        sliderSon.setMax(100);
        sliderSon.setBlockIncrement(10);
        imageSon.setImage( new Image("Images/Sons/volume_faible.png"));
        
        sliderSon.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
               System.out.println( String.valueOf((int) sliderSon.getValue()));
                if(sliderSon.getValue() == 0 )
                {
                    imageSon.setImage( new Image("Images/Sons/volume_off.png"));
                }
                else
                {
                    imageSon.setImage( new Image("Images/Sons/volume_faible.png"));
                }
                navigation.sonManager.setVolumeSon(sliderSon.getValue());
            }
        });
       
        
        screenProportion.setImage(new Image("Images/fullscreen.png"));
    
    }
    
    @Override
    public void onAppearing()
    {        
        sliderMusique.setValue((int)navigation.sonManager.volumeMusique);
        sliderSon.setValue((int)navigation.sonManager.volumeSon);

    
    }
    
    @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.sonManager.jouerSon(Constantes.CLIQUEBOUTON);
        navigation.changerVue(ControleurMenuPrincipal.class);
    }
    
    
    
    @FXML
    public void showSliderMusique(MouseEvent event)
    {
        sliderMusique.setVisible(!sliderMusique.isVisible());
    }
    
       
    
    @FXML
    public void showSliderSon(MouseEvent event)
    {
        sliderSon.setVisible(!sliderSon.isVisible());
    }
    
    @FXML
    public void changeScreenProportion(MouseEvent event)
    {
        navigation.setFullScreen();
    }
     
   
}
