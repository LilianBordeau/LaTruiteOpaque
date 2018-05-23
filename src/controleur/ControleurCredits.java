package controleur;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import modele.Constantes;
import vue.PanelSonManager;

import java.time.Instant;
import javafx.scene.input.KeyCode;

public class ControleurCredits extends ControleurBase
{    
   
    @FXML
    public AnchorPane anchorPane;
    
    @FXML
    public Label labelTitre;
    
    @FXML
    private Label labelCredits;
    
    @FXML
    private ImageView remerciements;
    
    @FXML
    private ImageView equipe;
    
    @FXML
    private Label membre1;
    @FXML
    private Label membre2;
    @FXML
    private Label membre3;
    @FXML
    private Label membre4;
    @FXML
    private Label membre5;
    @FXML
    private Label membre6;
    
    @FXML
    private Button boutonCredits;
    
    boolean fullscreen;
    boolean credits_equipe = true;
  

    
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {        
       
    
    }
    
    @Override
    public void onAppearing()
    {   
        membre1.setOpacity(0);
        membre2.setOpacity(0);
        membre3.setOpacity(0);
        membre4.setOpacity(0);
        membre5.setOpacity(0);
        membre6.setOpacity(0);
        
        membre1.setText("Adel Mouss\nPeintre architecte");
        membre2.setText("Quentin Desbrus\nChef des effets spéciaux");
        membre3.setText("Amine Tagui\nArtisan du bois");
        membre4.setText("Maxence Bleton-Giordano\nInterfaceur codeur");
        membre5.setText("Lilian Bordeau\nSculpteur de pingouins");
        membre6.setText("Justin Kossonogow\nGénie du réseau");
        
        equipe.setOpacity(0);
        labelCredits.setText("Il y a très très longtemps, sur une banquise très très lointaine...\n"
                + "Un jeu fut forgé par Alvydas Jakeliunas et Günter Cornett. Un jeu pour les gouverner tous. "
                + "En l'an de grâce 2006, la légendaire maison d'édition Phalanx Games décide d'en faire un jeu de plateau. "
                + "Douze ans plus tard, alors que tout espoir est perdu, une jeune équipe d'irréductibles codeurs décide de s'attaquer à l'impossible : "
                + "retranscrire ce chef-d'oeuvre dans la matrice. "
                + "Dans cette quête ils furent accompagnés par Thomas Lavocat qui les aida grandement.\n"
                + "Cette page raconte leur histoire...");

    
    }
    @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.sonManager.cliqueBouton();
        navigation.changerVue(ControleurMenuPrincipal.class);
    }
        
    @FXML
    private void suivantMenu(ActionEvent event)
    {
        if(credits_equipe)
        {
         //labelCredits.setVisible(false);
         //remerciements.setVisible(false);
         //equipe.setVisible(true);
         credits_equipe = false;
         boutonCredits.setText("Histoire");
         
         for(int i = 1; i <= 6; i++)
         {
         Label membre = (Label) anchorPane.lookup("#membre"+i);
         FadeTransition fadeMembre = new FadeTransition(Duration.seconds(0.5), membre);
         fadeMembre.setInterpolator(Interpolator.EASE_BOTH);
         fadeMembre.setCycleCount(1);
         fadeMembre.setAutoReverse(false);
         fadeMembre.setFromValue(0);
         fadeMembre.setToValue(1);
         fadeMembre.play();
         }
         
         FadeTransition fadeRemerciements = new FadeTransition(Duration.seconds(0.5), remerciements);
         fadeRemerciements.setInterpolator(Interpolator.EASE_BOTH);
         fadeRemerciements.setCycleCount(1);
         fadeRemerciements.setAutoReverse(false);
         fadeRemerciements.setFromValue(1);
         fadeRemerciements.setToValue(0);
         
         FadeTransition fadeLabel = new FadeTransition(Duration.seconds(0.5), labelCredits);
         fadeLabel.setInterpolator(Interpolator.EASE_BOTH);
         fadeLabel.setCycleCount(1);
         fadeLabel.setAutoReverse(false);
         fadeLabel.setFromValue(1);
         fadeLabel.setToValue(0);
         
         FadeTransition fadeEquipe = new FadeTransition(Duration.seconds(0.5), equipe);
         fadeEquipe.setInterpolator(Interpolator.EASE_BOTH);
         fadeEquipe.setCycleCount(1);
         fadeEquipe.setAutoReverse(false);
         fadeEquipe.setFromValue(0);
         fadeEquipe.setToValue(1);
         
         fadeLabel.play();
         fadeEquipe.play();
         fadeRemerciements.play();
        }
        else
        {
         //labelCredits.setVisible(true);
         //remerciements.setVisible(true);
         //equipe.setVisible(false);
         credits_equipe = true;
         boutonCredits.setText("La communauté");
         
         for(int i = 1; i <= 6; i++)
         {
         Label membre = (Label) anchorPane.lookup("#membre"+i);
         FadeTransition fadeMembre = new FadeTransition(Duration.seconds(0.5), membre);
         fadeMembre.setInterpolator(Interpolator.EASE_BOTH);
         fadeMembre.setCycleCount(1);
         fadeMembre.setAutoReverse(false);
         fadeMembre.setFromValue(1);
         fadeMembre.setToValue(0);
         fadeMembre.play();
         }
         
         FadeTransition fadeRemerciements = new FadeTransition(Duration.seconds(0.5), remerciements);
         fadeRemerciements.setInterpolator(Interpolator.EASE_BOTH);
         fadeRemerciements.setCycleCount(1);
         fadeRemerciements.setAutoReverse(false);
         fadeRemerciements.setFromValue(0);
         fadeRemerciements.setToValue(1);
         
         FadeTransition fadeLabel = new FadeTransition(Duration.seconds(0.5), labelCredits);
         fadeLabel.setInterpolator(Interpolator.EASE_BOTH);
         fadeLabel.setCycleCount(1);
         fadeLabel.setAutoReverse(false);
         fadeLabel.setFromValue(0);
         fadeLabel.setToValue(1);
         
         FadeTransition fadeEquipe = new FadeTransition(Duration.seconds(0.5), equipe);
         fadeEquipe.setInterpolator(Interpolator.EASE_BOTH);
         fadeEquipe.setCycleCount(1);
         fadeEquipe.setAutoReverse(false);
         fadeEquipe.setFromValue(1);
         fadeEquipe.setToValue(0);
         
         fadeLabel.play();
         fadeEquipe.play();
         fadeRemerciements.play();
        }
    }
     
    
     
   
}
