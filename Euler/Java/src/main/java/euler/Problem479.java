package euler;

import static euler.algo.Library.modinv;
import static euler.algo.Library.modmul;
import static euler.algo.Library.modpow;

public final class Problem479 {
    private Problem479() {}

    public static long solve(int n, long p) {
        long s = 0;
        for (long k = 1; k <= n; k++) {
            long t = 1 - modpow(1 - k * k, n + 1, p);
            t = modmul(t, modinv(k * k, p), p);
            s = (s + t - 1) % p;
        }
        return s;
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000, 1_000_000_007));
    }
}
