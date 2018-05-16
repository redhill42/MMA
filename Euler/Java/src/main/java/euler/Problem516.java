package euler;

import java.util.ArrayList;
import java.util.Arrays;

import static euler.algo.Library.isPrime;

public final class Problem516 {
    private Problem516() {}

    private static class Solver {
        private final long[] hamming;
        private final long[] primes;

        Solver(long limit) {
            ArrayList<Long> hamming = new ArrayList<>();
            ArrayList<Long> primes = new ArrayList<>();

            for (long a = 1; a <= limit; a *= 2)
            for (long b = 1; a*b <= limit; b *= 3)
            for (long c = 1; a*b*c <= limit; c *= 5) {
                long n = a * b * c;
                hamming.add(n);
                if (n > 5 && isPrime(n + 1)) {
                    primes.add(n + 1);
                }
            }

            this.hamming = toArray(hamming);
            this.primes = toArray(primes);
        }

        private static long[] toArray(ArrayList<Long> list) {
            long[] result = new long[list.size()];
            for (int i = 0; i < result.length; i++)
                result[i] = list.get(i);
            Arrays.sort(result);
            return result;
        }

        public long solve(long limit) {
            return search(1, 0, limit) & 0xFFFFFFFFL;
        }

        private long search(long n, int index, long limit) {
            long sum = 0;

            for (long h : hamming) {
                long next = multiplyExact(n, h);
                if (next < 0 || next > limit)
                    break;
                sum += next;
            }

            for (; index < primes.length; index++) {
                long next = multiplyExact(n, primes[index]);
                if (next < 0 || next > limit)
                    break;
                sum += search(next, index + 1, limit);
            }

            return sum;
        }

        private static long multiplyExact(long x, long y) {
            long r = x * y;
            if ((x | y) >>> 31 != 0 && r / y != x)
                return -1;
            return r;
        }
    }

    public static long solve(long limit) {
        return new Solver(limit).solve(limit);
    }

    public static void main(String[] args) {
        long n = 1_000_000_000_000L;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
