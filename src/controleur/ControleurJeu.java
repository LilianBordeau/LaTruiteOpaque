package controleur;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import modele.Case;
import modele.Constantes;
import modele.Coup;
import modele.Deplacement;
import modele.Joueur;
import modele.JoueurAAttendre;
import modele.JoueurClient;
import modele.JoueurHumain;
import modele.JoueurReseau;
import modele.Plateau;
import modele.Point;
import reseau.UnverifiedIOException;
import thread.MyThread;

public class ControleurJeu  extends ControleurBase {
    private static final String DEBUTIDTUILE = "c";
    private static final String DEBUTIDPINGOUIN = "p";
    private static final String DEBUTIDCASEACCESSIBLE = "a";
    private static final String SEPARATEURID = "_";
    public ImageView[][] pingouins;
    public ImageView[][] tuiles;
    public Point pingouinSel;
    public ArrayList<Point> casesAccessibles;
    public SimpleBooleanProperty estEnAttente;
    private SimpleBooleanProperty jeuInterrompu;
	public ImageView pingouinMvt;
 	ImageView pingouinFantome;
    ImageView tuileFantome;    
    private Line lineFantome;
    Point positionAmpoule;
    Line lineAmpoule;
    
    @FXML
    Button btnUndo,btnRedo,btnPause,btnIndice;
    
    @FXML
    Group groupEnPause;

    @FXML
    public AnchorPane anchorPane;
    
    @FXML
    public Label titreFinPartie;
     
    @FXML
    public ImageView sablier,fondFinPartie;
    
     @FXML
    public TableView tableClassement;
     
     
    
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        estEnAttente = new SimpleBooleanProperty(false);
        jeuInterrompu = new SimpleBooleanProperty(false);
        sablier.visibleProperty().bind(estEnAttente.and(jeuInterrompu.not()));
        RotateTransition transitionSablier = new RotateTransition(Duration.millis(2500), sablier);
        transitionSablier.setFromAngle(0);
        transitionSablier.setByAngle(359);
        transitionSablier.setCycleCount(Transition.INDEFINITE);
        transitionSablier.play();
        pingouinMvt =  new ImageView();
        lineFantome = new Line();
        lineAmpoule = new Line();
        lineAmpoule.setMouseTransparent(true);
        lineAmpoule.setOpacity(0.5);
        lineAmpoule.setStrokeWidth(5);
        anchorPane.getChildren().add(lineAmpoule);
        lineFantome.setMouseTransparent(true);
        lineFantome.setOpacity(0.5);
        lineFantome.setStrokeWidth(5);
        anchorPane.getChildren().add(pingouinMvt);
        anchorPane.getChildren().add(lineFantome);
        casesAccessibles = new ArrayList<>();      
        
        btnUndo.setPadding(Insets.EMPTY);
        btnRedo.setPadding(Insets.EMPTY);
        btnPause.setPadding(Insets.EMPTY);
        btnIndice.setPadding(Insets.EMPTY);
    
        btnUndo.setOnMouseExited(e -> btnUndo.setPadding(Insets.EMPTY));  
        btnRedo.setOnMouseExited(e -> btnRedo.setPadding(Insets.EMPTY));   
        btnPause.setOnMouseExited(e -> btnPause.setPadding(Insets.EMPTY));  
        btnIndice.setOnMouseExited(e -> btnIndice.setPadding(Insets.EMPTY));  
    }
    
    @Override
    public void onAppearing()
    {
        
        jeuInterrompu.set(false);
        Case[][] plateau = navigation.moteur.plateau.plateau;
        for(int i = 0 ; i < plateau.length ; i++)
        {
            for(int j = 0 ; j < Plateau.nbTuilesLigne(i) ; j++)
            {
                miseAJourTuile(i,j);
                miseAJourPingouin(i,j);   
            }
        }
        
            for(int i = 0 ; i < 4 ; i++)
            {
                ColorAdjust afk_color = new ColorAdjust();
                afk_color.setBrightness(-0.5);
                ImageView tbAll = (ImageView) anchorPane.lookup("#"+i+"_gui");
                tbAll.setEffect(afk_color);
            }
            ArrayList<Integer> couleursPrises = new ArrayList<>();
            for(int i = 0 ; i<navigation.moteur.joueurs.length ; i++ )
            {
                couleursPrises.add(navigation.moteur.joueurs[i].couleur);
                ImageView joueurExistant = (ImageView) anchorPane.lookup("#"+navigation.moteur.joueurs[i].couleur+"_gui");                
                joueurExistant.setVisible(true);
                joueurExistant.setImage(new Image("Images/gui/"+navigation.moteur.joueurs[i].couleur+"_gui_afk.png"));
                Label label3 = (Label) anchorPane.lookup("#"+navigation.moteur.joueurs[i].couleur+"_nom");        
                label3.setText(navigation.moteur.joueurs[i].nom);
                System.out.println(navigation.moteur.joueurs[i].nom);
            }
            for(int i = 0 ; i<4 ; i++ )
            {
                if(!couleursPrises.contains(i))
                {
                    
                    ImageView joueurInexistant = (ImageView) anchorPane.lookup("#"+i+"_gui");
                    joueurInexistant.setVisible(false);
                    Label label = (Label) anchorPane.lookup("#"+i+"_score");
                    Label label2 = (Label) anchorPane.lookup("#"+i+"_tuille");
                    Label label3 = (Label) anchorPane.lookup("#"+i+"_nom");                
                    label.setText("");
                    label2.setText("");
                    label3.setText("");
                }
            }
        hideFinPartie();
        miseAJourInfoJeu();
        reprendre();
        
    }
    
    public void initPartie()
    {
        lineFantome.setVisible(false);
        estEnAttente.set(false);        
        pingouinSel = null;
        suprimerCasesAccessible();
        effacerAmpoule();
    }
    
    @FXML
    private void retourMenu(ActionEvent event)
    {
        jeuInterrompu.set(true);
        navigation.changerVue(ControleurMenuPrincipal.class);
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
    
    @FXML
    private void clicTuile(MouseEvent event)
    {
        if(!estEnAttente.get() && !jeuInterrompu.get())
        {            
            ImageView tuileGraphique = (ImageView)event.getSource();
            Point coordonnees = idToIndices(tuileGraphique.getId()); 
            ArrayList<Point> nouveauxPingouinsBloques = null;
            if(!navigation.moteur.pingouinsPlaces())
            {        
                nouveauxPingouinsBloques = navigation.moteur.placerPingouin(coordonnees.ligne, coordonnees.colonne);
                if(nouveauxPingouinsBloques != null)
                {
                    effacerAmpoule();
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
                    ArrayList<Point> deplacements = navigation.moteur.deplacementsPossibles(coordonnees.ligne, coordonnees.colonne);
                    for(Point depl : deplacements)
                    {
                        String idTuile = indicesToId(depl.ligne, depl.colonne, DEBUTIDTUILE);
                        ImageView tuileDepl = (ImageView)anchorPane.lookup("#"+idTuile);
                        //tuileDepl.setImage(null);
                        afficherCaseAccessible(depl);

                    }
                }
                else if(pingouinSel != null)
                {      
                    effacerAmpoule();
                    nouveauxPingouinsBloques = navigation.moteur.deplacerPingouin(pingouinSel.ligne, pingouinSel.colonne, coordonnees.ligne, coordonnees.colonne);
                    if(nouveauxPingouinsBloques != null)
                    {
                        suprimerCasesAccessible();
                        miseAJourPingouin(pingouinSel.ligne, pingouinSel.colonne);
                        miseAJourTuile(pingouinSel.ligne, pingouinSel.colonne); 
                        //miseAJourPingouin(coordonnees.ligne, coordonnees.colonne);
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
            for(Joueur joueur : navigation.moteur.joueurs)
            {
                    System.out.println("J"+joueur.numero+"("+joueur.getClass().getSimpleName()+") : "+joueur.scorePoisson+"p et "+joueur.scoreTuile+"t, ");
            }
            
            showFinPartie();
        }
        /*else
        {
            texteInfoJeu = "Tour du joueur "+navigation.moteur.joueurCourant+"(";
            for(Joueur joueur : navigation.moteur.joueurs)
            {
                texteInfoJeu += "J"+joueur.numero+"("+joueur.getClass().getSimpleName()+") : "+joueur.scorePoisson+"p et "+joueur.scoreTuile+"t, ";
            }
            texteInfoJeu += ")";
        }*/        
            int joueurPrec = ((navigation.moteur.joueurPrecedent==-1)?0:navigation.moteur.joueurPrecedent);
            
            ColorAdjust afk_color = new ColorAdjust();
            ColorAdjust active_color = new ColorAdjust();
            afk_color.setBrightness(0);
            active_color.setBrightness(-0.5);
            
            ImageView tb2 = (ImageView) anchorPane.lookup("#"+navigation.moteur.joueurs[joueurPrec].couleur+"_gui");
            tb2.setImage(new Image("Images/gui/"+navigation.moteur.joueurs[joueurPrec].couleur+"_gui_afk.png"));
            tb2.setEffect(afk_color);
            
            ImageView tb = (ImageView) anchorPane.lookup("#"+navigation.moteur.joueurs[navigation.moteur.joueurCourant].couleur+"_gui");
            tb.setImage(new Image("Images/gui/"+navigation.moteur.joueurs[navigation.moteur.joueurCourant].couleur+"_gui_active.png"));
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
                    
            joueurPrec = navigation.moteur.joueurCourant;
            
            for(int i=0;i< navigation.moteur.joueurs.length ;i++)
            {
                Label label = (Label) anchorPane.lookup("#"+navigation.moteur.joueurs[i].couleur+"_score");
                Label label2 = (Label) anchorPane.lookup("#"+navigation.moteur.joueurs[i].couleur+"_tuille");
                
                label.setText(Integer.toString(navigation.moteur.joueurs[i].scorePoisson));
                label2.setText(Integer.toString(navigation.moteur.joueurs[i].scoreTuile));
               
            }
        
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
        tuileGraphique.setVisible(true);
    }

    private void miseAJourPingouin(int i, int j)
    {
        String idPingouin = indicesToId(i, j, DEBUTIDPINGOUIN);
        ImageView pingouinGraphique = (ImageView)anchorPane.lookup("#"+idPingouin);
        Case tuile = navigation.moteur.plateau.plateau[i][j];
        Image image = null;
        if(tuile.estOccupee() && !tuile.pingouin.estBloque)
        {
            String nomImage = Constantes.nomImagePingouin(navigation.moteur.joueurs[tuile.numJoueurPingouin()]);
            image = new Image(nomImage);
        }
        pingouinGraphique.setImage(image);
        pingouinGraphique.setVisible(true);
    }

    
    private void afficherCaseAccessible(Point depl)
    {        
        miseAJourCaseAccessible(depl, false);
        casesAccessibles.add(depl);
    }
    
    private void miseAJourCaseAccessible(Point uneCase, boolean supprimer)
    {
        String idAccessible = indicesToId(uneCase.ligne,uneCase.colonne,DEBUTIDCASEACCESSIBLE);
        ImageView caseAccessible = (ImageView)anchorPane.lookup("#"+idAccessible);
        Image image = null;
        if(!supprimer)
        {
            image = new Image(Constantes.nomImageCaseAccessible(navigation.moteur.joueurs[navigation.moteur.joueurCourant]));
        }
        caseAccessible.setImage(image);
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

    private void tourSuivant()
    {
        if(navigation.moteur.joueurs[navigation.moteur.joueurCourant] instanceof JoueurAAttendre && !navigation.moteur.estPartieTerminee())
        {            
            estEnAttente.set(true);
            JoueurAAttendre joueur = (JoueurAAttendre)navigation.moteur.joueurs[navigation.moteur.joueurCourant];
            joueur.moteur = navigation.moteur.clone();
            joueur.moteur.sauvegarderCoupJoues = false;
            joueur.moteur.coupAnnules = null;
            joueur.moteur.coupJoues = null;
            int dureeMinTourJoueurAAttendre = 0;
            effacerAmpoule();
            if(!navigation.moteur.pingouinsPlaces())
            {
                Thread threadPlacement = new Thread() {
                    @Override
                    public void run() {
                            try
                            {
                                Point lePlacement = joueur.getPlacementSuivant();                            
                                MyThread.sleep(dureeMinTourJoueurAAttendre);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run()
                                    {
                                            if(!jeuInterrompu.get())
                                            {                                      
                                                ArrayList<Point> nouveauxPingouinsBloques = navigation.moteur.placerPingouin(lePlacement.ligne, lePlacement.colonne);
                                                if(nouveauxPingouinsBloques != null)
                                                {
                                                    miseAJourPingouin(lePlacement.ligne, lePlacement.colonne);
                                                    finDuTour(nouveauxPingouinsBloques); 
                                                }
                                                else
                                                {
                                                    System.out.print("placement joueur a attendre abandonne");
                                                }
                                            }
                                            //estEnAttente = false;
                                    }
                                });
                            }
                            catch(UnverifiedIOException e)
                            {
                                Platform.runLater(ControleurJeu.this::erreurReseau);       
                            }
                        }
                };             
                threadPlacement.start();
                //threadPlacement.run();
            }
            else
            {
                Thread threadDeplacement = new Thread() {
                    @Override
                    public void run(){
                        try
                        {
                            Deplacement depl = joueur.getDeplacementSuivant();
                            MyThread.sleep(dureeMinTourJoueurAAttendre);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run()
                                {
                                        if(!jeuInterrompu.get())
                                        {                                        
                                            ArrayList<Point> nouveauxPingouinsBloques = navigation.moteur.deplacerPingouin(depl.ligneSrc, depl.colonneSrc, depl.ligneDest, depl.colonneDest);
                                            if(nouveauxPingouinsBloques != null)
                                            {
                                                miseAJourPingouin(depl.ligneSrc, depl.colonneSrc);
                                                miseAJourTuile(depl.ligneSrc, depl.colonneSrc);
                                             //   miseAJourPingouin(depl.ligneDest, depl.colonneDest);
                                                finDuTour(nouveauxPingouinsBloques);  
                                            }
                                            else
                                            {
                                                System.out.print("placement joueur a attendre abandonne");
                                            }
                                            // estEnAttente = false;
                                        } 
                                }
                            });
                        }
                        catch(UnverifiedIOException e)
                        {
                            Platform.runLater(ControleurJeu.this::erreurReseau);       
                        }
                    }
                };             
                threadDeplacement.start();
                //threadDeplacement.run();
            }           
        }
        
    }

    private void finDuTour(ArrayList<Point> nouveauxPingouinsBloques)
    {
        Coup coup = navigation.moteur.dernierCoupJoue;   
        System.out.println(coup);
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    for(Joueur joueur : navigation.moteur.joueurs)
                    {
                        if(joueur.numero != navigation.moteur.joueurPrecedent && joueur instanceof JoueurReseau)
                        {                        
                            if(joueur instanceof JoueurClient)
                            {
                                if(navigation.moteur.joueurs[navigation.moteur.joueurPrecedent] instanceof JoueurHumain)
                                {
                                    joueur.connexion.writeObject(navigation.moteur.dernierCoupJoue);
                                }
                                break;
                            }
                            else
                            {
                                 joueur.connexion.writeObject(navigation.moteur.dernierCoupJoue);
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
                                }else
                                {
                                    onCouleDuPingouin(nouveauxPingouinsBloques);   
                                }
                            }
                        }
                    });
                }
                catch(IOException e)
                {
                    Platform.runLater(ControleurJeu.this::erreurReseau);
                }
            }
        };    
        estEnAttente.set(true);
        thread.start();
    }
    
    @FXML
    private void clicSauvegarder(ActionEvent event)
    {
        if(!navigation.moteur.estEnReseau)
        {            
            System.out.println("clicSauvegarder");
            pause();
            navigation.changerVue(ControleurSauvegarderPartie.class); 
        }
        else
        {
            System.out.println("impossible de sauvegarder pendant un partie en réseau");
        }   
    }
    
    private void placerAmpoule(Point posAmpoule)
    {
        positionAmpoule = posAmpoule;
        String idPingouin = indicesToId(posAmpoule.ligne, posAmpoule.colonne, DEBUTIDPINGOUIN);   
        Scene scene = anchorPane.getScene();
        ImageView pingouinGraphique = (ImageView)anchorPane.lookup("#"+idPingouin);
        pingouinGraphique.setImage(new Image("Images/ampoule.png"));
        pingouinGraphique.setVisible(true);
    }
    
    public void effacerAmpoule()
    {
        if(positionAmpoule != null)
        {
            miseAJourPingouin(positionAmpoule.ligne, positionAmpoule.colonne);
            lineAmpoule.setVisible(false);
        }
    }
    
    @FXML
    private void clicIndice(ActionEvent event)
    {
        Joueur joueurCourant = navigation.moteur.joueurs[navigation.moteur.joueurCourant];
        if(!jeuInterrompu.get() && joueurCourant instanceof JoueurHumain)
        {
            joueurCourant.moteur = navigation.moteur.clone();
            if(!navigation.moteur.pingouinsPlaces())
            {
                Point placement = joueurCourant.choixPlacement();
                placerAmpoule(placement);
            }
            else
            {
                Deplacement deplacement = joueurCourant.choixDeplacement();
                placerAmpoule(new Point(deplacement.ligneDest, deplacement.colonneDest));
                String idTuileSrc = indicesToId(deplacement.ligneSrc, deplacement.colonneSrc, DEBUTIDTUILE);
                ImageView tuileSrc = (ImageView)anchorPane.lookup("#"+idTuileSrc);
                String idTuileDest = indicesToId(deplacement.ligneDest, deplacement.colonneDest, DEBUTIDTUILE);    
                ImageView tuileDest = (ImageView)anchorPane.lookup("#"+idTuileDest);
                double xDep = tuileSrc.getLayoutX() + tuileSrc.getFitWidth()/2;
                double yDep = tuileSrc.getLayoutY() + tuileSrc.getFitHeight()/2;
                double xArr = tuileDest.getLayoutX() + tuileDest.getFitWidth()/2;
                double yArr = tuileDest.getLayoutY() + tuileDest.getFitHeight()/2;
                lineAmpoule.setStartX(xDep);
                lineAmpoule.setStartY(yDep);
                lineAmpoule.setEndX(xArr);
                lineAmpoule.setEndY(yArr);
                lineAmpoule.setVisible(true);
            }
        }
        else
        {
            System.out.println("Attendez le tour d'un joueur humain pour avoir un indice");  
        }
    }
    
    @FXML
    private void clicRecommencer(ActionEvent event)
    {
        
        if(!navigation.moteur.estEnReseau)
        {
             hideFinPartie();
            jeuInterrompu.set(true);
            System.out.println(navigation.moteur.plateau.plateau == navigation.moteur.sauvegardeDebutPartie.plateau.plateau);
            System.out.println(navigation.moteur.plateau.plateau[0][0] == navigation.moteur.sauvegardeDebutPartie.plateau.plateau[0][0]);
            navigation.moteur = navigation.moteur.sauvegardeDebutPartie;
            navigation.moteur.sauvegarderDebutPartie();
            pingouinFantome = null;
            effacerAmpoule();
            miseAJourPlateau();
            jeuInterrompu.set(false);
            reprendre(); 
            tourSuivant();
        }
        else
        {
            System.out.println("impossible de recommencer une partie lorsque l'on joue en réseau");
        }
    }
    
    @FXML
    private void clicAnnuler(ActionEvent event)
    {    
        if(!navigation.moteur.coupJoues.isEmpty() && !navigation.moteur.estEnReseau)
        {
            hideFinPartie();
            jeuInterrompu.set(true);  
            effacerAmpoule();
            if(navigation.moteur.queDesIA)
            {
                pause();
            }
            do
            {                
                Coup dernierCoupJoue = navigation.moteur.dernierCoupJoue;   
                navigation.moteur.coupAnnules.push(navigation.moteur.clone());         
                navigation.moteur.dernierCoupJoue = null;
                navigation.moteur = navigation.moteur.coupJoues.pop();            
                miseAJourAnnulerRefaire(dernierCoupJoue, null);
            } while(!navigation.moteur.queDesIA && !navigation.moteur.coupJoues.isEmpty() && !(navigation.moteur.joueurs[navigation.moteur.joueurCourant] instanceof JoueurHumain));
            if(!navigation.moteur.queDesIA)
            {
                reprendre();
            }
            /*jeuInterrompu = false;
            tourSuivant();*/
        }
        else
        {
            System.out.println("aucun coup a annuler ou annule desactive (pour le jeu en reseau)");
        }       
    }
    
    @FXML
    private void clicRefaire(ActionEvent event)
    {
        /*jeuInterrompu = false;
        tourSuivant();*/
        if(!navigation.moteur.coupAnnules.isEmpty()  && !navigation.moteur.estEnReseau)
        {            
            jeuInterrompu.set(true);
            effacerAmpoule();
            if(navigation.moteur.queDesIA)
            {
                pause();
            }
            Coup coupRefaire = null;
            do
            {      
                coupRefaire = navigation.moteur.dernierCoupJoue;
                navigation.moteur.coupJoues.push(navigation.moteur.clone());    
                navigation.moteur = navigation.moteur.coupAnnules.pop();    
                miseAJourAnnulerRefaire(navigation.moteur.dernierCoupJoue, coupRefaire);
                /*jeuInterrompu = false;
                tourSuivant();*/
            } while(!navigation.moteur.queDesIA && !navigation.moteur.coupAnnules.isEmpty() && !(navigation.moteur.joueurs[navigation.moteur.joueurCourant] instanceof JoueurHumain));
            if(!navigation.moteur.queDesIA)
            {
                reprendre();
            }
            if(navigation.moteur.estPartieTerminee())
            {
                reprendre();
                showFinPartie();
            }
            
        }
        else
        {
            
            System.out.println("aucun coup a refaire ou refaire desactive (pour le jeu en reseau)");
        }  
    }
    
    @FXML
    private void clicPause(ActionEvent event)
    {
        if(!navigation.moteur.estEnReseau)
        {
            pause();  
        }
        else
        {
            System.out.println("impossible de mettre une partie en pause lorsque l'on joue en réseau");
        }
        
    }
    
    private void pause()
    {       
        jeuInterrompu.set(true);
        groupEnPause.setVisible(true);
    }
    
    @FXML
    private void clicReprendre(ActionEvent event)
    {
        reprendre();          
    }
    
    public void reprendre()
    {        
        jeuInterrompu.set(false);
        groupEnPause.setVisible(false);
        tourSuivant();
    }
    
    private void miseAJourPlateau()
    {
        pingouinSel = null;
        suprimerCasesAccessible();
        for(int i = 0 ; i < navigation.moteur.plateau.plateau.length ; i++)
        {
            for(int j = 0 ; j < Plateau.nbTuilesLigne(i) ; j++)
            {
                miseAJourPingouin(i, j);
                miseAJourTuile(i, j);
            }
        }
        lineFantome.setVisible(false);
        miseAJourInfoJeu();
    }

    private void miseAJourAnnulerRefaire(Coup dernierCoupJoue, Coup coupRefaire)
    {
        lineFantome.setVisible(false); 
        if(pingouinMvt != null)
        {
            pingouinMvt.setImage(null);
        }
        ArrayList<Point> points = new ArrayList<>();
        if(dernierCoupJoue instanceof Point)
        {
            points.add((Point)dernierCoupJoue);
        }
        else
        {
            Deplacement deplacement = (Deplacement)dernierCoupJoue;
            points.add(new Point(deplacement.ligneSrc, deplacement.colonneSrc));
            points.add(new Point(deplacement.ligneDest, deplacement.colonneDest));
        }
        /*
        boolean test = true;
        for(int i =0; i<navigation.moteur.joueurs.length && test ; i++ )
        {
            if(navigation.moteur.joueurs[i] instanceof JoueurAAttendre)
            {
                test = false;
            }
        }
        
        if(test)
        {
            
        }
        */
        
        
        suprimerCasesAccessible();
        miseAjourPoints(points);
        miseAjourPoints(dernierCoupJoue.nouveauxPingouinsBloques);
        miseAJourInfoJeu();
        if(coupRefaire!= null && coupRefaire instanceof Deplacement)
        {
            Deplacement deplRefaire = (Deplacement)coupRefaire;
            Point coordonneesTuile = idToIndices(tuileFantome.getId());
            miseAJourTuile(deplRefaire.ligneSrc, deplRefaire.colonneSrc);
            Point coordonneesPingouin = idToIndices(pingouinFantome.getId());
            miseAJourPingouin(deplRefaire.ligneSrc, deplRefaire.colonneSrc);
        } 
        if(navigation.moteur.dernierCoupJoue != null && navigation.moteur.dernierCoupJoue instanceof Deplacement)
        {
                montrerDernierCoup((Deplacement)navigation.moteur.dernierCoupJoue);
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

    private void onCouleDuPingouin(ArrayList<Point> nouveauxPingouinsBloques){
        estEnAttente.set(true);
        miseAjourPoints(nouveauxPingouinsBloques);   
        ParallelTransition parallelTransition = new ParallelTransition();
        for(Point point : nouveauxPingouinsBloques)
        {            
            Case tuile = navigation.moteur.plateau.plateau[point.ligne][point.colonne];
            String nomImage = Constantes.nomImagePingouin(navigation.moteur.joueurs[tuile.numJoueurPingouin()]);
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
        parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                miseAjourPoints(nouveauxPingouinsBloques);         
                miseAJourInfoJeu();
                estEnAttente.set(false);
                tourSuivant();

            }
        });
        parallelTransition.play();
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
        
        
        Case tuile = navigation.moteur.plateau.plateau[dep.ligneDest][dep.colonneDest];
        String nomImage = Constantes.nomImagePingouin(navigation.moteur.joueurs[tuile.numJoueurPingouin()]);
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

    public void  montrerDernierCoup(Deplacement dep)
    {
        
        String idCasePinguoin = indicesToId(dep.ligneDest, dep.colonneDest,DEBUTIDTUILE);
        ImageView pingouinCourant = (ImageView)anchorPane.lookup("#"+idCasePinguoin);
        
        
        String idCaseSource = indicesToId(dep.ligneSrc, dep.colonneSrc,DEBUTIDTUILE);
        tuileFantome = (ImageView)anchorPane.lookup("#"+idCaseSource);
        Image image = new Image("Images/fantomes/tuilevierge.png") ;
        
        //tuileFantome.setImage(image);
        
        String idCaseSourcePinguouin = indicesToId(dep.ligneSrc, dep.colonneSrc,DEBUTIDPINGOUIN);
        pingouinFantome = (ImageView)anchorPane.lookup("#"+idCaseSourcePinguouin);
        Image image2 = new  Image("Images/fantomes/" + navigation.moteur.joueurPrecedent + "_0_6.png") ;        
        //pingouinFantome.setImage(image2);

        
        
        double xDep = pingouinCourant.getLayoutX() + pingouinCourant.getFitWidth()/2;
        double yDep = pingouinCourant.getLayoutY() + pingouinCourant.getFitHeight()/2;
        double xArr = tuileFantome.getLayoutX() + tuileFantome.getFitWidth()/2;
        double yArr = tuileFantome.getLayoutY() + tuileFantome.getFitHeight()/2;
        

        lineFantome.setStartX(xDep);
        lineFantome.setStartY(yDep);
        lineFantome.setEndX(xArr);
        lineFantome.setEndY(yArr);
        lineFantome.setStroke(Constantes.couleurJoueur(navigation.moteur.joueurs[navigation.moteur.joueurPrecedent]));
        lineFantome.setVisible(true);
        
    }
         
         


    private void showFinPartie()
    {
     
        double largeur = 300;
        double colonne = 50;
        double gap  = 20;
        double debut = 250;
        
        ArrayList<Joueur>  classementJoueurs = navigation.moteur.getClassement();

        double xDebut = debut + (largeur -  (colonne * classementJoueurs.size()  + gap*(classementJoueurs.size()-1)))/2;
        
        Line line;
        
        fondFinPartie.setVisible(true);
        titreFinPartie.setVisible(true);
        titreFinPartie.setTextFill(Color.WHITE);
        titreFinPartie.setFont(Font.font(null, FontWeight.BOLD, 10));
        for(int i=0; i<classementJoueurs.size();i++)
        {
            ImageView image1 = (ImageView) anchorPane.lookup("#"+"position"+(i+1));
            image1.setLayoutX(xDebut);
            image1.setVisible(true);
            
            image1 = (ImageView) anchorPane.lookup("#"+"classementPingouin"+(i+1));
            image1.setLayoutX(xDebut);
            image1.setImage(new Image(Constantes.nomImagePingouin(classementJoueurs.get(i))));
            image1.setVisible(true);
            
            
            Label nom = (Label) anchorPane.lookup("#"+"classementNom"+(i+1));
            nom.setText(classementJoueurs.get(i).nom);
            nom.setLayoutX(xDebut);    
            nom.setTextFill(Color.WHITE);
            nom.setFont(Font.font(null, FontWeight.BOLD, 8));
            nom.setVisible(true);
            
            image1 = (ImageView) anchorPane.lookup("#"+"classementScore"+(i+1));
            image1.setLayoutX(xDebut+25);
            image1.setImage(new Image("Images/cadre.png"));
            image1.setVisible(true);
            
            
            nom = (Label) anchorPane.lookup("#"+"textPoisson"+(i+1));
            nom.setText(Integer.toString(classementJoueurs.get(i).scorePoisson));
            nom.setLayoutX(xDebut+10);    
            nom.setTextFill(Color.WHITE);
            nom.setFont(Font.font(null, FontWeight.BOLD, 10));
            nom.setVisible(true);
            
            nom = (Label) anchorPane.lookup("#"+"textBanquise"+(i+1));
            nom.setText(Integer.toString(classementJoueurs.get(i).scoreTuile));
            nom.setLayoutX(xDebut+10);       
            nom.setTextFill(Color.WHITE);
            nom.setFont(Font.font(null, FontWeight.BOLD, 10));
            nom.setVisible(true);
            
            
            if(i != classementJoueurs.size()-1)
            {
                line = (Line) anchorPane.lookup("#"+"classementLine"+(i+1));
                line.setLayoutX(xDebut+colonne+(gap/2));
                line.setVisible(true);
            }
            xDebut += gap + colonne;
           
        }

    }
   
    private void hideFinPartie()
    {
        fondFinPartie.setVisible(false);
        titreFinPartie.setVisible(false);
       
        
         Line line;
        for(int i=1; i<5;i++)
        {
            ImageView image1 = (ImageView) anchorPane.lookup("#"+"position"+i);
            image1.setVisible(false);
            
            image1 = (ImageView) anchorPane.lookup("#"+"classementPingouin"+i);
            image1.setVisible(false);
            
            
            Label nom = (Label) anchorPane.lookup("#"+"classementNom"+i);;
            nom.setVisible(false);
            
            image1 = (ImageView) anchorPane.lookup("#"+"classementScore"+i);
            image1.setVisible(false);
            
            
            nom = (Label) anchorPane.lookup("#"+"textPoisson"+i);
            nom.setVisible(false);
            
            nom = (Label) anchorPane.lookup("#"+"textBanquise"+i);    
            nom.setVisible(false);
            if( i<=3)
            {
                line = (Line) anchorPane.lookup("#"+"classementLine"+i);
               
                line.setVisible(false);
            }
        }
    }
    
       
}
