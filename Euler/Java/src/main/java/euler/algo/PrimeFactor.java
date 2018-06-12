package euler.algo;

public class PrimeFactor {
    private final long p;
    private final int a;

    public PrimeFactor(long p, int a) {
        this.p = p;
        this.a = a;
    }

    public long prime() {
        return p;
    }

    public int power() {
        return a;
    }

    public long value() {
        return Library.pow(p, a);
    }

    public boolean equals(Object obj) {
        if (obj instanceof PrimeFactor) {
            PrimeFactor other = (PrimeFactor)obj;
            return p == other.p && a == other.a;
        }
        return false;
    }

    public int hashCode() {
        return Long.hashCode(p) * 31 + a;
    }

    public String toString() {
        return "{" + p + "," + a + "}";
    }
}
