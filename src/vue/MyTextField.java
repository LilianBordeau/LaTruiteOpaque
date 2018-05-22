package vue;

import javafx.scene.control.TextField;

public class MyTextField extends TextField
{ 
    public int maxlength;
    
    public MyTextField()
    {
        this.maxlength = 10;
    }
    
    @Override
    public void replaceText(int start, int end, String text)
    {
        if (text.isEmpty() || getText().length() < maxlength)
        {
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
        super.replaceSelection(text);
    }
    
}
