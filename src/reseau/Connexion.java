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
    private Socket socket;
    
    
    public void setSocket(Socket socket)
    {
        try
        {
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());                    
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public synchronized Object readObject()
    {
        try {
            return in.readObject();
        }catch (SocketException|EOFException ex) {  
            System.out.println("SocketException|EOFException");
            return null;
        }catch (IOException|ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public synchronized void writeObject(Object object)
    {
        try {
            out.writeObject(object);
        }catch (SocketException|EOFException ex) {  
            System.out.println("SocketException|EOFException");
        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void close()
    {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            System.out.println("Impossible de fermer la connexion");
        }
    }
}
