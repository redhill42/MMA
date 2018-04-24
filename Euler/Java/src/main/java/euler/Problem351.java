package euler;

import euler.util.TotientSieve;

public final class Problem351 {
    private Problem351() {}

    public static long solve(int n) {
        TotientSieve sieve = new TotientSieve(n);
        long result = (long)n * (n + 1) / 2;
        for (int i = 1; i <= n; i++)
            result -= sieve.phi(i);
        return result * 6;
    }

    public static void main(String[] args) {
        int n = 100_000_000;
        if (args.length > 0)
            n = Integer.valueOf(args[0]);
        System.out.println(solve(n));
    }
}
