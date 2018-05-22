/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;


/**
 *
 * @author Justin Kossonogow
 */
public class PanelSonManager extends Group {
    
    public ImageView background;
    
    public MyButton screen;
    
    public MyButton imageMusique;
    public Slider sliderMusique = new Slider();
    
    public MyButton imageSon;
    public Slider sliderSon;
    
    public PanelSonManager()
    {
       
        background = new ImageView();
        background.setPickOnBounds(true);
        background.setLayoutX(0);
        background.setLayoutY(200);
        background.setFitWidth(40);
        background.setFitHeight(209);
        background.getStyleClass().add("managerPane");
        
        screen = new MyButton();
        screen.setPickOnBounds(true);
        screen.setLayoutX(2);
        screen.setLayoutY(250);
        screen.setPadding(Insets.EMPTY);
        screen.setBackground(Background.EMPTY);
        screen.setMaxWidth(30);
        screen.setMaxHeight(30);
        setHoverListener(screen);
        
       

    
        imageMusique = new MyButton();
        imageMusique.setPickOnBounds(true);
        imageMusique.setLayoutX(2);
        imageMusique.setLayoutY(300);
        imageMusique.setPadding(Insets.EMPTY);
        imageMusique.setBackground(Background.EMPTY);
        imageMusique.setMaxWidth(30);
        imageMusique.setMaxWidth(30);
        setHoverListener(imageMusique);

                
        sliderMusique = new Slider();
        sliderMusique.setLayoutX(35);
        sliderMusique.setLayoutY(307);

        imageSon = new MyButton();
        imageSon.setPickOnBounds(true);
        imageSon.setLayoutX(2);
        imageSon.setLayoutY(350);
        imageSon.setPadding(Insets.EMPTY);
        imageSon.setBackground(Background.EMPTY);
        imageSon.setMaxWidth(30);
        imageSon.setMaxWidth(30);
         setHoverListener(imageSon);
        
        sliderSon = new Slider();
        sliderSon.setLayoutX(35);
        sliderSon.setLayoutY(357);
        
        this.getChildren().addAll(background,screen,imageMusique,sliderMusique,imageSon,sliderSon);
    }
    
   
    
    private void setHoverListener(MyButton button)
    {
      button.afterOnMouseEntered = (() -> button.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.80), 0, 0, -2, 0) ;-fx-scale-x: 1;" +
"    -fx-scale-y: 1;" +
"    -fx-padding: 0 0 0 2;"));
        button.afterOnMouseExited = (() -> button.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.0), 0, 0, 0, 0) "));   
    }
    
}
