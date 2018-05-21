package modele;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;

public class Constantes
{
    private static final double SCREENRATIOWIDTHPOPUP = 0.25;
    private static final double SCREENRATIOHEIGHTPOPUP = SCREENRATIOWIDTHPOPUP;
    public static final double POPUPWIDTH = Screen.getPrimary().getVisualBounds().getWidth()*SCREENRATIOWIDTHPOPUP;
    public static final double POPUPHEIGHT = Screen.getPrimary().getVisualBounds().getHeight()*SCREENRATIOHEIGHTPOPUP;
    public static final String DOSSIERIMAGES = "Images";
    public static final String DOSSIERSONS = "Sons/";//"rsc/Sons/";
    public static final String nomFichierSauvegarde = "sauvegarde.txt";
    public static String nomImageCase(Case uneCase)
    {
        String nomImage = null;
        if(uneCase.estCoulee())
        {
            throw new RuntimeException("image manquante");
        }
        else
        {
            return DOSSIERIMAGES+"/tuile"+uneCase.nbPoissons+((uneCase.nbPoissons >= 2)?"":"")+".png";
        }
    }
    
    
    
    public static String nomImagePingouin(Joueur joueur)
    {
        if(joueur instanceof JoueurHumain || joueur instanceof JoueurReseau)
        {
          return DOSSIERIMAGES+"/pingouins/"+joueur.couleur+"_0.png";   
        }
        if(joueur instanceof JoueurIAAleatoire)
        {
          return DOSSIERIMAGES+"/pingouins/"+joueur.couleur+"_1.png";
        }
        else if(joueur instanceof JoueurIAMoyenne)
        {
          return DOSSIERIMAGES+"/pingouins/"+joueur.couleur+"_2.png";
        }
        else if(joueur instanceof JoueurIADifficile)
        {
          return DOSSIERIMAGES+"/pingouins/"+joueur.couleur+"_3.png";
        }
        else
        {
            throw new RuntimeException("image manquante");
        }
    }       

    
    public static final int CLIQUEBOUTON = 1;
    
    public static final int SON = 1;
    public static final int MUSIQUE = 0;
    public static final int ON = 1;
    public static final int OFF = 0;
    
    
    public static String[] SONS = {"Superboy.mp3","toc.mp3"};
    
    
    
    public static String cheminSon(int i)
    {
        return DOSSIERSONS+SONS[i];
    }    
    
     public static String nomImageCaseAccessible(Joueur joueur)
    {
        return DOSSIERIMAGES+"/accessible"+joueur.couleur+".png";
    }
    
    
    public static String nomImageSon(int status,int type)
    {
        String path = DOSSIERIMAGES+"/Sons/";
        String sType;
        String sStatus;
        if(status == ON)
        {
            sStatus = "on" ;
        }
        else
        {
            sStatus = "off" ;
        }
        
        if(type == SON)
        {
            sType = "son" ;
        }
        else
        {
            sType = "musique" ;
        }
        
        path = path + sType + "_" + sStatus+".png";
        return path;
    }

    public static Paint couleurJoueur(Joueur joueur)
    {
        if(joueur.couleur == 0)
        {
            return Color.MAGENTA;             
        }
        else if(joueur.couleur == 1)
        {
              return Color.YELLOW;        
        }
        else if(joueur.couleur == 2)
        {            
            return Color.RED;
        }
        else //  if(joueur.couleur == 3)
        {
            return Color.LIME;             
        }
    }
}
