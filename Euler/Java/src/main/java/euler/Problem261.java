package euler;

import java.util.HashSet;
import java.util.Set;

import euler.algo.FactorizationSieve;
import euler.algo.PellEquation;
import euler.algo.PrimeFactor;
import static euler.algo.Library.pow;

public final class Problem261 {
    private Problem261() {}

    public static long solve(long limit) {
        int max_m = (int)((Math.sqrt(2 * limit + 1) - 1) / 2);
        FactorizationSieve sieve = new FactorizationSieve(max_m + 1);
        Set<Long> solutions = new HashSet<>();

        for (int m = 1; m <= max_m; m++)
            solve(m, limit, sieve, solutions);
        return solutions.stream().mapToLong(x->x).sum();
    }

    private static void solve(long m, long limit, FactorizationSieve sieve, Set<Long> solutions) {
        // factor m*(m+1) into square free parts
        long p = 1;
        for (PrimeFactor f : sieve.factors((int)m))
            if (f.power() >= 2)
                p *= pow(f.prime(), f.power() / 2);
        for (PrimeFactor f : sieve.factors((int)m + 1))
            if (f.power() >= 2)
                p *= pow(f.prime(), f.power() / 2);

        // solve Pell's equation
        long d = m * (m + 1) / (p * p);
        long[] r = new long[2];
        PellEquation.solve(d, 1, r);
        long x0 = r[0], y0 = r[1];

        // recurence to find all solutions
        long a = 2 * m + 1;
        long b = 2 * m * (m + 1);
        for (;;) {
            long k = (m * a + b + m) / 2;
            long n = (m * a + a + b - m - 1) / 2;

            if (k > limit)
                break;
            if (k * (k - m) * (m + 1) == m * n * (m + n + 1))
                solutions.add(k);

            long a1 = y0 * b / p + x0 * a;
            long b1 = x0 * b + y0 * a * p * d;
            a = a1; b = b1;
        }
    }

    public static void main(String[] args) {
        long n = (long)1e10;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
