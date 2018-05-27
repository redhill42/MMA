package euler;

import euler.algo.SegmentedSieve;
import static euler.algo.Library.fibonacciMod;

public final class Problem304 {
    private Problem304() {}

    private static class Fibonacci {
        private final long modulus;
        private long a, b;
        private long next;

        Fibonacci(long start, long modulus) {
            this.modulus = modulus;
            this.next = start;

            long[] fib = new long[2];
            fibonacciMod(start, modulus, fib);
            a = fib[0];
            b = fib[1];
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
