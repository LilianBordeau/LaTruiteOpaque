package modele;

public class Point
{
    public int ligne;
    public int colonne;
    public Point(int ligne, int colonne)
    {
        this.ligne = ligne;
        this.colonne = colonne;
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
}
