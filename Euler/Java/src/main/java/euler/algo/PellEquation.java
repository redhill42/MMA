package euler.algo;

import java.math.BigInteger;

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
     * Solve the minimum solution of a Pell's equation. Returns the
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
     * number d/b as the parameter.
     */
    public static boolean solve(long d, int b, int c, long[] r) {
        if (solve(b * d, c, r)) {
            r[1] *= b;
            return true;
        }
        return false;
    }

    /**
     * Solve the minimum solution of a Pell's equation. Returns the
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
     * number b/d as the parameter.
     */
    public static Rational solve(long d, long b, int c) {
        Rational r = solve(b * d, c);
        if (r != null)
            r = r.divide(Rational.valueOf(b));
        return r;
    }

    /**
     * The functional interface that passed to pell equation solution
     * series. Since the solution is infinite, the function should return
     * a non-null value to terminate the series computation.
     */
    @FunctionalInterface
    public interface SeriesFunction<T> {
        /**
         * Applies this function with a pair of solution.
         *
         * @param x the x value in the solution
         * @param y the y value in the solution
         * @return the result of computation, a non-null value
         * to terminate the series.
         */
        T apply(long x, long y);
    }

    /**
     * Solve the Pell equation and process each solution with the supplied
     * function.
     */
    public static <T> T series(long d, int c, SeriesFunction<T> f) {
        long p, q, x, y;
        T z;

        long[] r = new long[2];
        if (!solve(d, c, r)) {
            throw new IllegalArgumentException("Unsolvable Pell's equation");
        }

        x = p = r[0];
        y = q = r[1];
        while ((z = f.apply(x, y)) == null) {
            long x0 = x, y0 = y;
            x = p * x0 + d * q * y0;
            y = p * y0 + q * x0;
            if (c == -1) {
                x0 = x; y0 = y;
                x = p * x0 + d * q * y0;
                y = p * y0 + q * x0;
            }
        }
        return z;
    }

    /**
     * Solve the Pell equation and process each solution with the supplied
     * function. Given a rational number d/b as the parameter.
     */
    public static <T> T series(long d, long b, int c, SeriesFunction<T> f) {
        return series(b * d, c, (x, y) -> f.apply(x, y * b));
    }
}
