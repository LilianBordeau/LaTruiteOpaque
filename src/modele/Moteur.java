package modele;

import java.io.Serializable;
import java.util.ArrayList;

public class Moteur implements Serializable
{
    public Plateau plateau;
    public Joueur[] joueurs;
    public int nbJoueursInit;
    public int joueurCourant;
    public int nbPingouinsPlaces;
    public int ligneSelectionnee;
    public int colonneSelectionnee;
    
    public Moteur(Joueur[] joueurs)
    {
        if(joueurs.length<=1||joueurs.length>=5)
        {
            throw new RuntimeException("Le nombre de joueur doit etre compris entre 2 et 4");
        }
        ligneSelectionnee = -1;
        colonneSelectionnee = -1;
        nbPingouinsPlaces = 0;
        joueurCourant = 0;
        plateau = new Plateau();
        this.joueurs = joueurs;
        nbJoueursInit = joueurs.length;
        for(int i = 0 ; i < this.joueurs.length ; i++)
        {
            joueurs[i].numero = i;
            joueurs[i].plateau = plateau;
        }
    }

    public boolean jouer(int i, int j)
    {
        Case caseCourante = plateau.plateau[i][j];
        if(!pingouinsPlaces())
        {
            if(caseCourante.peutPlacerPingouin())
            {                
                caseCourante.ajouterPingouin(joueurCourant);
                nbPingouinsPlaces++;
                joueurSuivant();
                return true;
            }
            else
            {
                return false;
            }
        }
        else if(ligneSelectionnee == -1)
        {
            /*if(caseCourante.contientPingouin(joueurCourant))
            {                
                ligneSelectionnee = i;
                colonneSelectionnee = j;
            }
            else
            {
                return false;
            }*/
            return false;
        }
        else
        {
            return false;
            /*if(caseCourante.contientPingouin(joueurCourant))
            {                
                ligneSelectionnee = i;
                colonneSelectionnee = j;
            }
            else
            {
                return false;
            }*/
        }
    }
    
    public Point[] deplacementsPossibles(int ligne, int colonne)
    {
        ArrayList<Point> deplacements = new ArrayList<>();
        // utiliser classe Limite
        /*for(int j = colonne-1 ; j>=0 ; j--)
        {
            if(plateau.plateau[ligne][j].peutDeplacerPingouin())
            {
                //deplacements.add(new Point(li))
            }
            else
            {
                break;
            }
        }*/
        return (Point[])deplacements.toArray();
    }
    

    public int nbPingouinsTotal()
    {
        if(nbJoueursInit == 3)
        {
            return 9;
        }
        else
        {
            return 8;
        }
    }
    
    public boolean pingouinsPlaces()
    {
        return nbPingouinsPlaces == nbPingouinsTotal();
    }
    
    private void joueurSuivant()
    {
        joueurCourant = (joueurCourant+1)%joueurs.length;
    }
}
