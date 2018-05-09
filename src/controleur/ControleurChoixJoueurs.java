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
    
    int BLEU = 0;
    int JAUNE = 1;
    int ROUGE = 2;
    int VERT = 3;
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
   
    
    public void setJoueur(int couleur, StackPane imagesCouleur,StackPane nomCouleur,int typeJoueur)
    {
        TextField textfield = null;
        Text text = null;
        
        imagesJoueur[couleur] = imagesCouleur; 
        typesJoueur[couleur] = typeJoueur;
        nomsJoueur[couleur] = nomCouleur;
        setImage(imagesCouleur,typeJoueur);
        
        if(typeJoueur == JOUEURREEL){
            textfield = (TextField) nomCouleur.getChildren().get(0); 
            textfield.setVisible(true);
            textfield.setAlignment(Pos.CENTER);
            textfield.setText("Joueur " + " " + COULEUR[couleur]);  

            text = (Text) nomCouleur.getChildren().get(1); 
            text.setVisible(false);
        }
        else{
            String message =  TYPEJOUEUR[ typesJoueur[couleur] ];
            nomCouleur.getChildren().get(0).setVisible(false);
            if(typesJoueur[couleur] == IAFACILE || typesJoueur[couleur] == IAMOYENNE || typesJoueur[couleur] == IADIFFICILE )
            { 
                message += " " +  COULEUR[couleur]  ; 
            }
            
            text = (Text) nomCouleur.getChildren().get(1); 
            text.setText(message);
            text.setVisible(true);
        }
     
    }    
    @Override
    public void onAppearing()
    {  
        nombreDeJoueurs = 2;
        
        setJoueur(BLEU,imagesBleu,nomBleu,JOUEURREEL);
        setJoueur(JAUNE,imagesJaune,nomJaune,JOUEURREEL);
        setJoueur(ROUGE,imagesRouge,nomRouge,AUCUNJOUEUR);
        setJoueur(VERT,imagesVert,nomVert,AUCUNJOUEUR);
       
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {     
        onAppearing();
       
    }
    
    
    public void setImage(StackPane imagesJoueurCouleur, int typeJoueur)
    {
        for(int i =0;i<imagesJoueurCouleur.getChildren().size();i++)
        {
            if( i == typeJoueur)
            {
                imagesJoueurCouleur.getChildren().get(i).setVisible(true);
            }         
            else
            {
                imagesJoueurCouleur.getChildren().get(i).setVisible(false);
            }
        }
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
        
        
        int nouveauType =  (typesJoueur[couleur] + rotation) % TYPEJOUEUR.length;
        
        if(nouveauType < 0){ nouveauType = TYPEJOUEUR.length-1;}
        
        setJoueur(couleur,imagesJoueur[couleur],nomsJoueur[couleur],nouveauType);
        
    }
    
    @FXML
    private void clicCommencer(ActionEvent event)
    {

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
                    joueurs[i].couleur = j;
                }
                else if(typeJoueur == IAMOYENNE)
                {
                    joueurs[i] = new JoueurIAMoyenne();
                     joueurs[i].couleur = j;
                }
                else if(typeJoueur == IADIFFICILE)
                {
                    joueurs[i] = new JoueurIADifficile();
                     joueurs[i].couleur = j;
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
