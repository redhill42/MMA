package euler;

import java.util.BitSet;

import euler.algo.FactorizationSieve;
import euler.util.IntArray;
import euler.util.RangedTask;

import static euler.algo.Library.fibonacciMod;
import static java.lang.Math.floor;
import static java.lang.Math.log10;
import static java.lang.Math.sqrt;

public final class Problem399 {
    private Problem399() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RangedTask<IntArray> {
        private final FactorizationSieve sieve;
        private final int limit;

        SolveTask(FactorizationSieve sieve, int limit, int from, int to) {
            super(from, to, 10000);
            this.sieve = sieve;
            this.limit = limit;
        }

        @Override
        protected IntArray compute(int from, int to) {
            IntArray res = new IntArray();
            for (int p = sieve.nextPrime(from-1); p > 0 && p <= to; p = sieve.nextPrime(p)) {
                int k = fibonacciEntry(p);
                if ((long)k * p < limit)
                    res.add(k * p);
            }
            return res;
        }

        private int fibonacciEntry(int p) {
            if (p == 5)
                return 5;
            int n = (p % 5 == 1 || p % 5 == 4) ? p - 1 : p + 1;
            for (int d : sieve.divisors(n))
                if (fibonacciMod(d, p, null) == 0)
                    return d;
            return n;
        }

        @Override
        protected IntArray combine(IntArray v1, IntArray v2) {
            int[] a = new int[v1.length + v2.length];
            System.arraycopy(v1.a, 0, a, 0, v1.length);
            System.arraycopy(v2.a, 0, a, v1.length, v2.length);
            return new IntArray(a);
        }

        @Override
        protected SolveTask fork(int from, int to) {
            return new SolveTask(sieve, limit, from, to);
        }
    }

    public static String solve(int n, long m) {
        int limit = n * 2;
        int nprimes = n < 10000 ? n : n / 50;

        FactorizationSieve sieve = new FactorizationSieve(nprimes);
        IntArray entries = new SolveTask(sieve, limit, 2, nprimes).invoke();

        BitSet squarefree = new BitSet(limit);
        entries.forEach((int k) -> {
            for (int i = k; i < limit; i += k)
                squarefree.set(i);
        });

        int i, count = 0;
        for (i = 0; count < n; i++)
            if (!squarefree.get(i))
                count++;
        return format(i, m);
    }

    private static String format(long n, long m) {
        long f = fibonacciMod(n, m, null);
        double x = n * log10((1 + sqrt(5)) / 2) - log10(5) / 2;
        double e = floor(x);
        double d = Math.pow(10, x - e);
        return String.format("%d,%.1fe%d", f, d, (long)e);
    }

    public static void main(String[] args) {
        int n = 100_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n, (long)1e16));
    }
}
