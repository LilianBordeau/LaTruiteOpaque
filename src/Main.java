import controleur.ControleurMenuPrincipal;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import vue.Navigation;

public class Main extends Application {
    private static final double SCREENRATIOWIDTHINIT = 0.7;
    private static final double SCREENRATIOHEIGHTINIT = SCREENRATIOWIDTHINIT;      
    public static final int LARGEURFENETREINIT = (int)(Screen.getPrimary().getVisualBounds().getWidth()*SCREENRATIOWIDTHINIT);
    public static final int HAUTEURFENETREINIT = (int)(Screen.getPrimary().getVisualBounds().getHeight()*SCREENRATIOHEIGHTINIT);
    private static final int LARGEURFENETREFXML = 800;
    private static final int HAUTEURFENETREFXML = 600;
    
    @Override
    public void start(Stage primaryStage) {
        Group noeudRacine = new Group();        
        Navigation navigation = new Navigation(noeudRacine);
        Scene scene = new Scene(noeudRacine);
        primaryStage.setScene(scene);
        primaryStage.show();
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
        //primaryStage.setWidth(LARGEURFENETREFXML); // remplacer par LARGEURFENETREINIT avant le rendu
        //primaryStage.setHeight(HAUTEURFENETREFXML); // remplacer par HAUTEURFENETREINIT avant le rendu 
        primaryStage.setWidth(LARGEURFENETREINIT);
        primaryStage.setHeight(HAUTEURFENETREINIT);
        primaryStage.centerOnScreen();        
        primaryStage.setTitle("La Truite Opaque");
        Image image2 = new Image("Images/mouse2.gif");
        scene.setCursor(new ImageCursor(image2));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent t) {  
            navigation.fermerToutesLesConnexions();
            primaryStage.setOnCloseRequest(null);
            t.consume();
        ImageView imageExit = new ImageView(new Image("Images/goodbye.gif"));
        ImageView bulle = new ImageView(new Image("Images/goodbyeBulle.png"));
        imageExit.setX(0);
        imageExit.setY(100);
        bulle.setX(300);
        bulle.setY(100);
        
        Rectangle rect = new Rectangle (0, 0, 800, 1);
        Rectangle rect2 = new Rectangle (0, 600, 800, 1);
        
        noeudRacine.getChildren().add(rect);
        noeudRacine.getChildren().add(rect2);
        noeudRacine.getChildren().add(bulle);
        noeudRacine.getChildren().add(imageExit);
        
     
     rect.setFill(Color.BLACK);
     rect2.setFill(Color.BLACK);
 
     ScaleTransition tt = new ScaleTransition(Duration.seconds(2), rect);
     tt.setByY(350f);
     
     ScaleTransition ImageExitTransition = new ScaleTransition(Duration.seconds(2), imageExit);
     TranslateTransition ImageExitTransition2 = new TranslateTransition(Duration.seconds(2), imageExit);
     ImageExitTransition.setByY(0.2f);
     ImageExitTransition.setByX(0.2f);
     
     ImageExitTransition2.setByX(30f);
     ImageExitTransition2.setByY(5f);

     ScaleTransition tt2 = new ScaleTransition(Duration.seconds(2), rect2);
     tt2.setByY(-350f);
       tt.setOnFinished(e -> {Platform.exit();});
     tt.play();
     tt2.play();
     ImageExitTransition.play();
     ImageExitTransition2.play();

    }
}); 
        navigation.changerVue(ControleurMenuPrincipal.class);
    }
        
    public static void main(String[] args)
    {
        launch(args);
    }

    
    
}
