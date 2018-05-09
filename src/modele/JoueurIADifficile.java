package modele;

import java.io.Serializable;

public class JoueurIADifficile extends JoueurAAttendre implements Serializable
{
    @Override
    public Point getPlacementSuivant()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Deplacement getDeplacementSuivant()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }  
}
