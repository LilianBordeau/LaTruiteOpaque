package modele;

public class Constantes
{
    public static final String DOSSIERIMAGES = "Images";
    public static final String DOSSIERSONS = "rsc/Sons/";
            
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
        return DOSSIERIMAGES+"/pingouins/"+joueur.couleur+"_0_6.png";
    }       

    
    public static final int CLIQUEBOUTON = 1;
    
    public static final int SON = 1;
    public static final int MUSIQUE = 0;
    public static final int ON = 1;
    public static final int OFF = 0;
    
    
    public static String[] SONS = {"Superboy.mp3","bruitBouton.mp3"};
    
    
    
    public static String cheminSon(int i)
    {
        return DOSSIERSONS+SONS[i];
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
}
