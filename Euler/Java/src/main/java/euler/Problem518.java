package euler;

import euler.algo.PrimeSieve;
import static euler.algo.Library.gcd;

public final class Problem518 {
    private Problem518() {}

    public static long solve(int limit) {
        PrimeSieve sieve = new PrimeSieve(limit);
        long total = 0;

        for (int x = 2; x * x < limit; x++) {
            for (int k = 1; k * x * x < limit; k++) {
                int a = k * x * x - 1;
                if (!sieve.isPrime(a))
                    continue;
                int r = (x & 1) == 0 ? 2 : 1;
                for (int y = 1; y < x; y += r) {
                    int b = k * x * y - 1;
                    int c = k * y * y - 1;
                    if (sieve.isPrime(b) && sieve.isPrime(c) && gcd(x, y) == 1)
                        total += a + b + c;
                }
            }
        }
        return total;
    }

    public static void main(String[] args) {
        int limit = 100_000_000;
        if (args.length > 0)
            limit = Integer.valueOf(args[0]);
        System.out.println(solve(limit));
    }
}
