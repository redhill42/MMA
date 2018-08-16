package euler;

import euler.algo.FactorizationSieve;
import euler.algo.PrimeFactor;
import euler.algo.PrimeSieve;
import euler.algo.ResidueSieve;

import static euler.algo.Library.exponent;
import static euler.algo.Library.isqrt;

public final class Problem229 {
    private Problem229() {}

    public static int solve(int limit) {
        int count = 0;

        // Find primes in the form 168k+1,25,121 and their combinations.
        // The first such prime is 193, so we need only search the primes
        // up to limit/193.
        PrimeSieve sieve = new PrimeSieve(limit / 193);
        count += search(sieve, 193, 1, limit);

        // Use a residue sieve to search for remaining primes.
        count += searchPrimes(limit);

        // Search for perfect squares conform to certain rules.
        count += searchSquares(limit);

        return count;
    }

    private static int search(PrimeSieve sieve, int p, long n, long limit) {
        int count = 0;
        for (; p > 0; p = sieve.nextPrime(p)) {
            if (!residue(p, 168, 1, 25, 121))
                continue;

            long next = n * p;
            if (next > limit)
                break;

            count += isqrt(limit / next);
            count += search(sieve, sieve.nextPrime(p), next, limit);
        }
        return count;
    }

    private static int searchPrimes(int limit) {
        int count = 0;
        for (int r : new int[]{1, 25, 121}) {
            ResidueSieve sieve = new ResidueSieve(168, r, limit/193 + 1, limit);
            for (int p; (p = (int)sieve.next()) > 0; ) {
                count += isqrt(limit / p);
            }
        }
        return count;
    }

    private static int searchSquares(int limit) {
        int L = isqrt(limit);
        FactorizationSieve sieve = new FactorizationSieve(L);
        int count = 0;

        for (int n = 60; n <= L; n++) {
            int r = exponent(n, 2);
            boolean a = false, b = false, c = false, d = false;
            for (PrimeFactor f : sieve.factorize(n >> r)) {
                int p = (int)f.prime();
                a |= residue(p, 4, 1);
                b |= residue(p, 8, 1, 3);
                c |= r >  0 || residue(p, 6, 1);
                d |= r >= 2 || residue(p, 14, 1, 9, 11);
            }
            if (a && b && c && d) {
                count++;
            }
        }
        return count;
    }

    private static boolean residue(int n, int m, int r) {
        return n % m == r;
    }

    private static boolean residue(int n, int m, int r1, int r2) {
        int a = n % m;
        return a == r1 || a == r2;
    }

    private static boolean residue(int n, int m, int r1, int r2, int r3) {
        int a = n % m;
        return a == r1 || a == r2 || a == r3;
    }

    public static void main(String[] args) {
        int n = 2_000_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
