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
    
    public static String nomImagePingouin(Joueur joueur)
    {
        String couleurJoueur = null;
        if(joueur.numero == 0)
        {
            couleurJoueur = "Bleu";
        }
        else if(joueur.numero == 1)
        {
            couleurJoueur = "Jaune";
        }
        else if(joueur.numero == 2)
        {
            couleurJoueur = "Rouge";
        }
        else // if(joueur.numero == 3)
        {
            couleurJoueur = "Vert";
        }
        return DOSSIERIMAGES+"/pingouin"+couleurJoueur+".png";
    }       
}
