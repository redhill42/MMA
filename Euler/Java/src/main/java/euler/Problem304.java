package euler;

import euler.util.SegmentedSieve;
import static euler.util.Utils.modmul;

public final class Problem304 {
    private Problem304() {}

    private static class Fibonacci {
        private final long modulus;
        private long a, b;
        private long next;

        Fibonacci(long start, long modulus) {
            this.modulus = modulus;
            this.next = start;

            long[] matrix = matrixPowerMod(start, modulus);
            a = matrix[3];
            b = matrix[2];
        }

        long next(long n) {
            while (next < n) {
                long t = (a + b) % modulus;
                a = b;
                b = t;
                next++;
            }
            return a;
        }

        private static long[] matrixPowerMod(long n, long m) {
            long a = 1, b = 1, c = 1, d = 0;
            long x = 1, y = 1, z = 1, w = 0;

            for (; n != 0; n >>= 1) {
                if ((n & 1) != 0) {
                    long x1 = (modmul(a, x, m) + modmul(c, y, m)) % m;
                    long y1 = (modmul(b, x, m) + modmul(d, y, m)) % m;
                    long z1 = (modmul(c, w, m) + modmul(a, z, m)) % m;
                    long w1 = (modmul(d, w, m) + modmul(b, z, m)) % m;
                    x = x1; y = y1; z = z1; w = w1;
                }

                long a1 = (modmul(a, a, m) + modmul(c, b, m)) % m;
                long b1 = (modmul(b, a, m) + modmul(d, b, m)) % m;
                long c1 = (modmul(a, c, m) + modmul(c, d, m)) % m;
                long d1 = (modmul(b, c, m) + modmul(d, d, m)) % m;
                a = a1; b = b1; c = c1; d = d1;
            }
            return new long[] {x, y, z, w};
        }
    }

    public static long solve(long start, int limit, long modulus) {
        SegmentedSieve sieve = new SegmentedSieve(start * 10);
        SegmentedSieve.Segment segment = sieve.segment(start);
        Fibonacci fibs = new Fibonacci(start, modulus);
        long sum = 0;

        for (int i = 0; i < limit; i++) {
            long fib = fibs.next(segment.next());
            sum = (sum + fib) % modulus;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solve((long)1e14, 100000, 1234567891011L));
    }
}
