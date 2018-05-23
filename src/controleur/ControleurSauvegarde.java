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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Case;
import modele.Constantes;
import modele.Joueur;
import modele.Moteur;
import modele.Pingouin;
import util.Couple;

/**
 *
 * @author maxence
 */
public abstract class ControleurSauvegarde extends ControleurBase 
{

    int NBSAUVEGARDES = 3;
    ArrayList<Couple> parties;
     ArrayList<File> files;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public ImageView trash0,trash1,trash2;
    
    @FXML
    public Text message,valeurNbPage,valeurPageActuelle;
    
    int tuileSelectionne;
    int tuileSupprimee;
    
    int indicePage;
    int nbPages;
    
    @FXML
    public void initialize(URL location, ResourceBundle resources)
    {
        
  
    }
    
    @Override
    public void onAppearing()
    {
        parties = new ArrayList<>();
        files = new ArrayList<>();
        tuileSupprimee = -1;
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
            Couple partie = new Couple(params,getMoteurFromFile(file));
            parties.add(partie);
            files.add(file);
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
    
    
    public void showTuile(int i,String[] params)
    { 
            Text nom = (Text)anchorPane.lookup("#"+getNomId(i));
            nom.setText("Nom : " + params[1]);
            nom.setVisible(true);
            Text invalide = (Text)anchorPane.lookup("#"+getVideId(i));
            invalide.setVisible(false);
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
        Text invalide = (Text)anchorPane.lookup("#"+getVideId(i));
        invalide.setVisible(true);
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
    public String getVideId(int i)
    {
        return "vide"+i;
    }
    
    
   public void showDialogTrash(MouseEvent event)
   {
        ImageView bin = (ImageView) event.getTarget();
        
        if(bin == trash0)
        {
            tuileSupprimee = 0;
        }else if(bin == trash1)
        {
            tuileSupprimee = 1;
        }else
        {
            tuileSupprimee = 2;
        }
       
       
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        VBox dialogVbox = new VBox(20);
        HBox hBoxButtons = new HBox();
        Text message = new Text("Etes vous sur de vouloir supprimer la sauvegarde : ");
        Text date = (Text) anchorPane.lookup("#"+getDateId(tuileSupprimee));
        Text nom = (Text) anchorPane.lookup("#"+getNomId(tuileSupprimee));
        Text dateDialog = new Text(date.getText());
        Text nomDialog = new Text(nom.getText());
        
        
        Button valider = new Button("Valider");
        Button annuler = new Button("Annuler");
        hBoxButtons.getChildren().addAll(annuler,valider);
        hBoxButtons.setSpacing(100);
        hBoxButtons.setAlignment(Pos.CENTER);
        
        dialogVbox.getChildren().addAll(message,nomDialog,dateDialog);
        dialogVbox.getChildren().add(hBoxButtons);
        dialogVbox.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(dialogVbox, Constantes.POPUPWIDTH, Constantes.POPUPHEIGHT);
       
        dialog.setScene(dialogScene);
        dialog.show();
        

        annuler.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
                dialog.close();
                tuileSupprimee = -1;
            }
        });
        
        valider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                 trashIt(tuileSupprimee);
                 dialog.close();
            }
        });
        
   }
    
    
   
    public void trashIt(int numSauvegarde)
    {   
        if(deleteSave(numSauvegarde))
        {
              showTuileEmpty(numSauvegarde);
        }
        
    }
    
    public boolean deleteSave(int indice) 
    {
        if(indice +  (indicePage*3)< parties.size()  )
        {
            boolean res = files.get(indice).delete();
            if(res)
            {
                files.set(indice,null);
                parties.set(indice,null);
            }
            return res;
        }
        else
        {
            return false;
        }
     
        
    }
    
    
    
    public void setMessage(String text)
    {
        message.setText(text);
    }
    public void clearMessage()
    {
        message.setText("");
    }
    
    
   
  abstract public void showCurrentTuiles() ;
    
    @FXML
    private void suivant(Event event)
    {
        if(indicePage == nbPages)
        {
            indicePage =  0;    
        }else
        {
            indicePage++;
        }
        tuileSelectionne = -1;
        valeurPageActuelle.setText(Integer.toString(indicePage+1));
        showCurrentTuiles();

    }
    
    @FXML
    private void precedent(Event event)
    {
        System.out.print("teffds");
        if(indicePage ==0)
        {
            indicePage = nbPages;    
            
        }else
        {
            indicePage --;
        }
        tuileSelectionne = -1;
        valeurPageActuelle.setText(Integer.toString(indicePage+1));
        showCurrentTuiles();
    }
    
}
