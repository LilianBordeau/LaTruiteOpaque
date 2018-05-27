package modele;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import java.util.Scanner;

public class Plateau implements Serializable,Cloneable
{
    public Case[][] plateau;
    public static final int NBLIGNES = 8;
    public static final int NBCASES1POISSONMIN = 9;
    public static final int NBTUILES3POISSONS = 10;
    public static final int NBTUILES2POISSONS = 20;
    public static String fichierNiveau = null;
    
    public Plateau()
    {
        plateau = new Case[NBLIGNES][NBLIGNES];
        // fichierNiveau = null;
        // fichierNiveau = "rsc/niveau.txt";
        // fichierNiveau = "rsc/niveauCoupNonPerdantTrouve.txt";
        // fichierNiveau = "rsc/niveauIsole.txt";
        // fichierNiveau = "rsc/niveauIADifficilePerd.txt";        
        if(fichierNiveau == null)
        {
            int[][] plateauInt = GenerateurNiveau.genererNiveau();
            Random random = new Random();
            for(int i = 0 ; i < plateau.length ; i++)
            {
                for(int j = 0 ; j < nbTuilesLigne(i) ; j++)
                {
                    plateau[i][j] = new Case(plateauInt[i][j],i,j);
                }
            }
        }
        else
        {
            lireNiveau(fichierNiveau);
        }
        ecrireNiveau(plateau, "dernierNiveau.txt");
    }    
    
    public static int nbTuilesLigne(int i)
    {
        return (i%2==0)?7:8;
    }

    private void lireNiveau(String fichierNiveau)
    {
        try {
            Scanner scanner = new Scanner(new FileInputStream(fichierNiveau));  
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
            if(nbCases1Poisson < NBCASES1POISSONMIN)
            {
                throw new RuntimeException("le fichier contient moins de "+NBCASES1POISSONMIN+" cases avec 1 poissons");
            }
        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static void ecrireNiveau(Case[][] plateau, String nomFichier)
    {
        int[][] plateauInt = new int[8][8];
        for(int i = 0 ; i <= 7 ; i++)
	{
	    for(int j = 0 ; j <= (((i%2)==0)?6:7) ; j++)
            {
               	plateauInt[i][j] = plateau[i][j].nbPoissons;
	    }
        }
        GenerateurNiveau.ecrireNiveau(plateauInt, nomFichier);
    }
    
    @Override
    public Plateau clone()
    {
        try {
            Plateau plateauClone = (Plateau)super.clone();
            plateauClone.plateau = plateauClone.plateau.clone();
            for(int i = 0 ; i < plateau.length ; i++)
            {
                plateauClone.plateau[i] = plateauClone.plateau[i].clone();
                for(int j = 0 ; j < nbTuilesLigne(i) ; j++)
                {
                    plateauClone.plateau[i][j] = plateauClone.plateau[i][j].clone();
                }
            }
            return plateauClone;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
