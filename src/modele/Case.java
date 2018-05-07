package modele;
public class Case
{
    /*POISSON1(1,-1),POISSON2(2,-1),POISSON3(3,-1),
    POISSON1J0(1,0),POISSON2J0(2,0),POISSON3J0(3,0),
    POISSON1J1(1,1),POISSON2J1(2,1),POISSON3J1(3,1),
    POISSON1J2(1,2),POISSON2J2(2,2),POISSON3J2(3,2),
    POISSON1J3(1,3),POISSON2J3(2,3),POISSON3J3(3,3),
    EAU(0,-1);*/
    
    public int nbPoissons;
    public int numJoueurPingouin;
    
    public Case(int nbPoissons, int numJoueurPingouin)
    {
        this.nbPoissons = nbPoissons;
        this.numJoueurPingouin = numJoueurPingouin;
    }
    
    public Case(int nbPoissons) // case sans pingouin
    {
        this(nbPoissons,-1);
    }
    
    public static Case eau()
    {
        return new Case(0);
    }
    
    public boolean peutPlacerPingouin()
    {
        return !estOccupee() && nbPoissons==1;
    }
    
    public boolean peutDeplacerPingouin()
    {
        return !estOccupee() && !estCoulee();
    }
    
    public boolean estOccupee()
    {
        return numJoueurPingouin >= 0;
    }
    
    public boolean estCoulee()
    {
        return nbPoissons == 0;
    }

    void ajouterPingouin(int numJoueurPingouin)
    {
        this.numJoueurPingouin = numJoueurPingouin;
    }

    boolean contientPingouin(int numJoueur)
    {
        return numJoueurPingouin == numJoueur;
    }
    
}
