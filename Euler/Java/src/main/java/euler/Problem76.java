package euler;

import euler.util.FactorizationSieve;

public final class Problem76 {
    private Problem76() {}

    public static long solve(int n) {
        FactorizationSieve sieve = new FactorizationSieve(n);
        long[] p = new long[n + 1];

        p[0] = 1;
        for (int i = 1; i <= n; i++) {
            long s = 0;
            for (int k = 0; k < i; k++)
                s += sieve.sigma(1, i - k) * p[k];
            p[i] = s / i;
        }
        return p[n];
    }

    public static void main(String[] args) {
        int n = 100;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n) - 1);
    }
}
