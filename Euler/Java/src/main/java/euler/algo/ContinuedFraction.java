package euler.algo;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

import euler.util.IntArray;

import static euler.algo.Library.big;
import static euler.algo.Library.even;
import static euler.algo.Library.factorize;
import static euler.algo.Library.gcd;
import static euler.algo.Library.isqrt;
import static java.lang.Math.abs;
import static java.lang.Math.addExact;
import static java.lang.Math.floor;
import static java.lang.Math.multiplyExact;
import static java.lang.Math.toIntExact;

@SuppressWarnings({"unused", "RedundantFieldInitialization"})
public abstract class ContinuedFraction {
    /**
     * Returns true if this is a finite continued fraction, false otherwise.
     */
    public boolean isFinite() {
        return !isPeriodic() && length() > 0;
    }

    /**
     * Returns true if this is a periodic continued fraction, false otherwise.
     */
    public boolean isPeriodic() {
        return false;
    }

    /**
     * Returns the period of this continued fraction if it's periodicly.
     */
    public int getPeriod() {
        return 0;
    }

    /**
     * Returns the length of terms in the continued fraction if it's finite
     * or periodic, return -1 if it's infinite.
     */
    public int length() {
        return -1;
    }

    /**
     * Returns the i'th term in the continued fraction.
     */
    public abstract int term(int i);

    /**
     * Returns the string representation of continued fraction.
     */
    public String toString() {
        if (isFinite())
            return finiteToString();
        if (isPeriodic())
            return periodicToString();
        return infiniteToString();
    }

    private String finiteToString() {
        int iMax = length() - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        for (int i = 0; ; i++) {
            buf.append(term(i));
            if (i == iMax)
                return buf.append(']').toString();
            buf.append(',');
        }
    }

    private String periodicToString() {
        int iMax = length() - 1;
        int iFix = iMax - getPeriod() + 1;
        if (iMax == -1)
            return "[]";

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        for (int i = 0; ; i++) {
            if (i == iFix)
                buf.append('{');
            buf.append(term(i));
            if (i == iMax)
                return buf.append("}]").toString();
            buf.append(',');
        }
    }

    private String infiniteToString() {
        StringBuilder buf = new StringBuilder();
        try {
            buf.append('[');
            for (int i = 0; i < 10; i++) {
                buf.append(term(i)).append(',');
            }
        } catch (NoSuchElementException ex) {
            // terminate if term exhausted
        }
        return buf.append("...]").toString();
    }

    /**
     * The interface for iterating on convergents of continued fraction.
     *
     * @param <R> the rational type
     */
    public interface Convergents<R> extends Iterable<R> {
        @Override
        default void forEach(Consumer<? super R> action) {
            forEach(r -> { action.accept(r); return true; });
        }

        /**
         * Convergent on the continued fraction. Passing every partial
         * rational number to the given operator. The operator should
         * return true to continue operate on convergence. Return false to
         * terminate the opreation.
         */
        void forEach(Predicate<? super R> action);

        /**
         * Returns the n'th convergent value.
         */
        default R get(int n) {
            Iterator<R> it = iterator();
            R r = null;
            for (int i = 0; i < n && it.hasNext(); i++)
                r = it.next();
            return r;
        }
    }

    /**
     * Convergent on the continued fraction.
     */
    public Convergents<Ratio> convergents() {
        return convergents(Integer.MAX_VALUE);
    }

    /**
     * Convergent on the continued fraction with the maximum length.
     */
    public Convergents<Ratio> convergents(int n) {
        if (isFinite() && n > length())
            n = length();
        return new SmallConvergents(n);
    }

    /**
     * Convergent on the continued fraction with BigInteger format.
     */
    public <R> Convergents<R> convergents(Class<R> type) {
        return convergents(Integer.MAX_VALUE, type);
    }

    /**
     * Convergent on the continued fraction with BigInteger format and
     * with the maximum length.
     */
    @SuppressWarnings("unchecked")
    public <R> Convergents<R> convergents(int n, Class<R> type) {
        if (isFinite() && n > length())
            n = length();

        if (type == Rational.class)
            return (Convergents<R>)new BigConvergents(n);
        if (type == Ratio.class)
            return (Convergents<R>)new SmallConvergents(n);
        throw new UnsupportedOperationException(type.toString());
    }

    /**
     * Returns the best approximation for this continued fraction
     * with the given bound.
     */
    public Ratio bestApproximation(long bound) {
        long p0 = 1;
        long q0 = 0;
        long p1 = term(0);
        long q1 = 1;
        long p, q;

        int n = isFinite() ? length() : Integer.MAX_VALUE;
        for (int i = 1; i < n; i++) {
            int a = term(i);
            p = a * p1 + p0;
            q = a * q1 + q0;

            if (q > bound) {
                long k = (q - bound) / q1 + 1;
                int b = (a + 1) / 2;
                if (even(a) && !allowHalf(i))
                    b++;
                return (k > a - b)
                    ?  Ratio.valueOf(p1, q1)
                    :  Ratio.valueOf(p - k * p1, q - k * q1);
            }

            p0 = p1; q0 = q1;
            p1 = p;  q1 = q;
        }
        return Ratio.valueOf(p1, q1);
    }

    private boolean allowHalf(int k) {
        int s = 1;
        for (int i = k; i > 0; i--) {
            int diff = s * (term(i) - term(2 * k - i));
            if (diff > 0)
                return true;
            if (diff < 0)
                return false;
            s = -s;
        }
        return even(k);
    }

    /**
     * Returns a sequence of approximations for this continued fraction
     * with the given bound.
     */
    public Convergents<Ratio> approximations(long bound) {
        return new Approximations(bound);
    }

    /*--------------------------------------------------------------------*/

    class SmallConvergents implements Convergents<Ratio> {
        final int n;

        SmallConvergents(int n) {
            this.n = n;
        }

        @Override
        public Iterator<Ratio> iterator() {
            return new Iterator<Ratio>() {
                long p0 = 1;
                long q0 = 0;
                long p1 = term(0);
                long q1 = 1;
                int  i;

                @Override
                public boolean hasNext() {
                    return i < n;
                }

                @Override
                public Ratio next() {
                    if (!hasNext())
                        throw new NoSuchElementException();

                    Ratio next = Ratio.valueOf(p1, q1);
                    if (++i < n) {
                        try {
                            int a = term(i);
                            long p = addExact(multiplyExact(a, p1), p0);
                            long q = addExact(multiplyExact(a, q1), q0);
                            p0 = p1; q0 = q1;
                            p1 = p;  q1 = q;
                        } catch (ArithmeticException ex) {
                            i = n; // no more terms when overflow
                        }
                    }
                    return next;
                }
            };
        }

        @Override
        public void forEach(Predicate<? super Ratio> action) {
            long p0 = 0, q0 = 1;
            long p1 = 1, q1 = 0;

            try {
                for (int i = 0; i < n; i++) {
                    int a = term(i);
                    long p = addExact(multiplyExact(a, p1), p0);
                    long q = addExact(multiplyExact(a, q1), q0);
                    if (!action.test(Ratio.valueOf(p1, q1)))
                        break;
                    p0 = p1; q0 = q1;
                    p1 = p;  q1 = q;
                }
            } catch (ArithmeticException ex) {
                // no more terms when overflow
            }
        }

        @Override
        public Ratio get(int n) {
            if (isFinite() && n > length())
                n = length();

            long p0 = 0, q0 = 1;
            long p1 = 1, q1 = 0;

            for (int i = 0; i < n; i++) {
                long a = term(i);
                long p = addExact(multiplyExact(a, p1), p0);
                long q = addExact(multiplyExact(a, q1), q0);
                p0 = p1; q0 = q1;
                p1 = p;  q1 = q;
            }
            return Ratio.valueOf(p1, q1);
        }
    }

    /*--------------------------------------------------------------------*/

    class BigConvergents implements Convergents<Rational> {
        final int n;

        BigConvergents(int n) {
            this.n = n;
        }

        @Override
        public Iterator<Rational> iterator() {
            return new Iterator<Rational>() {
                int i;
                BigInteger p0 = BigInteger.ZERO;
                BigInteger q0 = BigInteger.ONE;
                BigInteger p1 = BigInteger.ONE;
                BigInteger q1 = BigInteger.ZERO;

                @Override
                public boolean hasNext() {
                    return i < n;
                }

                @Override
                public Rational next() {
                    if (i >= n)
                        throw new NoSuchElementException();

                    BigInteger a = BigInteger.valueOf(term(i++));
                    BigInteger p = a.multiply(p1).add(p0);
                    BigInteger q = a.multiply(q1).add(q0);
                    p0 = p1; q0 = q1;
                    p1 = p;  q1 = q;
                    return Rational.valueOf(p, q);
                }
            };
        }

        @Override
        public void forEach(Predicate<? super Rational> action) {
            BigInteger p0 = BigInteger.ZERO;
            BigInteger q0 = BigInteger.ONE;
            BigInteger p1 = BigInteger.ONE;
            BigInteger q1 = BigInteger.ZERO;

            for (int i = 0; i < n; i++) {
                BigInteger a = BigInteger.valueOf(term(i));
                BigInteger p = a.multiply(p1).add(p0);
                BigInteger q = a.multiply(q1).add(q0);
                if (!action.test(Rational.valueOf(p, q)))
                    break;
                p0 = p1; q0 = q1;
                p1 = p;  q1 = q;
            }
        }

        @Override
        public Rational get(int n) {
            if (isFinite() && n > length())
                n = length();

            BigInteger p0 = BigInteger.ZERO;
            BigInteger q0 = BigInteger.ONE;
            BigInteger p1 = BigInteger.ONE;
            BigInteger q1 = BigInteger.ZERO;

            for (int i = 0; i < n; i++) {
                BigInteger a = BigInteger.valueOf(term(i));
                BigInteger p = a.multiply(p1).add(p0);
                BigInteger q = a.multiply(q1).add(q0);
                p0 = p1; q0 = q1;
                p1 = p;  q1 = q;
            }
            return Rational.valueOf(p1, q1);
        }
    }

    /*--------------------------------------------------------------------*/

    class Approximations implements Convergents<Ratio> {
        final long bound;

        Approximations(long bound) {
            this.bound = bound;
        }

        @Override
        public Iterator<Ratio> iterator() {
            return new Iterator<Ratio>() {
                long p0 = 0, q0 = 1;
                long p1 = 1, q1 = 0;
                long p, q;
                int i, k;

                {
                    int a = term(0);
                    k = (a + 1) / 2;
                    if (even(a) && !allowHalf(0))
                        k++;
                    p = k; q = 1;
                }

                @Override
                public boolean hasNext() {
                    return q <= bound;
                }

                @Override
                public Ratio next() {
                    if (!hasNext())
                        throw new NoSuchElementException();

                    Ratio next = Ratio.valueOf(p, q);

                    if (++k > term(i)) {
                        p0 = p1; q0 = q1;
                        p1 = p;  q1 = q;

                        if (isFinite() && i+1 >= length()) {
                            q = Long.MAX_VALUE; // no more elements
                            return next;
                        }

                        int a = term(++i);
                        k = (a + 1) / 2;
                        if (even(a) && !allowHalf(i))
                            k++;
                    }

                    p = k * p1 + p0;
                    q = k * q1 + q0;
                    return next;
                }
            };
        }

        @Override
        public void forEach(Predicate<? super Ratio> action) {
            long p0 = 0, q0 = 1;
            long p1 = 1, q1 = 0;
            long p  = 0, q  = 0;

            int n = isFinite() ? length() : Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                int a = term(i);
                int k = (a + 1) / 2;
                if (even(a) && !allowHalf(i))
                    k++;

                while (k <= a) {
                    p = k * p1 + p0;
                    q = k * q1 + q0;
                    if (q > bound || !action.test(Ratio.valueOf(p, q)))
                        return;
                    k++;
                }

                p0 = p1; q0 = q1;
                p1 = p;  q1 = q;
            }
        }
    }

    /*--------------------------------------------------------------------*/

    private static final int CF_INF = Integer.MAX_VALUE;

    /**
     * Returns the sum of this continued fraction with the one specified.
     *
     * @param that the continued fraction to be added
     * @return {@code this + that}
     */
    public ContinuedFraction add(ContinuedFraction that) {
        Quadratic x = toQuadratic();
        if (x != null && (x = x.add(that.toQuadratic())) != null)
            return x.toContinuedFraction();
        return new Arithmetic(this, that, 0, 1, 1, 0, 1, 0, 0, 0);
    }

    /**
     * Returns the difference between this continued fraction and the
     * one specified.
     *
     * @param that the continued fraction to be subtracted
     * @return {@code this - that}
     */
    public ContinuedFraction subtract(ContinuedFraction that) {
        Quadratic x = toQuadratic();
        if (x != null && (x = x.subtract(that.toQuadratic())) != null)
            return x.toContinuedFraction();
        return new Arithmetic(this, that, 0, 1, -1, 0, 1, 0, 0, 0);
    }

    /**
     * Returns the product of this continued fraction with the one specified.
     *
     * @param that the multiplier
     * @return {@code this * that}
     */
    public ContinuedFraction multiply(ContinuedFraction that) {
        Quadratic x = toQuadratic();
        if (x != null && (x = x.multiply(that.toQuadratic())) != null)
            return x.toContinuedFraction();
        return new Arithmetic(this, that, 0, 0, 0, 1, 1, 0, 0, 0);
    }

    /**
     * Returns this continued fraction divided by the one specified.
     *
     * @param that the divisor
     * @return {@code this / that}
     */
    public ContinuedFraction divide(ContinuedFraction that) {
        Quadratic x = toQuadratic();
        if (x != null && (x = x.divide(that.toQuadratic())) != null)
            return x.toContinuedFraction();
        return new Arithmetic(this, that, 0, 1, 0, 0, 0, 0, 1, 0);
    }

    /**
     * Returns the sum of this continued fraction with the specified rational
     * number.
     *
     * @param r the rational number to be added
     * @return {@code this + that}
     */
    public ContinuedFraction add(Ratio r) {
        Quadratic x;
        if ((x = toQuadratic()) != null)
            return x.add(r).toContinuedFraction();
        return new HomoArithmetic(this, r.numer(), r.denom(), r.denom(), 0);
    }

    /**
     * Returns the difference of this continued fraction with the specified
     * rational number.
     *
     * @param r the rational number to be added
     * @return {@code this - that}
     */
    public ContinuedFraction subtract(Ratio r) {
        Quadratic x;
        if ((x = toQuadratic()) != null)
            return x.subtract(r).toContinuedFraction();
        return new HomoArithmetic(this, -r.numer(), r.denom(), r.denom(), 0);
    }

    /**
     * Returns the product of this continued fraction with the specified
     * rational number.
     *
     * @param r the rational multiplier
     * @return {@code this * that}
     */
    public ContinuedFraction multiply(Ratio r) {
        Quadratic x;
        if ((x = toQuadratic()) != null)
            return x.multiply(r).toContinuedFraction();
        return new HomoArithmetic(this, 0, r.numer(), r.denom(), 0);
    }

    /**
     * Returns this continued fraction divided by the specified rational number.
     *
     * @param r the rational divisor
     * @return {@code this / that}
     */
    public ContinuedFraction divide(Ratio r) {
        Quadratic x;
        if ((x = toQuadratic()) != null)
            return x.divide(r).toContinuedFraction();
        return new HomoArithmetic(this, 0, r.denom(), r.numer(), 0);
    }

    /**
     * Perform homographic transformation on this continued fraction.
     *
     * @return {@code (a + b * this) / (c + d * this)}
     */
    public ContinuedFraction arithmetic(long a, long b, long c, long d) {
        return new HomoArithmetic(this, a, b, c, d);
    }

    /**
     * Returns this continued fraction raised to the specified power.
     *
     * @param n the exponent.
     * @return {@code this^n}
     */
    public ContinuedFraction pow(int n) {
        if (n == 0)
            return rational(1);
        if (n < 0) // noinspection TailRecursion
            return inverse().pow(-n);
        if (n == 1)
            return this;
        n--;

        ContinuedFraction x = this, y = this;
        while (n != 0) {
            if ((n & 1) == 1)
                y = y.multiply(x);
            n >>= 1;
            x = x.multiply(x);
        }
        return y;
    }

    /**
     * Returns the reciprocal of this continued fraction.
     *
     * @return {@code 1 / this}
     */
    public ContinuedFraction inverse() {
        Quadratic x;
        if ((x = toQuadratic()) != null)
            return x.inverse().toContinuedFraction();
        return new HomoArithmetic(this, 1, 0, 0, 1);
    }

    /**
     * Returns the negation of this continued fraction.
     *
     * @return {@code -this}
     */
    public ContinuedFraction negate() {
        return new Negation(this);
    }

    /**
     * Returns the quadratic irrational representation if this is a periodic
     * infinite continued fraction.
     *
     * @return the quadratic irrational that represent the periodic infinite
     * continued fraction
     */
    public Quadratic toQuadratic() {
        if (isFinite()) {
            try {
                Ratio r = convergents().get(length());
                return new Quadratic(r.numer(), 0, r.denom());
            } catch (ArithmeticException ex) {
                return null;
            }
        }

        if (isPeriodic()) {
            int p0 = 1, q0 = 0, p1 = 0, q1 = 1, s = 1;
            for (int i = length() - getPeriod(); i < length(); i++) {
                int a = term(i);
                if (a < 0) s = -s;
                int p = a * p1 + p0;
                int q = a * q1 + q0;
                p0 = p1; q0 = q1; p1 = p; q1 = q;
            }
            return toQuadratic(q1-p0, s, (q1-p0)*(q1-p0) + 4*q0*p1, 2*p1);
        }

        return null;
    }

    private Quadratic toQuadratic(long p, long a, long d, long q) {
        // extract the square factor
        if (d != 0) {
            for (PrimeFactor f : factorize(d)) {
                if (f.power() > 1) {
                    long x = Library.pow(f.prime(), f.power() / 2);
                    a *= x;
                    d /= x * x;
                }
            }
        }

        for (int i = length() - getPeriod() - 1; i >= 0; i--) {
            // inverse the irrational and add term
            long t = a * a * d - p * p;
            p = -p * q + term(i) * t;
            a *= q;
            q = t;

            // normalize the irrational
            long g = gcd(p, q, a);
            p = p / g; q = q / g; a = a / g;
        }

        return Quadratic.make(p, a, d, q);
    }

    /**
     * Instances of this class represents transformation of the form
     * <pre>
     *     a + bx + cy + dxy
     *   ---------------------
     *     e + fx + gy + hxy
     * </pre>
     * for <em>a, b, c, d, e, f, g, h ∈ ℤ</em>.
     *
     * @see <a href="https://www.plover.com/~mjd/cftalk/">https://www.plover.com/~mjd/cftalk/</a>
     */
    private static class Arithmetic extends ContinuedFraction {
        private final ContinuedFraction x, y;
        private BigInteger a, b, c, d, e, f, g, h;

        private final IntArray terms = new IntArray();
        private int i_x, i_y;

        Arithmetic(ContinuedFraction x, ContinuedFraction y,
                   long a, long b, long c, long d,
                   long e, long f, long g, long h)
        {
            if (e == 0 && f == 0 && g == 0 && h == 0)
                throw new ArithmeticException("divide by 0");

            this.x = x;
            this.y = y;
            this.a = big(a);
            this.b = big(b);
            this.c = big(c);
            this.d = big(d);
            this.e = big(e);
            this.f = big(f);
            this.g = big(g);
            this.h = big(h);

            // consume the initial input
            ingestion();

            // fill terms for finite continued fraction
            if (isFinite()) {
                int t;
                while ((t = next()) != CF_INF) {
                    terms.add(t);
                }
            }
        }

        @Override
        public boolean isFinite() {
            return x.isFinite() && y.isFinite();
        }

        @Override
        public int length() {
            return terms.length;
        }

        @Override
        public int term(int i) {
            for (int j = terms.length; j <= i; j++) {
                int t = next();
                if (t == CF_INF)
                    throw new NoSuchElementException();
                terms.add(t);
            }
            return terms.a[i];
        }

        private int next() {
            do {
                int r;

                if (e.signum() == 0 && f.signum() == 0 && g.signum() == 0 && h.signum() == 0)
                    return CF_INF;

                if ((r = agreeOutput()) != CF_INF) {
                    egestion(BigInteger.valueOf(r));
                    return r;
                }

                ingestion();
            } while (true);
        }

        private int agreeOutput() {
            if (e.signum() != 0 && f.signum() != 0 && g.signum() != 0 && h.signum() != 0) {
                int ae = Rational.valueOf(a, e).intValue();
                int bf = Rational.valueOf(b, f).intValue();
                int cg = Rational.valueOf(c, g).intValue();
                int dh = Rational.valueOf(d, h).intValue();
                if (ae == bf && bf == cg && cg == dh)
                    return ae;
            }
            return CF_INF;
        }

        private void egestion(BigInteger r) {
            BigInteger a1 = e, b1 = f, c1 = g, d1 = h;
            e = a.subtract(e.multiply(r));
            f = b.subtract(f.multiply(r));
            g = c.subtract(g.multiply(r));
            h = d.subtract(h.multiply(r));
            a = a1; b = b1; c = c1; d = d1;
        }

        private void ingestion() {
            Rational ae = Rational.valueOf(a, e);
            Rational bf = Rational.valueOf(b, f);
            Rational cg = Rational.valueOf(c, g);

            if (diff(bf, ae).compareTo(diff(cg, ae)) > 0) {
                if (x.isFinite() && i_x >= x.length()) {
                    a = b; c = d; e = f; g = h;
                } else {
                    BigInteger p = BigInteger.valueOf(x.term(i_x++));
                    BigInteger a1 = b;
                    BigInteger b1 = a.add(b.multiply(p));
                    BigInteger c1 = d;
                    BigInteger d1 = c.add(d.multiply(p));
                    BigInteger e1 = f;
                    BigInteger f1 = e.add(f.multiply(p));
                    BigInteger g1 = h;
                    BigInteger h1 = g.add(h.multiply(p));
                    a = a1; b = b1; c = c1; d = d1;
                    e = e1; f = f1; g = g1; h = h1;
                }
            } else {
                if (y.isFinite() && i_y >= y.length()) {
                    a = c; b = d; e = g; f = h;
                } else {
                    BigInteger q = BigInteger.valueOf(y.term(i_y++));
                    BigInteger a1 = c;
                    BigInteger b1 = d;
                    BigInteger c1 = a.add(c.multiply(q));
                    BigInteger d1 = b.add(d.multiply(q));
                    BigInteger e1 = g;
                    BigInteger f1 = h;
                    BigInteger g1 = e.add(g.multiply(q));
                    BigInteger h1 = f.add(h.multiply(q));
                    a = a1; b = b1; c = c1; d = d1;
                    e = e1; f = f1; g = g1; h = h1;
                }
            }
        }

        private static Rational diff(Rational p, Rational q) {
            if (p.isInfinite())
                return q.isInfinite() ? Rational.ZERO : p;
            if (q.isInfinite())
                return q;
            return p.subtract(q).abs();
        }
    }

    /**
     * Instances of this class represents a homographic transformation of a
     * continued fration, that is a map of the form
     * <pre>
     *     a + bx
     *   ----------
     *     c + dx
     * </pre>
     * for integers <em>a, b, c, d ∈ ℤ</em> with <em>ad - bc ≠ 0</em>.
     */
    private static class HomoArithmetic extends ContinuedFraction {
        private final ContinuedFraction x;
        private BigInteger a, b, c, d;

        private final IntArray terms = new IntArray();
        private int i_x;

        HomoArithmetic(ContinuedFraction x, long a, long b, long c, long d) {
            if (c == 0 && d == 0)
                throw new ArithmeticException("divide by 0");

            this.x = x;
            this.a = big(a);
            this.b = big(b);
            this.c = big(c);
            this.d = big(d);

            // consume the initial input
            ingestion();

            // fill terms for finite continued fraction
            if (x.isFinite()) {
                int t;
                while ((t = next()) != CF_INF) {
                    terms.add(t);
                }
            }
        }

        @Override
        public boolean isFinite() {
            return x.isFinite();
        }

        @Override
        public int length() {
            return terms.length;
        }

        @Override
        public int term(int i) {
            for (int j = terms.length; j <= i; j++) {
                int t = next();
                if (t == CF_INF)
                    throw new NoSuchElementException();
                terms.add(t);
            }
            return terms.a[i];
        }

        private int next() {
            do {
                int r;

                if (c.signum() == 0 && d.signum() == 0)
                    return CF_INF;

                if ((r = agreeOutput()) != CF_INF) {
                    egestion(BigInteger.valueOf(r));
                    return r;
                }

                ingestion();
            } while (true);
        }

        private int agreeOutput() {
            if (c.signum() != 0 && d.signum() != 0) {
                int ac = Rational.valueOf(a, c).intValue();
                int bd = Rational.valueOf(b, d).intValue();
                if (ac == bd) return ac;
            }
            return CF_INF;
        }

        private void egestion(BigInteger r) {
            BigInteger a1 = c, b1 = d;
            c = a.subtract(c.multiply(r));
            d = b.subtract(d.multiply(r));
            a = a1; b = b1;
        }

        private void ingestion() {
            if (x.isFinite() && i_x >= x.length()) {
                a = b; c = d;
            } else {
                BigInteger p = BigInteger.valueOf(x.term(i_x++));
                BigInteger a1 = b;
                BigInteger b1 = a.add(b.multiply(p));
                BigInteger c1 = d;
                BigInteger d1 = c.add(d.multiply(p));
                a = a1; b = b1; c = c1; d = d1;
            }
        }
    }

    private static class Negation extends ContinuedFraction {
        private final ContinuedFraction x;

        Negation(ContinuedFraction x) {
            this.x = x;
        }

        @Override
        public boolean isFinite() {
            return x.isFinite();
        }

        @Override
        public boolean isPeriodic() {
            return x.isPeriodic();
        }

        @Override
        public int getPeriod() {
            return x.getPeriod();
        }

        @Override
        public int length() {
            return x.length();
        }

        @Override
        public int term(int i) {
            return -x.term(i);
        }
    }

    /*--------------------------------------------------------------------*/

    /**
     * The default implementation of ContinuedFraction.
     */
    private static class DefaultContinuedFraction extends ContinuedFraction {
        private final int[] terms;
        private final int period;

        DefaultContinuedFraction(int[] terms, int period) {
            this.terms = terms;
            this.period = period;
        }

        @Override
        public boolean isPeriodic() {
            return period > 0;
        }

        @Override
        public int getPeriod() {
            return period;
        }

        @Override
        public int length() {
            return terms.length;
        }

        @Override
        public int term(int i) {
            if (i < terms.length)
                return terms[i];
            if (!isPeriodic())
                throw new ArrayIndexOutOfBoundsException();

            int k = terms.length - period;
            return terms[k + (i - k) % period];
        }
    }

    /**
     * Make a continued fraction object.
     */
    public static ContinuedFraction make(int[] terms, int period) {
        return new DefaultContinuedFraction(terms, period);
    }

    /**
     * Create a continued fraction from a rational number.
     */
    public static ContinuedFraction rational(long a, long b) {
        IntArray terms = new IntArray();
        long t;

        while (b != 0) {
            terms.add(toIntExact(a / b));
            t = a % b;
            a = b;
            b = t;
        }
        return make(terms.toArray(), 0);
    }

    /**
     * Create a continued fraction from an integer.
     */
    public static ContinuedFraction rational(int n) {
        return make(new int[]{n}, 0);
    }

    private static final double EPS = 1e-9;

    /**
     * Create a continued fraction from a real number.
     */
    public static ContinuedFraction real(double x, int n) {
        IntArray terms = new IntArray(n);
        for (int k = 0; k < n; k++) {
            int a = (int)floor(x);
            terms.add(a);
            if (abs(x - a) < EPS)
                break;
            x = 1.0 / (x - a);
        }
        return make(terms.toArray(), 0);
    }

    /**
     * Create a continued fraction from the square root of an integer.
     */
    public static ContinuedFraction sqrt(int n) {
        int a0 = isqrt(n);
        if (a0 * a0 == n)
            return rational(a0);

        IntArray terms = new IntArray();
        int P = 0, Q = 1;
        int a;

        do {
            a = (a0 + P) / Q;
            P = a * Q - P;
            Q = (n - P * P) / Q;
            terms.add(a);
        } while (a != a0 * 2);
        return make(terms.toArray(), terms.size() - 1);
    }

    /**
     * Create a continued fraction from the quadratic irrational in the form
     * (a+sqrt(b))/c.
     *
     * <pre>
     * Algorithm:
     *   1. Input: (a, b, c) ∈ Z × N × Z and b not a perfect square.
     *   2. If c does not divide b - a<sup>2</sup> then multiply everything by |c|/|c|.
     *   3. Set P<sub>0</sub> = a, Q<sub>0</sub> = c
     *   4. For k ≥ 0 set
     *      (a) a<sub>k</sub> = floor((P<sub>k</sub> + sqrt(b)) / Q<sub>k</sub>)
     *      (b) P<sub>k+1</sub> = a<sub>k</sub>Q<sub>k</sub> - P<sub>k</sub>
     *      (c) Q<sub>k+1</sub> = (b - P<sub>k+1</sub><sup>2</sup>) / Q<sub>k</sub>
     *   5. Let k &lt; l be the first pair of integers which satisfy that
     *              (P<sub>k</sub>, Q<sub>k</sub>) = (P<sub>l</sub>, Q<sub>l</sub>)
     *      then the period is (a<sub>k</sub>, ..., a<sub>l-1</sub>)
     * </pre>
     */
    public static ContinuedFraction quadratic(long a, long b, long c) {
        if (b < 0) {
            // convenient input parameter for (a-sqrt(b))/c instead of
            // (a+sqrt(-b))/c which is illegal
            a = -a;
            b = -b;
            c = -c;
        }

        if ((b - a * a) % c != 0) {
            long t = abs(c);
            a *= t;
            b *= t * t;
            c *= t;
        }

        long d = isqrt(b);
        if (d * d == b) {
            return rational(a + d, c);
        }

        IntArray terms = new IntArray();
        long[] P = new long[16];
        long[] Q = new long[16];

        P[0] = a;
        Q[0] = c;

        for (int k = 0; ; k++) {
            for (int i = 1; i < k; i++) {
                if (P[k] == P[i] && Q[k] == Q[i]) {
                    return make(terms.toArray(), k - i);
                }
            }

            P = expand(P, k + 1);
            Q = expand(Q, k + 1);

            int t = toIntExact((d + P[k]) / Q[k]);
            P[k+1] = t * Q[k] - P[k];
            Q[k+1] = (b - P[k+1] * P[k+1]) / Q[k];
            terms.add(t);
        }
    }

    /**
     * Create a continued fraction from the quadratic irrational in the form
     * (a+b*sqrt(c))/d.
     */
    public static ContinuedFraction quadratic(long a, long b, long c, long d) {
        if (c < 0)
            throw new IllegalArgumentException("Not a real number");
        c *= b * b;
        if (b < 0)
            c = -c;
        return quadratic(a, c, d);
    }

    private static long[] expand(long[] terms, int len) {
        if (terms.length == len) {
            long[] tmp = new long[len * 2];
            System.arraycopy(terms, 0, tmp, 0, len);
            terms = tmp;
        }
        return terms;
    }

    /**
     * The continued fraction representation of the base of natural logarithm.
     */
    public static final ContinuedFraction E = new ContinuedFraction() {
        @Override
        public int term(int i) {
            if (i == 0)
                return 2;
            if (i % 3 == 2)
                return 2 * (i / 3) + 2;
            return 1;
        }
    };

    /**
     * The continued fraction representation of the Pi.
     */
    public static final ContinuedFraction PI = make(new int[] {
        3, 7, 15, 1, 292, 1, 1, 1, 2, 1,
        3, 1, 14, 2, 1, 1, 2, 2, 2, 2,
        1, 84, 2, 1, 1, 15, 3, 13, 1, 4,
        2, 6, 6, 99, 1, 2, 2, 6, 3, 5,
        1, 1, 6, 8, 1, 7, 1, 2, 3, 7,
        1, 2, 1, 1, 12, 1, 1, 1, 3, 1,
        1, 8, 1, 1, 2, 1, 6, 1, 1, 5,
        2, 2, 3, 1, 2, 4, 4, 16, 1, 161,
        45, 1, 22, 1, 2, 2, 1, 4, 1, 2,
        24, 1, 2, 1, 3, 1, 2, 1, 10, 2
    }, 0);
}
