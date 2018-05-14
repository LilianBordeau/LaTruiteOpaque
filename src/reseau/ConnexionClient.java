package reseau;

import java.io.IOException;
import java.net.Socket;

public class ConnexionClient extends Connexion
{
    public String ipServeur;
    
    public ConnexionClient(String ipServeur)
    {
        try
        {
            setSocket(new Socket(ipServeur, Connexion.PORT));               
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
