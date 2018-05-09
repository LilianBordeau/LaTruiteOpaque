package modele;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Joueur implements Serializable
{
    public String nom;
    public int numero;
    public Moteur moteur;
    public int scorePoisson;
    public int scoreTuile;
    public int nbPingouinsAPlacer;
    public ArrayList<Pingouin> pingouins;
    
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
    
}
