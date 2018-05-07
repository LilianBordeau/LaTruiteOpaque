package modele;

public class Limite
{
    public int gauche;
    public int droite;
    public int basGauche;
    public int basDroite;
    public int hautGauche;
    public int hautDroite;
    
    public Limite(int gauche, int droite, int basGauche, int basDroite, int hautGauche, int hautDroite)
    {
        this.gauche = gauche;
        this.droite = droite;
        this.basGauche = basGauche;
        this.basDroite = basDroite;
        this.hautGauche = hautGauche;
        this.hautDroite = gauche;
    }
}
