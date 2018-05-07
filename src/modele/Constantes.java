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
            int randomInt = (int) (Math.random() * 3) + 1; 
            return DOSSIERIMAGES+"/tuile"+uneCase.nbPoissons+"_"+randomInt+((uneCase.nbPoissons >= 2)?"":"")+".png";
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
