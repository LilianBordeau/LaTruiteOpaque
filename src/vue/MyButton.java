/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author maxence
 */
public class MyButton extends Button {
    public Runnable afterOnMouseEntered;
    public Runnable afterOnMouseExited;
    public boolean changerCurseur;
    public MyButton()
    {        
        super();
        changerCurseur = true;
        afterOnMouseEntered = this::afterOnMouseEnteredMethod;
        afterOnMouseExited = this::afterOnMouseExitedMethod;
        this.setOnMouseEntered(new EventHandler<Event>(){
            @Override
            public void handle(Event event) {
                if(changerCurseur)
                {
                    Scene scene = MyButton.this.getScene();
                    scene.setCursor(Cursor.HAND);
                }                
                afterOnMouseEntered.run();
            
            }
        });
        
        this.setOnMouseExited(new EventHandler<Event>(){
            @Override
            public void handle(Event event) {
                if(changerCurseur)
                {
                    Scene scene = MyButton.this.getScene();
                    scene.setCursor(Cursor.DEFAULT);
                }                
                afterOnMouseExited.run();
            }
        });
    }
    
    

            private void afterOnMouseEnteredMethod()
            {
            }
            
            private void afterOnMouseExitedMethod()
            {
                
            }
    
}
