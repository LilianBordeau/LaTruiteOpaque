package reseau;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnexionServeur extends Connexion
{     
    public ConnexionServeur()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(Connexion.PORT);
            setSocket(serverSocket.accept());                    
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }    
}
