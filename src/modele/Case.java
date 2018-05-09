package modele;

import java.io.Serializable;

public class Case implements Serializable
{
    /*POISSON1(1,-1),POISSON2(2,-1),POISSON3(3,-1),
    POISSON1J0(1,0),POISSON2J0(2,0),POISSON3J0(3,0),
    POISSON1J1(1,1),POISSON2J1(2,1),POISSON3J1(3,1),
    POISSON1J2(1,2),POISSON2J2(2,2),POISSON3J2(3,2),
    POISSON1J3(1,3),POISSON2J3(2,3),POISSON3J3(3,3),
    EAU(0,-1);*/
    
    public int nbPoissons;
    public transient Pingouin pingouin;
    private int ligne;
    private int colonne;
    
    public int numJoueurPingouin()
    {
        return (pingouin == null) ? -1 : pingouin.numJoueur;
    }
    
    /*public Case(int nbPoissons, int numJoueurPingouin)
    {
        this.nbPoissons = nbPoissons;
        this.numJoueurPingouin = numJoueurPingouin;
    }*/
    
    public Case(int nbPoissons, int ligne, int colonne)
    {
        if(nbPoissons <= -1 || nbPoissons >= 4)
        {
            throw new RuntimeException("tentative d'instanciation d'une case avec un nombre invalide de poisson ("+nbPoissons+")");
        }
        else if(ligne <= -1 || ligne >= 8 ||colonne <= -1 || colonne >= Plateau.nbTuilesLigne(ligne))
        {
            throw new RuntimeException("tentative d'instanciation d'une case avec des coordonnees invalides "+new Point(ligne, colonne));
        }
        this.nbPoissons = nbPoissons;
        this.pingouin = null;
        this.ligne = ligne;
        this.colonne = colonne;
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
        return numJoueurPingouin() >= 0;
    }
    
    public boolean estCoulee()
    {
        return nbPoissons == 0;
    }

    public void ajouterPingouin(Pingouin pingouin)
    {
        this.pingouin = pingouin;
        this.pingouin.ligne = ligne;
        this.pingouin.colonne = colonne;
    }

    public boolean contientPingouin(int numJoueur)
    {
        return numJoueurPingouin() == numJoueur;
    }

    public Case enleverPingouin()
    {
        Case ancienneCase = new Case(nbPoissons, ligne, colonne);
        ancienneCase.pingouin = pingouin;
        nbPoissons = 0;
        pingouin = null;
        return ancienneCase;
    }
    
}
