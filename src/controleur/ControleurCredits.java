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
    
    boolean fullscreen;
    
    public void initSlider(Slider slider)
    {
        slider.setMin(0);
        slider.setMax(100);
        slider.setBlockIncrement(10);
    }
    
    
    
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {        
        labelTitre.setAlignment(Pos.CENTER);
        
        initSlider(sliderMusique);
        initSlider(sliderSon);

        setImageSlider(imageMusique,Constantes.nomImageSon(Constantes.OFF,Constantes.MUSIQUE),Constantes.nomImageSon(Constantes.ON,Constantes.MUSIQUE),sliderMusique.getValue());
        setImageSlider(imageSon,Constantes.nomImageSon(Constantes.OFF,Constantes.SON),Constantes.nomImageSon(Constantes.ON,Constantes.SON),sliderSon.getValue());
        

        sliderMusique.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
          //      System.out.println( String.valueOf((int) sliderMusique.getValue()));
               setImageSlider(imageMusique,Constantes.nomImageSon(Constantes.OFF,Constantes.MUSIQUE),Constantes.nomImageSon(Constantes.ON,Constantes.MUSIQUE),sliderMusique.getValue());
                navigation.sonManager.setVolumeMusique(sliderMusique.getValue());
            }
        });
               
        sliderSon.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
               System.out.println( String.valueOf((int) sliderSon.getValue()));
                setImageSlider(imageSon,Constantes.nomImageSon(Constantes.OFF,Constantes.SON),Constantes.nomImageSon(Constantes.ON,Constantes.SON),sliderSon.getValue());
                navigation.sonManager.setVolumeSon(sliderSon.getValue());
            }
        });
       
        
        screenProportion.setImage(new Image("Images/fullscreen.png"));
    
    }
    
    @Override
    public void onAppearing()
    {         
        
        
        if(navigation.isFullScreen())
        {
              screenProportion.setImage(new Image("Images/notfullscreen.png"));
        }else{
              screenProportion.setImage(new Image("Images/fullscreen.png"));
        }
        sliderMusique.setVisible(false);
        sliderSon.setVisible(false);

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
        if(navigation.isFullScreen())
        {
              screenProportion.setImage(new Image("Images/notfullscreen.png"));
        }else{
              screenProportion.setImage(new Image("Images/fullscreen.png"));
        }
        
    }

    private void setImageSlider(ImageView image, String imageOff, String imageOn, double value) {
        if(value == 0 )
        {
            image.setImage( new Image(imageOff));
        }
        else
        {
            image.setImage( new Image(imageOn));
        }    
    }
     
   
}
