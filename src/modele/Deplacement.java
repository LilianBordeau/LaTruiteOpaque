package modele;

public class Deplacement
{
    public int ligneSrc;
    public int colonneSrc;
    public int ligneDest;
    public int colonneDest;
		
    public Deplacement(int ligneSrc, int colonneSrc, int ligneDest, int colonneDest)
    {
    	this.ligneSrc = ligneSrc;
	this.colonneSrc = colonneSrc;
	this.ligneDest = ligneDest;
	this.colonneDest = colonneDest;
    }
}
