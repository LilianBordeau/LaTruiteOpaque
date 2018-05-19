package modele;

import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class GenererFichierNiveau
{        
        public static final int NBTUILES3POISSONS = 10;
        public static final int NBTUILES2POISSONS = 20;
                
        public static void ecrireNiveau(int[][] plateau, String nomFichier)
	{                
		PrintStream out = null;
		try
		{
			out = new PrintStream(nomFichier);
		}
		catch(FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}
                for(int i = 0 ; i <= 7 ; i++)
		{
			for(int j = 0 ; j < nbTuilesLigne(i) ; j++)
			{
				if(j!=0)
				{
					out.print(" ");
				}
				out.print(plateau[i][j]);
			}
			if(i!=7)
			{
				out.println();
			}
		}
		out.close();
        }
        
	public static void generer()
	{                       
                int[][] plateau = new int[8][8];
		Random random = new Random();
                for(int i = 0 ; i < plateau.length ; i++)
                {
                    for(int j = 0 ; j < nbTuilesLigne(i) ; j++)
                    {
                        plateau[i][j] = 1;
                    }
                }
                for(int i = 1 ; i <= NBTUILES3POISSONS+NBTUILES2POISSONS ; i++)
                {
                    int nbPoissons = (i<=NBTUILES2POISSONS)?2:3;
                    int ligne = -1;
                    int colonne = -1;
                    do
                    {
                        ligne = random.nextInt(plateau.length);
                        colonne = random.nextInt(nbTuilesLigne(i));
                    } while(plateau[ligne][colonne] != 1);
                    plateau[ligne][colonne] = nbPoissons;
                } 
                ecrireNiveau(plateau, "niveau.txt");		
	}
        
        /* cette methode est copiee de la classe plateau car ce fichier ne doit pas dependre du reste du projet car il peut etre utilise seul pour generer un fichier
           contenant la description du niveau */
        public static int nbTuilesLigne(int i)
        {
            return (i%2==0)?7:8;
        }
}
