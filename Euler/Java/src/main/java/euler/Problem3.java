package euler;

import static euler.algo.Library.isqrt;

public final class Problem3 {
    private Problem3() {}

    public static long solve(long n) {
        long lastFactor = 0;

        if (n % 2 == 0) {
            lastFactor = 2;
            do {
                n /= 2;
            } while (n % 2 == 0);
        }

        long factor = 3;
        long maxFactor = isqrt(n);
        while (n > 1 && factor <= maxFactor) {
            if (n % factor == 0) {
                lastFactor = factor;
                do {
                    n /= factor;
                } while (n % factor == 0);
                maxFactor = isqrt(n);
            }
            factor += 2;
        }

        return n == 1 ? lastFactor : n;
    }

    public static void main(String[] args) {
        long n = 600851475143L;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
