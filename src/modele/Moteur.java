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
    
    public Moteur(Joueur[] joueurs)
    {
        if(joueurs.length<=1||joueurs.length>=5)
        {
            throw new RuntimeException("Le nombre de joueur doit etre compris entre 2 et 4");
        }
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

    public boolean placerPingouin(int i, int j)
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
        else
        {
            return false;
        }
    }
    
 
    
    public ArrayList<Point> deplacementsPossibles(int ligne, int colonne)
    {
        ArrayList<Point> deplacements = new ArrayList<>();
        if(plateau.plateau[ligne][colonne].contientPingouin(joueurCourant))
        {    
            System.out.println("GAUCHE");
            for(int j = colonne-1 ; j>=0 ; j--)
            {
                if(plateau.plateau[ligne][j].peutDeplacerPingouin())
                {
                    System.out.println(new Point(ligne,j));
                    deplacements.add(new Point(ligne,j));
                }
                else
                {
                    break;
                }
            }
            System.out.println("DROITE");
            for(int j = colonne+1 ; j<Plateau.nbTuilesLigne(ligne) ; j++)
            {
                if(plateau.plateau[ligne][j].peutDeplacerPingouin())
                {
                    System.out.println(new Point(ligne,j));
                    deplacements.add(new Point(ligne,j));
                }
                else
                {
                    break;
                }
            }
            System.out.println("HAUT GAUCHE");
            for(int i = ligne-1 ; i>=0 ; i--)
            {
                int j = colonne-(ligne-i-(ligne%2))/2-(ligne%2);
                System.out.println(new Point(i,j));
                if(j>=0 && plateau.plateau[i][j].peutDeplacerPingouin())
                {
                    deplacements.add(new Point(i,j));
                }
                else
                {
                    break;
                }
            }
            System.out.println("HAUT DROITE");
            for(int i = ligne-1 ; i>=0 ; i--)
            {
                int j = colonne+(ligne-i-((ligne+1)%2))/2+((ligne+1)%2);
                System.out.println(j);
                if(j<Plateau.nbTuilesLigne(i) && plateau.plateau[i][j].peutDeplacerPingouin())
                {
                    System.out.println(new Point(i,j));
                    deplacements.add(new Point(i,j));
                }
                else
                {
                    break;
                }
            }
            System.out.println("BAS GAUCHE");
            for(int i = ligne+1 ; i<plateau.plateau.length ; i++)
            {
                int j = colonne-(i-ligne-(ligne%2))/2-(ligne%2);
                System.out.println(j);
                if(j>=0 && plateau.plateau[i][j].peutDeplacerPingouin())
                {
                    System.out.println(new Point(i,j));
                    deplacements.add(new Point(i,j));
                }
                else
                {
                    break;
                }
            }
            System.out.println("BAS DROITE");
            for(int i = ligne+1 ; i<plateau.plateau.length ; i++)
            {
                int j = colonne+(i-ligne-((ligne+1)%2))/2+((ligne+1)%2);
                System.out.println(j);
                if(j<Plateau.nbTuilesLigne(i) && plateau.plateau[i][j].peutDeplacerPingouin())
                {
                    System.out.println(new Point(i,j));
                    deplacements.add(new Point(i,j));
                }
                else
                {
                    break;
                }
            }
        }
        return deplacements;
    }
    

    public int nbPingouinsTotal()
    {
        return nbJoueursInit;
        /*if(nbJoueursInit == 3)
        {
            return 9;
        }
        else
        {
            return 8;
        }*/
    }
    
    public boolean pingouinsPlaces()
    {
        return nbPingouinsPlaces == nbPingouinsTotal();
    }
    
    private void joueurSuivant()
    {
        joueurCourant = (joueurCourant+1)%joueurs.length;
    }

    public boolean contientJoueurCourant(int ligne, int colonne)    
    {
        return plateau.plateau[ligne][colonne].contientPingouin(joueurCourant);
    }

    public boolean deplacerPingouin(int ligneSource, int colonneSource, int ligneDest, int colonneDest)
    {
        if(contientJoueurCourant(ligneSource, colonneSource) && deplacementsPossibles(ligneSource, colonneSource).contains(new Point(ligneDest,colonneDest)))
        {
            plateau.plateau[ligneSource][colonneSource].enleverPingouin();
            plateau.plateau[ligneDest][colonneDest].ajouterPingouin(joueurCourant);
            joueurSuivant();
            return true;
        }
        else
        {
            return false;
        }
    }
}
