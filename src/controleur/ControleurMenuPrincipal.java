package controleur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ControleurMenuPrincipal  extends ControleurBase
{    
    @FXML
    public AnchorPane anchorPane;
    
    @FXML
    public Label labelTitre;
    
    @FXML
    public ImageView logo;
    
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {        
        //labelTitre.setAlignment(Pos.CENTER);
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.2),logo);
        
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(3), logo);
        
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);
        rotateTransition.setCycleCount(10);
        rotateTransition.setOnFinished(e -> rotation2());
        
        
        
        scaleTransition.setByX(2f);
        scaleTransition.setByY(2f);
        rotateTransition.play();
        scaleTransition.play();
    }
    
    @FXML
    private void clicJouer(ActionEvent event)
    {
        navigation.enReseau = false;
        navigation.changerVue(ControleurChoixJoueurs.class);
    }
    
    @FXML
    private void clicCreerReseau(ActionEvent event)
    {
        navigation.enReseau = true;
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
        navigation.changerVue(ControleurChargerPartie.class);
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
    
    private void rotation2()
    {
        RotateTransition rotateTransition2 = new RotateTransition(Duration.seconds(1),logo);
        ScaleTransition scaleTransition2 = new ScaleTransition(Duration.seconds(1), logo);
        rotateTransition2.setFromAngle(-5);
        rotateTransition2.setToAngle(5);
        rotateTransition2.setCycleCount(100);
        rotateTransition2.setAutoReverse(true);
        scaleTransition2.setByX(0.3f);
        scaleTransition2.setByY(0.3f);
        scaleTransition2.setAutoReverse(true);
        scaleTransition2.setCycleCount(100);
        scaleTransition2.play();
        rotateTransition2.play();
        
    }
}
