package controleur;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import modele.Point;
import java.util.Iterator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import modele.Joueur;
import modele.JoueurHumain;
import modele.Moteur;
import modele.Plateau;
import  modele.Case;
import modele.Constantes;
import modele.Coup;
import modele.Deplacement;
import modele.Pingouin;




public class ControleurTutoriel extends ControleurBase
{     //private boolean estEnAttente;
    Moteur moteur;
    public Point pingouinSel;
    public ArrayList<Point> casesAccessibles;
    private int etape = 0;
    private String[] tab;
    private static final String DEBUTIDTUILE = "c";
    private static final String DEBUTIDPINGOUIN = "p";
    private static final String DEBUTIDCASEACCESSIBLE = "a";
    private static final String SEPARATEURID = "_";
    private boolean canDeplace;
    private boolean canPlace;
    ImageView tuileFantome;   
    public ImageView pingouinMvt;
    
    private Line lineFantome;
    ImageView pingouinFantome;
    
    int lPenguin,cPenguin;
   
 @FXML
    public AnchorPane anchorPane;
    
    @FXML
    public Label labelTitre;
    
    @FXML
    public Button suivant,choixPartie;
    @FXML
    public ImageView imgv;
    public ImageView img;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {   
        canDeplace = false;
        canPlace = false;
        Rule_of_game();
        casesAccessibles = new ArrayList<>();  
        labelTitre.setAlignment(Pos.CENTER);
        labelTitre.setText(tab[etape]);
        pingouinMvt =  new ImageView();
        lineFantome = new Line();
        lineFantome.setMouseTransparent(true);
        lineFantome.setOpacity(0.5);
        lineFantome.setStrokeWidth(5);
        lineFantome.setMouseTransparent(true);
        lineFantome.setVisible(false);
        anchorPane.getChildren().add(pingouinMvt);
        anchorPane.getChildren().add(lineFantome);
    }
    
    @Override
    public void onAppearing(){
    suivant.setVisible(true);
    suivant.setDisable(false);
    
    
    for(Node element : anchorPane.getChildren())
    {
        element.setVisible(true);
        
    }
        Label label1 = (Label) anchorPane.lookup("#0_tuille");
        label1.setText("0");
        lineFantome.setVisible(false);
        suprimerCasesAccessible();
        etape=0;
        labelTitre.setText(tab[etape]);
       
        Joueur[] joueurs = new Joueur[]{new JoueurHumain(), new JoueurHumain()};
        moteur = new Moteur(joueurs);
        for(int i = 0 ; i < moteur.plateau.plateau.length ; i++)
        {
            for(int j = 0 ; j < moteur.plateau.nbTuilesLigne(i) ; j++)
            {
                if(i >= 4 || j >= 4)
                {
                    moteur.plateau.plateau[i][j].nbPoissons = 1; 
                }

            }
        }
                    moteur.placerPingouin(4,4);   
                    moteur.placerPingouin(4,5);  
                    moteur.placerPingouin(4,6);  
                    moteur.placerPingouin(5,4);  	
                    moteur.placerPingouin(5,5);   
                    moteur.placerPingouin(5,6);  
                    moteur.placerPingouin(6,4);
        for(Joueur joueur : moteur.joueurs)
        {
            for(Pingouin pingouin : joueur.pingouins)
            {
                pingouin.estBloque = true;
            }
        }
        for(int i = 0 ; i < moteur.plateau.plateau.length ; i++)
        {
            for(int j = 0 ; j < Plateau.nbTuilesLigne(i) ; j++)
            {
                if(i >= 4 || j >= 4)
                {
                    moteur.plateau.plateau[i][j].nbPoissons = 0; 
                }					
            }
        }
        moteur.plateau.plateau[0][0].nbPoissons = 1; 
        for(Pingouin pingouin : moteur.joueurs[0].pingouins)
        {
            pingouin.estBloque = true;
        }
        moteur.sauvegarderCoupJoues = false;
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                if(anchorPane.lookup("#"+indicesToId(i,j, DEBUTIDTUILE)) != null)
                {
                    ImageView tuileGraphique = (ImageView)anchorPane.lookup("#"+indicesToId(i,j, DEBUTIDTUILE));
                    ImageView pASupp = (ImageView)anchorPane.lookup("#"+indicesToId(i,j, DEBUTIDPINGOUIN));
                    pASupp.setImage(null);
                    Case tuile = moteur.plateau.plateau[i][j];
                    Image image = null;
                    if(!tuile.estCoulee())
                    {
                        image = new Image(Constantes.nomImageCase(tuile));
                    }
                    tuileGraphique.setImage(image);
                    tuileGraphique.setVisible(true);
                  
                      
                    
                }

            }
        }


        ArrayList<Integer> couleursPrises = new ArrayList<>();
        for(int i = 0 ; i<1 ; i++ )
        {
            couleursPrises.add(moteur.joueurs[i].couleur);
            ImageView joueurExistant = (ImageView) anchorPane.lookup("#"+moteur.joueurs[i].couleur+"_gui");                
            joueurExistant.setVisible(true);
            joueurExistant.setImage(new Image("Images/gui/"+moteur.joueurs[i].couleur+"_gui_afk.png"));
            System.out.println(moteur.joueurs[i].nom);
        }
        for(int i = 0 ; i<1 ; i++ )
        {
            if(!couleursPrises.contains(i))
            {

                ImageView joueurInexistant = (ImageView) anchorPane.lookup("#"+i+"_gui");
                joueurInexistant.setVisible(false);
                Label label = (Label) anchorPane.lookup("#"+i+"_score");
                Label label2 = (Label) anchorPane.lookup("#"+i+"_tuille");                
                label.setText("");
                label2.setText("");

            }

        }
        choixPartie.setVisible(false);   
    }  
    
     @FXML
     private void clicTuile(MouseEvent event)
    {        
        if(etape == 1 || etape >= 3  ){             
            ImageView tuileGraphique = (ImageView)event.getSource();
            Point coordonnees = idToIndices(tuileGraphique.getId()); 
            System.out.print(moteur.deplacementsPossibles(coordonnees.ligne, coordonnees.colonne));
            ArrayList<Point> nouveauxPingouinsBloques = null;
          
            if(!moteur.pingouinsPlaces())
            {     
                
                canPlace=true;
                nouveauxPingouinsBloques = moteur.placerPingouin(coordonnees.ligne, coordonnees.colonne);
               
                if(nouveauxPingouinsBloques != null)
                {
                    suprimerCasesAccessible();
                    suivant.setDisable(!canPlace);
                    miseAJourPingouin(coordonnees.ligne, coordonnees.colonne);
                }   
                else
                {
                    System.out.println("Cette case ne peut pas accueillir de pingouin");
                }
            }        
            else
            {
                if(etape >= 3 && canDeplace)
                {
                    if(moteur.contientJoueurCourant(coordonnees.ligne,coordonnees.colonne))
                    {                    
                        suprimerCasesAccessible();
                        pingouinSel = new Point(coordonnees.ligne,coordonnees.colonne);
                        ArrayList<Point> deplacements = moteur.deplacementsPossibles(coordonnees.ligne, coordonnees.colonne);
                        for(Point depl : deplacements)
                        {
                            String idTuile = indicesToId(depl.ligne, depl.colonne, DEBUTIDTUILE);     
                            Scene scene = anchorPane.getScene();
                            if(scene == null)
                            {
                                return;
                            }
                            ImageView tuileDepl = (ImageView)scene.lookup("#"+idTuile);
                            //tuileDepl.setImage(null);

                            afficherCaseAccessible(depl);

                        }
                    }
                    else if(pingouinSel != null)
                    {      
                        
                        nouveauxPingouinsBloques = moteur.deplacerPingouin(pingouinSel.ligne, pingouinSel.colonne, coordonnees.ligne, coordonnees.colonne);
                        Label labelScore = (Label) anchorPane.lookup("#0_score");
                        labelScore.setText(Integer.toString(moteur.joueurs[moteur.joueurCourant].scorePoisson));
                        Label labeltuille = (Label) anchorPane.lookup("#0_tuille");
                        labeltuille.setText(Integer.toString(moteur.joueurs[moteur.joueurCourant].scoreTuile));
                       
                        if(nouveauxPingouinsBloques != null)
                        {
                            suprimerCasesAccessible();
                            miseAJourPingouin(pingouinSel.ligne, pingouinSel.colonne);
                            miseAJourTuile(pingouinSel.ligne, pingouinSel.colonne); 
                            pingouinSel = null;
                        }
                        
                        if(etape == 3 || moteur.estPartieTerminee() )
                        {
                            suivant.setDisable(false);
                            canDeplace = false;
                        }
                        
                    }
                }
                
            }
            if(nouveauxPingouinsBloques != null)
            {
                finDuTour(nouveauxPingouinsBloques);
            }
              
        }
    }

    
    

    
    public void nextPhrase(){
        if(etape<6)
        {
            etape++;
            if( etape == 1)
            {
                showTuilePlaceble();
                suivant.setDisable(true);
            }
            else if(etape == 3 || etape == 4 )
            {
                suivant.setDisable(true);
                canDeplace = true;
            }

            if(etape == 5)
            {
                suivant.setVisible(false);
                choixPartie.setVisible(true);
            }
        }

        System.out.println("phrase: " + etape);
    }


    public void Rule_of_game(){
        tab = new String[8];
        tab[0] = "Bonjour, jeune padawan. Je suis Obikwak Kenobi et je vais te former à l'art d'amasser du poisson.\n"
                + "L'objectif du jeu est simple : chaque joueur doit attraper un maximum de poissons avec ses pingouins. "
                 +"Mais attention ! Si un pingouin est isolé sur une banquise il ne peut plus jouer et devient par conséquent inutile.";
        
        tab[1] = "Chaque joueur place tour à tour un de ses pingouins sur un bloc de glace contenant un et un seul poisson."
                + " Il ne peut y avoir qu’un seul pingouin par bloc de glace. Pour mieux comprendre cela, clique sur une case avec un poisson pour placer un pingouin.";

        tab[2] = "Le deplacement doit respecter les contraintes suivantes :\n"
                + "* Un pingouin doit se déplacer en ligne droite dans une des 6 directions qui entoure son bloc de glace.\n"
                + "* Un pingouin peut avancer d’autant de cases que le joueur le souhaite.\n"
                + "* Enfin un pingouin ne peut pas franchir d’obtacle.\n";
        
        tab[3] = "Tour à tour, chaque joueur doit déplacer ces pingouin, sélectionne le en cliquant dessus puis clique sur la case où tu veux le déplacer.";
   
        tab[4] ="Essayes de ramasser un maximum de poissons ! " ;

        tab[5] ="Tu es fin prêt, tu peux commencer à jouer. Et surtout n'oublie pas : une truite vaut mieux que deux tu l'auras." ;
    }

    private Point idToIndices(String id)
    {        
        int indiceSeparateur = id.indexOf(SEPARATEURID);
        int ligne = Integer.parseInt(id.substring(DEBUTIDTUILE.length(), indiceSeparateur));
        int colonne = Integer.parseInt(id.substring(indiceSeparateur+1, id.length()));
        return new Point(ligne, colonne);
    }
    
    private String indicesToId(int ligne, int colonne, String prefixeId)
    {
        return prefixeId+ligne+SEPARATEURID+colonne;
    }
    private void miseAJourPingouin(int i, int j)
    {
        
        String idPingouin = indicesToId(i, j, DEBUTIDPINGOUIN);
        ImageView pingouinGraphique = (ImageView)anchorPane.lookup("#"+idPingouin);
        Case tuile = moteur.plateau.plateau[i][j];
        Image image = null;
        if(tuile.estOccupee() && !tuile.pingouin.estBloque)
        {
            String nomImage = Constantes.nomImagePingouin(moteur.joueurs[tuile.numJoueurPingouin()]);
            image = new Image(nomImage);
        }
        pingouinGraphique.setImage(image);
        pingouinGraphique.setVisible(true);
    }
    private void miseAJourTuile(int i, int j)
    {
        ImageView tuileGraphique = (ImageView)anchorPane.lookup("#"+indicesToId(i,j, DEBUTIDTUILE));
        Case tuile = moteur.plateau.plateau[i][j];
        Image image = null;
        if(!tuile.estCoulee())
        {
            image = new Image(Constantes.nomImageCase(tuile));
        }
        tuileGraphique.setImage(image);
        tuileGraphique.setVisible(true);
        ImageView peng = (ImageView)anchorPane.lookup("#p"+i+"_"+j);
        peng.setImage(null);
    }
     public void suprimerCasesAccessible() {
                Iterator it = casesAccessibles.iterator();
                while(it.hasNext())
                {   
                   Point depl = (Point) it.next();
                   miseAJourCaseAccessible(depl, true);
                   it.remove();
                }
    }
     private void miseAJourCaseAccessible(Point uneCase, boolean supprimer)
    {
        ImageView isNull = (ImageView)anchorPane.lookup("#c"+uneCase.ligne+"_"+uneCase.colonne);
        if(isNull.getImage() != null)
        {
            String idAccessible = indicesToId(uneCase.ligne,uneCase.colonne,DEBUTIDCASEACCESSIBLE);
        Scene scene = anchorPane.getScene();
        if(scene == null)
        {
            return;
        }
        ImageView caseAccessible = (ImageView)scene.lookup("#"+idAccessible);
        Image image = null;
        if(!supprimer)
        {
            image = new Image(Constantes.nomImageCaseAccessible(moteur.joueurs[moteur.joueurCourant]));
        }
        caseAccessible.setImage(image);
        }
        
    }
     private void afficherCaseAccessible(Point depl)
    {        
        miseAJourCaseAccessible(depl, false);
        casesAccessibles.add(depl);
    }
     private void finDuTour(ArrayList<Point> nouveauxPingouinsBloques)
    {
        Coup coup = moteur.dernierCoupJoue;   
        System.out.println(coup);                     
        if(coup instanceof Deplacement)
        {
           onTranslateDuPinguoin((Deplacement)coup, nouveauxPingouinsBloques);
            System.out.println("onTranslateDuPinguoin");
        }else
        {
            onCouleDuPingouin(nouveauxPingouinsBloques);   
            System.out.println("onCouleDuPingouin");
        }
   
    }
     
        private void miseAjourPoints(ArrayList<Point> points)
        {
            for(Point point : points)
            {
                miseAJourPingouin(point.ligne, point.colonne);
                miseAJourTuile(point.ligne, point.colonne);
            }
        }
    
    
    public void  montrerDernierCoup(Deplacement dep)
    {     
        String idCasePinguoin = indicesToId(dep.ligneDest, dep.colonneDest,DEBUTIDTUILE);
        ImageView pingouinCourant = (ImageView)anchorPane.lookup("#"+idCasePinguoin);       
        
        String idCaseSource = indicesToId(dep.ligneSrc, dep.colonneSrc,DEBUTIDTUILE);
        tuileFantome = (ImageView)anchorPane.lookup("#"+idCaseSource);

        String idCaseSourcePinguouin = indicesToId(dep.ligneSrc, dep.colonneSrc,DEBUTIDPINGOUIN);
        pingouinFantome = (ImageView)anchorPane.lookup("#"+idCaseSourcePinguouin);
        
        double xDep = pingouinCourant.getLayoutX() + pingouinCourant.getFitWidth()/2;
        double yDep = pingouinCourant.getLayoutY() + pingouinCourant.getFitHeight()/2;
        double xArr = tuileFantome.getLayoutX() + tuileFantome.getFitWidth()/2;
        double yArr = tuileFantome.getLayoutY() + tuileFantome.getFitHeight()/2;   

        lineFantome.setStartX(xDep);
        lineFantome.setStartY(yDep);
        lineFantome.setEndX(xArr);
        lineFantome.setEndY(yArr);
        lineFantome.setStroke(Constantes.couleurJoueur(moteur.joueurs[moteur.joueurPrecedent]));
        lineFantome.setVisible(true);   
    }
         
    
    private void onTranslateDuPinguoin(Deplacement dep, ArrayList<Point> nouveauxPingouinsBloques) {

        if(pingouinFantome != null)
        {
            pingouinFantome.setImage(null);  
            //pingouinFantome.setVisible(false);
            tuileFantome.setImage(null); 
            Point coordonneesTuile = idToIndices(tuileFantome.getId());
            miseAJourTuile(coordonneesTuile.ligne, coordonneesTuile.colonne);
            Point coordonneesPingouin = idToIndices(pingouinFantome.getId());
            miseAJourPingouin(coordonneesPingouin.ligne, coordonneesPingouin.colonne);
            /*tuileFantome.setImage(null); 
            tuileFantome.setVisible(false);*/
            lineFantome.setVisible(false);
        }
        montrerDernierCoup(dep);
        String idPingouin = indicesToId(dep.ligneSrc, dep.colonneSrc,DEBUTIDPINGOUIN);
        ImageView pingouinGraphique = (ImageView)anchorPane.lookup("#"+idPingouin);
        
        
        String idCaseDest = indicesToId(dep.ligneDest, dep.colonneDest,DEBUTIDPINGOUIN);
        ImageView caseDest = (ImageView)anchorPane.lookup("#"+idCaseDest);
        
        
        Case tuile = moteur.plateau.plateau[dep.ligneDest][dep.colonneDest];
        String nomImage = Constantes.nomImagePingouin(moteur.joueurs[tuile.numJoueurPingouin()]);
        Image image = new Image(nomImage);
      
        
        pingouinMvt.setLayoutX(pingouinGraphique.getLayoutX());
        pingouinMvt.setLayoutY(pingouinGraphique.getLayoutY());
        pingouinMvt.setFitHeight(caseDest.getFitHeight());
        pingouinMvt.setFitWidth(caseDest.getFitWidth());
        pingouinMvt.setPreserveRatio(true);
        pingouinMvt.setVisible(true);
        pingouinMvt.setImage(image);
        
        
        TranslateTransition animationPingouin = new TranslateTransition(Duration.seconds(0.7), pingouinMvt);
        
        Double  distanceY = caseDest.getLayoutY() - pingouinMvt.getLayoutY();
        Double  distanceX = caseDest.getLayoutX() - pingouinMvt.getLayoutX();
        animationPingouin.setFromX(0);
        animationPingouin.setFromY(0);
        animationPingouin.setToY(distanceY);
        animationPingouin.setToX(distanceX);
        animationPingouin.setAutoReverse(true);
        
        ScaleTransition animationLine = new ScaleTransition(Duration.seconds(0.7), lineFantome);
        animationLine.setFromX(0);
        animationLine.setFromY(0);
        animationLine.setToY(1);
        animationLine.setToX(1);
        
        TranslateTransition translateLine = new TranslateTransition(Duration.seconds(0.7), lineFantome);
        translateLine.setFromX(-distanceX/2.0);
        translateLine.setFromY(-distanceY/2.0);
        translateLine.setToX(0);
        translateLine.setToY(0);
        
        ParallelTransition parallelTransition = new ParallelTransition(animationPingouin, animationLine, translateLine);
        lineFantome.setVisible(true);
        pingouinGraphique.setImage(null);
        parallelTransition.play();
        
        
        
        parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                pingouinMvt.setImage(null);
                pingouinMvt.setVisible(false);
                miseAJourPingouin(dep.ligneDest, dep.colonneDest);
                onCouleDuPingouin(nouveauxPingouinsBloques);                

            }
        });
  
    }
       
     private void onCouleDuPingouin(ArrayList<Point> nouveauxPingouinsBloques){
        miseAjourPoints(nouveauxPingouinsBloques);   
        ParallelTransition parallelTransition = new ParallelTransition();
        for(Point point : nouveauxPingouinsBloques)
        {            
          
            Case tuile = moteur.plateau.plateau[point.ligne][point.colonne];
            if(tuile.numJoueurPingouin() != -1)
            {
                String nomImage = Constantes.nomImagePingouin(moteur.joueurs[tuile.numJoueurPingouin()]);
                System.out.println(nomImage);
                ImageView pingouinGraphique2 = null;
                pingouinGraphique2 = (ImageView)anchorPane.lookup("#"+indicesToId(point.ligne,point.colonne,DEBUTIDPINGOUIN));
                System.out.println(pingouinGraphique2);
                ImageView pingouinFondu = new ImageView(new Image(nomImage));
                anchorPane.getChildren().add(pingouinFondu);
                pingouinFondu.setLayoutX(pingouinGraphique2.getLayoutX());
                pingouinFondu.setLayoutY(pingouinGraphique2.getLayoutY());
                pingouinFondu.setFitHeight(pingouinGraphique2.getFitHeight());
                pingouinFondu.setFitWidth(pingouinGraphique2.getFitWidth());
                pingouinFondu.setPreserveRatio(true);
                pingouinFondu.setVisible(true);
                ScaleTransition scaleTransitionMax = new ScaleTransition(Duration.seconds(0.5), pingouinFondu);
                scaleTransitionMax.setFromX(1);
                scaleTransitionMax.setToX(1.5);
                ScaleTransition scaleTransitionMin = new ScaleTransition(Duration.seconds(0.5), pingouinFondu);
                scaleTransitionMin.setFromX(1.5);
                scaleTransitionMin.setToX(0);
                scaleTransitionMin.setFromY(1);
                scaleTransitionMin.setToY(0);
                SequentialTransition sequentialTransition = new SequentialTransition(scaleTransitionMax, scaleTransitionMin);
                parallelTransition.getChildren().add(sequentialTransition);
            } 
        }    
         parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                miseAjourPoints(nouveauxPingouinsBloques);                       
            }
        });
        parallelTransition.play();
     }

    @FXML
    private void clicChoixPartie() {
        navigation.enReseau = false;
        navigation.changerVue(ControleurChoixJoueurs.class);
    }
     @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.changerVue(ControleurMenuPrincipal.class);
    }
    @FXML
    private void clicSuivant(ActionEvent event){
        nextPhrase();
        labelTitre.setText(tab[etape]);  
    }

    private void showTuilePlaceble() {
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                
                Case tuile = moteur.plateau.plateau[i][j];
                if(tuile.peutPlacerPingouin())
                {
                    ImageView accessible = (ImageView)anchorPane.lookup("#"+indicesToId(i,j, DEBUTIDCASEACCESSIBLE));
                    Image imageAccessible = new Image(Constantes.nomImageCaseAccessible(moteur.joueurs[moteur.joueurCourant]));
                    accessible.setImage(imageAccessible);
                    accessible.setVisible(true); 
                    casesAccessibles.add(new Point(i,j));
                }
            }
        }
    }
     
}

