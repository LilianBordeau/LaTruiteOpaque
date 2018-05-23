package controleur;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
        tuileSupprimee = -1;
        tuileSelectionne = -1;
  
        
        if(parties.isEmpty())
        {
            setMessage("Aucune sauvegarde n'est disponible !");            
        }else
        {
            setMessage("Veuillez choisir une sauvegarde !");            
        }
        
        nbPages  = parties.size()/3 ;
        if(parties.size()%3==0)
        {
           nbPages--; 
        }
        
        indicePage = nbPages;

        valeurPageActuelle.setText(Integer.toString(indicePage +1));
        valeurNbPage.setText(Integer.toString(nbPages + 1));
        showCurrentTuiles();
    }
    
    public String indicesToId(int ligne, int colonne, String prefixeId)
    {
        return prefixeId+ligne+"_"+colonne;
    }
    
    
    
    
    public void showSave(int nbPartie)
    {   
        Moteur m  = (Moteur) parties.get(nbPartie).second;
        Case[][] plateau = m.plateau.plateau;
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
        Joueur[] joueurs = m.joueurs;
        for(int j = 0 ; j < joueurs.length ; j++)
        {
            
            ArrayList<Pingouin> pingouins = joueurs[j].pingouins;
            for(Pingouin p : pingouins)
            { 
                if(!p.estBloque)
                {
                    ImageView imagePingouin = (ImageView)anchorPane.lookup("#"+indicesToId(p.ligne,p.colonne, "p"));
                    String pathImagePingouin = Constantes.nomImagePingouin(joueurs[j]);
                    imagePingouin.setImage(new Image(pathImagePingouin));
                    imagePingouin.setVisible(true);
                }
            }
        }     
    }
    
   
    @FXML
    private void selectEmplacement(MouseEvent event)
    {
        ImageView b =  (ImageView) event.getTarget();
        int numTuile =  Character.getNumericValue(b.getId().charAt(b.getId().length()-1));
        int indice = numTuile+(indicePage*3);
        if(tuileSelectionne != -1)
        {
            ImageView imageSelectedPrec = (ImageView) anchorPane.lookup("#"+getSelectedId(tuileSelectionne));
            imageSelectedPrec.setVisible(false);
        }
                  
        
        if( indice < parties.size() && parties.get(indice) != null  )
        {  
            tuileSelectionne = numTuile;
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
            Moteur moteur = (Moteur) parties.get(tuileSelectionne).second;
            navigation.moteur = moteur;
            ControleurJeu controleurJeu = (ControleurJeu)navigation.getController(ControleurJeu.class);
            navigation.changerVue(ControleurJeu.class);            
            controleurJeu.initPartie();

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
    
    
    private void showCurrentTuiles() {
        System.out.println(indicePage+ "test" + nbPages);
        int nbEmplacement = 0;
        int debut = (indicePage*3);
      
        for(int i = debut;i < debut+3;i++)
        {
            if(i >= parties.size() || parties.get(i) == null)
            { 
                showTuileEmpty(nbEmplacement);
            }
            else 
            {
                String[] params = (String[]) parties.get(i).premier;
                showTuile(nbEmplacement,params);
            }
            nbEmplacement++;
        }
      

    }
}
