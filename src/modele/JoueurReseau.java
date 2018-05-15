package modele;

import java.io.Serializable;
import reseau.Connexion;

public abstract class JoueurReseau extends JoueurAAttendre implements Serializable
{
    @Override
    public Point getPlacementSuivant()
    {
        return (Point)connexion.readObject();
    }

    @Override
    public Deplacement getDeplacementSuivant()
    {
        return (Deplacement)connexion.readObject();
    }
    
}
