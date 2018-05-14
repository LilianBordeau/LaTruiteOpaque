package thread;

public class MyThread
{
    public static void sleep(long millis)
    {
        try
        {                          
            Thread.sleep(millis);             
        }
        catch (InterruptedException e)
        {
           throw new RuntimeException(e);
        }
    }
}
