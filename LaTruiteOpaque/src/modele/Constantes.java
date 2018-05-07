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
            return DOSSIERIMAGES+"/hexagone"+uneCase.nbPoissons+"Poisson"+((uneCase.nbPoissons >= 2)?"s":"")+".png";
        }
    }
    
    public static String nomImagePingouin(Case uneCase)
    {
        String nomImage = null;
        if(uneCase.estOccupee())
        {
            return DOSSIERIMAGES+"/pingouin"+(uneCase.numJoueurPingouin+1)+".png";
        }
        else
        {
            throw new RuntimeException("image manquante");
        }
    }
            
        
}
