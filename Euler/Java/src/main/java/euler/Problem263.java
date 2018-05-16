package euler;

import euler.algo.SegmentedSieve;
import static euler.algo.Library.isqrt;

public final class Problem263 {
    private Problem263() {}

    private static final long LIMIT = 10_000_000_000L;

    public static long solve(int count) {
        SegmentedSieve sieve = new SegmentedSieve(LIMIT);
        SegmentedSieve.Segment primes = sieve.segment();

        long p1 = primes.next();
        long p2 = primes.next();
        long p3 = primes.next();
        long p4;
        long sum = 0;

        for (; (p4 = primes.next()) > 0 && count > 0; p1 = p2, p2 = p3, p3 = p4) {
            if (p4 - p3 != 6 || p3 - p2 != 6 || p2 - p1 != 6)
                continue;

            long n = p4 - 9;
            if (isPracticalNumber(n) &&
                isPracticalNumber(n - 4) && isPracticalNumber(n + 4) &&
                isPracticalNumber(n - 8) && isPracticalNumber(n + 8))
            {
                System.out.println(n);
                sum += n;
                count--;
            }
        }
        return sum;
    }

    private static boolean isPracticalNumber(long n) {
        if ((n & 1) != 0)
            return false;

        long max_p = isqrt(n);
        long divisor = divisor(n, 2);
        long sigma = (divisor << 1) - 1;
        n /= divisor;

        if (n % 3 == 0) {
            if (3 > 1 + sigma)
                return false;
            divisor = divisor(n, 3);
            sigma *= (divisor * 3 - 1) / 2;
            n /= divisor;
        }

        for (long p = 5; p <= max_p; p += 6) {
            if (n % p == 0) {
                if (p > 1 + sigma)
                    return false;
                divisor = divisor(n, p);
                sigma *= (divisor * p - 1) / (p - 1);
                n /= divisor;
            }

            long q = p + 2;
            if (n % q == 0) {
                if (q > 1 + sigma)
                    return false;
                divisor = divisor(n, q);
                sigma *= (divisor * q - 1) / (q - 1);
                n /= divisor;
            }
        }

        return n <= 1 + sigma;
    }

    private static long divisor(long n, long p) {
        long d = 1;
        if (p == 2) {
            while ((n & 1) == 0) {
                n >>= 1;
                d <<= 1;
            }
        } else {
            while (n % p == 0) {
                n /= p;
                d *= p;
            }
        }
        return d;
    }

    public static void main(String[] args) {
        System.out.println(solve(4));
    }
}
