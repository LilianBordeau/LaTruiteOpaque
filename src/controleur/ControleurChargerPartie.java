package controleur;

import java.io.File;
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

public class ControleurChargerPartie extends ControleurBase
{
   
    int NBSAUVEGARDES = 3;
    Moteur[] moteurs;
    File[] files;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public ImageView trash0,trash1,trash2;
    
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        moteurs = new Moteur[NBSAUVEGARDES];
        files = new File[NBSAUVEGARDES];
    }
    
    
    
    
    @Override
    public void onAppearing()
    {
        scanSaveFolder();
    }
    
    private String indicesToId(int ligne, int colonne, String prefixeId)
    {
        return prefixeId+ligne+"_"+colonne;
    }
    public static int nbTuilesLigne(int i)
    {
        return (i%2==0)?7:8;
    }
    
    
    
    
    @FXML
    public void showSave(MouseEvent event)
    {
        ImageView image = (ImageView) event.getTarget();
        String id = image.getId();
        
        Case[][] plateau = navigation.moteur.plateau.plateau;
        for(int i = 0 ; i < plateau.length ; i++)
        {
            for(int j =0 ; j < nbTuilesLigne(i) ; j++)
            {
               int nbPoissons  = plateau[i][j].nbPoissons;
               ImageView imagePlateau = (ImageView)anchorPane.lookup("#"+indicesToId(i,j, "c"));
               
               if( nbPoissons == 0 )
               {
                   imagePlateau.setVisible(false);
                   imagePlateau.setImage(null);
               }else
               {
                   imagePlateau.setVisible(true);
                   String pathImage = Constantes.nomImageCase(plateau[i][j]);
                imagePlateau.setImage(new Image(pathImage));
               }
            }  
        }
        Joueur[] joueurs =  navigation.moteur.joueurs;
        for(int i = 0 ; i < joueurs.length ; i++)
        {
            
            ArrayList<Pingouin> pingouins = joueurs[i].pingouins;
            for(Pingouin p : pingouins)
            {
                ImageView imagePingouin = (ImageView)anchorPane.lookup("#"+indicesToId(p.ligne,p.colonne, "p"));
                String pathImagePingouin = Constantes.nomImagePingouin(joueurs[i]);
                imagePingouin.setImage(new Image(pathImagePingouin));
                
            }
        }    
        
    }
    /*
    public void showSave(int nbPartie)
    {
     
        
        Case[][] plateau = moteurs[nbPartie].plateau.plateau;
        for(int i = 0 ; i < plateau.length ; i++)
        {
            for(int j =0 ; j < nbTuilesLigne(i) ; j++)
            {
               int nbPoissons  = plateau[i][j].nbPoissons;
               ImageView imagePlateau = (ImageView)anchorPane.lookup("#"+indicesToId(i,j, "c"));
            
               if( nbPoissons == 0 )
               {
                   imagePlateau.setImage(null);
               }else
               {
                   String pathImage = Constantes.nomImageCase(plateau[i][j]);
                imagePlateau.setImage(new Image(pathImage));
               }
            }  
        }
        Joueur[] joueurs =  navigation.moteur.joueurs;
        for(int j = 0 ; j < joueurs.length ; j++)
        {
            
            ArrayList<Pingouin> pingouins = joueurs[j].pingouins;
            for(Pingouin p : pingouins)
            {
                ImageView imagePingouin = (ImageView)anchorPane.lookup("#"+indicesToId(p.ligne,p.colonne, "p"));
                String pathImagePingouin = Constantes.nomImagePingouin(joueurs[j]);
                imagePingouin.setImage(new Image(pathImagePingouin));
                
            }
        }    
        
    }
    
    
    
    */
    
    
     @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.changerVue(ControleurMenuPrincipal.class);
    }
    
    private String getNomId(int i)
    {
        return "nom"+i;
    }
    private String getDateId(int i)
    {
        return "date"+i;
    }
    private String getBinId(int i) {
            return "trash"+i;
    }
    private String getRedCrossId(int i) {
            return "cross"+i;
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
             
    
    private void showTuile(int i,String[] params)
    {
            Text nom = (Text)anchorPane.lookup("#"+getNomId(i));
            nom.setText("Nom : " + params[0]);
            nom.setVisible(true);
            Text date = (Text)anchorPane.lookup("#"+getDateId(i));
            date.setText("Date : " + params[1].replace(":", "/")); 
            date.setVisible(true);
            ImageView bin = (ImageView) anchorPane.lookup("#"+getBinId(i));
            bin.setVisible(true);
            ImageView redCross = (ImageView) anchorPane.lookup("#"+getRedCrossId(i));
            redCross.setVisible(false);
    }
    
    
    private void showTuileEmpty(int i)
    {
        Text nom = (Text)anchorPane.lookup("#"+getNomId(i));
        nom.setVisible(false);
        Text date = (Text)anchorPane.lookup("#"+getDateId(i));
        date.setVisible(false);
        ImageView bin = (ImageView) anchorPane.lookup("#"+getBinId(i));
        bin.setVisible(false);
        ImageView redCross = (ImageView) anchorPane.lookup("#"+getRedCrossId(i));
        redCross.setVisible(true);
    }
    
    
    
    
    

    private void scanSaveFolder() {
        File folder =  new File("Sauvegardes");
        if(folder.exists() == false )
        {
          boolean res = (folder.mkdirs()); 
        }
        File[] filestmp = folder.listFiles();
        int i= 0;
        if(filestmp != null)
        {
            while( i < filestmp.length  )
            {
                files[i] = filestmp[i];
                String[] params = filestmp[i].getName().split("_");
                showTuile(i,params);
                i++;
            }
        }
        
        if(i < NBSAUVEGARDES)
        {
            for(int j = i ; j < NBSAUVEGARDES ; j++)
            {
               showTuileEmpty(j);
            
            }
        }
    }

    private boolean deleteSave(int indice) {
        moteurs[indice] = null;
        boolean res = files[indice].delete();
        if(res)
        {
            files[indice] = null;
        }
        return res;
    }

    
}
