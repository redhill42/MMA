package euler;

import static euler.algo.Library.mul128;
import static euler.algo.Library.nextPrime;

public final class Problem69 {
    private Problem69() {}

    public static long solve(long limit) {
        long p = 2, n = 1;
        while (mul128(n, p) <= limit) {
            n *= p;
            p = nextPrime(p);
        }
        return n;
    }

    public static void main(String[] args) {
        long n = 1_000_000;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
