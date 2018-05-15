package controleur;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
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
import modele.Moteur;
import modele.Plateau;
import modele.Point;
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
    private boolean estEnAttente;
    private boolean jeuInterrompu;
	public ImageView pingouinMvt;
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
        anchorPane.getChildren().add(pingouinMvt);
        anchorPane.getChildren().add(lineFantome);
        casesAccessibles = new ArrayList<>();        
    }
    
    @Override
    public void onAppearing()
    {
        jeuInterrompu = false;
        Case[][] plateau = navigation.moteur.plateau.plateau;
        for(int i = 0 ; i < plateau.length ; i++)
        {
            for(int j = 0 ; j < Plateau.nbTuilesLigne(i) ; j++)
            {
                miseAJourTuile(i,j);                   
            }
        }
        
            for(int i = 0 ; i < 4 ; i++)
            {
                ColorAdjust desaturate = new ColorAdjust();
                desaturate.setSaturation(-0.75);
                ImageView tbAll = (ImageView) anchorPane.lookup("#"+i+"_gui");
                tbAll.setEffect(desaturate);
            }
            ArrayList<Integer> couleursPrises = new ArrayList<>();
            for(int i = 0 ; i<navigation.moteur.joueurs.length ; i++ )
            {
                couleursPrises.add(navigation.moteur.joueurs[i].couleur);
                ImageView joueurInexistant = (ImageView) anchorPane.lookup("#"+navigation.moteur.joueurs[i].couleur+"_gui");
                joueurInexistant.setImage(new Image("Images/"+navigation.moteur.joueurs[i].couleur+"_gui_afk.png"));
                Label label3 = (Label) anchorPane.lookup("#"+i+"_nom");        
                label3.setText(navigation.moteur.joueurs[i].nom);
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
        miseAJourInfoJeu();
        tourSuivant();
    }
    
    @FXML
    private void retourMenu(ActionEvent event)
    {
        jeuInterrompu = true;
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
        if(!estEnAttente && !jeuInterrompu)
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
            infosJeu.setText(texteInfoJeu);
            int joueurPrec = ((navigation.moteur.joueurPrecedent==-1)?0:navigation.moteur.joueurPrecedent);
            ColorAdjust desaturate = new ColorAdjust();
            ColorAdjust colored = new ColorAdjust();
            desaturate.setSaturation(-0.75);
            colored.setSaturation(0);
            ImageView tb2 = (ImageView) anchorPane.lookup("#"+navigation.moteur.joueurs[joueurPrec].couleur+"_gui");
            //tb2.setStyle("-fx-scale-x:0.9;");
            tb2.setEffect(desaturate);
            tb2.setImage(new Image("Images/"+navigation.moteur.joueurs[joueurPrec].couleur+"_gui_afk.png"));
            if(joueurPrec%2==0)
            {
                tb2.setStyle("-fx-padding: 0 100 0 0;");
            }
            else
            {
                tb2.setStyle("-fx-padding: 0 0 100 0;");
            }
            //System.out.println(texteInfoJeu);
            ImageView tb = (ImageView) anchorPane.lookup("#"+navigation.moteur.joueurs[navigation.moteur.joueurCourant].couleur+"_gui");
            //tb.setStyle("-fx-scale-x:2.5;");
             //tb.setStyle("-fx-scale-y:1.2;");
            
             tb.setEffect(colored);
             tb.setImage(new Image("Images/"+navigation.moteur.joueurs[navigation.moteur.joueurCourant].couleur+"_gui_active.png"));
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
        ImageView pingouinGraphique = (ImageView)anchorPane.getScene().lookup("#"+idPingouin);
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
        ImageView caseAccessible = (ImageView)anchorPane.getScene().lookup("#"+idAccessible);
        Image image = null;
        if(!supprimer)
        {
            image = new Image(Constantes.nomImageCaseAccessible(navigation.moteur.joueurs[navigation.moteur.joueurCourant]));
        }
        caseAccessible.setImage(image);
    }

    private void suprimerCasesAccessible() {
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
            estEnAttente = true;
            JoueurAAttendre joueur = (JoueurAAttendre)navigation.moteur.joueurs[navigation.moteur.joueurCourant];
            joueur.moteur = navigation.moteur.clone();
            joueur.moteur.sauvegarderCoupJoues = false;
            joueur.moteur.coupAnnules = null;
            joueur.moteur.coupJoues = null;
            int dureeMinTourJoueurAAttendre = 0;
            if(!navigation.moteur.pingouinsPlaces())
            {
                Thread threadPlacement = new Thread() {
                    @Override
                    public void run() {
                            Point lePlacement = joueur.getPlacementSuivant();                            
                            MyThread.sleep(dureeMinTourJoueurAAttendre);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run()
                                {
                                    if(!jeuInterrompu)
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
                };             
                threadPlacement.start();
                //threadPlacement.run();
            }
            else
            {
                Thread threadDeplacement = new Thread() {
                    @Override
                    public void run(){
                            Deplacement depl = joueur.getDeplacementSuivant();
                            MyThread.sleep(dureeMinTourJoueurAAttendre);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run()
                                {
                                    if(!jeuInterrompu)
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
                        estEnAttente = false;
                        if(!jeuInterrompu)
                        {                               
                            if(coup instanceof Deplacement)
                            {
                                onTranslateDuPinguoin((Deplacement)coup, nouveauxPingouinsBloques);
                            }else
                            {
                                miseAjourPoints(nouveauxPingouinsBloques);         
                                miseAJourInfoJeu();
                                tourSuivant();  
                            }
                        }
                    }
                });
            }
        };    
        estEnAttente = true;
        thread.start();
    }
    
    @FXML
    private void clicSauvegarder(ActionEvent event)
    {
        System.out.println("clicSauvegarder");
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constantes.nomFichierSauvegarde));
            oos.writeObject(navigation.moteur);            
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @FXML
    private void clicCharger(ActionEvent event)
    {
        jeuInterrompu = false;
        System.out.println("clicCharger");
        try
        {
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(Constantes.nomFichierSauvegarde));
            navigation.moteur = (Moteur)oos.readObject();  
            miseAJourPlateau();
            tourSuivant();
        }
        catch(IOException|ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @FXML
    private void clicRecommencer(ActionEvent event)
    {
        jeuInterrompu = true;
        System.out.println(navigation.moteur.plateau.plateau == navigation.moteur.sauvegardeDebutPartie.plateau.plateau);
        System.out.println(navigation.moteur.plateau.plateau[0][0] == navigation.moteur.sauvegardeDebutPartie.plateau.plateau[0][0]);
        navigation.moteur = navigation.moteur.sauvegardeDebutPartie;
        navigation.moteur.sauvegarderDebutPartie();
        pingouinFantome = null;
        miseAJourPlateau();
        jeuInterrompu = false;
        tourSuivant();
    }
    
    @FXML
    private void clicAnnuler(ActionEvent event)
    {        
        if(!navigation.moteur.coupJoues.isEmpty())
        {
            jeuInterrompu = true;
            Coup dernierCoupJoue = navigation.moteur.dernierCoupJoue;   
            navigation.moteur.coupAnnules.push(navigation.moteur.clone());         
            navigation.moteur.dernierCoupJoue = null;
            navigation.moteur = navigation.moteur.coupJoues.pop();
            miseAJourAnnulerRefaire(dernierCoupJoue);
            jeuInterrompu = false;
            tourSuivant();
        }
        else
        {
            System.out.println("aucun coup a annuler");
        }        
    }
    
    @FXML
    private void clicRefaire(ActionEvent event)
    {
        if(!navigation.moteur.coupAnnules.isEmpty())
        {            
            jeuInterrompu = true;
            navigation.moteur.coupJoues.push(navigation.moteur.clone());    
            navigation.moteur = navigation.moteur.coupAnnules.pop();    
            miseAJourAnnulerRefaire(navigation.moteur.dernierCoupJoue);
            jeuInterrompu = false;
            tourSuivant();
        }
        else
        {
            System.out.println("aucun coup a refaire");
        }  
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

    private void miseAJourAnnulerRefaire(Coup dernierCoupJoue)
    {
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
        suprimerCasesAccessible();
        miseAjourPoints(points);
        miseAjourPoints(dernierCoupJoue.nouveauxPingouinsBloques);
        miseAJourInfoJeu();
    }
    private void miseAjourPoints(ArrayList<Point> points)
    {
        for(Point point : points)
        {
            miseAJourPingouin(point.ligne, point.colonne);
            miseAJourTuile(point.ligne, point.colonne);
        }
    }



	private void onTranslateDuPinguoin(Deplacement dep, ArrayList<Point> nouveauxPingouinsBloques) {
        estEnAttente = true;
       
        if(pingouinFantome != null)
        {
            pingouinFantome.setImage(null);  
            pingouinFantome.setVisible(false);
            tuileFantome.setImage(null); 
            tuileFantome.setVisible(false);
            lineFantome.setVisible(false);
        }
         montrerDernierCoup(dep);
        String idPingouin = indicesToId(dep.ligneSrc, dep.colonneSrc,DEBUTIDPINGOUIN);
        ImageView pingouinGraphique = (ImageView)anchorPane.getScene().lookup("#"+idPingouin);
        
        
        String idCaseDest = indicesToId(dep.ligneDest, dep.colonneDest,DEBUTIDPINGOUIN);
        ImageView caseDest = (ImageView)anchorPane.getScene().lookup("#"+idCaseDest);
        
        
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
                miseAJourPingouin(dep.ligneDest, dep.colonneDest);
                miseAjourPoints(nouveauxPingouinsBloques);         
                miseAJourInfoJeu();
                estEnAttente = false;
                tourSuivant();

            }
        });
  
    }

	 private void  montrerDernierCoup(Deplacement dep)
    {
        
        String idCasePinguoin = indicesToId(dep.ligneDest, dep.colonneDest,DEBUTIDTUILE);
        ImageView pingouinCourant = (ImageView)anchorPane.getScene().lookup("#"+idCasePinguoin);
        
        
        String idCaseSource = indicesToId(dep.ligneSrc, dep.colonneSrc,DEBUTIDTUILE);
        tuileFantome = (ImageView)anchorPane.getScene().lookup("#"+idCaseSource);
        Image image = new Image("Images/fantomes/tuilevierge.png") ;
        
        tuileFantome.setImage(image);
        
        String idCaseSourcePinguouin = indicesToId(dep.ligneSrc, dep.colonneSrc,DEBUTIDPINGOUIN);
        pingouinFantome = (ImageView)anchorPane.getScene().lookup("#"+idCaseSourcePinguouin);
        Image image2 = new  Image("Images/fantomes/" + navigation.moteur.joueurPrecedent + "_0_6.png") ;        
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
