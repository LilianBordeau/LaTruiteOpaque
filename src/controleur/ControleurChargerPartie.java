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

public class ControleurChargerPartie extends ControleurSauvegarde
{
    @FXML
    public Button btnCommencer;
    
    @Override
    public void onAppearing()
    {
        super.onAppearing();
        btnCommencer.setDisable(true);
        setMessage("Veuillez choisir une sauvegarde !");
    }
    
    
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
        if(moteurs[indice] != null)
        {
                btnCommencer.setDisable(false);
                tuileSelectionne = indice;
                showSave(tuileSelectionne);
                System.out.println("TUILE "+ tuileSelectionne+" selectionnee");
                setMessage("");
        }else
        {
            clearPlateau();
            btnCommencer.setDisable(true);
            setMessage("Veuillez choisir une sauvegarde !");
            tuileSelectionne = -1;
            System.out.println("Pas de sauvegarde" + indice);
        }
    
       
    }
    
             
    @FXML
    private void commencer(ActionEvent event)
    {
        
        
        if(tuileSelectionne != -1)
        {
            System.out.println("commencerPartie");
            
            navigation.moteur = moteurs[tuileSelectionne];
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
    
}
