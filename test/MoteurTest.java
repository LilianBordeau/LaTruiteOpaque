import java.util.ArrayList;
import java.util.Arrays;
import modele.Joueur;
import modele.JoueurHumain;
import modele.Moteur;
import modele.Pingouin;
import modele.Plateau;
import modele.Point;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class MoteurTest
{    
    Moteur moteur;
    
    @Before
    public void setUp()
    {
        moteur = new Moteur(new Joueur[]{new JoueurHumain(), new JoueurHumain()});        
    }

    @Test
    public void taillePlateauTest()
    {
        assertEquals(8,moteur.plateau.plateau.length);
        assertEquals(8,moteur.plateau.plateau[0].length);
        for(int i = 0 ; i < moteur.plateau.plateau.length ; i++)
        {
            for(int j = 0 ; j < moteur.plateau.plateau.length ; j++)
            {
                if(j==7 && (i%2)==0)
                {
                    assertNull(moteur.plateau.plateau[i][j]);
                }
                else
                {                    
                    assertNotNull(moteur.plateau.plateau[i][j]);
                }
            }
        }
    }
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void nombreDeJoueurValide1Test()
    {
        thrown.expect(Throwable.class);
        moteur = new Moteur(new Joueur[]{new JoueurHumain()});
    }
    
    @Test
    public void nombreDeJoueurValide5Test()
    {
        thrown.expect(Throwable.class);
        moteur = new Moteur(new Joueur[]{new JoueurHumain(), new JoueurHumain(), new JoueurHumain(), new JoueurHumain(), new JoueurHumain()});
    }
    
    @Test
    public void nombreDeJoueurValide234Test()
    {
        // teste qu'aucune exception n'est levee pour un nombre valide de joueurs (de 2 a 4)
        moteur = new Moteur(new Joueur[]{new JoueurHumain(), new JoueurHumain()});
        moteur = new Moteur(new Joueur[]{new JoueurHumain(), new JoueurHumain(), new JoueurHumain()});
        moteur = new Moteur(new Joueur[]{new JoueurHumain(), new JoueurHumain(), new JoueurHumain()});
        moteur = new Moteur(new Joueur[]{new JoueurHumain(), new JoueurHumain(), new JoueurHumain(), new JoueurHumain()});
    }
    
    @Test
    public void partieCompleteTest()
    {        
        for(int i = 0 ; i < moteur.plateau.plateau.length ; i++)
        {
            for(int j = 0 ; j < Plateau.nbTuilesLigne(i) ; j++)
            {
                moteur.plateau.plateau[i][j].nbPoissons = 1;
            }
        }        
        moteur.plateau.plateau[6][6].nbPoissons = 3;           
        moteur.plateau.plateau[7][7].nbPoissons = 2;
        moteur.plateau.plateau[3][1].nbPoissons = 2;
        moteur.plateau.plateau[1][3].nbPoissons = 3;
        for(Joueur joueur : moteur.joueurs)
        {
            assertEquals(0,joueur.scorePoisson);
            assertEquals(0,joueur.scoreTuile);
        }        
        assertNull(moteur.placerPingouin(6,6));     
        assertNull(moteur.placerPingouin(7,7));
        ArrayList<Point> nouveauxPingouinsBloques = moteur.placerPingouin(0, 0);
        assertNotNull(nouveauxPingouinsBloques);
        assertTrue(nouveauxPingouinsBloques.isEmpty());
        moteur.placerPingouin(0, 1);
        moteur.placerPingouin(1, 0);
        nouveauxPingouinsBloques = moteur.placerPingouin(1, 1);
        assertEquals(new ArrayList<>(Arrays.asList(new Point[]{new Point(0,0)})), nouveauxPingouinsBloques);
        assertEquals(1,moteur.joueurs[0].scorePoisson);
        assertEquals(1,moteur.joueurs[0].scoreTuile);
        assertEquals(0,moteur.joueurs[1].scorePoisson);
        assertEquals(0,moteur.joueurs[1].scoreTuile);
        assertNotNull(moteur.placerPingouin(0, 2));
        assertNotNull(moteur.placerPingouin(1, 2));        
        assertNotNull(moteur.placerPingouin(2, 1));
        assertNotNull(moteur.placerPingouin(2, 0));
        assertNull(moteur.placerPingouin(0, 6));
        
        assertNull(moteur.deplacerPingouin(2,0,3,1));
        assertNotNull(moteur.deplacerPingouin(0,2,1,3));
        
        ArrayList<Point> casesAdjLibres2_0 = moteur.casesAdjacentesLibres(new Pingouin(2,0,0));
        assertEquals(2,casesAdjLibres2_0.size());
        assertTrue(casesAdjLibres2_0.contains(new Point(3,0)));
        assertTrue(casesAdjLibres2_0.contains(new Point(3,1)));
        ArrayList<Point> casesAdj2_0 = moteur.casesAdjacentes(new Pingouin(2,0,0));
        assertEquals(5,casesAdj2_0.size());
        assertTrue(casesAdj2_0.contains(new Point(3,0)));
        assertTrue(casesAdj2_0.contains(new Point(3,1)));
        assertTrue(casesAdj2_0.contains(new Point(1,0)));
        assertTrue(casesAdj2_0.contains(new Point(1,1)));
        assertTrue(casesAdj2_0.contains(new Point(2,1)));
        ArrayList<Point> deplPossible2_0 = moteur.deplacementsPossibles(2,0);
        assertEquals(6,deplPossible2_0.size());
        assertTrue(deplPossible2_0.contains(new Point(3,0)));
        assertTrue(deplPossible2_0.contains(new Point(3,1)));
        assertTrue(deplPossible2_0.contains(new Point(4,1)));
        assertTrue(deplPossible2_0.contains(new Point(5,2)));
        assertTrue(deplPossible2_0.contains(new Point(6,2)));
        assertTrue(deplPossible2_0.contains(new Point(7,3)));
        
        assertNotNull(moteur.deplacerPingouin(2,0,3,1)); // joueur 1
        assertNotNull(moteur.deplacerPingouin(1,3,2,3));
        assertEquals(6, moteur.joueurs[0].scorePoisson);
        assertEquals(4, moteur.joueurs[0].scoreTuile);
        assertNotNull(moteur.deplacerPingouin(3,1,4,0)); // joueur 1
        assertEquals(5, moteur.joueurs[1].scorePoisson);
        assertEquals(4, moteur.joueurs[1].scoreTuile);
        assertNotNull(moteur.deplacerPingouin(2,3,3,3));
        nouveauxPingouinsBloques = moteur.deplacerPingouin(4,0,3,0);  // joueur 1
        assertNotNull(nouveauxPingouinsBloques);
        assertNotNull(moteur.deplacerPingouin(3,3,3,2));
        assertNotNull(moteur.deplacerPingouin(1,2,2,2));  // joueur 1
        assertNotNull(moteur.deplacerPingouin(3,2,4,1));
        assertNotNull(moteur.deplacerPingouin(4,1,5,1));
        assertNotNull(moteur.deplacerPingouin(5,1,6,0));
        assertFalse(moteur.estPartieTerminee());
        assertNotNull(moteur.deplacerPingouin(6,0,5,0));
        assertTrue(moteur.estPartieTerminee());
        assertEquals(14, moteur.joueurs[0].scorePoisson);
        assertEquals(12, moteur.joueurs[0].scoreTuile);
        ArrayList<Joueur> gagnants = moteur.joueursGagnants();
        assertEquals(1,gagnants.size());
        assertEquals(0,gagnants.get(0).numero);
        ArrayList<Joueur> classement = moteur.getClassement();
        assertEquals(2,classement.size());
        assertEquals(0,classement.get(0).numero);
        assertEquals(1,classement.get(1).numero);
    }
    
    @Test
    public void classementEtDeterminationGagnantsTest()
    {             
        moteur.setJoueurs(new Joueur[]{new JoueurHumain(), new JoueurHumain(), new JoueurHumain(), new JoueurHumain()});
        for(Joueur joueur : moteur.joueurs)
        {
            joueur.scorePoisson = 10;
            joueur.scoreTuile = 9;
        }
        assertEquals(4,moteur.joueursGagnants().size());
        moteur.joueurs[0].scorePoisson = 8;
        moteur.joueurs[0].scoreTuile = 8;
        moteur.joueurs[1].scorePoisson = 7;
        moteur.joueurs[1].scoreTuile = 7;
        ArrayList<Joueur> gagnants = moteur.joueursGagnants();
        assertEquals(2,gagnants.size());
        assertTrue(gagnants.get(0).numero == 2 || gagnants.get(0).numero == 3);
        assertTrue(gagnants.get(1).numero == 2 || gagnants.get(1).numero == 3);
        ArrayList<Joueur> classement = moteur.getClassement();
        assertTrue(classement.get(0).numero == 2 || classement.get(0).numero == 3);
        assertTrue(classement.get(1).numero == 2 || classement.get(1).numero == 3);
        assertEquals(0,classement.get(2).numero);
        assertEquals(1,classement.get(3).numero);
        moteur.joueurs[3].scoreTuile = 8;
        gagnants = moteur.joueursGagnants();
        assertEquals(1,gagnants.size());
        assertEquals(2,gagnants.get(0).numero);
        classement = moteur.getClassement();
        assertEquals(2,classement.get(0).numero);
        assertEquals(3,classement.get(1).numero);
        assertEquals(0,classement.get(2).numero);
        assertEquals(1,classement.get(3).numero);
    }
    
}
