package reseau;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public abstract class Connexion
{
    public static final int PORT = 1234;
    private ObjectOutputStream out; 
    private ObjectInputStream in;
    protected Socket socket;
    
    
    public void setSocket(Socket socket) throws IOException
    {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
    
    public synchronized Object readObject() throws IOException
    {
        try {
            return in.readObject();
        }catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public synchronized void writeObject(Object object) throws IOException
    {
        out.writeObject(object);
    }
    
    public void close()
    {
        try {
            /*in.close();
            out.close();*/
            if(socket != null)
            {
                socket.close();
            }            
        } catch (IOException ex) {
            System.out.println("Impossible de fermer la connexion (Socket)");
        }
    }
}
