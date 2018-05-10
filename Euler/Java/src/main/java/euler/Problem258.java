package euler;

import static euler.util.Utils.modmul;

public final class Problem258 {
    private Problem258() {}

    private static void polymul(long[] x, long[] y, long[] z, long M) {
        int n = x.length;
        long[] t = new long[n * 2];

        // t = x * y
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                t[i + j] += modmul(x[i], y[j], M);

        // t = t % (x^n - x - 1)
        for (int i = n * 2 - 1; i >= n; i--) {
            t[i - n    ] += t[i];
            t[i - n + 1] += t[i];
        }

        // z = t
        for (int i = 0; i < n; i++) {
            z[i] = t[i] % M;
        }
    }

    public static long solve(int n, long index, long M) {
        long[] t = new long[n], b = new long[n];
        b[1] = t[0] = 1;

        for (long e = index; e != 0; e >>= 1) {
            if ((e & 1) != 0)
                polymul(t, b, t, M);
            polymul(b, b, b, M);
        }

        long res = 0;
        for (int i = 0; i < n; i++)
            res += t[i];
        return res % M;
    }

    public static void main(String[] args) {
        System.out.println(solve(2000, (long)1e18, 20092010));
    }
}
