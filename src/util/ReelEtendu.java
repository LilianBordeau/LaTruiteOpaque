package util;

public class ReelEtendu implements Comparable<ReelEtendu>, Cloneable {

    @Override
    public int compareTo(ReelEtendu that) {
        if (estPlusLInfini()) {
            if(that.estPlusLInfini())
            {
                return 0;
            }
            else
            {
                return 1;
            }
        } else if (estMoinsLInfini()) {
            if(that.estMoinsLInfini())
            {
                return 0;
            }
            else
            {
                return -1;
            }
        } else {
            if (that.estPlusLInfini()) {
                return -1;
            } else if (that.estMoinsLInfini()) {
                return 1;
            } else {
                return (new Long(reel)).compareTo(that.reel);
            }
        }
    }

    private static enum Type {
        REEL, PLUSLINFINI, MOINSLINFINI
    }

    public long reel;
    private Type type;

    public ReelEtendu(long reel) {
        this.reel = reel;
        this.type = Type.REEL;
    }

    private ReelEtendu(Type type) {
        this.reel = 0;
        this.type = type;
    }

    public boolean estPlusLInfini() {
        return type == Type.PLUSLINFINI;
    }

    public boolean estMoinsLInfini() {
        return type == Type.MOINSLINFINI;
    }

    @Override
    public String toString() {
        if (estMoinsLInfini()) {
            return "-infini";
        } else if (estPlusLInfini()) {
            return "+infini";
        } else {
            return reel + "";
        }
    }

    public static ReelEtendu moinsLInfini()
    {
        return new ReelEtendu(Type.MOINSLINFINI);
    }
    
    public static ReelEtendu plusLInfini()
    {
        return new ReelEtendu(Type.PLUSLINFINI);
    }
    
    @Override
    public ReelEtendu clone()
    {
        ReelEtendu resultat = new ReelEtendu(this.reel);
        resultat.type = type;
        return resultat;
    }
    
    public static ReelEtendu min(ReelEtendu a, ReelEtendu b) {
        if (a.compareTo(b) <= 0) {
            return a.clone();
        } else {
            return b.clone();
        }
    }
    
    public static ReelEtendu max(ReelEtendu a, ReelEtendu b) {
        if (a.compareTo(b) >= 0) {
            return a.clone();
        } else {
            return b.clone();
        }
    }
    
    public ReelEtendu oppose()
    {
        if(this.estMoinsLInfini())
        {
            return plusLInfini();
        }
        else if(this.estPlusLInfini())
        {
            return moinsLInfini();
        }
        else
        {
            return new ReelEtendu(-reel);
        }
    }

}