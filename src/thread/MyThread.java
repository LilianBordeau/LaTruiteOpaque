package thread;

import vue.Navigation;

public class MyThread
{
    public static void sleep(long millis)
    {
        try
        {      
            if(!Navigation.estEnModeDebug)
            {
                Thread.sleep(millis); 
            }            
        }
        catch (InterruptedException e)
        {
           throw new RuntimeException(e);
        }
    }
}
