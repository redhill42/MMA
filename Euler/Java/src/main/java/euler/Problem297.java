package euler;

import java.util.HashMap;
import java.util.Map;

public final class Problem297 {
    private Problem297() {}

    private static class Solver {
        private final long[] Fibonacci;
        private final Map<Long, Long> memo = new HashMap<>();

        Solver(long limit) {
            int n = 0;
            long a = 1, b = 2;
            while (a < limit) {
                long t = b;
                b = a + b;
                a = t;
                n++;
            }

            Fibonacci = new long[n];
            Fibonacci[0] = 1;
            Fibonacci[1] = 2;
            for (int i = 2; i < n; i++) {
                Fibonacci[i] = Fibonacci[i - 1] + Fibonacci[i - 2];
            }
        }

        public long solve(long n) {
            return memo.computeIfAbsent(n, t -> {
                for (int i = Fibonacci.length - 1; i >= 0; i--) {
                    long f = Fibonacci[i];
                    if (f < n)
                        return solve(f) + solve(n - f) + n - f;
                }
                return 0L;
            });
        }

        @SuppressWarnings("unused")
        public long zeckendorf(long n) {
            int r = 0;
            for (int i = Fibonacci.length - 1; i >= 0; i--) {
                if (Fibonacci[i] <= n) {
                    r++;
                    n -= Fibonacci[i];
                }
            }
            return r;
        }
    }

    public static long solve(long limit) {
        Solver solver = new Solver(limit);
        return solver.solve(limit);
    }

    public static void main(String[] args) {
        long n = (long)1e17;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
