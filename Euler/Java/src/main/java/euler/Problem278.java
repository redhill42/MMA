package euler;

import euler.algo.PrimeSieve;

public final class Problem278 {
    private Problem278() {}

    public static long solve(int limit) {
        PrimeSieve sieve = new PrimeSieve(limit);
        long a = 0, b = 0, c = 0, s = 0, i = 0;
        for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
            s += c * p - b;
            c += (2 * a - i) * p - a;
            b += a * p;
            a += p;
            i += 1;
        }
        return s;
    }

    public static void main(String[] args) {
        int n = 5000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
