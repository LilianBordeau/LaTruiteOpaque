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
    
    public static String[] SONS = {"Superboy.mp3","bruitBouton.mp3"};
    
    
    
    public static String cheminSon(int i)
    {
        return DOSSIERSONS+SONS[i];
    }    
}
