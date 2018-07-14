package euler;

import java.util.BitSet;

import static euler.algo.Library.isqrt;
import static euler.algo.Library.mod;
import static euler.algo.Library.modmul;
import static euler.algo.Library.tri;

public final class Problem521 {
    private Problem521() {}

    public static long solve(long N, long m) {
        int     root    = (int)isqrt(N);
        long[]  cntA    = new long[root+1];
        long[]  sumA    = new long[root+1];
        long[]  cntB    = new long[root+1];
        long[]  sumB    = new long[root+1];
        BitSet  u       = new BitSet(root+1);
        long    res     = 0;
        int     i;

        for (i = 0; i <= root; i++) {
            cntA[i] = i - 1;
            sumA[i] = tri(i, m) - 1;
        }

        for (i = 1; i <= root; i++) {
            cntB[i] = N/i - 1;
            sumB[i] = tri(N/i, m) - 1;
        }

        for (int p = 2; p <= root; p++) {
            if (cntA[p] == cntA[p - 1])
                continue;

            long pcnt = cntA[p - 1];
            long psum = sumA[p - 1];
            long q    = (long)p * p;

            res += modmul(cntB[p] - pcnt, p, m);
            cntB[1] -= cntB[p] - pcnt;
            sumB[1] -= modmul(sumB[p] - psum, p, m);

            int step = (p & 1) + 1;
            int end  = (int)Math.min(root, N / q);
            for (i = p + step; i <= end; i += step) {
                if (u.get(i))
                    continue;

                long d = (long)i * p;
                if (d <= root) {
                    cntB[i] -= cntB[(int)d] - pcnt;
                    sumB[i] -= modmul(sumB[(int)d] - psum, p, m);
                } else {
                    int t = (int)(N / d);
                    cntB[i] -= cntA[t] - pcnt;
                    sumB[i] -= modmul(sumA[t] - psum, p, m);
                }
            }

            if (q <= root) {
                for (i = (int)q; i < end; i += p * step)
                    u.set(i);
            }

            for (i = root; i >= q-1; i--) {
                int t = i / p;
                cntA[i] -= cntA[t] - pcnt;
                sumA[i] -= modmul(sumA[t] - psum, p, m);
            }
        }

        return mod(sumB[1] + res, m);
    }

    public static void main(String[] args) {
        long n = (long)1e12, m = (long)1e9;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        if (args.length > 1)
            m = Long.parseLong(args[1]);
        System.out.println(solve(n, m));
    }
}
