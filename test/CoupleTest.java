import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
        // on cherche bien a tester que ce sont les meme instances que celles passees au constructeur
        assertEquals(couple.premier == unEntier, couple.second == uneChaine); 
    }
}
