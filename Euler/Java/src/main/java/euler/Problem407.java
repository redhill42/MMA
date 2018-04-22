package euler;

import java.util.BitSet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import euler.util.FactorizationSieve;
import static euler.util.Utils.modinv;

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

                for (FactorizationSieve.Factor f : sieve.factors(n)) {
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
        class SolveTask extends RecursiveTask<Long> {
            private final int from, to;

            SolveTask(int from, int to) {
                this.from = from;
                this.to = to;
            }

            @Override
            public Long compute() {
                if (to - from <= 10000) {
                    return Solver.this.compute(from, to);
                } else {
                    int M = (from + to) / 2;
                    SolveTask L = new SolveTask(from, M);
                    SolveTask R = new SolveTask(M+1, to);
                    L.fork();
                    R.fork();
                    return L.join() + R.join();
                }
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
