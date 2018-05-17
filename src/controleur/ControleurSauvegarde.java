/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import modele.Case;
import modele.Constantes;
import modele.Joueur;
import modele.Moteur;
import modele.Pingouin;

/**
 *
 * @author maxence
 */
public class ControleurSauvegarde extends ControleurBase 
{

    int NBSAUVEGARDES = 3;
    Moteur[] moteurs;
    File[] files;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public ImageView trash0,trash1,trash2;
    
    @FXML
    public Text message;
    
    int tuileSelectionne;
    
    @FXML
    public void initialize(URL location, ResourceBundle resources)
    {
        moteurs = new Moteur[NBSAUVEGARDES];
        files = new File[NBSAUVEGARDES];
    }
    
    @Override
    public void onAppearing()
    {
        tuileSelectionne = -1;
        scanSaveFolder();
       
    }
    
    
    
    
    private void scanSaveFolder()  {
        File folder =  new File("Sauvegardes");
        if(folder.exists() == false )
        {
          boolean res = (folder.mkdirs()); 
        }
        File[] filestmp = folder.listFiles();
        
        for(File file : filestmp)
        {
            String[] params = file.getName().split("_");
            int indice = Integer.parseInt(params[0]); 
            files[indice] = file;
            showTuile(indice,params);
            
           moteurs[indice] = getMoteurFromFile(file);

           
        }
        
 
        for(int i = 0 ; i< NBSAUVEGARDES;i++)
        {
            if(files[i] == null)
            {
                showTuileEmpty(i);
            }
        }
      
    }
    
    
    
    private Moteur getMoteurFromFile(File f)
    {
        try
        {
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(f));
            return  (Moteur)oos.readObject();  
          
       
         }   
        catch (IOException  |  ClassNotFoundException ex ) {
            throw new RuntimeException(ex);
        } 
    }
    
    
    private void showTuile(int i,String[] params)
    { 
            Text nom = (Text)anchorPane.lookup("#"+getNomId(i));
            nom.setText("Nom : " + params[1]);
            nom.setVisible(true);
            Text date = (Text)anchorPane.lookup("#"+getDateId(i));
            String dateText = params[2].replace("-", "/");
            dateText = dateText.replaceFirst("[.][^.]+$", "");
            date.setText("Date : " + dateText ); 
            date.setVisible(true);
            ImageView bin = (ImageView) anchorPane.lookup("#"+getBinId(i));
            bin.setVisible(true);
            ImageView selected = (ImageView) anchorPane.lookup("#"+getSelectedId(i));
            selected.setVisible(false);
            ImageView imageTuile = (ImageView) anchorPane.lookup("#"+getImageId(i));
            imageTuile.opacityProperty().setValue(1);
           
    }
    
    
   
    public void showTuileEmpty(int i)
    {
        Text nom = (Text)anchorPane.lookup("#"+getNomId(i));
        nom.setVisible(false);
        Text date = (Text)anchorPane.lookup("#"+getDateId(i));
        date.setVisible(false);
        ImageView bin = (ImageView) anchorPane.lookup("#"+getBinId(i));
        bin.setVisible(false);
        ImageView selected = (ImageView) anchorPane.lookup("#"+getSelectedId(i));
        selected.setVisible(false);
        ImageView tuile = (ImageView) anchorPane.lookup("#"+getImageId(i));
        tuile.opacityProperty().setValue(0.50);
        tuile.setVisible(true);
    }
    
     
    public String getNomId(int i)
    {
        return "nom"+i;
    }
    public String getDateId(int i)
    {
        return "date"+i;
    }
    public String getBinId(int i) {
            return "trash"+i;
    }
    public String getRedCrossId(int i) {
            return "cross"+i;
    }
    public String getSelectedId(int i) {
            return "selected"+i;
    }
    public String getImageId(int i)
    {
        return "image"+i;
    }
    
    
    
   
    public void trashIt(MouseEvent event)
    {
        ImageView bin = (ImageView) event.getTarget();
        int indice;
        if(bin == trash0)
        {
            indice = 0;
        }else if(bin == trash1)
        {
            indice = 1;
        }else
        {
            indice = 2;
        }
     
        if(deleteSave(indice))
        {
              showTuileEmpty(indice);
        }
        
      
       
    }
    
    private boolean deleteSave(int indice) 
    {
        moteurs[indice] = null;
        boolean res = files[indice].delete();
        if(res)
        {
            files[indice] = null;
        }
        return res;
    }
    
    
    
    public void setMessage(String text)
    {
        message.setText(text);
    }
    public void clearMessage()
    {
        message.setText("");
    }
}
