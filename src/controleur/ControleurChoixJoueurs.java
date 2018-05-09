package controleur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import modele.Joueur;
import modele.JoueurHumain;
import modele.JoueurIAAleatoire;
import modele.JoueurIADifficile;
import modele.JoueurIAMoyenne;

public class ControleurChoixJoueurs extends ControleurBase
{    
   
    
    int ROUGE = 0;
    int VERT = 1;
    int BLEU = 2;
    int JAUNE = 3;
    String[] TYPEJOUEUR = {"JOUEUR", "IA FACILE", "IA MOYENNE", "IA DIFFICILE", "AUCUN"};
    String[] COULEUR = {"BLEU","JAUNE","ROUGE","VERT"};
    int JOUEURREEL = 0;    
    int IAFACILE = 1;
    int IAMOYENNE = 2;
    int IADIFFICILE = 3;
    int AUCUNJOUEUR = 4;
    
    int nombreDeJoueurs = 2;
    
    @FXML
    private Button btnCommencer;
    @FXML
    private Label labelMessage;
    
    @FXML
    private Button precedentRouge,suivantRouge,precedentVert,suivantVert,precedentBleu,suivantBleu,precedentJaune,suivantJaune;
    @FXML
    private StackPane imagesRouge ,nomRouge,imagesVert,nomVert,imagesBleu,nomBleu,imagesJaune,nomJaune;
    
    StackPane[] imagesJoueur = new StackPane[4];
    
    StackPane[] nomsJoueur = new StackPane[4];
    int[] typesJoueur = new int[4];
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {     
        imagesJoueur[ROUGE] = imagesRouge;  
        nomsJoueur[ROUGE] = nomRouge;
        TextField textfield = (TextField) nomRouge.getChildren().get(0); 
        textfield.setVisible(true);
        textfield.setAlignment(Pos.CENTER);
        textfield.setText("Joueur " + " " + COULEUR[ROUGE]);      
        typesJoueur[ROUGE] = JOUEURREEL;
   
        
        imagesJoueur[VERT] = imagesVert;  
        nomsJoueur[VERT] = nomVert;
        textfield = (TextField) nomVert.getChildren().get(0); 
        textfield.setVisible(true);
        textfield.setAlignment(Pos.CENTER);
        textfield.setText("Joueur " + " " + COULEUR[VERT]);   
        typesJoueur[VERT] = JOUEURREEL;
        
        imagesJoueur[BLEU] = imagesBleu;  
        nomsJoueur[BLEU] = nomBleu; 
        typesJoueur[BLEU] = AUCUNJOUEUR;
   
        
        imagesJoueur[JAUNE] = imagesJaune;  
        nomsJoueur[JAUNE] = nomJaune;  
        typesJoueur[JAUNE] = AUCUNJOUEUR;
   
    
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
       } 
       else if(button == precedentVert)
       {
           System.out.println("precedent vert");
           couleur = VERT;
           rotation = -1;
       } 
       else if(button == suivantVert)
       {
           System.out.println("suivant vert");
           couleur = VERT;
           rotation = 1;
       }
       else if(button == suivantBleu)
       {
           System.out.println("suivant bleu");
           couleur = BLEU;
           rotation = 1;
       }  
       else if(button == precedentBleu)
       {
           System.out.println("precedent bleu");
           couleur = BLEU;
           rotation = -1;
       }
        else if(button == suivantJaune)
       {
           System.out.println("suivant jaune");
           couleur = JAUNE;
           rotation = 1;
       }  
       else if(button == precedentJaune)
       {
           System.out.println("precedent jaune");
           couleur = JAUNE;
           rotation = -1;
       }  
       if(couleur != -1)
       {
            changerAffichage(couleur,rotation);
            estPartieConforme();
       }
      
    }

    private void changerAffichage(int couleur, int rotation) {
        
        StackPane images = imagesJoueur[couleur];
        typesJoueur[couleur] =  (typesJoueur[couleur] + rotation) % TYPEJOUEUR.length;
        if(typesJoueur[couleur] <0){ typesJoueur[couleur] = TYPEJOUEUR.length-1;}
        for (int i = 0; i< images.getChildren().size() ; i++){
    		if (i==typesJoueur[couleur]){
    			images.getChildren().get(i).setVisible(true);
    		}else{
    			images.getChildren().get(i).setVisible(false);
    		}
    	}
        
        StackPane nom = nomsJoueur[couleur];
          
        if(typesJoueur[couleur] == JOUEURREEL)
        {
            TextField textfield = (TextField) nom.getChildren().get(0); 
            textfield.setVisible(true);
            textfield.setAlignment(Pos.CENTER);
            textfield.setText("Joueur " + " " + COULEUR[couleur]);
            nom.getChildren().get(1).setVisible(false);
            
        }
        else             
        {
            String message =  TYPEJOUEUR[ typesJoueur[couleur] ];
            nom.getChildren().get(0).setVisible(false);
            if(typesJoueur[couleur] == IAFACILE)
            { 
                message += " " +  COULEUR[couleur]  ; 
            }
            Text text = (Text) nom.getChildren().get(1);
            text.setVisible(true);
            text.setText(message);   
        }
    }
    
    @FXML
    private void clicCommencer(ActionEvent event)
    {
        /*int nbJoueurs = 0;
        for(int typeJoueur : typesJoueur)
        {
            if(typeJoueur != AUCUNJOUEUR)
            {
                nbJoueurs++;
            }
        }*/ 
        Joueur[] joueurs = new Joueur[nombreDeJoueurs];
        int i = 0;
        for(int j = 0; j < typesJoueur.length;j++)
        {
            
            int typeJoueur  = typesJoueur[j];
            if(typeJoueur != AUCUNJOUEUR)
            {

                
                if(typeJoueur == JOUEURREEL)
                {
                    joueurs[i] = new JoueurHumain();
                    TextField tf = (TextField) nomsJoueur[j].getChildren().get(0);
                    joueurs[i].nom = tf.getText();
                    joueurs[i].couleur = j;

                }
                else if(typeJoueur == IAFACILE)
                {
                   joueurs[i] = new JoueurIAAleatoire();
                }
                else if(typeJoueur == IAMOYENNE)
                {
                    joueurs[i] = new JoueurIAMoyenne();
                }
                else if(typeJoueur == IADIFFICILE)
                {
                    joueurs[i] = new JoueurIADifficile();
                }
                i++;
            }
        }
        navigation.moteur.setJoueurs(joueurs);
        navigation.changerVue(ControleurJeu.class);
    }
    
    @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.changerVue(ControleurMenuPrincipal.class);
    }

    private void estPartieConforme() {
        int nbJoueurs = 0;
        for(int i=0; i < typesJoueur.length;i++ )
        {
            if(typesJoueur[i] != AUCUNJOUEUR )
            {
                nbJoueurs++;
            }
        }
        nombreDeJoueurs = nbJoueurs ;
        if(nbJoueurs < 2 ) //nombre de joueur minimum non ok 
        {
            btnCommencer.setDisable(true);
            labelMessage.setVisible(true);
        }else{
            labelMessage.setVisible(false);
            btnCommencer.setDisable(false);
        }
    }
    
    
}
