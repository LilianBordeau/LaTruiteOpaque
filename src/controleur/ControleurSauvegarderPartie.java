/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import vue.MyTextField;


/**
 *
 * @author maxence
 */
public class ControleurSauvegarderPartie extends ControleurSauvegarde
{
   
    
    @FXML
    public MyTextField   nouveauNomInput;   
    @FXML
    public Text nouvelleDateText;  
    @FXML
    public Button btnSauvegarder;
    
    String dateString;
    
    @Override
    public void onAppearing()
    {
        tuileSelectionne = -1;
        super.onAppearing();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'à' hh:mm");
        Date date = new Date();
        dateString = dateFormat.format(date).toString().replaceAll("[/:]","-");
        nouvelleDateText.setText("Date : " + dateFormat.format(date));
        nouveauNomInput.clear();
        nouveauNomInput.charactereInvalides = "[/\\<>|\"*_:]";
        setMessage("Veuillez choisir un emplacement de sauvegarde !");
        disableSaisie();
       
   
        nbPages  = parties.size()/3  ;

        indicePage = nbPages;
    
        
        valeurPageActuelle.setText(Integer.toString(indicePage +1));
        valeurNbPage.setText(Integer.toString(nbPages + 1));
        showCurrentTuiles();
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
             showTuileEmpty(tuileSelectionne);
         }
        
        if( indice +1> parties.size() || parties.get(indice) == null  )
        {
            tuileSelectionne = numTuile;          
            ImageView imageSelected = (ImageView) anchorPane.lookup("#"+getSelectedId(tuileSelectionne));
            imageSelected.setVisible(true);
            enableSaisie();
            clearMessage();
            System.out.println("TUILE "+ tuileSelectionne+" selectionnee");
        } else
        {
            tuileSelectionne = -1;
            setMessage("Veuillez choisir un emplacement de sauvegarde valide ! ");
            disableSaisie();
            System.out.println("Tuile occupée");
        }
    
       
    }
    
    
    @FXML
    private void retourJeu(ActionEvent event)
    {
        navigation.changerVue(ControleurJeu.class);
    }
    
    @FXML
    private void sauvegarder(ActionEvent event) 
    {
              
        if( nouveauNomInput.getText().trim().isEmpty())
        {
            super.setMessage("Nom de sauvegarde incorrect");
        }
        else
        {
            if( tuileSelectionne  != - 1)
            {
           
            String nomSauvegarde = tuileSelectionne + "_"+nouveauNomInput.getText().trim()+"_"+dateString+".txt";
                    
            try {
                File fichier = new File("Sauvegardes/"+nomSauvegarde);
                boolean res = fichier.createNewFile();
                if(res)
                {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier));
                    oos.writeObject(navigation.moteur);
                    oos.close();
                }
                else
                {
                    throw new RuntimeException("impossible de creer le fichier de sauvegarde");
                }
            } catch (IOException ex) {
               throw new RuntimeException(ex);
            }
           
            navigation.changerVue(ControleurJeu.class);
            super.setMessage("");
            }
        }
        
       
        
       
    }
    
    
    
    private void disableSaisie()
    {
        nouveauNomInput.setDisable(true);
        btnSauvegarder.setDisable(true);

        
    }
    private void enableSaisie()
    {
        btnSauvegarder.setDisable(false);
        nouveauNomInput.setDisable(false);
    }
    
    
    @Override
     public void showCurrentTuiles() {
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
