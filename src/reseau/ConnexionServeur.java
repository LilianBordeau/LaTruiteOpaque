package reseau;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnexionServeur extends Connexion
{     
    public ConnexionServeur(int port) throws IOException
    {
            ServerSocket serverSocket = new ServerSocket(port);
            setSocket(serverSocket.accept());
    }    
}
