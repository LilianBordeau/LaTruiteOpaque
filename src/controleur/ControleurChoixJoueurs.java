package controleur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ControleurChoixJoueurs extends ControleurBase
{    
    @FXML
    private void clicCommencer(ActionEvent event)
    {
        navigation.changerVue(ControleurJeu.class);
    }
    
    @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.changerVue(ControleurMenuPrincipal.class);
    }
    
    int ROUGE = 0;int BLEU = 2;
    int VERT = 1;int JAUNE = 3;
    String[] TYPEJOUEUR = {"JOUEUR", "IA1"};
    String[] COULEUR = {"ROUGE","VERT","BLEU","JAUNE"};
    
    @FXML
    private Button precedentRouge,suivantRouge,precedentVert,suivantVert;
    @FXML
    private StackPane imagesRouge ,nomRouge,imagesVert,nomVert;
    
    StackPane[] imagesJoueur = new StackPane[2];
    
    StackPane[] nomsJoueur = new StackPane[2];
    int[] typesJoueur = new int[2];
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {     
        imagesJoueur[ROUGE] = imagesRouge;  
        nomsJoueur[ROUGE] = nomRouge;
        TextField textfield = (TextField) nomRouge.getChildren().get(0); 
        textfield.setVisible(true);
        textfield.setAlignment(Pos.CENTER);
        textfield.setText("Joueur " + " " + COULEUR[ROUGE]);      
        typesJoueur[ROUGE] = 0;
        
        /*
        imagesJoueur[VERT] = imagesVert;  
        nomsJoueur[VERT] = nomVert;
        textfield = (TextField) nomVert.getChildren().get(0); 
        textfield.setVisible(true);
        textfield.setAlignment(Pos.CENTER);
        textfield.setText("Joueur " + " " + COULEUR[VERT]);   
        typesJoueur[VERT] = 0;
   */
    
    }
    
    
    public void changerJoueur(Event event)
    {
       //QUELLE BOUTON EST CLIQUÉ 
       Button button = (Button) event.getTarget();
       int couleur = -1;
       int rotation = -1;
       if(button == suivantRouge)
       {
           System.out.println("suivant rouge");
           couleur = ROUGE;
           rotation = 1;
           
       }
       else if(button == precedentRouge)
       {
           System.out.println("precedent rouge");
           couleur = ROUGE;
           rotation = -1;
       } else if(button == precedentVert)
       {
           System.out.println("precedent vert");
           couleur = VERT;
           rotation = -1;
       } else if(button == suivantVert)
       {
           System.out.println("precedent vert");
           couleur = VERT;
           rotation = 1;
       }
       
       changerAffichage(couleur,rotation);
    }

    private void changerAffichage(int couleur, int rotation) {
        
        StackPane images = imagesJoueur[couleur];
        typesJoueur[couleur] =  (typesJoueur[couleur] + rotation) % 2;
        if(typesJoueur[couleur] <0){ typesJoueur[couleur] = 1;}
        for (int i = 0; i< images.getChildren().size() ; i++){
    		if (i==typesJoueur[couleur]){
    			images.getChildren().get(i).setVisible(true);
    		}else{
    			images.getChildren().get(i).setVisible(false);
    		}
    	}
        
        StackPane nom = nomsJoueur[couleur];
        if(typesJoueur[couleur] == 0)
        {
            TextField textfield = (TextField) nom.getChildren().get(0); 
            textfield.setVisible(true);
            textfield.setText("Joueur " + " " + COULEUR[couleur]);
            nom.getChildren().get(1).setVisible(false);
        }else
        {
            nom.getChildren().get(0).setVisible(false);
            Text text = (Text) nom.getChildren().get(1);
            text.setVisible(true);
            text.setText(TYPEJOUEUR[ typesJoueur[couleur] ] + " " + COULEUR[couleur]);   
        }
    }
    
    
}
