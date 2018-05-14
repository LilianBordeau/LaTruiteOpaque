package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import thread.MyThread;

public class JoueurIAAleatoire extends JoueurAAttendre implements Serializable,Cloneable
{
    private static final int DUREEMINTOURMILLIS = 500;
    
     /* tire une case au hasard parmi les 60 du plateau (meme celles coulees et deja occupees) et renvoie la premiere case contenant
        1 poisson a partir de celle-ci (en parcourant le plateau de bas en haut puis de gauche a droite et en revenant au debut quand
        la fin du plateau est atteinte) */
    @Override
    public Point getPlacementSuivant()
    {
        initRandomSiNull();
        int ligne = random.nextInt(8);
        Point placement = new Point(ligne, random.nextInt(Plateau.nbTuilesLigne(ligne)));		
        do
        {
            placement.colonne++;
            if(placement.colonne == Plateau.nbTuilesLigne(placement.ligne))
            {
		placement.colonne = 0;
		placement.ligne = (placement.ligne+1)%moteur.plateau.plateau.length;
            }
        } while(!moteur.plateau.plateau[placement.ligne][placement.colonne].peutPlacerPingouin());
        MyThread.sleep(DUREEMINTOURMILLIS);
        return placement;
    }
    
    
    /* tire un pingouin au hasard et choisi le premier pingouin non bloque a partir de celui ci (en parcourant les pingouins du joueur dans
    l'ordre dans lequel ils ont ete place en debut de partie et en revenant au debut la liste quand sa fin est atteinte) puis renvoi un des
    deplacements possibles (choisi aleatoirement) de ce pingouin */
    @Override
    public Deplacement getDeplacementSuivant()
    {
        initRandomSiNull();
        int numPingouin = random.nextInt(pingouins.size());
	do
        {
            numPingouin = (numPingouin+1)%pingouins.size();
        } while(pingouins.get(numPingouin).estBloque);
        Pingouin pingouin = pingouins.get(numPingouin);
        ArrayList<Point> deplacements = moteur.deplacementsPossibles(pingouin.ligne, pingouin.colonne);
	Point destination = deplacements.get(random.nextInt(deplacements.size()));
        MyThread.sleep(DUREEMINTOURMILLIS);
	return new Deplacement(pingouin.ligne, pingouin.colonne, destination.ligne, destination.colonne);
    }
    
    @Override
    public JoueurIAAleatoire clone()
    {
        return (JoueurIAAleatoire)super.clone();
    }
}
