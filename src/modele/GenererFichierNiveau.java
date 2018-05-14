package modele;

import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class GenererFichierNiveau
{
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
                ecrireNiveau(plateauInt, nomFichier);
        }
        
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
			for(int j = 0 ; j <= (((i%2)==0)?6:7) ; j++)
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
                int nbCases1Poisson = 0;
                while(nbCases1Poisson < 9)
                {
                    nbCases1Poisson = 0;
                    for(int i = 0 ; i <= 7 ; i++)
                    {
                            for(int j = 0 ; j <= (((i%2)==0)?6:7) ; j++)
                            {
                                    plateau[i][j] = random.nextInt(3)+1;
                                    if(plateau[i][j] == 1)
                                    {
                                        nbCases1Poisson++;
                                    }
                            }
                    }
                }
                ecrireNiveau(plateau, "niveau.txt");		
	}
}
