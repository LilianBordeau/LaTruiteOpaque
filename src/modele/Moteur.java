package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Moteur implements Serializable
{
    public Plateau plateau;
    public Joueur[] joueurs;
    public int nbJoueursInit;
    public int joueurCourant;
    public int nbPingouinsPlaces;
    
    public Moteur(Joueur[] joueurs)
    {
        nbPingouinsPlaces = 0;
        joueurCourant = 0; 
        plateau = new Plateau();  
        setJoueurs(joueurs);
    }
    
    public void setJoueurs(Joueur[] joueurs)
    {
        if(joueurs.length<=1||joueurs.length>=5)
        {
            throw new RuntimeException("Le nombre de joueur doit etre compris entre 2 et 4");
        }
        this.joueurs = joueurs;
        nbJoueursInit = joueurs.length;
        for(int i = 0 ; i < this.joueurs.length ; i++)
        {
            joueurs[i].numero = i;
            joueurs[i].nbPingouinsAPlacer = nbPingouinsTotal()/joueurs.length;                 
            joueurs[i].moteur = this;
        }
    }

    // renvoie null si le placement n'est pas possible ou la liste des nouveaux pingouins bloques sinon
    public ArrayList<Point> placerPingouin(int i, int j)
    {
        Case caseCourante = plateau.plateau[i][j];
        if(!pingouinsPlaces())
        {
            if(caseCourante.peutPlacerPingouin())
            {           
                Pingouin pingouin = new Pingouin(i,j,joueurCourant);
                joueurs[joueurCourant].ajouterPingouin(pingouin);
                caseCourante.ajouterPingouin(pingouin);
                nbPingouinsPlaces++;
                return finTour(i, j);
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    
 
    
    public ArrayList<Point> deplacementsPossibles(int ligne, int colonne)
    {
        ArrayList<Point> deplacements = new ArrayList<>();
        if(plateau.plateau[ligne][colonne].contientPingouin(joueurCourant))
        {    
            //System.out.println("GAUCHE");
            for(int j = colonne-1 ; j>=0 ; j--)
            {
                if(plateau.plateau[ligne][j].peutDeplacerPingouin())
                {
                    //System.out.println(new Point(ligne,j));
                    deplacements.add(new Point(ligne,j));
                }
                else
                {
                    break;
                }
            }
            //System.out.println("DROITE");
            for(int j = colonne+1 ; j<Plateau.nbTuilesLigne(ligne) ; j++)
            {
                if(plateau.plateau[ligne][j].peutDeplacerPingouin())
                {
                    //System.out.println(new Point(ligne,j));
                    deplacements.add(new Point(ligne,j));
                }
                else
                {
                    break;
                }
            }
            //System.out.println("HAUT GAUCHE");
            for(int i = ligne-1 ; i>=0 ; i--)
            {
                int j = colonne-(ligne-i-(ligne%2))/2-(ligne%2);
                //System.out.println(new Point(i,j));
                if(j>=0 && plateau.plateau[i][j].peutDeplacerPingouin())
                {
                    deplacements.add(new Point(i,j));
                }
                else
                {
                    break;
                }
            }
            //System.out.println("HAUT DROITE");
            for(int i = ligne-1 ; i>=0 ; i--)
            {
                int j = colonne+(ligne-i-((ligne+1)%2))/2+((ligne+1)%2);
                //System.out.println(j);
                if(j<Plateau.nbTuilesLigne(i) && plateau.plateau[i][j].peutDeplacerPingouin())
                {
                    //System.out.println(new Point(i,j));
                    deplacements.add(new Point(i,j));
                }
                else
                {
                    break;
                }
            }
            //System.out.println("BAS GAUCHE");
            for(int i = ligne+1 ; i<plateau.plateau.length ; i++)
            {
                int j = colonne-(i-ligne-(ligne%2))/2-(ligne%2);
                //System.out.println(j);
                if(j>=0 && plateau.plateau[i][j].peutDeplacerPingouin())
                {
                    //System.out.println(new Point(i,j));
                    deplacements.add(new Point(i,j));
                }
                else
                {
                    break;
                }
            }
            //System.out.println("BAS DROITE");
            for(int i = ligne+1 ; i<plateau.plateau.length ; i++)
            {
                int j = colonne+(i-ligne-((ligne+1)%2))/2+((ligne+1)%2);
                //System.out.println(j);
                if(j<Plateau.nbTuilesLigne(i) && plateau.plateau[i][j].peutDeplacerPingouin())
                {
                    //System.out.println(new Point(i,j));
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
        int joueurPrecedent = joueurCourant;
        do
        {
            joueurCourant = (joueurCourant+1)%joueurs.length;
        } while(joueurs[joueurCourant].estBloque() && joueurCourant != joueurPrecedent);
    }

    public boolean contientJoueurCourant(int ligne, int colonne)    
    {
        return plateau.plateau[ligne][colonne].contientPingouin(joueurCourant);
    }

    // renvoie null si le deplacement n'est pas possible ou la liste des nouveaux pingouins bloques sinon
    public ArrayList<Point> deplacerPingouin(int ligneSource, int colonneSource, int ligneDest, int colonneDest)
    {
        if(contientJoueurCourant(ligneSource, colonneSource) && deplacementsPossibles(ligneSource, colonneSource).contains(new Point(ligneDest,colonneDest)))
        {
            Case ancienneCase = plateau.plateau[ligneSource][colonneSource].enleverPingouin();
            joueurs[joueurCourant].scorePoisson += ancienneCase.nbPoissons;
            joueurs[joueurCourant].scoreTuile++;
            plateau.plateau[ligneDest][colonneDest].ajouterPingouin(ancienneCase.pingouin);            
            return finTour(ligneDest, colonneDest);
        }
        else
        {
            return null;
        }
    }
    
    public boolean peutDeplacerPingouin(Pingouin pingouin)
    {
        /*int colonneGauche = pingouin.colonne-(pingouin.ligne%2);
        if(pingouin.colonne >= 1 && plateau.plateau[pingouin.ligne][pingouin.colonne-1].peutDeplacerPingouin())
        {
            return true;
        }
        else if(pingouin.colonne <= Plateau.nbTuilesLigne(pingouin.ligne)-2 && plateau.plateau[pingouin.ligne][pingouin.colonne+1].peutDeplacerPingouin())
        {
            return true;
        }
        else if(pingouin.colonne <= 6)
        {
            if(pingouin.ligne >= 1 && plateau.plateau[pingouin.ligne-1][colonneGauche+1].peutDeplacerPingouin())
            {
                return true;
            }
            else if(pingouin.ligne <= 6 && plateau.plateau[pingouin.ligne+1][colonneGauche+1].peutDeplacerPingouin())
            {
                return true;
            }
        }
        else if(pingouin.colonne >= 1 || Plateau.nbTuilesLigne(pingouin.ligne) == 7)
        {
            if(pingouin.ligne >= 1 && plateau.plateau[pingouin.ligne-1][colonneGauche].peutDeplacerPingouin())
            {
                return true;
            }
            else if(pingouin.ligne <= 6 && plateau.plateau[pingouin.ligne+1][colonneGauche].peutDeplacerPingouin())
            {
                return true;
            }
        }
        return false;*/      
        return !casesAdjacentesLibres(pingouin).isEmpty();
    }
    
    public ArrayList<Point> casesAdjacentesLibres(Pingouin pingouin)
    {
        ArrayList<Point> casesAdjLibre = casesAdjacentes(pingouin);
        for(int i = casesAdjLibre.size()-1 ; i >= 0 ; i--)
        {
            Point point = casesAdjLibre.get(i);
            if(!plateau.plateau[point.ligne][point.colonne].peutDeplacerPingouin())
            {
                casesAdjLibre.remove(i);
            }
        }      
        return casesAdjLibre;
    }
    
    public ArrayList<Point> casesAdjacentes(Pingouin pingouin)
    {
        ArrayList<Point> cases = new ArrayList<>();
        int colonneGauche = pingouin.colonne-(pingouin.ligne%2);
        cases.add(new Point(pingouin.ligne,pingouin.colonne-1));
        cases.add(new Point(pingouin.ligne,pingouin.colonne+1));
        cases.add(new Point(pingouin.ligne-1,colonneGauche));
        cases.add(new Point(pingouin.ligne+1,colonneGauche));
        cases.add(new Point(pingouin.ligne-1,colonneGauche+1));
        cases.add(new Point(pingouin.ligne+1,colonneGauche+1));
        for(int i = cases.size()-1 ; i >= 0 ; i--)
        {
            Point caseCourante = cases.get(i);
            if(caseCourante.ligne <= -1 || caseCourante.ligne >= plateau.plateau.length 
               || caseCourante.colonne <= -1 || caseCourante.colonne >= Plateau.nbTuilesLigne(caseCourante.ligne))
            {
                cases.remove(i);
            }
        }
        return cases;
    }
    
    public boolean estPartieTerminee()
    {
        for(Joueur joueur : joueurs)
        {
            if(!joueur.estBloque())
            {
                return false;
            }
        }
        return true;
    }
    
    public ArrayList<Joueur> joueursGagnants()
    {
        /*for(Joueur joueur : joueurs)
        {
            for(Pingouin pingouin : joueur.pingouins)
            {
                joueur.scorePoisson += plateau.plateau[pingouin.ligne][pingouin.colonne].nbPoissons;
                joueur.scoreTuile++;
            }
        }*/
        ArrayList<Joueur> lesGagnants = new ArrayList<>(Arrays.asList(joueurs));
        Comparator comparator = new Comparator<Joueur>()
                         {
                            @Override
                            public int compare(Joueur j1, Joueur j2)
                            {
                                if(j1.scorePoisson == j2.scorePoisson)
                                {
                                    return -new Integer(j1.scoreTuile).compareTo(j2.scoreTuile);
                                }
                                else
                                {
                                    return -new Integer(j1.scorePoisson).compareTo(j2.scorePoisson);
                                }
                            }
                         };
        lesGagnants.sort(comparator);  
        for(int i = lesGagnants.size()-1 ; i >= 0 && comparator.compare(lesGagnants.get(i), lesGagnants.get(0)) > 0  ; i--)
        {
            lesGagnants.remove(i);
        }
        return lesGagnants;
    }

    /* renvoie la liste des nouveaux pingouins bloques apres placement ou deplacement d'un pingouin (methode appelee par placerPingouin et 
       deplacerPingouin) */
    private ArrayList<Point> finTour(int ligneDest, int colonneDest)
    {
        ArrayList<Point> nouveauxPingouinsBloques = casesAdjacentes(plateau.plateau[ligneDest][colonneDest].pingouin);
        nouveauxPingouinsBloques.add(new Point(ligneDest,colonneDest));
        for(int i = nouveauxPingouinsBloques.size()-1 ; i >= 0 ; i--)
        {
            Point pointPingouin = nouveauxPingouinsBloques.get(i);
            Case casePingouin = plateau.plateau[pointPingouin.ligne][pointPingouin.colonne];
            if(!casePingouin.estOccupee() || casePingouin.pingouin.estBloque || peutDeplacerPingouin(casePingouin.pingouin) )
            {
                nouveauxPingouinsBloques.remove(i);
            }
            else
            {
                casePingouin.pingouin.estBloque = true;
                joueurs[casePingouin.pingouin.numJoueur].scoreTuile++;
                joueurs[casePingouin.pingouin.numJoueur].scorePoisson += casePingouin.nbPoissons;
                casePingouin.nbPoissons = 0;
            }
        }
        joueurSuivant();
        System.out.println(nouveauxPingouinsBloques);
        return nouveauxPingouinsBloques;
    }

}
