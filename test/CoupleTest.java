import org.junit.Test;
import static org.junit.Assert.*;
import util.Couple;

public class CoupleTest
{
    @Test
    public void coupleTest()
    {
        Integer unEntier = 3;
        String uneChaine = "uneChaine";
        Couple<Integer,String> couple = new Couple<>(unEntier, uneChaine);
        /* il faut bien a tester que les composantes du couple sont les meme instances (verifie par assertSame) que celles passees 
           au constructeur et non simplement que equals renvoie vrai (verifie par assertEquals) */
        assertSame(unEntier, couple.premier);
        assertSame(uneChaine, couple.second);
    }
}
