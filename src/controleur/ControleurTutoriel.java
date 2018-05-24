package controleur;

import java.io.Serializable;
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
import controleur.ControleurJeu;
import java.io.IOException;
import java.util.Iterator;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import modele.Joueur;
import modele.JoueurHumain;
import modele.Moteur;
import modele.Plateau;
import static modele.Plateau.*;
import  modele.Case;
import static modele.Case.*;
import modele.Constantes;
import modele.Coup;
import modele.Deplacement;
import modele.JoueurClient;
import modele.JoueurReseau;
import modele.Pingouin;
import thread.MyThread;




public class ControleurTutoriel extends ControleurBase
{     //private boolean estEnAttente;
    Moteur moteur;
    public Point pingouinSel;
    public SimpleBooleanProperty estEnAttente;
    private SimpleBooleanProperty jeuInterrompu;
    public ArrayList<Point> casesAccessibles;
   
    private int mots = 0;
    private String[] Tab;
    private ImageView tuileGraphique;
    private static final String DEBUTIDTUILE = "c";
    private static final String DEBUTIDPINGOUIN = "p";
    private static final String DEBUTIDCASEACCESSIBLE = "a";
    private static final String SEPARATEURID = "_";
    private boolean canDeplace;
    private boolean canPlace;
    public ImageView pingouinMvt;
    private Line lineFantome;
    ImageView tuileFantome;
    ImageView pingouinFantome;
   
 @FXML
    public AnchorPane anchorPane;
    
    @FXML
    public Label labelTitre;
    
    @FXML
    public Button suivant;
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
        labelTitre.setText(Tab[mots]);
        pingouinMvt =  new ImageView();
         lineFantome = new Line();
         lineFantome.setMouseTransparent(true);
        lineFantome.setOpacity(0.5);
        lineFantome.setStrokeWidth(5);
        anchorPane.getChildren().add(lineFantome);
	estEnAttente = new SimpleBooleanProperty(false);
        jeuInterrompu = new SimpleBooleanProperty(false);
    }
    
    @Override
    public void onAppearing(){
    suivant.setDisable(false);
    for(Node element : anchorPane.getChildren())
    {
        element.setVisible(true);
        
    }
        Label label1 = (Label) anchorPane.lookup("#0_tuille");
        label1.setText("0");
        lineFantome.setVisible(false);
       suprimerCasesAccessible();
       mots=0;
       labelTitre.setText(Tab[mots]);
       
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
       Joueur joueurCourant = moteur.joueurs[moteur.joueurCourant];
        miseAJourInfoJeu();
         
    }  
    
    
    @FXML
    private void retourMenu(ActionEvent event)
    {
        navigation.changerVue(ControleurMenuPrincipal.class);
       
        
        
    }
    @FXML
    private void suivant(ActionEvent event)
    {
      
      clicSuivant(event);
       
    }
     @FXML
    private void clicSuivant(ActionEvent event){
        nextPhrase();
        labelTitre.setText(Tab[mots]);  

    }
    int lPenguin,cPenguin;
     @FXML
     private void clicTuile(MouseEvent event)
    {        
        if(mots>=2){             
            ImageView tuileGraphique = (ImageView)event.getSource();
            Point coordonnees = idToIndices(tuileGraphique.getId()); 
            System.out.print(moteur.deplacementsPossibles(coordonnees.ligne, coordonnees.colonne));
            ArrayList<Point> nouveauxPingouinsBloques = null;
            if(!moteur.pingouinsPlaces())
            {     
                canPlace=true;
                nouveauxPingouinsBloques = moteur.placerPingouin(coordonnees.ligne, coordonnees.colonne);
                suivant.setDisable(!canPlace);
                if(nouveauxPingouinsBloques != null)
                {
                    //effacerAmpoule();
                    miseAJourPingouin(coordonnees.ligne, coordonnees.colonne);
                }   
                else
                {
                    System.out.println("Cette case ne peut pas accueillir de pingouin");
                }
            }        
            else
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
                        if(mots>=6)
                        afficherCaseAccessible(depl);

                    }
                }
                else if(pingouinSel != null && mots>=6)
                {      
                    //effacerAmpoule();
                    nouveauxPingouinsBloques = moteur.deplacerPingouin(pingouinSel.ligne, pingouinSel.colonne, coordonnees.ligne, coordonnees.colonne);
                    canDeplace = true;
                    suivant.setDisable(!canDeplace);
                    System.out.println("SCORE JC ::::  " + moteur.joueurs[moteur.joueurCourant].scorePoisson );
                    Label labelScore = (Label) anchorPane.lookup("#0_score");
                    labelScore.setText(Integer.toString(moteur.joueurs[moteur.joueurCourant].scorePoisson));
                    Label labeltuille = (Label) anchorPane.lookup("#0_tuille");
                    labeltuille.setText(Integer.toString(moteur.joueurs[moteur.joueurCourant].scoreTuile));
                    if(mots == 7)
                    {
                        suivant.setDisable(true);
                    }
                    if(nouveauxPingouinsBloques != null)
                    {
                        suprimerCasesAccessible();
                        miseAJourPingouin(pingouinSel.ligne, pingouinSel.colonne);
                        miseAJourTuile(pingouinSel.ligne, pingouinSel.colonne); 
                        miseAJourPingouin(coordonnees.ligne, coordonnees.colonne);
                        pingouinSel = null;
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
        if(mots<7)
        {
            mots++;
            if(mots == 2 || mots == 6 || mots == 7)
            {
                suivant.setDisable(true);
            }
            
          
        }
        
        
        System.out.println("phrase: " + mots);
    }

    public int getphrase() {
        return mots;
    }
    
    public boolean istutoFinie(){
        if(mots == 5){
            return true;
        } else {
            return false;
        }
    }
    
 
    public String getMots() {
        return Tab[mots];
    }
    public void Rule_of_game(){
        Tab = new String[8];
        Tab[0] = "L'objectif du jeu est que chaque joueur essaie d’attraper un maximum de poissons avec ses pingouins "
                 +"avant d’être définitivement isolé sur la banquise.";
        
        Tab[1] = "Chaque joueur place tour à tour un de ses pingouins sur un bloc de glace qui contenant un et un seul poisson."
                + " Il ne peut y avoir qu’un seul pingouin par bloc de glace." ;
                
        Tab[2] = "Pour mieur comprendre cela, cliquez sur une case avec UN POISSON pour placer vos pingouins.";

        Tab[3] = "Le deplacement doit respecer les contraintes suivantes :";
      
        Tab[4] = "* Le pingouin doit se déplacer en ligne droite dans une des 6 directions qui entoure son bloc de glace.";
             
        Tab[5] = "* Le pingouin peut avancer d’autant de cases que le joueur le souhaite.\n"
                +"* Enfin un pingouin ne peut pas franchir d’obtacle.";
        
        Tab[6] = "Tour à tour, chaque joueur doit déplacer ces pingouin, sélectionne le en cliquant dessus puis clique sur la case où vous voulez le déplacer.";
   
        Tab[7] ="Vous pouvez commencer à jouer." ;
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
        ImageView pinguin = (ImageView) anchorPane.lookup("#p"+i+"_"+j);
                    pinguin.setImage(new Image("Images/pingouins/0_0.png"));
                    pinguin.setVisible(true);
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
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    for(Joueur joueur : moteur.joueurs)
                    {
                        if(joueur.numero != moteur.joueurPrecedent && joueur instanceof JoueurReseau)
                        {                        
                            if(joueur instanceof JoueurClient)
                            {
                                if(moteur.joueurs[moteur.joueurPrecedent] instanceof JoueurHumain)
                                {
                                    joueur.connexion.writeObject(moteur.dernierCoupJoue);
                                }
                                break;
                            }
                            else
                            {
                                 joueur.connexion.writeObject(moteur.dernierCoupJoue);
                            }
                        }                    
                    }
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {                        
                            estEnAttente.set(false);
                            if(!jeuInterrompu.get())
                            {                               
                                if(coup instanceof Deplacement)
                                {
                                   onTranslateDuPinguoin((Deplacement)coup, nouveauxPingouinsBloques);
                                   miseAJourInfoJeu();
                                    System.out.println("onTranslateDuPinguoin");
                                }else
                                {
                                    onCouleDuPingouin(nouveauxPingouinsBloques);   
                                    System.out.println("onCouleDuPingouin");
                                }
                            }
                        }
                    });
                }
                catch(IOException e)
                {
                    Platform.runLater(ControleurTutoriel.this::erreurReseau);
                }
            }
        };    
        estEnAttente.set(true);
        thread.start();
    }
     
       private void miseAjourPoints(ArrayList<Point> points)
    {
        for(Point point : points)
        {
            miseAJourPingouin(point.ligne, point.colonne);
            miseAJourTuile(point.ligne, point.colonne);
        }
    }
    
    private void miseAJourInfoJeu()
    {
        String texteInfoJeu = null;
        if(moteur.estPartieTerminee())
        {
            texteInfoJeu = "Partie terminée ! Gagnant(s) : ";
            for(Joueur joueur : moteur.joueursGagnants())
            {
                texteInfoJeu += "J"+joueur.numero+" ("+joueur.scorePoisson+"p et "+joueur.scoreTuile+"t), ";
            }            
            for(Joueur joueur : moteur.joueurs)
            {
                    System.out.println("J"+joueur.numero+"("+joueur.getClass().getSimpleName()+") : "+joueur.scorePoisson+"p et "+joueur.scoreTuile+"t, ");
            }
            
            //showFinPartie();
        }
            Label labelScore = (Label) anchorPane.lookup("#0_score");
            labelScore.setText(Integer.toString(moteur.joueurs[1].scorePoisson));
            ColorAdjust afk_color = new ColorAdjust();
            ColorAdjust active_color = new ColorAdjust();
            afk_color.setBrightness(0);
            active_color.setBrightness(-0.5);
            
            for(int i = 0 ; i < 1 ; i++)
            {
                ImageView tb2 = (ImageView) anchorPane.lookup("#"+i+"_gui");
                tb2.setImage(new Image("Images/gui/"+i+"_gui_afk.png"));
                tb2.setEffect(afk_color);
            }
            
            ImageView tb = (ImageView) anchorPane.lookup("#"+moteur.joueurs[moteur.joueurCourant].couleur+"_gui");
            tb.setImage(new Image("Images/gui/"+moteur.joueurs[moteur.joueurCourant].couleur+"_gui_active.png"));
            tb.setEffect(active_color);
            
            Timeline fadeOutTimeline = new Timeline();
            fadeOutTimeline.setCycleCount(1);
            fadeOutTimeline.setAutoReverse(false);
            final KeyValue kv = new KeyValue(afk_color.brightnessProperty(), -0.5, Interpolator.LINEAR);
            final KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
            fadeOutTimeline.getKeyFrames().add(kf);
            fadeOutTimeline.play();
            
            Timeline fadeInTimeline = new Timeline();
            fadeInTimeline.setCycleCount(1);
            fadeInTimeline.setAutoReverse(false);
            final KeyValue kv2 = new KeyValue(active_color.brightnessProperty(), 0, Interpolator.LINEAR);
            final KeyFrame kf2 = new KeyFrame(Duration.millis(300), kv2);
            fadeInTimeline.getKeyFrames().add(kf2);
            fadeInTimeline.play();
                    
            //joueurPrec = navigation.moteur.joueurCourant;
            
           
        
    }
     public void  montrerDernierCoup(Deplacement dep)
    {
        System.out.println("ya houlou");
        String idCasePinguoin = indicesToId(dep.ligneDest, dep.colonneDest,DEBUTIDTUILE);
        ImageView pingouinCourant = (ImageView)anchorPane.lookup("#"+idCasePinguoin);
        
        
        String idCaseSource = indicesToId(dep.ligneSrc, dep.colonneSrc,DEBUTIDTUILE);
        tuileFantome = (ImageView)anchorPane.lookup("#"+idCaseSource);
        Image image = new Image("Images/fantomes/tuilevierge.png") ;
        
        //tuileFantome.setImage(image);
        
        String idCaseSourcePinguouin = indicesToId(dep.ligneSrc, dep.colonneSrc,DEBUTIDPINGOUIN);
        pingouinFantome = (ImageView)anchorPane.lookup("#"+idCaseSourcePinguouin);
        Image image2 = new  Image("Images/fantomes/" + moteur.joueurCourant + "_0_6.png") ;        
        //pingouinFantome.setImage(image2);

        
        
        double xDep = pingouinCourant.getLayoutX() + pingouinCourant.getFitWidth()/2;
        double yDep = pingouinCourant.getLayoutY() + pingouinCourant.getFitHeight()/2;
        double xArr = tuileFantome.getLayoutX() + tuileFantome.getFitWidth()/2;
        double yArr = tuileFantome.getLayoutY() + tuileFantome.getFitHeight()/2;
        

        lineFantome.setStartX(xDep);
        lineFantome.setStartY(yDep);
        lineFantome.setEndX(xArr);
        lineFantome.setEndY(yArr);
        lineFantome.setStroke(Constantes.couleurJoueur(moteur.joueurs[moteur.joueurCourant]));
        lineFantome.setVisible(true);
        
    }
    private void onTranslateDuPinguoin(Deplacement dep, ArrayList<Point> nouveauxPingouinsBloques) {
        estEnAttente.set(true);
       
        if(pingouinFantome != null)
        {
            pingouinFantome.setImage(null);  
            //pingouinFantome.setVisible(false);
            tuileFantome.setImage(null); 
            Point coordonneesTuile = idToIndices(tuileFantome.getId());
            miseAJourTuile(coordonneesTuile.ligne, coordonneesTuile.colonne);
            //Point coordonneesPingouin = idToIndices(pingouinFantome.getId());
            //miseAJourPingouin(coordonneesPingouin.ligne, coordonneesPingouin.colonne);
            /*tuileFantome.setImage(null); 
            tuileFantome.setVisible(false);*/
            lineFantome.setVisible(false);
        }
         montrerDernierCoup(dep);
        String idPingouin = indicesToId(dep.ligneSrc, dep.colonneSrc,DEBUTIDPINGOUIN);
        ImageView pingouinGraphique = (ImageView)anchorPane.lookup("#"+idPingouin);
        
        
        String idCaseDest = indicesToId(dep.ligneDest, dep.colonneDest,DEBUTIDPINGOUIN);
        ImageView caseDest = (ImageView)anchorPane.lookup("#"+idCaseDest);
        
        
        Case tuile =moteur.plateau.plateau[dep.ligneDest][dep.colonneDest];
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
        estEnAttente.set(true);
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
                miseAJourInfoJeu();
                estEnAttente.set(false);
               

            }
        });
        parallelTransition.play();
     }
     
     
}

