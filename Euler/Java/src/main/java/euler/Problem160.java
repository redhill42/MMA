package euler;

import static euler.algo.Library.modmul;
import static euler.algo.Library.modpow;

public final class Problem160 {
    private Problem160() {}

    private static final int M = 1000000;

    public static long solve(long a) {
        long r = 1;
        while (a >= 50) {
            int b = (int)(a % 50);
            a /= 50;
            r = modmul(r, modpow(364224, a, M), M);
            r = modmul(r, tail(a, b), M);
            a *= 10;
        }
        return modmul(r, tail(0, (int)a), M);
    }

    private static long tail(long a, int b) {
        long r = 1;
        a = (a % M) * 50 + 1;
        for (int i = 1; i <= b; i++, a++) {
            r *= a;
            while (r % 10 == 0)
                r /= 10;
            r %= M;
        }
        return r;
    }

    public static void main(String[] args) {
        long n = 1_000_000_000_000L;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n) % 100000);
    }
}
