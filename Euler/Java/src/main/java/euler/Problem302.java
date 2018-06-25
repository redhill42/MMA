package euler;

import euler.algo.Factorization;
import euler.algo.FactorizationSieve;
import euler.algo.PrimeFactor;

import static euler.algo.Library.factorize;
import static euler.algo.Library.icbrt;
import static euler.algo.Library.gcd;
import static euler.algo.Library.isqrt;
import static euler.algo.Library.pow;

public final class Problem302 {
    private Problem302() {}

    private static class Solver {
        private final long limit;
        private final FactorizationSieve sieve;

        Solver(long limit) {
            this.sieve = new FactorizationSieve(icbrt(limit / 4));
            this.limit = limit;
        }

        public long solve() {
            return search(1, factorize(1), sieve.getLimit(), 0);
        }

        private long search(long n, Factorization phi, int lastp, int g) {
            long count = 0;

            if (g == 1 && isAchilles(phi))
                count++;

            int minp = 2;
            for (PrimeFactor f : phi)
                if (f.power() == 1)
                    minp = (int)f.prime();

            int maxp = (int)isqrt(limit / n) + 1;
            if (maxp >= lastp) maxp = lastp;
            maxp = sieve.previousPrime(maxp);

            for (int p = maxp; p >= minp; p = sieve.previousPrime(p)) {
                long next = n * p;
                Factorization next_phi = phi.multiply(sieve.factorize(p - 1));

                for (int a = 2; next <= limit / p; a++) {
                    next *= p;
                    next_phi = next_phi.multiply(p, 1);
                    count += search(next, next_phi, p, gcd(g, a));
                }
            }

            return count;
        }

        private static boolean isAchilles(Factorization t) {
            int g = 0;
            for (PrimeFactor f : t) {
                if (f.power() == 1)
                    return false;
                g = gcd(g, f.power());
            }
            return g == 1;
        }
    }

    public static long solve(long limit) {
        return new Solver(limit - 1).solve();
    }

    public static void main(String[] args) {
        int e = 18;
        if (args.length > 0)
            e = Integer.parseInt(args[0]);
        System.out.println(solve(pow(10L, e)));
    }
}
