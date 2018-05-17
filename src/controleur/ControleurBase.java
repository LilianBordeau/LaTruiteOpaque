package controleur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Constantes;
import vue.Navigation;
import vue.PanelSonManager;

public abstract class ControleurBase implements Initializable
{
    public Navigation navigation;
    
     @FXML
    public PanelSonManager panelSonManager;
     
     
       
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
        
        initSlider(panelSonManager.sliderMusique);
        initSlider(panelSonManager.sliderSon);

       
        panelSonManager.background.setImage(new Image("Images/soundboard.png"));
        panelSonManager.screen.setGraphic(new ImageView("Images/fullscreen.png"));
        //panelSonManager.screen.setImage());
    }
    
    
    public final void onAppearingCommun()
    {
        panelSonManager.sliderMusique.setValue((int)navigation.sonManager.volumeMusique);
        panelSonManager.sliderSon.setValue((int)navigation.sonManager.volumeSon);
        
        panelSonManager.imageMusique.setOnMouseClicked(this::showSliderMusique);
        panelSonManager.imageSon.setOnMouseClicked(this::showSliderSon);
        panelSonManager.screen.setOnMouseClicked(this::changeScreenProportion);
        
        setImageSlider(panelSonManager.imageMusique,Constantes.nomImageSon(Constantes.OFF,Constantes.MUSIQUE),Constantes.nomImageSon(Constantes.ON,Constantes.MUSIQUE),panelSonManager.sliderMusique.getValue());
        setImageSlider(panelSonManager.imageSon,Constantes.nomImageSon(Constantes.OFF,Constantes.SON),Constantes.nomImageSon(Constantes.ON,Constantes.SON),panelSonManager.sliderSon.getValue());
     
        
        panelSonManager.sliderMusique.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
          //      System.out.println( String.valueOf((int) sliderMusique.getValue()));
               setImageSlider(panelSonManager.imageMusique,Constantes.nomImageSon(Constantes.OFF,Constantes.MUSIQUE),Constantes.nomImageSon(Constantes.ON,Constantes.MUSIQUE),panelSonManager.sliderMusique.getValue());
                navigation.sonManager.setVolumeMusique(panelSonManager.sliderMusique.getValue());
            }
        });
               
        panelSonManager.sliderSon.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
               System.out.println( String.valueOf((int) panelSonManager.sliderSon.getValue()));
                setImageSlider(panelSonManager.imageSon,Constantes.nomImageSon(Constantes.OFF,Constantes.SON),Constantes.nomImageSon(Constantes.ON,Constantes.SON),panelSonManager.sliderSon.getValue());
                navigation.sonManager.setVolumeSon(panelSonManager.sliderSon.getValue());
            }
        });
        
        
       
        Image img;
        if(navigation.isFullScreen())
        {       
            img  = new Image("Images/notfullscreen.png");
            
        }else{
            img  = new Image("Images/fullscreen.png");            
        }
        
        
        ImageView image = new ImageView();
        image.setFitHeight(30); 
        image.setFitWidth(30);
        image.setImage(img);
        panelSonManager.screen.setGraphic(image);
        
        panelSonManager.background.setImage(new Image("Images/soundboard.png"));
        panelSonManager.sliderMusique.setVisible(false);
        panelSonManager.sliderSon.setVisible(false);

     
    }
    
     public void onAppearing()
    {
       
    }
    
    
     
    public void showSliderMusique(MouseEvent event)
    {
        panelSonManager.sliderMusique.setVisible(!panelSonManager.sliderMusique.isVisible());
    }
    
       
    
   
    public void showSliderSon(MouseEvent event)
    {
        panelSonManager.sliderSon.setVisible(!panelSonManager.sliderSon.isVisible());
    }
    
  
    public void changeScreenProportion(MouseEvent event)
    {
        navigation.setFullScreen();
          Image img;
        if(navigation.isFullScreen())
        {       
            img  = new Image("Images/notfullscreen.png");
            
        }else{
            img  = new Image("Images/fullscreen.png");            
        }
        
        
        ImageView image = new ImageView();
        image.setFitHeight(30); 
        image.setFitWidth(30);
        image.setImage(img);
        panelSonManager.screen.setGraphic(image);
        
    }

    private void setImageSlider(Button btn, String imageOff, String imageOn, double value) {
        Image img;
        if(value == 0 )
        {
           img = new Image(imageOff);
        }
        else
        {
            img = new Image(imageOn);
        }    
        
        ImageView image = new ImageView();
        image.setFitHeight(30); 
        image.setFitWidth(30);
        image.setImage(img);
        
        btn.setGraphic(image);
    }
    
     protected void erreur(String message)
    {
        navigation.changerVue(ControleurMenuPrincipal.class);
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(message));
        Scene dialogScene = new Scene(dialogVbox, 350, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    
    protected void erreurReseau()
    {
        erreur("Connexion perdue");
    }
    
    protected void erreurAdresseDejaUtilisee()
    {
        erreur("Un thread utilise la même adresse IP que ce jeu");
    }
    
}
