package reseau;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnexionClient extends Connexion
{
    public String ipServeur;
    
    ConnexionClient()
    {
            socket = new Socket();
    }
    
    public void connect(String ipServeur, int port, int timeoutConnectMillis) throws IOException
    {
        socket.connect(new InetSocketAddress(ipServeur,port),timeoutConnectMillis);
        socket.setSoTimeout(timeoutConnectMillis);
        setSocket(socket);   
        socket.setSoTimeout(0);
    }
}
