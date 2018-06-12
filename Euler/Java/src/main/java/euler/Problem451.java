package euler;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

import euler.algo.FactorizationSieve;
import euler.algo.PrimeFactor;
import euler.util.RangedTask;
import static euler.algo.Library.chineseRemainder;
import static euler.algo.Library.even;
import static euler.algo.Library.isPowerOfTwo;

public final class Problem451 {
    private Problem451() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RangedTask<Long> {
        private final FactorizationSieve sieve;

        SolveTask(FactorizationSieve sieve, int from, int to) {
            super(from, to);
            this.sieve = sieve;
        }

        @Override
        protected Long compute(int from, int to) {
            long sum = 0;
            for (int n = from; n <= to; n++)
                sum += solve(n);
            return sum;
        }

        @Override
        protected Long combine(Long v1, Long v2) {
            return v1 + v2;
        }

        @Override
        protected RangedTask<Long> fork(int from, int to) {
            return new SolveTask(sieve, from, to);
        }

        private long solve(int n) {
            if (sieve.isPrime(n) || n == 4)
                return 1;
            if (isPowerOfTwo(n))
                return (n >> 1) + 1;

            List<PrimeFactor> factors = sieve.factors(n);
            if (factors.size() == 1)
                return 1;

            int[] gene = new int[factors.size() + 1];
            int k = 0;

            // get the generating set of square roots of unity modulo n
            for (PrimeFactor f : factors) {
                int a = (int)f.value();
                gene[k++] = (int)chineseRemainder(-1, a, 1, n / a);
            }

            // fix for even numbers
            if (even(n)) {
                int a = factors.get(0).power();
                if (a == 2) {
                    gene[0] = n / 2 + 1;
                } else if (a >= 3) {
                    gene[k++] = n / 2 + 1;
                }
            }

            // get all solutions from generating set and find largest one
            long max = 0;
            int ord = (1 << k) - 1;
            for (int mask = 1; mask < ord; mask++) {
                long prod = 1;
                for (int x = mask, i = 0; x > 0; x >>= 1, i++) {
                    if ((x & 1) != 0)
                        prod = prod * gene[i] % n;
                }
                if (prod != n - 1 && prod > max) {
                    max = prod;
                }
            }
            return max;
        }
    }

    public static long solve(int from, int to) {
        ForkJoinPool pool = new ForkJoinPool();
        FactorizationSieve sieve = new FactorizationSieve(to);
        long result = pool.invoke(new SolveTask(sieve, from, to));
        pool.shutdown();
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solve(3, 20_000_000));
    }
}
