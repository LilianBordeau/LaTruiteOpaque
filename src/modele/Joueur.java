package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import reseau.Connexion;
import thread.MyThread;
import util.Couple;
import util.ReelEtendu;

public abstract class Joueur implements Serializable,Cloneable
{
    public String nom;
    public int couleur;
    public int numero;
    public transient Moteur moteur;
    public int scorePoisson;
    public int scoreTuile;
    public int nbPingouinsAPlacer;
    public ArrayList<Pingouin> pingouins;
    public transient Connexion connexion;
    
    public Joueur()
    {
        scorePoisson = 0;
        scoreTuile = 0;
        pingouins = new ArrayList<>();
    }

    void ajouterPingouin(Pingouin pingouin)
    {
        pingouins.add(pingouin);
    }
    
    public boolean estBloque()
    {
        if(pingouins.size() < nbPingouinsAPlacer)
        {
            return false;
        }
        for(Pingouin pingouin : pingouins)
        {
            if(!pingouin.estBloque)
            {
                return false;
            }
        }
        return true;
    }
    
    /* choisi la premiere case telle que l'heuristique décrite ci dessous est maximale
    heuristique pour une case c : -1 s'il n'y a aucun deplacement possible depuis c,
    sinon, pour chaque direction (GAUCHE,DROITE, BAS GAUCHE, HAUT GAUCHE, BAS DROITE ou HAUT DROITE);
    ajouter le nombre de poissons de la case ayant le plus de poisson (parmi les cases avec 2 ou 3 poissons) accessible depuis c en se deplacant dans cette direction sans passer 
    par une case avec 1 seul poisson ou 0 s'il n'existe pas de telle case (en effet, ces cases risque d'etre occupee aux tours suivants par un nouveau pingouin place et d'empecher 
    d'atteindre la case avec plusieurs poisson)*/
    public Point<Integer> choixPlacement()
    {		
        Case[][] plateau = moteur.plateau.plateau;
        Point<Integer> placementMax = new Point<>(-1,-1,-2); // le max est initialise a -2 car l'heuristique est superieure ou egale a -1 
        for(int i = 0 ; i < plateau.length ; i++)
        {
            for(int j = 0 ; j < Plateau.nbTuilesLigne(i) ; j++)
            {
                if(plateau[i][j].peutPlacerPingouin())
                {
                    plateau[i][j].pingouin = new Pingouin(i,j, moteur.joueurCourant);
                    Couple<ArrayList<Point>, ArrayList<Integer>> deplEtDirections = moteur.deplacementsPossiblesEtDirection(i, j);
                    Point<Integer> placementCourant = new Point(i,j,-1);
                    int indicePrecedent = 0;
                    for(int k : deplEtDirections.second)
                    {
                        if(placementCourant.gain < 0)
                        {
                            placementCourant.gain = 0;
                        }
                        int gainDirectionCourante = 0;
                        for(int m = indicePrecedent ; m < k && !plateau[deplEtDirections.premier.get(m).ligne][deplEtDirections.premier.get(m).colonne].estOccupee() && plateau[deplEtDirections.premier.get(m).ligne][deplEtDirections.premier.get(m).colonne].nbPoissons >= 2 ; m++)
                        {
                            Point deplCourant = deplEtDirections.premier.get(m);
                            Case caseCourante = plateau[deplCourant.ligne][deplCourant.colonne];
                            if(caseCourante.nbPoissons > gainDirectionCourante)
                            {
                                gainDirectionCourante = caseCourante.nbPoissons;
                            }
                        }
                        indicePrecedent = k;
                        placementCourant.gain += gainDirectionCourante;                        
                    }
                    if(placementCourant.gain > placementMax.gain)
                    {
                        placementMax = placementCourant;
                    }
                    plateau[i][j].pingouin = null;
                }                
            }
        }
        return placementMax;
    }
    
    /* renvoie le premier deplacement tel que le nombre de poisson sur la case d'arrivee du pingouin est maximal */
    public Deplacement<Integer> choixDeplacement()
    {
        Deplacement<Integer> deplacementMax = new Deplacement<>(-1,-1,-1,-1);
        deplacementMax.gain = 0;
        for(Pingouin pingouin : moteur.joueurs[moteur.joueurCourant].pingouins)
        {
            ArrayList<Point> casesAdj = moteur.deplacementsPossibles(pingouin.ligne, pingouin.colonne);
            for(Point point : casesAdj)
            {
                Case caseCourante = moteur.plateau.plateau[point.ligne][point.colonne];
                Deplacement<Integer> deplacementCourant = new Deplacement<>(pingouin.ligne, pingouin.colonne, point.ligne,point.colonne);
                deplacementCourant.gain = caseCourante.nbPoissons;
                if(deplacementCourant.gain > deplacementMax.gain)
                {
                    deplacementMax = deplacementCourant;
                }
            }
        }
        return deplacementMax;
    }
    
    
    /*public Point<ReelEtendu> enumPlacement()
    {        
        int nbTuiles1Poisson = moteur.nbCases1Poissons();
        int nbPoissonsParJoueur = moteur.nbPingouinsTotal()/moteur.joueurs.length;
        return enumPlacementWorker(new int[nbPoissonsParJoueur], 0, new int[moteur.nbPingouinsTotal()-nbPoissonsParJoueur], nbTuiles1Poisson);
    }*/
    
    private static int score(int nbPoissons, int nbTuiles)
    {
        /* le gagnant est celui qui a le plus de tuile ou le plus de poissons parmi ceux qui ont le nombre maximal
        de tuile en cas d'egalite (avec cette formule 1 poisson apporte plus de points (1000) que le nombre maximal de tuile (60)) */
        return 3*4*(1000*nbPoissons+nbTuiles);
    }
    
    public static int nbAppels = 0;
    public static int POINTSDESGAGNANTS = 1000000*3*4;
    
    public Deplacement<ReelEtendu> negamax(ReelEtendu alpha, ReelEtendu beta, int profondeur, boolean coupNonPerdant, boolean pingouinsIsoles) 
    {    
            nbAppels++;
            Deplacement<ReelEtendu> coupGainMax = new Deplacement<>(-1,-1,-1,-1);          
            if(moteur.estPartieTerminee() || profondeur == 0)
            {                
                coupGainMax.gain = heuristique(coupNonPerdant, pingouinsIsoles);
            }
            else
            {
                coupGainMax.gain = ReelEtendu.moinsLInfini();
                boolean continuer = true;
                for (Pingouin pingouin : moteur.joueurs[moteur.joueurCourant].pingouins)
                {
                        ArrayList<Point> deplPossibles = moteur.deplacementsPossibles(pingouin.ligne, pingouin.colonne);
                        for(Point dest : deplPossibles)
                        {
                                Moteur sauvegardeMoteur = moteur;
                                moteur = moteur.clone();
                                int joueurPrecedent = moteur.joueurCourant;
                                moteur.deplacerPingouin(pingouin.ligne, pingouin.colonne, dest.ligne, dest.colonne);
                                boolean changeTypeNoeud = !coupNonPerdant && ((joueurPrecedent == numero && moteur.joueurCourant != numero) || (joueurPrecedent != numero && moteur.joueurCourant == numero));
                                ReelEtendu alphaAppel = alpha;
                                ReelEtendu betaAppel = beta;
                                if(changeTypeNoeud)
                                {
                                    alphaAppel = beta.oppose();
                                    betaAppel = alpha.oppose();
                                }
                                Deplacement<ReelEtendu> coupExamine = negamax(alphaAppel, betaAppel, profondeur-1, coupNonPerdant, pingouinsIsoles);                                
                                ReelEtendu gain = coupExamine.gain;
                                if(changeTypeNoeud)
                                {
                                    gain = gain.oppose();
                                }
                                coupExamine = new Deplacement<>(pingouin.ligne, pingouin.colonne, dest.ligne, dest.colonne);
                                coupExamine.gain = gain;
                                if(coupGainMax.ligneSrc == -1 || coupExamine.gain.compareTo(coupGainMax.gain) > 0)
                                {
                                    coupGainMax = new Deplacement<>(coupExamine.ligneSrc, coupExamine.colonneSrc, coupExamine.ligneDest, coupExamine.colonneDest);
                                    coupGainMax.gain = coupExamine.gain;
                                    alpha = ReelEtendu.max(alpha, coupGainMax.gain);
                                    if((!coupNonPerdant && alpha.compareTo(beta)>=0 || coupGainMax.gain.compareTo(beta)>0)
                                        || (coupNonPerdant && coupGainMax.gain.compareTo(new ReelEtendu(POINTSDESGAGNANTS/4))>=0))
                                    {    
                                        continuer = false;
                                        moteur = sauvegardeMoteur;
                                        break;
                                    }
                                }
                                moteur = sauvegardeMoteur;
                        }
                        if(!continuer)
                        {
                            break;
                        }
                }
            }
            // pour afficher l'arbre d'appel ( a garder)
            /*System.out.print(profondeur+":");
            for(int n = 0 ; n < profondeur ; n++)
            {
                System.out.print("    ");
            }
            System.out.println("negamax("+moteur.joueurCourant+","+Arrays.asList(etatJeu.deplacements)+")=("+coupGainMax.ligne+","+coupGainMax.colonne+","+coupGainMax.gain+")");*/
            return coupGainMax;
    }
    
    public ReelEtendu heuristique(boolean coupNonPerdant, boolean pingouinsIsoles)
    {        
        int heur = 0;
        int joueurCour = coupNonPerdant ? numero : moteur.joueurCourant;
        if(moteur.estPartieTerminee() && !pingouinsIsoles)
        {
            ArrayList<Joueur> gagnants = moteur.joueursGagnants();
            if(moteur.joueurs.length!=gagnants.size())
            {
                heur = -POINTSDESGAGNANTS/(moteur.joueurs.length-gagnants.size());
                for(Joueur joueur : gagnants)
                {
                    if(joueur.numero == joueurCour)
                    {
                        heur = POINTSDESGAGNANTS/(gagnants.size());
                    }
                }
            }                
        }
        else
        {
            int gainJoueurCourant = gain(moteur.joueurs[joueurCour]);
            for(Joueur joueur : moteur.joueurs)
            {
                if(joueur.numero != joueurCour)
                {
                    heur += gainJoueurCourant-gain(joueur);
                }
            }
        }        
        return new ReelEtendu(heur);
    }

    private int gain(Joueur joueur)
    {
        int heur = score(joueur.scorePoisson, joueur.scoreTuile);
        boolean[][] sommetsVisites = new boolean[moteur.plateau.plateau.length][moteur.plateau.plateau.length];
        for(Pingouin pingouin : joueur.pingouins)
        {     
            HashMap<Integer, Boolean> autresJoueursComposante = new HashMap<>();
            Case casePingouin = moteur.plateau.plateau[pingouin.ligne][pingouin.colonne];             
            heur += score(casePingouin.nbPoissons, 1);
                Stack<Point> pileSommets = new Stack<>();
                pileSommets.push(new Point(pingouin.ligne,pingouin.colonne));
                sommetsVisites[pingouin.ligne][pingouin.colonne] = true;
                boolean continuer = true;
                int gainComposante = 0;
                while(!pileSommets.isEmpty() && continuer)
                {
                    Point sommetCourant = pileSommets.pop();
                    ArrayList<Point> successeurs = moteur.casesAdjacentes(new Pingouin(sommetCourant.ligne, sommetCourant.colonne, joueur.numero));
                    for(Point successeur : successeurs)
                    {
                        Case caseCourante = moteur.plateau.plateau[successeur.ligne][successeur.colonne];
                        if(!sommetsVisites[successeur.ligne][successeur.colonne])
                        {
                            sommetsVisites[successeur.ligne][successeur.colonne] = true;
                            if(caseCourante.peutDeplacerPingouin())
                            {
                                gainComposante += score(caseCourante.nbPoissons,1);
                                pileSommets.push(successeur);
                            }
                            else if(caseCourante.estOccupee() && !caseCourante.pingouin.estBloque && !caseCourante.contientPingouin(joueur.numero))
                            {
                                autresJoueursComposante.put(caseCourante.numJoueurPingouin(), true);
                                /*continuer = false;
                                gainComposante = 0;
                                break;*/
                            }
                        }
                    }
                }
                heur += gainComposante/(autresJoueursComposante.size()+1);
        }
        /*sommetsVisites = new boolean[moteur.plateau.plateau.length][moteur.plateau.plateau.length];
        for(Joueur ennemi : moteur.joueurs)
        {
            if(ennemi.numero != joueur.numero)
            {
                for(Pingouin pingouin : joueur.pingouins)
                {                       
                        Case casePingouin = moteur.plateau.plateau[pingouin.ligne][pingouin.colonne];
                        Stack<Point> pileSommets = new Stack<>();
                        pileSommets.push(new Point(pingouin.ligne,pingouin.colonne));
                        sommetsVisites[pingouin.ligne][pingouin.colonne] = true;
                        boolean continuer = true;
                        int gainComposante = 0;
                        while(!pileSommets.isEmpty() && continuer)
                        {
                            Point sommetCourant = pileSommets.pop();
                            ArrayList<Point> successeurs = moteur.casesAdjacentes(new Pingouin(sommetCourant.ligne, sommetCourant.colonne, joueur.numero));
                            for(Point successeur : successeurs)
                            {
                                Case caseCourante = moteur.plateau.plateau[successeur.ligne][successeur.colonne];
                                if(!sommetsVisites[successeur.ligne][successeur.colonne])
                                {
                                    sommetsVisites[successeur.ligne][successeur.colonne] = true;
                                    if(caseCourante.peutDeplacerPingouin())
                                    {
                                        gainComposante += score(caseCourante.nbPoissons,1);
                                        pileSommets.push(successeur);
                                    }
                                    else if(caseCourante.estOccupee() && caseCourante.contientPingouin(joueur.numero))
                                    {
                                        continuer = false;
                                        gainComposante = 0;
                                        break;
                                    }
                                }
                            }
                        }
                        heur -= gainComposante;
                }
            }
        }        */
        return heur;
    }
    
    ArrayList<Pingouin> pingouinsNonIsoles()
    {
        ArrayList<Pingouin> isoles = new ArrayList<>();
        Joueur joueur = moteur.joueurs[moteur.joueurCourant];
        for(Pingouin pingouin : joueur.pingouins)
        {                          
            if(!pingouin.estBloque)
            {
                boolean[][] sommetsVisites = new boolean[moteur.plateau.plateau.length][moteur.plateau.plateau.length];
                Stack<Point> pileSommets = new Stack<>();
                pileSommets.push(new Point(pingouin.ligne,pingouin.colonne));
                sommetsVisites[pingouin.ligne][pingouin.colonne] = true;
                boolean continuer = true;
                while(!pileSommets.isEmpty() && continuer)
                {
                    Point sommetCourant = pileSommets.pop();
                    ArrayList<Point> successeurs = moteur.casesAdjacentes(new Pingouin(sommetCourant.ligne, sommetCourant.colonne, joueur.numero));
                    for(Point successeur : successeurs)
                    {
                        Case caseCourante = moteur.plateau.plateau[successeur.ligne][successeur.colonne];
                        if(!sommetsVisites[successeur.ligne][successeur.colonne])
                        {
                            sommetsVisites[successeur.ligne][successeur.colonne] = true;
                            if(caseCourante.peutDeplacerPingouin())
                            {
                                pileSommets.push(successeur);
                            }
                            else if(caseCourante.estOccupee() && !caseCourante.contientPingouin(joueur.numero))
                            {
                                continuer = false;
                                break;
                            }
                        }
                    }
                }
                if(!continuer)
                {
                    isoles.add(pingouin);
                }
            }            
        }
        return isoles;
    }
    
    @Override
    public Joueur clone()
    {
        try {
            Moteur sauvegardeMoteur = moteur;
            moteur = null;
            Joueur joueurClone = (Joueur)super.clone();
            moteur = sauvegardeMoteur;
            joueurClone.pingouins = (ArrayList<Pingouin>)joueurClone.pingouins.clone();
            for(int i = 0 ; i < joueurClone.pingouins.size() ; i++)
            {
                joueurClone.pingouins.set(i, joueurClone.pingouins.get(i).clone());
            }
            return joueurClone;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }   
    
}
