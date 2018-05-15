package reseau;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnexionServeur extends Connexion
{     
    public ConnexionServeur(int port)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            setSocket(serverSocket.accept());                    
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }    
}
