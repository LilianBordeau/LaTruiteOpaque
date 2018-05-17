package modele;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import reseau.Connexion;
import reseau.UnverifiedIOException;

public abstract class JoueurReseau extends JoueurAAttendre implements Serializable
{
    @Override
    public Point getPlacementSuivant()
    {
        try {
            return (Point)connexion.readObject();
        } catch (IOException ex) {
            throw new UnverifiedIOException(ex);
        }
    }

    @Override
    public Deplacement getDeplacementSuivant()
    {
        try {
            return (Deplacement)connexion.readObject();
        } catch (IOException ex) {
            throw new UnverifiedIOException(ex);
        }
    }
    
}
