package euler.algo;

public class PrimeFactor {
    private final int p, a;

    public PrimeFactor(int p, int a) {
        this.p = p;
        this.a = a;
    }

    public int prime() {
        return p;
    }

    public int power() {
        return a;
    }

    public int value() {
        return (int)Library.pow(p, a);
    }

    public boolean equals(Object obj) {
        if (obj instanceof PrimeFactor) {
            PrimeFactor other = (PrimeFactor)obj;
            return p == other.p && a == other.a;
        }
        return false;
    }

    public int hashCode() {
        return p * 31 + a;
    }

    public String toString() {
        return "{" + p + "," + a + "}";
    }
}
