package modele;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Coup<T> implements Serializable,Cloneable
{
    public ArrayList<Point> nouveauxPingouinsBloques;  
    T gain; // le gain peut etre de plusieurs types differents : un ReelEtendu (un reel, - l'infini ou + l'infini), un Integer, un Double...
    
    @Override
    public Coup<T> clone()
    {
        try {
            Coup<T> coupClone = (Coup<T>)super.clone();
            coupClone.nouveauxPingouinsBloques = (ArrayList<Point>)coupClone.nouveauxPingouinsBloques.clone();
            for(int i = 0 ; i < coupClone.nouveauxPingouinsBloques.size() ; i++)
            {
                coupClone.nouveauxPingouinsBloques.set(i, coupClone.nouveauxPingouinsBloques.get(i).clone());
            }
            return coupClone;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
