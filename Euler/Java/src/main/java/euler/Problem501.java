package euler;

import euler.util.PrimeCounter;
import euler.util.PrimeSieve;
import static euler.util.Utils.isqrt;

public final class Problem501 {
    private Problem501() {}

    private static class Solver {
        private final long limit;
        private final PrimeSieve sieve;
        private final PrimeCounter counter;

        Solver(long limit) {
            this.limit = limit;
            this.sieve = new PrimeSieve((int)isqrt(limit / 2));
            this.counter = new PrimeCounter(limit);
        }

        public long solve() {
            long count = 0;

            // count n in the form a*b*c where a, b, c are primes
            for (int i = 0, a = 2; a > 0; i++, a = sieve.nextPrime(a)) {
                if ((long)a * a * a > limit)
                    break;
                for (int j = i+1, b = sieve.nextPrime(a); b > 0; j++, b = sieve.nextPrime(b)) {
                    long c = limit / a / b;
                    if (c <= b)
                        break;
                    count += counter.countPrimes(c) - j - 1;
                }
            }

            // count n in the form p^3*q, where p, q are primes
            for (int a = 2; a > 0; a = sieve.nextPrime(a)) {
                long b = limit / a / a / a;
                if (b < 2)
                    break;
                long n = counter.countPrimes(b);
                count += b >= a ? n - 1 : n;
            }

            // count n in the form p^7, where p is prime
            count += counter.countPrimes((long)Math.pow(limit, 1.0/7));

            return count;
        }
    }

    public static long solve(long limit) {
        return new Solver(limit).solve();
    }

    public static void main(String[] args) {
        System.out.println(solve(100));
        System.out.println(solve(1000));
        System.out.println(solve(1_000_000));
        System.out.println(solve(1_000_000_000_000L));
    }
}
