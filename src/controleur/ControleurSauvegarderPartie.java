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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;


/**
 *
 * @author maxence
 */
public class ControleurSauvegarderPartie extends ControleurSauvegarde
{
   
    
    @FXML
    public TextField nouveauNomInput;   
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
        dateString = dateFormat.format(date).toString().replace("/","-");
        nouvelleDateText.setText("Date : " + dateFormat.format(date));
        nouveauNomInput.clear();
        setMessage("Veuillez choisir un emplacement de sauvegarde !");
        disableSaisie();

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
             showTuileEmpty(tuileSelectionne);
         }
        
        
        if(moteurs[indice] == null)
        {
            tuileSelectionne = indice;          
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
                
                boolean res = new File("Sauvegardes/"+nomSauvegarde).createNewFile();
                if(res)
                {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Sauvegardes/"+nomSauvegarde));
                    oos.writeObject(navigation.moteur);    
                    oos.close();
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
}
