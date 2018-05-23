import controleur.ControleurChoixJoueurs;
import controleur.ControleurMenuPrincipal;
import java.io.IOException;
import java.net.BindException;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import modele.DonneesDebutPartie;
import modele.Joueur;
import modele.JoueurReseau;
import reseau.ConnexionServeur;
import vue.Navigation;

public class Main extends Application {
    private static final double SCREENRATIOWIDTHINIT = 0.7;
    private static final double SCREENRATIOHEIGHTINIT = SCREENRATIOWIDTHINIT;      
    public static final int LARGEURFENETREINIT = (int)(Screen.getPrimary().getVisualBounds().getWidth()*SCREENRATIOWIDTHINIT);
    public static final int HAUTEURFENETREINIT = (int)(Screen.getPrimary().getVisualBounds().getHeight()*SCREENRATIOHEIGHTINIT);
    private static final int LARGEURFENETREFXML = 800;
    private static final int HAUTEURFENETREFXML = 600;
    private Navigation navigation;
    @Override
    public void start(Stage primaryStage) {
        
        
        AnchorPane anchorPane = new AnchorPane();  
        Group noeudRacine = new Group(anchorPane);       
        Scene scene = new Scene(noeudRacine);
        primaryStage.setScene(scene);
        
     
        anchorPane.setPrefWidth(LARGEURFENETREFXML);
        anchorPane.setPrefHeight(HAUTEURFENETREFXML);
       

         scene.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {
            double newWidthDouble = newSceneWidth.doubleValue();
            TranslateTransition translate = new TranslateTransition(Duration.millis(1), noeudRacine);
            translate.setToX((newWidthDouble-LARGEURFENETREFXML)/2);
            ScaleTransition scale = new ScaleTransition(Duration.millis(1), noeudRacine);
            scale.setToX(newWidthDouble/LARGEURFENETREFXML);
            ParallelTransition transition = new ParallelTransition(noeudRacine, scale, translate);
            transition.play();
        });
        scene.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) -> {
            double newHeightDouble = newSceneHeight.doubleValue();
            TranslateTransition translate = new TranslateTransition(Duration.millis(1), noeudRacine);
            translate.setToY((newHeightDouble-HAUTEURFENETREFXML)/2.0);
            ScaleTransition scale = new ScaleTransition(Duration.millis(1), noeudRacine);
            scale.setToY(newHeightDouble/HAUTEURFENETREFXML);
            ParallelTransition transition = new ParallelTransition(noeudRacine, scale, translate);
            transition.play();
        });
  
        
        
        ImageView sablier = new ImageView(new Image("Images/sablier.png"));
        sablier.setFitHeight(150);
        sablier.setFitWidth(150);
        sablier.setLayoutX(325);
        sablier.setLayoutY(215);
        
        ImageView panneau = new ImageView(new Image("Images/panneauPlacementDeplacement.png"));
        panneau.setLayoutX(250);
        panneau.setLayoutY(400);
        panneau.setFitHeight(75);
        panneau.setFitWidth(300);
        
        Label message = new Label("Pêchage de truites en cours ...");
        message.setFont(Font.font(null,FontWeight.BOLD,15));
        message.setTextFill(Color.WHITE);
        message.setLayoutX(295);
        message.setLayoutY(425);
        
        
        RotateTransition transitionSablier = new RotateTransition(Duration.millis(2500), sablier);
        transitionSablier.setFromAngle(0);
        transitionSablier.setByAngle(359);
        transitionSablier.setCycleCount(Transition.INDEFINITE);
        
       
        ImageView fond = new ImageView(new Image("Images/fond.png"));
        fond.setFitHeight(HAUTEURFENETREFXML);
        fond.setFitWidth(LARGEURFENETREFXML);
        
        anchorPane.getChildren().add(fond);
        anchorPane.getChildren().add(sablier);
        anchorPane.getChildren().add(panneau);
        anchorPane.getChildren().add(message);
        transitionSablier.play();
        
        
              
       
           
        primaryStage.setWidth(LARGEURFENETREINIT);
        primaryStage.setHeight(HAUTEURFENETREINIT);
        primaryStage.centerOnScreen();
        scene.setCursor(Cursor.WAIT);
        primaryStage.show();
        
     
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                
             
                navigation = new Navigation(noeudRacine);
  
                Platform.runLater(new Runnable(){
                    @Override
                    public void run()
                    {
                         primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent t) 
                            {  
                                navigation.fermerToutesLesConnexions();
                                primaryStage.setOnCloseRequest(null);
                                t.consume();
                                ImageView imageExit = new ImageView(new Image("Images/goodbye.gif"));
                                //ImageView bulle = new ImageView(new Image("Images/goodbyeBulle.png"));
                                imageExit.setX(165);
                                imageExit.setY(180);
                                //bulle.setX(300);
                                //bulle.setY(100);

                                Rectangle rect = new Rectangle (0, 0, 800, 600);
                                //Rectangle rect2 = new Rectangle (0, 600, 800, 1);

                                noeudRacine.getChildren().add(rect);
                                //noeudRacine.getChildren().add(rect2);
                                //noeudRacine.getChildren().add(bulle);
                                noeudRacine.getChildren().add(imageExit);


                                rect.setFill(Color.BLACK);
                                //rect2.setFill(Color.BLACK);

                                //ScaleTransition tt = new ScaleTransition(Duration.seconds(2), rect);
                                //tt.setByY(350f);

                                FadeTransition tt = new FadeTransition(Duration.seconds(0.5), rect);
                                tt.setInterpolator(Interpolator.LINEAR);
                                tt.setCycleCount(1);
                                tt.setAutoReverse(false);
                                tt.setFromValue(0);
                                tt.setToValue(1);
                                
                                ScaleTransition ImageExitTransition = new ScaleTransition(Duration.seconds(1.5), imageExit);
                                TranslateTransition ImageExitTransition2 = new TranslateTransition(Duration.seconds(1.5), imageExit);
                                ImageExitTransition.setByY(0.2f);
                                ImageExitTransition.setByX(0.2f);
                                ImageExitTransition2.setByX(30f);
                                ImageExitTransition2.setByY(5f);

                                //ScaleTransition tt2 = new ScaleTransition(Duration.seconds(2), rect2);
                                //tt2.setByY(-350f);
                                //tt.setOnFinished(e -> {Platform.exit();});
                                //tt.play();
                                //tt2.play();
                                ImageExitTransition2.setOnFinished(e -> {Platform.exit();});
                                tt.play();
                                ImageExitTransition.play();
                                ImageExitTransition2.play();

                               }
                        }); 
                        navigation.changerVue(ControleurMenuPrincipal.class);
                        
                    }
                });
            }                       
        };
        thread.start();
        
        
        
       
       
        //primaryStage.setWidth(LARGEURFENETREFXML); // remplacer par LARGEURFENETREINIT avant le rendu
        //primaryStage.setHeight(HAUTEURFENETREFXML); // remplacer par HAUTEURFENETREINIT avant le rendu 
    
        primaryStage.setTitle("La Truite Opaque");
        /*Image image2 = new Image("Images/mouse2.gif");
        scene.setCursor(new ImageCursor(image2));*/
        
      
    }
        
    public static void main(String[] args)
    {
        launch(args);
    }

    
    
}
