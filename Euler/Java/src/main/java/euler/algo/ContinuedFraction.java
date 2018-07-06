package euler.algo;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

import euler.util.IntArray;

import static euler.algo.Library.even;
import static euler.algo.Library.isqrt;
import static euler.algo.Library.mul128;
import static java.lang.Math.abs;
import static java.lang.Math.floor;

@SuppressWarnings({"unused", "RedundantFieldInitialization"})
public abstract class ContinuedFraction {
    /**
     * Returns true if this is a infinite continued fraction, false otherwise.
     */
    public boolean isInfinite() {
        return isPeriodic() || length() < 0;
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
        if (!isInfinite() && n > length())
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
        if (!isInfinite() && n > length())
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

        int n = !isInfinite() ? length() : Integer.MAX_VALUE;
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
                    return i < n && p1 < Long.MAX_VALUE && q1 < Long.MAX_VALUE;
                }

                @Override
                public Ratio next() {
                    if (!hasNext()) throw new NoSuchElementException();

                    Ratio next = new Ratio(p1, q1);

                    if (++i < n) {
                        int a = term(i);

                        long p = mul128(a, p1);
                        if (p <= Long.MAX_VALUE - p0)
                            p += p0;
                        else
                            p = Long.MAX_VALUE;

                        long q = mul128(a, q1);
                        if (q <= Long.MAX_VALUE - q0)
                            q += q0;
                        else
                            q = Long.MAX_VALUE;

                        p0 = p1; q0 = q1;
                        p1 = p;  q1 = q;
                    }

                    return next;
                }
            };
        }

        @Override
        public void forEach(Predicate<? super Ratio> action) {
            long p0 = 0, q0 = 1;
            long p1 = 1, q1 = 0;

            for (int i = 0; i < n; i++) {
                int a = term(i);

                long p = mul128(a, p1);
                if (p > Long.MAX_VALUE - p0)
                    break;
                p += p0;

                long q = mul128(a, q1);
                if (q > Long.MAX_VALUE - q0)
                    break;
                q += q0;

                p0 = p1; q0 = q1;
                p1 = p;  q1 = q;

                if (!action.test(new Ratio(p1, q1)))
                    break;
            }
        }

        @Override
        public Ratio get(int n) {
            if (!isInfinite() && n > length())
                n = length();

            long p0 = 0, q0 = 1;
            long p1 = 1, q1 = 0;

            for (int i = 0; i < n; i++) {
                long a = term(i);
                long p = a * p1 + p0;
                long q = a * q1 + q0;
                p0 = p1; q0 = q1;
                p1 = p;  q1 = q;
            }
            return new Ratio(p1, q1);
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
            if (!isInfinite() && n > length())
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
                    if (!hasNext()) throw new NoSuchElementException();

                    Ratio next = new Ratio(p, q);

                    if (++k > term(i)) {
                        p0 = p1; q0 = q1;
                        p1 = p;  q1 = q;

                        if (!isInfinite() && i+1 >= length()) {
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

            int n = !isInfinite() ? length() : Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                int a = term(i);
                int k = (a + 1) / 2;
                if (even(a) && !allowHalf(i))
                    k++;

                while (k <= a) {
                    p = k * p1 + p0;
                    q = k * q1 + q0;
                    if (q > bound || !action.test(new Ratio(p, q)))
                        return;
                    k++;
                }

                p0 = p1; q0 = q1;
                p1 = p;  q1 = q;
            }
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


        public String toString() {
            StringBuilder buf = new StringBuilder();
            int i;

            buf.append("[");
            buf.append(terms[0]);
            if (terms.length > 1)
                buf.append(";");

            for (i = 1; i < terms.length - period; i++) {
                buf.append(terms[i]);
                if (i < terms.length - 1)
                    buf.append(",");
            }

            if (i < terms.length) {
                buf.append("{");
                for (; i < terms.length; i++) {
                    buf.append(terms[i]);
                    if (i < terms.length - 1)
                        buf.append(",");
                }
                buf.append("}");
            }

            buf.append("]");
            return buf.toString();
        }

        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof DefaultContinuedFraction))
                return false;
            DefaultContinuedFraction other = (DefaultContinuedFraction)obj;
            return period == other.period && Arrays.equals(terms, other.terms);
        }

        public int hashCode() {
            return period ^ Arrays.hashCode(terms);
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
    public static ContinuedFraction rational(int a, int b) {
        IntArray terms = new IntArray();
        int t;

        while (b != 0) {
            terms.add(a / b);
            t = a % b;
            a = b;
            b = t;
        }
        return make(terms.toArray(), 0);
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
            return make(new int[]{a0}, 0);

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
    public static ContinuedFraction quadratic(int a, int b, int c) {
        if (b < 0) {
            // convenient input parameter for (a-sqrt(b))/c instead of
            // (a+sqrt(-b))/c which is illegal
            a = -a;
            b = -b;
            c = -c;
        }

        if ((b - a * a) % c != 0) {
            int t = abs(c);
            a *= t;
            b *= t * t;
            c *= t;
        }

        int d = isqrt(b);
        if (d * d == b) {
            return rational(a + d, c);
        }

        int[] A = new int[16];
        int[] P = new int[16];
        int[] Q = new int[16];

        P[0] = a;
        Q[0] = c;

        for (int k = 0; ; k++) {
            for (int i = 1; i < k; i++) {
                if (P[k] == P[i] && Q[k] == Q[i]) {
                    return make(Arrays.copyOf(A, k), k - i);
                }
            }

            A = expand(A, k);
            P = expand(P, k + 1);
            Q = expand(Q, k + 1);

            A[k] = (d + P[k]) / Q[k];
            P[k+1] = A[k] * Q[k] - P[k];
            Q[k+1] = (b - P[k+1] * P[k+1]) / Q[k];
        }
    }

    /**
     * Create a continued fraction from the quadratic irrational in the form
     * (a+b*sqrt(c))/d.
     */
    public static ContinuedFraction quadratic(int a, int b, int c, int d) {
        if (c < 0)
            throw new IllegalArgumentException("Not a real number");
        c *= b * b;
        if (b < 0)
            c = -c;
        return quadratic(a, c, d);
    }

    private static int[] expand(int[] terms, int len) {
        if (terms.length == len) {
            int[] tmp = new int[len * 2];
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

        public String toString() {
            return "[2;1,2,1,1,4,1,1,6,1,...]";
        }

        public boolean equals(Object o) {
            return this == o;
        }

        public int hashCode() {
            return System.identityHashCode(this);
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
