package euler;

import static euler.algo.Library.factorize;
import static euler.algo.Library.modmul;
import static euler.algo.Library.modpow;
import static euler.algo.Library.pow;

public final class Problem312 {
    private Problem312() {}

    private static long C(int ord, long n, long m) {
        if (ord == 0)
            return n % m;

        long phi = phi(m);
        long c = C(ord - 1, n, phi(phi)) - 2;
        long e = (modpow(3, c, phi) - 3) / 2;
        return 8 * modpow(12, e, m) % m;
    }

    private static long phi(long n) {
        return factorize(n).phi();
    }

    public static long solve(int ord, long n, long m) {
        return C(ord, n, m);
    }

    public static void main(String[] args) {
        long n = 10000;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(3, n, pow(13, 8)));
    }
}
