package euler;

import euler.algo.FactorizationSieve;

public final class Problem12 {
    private Problem12() {}

    public static int solve(int limit) {
        FactorizationSieve sieve = new FactorizationSieve(50000);
        for (int n = 1; ; n++) {
            int a, b;
            if (n % 2 == 0) {
                a = n / 2;
                b = n + 1;
            } else {
                a = n;
                b = (n + 1) / 2;
            }

            int cnt = sieve.sigma(a) * sieve.sigma(b);
            if (cnt > limit) {
                return n * (n + 1) / 2;
            }
        }
    }

    public static void main(String[] args) {
        int limit = 500;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
