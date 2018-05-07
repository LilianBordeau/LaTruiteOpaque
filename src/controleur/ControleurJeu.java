package controleur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import modele.Case;
import modele.Constantes;
import modele.Plateau;
import modele.Point;

public class ControleurJeu  extends ControleurBase {
    private static final String DEBUTIDTUILE = "c";
    private static final String DEBUTIDPINGOUIN = "p";
    private static final String SEPARATEURID = "_";
    public ImageView[][] pingouins;
    public ImageView[][] tuiles;
    
    @FXML
    public AnchorPane anchorPane;
    
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        anchorPane.getId(); // a supprimer
        Case[][] plateau = navigation.moteur.plateau.plateau;
        for(int i = 0 ; i < plateau.length ; i++)
        {
            for(int j = 0 ; j < Plateau.nbTuilesLigne(i) ; j++)
            {
                ImageView tuileGraphique = (ImageView)anchorPane.lookup("#"+indicesToId(i,j, DEBUTIDTUILE));
                String nomImage = Constantes.nomImageCase(plateau[i][j]);
                tuileGraphique.setImage(new Image(nomImage));                    
            }
        }
    }
    
    @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.changerVue(ControleurMenuPrincipal.class);
    }
    
    private Point idToIndices(String id)
    {        
        int indiceSeparateur = id.indexOf(SEPARATEURID);
        int ligne = Integer.parseInt(id.substring(DEBUTIDTUILE.length(), indiceSeparateur));
        int colonne = Integer.parseInt(id.substring(indiceSeparateur+1, id.length()));
        System.out.println("clic:["+ligne+"]["+colonne+"]");
        return new Point(ligne, colonne);
    }
    
    private String indicesToId(int ligne, int colonne, String prefixeId)
    {
        return prefixeId+ligne+SEPARATEURID+colonne;
    }
    
    @FXML
    private void clicTuile(MouseEvent event)
    {        
        
        ImageView tuileGraphique = (ImageView)event.getSource();
        Point coordonnees = idToIndices(tuileGraphique.getId()); 
        if(navigation.moteur.jouer(coordonnees.ligne, coordonnees.colonne))
        {            
            String idPingouin = indicesToId(coordonnees.ligne, coordonnees.colonne, DEBUTIDPINGOUIN);        
            ImageView pingouinGraphique = (ImageView)anchorPane.getScene().lookup("#"+idPingouin);
            String nomImage = Constantes.nomImagePingouin(navigation.moteur.plateau.plateau[coordonnees.ligne][coordonnees.colonne]);
            pingouinGraphique.setImage(new Image(nomImage));
        }        
        else
        {
            System.out.println("Cette case ne peut pas accueillir de pingouin");
        }
        /*ImageViewPane tuileGraphique = (ImageViewPane)event.getSource();
        Image image = tuileGraphique.getImageView().getImage();
        PixelReader reader = image.getPixelReader();
        double xRatio = image.getWidth()/tuileGraphique.getWidth();
        double yRatio = image.getHeight()/tuileGraphique.getHeight();
        double xImage = event.getX()*xRatio;
        double yImage = event.getY()*yRatio;
        boolean aGauche = (event.getX() <= tuileGraphique.getWidth()/2.0);
        boolean enHaut = (event.getY() <= tuileGraphique.getHeight()/2.0);
        boolean dansHexagone = reader.getColor((int)xImage, (int)yImage).getOpacity() > Double.MIN_VALUE;
        if(!dansHexagone)
        {
            if(enHaut)
            {
                System.out.print("Tuile en haut a ");
                if(aGauche)
                {
                    System.out.print("gauche de ");
                }
                else
                {
                    System.out.print("droite de ");
                }
            }
            else
            {
                System.out.print("Tuile en bas a ");
                if(aGauche)
                {
                    System.out.print("gauche de ");
                }
                else
                {
                    System.out.print("droite de ");
                }
            }
        }
        else
        {
            
            System.out.print("Dans ");
        }
        System.out.println(tuileGraphique.getId());*/
        
        
    }
   
    /*@FXML
    private AnchorPane paneTuiles;
    
    @FXML
    public void clicTuile(MouseEvent event)
    {
        System.out.println(((ImageView)event.getSource()).getId()+"clicTuile:"+event.getX()+","+event.getY());
        Scene scene = paneTuiles.getScene();
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                double ratio = newSceneWidth.doubleValue()/oldSceneWidth.doubleValue();
                for(Node node : paneTuiles.getChildren())
                {
                   ImageView tuile = (ImageView)node;
                   tuile.setFitWidth(tuile.getFitWidth()*ratio);
                   tuile.setLayoutX(tuile.getLayoutX()*ratio);
                   tuile.setLayoutY(tuile.getLayoutY()*ratio);
                }
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                double ratio = newSceneHeight.doubleValue()/oldSceneHeight.doubleValue();
                for(Node node : paneTuiles.getChildren())
                {
                    ImageView tuile = (ImageView)node;
                    tuile.setFitHeight(tuile.getFitHeight()*ratio);
                    tuile.setLayoutX(tuile.getLayoutX()*ratio);
                    tuile.setLayoutY(tuile.getLayoutY()*ratio);
                }
            }
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //paneTuiles.pickOnBoundsProperty().set(true);
    }    
    */

    
}
