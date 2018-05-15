/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
       super.onAppearing();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        dateString = dateFormat.format(date).toString().replace("/",":");
        nouvelleDateText.setText("Date : " + dateFormat.format(date));

       btnSauvegarder.setDisable(true);
       setMessage("Veuillez choisir un emplacement de sauvegarde !");
    }
    
    
    @FXML
    private void selectEmplacement(MouseEvent event)
    {
        ImageView b =  (ImageView) event.getTarget();
        int indice =  Character.getNumericValue(b.getId().charAt(b.getId().length()-1));
        
        if(moteurs[indice] == null)
        {
            btnSauvegarder.setDisable(false);
            tuileSelectionne = indice;
             setMessage("");
            System.out.println("TUILE "+ tuileSelectionne+" selectionnee");
        }else
        {
            setMessage("Veuillez choisir un emplacement de sauvegarde valide ! ");
            btnSauvegarder.setDisable(true);
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
       }
    }
}
