package modele;

import java.io.Serializable;

public class Deplacement<T> extends Coup<T> implements Serializable,Cloneable
{
    public int ligneSrc;
    public int colonneSrc;
    public int ligneDest;
    public int colonneDest;
    public int nbPoissonsCaseSrc;
    
    public Deplacement(int ligneSrc, int colonneSrc, int ligneDest, int colonneDest)
    {
    	this.ligneSrc = ligneSrc;
	this.colonneSrc = colonneSrc;
	this.ligneDest = ligneDest;
	this.colonneDest = colonneDest;
    }
    
    public Deplacement(int ligneSrc, int colonneSrc, int ligneDest, int colonneDest, int nbPoissonsCaseSrc)
    {
    	this(ligneSrc, colonneSrc, ligneDest, colonneDest);
	this.nbPoissonsCaseSrc = nbPoissonsCaseSrc;
    }
    
    @Override
    public Deplacement<T> clone()
    {
        return (Deplacement<T>)super.clone();
    }
    
    @Override
    public String toString()
    {
        return "("+ligneSrc+","+colonneSrc+","+ligneDest+","+colonneDest+")";
    }
    
}
