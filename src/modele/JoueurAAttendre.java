package modele;

import java.io.Serializable;

public abstract class JoueurAAttendre extends Joueur implements Serializable
{
    public abstract Point getPlacementSuivant();    
    public abstract Deplacement getDeplacementSuivant();
}
