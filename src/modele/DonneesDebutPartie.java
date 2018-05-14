package modele;

import java.io.Serializable;

public class DonneesDebutPartie implements Serializable
{
    public Plateau plateau;
    public int numJoueur;
    public Joueur[] joueurs;
    
    public DonneesDebutPartie(Plateau plateau, Joueur[] joueurs, int numJoueur)
    {
        this.plateau = plateau;
        this.joueurs = joueurs;
        this.numJoueur = numJoueur;
    }
}
