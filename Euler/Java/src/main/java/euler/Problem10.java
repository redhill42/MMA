package euler;

import euler.algo.PrimeSieve;

public final class Problem10 {
    private Problem10() {}

    public static long solve(int limit) {
        PrimeSieve sieve = new PrimeSieve(limit - 1);
        long sum = 0;
        for (int p = 2; p > 0; p = sieve.nextPrime(p))
            sum += p;
        return sum;
    }

    public static void main(String[] args) {
        int limit = 2_000_000;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
