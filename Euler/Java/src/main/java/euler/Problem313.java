package euler;

import euler.algo.PrimeSieve;

public final class Problem313 {
    private Problem313() {}

    public static long solve(int limit) {
        PrimeSieve sieve = new PrimeSieve(limit);
        long sum = 0;
        for (int p = 3; p > 0; p = sieve.nextPrime(p))
            sum += ((long)p*p - 1) / 12;
        return sum + 2;
    }

    public static void main(String[] args) {
        int n = 1_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
