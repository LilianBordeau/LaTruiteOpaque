package reseau;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnexionClient extends Connexion
{
    public String ipServeur;
    
    public ConnexionClient(String ipServeur, int port, int timeoutConnectMillis) throws IOException
    {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ipServeur,port),timeoutConnectMillis);
            socket.setSoTimeout(timeoutConnectMillis);
            setSocket(socket);   
            socket.setSoTimeout(0);
    }
}
