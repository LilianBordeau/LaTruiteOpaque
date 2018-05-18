package controleur;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
import modele.Plateau;

public class ControleurChargerPartie extends ControleurSauvegarde
{
    @FXML
    public Button btnCommencer;
    
    @Override
    public void onAppearing()
    {
        super.onAppearing();
        clearPlateau();
        btnCommencer.setDisable(true);
        
        int i = 0;
        while(i < moteurs.length && moteurs[i] == null)
        {
            i++;     
        }
        
        if(i== moteurs.length)
        {
            setMessage("Aucune sauvegarde n'est disponible !");            
        }else
        {
            setMessage("Veuillez choisir une sauvegarde !");            
        }

    }
    
    public String indicesToId(int ligne, int colonne, String prefixeId)
    {
        return prefixeId+ligne+"_"+colonne;
    }
    
    
    
    
    
    public void showSave(int nbPartie)
    {   
        Case[][] plateau = moteurs[nbPartie].plateau.plateau;
        for(int i = 0 ; i < plateau.length ; i++)
        {
            for(int j =0 ; j < Plateau.nbTuilesLigne(i) ; j++)
            {
               int nbPoissons  = plateau[i][j].nbPoissons;
               ImageView imagePlateau = (ImageView)anchorPane.lookup("#"+indicesToId(i,j, "c"));
            
               if( nbPoissons == 0 )
               {
                   imagePlateau.setImage(null);
                   
               }else
               {
                   String pathImage = Constantes.nomImageCase(plateau[i][j]);
                   imagePlateau.setVisible(true);
                   imagePlateau.setImage(new Image(pathImage));
               }
            }  
        }
        Joueur[] joueurs =  moteurs[nbPartie].joueurs;
        for(int j = 0 ; j < joueurs.length ; j++)
        {
            
            ArrayList<Pingouin> pingouins = joueurs[j].pingouins;
            for(Pingouin p : pingouins)
            {
                ImageView imagePingouin = (ImageView)anchorPane.lookup("#"+indicesToId(p.ligne,p.colonne, "p"));
                String pathImagePingouin = Constantes.nomImagePingouin(joueurs[j]);
                imagePingouin.setImage(new Image(pathImagePingouin));
                imagePingouin.setVisible(true);
            }
        }     
    }
    
   
    @FXML
    private void selectEmplacement(MouseEvent event)
    {
        ImageView b =  (ImageView) event.getTarget();
        int indice =  Character.getNumericValue(b.getId().charAt(b.getId().length()-1));
        
        if(tuileSelectionne != -1)
        {
            ImageView imageSelectedPrec = (ImageView) anchorPane.lookup("#"+getSelectedId(tuileSelectionne));
            imageSelectedPrec.setVisible(false);
        }
                  
        if(moteurs[indice] != null)
        {  
            tuileSelectionne = indice;
            ImageView imageSelected = (ImageView) anchorPane.lookup("#"+getSelectedId(tuileSelectionne));
            imageSelected.setVisible(true);
            
            btnCommencer.setDisable(false);
            clearPlateau();
            showSave(tuileSelectionne);
            
            System.out.println("TUILE "+ tuileSelectionne+" selectionnee");
            clearMessage();
        }else
        {
           
            clearPlateau();
            btnCommencer.setDisable(true);
            setMessage("Veuillez choisir une sauvegarde !");
            tuileSelectionne = -1;
            System.out.println("Pas de sauvegarde" + indice);
        }
    
       
    }
    

    public void clearPlateau()
    {
         for(int i = 0 ; i < 8 ; i++)
        {
            for(int j =0 ; j < Plateau.nbTuilesLigne(i) ; j++)
            {
              
               ImageView imagePlateau;
               imagePlateau = (ImageView)anchorPane.lookup("#"+indicesToId(i,j, "c"));
               imagePlateau.setImage(null);
               imagePlateau.setVisible(false);
               imagePlateau = (ImageView)anchorPane.lookup("#"+indicesToId(i,j, "p"));
               imagePlateau.setImage(null);
               imagePlateau.setVisible(false);
            }  
        }
    }
    
    
    @FXML
    private void commencer(ActionEvent event)
    {
        if(tuileSelectionne != -1)
        {
            System.out.println("commencerPartie");
            navigation.moteur = moteurs[tuileSelectionne];
            ControleurJeu controleurJeu = (ControleurJeu)navigation.getController(ControleurJeu.class);
            controleurJeu.lineFantome.setVisible(false);
            controleurJeu.estEnAttente.set(false);
            controleurJeu.reprendre();
            navigation.changerVue(ControleurJeu.class);
             

        }else{
            System.out.println("Pas de selection valide ");
        }
    }
    
     @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.changerVue(ControleurMenuPrincipal.class);
    }
  
    @Override
    public void trashIt(int numSauvegarde)
    {
        
        super.trashIt(numSauvegarde);
        
        if(tuileSupprimee == tuileSelectionne)
        {
            clearPlateau();
            btnCommencer.setDisable(true);
            setMessage("Veuillez choisir une sauvegarde !");
            tuileSelectionne = -1;
            
        }
        
    }
}
