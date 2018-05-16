package euler.algo;

import static euler.algo.Library.modpow;

public final class TonelliShanks {
    private TonelliShanks() {}

    public static int solve(int n, int p) {
        if (n % p == 0)
            return 0;
        if (modpow(n, (p - 1) / 2, p) != 1)
            return -1; // no solution

        int s = 0, q = p - 1;
        while ((q & 1) == 0) {
            s++;
            q >>= 1;
        }

        int z = 1;
        while (modpow(z, (p - 1) / 2, p) != p - 1)
            z++;

        int c = modpow(z, q, p);
        int t = modpow(n, q, p);
        int r = modpow(n, (q + 1) / 2, p);
        int m = s;

        while (t != 1) {
            int i = 0, t1 = t;
            while (t1 != 1) {
                t1 = (int)((long)t1 * t1 % p);
                i++;
            }

            int b = modpow(c, 1 << (m - i - 1), p);
            c = (int)((long)b * b % p);
            t = (int)((long)t * c % p);
            r = (int)((long)r * b % p);
            m = i;
        }
        return r;
    }
}
