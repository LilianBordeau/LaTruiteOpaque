package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Stack;
import util.Couple;

public class Moteur implements Serializable,Cloneable
{
    public Plateau plateau;
    public Joueur[] joueurs;
    public int nbJoueursInit;
    public int joueurCourant;
    public int nbPingouinsPlaces;
    public Moteur sauvegardeDebutPartie;
    public Stack<Moteur> coupJoues;
    public Stack<Moteur> coupAnnules;
    public Coup dernierCoupJoue;
    public int joueurPrecedent;
    public boolean sauvegarderCoupJoues;
    public boolean estEnReseau;
    public boolean queDesIA;
    
    public Moteur(Joueur[] joueurs)
    {
        sauvegarderCoupJoues = true;
        coupJoues = new Stack<>();
        coupAnnules = new Stack<>();
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
        joueurPrecedent = -1;
        this.joueurs = joueurs;
        nbJoueursInit = joueurs.length;
        estEnReseau = false;
        queDesIA = true;
        for(int i = 0 ; i < this.joueurs.length ; i++)
        {            
            joueurs[i].numero = i;
            joueurs[i].nbPingouinsAPlacer = nbPingouinsTotal()/joueurs.length;
            if(joueurs[i] instanceof JoueurHumain)
            {
                queDesIA = false;
            }
            else if(joueurs[i] instanceof JoueurReseau)
            {
                estEnReseau = true;
                queDesIA = false;
            }
        }
        sauvegarderDebutPartie();
    }

    // renvoie null si le placement n'est pas possible ou la liste des nouveaux pingouins bloques sinon
    public ArrayList<Point> placerPingouin(int i, int j)
    {
        Case caseCourante = plateau.plateau[i][j];
        if(!pingouinsPlaces())
        {
            if(caseCourante.peutPlacerPingouin())
            {        
                Moteur moteurAvantCoup = this.clone();
                Pingouin pingouin = new Pingouin(i,j,joueurCourant);
                joueurs[joueurCourant].ajouterPingouin(pingouin);
                caseCourante.ajouterPingouin(pingouin);
                nbPingouinsPlaces++;
                dernierCoupJoue = new Point(i,j);
                return finTour(i, j, moteurAvantCoup);
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
        return deplacementsPossiblesEtDirection(ligne, colonne).premier;
    }
    
    private void ajouterDeplacementsHorizontaux(ArrayList<Point> deplacements, ArrayList<Integer> indicesDirections, int ligne, int debut, int apresFin, int pas)
    {
        for(int j = debut ; j!=apresFin ; j+=pas)
        {
            if(plateau.plateau[ligne][j].peutDeplacerPingouin())
            {
                deplacements.add(new Point(ligne,j));
            }
            else
            {
                break;
            }
        }
        indicesDirections.add(deplacements.size());
    }
    
    private void ajouterDeplacementsDiagonale(ArrayList<Point> deplacements, ArrayList<Integer> indicesDirections, int ligne, int debut, int apresFin, int pas, int numDiagonale, int colonne)
    {
        int j = -1;
        for(int i = debut ; i!=apresFin ; i+=pas)
        {
            if(numDiagonale == 0)
            {
                j = colonne-(ligne-i-(ligne%2))/2-(ligne%2);
            }
            else if(numDiagonale == 1)
            {
                j = colonne+(ligne-i-((ligne+1)%2))/2+((ligne+1)%2);
            }
            else if(numDiagonale == 2)
            {
                j = colonne-(i-ligne-(ligne%2))/2-(ligne%2);
            }
            else if(numDiagonale == 3)
            {
                j = colonne+(i-ligne-((ligne+1)%2))/2+((ligne+1)%2);
            }            
            if(j>=0 && j<Plateau.nbTuilesLigne(i) && plateau.plateau[i][j].peutDeplacerPingouin())
            {
                deplacements.add(new Point(i,j));
            }
            else
            {
                break;
            }
        }
        indicesDirections.add(deplacements.size());
    }
    
    /* renvoie la liste des deplacements possible en partant du pingouin ligne colonne et un tableau contenant les indices
    de cette liste ou la direction (GAUCHE,DROITE, BAS GAUCHE, HAUT GAUCHE, BAS DROITE ou HAUT DROITE) change
    (les deplacements dans la meme direction sont a des indices qui se suivent dans le tableau) */
    public Couple<ArrayList<Point>, ArrayList<Integer>> deplacementsPossiblesEtDirection(int ligne, int colonne)
    {
        ArrayList<Point> deplacements = new ArrayList<>();
        ArrayList<Integer> indicesDirections = new ArrayList<>();
        if(plateau.plateau[ligne][colonne].contientPingouin(joueurCourant))
        {    
            ajouterDeplacementsHorizontaux(deplacements, indicesDirections, ligne, colonne-1, -1, -1);
            ajouterDeplacementsHorizontaux(deplacements, indicesDirections, ligne, colonne+1, Plateau.nbTuilesLigne(ligne), 1);
            ajouterDeplacementsDiagonale(deplacements, indicesDirections, ligne, ligne-1, -1, -1, 0, colonne);
            ajouterDeplacementsDiagonale(deplacements, indicesDirections, ligne, ligne-1, -1, -1, 1, colonne);
            ajouterDeplacementsDiagonale(deplacements, indicesDirections, ligne, ligne+1, plateau.plateau.length, 1, 2, colonne);
            ajouterDeplacementsDiagonale(deplacements, indicesDirections, ligne, ligne+1, plateau.plateau.length, 1, 3, colonne);
        }
        return new Couple<>(deplacements,indicesDirections);
    }
    

    public int nbPingouinsTotal()
    {
        //return nbJoueursInit*2;
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
        joueurPrecedent = joueurCourant;
        joueurCourant = getJoueurSuivant();
    }
    
    public int getJoueurSuivant()
    {
        int joueurCour = joueurCourant;
        do
        {
            joueurCour = (joueurCour+1)%joueurs.length;
        } while(joueurs[joueurCour].estBloque() && joueurCour != joueurCourant);
        return joueurCour;
    }

    public boolean contientJoueurCourant(int ligne, int colonne)    
    {
        try{
            return plateau.plateau[ligne][colonne].contientPingouin(joueurCourant);
        }catch(IndexOutOfBoundsException e)
        {
            throw new RuntimeException((new Point(ligne,colonne)).toString());
        }
        
    }

    // renvoie null si le deplacement n'est pas possible ou la liste des nouveaux pingouins bloques sinon
    public ArrayList<Point> deplacerPingouin(int ligneSource, int colonneSource, int ligneDest, int colonneDest)
    {
        if(contientJoueurCourant(ligneSource, colonneSource) && deplacementsPossibles(ligneSource, colonneSource).contains(new Point(ligneDest,colonneDest)))
        {
            Moteur moteurAvantCoup = this.clone();
            Case ancienneCase = plateau.plateau[ligneSource][colonneSource].enleverPingouin();
            joueurs[joueurCourant].scorePoisson += ancienneCase.nbPoissons;
            joueurs[joueurCourant].scoreTuile++;
            plateau.plateau[ligneDest][colonneDest].ajouterPingouin(ancienneCase.pingouin); 
            dernierCoupJoue = new Deplacement(ligneSource, colonneSource, ligneDest, colonneDest, ancienneCase.nbPoissons);
            return finTour(ligneDest, colonneDest, moteurAvantCoup);
        }
        else
        {
            return null;
        }
    }
    
    public boolean peutDeplacerPingouin(Pingouin pingouin)
    {    
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
    
    
    
    private class ComparatorJoueur implements Comparator<Joueur> {
        
 
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
    
    }
    
    
    
    public ArrayList<Joueur> joueursGagnants()
    {
        ComparatorJoueur comparatorJoueur = new ComparatorJoueur();
        ArrayList<Joueur> lesGagnants = getClassement();
        
        for(int i = lesGagnants.size()-1 ; i >= 0 && comparatorJoueur.compare(lesGagnants.get(i), lesGagnants.get(0)) > 0  ; i--)
        {
            lesGagnants.remove(i);
        }
        return lesGagnants;
    }
    
    public ArrayList<Joueur> getClassement()
    {
        ComparatorJoueur comparatorJoueur = new ComparatorJoueur();
        ArrayList<Joueur> joueursClassement = new ArrayList<>(Arrays.asList(joueurs));
                         
        joueursClassement.sort(comparatorJoueur);  
        return joueursClassement;
    }

    /* renvoie la liste des nouveaux pingouins bloques apres placement ou deplacement d'un pingouin (methode appelee par placerPingouin et 
       deplacerPingouin) */
    private ArrayList<Point> finTour(int ligneDest, int colonneDest, Moteur moteurAvantCoup)
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
        dernierCoupJoue.nouveauxPingouinsBloques = nouveauxPingouinsBloques; 
        if(sauvegarderCoupJoues)
        {
            coupJoues.push(moteurAvantCoup);
            coupAnnules.clear();
        }        
        /*suivant = null;
        moteurAvantCoup.suivant = this.clone();        
        precedent = moteurAvantCoup; 
        moteurAvantCoup.suivant.precedent = moteurAvantCoup;
        if(precedent.precedent != null)
        {
            System.out.println(precedent.precedent.suivant.suivant);
        }*/
        joueurSuivant();
        return nouveauxPingouinsBloques;
    }    

    public void sauvegarderDebutPartie()
    {
        sauvegardeDebutPartie = this.clone();
        sauvegardeDebutPartie.coupAnnules = new Stack<>();
        sauvegardeDebutPartie.coupJoues = new Stack<>();
    }
    
    public int nbTuilesLibres()
    {
        int nbCasesLibres = 0;
        for(int i = 0 ; i < plateau.plateau.length ; i++)
        {
            for(int j = 0 ; j < Plateau.nbTuilesLigne(i) ; j++)
            {
                if(plateau.plateau[i][j].peutDeplacerPingouin())
                {
                    nbCasesLibres++;
                }
            }
        }
        return nbCasesLibres;
    }
    
    @Override
    public Moteur clone()
    {
        try {
            //Moteur ancienneSauvegardeDebutPartie = sauvegardeDebutPartie;
            /*Moteur ancienSuivant = suivant;
            Moteur ancienPrecedent = precedent;
            suivant = null;
            precedent = null;*/
            Moteur moteurClone = (Moteur)super.clone();
            /*suivant = ancienSuivant;
            precedent = ancienPrecedent;*/
            moteurClone.plateau = plateau.clone();
            moteurClone.joueurs = moteurClone.joueurs.clone();
            if(moteurClone.dernierCoupJoue != null)
            {
                moteurClone.dernierCoupJoue = moteurClone.dernierCoupJoue.clone();
            }
            for(int i = 0 ; i < moteurClone.joueurs.length ; i++)
            {
                moteurClone.joueurs[i] = moteurClone.joueurs[i].clone();
                moteurClone.joueurs[i].moteur = moteurClone;
                for(Pingouin pingouin : moteurClone.joueurs[i].pingouins)
                {
                    moteurClone.plateau.plateau[pingouin.ligne][pingouin.colonne].ajouterPingouin(pingouin);
                }
            }
            //sauvegardeDebutPartie = ancienneSauvegardeDebutPartie;
            return moteurClone;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
