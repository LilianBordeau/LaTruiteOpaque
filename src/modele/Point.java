package modele;

import java.io.Serializable;

public class Point<T> extends Coup<T> implements Serializable,Cloneable
{
    public int ligne;
    public int colonne;
    
    public Point(int ligne, int colonne)
    {
        this.ligne = ligne;
        this.colonne = colonne;
    }
    
    public Point(int ligne, int colonne, T gain)
    {
        this(ligne, colonne);
        this.gain = gain;
    }
    
    @Override
    public String toString()
    {
        return "("+ligne+","+colonne+")";
    }
    
    @Override
    public boolean equals(Object that)
    {
        if(that instanceof Point)
        {
            Point point = ((Point) that);
            return ligne==point.ligne && colonne==point.colonne;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public Point<T> clone()
    {
        return (Point<T>)super.clone();
    }
}
