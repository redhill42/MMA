package euler.algo;

import java.math.BigInteger;
import java.util.Iterator;

import static euler.algo.Library.isqrt;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * Pell's equation solver.
 *
 * @see <a href="http://mathworld.wolfram.com/PellEquation.html">Pell's equation</a>
 */
public final class PellEquation {
    private PellEquation() {}

    /**
     * Solve the fundamental solution of a Pell's equation. Returns the
     * solution as a fraction of long.
     */
    public static boolean solve(long d, int c, long[] r) {
        long a0 = isqrt(d);
        if (a0 * a0 == d)
            return false;

        long a = a0, P = 0, Q = 1;
        long p0 = 1, q0 = 0;
        long p = a0, q = 1;
        long p1, q1;

        for (int k = 1; ; k++) {
            // check for solution
            if (p * p - d * q * q == c) {
                r[0] = p;
                r[1] = q;
                return true;
            }

            // calculate the terms in the continued fraction
            P = a * Q - P;
            Q = (d - P * P) / Q;
            a = (a0 + P) / Q;

            // for equation x^2-dy^2=-1, there must be odd period of terms
            // in the continued fraction
            if (Q == 1 && c == -1 && k % 2 == 0)
                return false;

            // calculate convergents of the continued fraction
            p1 = p; q1 = q;
            p = a * p + p0;
            q = a * q + q0;
            p0 = p1; q0 = q1;
        }
    }

    /**
     * Solve the fundamental solution of a Pell's equation. Given a rational
     * number {@code d/b} as the parameter.
     */
    public static boolean solve(long d, int b, int c, long[] r) {
        if (solve(b * d, c, r)) {
            r[1] *= b;
            return true;
        }
        return false;
    }

    /**
     * Solve the fundamental solution of a Pell's equation. Returns the
     * solution as a fraction of BigInteger. Returns null if no solution
     * found.
     */
    public static Rational solve(long d, int c) {
        long a0 = isqrt(d);
        if (a0 * a0 == d)
            return null;

        long a = a0, P = 0, Q = 1;
        BigInteger p0 = ONE, q0 = ZERO;
        BigInteger p = BigInteger.valueOf(a0);
        BigInteger q = ONE;
        BigInteger p1, q1;
        BigInteger D = BigInteger.valueOf(d);
        BigInteger C = BigInteger.valueOf(c);

        for (int k = 1; ; k++) {
            // check for solution
            BigInteger t = p.multiply(p).subtract(D.multiply(q).multiply(q));
            if (t.compareTo(C) == 0) {
                return Rational.valueOf(p, q);
            }

            // calculate the terms in the continued fraction
            P = a * Q - P;
            Q = (d - P * P) / Q;
            a = (a0 + P) / Q;

            // for equation x^2-dy^2=-1, there must be odd period of terms
            // in the continued fraction
            if (Q == 1 && c == -1 && k % 2 == 0)
                return null;

            // calculate convergents of the continued fraction
            p1 = p; q1 = q;
            p = p.multiply(BigInteger.valueOf(a)).add(p0);
            q = q.multiply(BigInteger.valueOf(a)).add(q0);
            p0 = p1; q0 = q1;
        }
    }

    /**
     * Solve the fundamental solution of a Pell's equation. Given a rational
     * number {@code b/d} as the parameter.
     */
    public static Rational solve(long d, long b, int c) {
        Rational r = solve(b * d, c);
        if (r != null)
            r = r.divide(Rational.valueOf(b));
        return r;
    }

    /**
     * Solve the Pell equation and returns the collection of all solutions.
     */
    public static Iterable<Pair> series(long d, int c) {
        return series(d, 1, c);
    }

    /**
     * Solve the Pell equation and returns the collection of all solutions.
     * Given a rational number {@code d/b} as the parameter.
     */
    public static Iterable<Pair> series(long d, long b, int c) {
        long[] r = new long[2];
        if (!solve(b * d, c, r)) {
            throw new IllegalArgumentException("Unsolvable Pell's equation");
        }

        return new Iterable<Pair>() {
            @Override
            public Iterator<Pair> iterator() {
                return new SeriesIterator(r[0], r[1], b * d, b, c);
            }
        };
    }

    private static class SeriesIterator implements Iterator<Pair> {
        private final long p, q;
        private final long d, b;
        private final int c;
        private final Pair pair = new Pair();
        private long x, y;

        SeriesIterator(long p, long q, long d, long b, int c) {
            this.p = this.x = p;
            this.q = this.y = q;
            this.d = d;
            this.b = b;
            this.c = c;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        @SuppressWarnings("IteratorNextCanNotThrowNoSuchElementException")
        public Pair next() {
            long x0 = pair.x = this.x;
            long y0 = pair.y = this.y;

            x = p * x0 + d * q * y0;
            y = p * y0 + q * x0;
            if (c == -1) {
                x0 = x; y0 = y;
                x = p * x0 + d * q * y0;
                y = p * y0 + q * x0;
            }

            pair.y *= b;
            return pair;
        }
    }
}
