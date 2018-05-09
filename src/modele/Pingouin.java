package modele;

import java.io.Serializable;

public class Pingouin implements Serializable
{
    public int ligne;
    public int colonne;
    public int numJoueur;
    public boolean estBloque;
    
    public Pingouin(int ligne, int colonne, int numJoueur)
    {
        this.ligne = ligne;
        this.colonne = colonne;
        this.numJoueur = numJoueur;
        this.estBloque = false;
    }
}
