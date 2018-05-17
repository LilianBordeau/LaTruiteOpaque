package reseau;

import java.io.IOException;

public class UnverifiedIOException extends RuntimeException
{
    public UnverifiedIOException(IOException e)
    {
        super(e);
    }
}
