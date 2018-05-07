package modele;

import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class GenererFichierNiveau
{
	public static void generer()
	{
		Random random = new Random();
		PrintStream out = null;
		try
		{
			out = new PrintStream("niveau.txt");
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
				out.print((random.nextInt(3)+1));
			}
			if(i!=6)
			{
				out.println();
			}
		}
		out.close();
	}
}
