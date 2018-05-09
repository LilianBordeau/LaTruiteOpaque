package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class JoueurIAAleatoire extends JoueurAAttendre implements Serializable
{
    // un generateur de nombres aleatoires ne doit pas etre serialise car sinon il produira les memes valeurs apres deserialisation
    private transient Random random = new Random();
    
    @Override
    public Point getPlacementSuivant() // tire une case au hasard et renvoie la premiere case contenant 1 poisson a partir de celle-ci
    {
	int ligne = random.nextInt(8);
        Point placement = new Point(ligne, random.nextInt(Plateau.nbTuilesLigne(ligne)));		
        do
        {
            placement.colonne++;
            if(placement.colonne == Plateau.nbTuilesLigne(ligne))
            {
		placement.colonne = 0;
		placement.ligne = (placement.ligne+1)%8;
            }
        } while(moteur.plateau.plateau[placement.ligne][placement.colonne].nbPoissons != 1);
        return placement;
    }
    
    @Override
    public Deplacement getDeplacementSuivant()
    {
        int numPingouin = random.nextInt(4);
	do
        {
            numPingouin = (numPingouin+1)%pingouins.size();
        } while(pingouins.get(numPingouin).estBloque);
        Pingouin pingouin = pingouins.get(numPingouin);
        ArrayList<Point> deplacements = moteur.deplacementsPossibles(pingouin.ligne, pingouin.colonne);
	Point destination = deplacements.get(random.nextInt(deplacements.size()));
	return new Deplacement(pingouin.ligne, pingouin.colonne, destination.ligne, destination.colonne);
    }
}
