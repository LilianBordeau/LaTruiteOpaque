package modele;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import java.util.Scanner;

public class Plateau implements Serializable
{
    public Case[][] plateau;
    
    public Plateau()
    {
        plateau = new Case[8][8];
        String fichierNiveau = null;
        // fichierNiveau = "rsc/niveau.txt";
        if(fichierNiveau == null)
        {
            Random random = new Random();
            int nbCases1Poisson = 0;
            /* le plateau est regenere tant qu'il n'y a pas au moins 9 case avec un poisson (la probabilite d'en avoir au moins 9 est
            sum (60 choose k)*((1/3)^k)*((2/3)^(60-k)) k=9 to 60 = 4708335263475879410443936361/4710128697246244834921603689 ~ 0.99962, on genere donc
            souvent un plateau satisfaisant cette condition des le premier tirage) */
            while(nbCases1Poisson < 9)
            {   
                nbCases1Poisson = 0;
                for(int i = 0 ; i < plateau.length ; i++)
                {
                    for(int j = 0 ; j < nbTuilesLigne(i) ; j++)
                    {
                        plateau[i][j] = new Case(random.nextInt(3)+1,i,j);
                        if(plateau[i][j].nbPoissons == 1)
                        {
                            nbCases1Poisson++;
                        }
                    }
                }
            }
        }
        else
        {
            lireNiveau(fichierNiveau);
        }
        
    }
    
    
    
    public static int nbTuilesLigne(int i)
    {
        return (i%2==0)?7:8;
    }

    private void lireNiveau(String fichierNiveau)
    {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(fichierNiveau));  
            int nbCases1Poisson = 0;
            for(int i = 0 ; i < plateau.length ; i++)
            {
                for(int j = 0 ; j < nbTuilesLigne(i) ; j++)
                {
                    plateau[i][j] = new Case(scanner.nextInt(),i,j);
                    if(plateau[i][j].nbPoissons <= 0 || plateau[i][j].nbPoissons >= 4)
                    {
                        throw new RuntimeException("le fichier "+fichierNiveau+"contient une case avec un nombre invalide de poisson ("+plateau[i][j].nbPoissons+")");
                    }
                    else if(plateau[i][j].nbPoissons == 1)
                    {
                        nbCases1Poisson++;
                    }
                }
            }
            scanner.close();
        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
