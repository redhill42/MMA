package euler;

import euler.algo.Sublinear;
import static euler.algo.Library.isqrt;

public final class Problem184 {
    private Problem184() {}

    public static long solve(int n) {
        byte[] mu = Sublinear.moebius(n);
        long[] A = new long[n + 1];
        long[] B = new long[n + 1];
        long[] C = new long[n];

        for (int d = 1; d <= n; d++) {
            long t = isqrt(u(n, d));
            long s = t;
            for (long i = 1; i <= t; i++)
                s += isqrt(u(n, d) - i * i);
            B[d] = 4 * s;
        }

        for (int d = 1; d <= n; d++) {
            long t = isqrt(u(n, d));
            long s = 0;
            for (int j = 1; j <= t; j++)
                s += mu[j] * B[j * d];
            A[d] = s;
        }

        for (int d = 1; d < n; d++) {
            C[d] = (A[d] - A[d+1]) / 2;
        }

        long F = B[1];
        long tot = 0;
        long s = 0, s2 = 0;

        for (int i = 1; i < n; i++) {
            tot += i * C[i] * s * (F-2*i)
                 - 2 * i * C[i] * s2
                 + i*i * C[i] * (C[i]-1) * (F-4*i) / 2;
            s  += i * C[i];
            s2 += i * i * C[i];
        }

        return tot / 3;
    }

    private static long u(long n, long d) {
        return (n * n - 1) / (d * d);
    }

    public static void main(String[] args) {
        int n = 105;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
