package vue;

import java.util.Arrays;
import javafx.beans.NamedArg;
import javafx.scene.control.TextField;

public class MyTextField extends TextField
{ 
    public int maxlength;
    public String charactereInvalides;
    
   
    public MyTextField()
    {
        this.charactereInvalides = "";
        this.maxlength = 500;
        
    }
    
    @Override
    public void replaceText(int start, int end, String text)
    {
        if (text.isEmpty() || getText().length() < maxlength)
        {
            text = text.replaceAll(charactereInvalides,"");
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        

        if (!text.isEmpty() && getText().length() < maxlength && text.length() > maxlength - getText().length())
        {
            text = text.substring(0, maxlength- getText().length());
        }
        text = text.replaceAll(charactereInvalides,"");

        super.replaceSelection(text);
    }
    
}
