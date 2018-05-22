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
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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




public class ControleurTutoriel extends ControleurBase
{     //private boolean estEnAttente;
    Moteur moteur;
    public Point pingouinSel;
    public SimpleBooleanProperty estEnAttente;
    private SimpleBooleanProperty jeuInterrompu;
    public ArrayList<Point> casesAccessibles;
   // private boolean jeuInterrompu;
    private int mots = 0;
    private String[] Tab;
    private ImageView tuileGraphique;
    private static final String DEBUTIDTUILE = "c";
    private static final String DEBUTIDPINGOUIN = "p";
    private static final String DEBUTIDCASEACCESSIBLE = "a";
    private static final String SEPARATEURID = "_";
    private boolean canDeplace;
    private boolean canPlace;
   
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
        
			
    }
    
    @Override
    public void onAppearing(){
    suivant.setDisable(false);
    for(Node element : anchorPane.getChildren())
    {
        element.setVisible(true);
        
    }
    
       suprimerCasesAccessible();
       mots=0;
       labelTitre.setText(Tab[mots]);
       
            Joueur[] joueurs = new Joueur[]{new JoueurHumain(), new JoueurHumain()};
            moteur = new Moteur(joueurs);
            for(int i = 4 ; i < moteur.plateau.plateau.length ; i++)
            {
                for(int j = 4 ; j < moteur.plateau.nbTuilesLigne(i) ; j++)
		{
		     moteur.plateau.plateau[i][j].nbPoissons = 1;  
					
		}
            }
			moteur.placerPingouin(4,4);   
			moteur.placerPingouin(4,5);  
			moteur.placerPingouin(4,6);  
			moteur.placerPingouin(5,4);  	
			moteur.placerPingouin(5,5);   
			moteur.placerPingouin(5,6);  
			moteur.placerPingouin(6,4);	
            for(int i = 4 ; i < moteur.plateau.plateau.length ; i++)
            {
                for(int j = 4 ; j < Plateau.nbTuilesLigne(i) ; j++)
		{
		    moteur.plateau.plateau[i][j].nbPoissons = 0;  					
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
                //finDuTour(nouveauxPingouinsBloques);
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
        Tab[0] = "Objectif du jeu est que Chaque joueur essaie d’attraper \n" 
                 +"un maximum de poissons avec ses pingouins,\n"
                 +"avant d’être définitivement isolé sur la banquise.";
        
        Tab[1]= "chaque joueur place tour à tour un de ses pingouins\n"
                + " sur un bloc de glace qui contenant un et un seul poisson\n"
                + "Il ne peut y avoir qu’un seul pingouin par bloc de glace.\n" ;
                
        Tab[2]=" Pour mieur comprendre cela\n"+" Cliquez sur une case avec UN POISSON pour placer vos pingouins\n";

        Tab[3] = "Le deplacement doit respecer les contraintes suivantes : ";
        
      
        Tab[4] ="*Le pingouin doit se déplacer en ligne droite dans une des 6\n" 
                +"*directions qui entoure son bloc de glace.\n ";
             
                
        Tab[5] = "*Le pingouin peut avancer d’autant de cases\n"+"              que le joueur le souhaite.\n"
                +"*enfin un pingouin ne peut pas franchir d’obtacle.";
        
        Tab[6] = "Tour à tour,chaque joueur doit Déplacer ces pingouin"
                + "\nPour déplacer un pingouin, séléctionne le en cliquant dessus\n"
                + "puis clique sur la case où vous voulez le déplacer\n";
   
        
                
        Tab[7] ="vous pouvez commencer à jouer." ;
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
                                    //onTranslateDuPinguoin((Deplacement)coup, nouveauxPingouinsBloques);
                                }else
                                {
                                   // onCouleDuPingouin(nouveauxPingouinsBloques);   
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
     

   

}
