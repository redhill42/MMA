package euler;

import euler.algo.FactorizationSieve;

public final class Problem26 {
    private Problem26() {}

    private static class Solver {
        private final int limit;
        private final FactorizationSieve sieve;

        Solver(int limit) {
            this.limit = limit;
            this.sieve = new FactorizationSieve(limit);
        }

        private int period(int n) {
            while (n % 2 == 0)
                n >>= 1;
            while (n % 5 == 0)
                n /= 5;
            if (n == 1)
                return 0;
            return sieve.ord(10, n);
        }

        public int solve() {
            int max_period = 0, max_n = 0;
            for (int n = 3; n < limit; n++) {
                int period = period(n);
                if (period > max_period) {
                    max_period = period;
                    max_n = n;
                }
            }
            return max_n;
        }
    }

    public static int solve(int limit) {
        return new Solver(limit).solve();
    }

    public static void main(String[] args) {
        int n = 1000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
