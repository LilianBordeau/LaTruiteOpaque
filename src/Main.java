import controleur.ControleurMenuPrincipal;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
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
        primaryStage.setWidth(LARGEURFENETREFXML); // remplacer par LARGEURFENETREINIT avant le rendu
        primaryStage.setHeight(HAUTEURFENETREFXML); // remplacer par HAUTEURFENETREINIT avant le rendu 
        /*primaryStage.setWidth(LARGEURFENETREINIT);
        primaryStage.setHeight(HAUTEURFENETREINIT);*/
        primaryStage.centerOnScreen();        
        navigation.changerVue(ControleurMenuPrincipal.class);
        //primaryStage.setFullScreen(true);
    }
        
    public static void main(String[] args)
    {
        launch(args);
    }

    
    
}
