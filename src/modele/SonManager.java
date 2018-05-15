/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 *  
 * @author maxence
 */
public class SonManager {
    

    Media[] sons;
    MediaPlayer mediaPlayerMusique;
    MediaPlayer mediaPlayerSon;
    public double volumeMusique; 
    public double volumeSon; 
    public SonManager() 
    {
        volumeMusique = 50;
        volumeSon = 50;
        sons = new Media[Constantes.SONS.length];
        for(int i =0; i <Constantes.SONS.length ;i++ )
        {
            sons[i] = new Media(new File(Constantes.cheminSon(i)).toURI().toString());
        }    
       
    }
    
    
    public void setVolumeMusique(double i)
    {
        if(mediaPlayerMusique != null)
        {
            volumeMusique = i;
            mediaPlayerMusique.setVolume(i/100);
        }
    }
    
    public void setVolumeSon(double i)
    {
        volumeSon = i;
        if(mediaPlayerSon != null)
        {
            mediaPlayerSon.setVolume(i/100);
        }
    }
    
    public void jouerMusique()
    {
        if(mediaPlayerMusique == null)
        {
            mediaPlayerMusique = new MediaPlayer(sons[0]);
        }
        mediaPlayerMusique.setVolume(volumeMusique/100);
        mediaPlayerMusique.setOnEndOfMedia(new Runnable() {
            public void run() {
              mediaPlayerMusique.seek(Duration.ZERO);
            }
        });
        mediaPlayerMusique.play();
    }
    
     public void arreterMusique()
    {
        if(mediaPlayerMusique != null)
        {
            mediaPlayerMusique.stop();
        }
    }
     
     
     
    public void jouerSon(int i)
    {
        if(sons[i] != null)
        {
            mediaPlayerSon = new MediaPlayer(sons[i]);
            mediaPlayerSon.setVolume(volumeSon/100);
            mediaPlayerSon.play();
            
        }
    }
     
  


    
    
}
