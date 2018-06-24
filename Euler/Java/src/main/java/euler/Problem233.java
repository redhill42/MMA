package euler;

import java.util.ArrayList;
import java.util.List;
import euler.algo.PrimeSieve;

import static euler.algo.Library.mul128;

public final class Problem233 {
    private Problem233() {}

    private static class Solver {
        private final long limit;
        private final int[] primes;
        private final int[] salts;

        Solver(long limit) {
            this.limit = limit;

            PrimeSieve sieve = new PrimeSieve((int)(limit / 21125)); // 5^3*13^2
            List<Integer> primes = new ArrayList<>();
            List<Integer> salts = new ArrayList<>();

            for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
                if (p % 4 == 1)
                    primes.add(p);
                else
                    salts.add(p);
            }

            this.primes = primes.stream().mapToInt(x->x).toArray();
            this.salts = salts.stream().mapToInt(x->x).toArray();
        }

        public long solve() {
            long a, b, c;
            long sum = 0;

            // search n in the form p^3*q^2*r, where p, q, r are the primes
            // in the form 4k+1
            for (int i = 0; i < primes.length; i++) {
                if ((a = mult(1, primes[i], 3)) > limit)
                    break;
                for (int j = 0; j < primes.length; j++) {
                    if (j == i)
                        continue;
                    if ((b = mult(a, primes[j], 2)) > limit)
                        break;
                    for (int k = 0; k < primes.length; k++) {
                        if (k == i || k == j)
                            continue;
                        if ((c = mult(b, primes[k], 1)) > limit)
                            break;
                        sum += search(c);
                    }
                }
            }

            // search n in the form p^10*q^2, where p and q are the primes
            // in the form 4k+1
            for (int i = 0; i < primes.length; i++) {
                if ((a = mult(1, primes[i], 10)) > limit)
                    break;
                for (int j = 0; j < primes.length; j++) {
                    if (j == i)
                        continue;
                    if ((b = mult(a, primes[j], 2)) > limit)
                        break;
                    sum += search(b);
                }
            }

            // search n in the form p^7*q^3, where p and q are the primes
            // in the form 4k+1
            for (int i = 0; i < primes.length; i++) {
                if ((a = mult(1, primes[i], 7)) > limit)
                    break;
                for (int j = 0; j < primes.length; j++) {
                    if (j == i)
                        continue;
                    if ((b = mult(a, primes[j], 3)) > limit)
                        break;
                    sum += search(b);
                }
            }

            return sum;
        }

        private long search(long n) {
            return n + search(n, 0);
        }

        private long search(long n, int i) {
            long sum = 0;
            for (; i < salts.length; i++) {
                long next = mul128(n, salts[i]);
                if (next > limit)
                    break;
                while (next <= limit) {
                    sum += next + search(next, i + 1);
                    next = mul128(next, salts[i]);
                }
            }
            return sum;
        }

        private long mult(long a, long b, int n) {
            for (int i = 1; i <= n && a <= limit; i++)
                a = mul128(a, b);
            return a;
        }
    }

    public static long solve(long limit) {
        return new Solver(limit).solve();
    }

    public static void main(String[] args) {
        long n = (long)1e11;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
