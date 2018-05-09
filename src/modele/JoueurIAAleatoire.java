package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class JoueurIAAleatoire extends Joueur implements Serializable
{
    // un generateur de nombres aleatoires ne doit pas etre serialise car sinon il produira les memes valeurs apres deserialisation
    private transient Random random = new Random();
    
    public Point getPlacementSuivant()
    {
        Point placement = new Point(-1, -1);
        do
        {
            placement.ligne = random.nextInt(8);
            placement.colonne = random.nextInt(Plateau.nbTuilesLigne(placement.ligne));
        } while(moteur.plateau.plateau[placement.ligne][placement.colonne].estOccupee());
        return placement;
    }
    
    /*public ArrayList<Deplacement> getDeplacementSuivant()
    {
        int numeroPingouin = random.nextInt(4);
        ArrayList<Point> deplacementsPossibles = null;
        for(Pingouin pingouin : pingouins)
        {
            
        }
    }*/
}
