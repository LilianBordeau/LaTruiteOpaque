package reseau;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnexionServeur extends Connexion
{     
    ServerSocket serverSocket;
    ConnexionServeur(int port) throws IOException
    {
            serverSocket = new ServerSocket(port);            
    }
    
    public void accept() throws IOException
    {
        setSocket(serverSocket.accept());
    }
    
    @Override
    public void close()
    {
        super.close();
        try {
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println("Impossible de fermer la connexion (ServerSocket)");
        }
    }
}
