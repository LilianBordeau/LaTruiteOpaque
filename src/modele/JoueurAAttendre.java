package modele;

import java.io.Serializable;
import java.util.Random;

public abstract class JoueurAAttendre extends Joueur implements Serializable,Cloneable
{
    public abstract Point getPlacementSuivant();    
    public abstract Deplacement getDeplacementSuivant();    
    
    // un generateur de nombres aleatoires ne doit pas etre serialise car sinon il produira les memes valeurs apres deserialisation
    protected transient Random random;
    
    public JoueurAAttendre()
    {
        initRandomSiNull();
    }
    
    protected void initRandomSiNull()
    {
        if(random == null)
        {
            random = new Random();
        }
    }   
    
    @Override
    public JoueurAAttendre clone()
    {
        JoueurAAttendre joueurClone = (JoueurAAttendre)super.clone();
        joueurClone.random = new Random(); // // un generateur de nombres aleatoires ne doit pas etre clone car sinon il produira les memes valeurs le generateur original
        return joueurClone;
    }
}
