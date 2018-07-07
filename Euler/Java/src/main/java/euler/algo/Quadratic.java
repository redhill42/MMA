package euler.algo;

import static euler.algo.Library.factorize;
import static euler.algo.Library.gcd;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * Represents the quadratic irrational <em>(P + A * sqrt(D)) / Q</em>.
 */
public class Quadratic {
    public final long P, A, D, Q;

    /**
     * Construct the quadratic irrational in the form <em>(p + sqrt(d)) / q</em>.
     */
    public Quadratic(long p, long d, long q) {
        this(p, 1, d, q);
    }

    /**
     * Construct the quadratic irrational in the form <em>(p + a * sqrt(d)) / q</em>.
     */
    public Quadratic(long p, long a, long d, long q) {
        if (d < 0)
            throw new IllegalArgumentException("Negative square root");
        if (q == 0)
            throw new IllegalArgumentException("Zero denominator");

        if (d != 0) {
            for (PrimeFactor f : factorize(d)) {
                if (f.power() > 1) {
                    long x = Library.pow(f.prime(), f.power() / 2);
                    a *= x;
                    d /= x * x;
                }
            }
        }

        if (d == 1) {
            p += a;
            d = 0;
        }
        if (a == 0 || d == 0) {
            a = d = 0;
        }

        long g = gcd(p, q, a);
        if (q < 0) g = -g;
        this.P = p / g;
        this.A = a / g;
        this.D = d;
        this.Q = q / g;
    }

    @SuppressWarnings("UnusedParameters")
    private Quadratic(long p, long a, long d, long q, int dummy) {
        if (d == 1) {
            p += a;
            d = 0;
        }
        if (a == 0 || d == 0) {
            a = d = 0;
        }

        long g = gcd(p, q, a);
        if (q < 0) g = -g;
        this.P = p / g;
        this.A = a / g;
        this.D = d;
        this.Q = q / g;
    }

    static Quadratic make(long p, long a, long d, long q) {
        return new Quadratic(p, a, d, q, 0);
    }

    /**
     * Returns the sum of this quadratic irrational with the one specified.
     * If the sum is not a quadratic irrational then null is returned.
     *
     * @param that the quadratic irrational to be added.
     * @return {@code this + that}
     */
    public Quadratic add(Quadratic that) {
        if (that == null)
            return null;

        if (this.D == that.D) {
            long p = this.P * that.Q + this.Q * that.P;
            long a = this.A * that.Q + this.Q * that.A;
            long q = this.Q * that.Q;
            return make(p, a, D, q);
        }

        if (that.A == 0 || that.D == 0)
            return add(that.P, that.Q);
        if (A == 0 || D == 0)
            return that.add(P, Q);
        return null;
    }

    /**
     * Returns the sum of this quadratic irrational with the specified
     * rational number.
     *
     * @param r the rational to be added
     * @return {@code this + that}
     */
    public Quadratic add(Ratio r) {
        return add(r.numer(), r.denom());
    }

    private Quadratic add(long n, long d) {
        return make(P * d + Q * n, A * d, D, Q * d);
    }

    /**
     * Returns the difference between this quadratic irrational and the one
     * specified. If the difference is not a quadratic irrational then null
     * is returned.
     *
     * @param that the quadratic irrational to be subtracted.
     * @return {@code this - that}
     */
    public Quadratic subtract(Quadratic that) {
        if (this.D == that.D) {
            long p = this.P * that.Q - this.Q * that.P;
            long a = this.A * that.Q - this.Q * that.A;
            long q = this.Q * that.Q;
            return make(p, a, D, q);
        }

        if (that.A == 0 || that.D == 0)
            return add(-that.P, that.Q);
        if (A == 0 || D == 0)
            return that.add(-P, Q);
        return null;
    }

    /**
     * Returns the difference between this quadratic irrational and the
     * specified rational number.
     *
     * @param r the rational number to be subtracted
     * @return {@code this - that}
     */
    public Quadratic subtract(Ratio r) {
        return add(-r.numer(), r.denom());
    }

    /**
     * Returns the product of this quadratic irrational with the one specified.
     * If the product is not a quadratic irrational then null is returned.
     *
     * @param that the quadratic irrational multiplier
     * @return {@code this * that}
     */
    public Quadratic multiply(Quadratic that) {
        if (this.D == that.D) {
            long p = this.P * that.P + this.A * that.A * D;
            long a = this.P * that.A + this.A * that.P;
            long q = this.Q * that.Q;
            return make(p, a, D, q);
        }

        if (this.P == 0 && that.P == 0)
            return new Quadratic(0, A * that.A, D * that.D, Q * that.Q);
        if (that.A == 0 || that.D == 0)
            return multiply(that.P, that.Q);
        if (A == 0 || D == 0)
            return that.multiply(P, Q);
        return null;
    }

    /**
     * Returns the product of this quadratic irrational with the specified
     * rational number.
     *
     * @param r the rational multiplier
     * @return {@code this * that}
     */
    public Quadratic multiply(Ratio r) {
        return multiply(r.numer(), r.denom());
    }

    private Quadratic multiply(long n, long d) {
        return make(P * n, A * n, D, Q * d);
    }

    /**
     * Returns this quadratic irrational divided by the one specified.
     *
     * @param that the quadratic irrational divisor.
     * @return {@code this / that}
     */
    public Quadratic divide(Quadratic that) {
        return multiply(that.inverse());
    }

    /**
     * Returns this quadratic irrational divided by the specified rational number.
     *
     * @param r the rational divisor.
     * @return {@code this / that}
     */
    public Quadratic divide(Ratio r) {
        return multiply(r.denom(), r.numer());
    }

    /**
     * Returns this quadratic irrational raised to the specified power.
     *
     * @param n the exponent.
     * @return {@code this^n}
     */
    public Quadratic pow(int n) {
        if (n == 0)
            return make(1, 0, 0, 1);
        if (n < 0) // noinspection TailRecursion
            return inverse().pow(-n);
        if (n == 1)
            return this;
        n--;

        Quadratic x = this, y = this;
        while (n != 0) {
            if ((n & 1) == 1)
                y = y.multiply(x);
            n >>= 1;
            x = x.multiply(x);
        }
        return y;
    }

    /**
     * Returns the reciprocal of this quadratic irrational.
     *
     * @return {@code 1 / this}
     */
    public Quadratic inverse() {
        if (A == 0 || D == 0)
            return make(Q, 0, 0, P);

        long p = -P * Q;
        long a = A * Q;
        long q = A * A * D - P * P;
        return make(p, a, D, q);
    }

    /**
     * Returns the negation of this quadratic irrational.
     *
     * @return {@code -this}
     */
    public Quadratic negate() {
        return make(-P, -A, D, Q);
    }

    /**
     * Returns the continued fraction representation of this quadratic irrational.
     *
     * @return the continued fraction
     */
    public ContinuedFraction toContinuedFraction() {
        return ContinuedFraction.quadratic(P, A, D, Q);
    }

    /**
     * Converts this quadratic irrational to a {@code double}. This conversion
     * can lose information about the precision of the irrational value.
     *
     * @return this quadratic irrational number converted to a {@code double}.
     */
    public double doubleValue() {
        return (P + A * sqrt(D)) / Q;
    }

    /**
     * Returns the string representation of this quadratic irrational.
     *
     * @return the string representation
     */
    public String toString() {
        StringBuilder b = new StringBuilder();

        if (P == 0 && (A == 0 || D == 0))
            return "0";

        if (P == 0) {
            if (A < 0)
                b.append('-');
            if (abs(A) != 1)
                b.append(abs(A));
            b.append('√').append(D);
            if (Q != 1)
                b.append('/').append(Q);
            return b.toString();
        }

        if (A == 0 || D == 0) {
            b.append(P);
            if (Q != 1)
                b.append('/').append(Q);
            return b.toString();
        }

        if (Q != 1)
            b.append('(');
        b.append(P);
        b.append(A < 0 ? '-' : '+');
        if (abs(A) != 1)
            b.append(abs(A));
        b.append('√').append(D);
        if (Q != 1)
            b.append(")/").append(Q);
        return b.toString();
    }
}
