package euler.algo;

import java.math.BigInteger;
import java.util.Arrays;

import static euler.algo.Library.modmul;

public class Polynomial {
    private final long[] coef; // polynomial coefficients
    private int deg; // degree of polynomial

    /**
     * Creates the constant polynomial P(x)=0.
     */
    public Polynomial() {
        this(0);
    }

    /**
     * Creates the constant polynomial P(x)=a.
     */
    public Polynomial(long a) {
        this(a, 0);
    }

    /**
     * Creates the polynomial P(x)=a*x^b.
     */
    public Polynomial(long a, int b) {
        coef = new long[b + 1];
        coef[b] = a;
        deg = updateDegree();
    }

    /**
     * Creates the polynomial with coefficients.
     */
    public Polynomial(long[] coef) {
        long[] rev = new long[coef.length];
        for (int i = 0; i < coef.length; i++)
            rev[i] = coef[coef.length - i - 1];
        this.coef = rev;
        this.deg = updateDegree();
    }

    private Polynomial(long[] coef, int deg) {
        this.coef = coef;
        this.deg = deg;
    }

    /**
     * Returns the degree of this polynomial.
     */
    public int degree() {
        return deg;
    }

    private int updateDegree() {
        int d = coef.length - 1;
        while (d > 0 && coef[d] == 0)
            d--;
        return d;
    }

    /**
     * Returns the negation of this polynomial.
     */
    public Polynomial neg() {
        long[] t = Arrays.copyOf(coef, deg + 1);
        for (int i = 0; i < t.length; i++)
            t[i] = -t[i];
        return new Polynomial(t, deg);
    }

    /**
     * Returns the sum of this polynomial and b.
     */
    public Polynomial add(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0, Math.max(a.deg, b.deg));

        for (int i = 0; i <= a.deg; i++)
            c.coef[i] = a.coef[i];
        for (int i = 0; i <= b.deg; i++)
            c.coef[i] += b.coef[i];
        c.deg = c.updateDegree();
        return c;
    }

    /**
     * Returns the sum of this polynomial and constant c.
     */
    public Polynomial add(long c) {
        long[] t = Arrays.copyOf(coef, deg + 1);
        t[0] += c;
        return new Polynomial(t, deg);
    }

    /**
     * Returns the difference of this polynomial and b.
     */
    public Polynomial sub(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0, Math.max(a.deg, b.deg));

        for (int i = 0; i <= a.deg; i++)
            c.coef[i] = a.coef[i];
        for (int i = 0; i <= b.deg; i++)
            c.coef[i] -= b.coef[i];
        c.deg = c.updateDegree();
        return c;
    }

    /**
     * Returns the difference of this polynomial and c.
     */
    public Polynomial sub(long c) {
        long[] t = Arrays.copyOf(coef, deg + 1);
        t[0] -= c;
        return new Polynomial(t, deg);
    }

    /**
     * Returns the product of this polynomial and b.
     */
    public Polynomial mul(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0, a.deg + b.deg);

        for (int i = 0; i <= a.deg; i++)
            for (int j = 0; j <= b.deg; j++)
                c.coef[i+j] += a.coef[i] * b.coef[j];
        c.deg = c.updateDegree();
        return c;
    }

    /**
     * Returns the product of this polynomial and constant c.
     */
    public Polynomial mul(long c) {
        long[] t = Arrays.copyOf(coef, deg + 1);
        for (int i = 0; i < t.length; i++)
            t[i] *= c;
        return new Polynomial(t, deg);
    }

    /**
     * Returns the power of this polynomial by n.
     */
    public Polynomial pow(int n) {
        if (n < 0)
            throw new UnsupportedOperationException("Negative exponential");
        if (n == 0)
            return new Polynomial(1);
        n--;

        Polynomial a = this, b = this;
        while (n > 0) {
            if ((n & 1) != 0)
                b = a.mul(b);
            a = a.mul(a);
            n >>= 1;
        }
        return b;
    }

    /**
     * Returns the composite of this polynomial and b.
     */
    public Polynomial compose(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial();
        for (int i = a.deg; i >= 0; i--)
            c = c.mul(b).add(a.coef[i]);
        return c;
    }

    /**
     * Evaluate this polynomial at x.
     */
    public long evaluate(long x) {
        long p = 0;
        for (int i = deg; i >= 0; i--)
            p = p * x + coef[i];
        return p;
    }

    /**
     * Evaluate this polynomial at x modulo m.
     */
    public long evaluate(long x, long m) {
        long p = 0;
        for (int i = deg; i >= 0; i--)
            p = (modmul(p, x, m) + coef[i]) % m;
        return p;
    }

    /**
     * Evaluate this polynomial at x.
     */
    public BigInteger evaluate(BigInteger x) {
        BigInteger p = BigInteger.ZERO;
        for (int i = deg; i >= 0; i--)
            p = p.multiply(x).add(BigInteger.valueOf(coef[i]));
        return p;
    }

    /**
     * Returns true if this polynomial is idential to the given object.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Polynomial))
            return false;

        Polynomial a = this, b = (Polynomial)obj;
        if (a.deg != b.deg)
            return false;
        for (int i = 0; i <= a.deg; i++)
            if (a.coef[i] != b.coef[i])
                return false;
        return true;
    }

    /**
     * Returns the hash code of this polynomial.
     */
    public int hashCode() {
        int result = 1;
        for (int i = 0; i <= deg; i++) {
            int h = (int)(coef[i] ^ (coef[i] >>> 32));
            result = 31 * result + h;
        }
        return result;
    }

    /**
     * Returns a String representation of this polynomial.
     */
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = deg; i >= 0; i--) {
            if (i != 0 && coef[i] == 0)
                continue;
            if (i != deg)
                b.append(coef[i] > 0 ? " + " : " - ");
            if (i == 0 || Math.abs(coef[i]) != 1)
                b.append(Math.abs(coef[i]));
            if (i > 0)
                b.append("x");
            if (i > 1)
                b.append("^").append(i);
        }
        return b.toString();
    }
}
