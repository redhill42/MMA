package euler;

import static euler.algo.Library.pow;

public final class Problem288 {
    private Problem288() {}

    public static long solve(long prime, int limit, int exponent) {
        long m = pow(prime, exponent);
        long s = 290797;
        long x = 0;
        long r = 0;

        for (int n = 0; n <= limit; n++) {
            long t = s % prime;
            r = (r + t * x) % m;
            if (n < exponent)
                x = x * prime + 1;
            s = s * s % 50515093;
        }
        return r;
    }

    public static void main(String[] args) {
        System.out.println(solve(3, 10000, 20));
        System.out.println(solve(61, 10_000_000, 10));
    }
}
