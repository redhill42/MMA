package euler;

import euler.util.PrimeSieve;
import static euler.util.Utils.exponent;
import static euler.util.Utils.pow;
import static java.lang.Math.log;

public final class Problem549 {
    private Problem549() {}

    public static long solve(int limit) {
        PrimeSieve sieve = new PrimeSieve(limit);
        int[] s = new int[limit + 1];

        for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
            s[p] = p;
            for (int n = p + p; n <= limit; n += p) {
                int k = kempner(p, exponent(n, p));
                if (k > s[n])
                    s[n] = k;
            }
        }

        long ret = 0;
        for (int n = 2; n <= limit; n++)
            ret += s[n];
        return ret;
    }

    private static int kempner(int p, int a) {
        if (a == 1)
            return p;
        if (a <= p)
            return p * a;

        int k = 0, r = a;
        int j = (int)(log(1 + a * (p - 1)) / log(p));
        int s = 0;
        while (r != 0) {
            int aj = (pow(p, j) - 1) / (p - 1);
            s += k;
            k = r / aj;
            r %= aj;
            j--;
        }
        return s + k + a * (p - 1);
    }

    public static void main(String[] args) {
        System.out.println(solve(100_000_000));
    }
}
