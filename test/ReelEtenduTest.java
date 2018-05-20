import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import util.ReelEtendu;

public class ReelEtenduTest
{
    public static ReelEtendu moinsUn;
    public static ReelEtendu un;
    public static ReelEtendu plusInf;
    public static ReelEtendu moinsInf;
    
    @BeforeClass
    public static void init()
    {
        moinsUn = new ReelEtendu(-1);
        un = new ReelEtendu(1);
        plusInf = ReelEtendu.plusLInfini();
        moinsInf = ReelEtendu.moinsLInfini();
    }
    
    @Test
    public void reelEtenduCompareToTest()
    {
        assertTrue(moinsUn.compareTo(un)<0);
        assertTrue(un.compareTo(moinsUn)>0);
        assertTrue(moinsUn.compareTo(moinsUn)==0);
        assertTrue(moinsUn.compareTo(plusInf)<0);
        assertTrue(plusInf.compareTo(moinsUn)>0);
        assertTrue(plusInf.compareTo(plusInf)==0);
        assertTrue(moinsUn.compareTo(moinsInf)>0);
        assertTrue(moinsInf.compareTo(moinsUn)<0);
        assertTrue(moinsInf.compareTo(moinsInf)==0);
        assertTrue(plusInf.compareTo(moinsInf)>0);
        assertTrue(moinsInf.compareTo(plusInf)<0);
    }
    
    @Test
    public void reelEtenduEstPlusLInfiniTest()
    {
        assertTrue(plusInf.estPlusLInfini());
        assertFalse(moinsInf.estPlusLInfini());
        assertFalse(moinsUn.estPlusLInfini());
    }
    
    @Test
    public void reelEtenduEstMoinsLInfiniTest()
    {
        assertTrue(moinsInf.estMoinsLInfini());
        assertFalse(plusInf.estMoinsLInfini());
        assertFalse(moinsUn.estMoinsLInfini());
    }
    
    @Test
    public void reelEtenduMinTest()
    {
        assertTrue(ReelEtendu.min(moinsUn, moinsUn).compareTo(moinsUn)==0);
        assertTrue(ReelEtendu.min(plusInf, plusInf).compareTo(plusInf)==0);
        assertTrue(ReelEtendu.min(moinsInf, moinsInf).compareTo(moinsInf)==0);
        assertTrue(ReelEtendu.min(moinsUn, un).compareTo(moinsUn)==0);
        assertTrue(ReelEtendu.min(un, moinsUn).compareTo(moinsUn)==0);
        assertTrue(ReelEtendu.min(moinsUn, plusInf).compareTo(moinsUn)==0);
        assertTrue(ReelEtendu.min(plusInf, moinsUn).compareTo(moinsUn)==0);
        assertTrue(ReelEtendu.min(moinsUn, moinsInf).compareTo(moinsInf)==0);
        assertTrue(ReelEtendu.min(moinsInf, moinsUn).compareTo(moinsInf)==0);
        assertTrue(ReelEtendu.min(plusInf, moinsInf).compareTo(moinsInf)==0);
        assertTrue(ReelEtendu.min(moinsInf, plusInf).compareTo(moinsInf)==0);
    }
    
    @Test
    public void reelEtenduMaxTest()
    {
        assertTrue(ReelEtendu.max(moinsUn, moinsUn).compareTo(moinsUn)==0);
        assertTrue(ReelEtendu.max(plusInf, plusInf).compareTo(plusInf)==0);
        assertTrue(ReelEtendu.max(moinsInf, moinsInf).compareTo(moinsInf)==0);
        assertTrue(ReelEtendu.max(moinsUn, un).compareTo(un)==0);
        assertTrue(ReelEtendu.max(un, moinsUn).compareTo(un)==0);
        assertTrue(ReelEtendu.max(moinsUn, plusInf).compareTo(plusInf)==0);
        assertTrue(ReelEtendu.max(plusInf, moinsUn).compareTo(plusInf)==0);
        assertTrue(ReelEtendu.max(moinsUn, moinsInf).compareTo(moinsUn)==0);
        assertTrue(ReelEtendu.max(moinsInf, moinsUn).compareTo(moinsUn)==0);
        assertTrue(ReelEtendu.max(plusInf, moinsInf).compareTo(plusInf)==0);
        assertTrue(ReelEtendu.max(moinsInf, plusInf).compareTo(plusInf)==0);
    }
    
    @Test
    public void reelEtenduOpposeTest()
    {
        assertTrue(un.oppose().compareTo(moinsUn)==0);
        assertTrue(moinsUn.oppose().compareTo(un)==0);
        assertTrue(plusInf.oppose().compareTo(moinsInf)==0);
        assertTrue(moinsInf.oppose().compareTo(plusInf)==0);
    }
}
