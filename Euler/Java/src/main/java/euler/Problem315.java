package euler;

import java.util.concurrent.ForkJoinPool;
import euler.util.PrimeSieve;
import euler.util.RangedTask;

public final class Problem315 {
    private Problem315() {}

    private static class Solver {
        private final byte[] digits = {
            0x7e, // (1111110)2
            0x60, // (1100000)2
            0x5b, // (1011011)2
            0x73, // (1110011)2
            0x65, // (1100101)2
            0x37, // (0110111)2
            0x3f, // (0111111)2
            0x66, // (1100110)2
            0x7f, // (1111111)2
            0x77, // (1110111)2
        };

        private final int[] digitPower = new int[10];
        private final int[][] digitPowerDiff = new int[10][10];
        private final PrimeSieve sieve;

        private static int count1(int d) {
            int c = 0;
            while (d != 0) {
                if ((d & 1) != 0)
                    c++;
                d >>= 1;
            }
            return c;
        }

        Solver(int limit) {
            for (int i = 0; i <= 9; i++)
                digitPower[i] = count1(digits[i]);
            for (int i = 0; i <= 9; i++)
                for (int j = 0; j <= 9; j++)
                    digitPowerDiff[i][j] = count1(digits[i] ^ digits[j]);
            sieve = new PrimeSieve(limit);
        }

        private int power(int n) {
            int r = 0;
            while (n != 0) {
                r += digitPower[n % 10];
                n /= 10;
            }
            return r;
        }

        private int powerDiff(int a, int b) {
            if (a < b) {
                int t = a; a = b; b = t;
            }

            int r = 0;
            while (b != 0) {
                r += digitPowerDiff[a % 10][b % 10];
                a /= 10;
                b /= 10;
            }
            return r + power(a);
        }

        private static int digitRoot(int n) {
            int r = 0;
            while (n != 0) {
                r += n % 10;
                n /= 10;
            }
            return r;
        }

        private int samClock(int n) {
            int r = 0;
            while (n >= 10) {
                r += power(n);
                n = digitRoot(n);
            }
            return 2 * (r + power(n));
        }

        private int maxClock(int n) {
            int r = power(n);
            while (n >= 10) {
                int m = digitRoot(n);
                r += powerDiff(n, m);
                n = m;
            }
            return r + power(n);
        }

        @SuppressWarnings("serial")
        class SolveTask extends RangedTask<Long> {
            SolveTask(int from, int to) {
                super(from, to, 1000);
            }

            @Override
            protected Long compute(int from, int to) {
                long ret = 0;
                for (int p = sieve.nextPrime(from-1); p > 0 && p <= to; p = sieve.nextPrime(p))
                    ret += samClock(p) - maxClock(p);
                return ret;
            }

            @Override
            protected Long combine(Long v1, Long v2) {
                return v1 + v2;
            }

            @Override
            protected SolveTask fork(int from, int to) {
                return new SolveTask(from, to);
            }
        }

        public long solve(int from, int to) {
            ForkJoinPool pool = new ForkJoinPool();
            long result = pool.invoke(new SolveTask(from, to));
            pool.shutdown();
            return result;
        }
    }

    public static long solve(int from, int to) {
        Solver solver = new Solver(to);
        return solver.solve(from, to);
    }

    public static void main(String[] args) {
        System.out.println(solve(10_000_000, 20_000_000));
    }
}
