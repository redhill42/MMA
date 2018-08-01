package euler;

import static euler.algo.Library.isqrt;

public final class Problem291 {
    private Problem291() {}

    public static long solve(long limit) {
        int    L = (int)isqrt(limit / 2);
        long[] T = new long[L + 1];
        long   cnt = 0;

        T[0] = 1;
        for (int n = 1, m = 4; n <= L; n++, m += 4) {
            T[n] = T[n-1] + m;
        }

        for (int n = 1; n <= L; n++) {
            if (T[n] == 1)
                continue;

            long p = T[n];
            if (p == 2L * n * (n+1) + 1)
                cnt++;

            for (long i = n; i <= L; i += p)
                while (T[(int)i] % p == 0)
                    T[(int)i] /= p;
            for (long i = p - n - 1; i <= L; i += p)
                while (T[(int)i] % p == 0)
                    T[(int)i] /= p;
        }

        return cnt;
    }

    public static void main(String[] args) {
        long n = (long)5e15;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
