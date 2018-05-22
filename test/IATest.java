import modele.Deplacement;
import modele.JoueurAAttendre;
import modele.JoueurIAAleatoire;
import modele.JoueurIADifficile;
import modele.JoueurIAMoyenne;
import modele.Moteur;
import modele.Point;
import org.junit.Test;
import static org.junit.Assert.*;
import vue.Navigation;

public class IATest
{
    @Test
    public void iaTest()
    {
        Navigation.estEnModeDebug = true;
        // test que les IA ne joue que des coups valides
        duelIA(JoueurIAAleatoire.class, JoueurIAAleatoire.class);
        duelIA(JoueurIAAleatoire.class, JoueurIAAleatoire.class, JoueurIAAleatoire.class);
        duelIA(JoueurIAAleatoire.class, JoueurIAAleatoire.class, JoueurIAAleatoire.class, JoueurIAAleatoire.class);
        duelIA(JoueurIAMoyenne.class, JoueurIAMoyenne.class);
        duelIA(JoueurIAMoyenne.class, JoueurIAMoyenne.class, JoueurIAMoyenne.class);
        duelIA(JoueurIAMoyenne.class, JoueurIAMoyenne.class, JoueurIAMoyenne.class, JoueurIAMoyenne.class);
        duelIA(JoueurIADifficile.class, JoueurIADifficile.class);
        duelIA(JoueurIADifficile.class, JoueurIADifficile.class, JoueurIADifficile.class);
        duelIA(JoueurIADifficile.class, JoueurIADifficile.class, JoueurIADifficile.class, JoueurIADifficile.class);
        Navigation.estEnModeDebug = false;
    }
    
    private void duelIA(Class<? extends JoueurAAttendre>... classesIA)
    {
        try
        {
            JoueurAAttendre[] joueurs = new JoueurAAttendre[classesIA.length];
            for(int i = 0 ; i < joueurs.length ; i++)
            {
                joueurs[i] = classesIA[i].newInstance();
            }
            Moteur moteur = new Moteur(joueurs);
            int nbCoups = 0;
            while(!moteur.estPartieTerminee())
            {
                assertTrue(nbCoups<=70); // pour eviter de boucler indefinimment si moteur.estPartieTerminee() ne renvoie pas true quand la partie est terminee
                JoueurAAttendre joueurCourant = joueurs[moteur.joueurCourant];
                joueurCourant.moteur = moteur.clone();
                joueurCourant.moteur.sauvegarderCoupJoues = false;
                if(!moteur.pingouinsPlaces())
                {
                    Point placement = joueurCourant.getPlacementSuivant();
                    assertNotNull(moteur.placerPingouin(placement.ligne, placement.colonne));                    
                }
                else
                {
                    Deplacement deplacement = joueurCourant.getDeplacementSuivant();
                    assertNotNull(moteur.deplacerPingouin(deplacement.ligneSrc, deplacement.colonneSrc, deplacement.ligneDest, deplacement.colonneDest)); 
                }
                nbCoups++;
            }
        }
        catch(InstantiationException|IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
    
}
