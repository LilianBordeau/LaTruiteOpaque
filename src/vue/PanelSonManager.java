/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


/**
 *
 * @author Justin Kossonogow
 */
public class PanelSonManager extends Group {
    
    public ImageView screen;
    
    public ImageView imageMusique;
    public Slider sliderMusique = new Slider();
    
    public ImageView imageSon;
    public Slider sliderSon;
    
    public PanelSonManager()
    {
       

        screen = new ImageView();
        screen.setPickOnBounds(true);
        screen.setLayoutX(2);
        screen.setLayoutY(250);
        screen.setFitWidth(30);
        screen.setFitHeight(30);
    

        
        imageMusique = new ImageView();
        imageMusique.setPickOnBounds(true);
        imageMusique.setLayoutX(2);
        imageMusique.setLayoutY(285);
        imageMusique.setFitWidth(30);
        imageMusique.setFitHeight(30);
                
        sliderMusique = new Slider();
        sliderMusique.setLayoutX(35);
        sliderMusique.setLayoutY(292);

        imageSon = new ImageView();
        imageSon.setPickOnBounds(true);
        imageSon.setLayoutX(2);
        imageSon.setLayoutY(327);
        imageSon.setFitWidth(30);
        imageSon.setFitHeight(30);
        
        sliderSon = new Slider();
        sliderSon.setLayoutX(35);
        sliderSon.setLayoutY(327);
        this.getChildren().addAll(screen,imageMusique,sliderMusique,imageSon,sliderSon);
    }
    
   

    
}
