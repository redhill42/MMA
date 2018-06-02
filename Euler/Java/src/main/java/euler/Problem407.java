package euler;

import java.util.BitSet;
import java.util.concurrent.ForkJoinPool;
import euler.algo.FactorizationSieve;
import euler.algo.PrimeFactor;
import euler.util.RangedTask;
import static euler.algo.Library.modinv;

public final class Problem407 {
    private Problem407() {}

    static class Solver {
        private final FactorizationSieve sieve;
        private final BitSet primePowers;
        private final int maxPrimeFactors;

        Solver(int limit) {
            FactorizationSieve sieve = new FactorizationSieve(limit);
            BitSet primePowers = new BitSet(limit + 1);
            int maxPrimeFactors = 0;

            for (int p = 2, prod = 1; p > 0; p = sieve.nextPrime(p)) {
                for (int n = p; n <= limit / p; n *= p)
                    primePowers.set(n);
                if (p <= limit / prod) {
                    prod *= p;
                    maxPrimeFactors++;
                }
            }

            this.sieve = sieve;
            this.primePowers = primePowers;
            this.maxPrimeFactors = maxPrimeFactors;
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        long compute(int from, int to) {
            int[] sol = new int[1 << maxPrimeFactors];
            int[] newsol = new int[1 << maxPrimeFactors];
            int solen, modulus;
            long result = 0;

            for (int n = from; n <= to; n++) {
                if (primePowers.get(n)) {
                    result++;
                    continue;
                }

                sol[0] = 0;
                solen = 1;
                modulus = 1;

                for (PrimeFactor f : sieve.factors(n)) {
                    int q = f.value();
                    int recip = (int)modinv(q % modulus, modulus);
                    int newmod = q * modulus;
                    int newsolen = 0;

                    for (int i = 0; i < solen; i++) {
                        newsol[newsolen++] =
                            ((0 + (int)((long)(sol[i] - 0 + modulus) * recip % modulus) * q) % newmod);
                        newsol[newsolen++] =
                            ((1 + (int)((long)(sol[i] - 1 + modulus) * recip % modulus) * q) % newmod);
                    }

                    solen = newsolen;
                    modulus = newmod;

                    int[] t = sol;
                    sol = newsol;
                    newsol = t;
                }

                int max = 0;
                for (int i = 0; i < solen; i++)
                    max = Math.max(sol[i], max);
                result += max;
            }

            return result;
        }

        @SuppressWarnings("serial")
        class SolveTask extends RangedTask<Long> {
            SolveTask(int from, int to) {
                super(from, to);
            }

            @Override
            protected Long compute(int from, int to) {
                return Solver.this.compute(from, to);
            }

            @Override
            protected Long combine(Long v1, Long v2) {
                return v1 + v2;
            }

            @Override
            protected SolveTask fork(int from, int to) {
                return new SolveTask(from, to);
            }
        }

        public long solve(int n) {
            ForkJoinPool pool = new ForkJoinPool();
            long result = pool.invoke(new SolveTask(1, n));
            pool.shutdown();
            return result;
        }
    }

    public static long solve(int limit) {
        return new Solver(limit).solve(limit);
    }

    public static void main(String[] args) {
        System.out.println(solve(10_000_000));
    }
}
