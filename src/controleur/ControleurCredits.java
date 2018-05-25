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
import javafx.scene.paint.Color;

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
    private Label membre1_metier;
    @FXML
    private Label membre2_metier;
    @FXML
    private Label membre3_metier;
    @FXML
    private Label membre4_metier;
    @FXML
    private Label membre5_metier;
    @FXML
    private Label membre6_metier;
    
    @FXML
    private Button boutonCredits;
    
    boolean fullscreen;
    boolean credits_equipe;
  
    
    int keyA;
    int keyB;
    int key5;
    int key$;
    boolean sexyMove;

    
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {        
       
    
    }
    
    @Override
    public void onAppearing()
    {   
        
        credits_equipe = true;
        sexyMove = true;
        
        membre1.setOpacity(0);
        membre2.setOpacity(0);
        membre3.setOpacity(0);
        membre4.setOpacity(0);
        membre5.setOpacity(0);
        membre6.setOpacity(0);        
        membre1_metier.setOpacity(0);
        membre2_metier.setOpacity(0);
        membre3_metier.setOpacity(0);
        membre4_metier.setOpacity(0);
        membre5_metier.setOpacity(0);
        membre6_metier.setOpacity(0);
        
        remerciements.setOpacity(1);
        labelCredits.setOpacity(1);
        boutonCredits.setText("La communauté");
        
        membre1.setText("Adel Mouss");
        membre1.setTextFill(Color.valueOf("d80000"));
        membre2.setText("Quentin Desbrus");
        membre2.setTextFill(Color.valueOf("9e20c6"));
        membre3.setText("Amine Tagui");
        membre3.setTextFill(Color.valueOf("381ec6"));
        membre4.setText("Maxence Bleton-Giordano");
        membre4.setTextFill(Color.valueOf("ffe100"));
        membre5.setText("Lilian Bordeau");
        membre5.setTextFill(Color.valueOf("36a525"));
        membre6.setText("Justin Kossonogow");
        membre6.setTextFill(Color.valueOf("1bbcd0"));
        
        membre1_metier.setText("Peintre architecte");
        membre2_metier.setText("Chef des effets spéciaux");
        membre3_metier.setText("Artisan du bois");
        membre4_metier.setText("Interfaceur codeur");
        membre5_metier.setText("Sculpteur de pingouins");
        membre6_metier.setText("Seigneur des réseaux");
        
        equipe.setOpacity(0);
        labelCredits.setText("Il y a très très longtemps, sur une banquise très très lointaine...\n"
                + "Un jeu fut forgé par Alvydas Jakeliunas et Günter Cornett. Un jeu pour les gouverner tous. "
                + "En l'an de grâce 2006, la légendaire maison d'édition Phalanx Games décide d'en faire un jeu de plateau. "
                + "Douze ans plus tard, alors que tout espoir est perdu, une jeune équipe d'irréductibles codeurs décide de s'attaquer à l'impossible : "
                + "retranscrire ce chef-d'oeuvre dans la matrice. "                
                + "Cette page raconte leur histoire...\n"
                + "Remerciements à notre tuteur, Thomas Lavocat.");
    
        
        anchorPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                Timestamp tm =  new Timestamp(System.currentTimeMillis());
                
                if(event.getCode() == KeyCode.A)
                {
                    keyA  = tm.getNanos();
                    sexyMove = true;
                }
                
                if(event.getCode() == KeyCode.B)
                {   
                    keyB  = tm.getNanos();
                    int diff = keyB - keyA;
                    System.out.println("!ok"+diff);

                    if(sexyMove &&  diff < 250000000 && diff > 0)
                    {
                        System.out.println("ok");
                      sexyMove = true;  
                    }else
                    {
                      sexyMove = false;  
                    }
                    
                }
                  
                if(event.getCode() == KeyCode.SOFTKEY_5 || event.getCode() == KeyCode.NUMPAD5 )
                {
                    key5  = tm.getNanos();
                    int diff = key5 - keyB;
                    System.out.println("!ok2 " + diff);

                    if(sexyMove &&  diff < 1000000000 && diff > 500000000)
                    {
                        System.out.print("ok2");
                      sexyMove = true;  
                      afficherImage();
                    }else
                    {
                      sexyMove = false;  
                    }
                }
                /*    
                if(event.getCode() == KeyCode.DOLLAR)
                {
                    key$  = tm.getNanos();
                }
                */

            }
        });
        
    }
    
    
    private void  afficherImage() {

        Image img = new Image("Images/neige.gif");
        ImageView imageView = new ImageView(img);
        imageView.setLayoutX(0);
        imageView.setLayoutY(0);
        imageView.setFitHeight(600);
        imageView.setFitWidth(800);
        anchorPane.getChildren().add(imageView);
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
         Label membre_metier = (Label) anchorPane.lookup("#membre"+i+"_metier");
         
         FadeTransition fadeMembre = new FadeTransition(Duration.seconds(0.5), membre);
         fadeMembre.setInterpolator(Interpolator.EASE_BOTH);
         fadeMembre.setCycleCount(1);
         fadeMembre.setAutoReverse(false);
         fadeMembre.setFromValue(0);
         fadeMembre.setToValue(1);
         
         FadeTransition fadeMembreMetier = new FadeTransition(Duration.seconds(0.5), membre_metier);
         fadeMembreMetier.setInterpolator(Interpolator.EASE_BOTH);
         fadeMembreMetier.setCycleCount(1);
         fadeMembreMetier.setAutoReverse(false);
         fadeMembreMetier.setFromValue(0);
         fadeMembreMetier.setToValue(1);
         
         fadeMembreMetier.play();
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
         Label membre_metier = (Label) anchorPane.lookup("#membre"+i+"_metier");
         
         FadeTransition fadeMembre = new FadeTransition(Duration.seconds(0.5), membre);
         fadeMembre.setInterpolator(Interpolator.EASE_BOTH);
         fadeMembre.setCycleCount(1);
         fadeMembre.setAutoReverse(false);
         fadeMembre.setFromValue(1);
         fadeMembre.setToValue(0);
         
         FadeTransition fadeMembreMetier = new FadeTransition(Duration.seconds(0.5), membre_metier);
         fadeMembreMetier.setInterpolator(Interpolator.EASE_BOTH);
         fadeMembreMetier.setCycleCount(1);
         fadeMembreMetier.setAutoReverse(false);
         fadeMembreMetier.setFromValue(1);
         fadeMembreMetier.setToValue(0);
         
         fadeMembreMetier.play();
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
