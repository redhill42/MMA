package euler.algo;

import java.io.Serializable;

public class Triple implements Comparable<Triple>, Serializable {
    private static final long serialVersionUID = -1411614514320661827L;

    public final long a, b, c;

    public Triple(long a, long b, long c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public long perimeter() {
        return a + b + c;
    }

    public double area() {
        long s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    public boolean equals(Object o) {
        if (o instanceof Triple) {
            Triple t = (Triple)o;
            return a == t.a && b == t.b && c == t.c;
        }
        return false;
    }

    public int hashCode() {
        int result = (int)(a ^ (a >>> 32));
        result = 31 * result + (int)(b ^ (b >>> 32));
        result = 31 * result + (int)(c ^ (c >>> 32));
        return result;
    }

    public String toString() {
        return String.format("(%d,%d,%d)", a, b, c);
    }

    @Override
    public int compareTo(Triple o) {
        int r = Long.compare(a, o.a);
        if (r != 0) return r;
        r = Long.compare(b, o.b);
        if (r != 0) return r;
        return Long.compare(c, o.c);
    }
}
