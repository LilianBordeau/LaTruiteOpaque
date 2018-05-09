package modele;

public class Constantes
{
    public static final String DOSSIERIMAGES = "Images";
            
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
        return DOSSIERIMAGES+"/pingouins/"+joueur.numero+"_0_6.png";
    }       
}
