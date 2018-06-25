package euler.algo;

import euler.util.IntArray;
import static euler.algo.Library.fibonacci;

@SuppressWarnings("unused")
public class Phigital implements Comparable<Phigital> {
    public static final Phigital PHI  = new Phigital(0, 1);
    public static final Phigital ZERO = new Phigital(0, 0);
    public static final Phigital ONE  = new Phigital(1, 0);

    private static final double phi = (1 + Math.sqrt(5)) / 2;

    private final long a, b;

    public Phigital(long a, long b) {
        this.a = a;
        this.b = b;
    }

    public static Phigital valueOf(long n) {
        return new Phigital(n, 0);
    }

    public Phigital plus(Phigital x) {
        return new Phigital(a + x.a, b + x.b);
    }

    public Phigital subtract(Phigital x) {
        return new Phigital(a - x.a, b - x.b);
    }

    public Phigital times(Phigital x) {
        return new Phigital(a * x.a + b * x.b, a * x.b + b * x.a + b * x.b);
    }

    @Override
    public int compareTo(Phigital x) {
        if (a == x.a && b == x.b)
            return 0;
        return (a - x.a) > (x.b - b) * phi ? 1 : -1;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Phigital) {
            Phigital x = (Phigital)obj;
            return a == x.a && b == x.b;
        }
        return false;
    }

    public int hashCode() {
        int result = (int)(a ^ (a >>> 32));
        result = 31 * result + (int)(b ^ (b >>> 32));
        return result;
    }

    public String toString() {
        return String.format("%d+%dφ", a, b);
    }

    public double doubleValue() {
        return a + b * phi;
    }

    public static Phigital phiPower(int n) {
        // φ^n = φ*F(n) + F(n-1)
        return new Phigital(fibonacci(n - 1), fibonacci(n));
    }

    private static int phiExponent(Phigital x) {
        if (x.a == 1 && x.b == 0)
            return 0;

        int a = 0;
        if (x.compareTo(PHI) >= 0) {
            do {
                a++;
            } while (x.compareTo(phiPower(a)) > 0);
            return a - 1;
        } else {
            do {
                a--;
            } while (x.compareTo(phiPower(a)) < 0);
            return a;
        }
    }

    public IntArray encode() {
        IntArray r = new IntArray();
        Phigital x = this;
        while (x.a != 0 || x.b != 0) {
            int a = phiExponent(x);
            x = x.subtract(phiPower(a));
            r.add(a);
        }
        return r;
    }
}
