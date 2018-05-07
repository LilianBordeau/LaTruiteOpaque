package modele;

import java.util.Random;

public class Plateau
{
    public Case[][] plateau;
    public Plateau()
    {
        plateau = new Case[8][8];
        Random random = new Random();
        int nbCases1Poisson = 0;
        while(nbCases1Poisson < 9)
        {   
            nbCases1Poisson = 0;
            for(int i = 0 ; i < plateau.length ; i++)
            {
                for(int j = 0 ; j < (((i%2)==0)?(plateau.length-1):plateau.length) ; j++)
                {
                    plateau[i][j] = new Case(random.nextInt(3)+1);
                    if(plateau[i][j].nbPoissons == 1)
                    {
                        nbCases1Poisson++;
                    }
                }
            }
        }
    }
}
