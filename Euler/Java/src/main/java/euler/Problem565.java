package euler;

import java.util.BitSet;

import euler.algo.PrimeSieve;
import euler.util.LongArray;

import static euler.algo.Library.isqrt;
import static euler.algo.Library.modinv;
import static euler.algo.Library.pow;

public final class Problem565 {
    private Problem565() {}

    public static long solve(long limit, int d) {
        int sqrt_n = (int)isqrt(limit);
        PrimeSieve sieve = new PrimeSieve(sqrt_n);
        LongArray candidates = new LongArray();
        long sum = 0;

        // handle primes less than sqrt(n)
        for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
            int a = ord(p, d, limit);
            if (a == 0)
                continue;

            // add multiples of p^a
            long n = pow(p, a);
            sum += sum(n, limit);

            // subtract multiples of p^(a+1)
            if (n * p <= limit)
                sum -= sum(n * p, limit);

            // subtract multiples of p^a and other candidates
            for (int i = 0; i < candidates.length; i++) {
                if (n * candidates.a[i] <= limit)
                    sum -= sum(n * candidates.a[i], limit);
            }

            // add p^a to candidates
            candidates.add(n);
        }

        candidates.sort();

        // sieve primes of the form kd-1
        BitSet composite = new BitSet((int)((limit + 1) / d + 1));
        for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
            if (p != d) {
                for (long k = modinv(d, p); k * d - 1 <= limit; k += p) {
                    if (k * d - 1 > p)
                        composite.set((int)k);
                }
            }
        }

        // handle primes greater than sqrt(n)
        for (int k = composite.nextClearBit(1); k > 0; k = composite.nextClearBit(k+1)) {
            long p = (long)k * d - 1;
            if (p <= sqrt_n)
                continue;
            if (p > limit)
                break;

            // same inclusion-exclusion as before
            sum += sum(p, limit);
            for (int i = 0; i < candidates.length; i++) {
                long n = p * candidates.a[i];
                if (n > limit)
                    break;
                sum -= sum(n, limit);
            }
        }

        return sum;
    }

    private static int ord(long p, int d, long limit) {
        long s = p + 1, t = p;
        int a = 1;
        do {
            if (s % d == 0)
                return a;
            t *= p;
            s += t;
            a++;
        } while (t <= limit);
        return 0;
    }

    private static long sum(long n, long limit) {
        long k = limit / n;
        return k * (k + 1) / 2 * n;
    }

    public static void main(String[] args) {
        long n = (long)1e11;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n, 2017));
    }
}
