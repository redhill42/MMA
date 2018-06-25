package euler.algo;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.LongBinaryOperator;
import java.util.function.Predicate;

import euler.util.IntArray;

import static euler.algo.Library.isqrt;
import static java.lang.Math.abs;
import static java.lang.Math.floor;

public abstract class ContinuedFraction {
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
     * Convergent on the continued fraction. Passing every partial
     * rational number to the given operator. The operator should
     * return -1 to continue operate on convergence. Return -1 to
     * terminate the opreation.
     */
    public long convergents(LongBinaryOperator op) {
        return convergents(Integer.MAX_VALUE, op);
    }

    /**
     * Convergent on the continued fraction with the maximum length.
     */
    public long convergents(int n, LongBinaryOperator op) {
        if (!isPeriodic() && length() > 0 && n > length())
            n = length();

        long p0 = 0, q0 = 1;
        long p1 = 1, q1 = 0;
        long r = -1;

        for (int i = 0; i < n && r == -1; i++) {
            int a = term(i);
            long p = a * p1 + p0;
            long q = a * q1 + q0;
            r = op.applyAsLong(p, q);
            p0 = p1; q0 = q1;
            p1 = p;  q1 = q;
        }
        return r;
    }

    /**
     * Convergent on the continued fraction. Passing every partial rational
     * rational number to the given operator. The operator should
     * return -1 to continue operate on convergence. Return -1 to
     * terminate the opreation.
     */
    public void convergents(Predicate<Rational> op) {
        convergents(Integer.MAX_VALUE, op);
    }

    /**
     * Convergent on the continued fraction with the maximum length.
     */
    public void convergents(int n, Predicate<Rational> op) {
        if (!isPeriodic() && length() > 0 && n > length())
            n = length();

        BigInteger p0 = BigInteger.ZERO;
        BigInteger q0 = BigInteger.ONE;
        BigInteger p1 = BigInteger.ONE;
        BigInteger q1 = BigInteger.ZERO;

        for (int i = 0; i < n; i++) {
            BigInteger a = BigInteger.valueOf(term(i));
            BigInteger p = a.multiply(p1).add(p0);
            BigInteger q = a.multiply(q1).add(q0);
            if (!op.test(Rational.valueOf(p, q)))
                break;
            p0 = p1; q0 = q1;
            p1 = p;  q1 = q;
        }
    }

    /**
     * Returns the n'th convergent value.
     */
    public Rational convergent(int n) {
        if (!isPeriodic() && length() > 0 && n > length())
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
}
