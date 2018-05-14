package controleur;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import modele.Case;
import modele.Constantes;
import modele.Joueur;
import modele.Plateau;
import modele.Point;

public class ControleurJeu  extends ControleurBase {
    private static final String DEBUTIDTUILE = "c";
    private static final String DEBUTIDPINGOUIN = "p";
    private static final String SEPARATEURID = "_";
    public ImageView[][] pingouins;
    public ImageView[][] tuiles;
    public Point pingouinSel;
    public ImageView pingouinMvt;
    public ArrayList<Point> casesAccessibles;
    ImageView pingouinFantome;
    ImageView tuileFantome;    
    Line lineFantome;

    @FXML
    public AnchorPane anchorPane;
    
    @FXML
    public Label infosJeu;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        pingouinMvt =  new ImageView();
        lineFantome = new Line();
        lineFantome.setMouseTransparent(true);
        lineFantome.setOpacity(0.35);
        lineFantome.setStrokeWidth(5);
        anchorPane.getChildren().add(lineFantome);
        anchorPane.getChildren().add(pingouinMvt);
        anchorPane.getId(); // a supprimer
        Case[][] plateau = navigation.moteur.plateau.plateau;
        for(int i = 0 ; i < plateau.length ; i++)
        {
            for(int j = 0 ; j < Plateau.nbTuilesLigne(i) ; j++)
            {
                miseAJourTuile(i,j);                   
            }
        }
        casesAccessibles = new ArrayList<>();

        miseAJourInfoJeu();
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
        ArrayList<Point> nouveauxPingouinsBloques = null;
        if(!navigation.moteur.pingouinsPlaces())
        {        
            nouveauxPingouinsBloques = navigation.moteur.placerPingouin(coordonnees.ligne, coordonnees.colonne);
            if(nouveauxPingouinsBloques != null)
            {
                miseAJourPingouin(coordonnees.ligne, coordonnees.colonne);
            }   
            else
            {
                System.out.println("Cette case ne peut pas accueillir de pingouin");
            }
        }        
        else
        {
              
            if(navigation.moteur.contientJoueurCourant(coordonnees.ligne,coordonnees.colonne))
            {
                suprimerCasesAccessible();
                pingouinSel = new Point(coordonnees.ligne,coordonnees.colonne);
                System.out.println("pingouin selectionne : "+pingouinSel);
                ArrayList<Point> deplacements = navigation.moteur.deplacementsPossibles(coordonnees.ligne, coordonnees.colonne);
                for(Point depl : deplacements)
                {
                    String idTuile = indicesToId(depl.ligne, depl.colonne, DEBUTIDTUILE);        
                    ImageView tuileDepl = (ImageView)anchorPane.getScene().lookup("#"+idTuile);
                    //tuileDepl.setImage(null);
                    afficherCaseAccessible(depl);

                }
            }
            else if(pingouinSel != null)
            {       
                nouveauxPingouinsBloques = navigation.moteur.deplacerPingouin(pingouinSel.ligne, pingouinSel.colonne, coordonnees.ligne, coordonnees.colonne);
                if(nouveauxPingouinsBloques != null)
                {
                    onTranslateDuPinguoin(coordonnees.ligne, coordonnees.colonne);

                    suprimerCasesAccessible();
                    miseAJourPingouin(pingouinSel.ligne, pingouinSel.colonne);
                    miseAJourTuile(pingouinSel.ligne, pingouinSel.colonne); 
               //     miseAJourPingouin(coordonnees.ligne, coordonnees.colonne);
                    pingouinSel = null;
                }
            }
        }
        if(nouveauxPingouinsBloques != null)
        {
            for(Point point : nouveauxPingouinsBloques)
            {
                miseAJourPingouin(point.ligne, point.colonne);
                miseAJourTuile(point.ligne, point.colonne);
            }
        }
        miseAJourInfoJeu();
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
    */

    private void miseAJourInfoJeu()
    {
        String texteInfoJeu = null;
        if(navigation.moteur.estPartieTerminee())
        {
            texteInfoJeu = "Partie terminée ! Gagnant(s) : ";
            for(Joueur joueur : navigation.moteur.joueursGagnants())
            {
                texteInfoJeu += "J"+joueur.numero+" ("+joueur.scorePoisson+"p et "+joueur.scoreTuile+"t), ";
            }            
        }
        else
        {
            texteInfoJeu = "Tour du joueur "+navigation.moteur.joueurCourant+"(";
            for(Joueur joueur : navigation.moteur.joueurs)
            {
                texteInfoJeu += "J"+joueur.numero+" : "+joueur.scorePoisson+"p et "+joueur.scoreTuile+"t, ";
            }
            texteInfoJeu += ")";
        }
        infosJeu.setText(texteInfoJeu);
        
    }

    private void miseAJourTuile(int i, int j)
    {
        ImageView tuileGraphique = (ImageView)anchorPane.lookup("#"+indicesToId(i,j, DEBUTIDTUILE));
        Case tuile = navigation.moteur.plateau.plateau[i][j];
        Image image = null;
        if(!tuile.estCoulee())
        {
            image = new Image(Constantes.nomImageCase(tuile));
        }
        tuileGraphique.setImage(image);
    }

    private void miseAJourPingouin(int i, int j)
    {
        String idPingouin = indicesToId(i, j, DEBUTIDPINGOUIN);        
        ImageView pingouinGraphique = (ImageView)anchorPane.getScene().lookup("#"+idPingouin);
        Case tuile = navigation.moteur.plateau.plateau[i][j];
        Image image = null;
        if(tuile.estOccupee() && !tuile.pingouin.estBloque)
        {
            String nomImage = Constantes.nomImagePingouin(navigation.moteur.joueurs[tuile.numJoueurPingouin()]);
            image = new Image(nomImage);
        }
        pingouinGraphique.setImage(image);
    }

    
    private void afficherCaseAccessible(Point depl) {
        
        String idAccessible = indicesToId(depl.ligne,depl.colonne,"a");
        ImageView caseAccessible = (ImageView)anchorPane.getScene().lookup("#"+idAccessible);
        Image image = new Image("Images/accessible" + navigation.moteur.joueurCourant + ".png");
        caseAccessible.setImage(image);
        casesAccessibles.add(depl);
        

    }

    private void suprimerCasesAccessible() {
                Iterator it = casesAccessibles.iterator();
                while(it.hasNext())
                {   
                   Point depl = (Point) it.next();
                   String idAccessible = indicesToId(depl.ligne,depl.colonne,"a");
                   ImageView pingouinGraphique = (ImageView)anchorPane.getScene().lookup("#"+idAccessible);
                   pingouinGraphique.setImage(null);
                   it.remove();
                }    
    }

    private void onTranslateDuPinguoin(int ligne, int colonne) {
        if(pingouinFantome != null)
        {
            pingouinFantome.setImage(null);  
            pingouinFantome.setVisible(false);
            tuileFantome.setImage(null); 
            tuileFantome.setVisible(false);
            lineFantome.setVisible(false);
        }
        String idPingouin = indicesToId(pingouinSel.ligne, pingouinSel.colonne,DEBUTIDPINGOUIN);
        ImageView pingouinGraphique = (ImageView)anchorPane.getScene().lookup("#"+idPingouin);
        
        
        String idCaseDest = indicesToId(ligne, colonne,DEBUTIDPINGOUIN);
        ImageView caseDest = (ImageView)anchorPane.getScene().lookup("#"+idCaseDest);
        
        Point coordonnees = idToIndices(pingouinGraphique.getId());   
        
        Case tuile = navigation.moteur.plateau.plateau[ligne][colonne];
        String nomImage = Constantes.nomImagePingouin(navigation.moteur.joueurs[tuile.numJoueurPingouin()]);
        Image image = new Image(nomImage);
      
        
        pingouinMvt.setLayoutX(pingouinGraphique.getLayoutX());
        pingouinMvt.setLayoutY(pingouinGraphique.getLayoutY());
        pingouinMvt.setFitHeight(pingouinGraphique.getFitHeight());
        pingouinMvt.setFitWidth(pingouinGraphique.getFitWidth());
        pingouinMvt.setPreserveRatio(true);
        pingouinMvt.setVisible(true);
        pingouinMvt.setImage(image);
        
        
        TranslateTransition animation = new TranslateTransition(
                Duration.seconds(0.7), pingouinMvt
        );
     
        Double  distanceY = caseDest.getLayoutY() - pingouinMvt.getLayoutY();
        Double  distanceX = caseDest.getLayoutX() - pingouinMvt.getLayoutX();
        animation.setFromX(0);
        animation.setFromY(0);
        animation.setToY(distanceY);
        animation.setToX(distanceX);
        animation.setAutoReverse(true);
        animation.play();
        
        
        
        animation.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                pingouinMvt.setImage(null);
                pingouinMvt.setVisible(false);
                miseAJourPingouin(ligne, colonne);
                montrerDernierCoup(coordonnees.ligne, coordonnees.colonne,ligne,colonne);


            }
        });
  
    }

    private void  montrerDernierCoup(int xSource,int ySource, int xDest, int yDest)
    {
        
        String idCasePinguoin = indicesToId(xDest, yDest,DEBUTIDTUILE);
        ImageView pingouinCourant = (ImageView)anchorPane.getScene().lookup("#"+idCasePinguoin);
        
        
        String idCaseSource = indicesToId(xSource, ySource,DEBUTIDTUILE);
        tuileFantome = (ImageView)anchorPane.getScene().lookup("#"+idCaseSource);
        Image image = new Image("Images/tuileCoule.png") ;
        
        tuileFantome.setImage(image);
        
        String idCaseSourcePinguouin = indicesToId(xSource, ySource,DEBUTIDPINGOUIN);
        pingouinFantome = (ImageView)anchorPane.getScene().lookup("#"+idCaseSourcePinguouin);
        Image image2 = new  Image("Images/pingouins/fantome.png") ;        
        pingouinFantome.setImage(image2);

        
        
        double xDep = pingouinCourant.getLayoutX() + pingouinCourant.getFitWidth()/2;
        double yDep = pingouinCourant.getLayoutY() + pingouinCourant.getFitHeight()/2;
        double xArr = tuileFantome.getLayoutX() + tuileFantome.getFitWidth()/2;
        double yArr = tuileFantome.getLayoutY() + tuileFantome.getFitHeight()/2;
        

        lineFantome.setStartX(xDep);
        lineFantome.setStartY(yDep);
        lineFantome.setEndX(xArr);
        lineFantome.setEndY(yArr);
               lineFantome.setVisible(true);
        
        
        
        
    }
}
