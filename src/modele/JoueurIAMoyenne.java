package modele;

import java.io.Serializable;
import java.util.ArrayList;
import thread.MyThread;
import util.Couple;

public class JoueurIAMoyenne extends JoueurAAttendre implements Serializable,Cloneable
{
    private static final int DUREEMINTOURMILLIS = 500;
    
    @Override
    public Point getPlacementSuivant()
    {		
        Point placementMax = choixPlacement();
        MyThread.sleep(DUREEMINTOURMILLIS);
        return placementMax;
    }

    @Override
    public Deplacement getDeplacementSuivant()
    {
        Deplacement deplacementMax = choixDeplacement();
        MyThread.sleep(DUREEMINTOURMILLIS);
        return deplacementMax;
    }
    
    @Override
    public JoueurIAMoyenne clone()
    {
        return (JoueurIAMoyenne)super.clone();
    }
}
