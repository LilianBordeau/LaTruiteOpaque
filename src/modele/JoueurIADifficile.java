package modele;

import java.io.Serializable;
import java.util.ArrayList;
import thread.MyThread;
import util.Couple;
import util.ReelEtendu;

public class JoueurIADifficile extends JoueurAAttendre implements Serializable,Cloneable
{
    private static final int DUREEMINTOURMILLIS = 500;

    @Override
    public Point getPlacementSuivant()
    {		
        Point placementMax = placementMax = choixPlacement();
        MyThread.sleep(DUREEMINTOURMILLIS);
        return placementMax;
    }

    @Override
    public Deplacement getDeplacementSuivant()
    {
        int profondeur = 2;  
        int nbCaseLibres = moteur.nbTuilesLibres();
        if (nbCaseLibres <= 13)
        {
            profondeur = 13;
        }
        else if (nbCaseLibres <= 20)
        {
            profondeur = 8;
        }
        else if (nbCaseLibres <= 30)
        {
            profondeur = 6;
        }
        else
        {
            profondeur = 5;
        }        
        System.out.println("profondeur choisie : "+profondeur);
        ArrayList<Pingouin> pingouinsNonIsol = pingouinsNonIsoles();
        if(pingouinsNonIsol.isEmpty())
        {
            System.out.println("recherche de chemin hamiltonien");
            /* solution approchee : change l'heuristique en la somme de la valeur des cases sur au moins une des composantes connexe contenant 
            au moins 1 des pingouins du joueur */
            Deplacement cheminMax = negamax(ReelEtendu.moinsLInfini(), ReelEtendu.plusLInfini(), profondeur-2, false, true);
            MyThread.sleep(DUREEMINTOURMILLIS);
            return cheminMax;
        }
        else
        {               
            nbAppels = 0;            
            Deplacement<ReelEtendu> deplacementMax = negamax(ReelEtendu.moinsLInfini(), ReelEtendu.plusLInfini(), 3, false, false);
            //System.out.println(nbAppels+" appel(s)");
            if(deplacementMax.gain.compareTo(new ReelEtendu(POINTSDESGAGNANTS/4)) >= 0 )
            {
                //System.out.println("coup GAGNANT");
            }
            else if(deplacementMax.gain.compareTo(new ReelEtendu(-POINTSDESGAGNANTS/4)) <= 0 )
            {
                System.out.println("recherche d'un coup non perdant");
                deplacementMax = negamax(ReelEtendu.moinsLInfini(), ReelEtendu.plusLInfini(), profondeur, true, false);
                if(deplacementMax.gain.compareTo(new ReelEtendu(-POINTSDESGAGNANTS/4)) <= 0)
                {                
                    System.out.println("va FORCEMENT PERDRE");
                }
            }
            if(deplacementMax.gain.compareTo(new ReelEtendu(-POINTSDESGAGNANTS/4)) > 0 && deplacementMax.gain.compareTo(new ReelEtendu(POINTSDESGAGNANTS/4)) <= 0)
            {
                //System.out.println("coup NON PERDANT : "+deplacementMax.gain);
            }
            MyThread.sleep(DUREEMINTOURMILLIS);
            return deplacementMax;
        }
    }
    
    @Override
    public JoueurIADifficile clone()
    {
        return (JoueurIADifficile)super.clone();
    }       
}
