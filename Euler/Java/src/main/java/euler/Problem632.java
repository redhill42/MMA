package euler;

import java.util.Arrays;

import euler.algo.PrimeSieve;

import static euler.algo.Library.isqrtExact;
import static euler.algo.Library.modmul;
import static euler.algo.Library.nextPrime;
import static java.lang.Math.abs;

public final class Problem632 {
    private Problem632() {}

    public static long solve(long n, long m) {
        int L = (int)isqrtExact(n);
        int B = isqrtExact(L);
        int K = primorial(n);

        PrimeSieve sieve = new PrimeSieve(B);
        int[]      mu    = new int[B];
        int[]      omega = new int[B];
        long[]     S     = new long[K];
        long[][]   C     = binomials();

        long a, b;
        for (a = 1; a <= L; a = b + 1) {
            b = Math.min(a + B - 1, L);
            tabulate(sieve, mu, omega, a, b);
            for (long i = a; i <= b; i++) {
                int j = (int)(i - a);
                if (mu[j] != 0) {
                    long s = mu[j] * (n/i/i);
                    for (int k = 0; k < K; k++)
                        S[k] += s * C[omega[j]][k];
                }
            }
        }

        long res = 1;
        for (int k = 0; k < K; k++)
            res = modmul(res, abs(S[k]), m);
        return res;
    }

    private static void tabulate(PrimeSieve sieve, int[] mu, int[] omega, long a, long b) {
        long[] m = new long[(int)(b - a + 1)];
        Arrays.fill(mu, 1);
        Arrays.fill(omega, 0);
        Arrays.fill(m, 1);

        int crossto = (int)isqrtExact(b);
        for (int p = 2; p > 0 && p <= crossto; p = sieve.nextPrime(p)) {
            for (long k = (a + p - 1) / p * p; k <= b; k += p) {
                int i = (int)(k - a);
                mu[i] = -mu[i];
                omega[i]++;
                m[i] *= p;
            }

            long q = (long)p * p;
            for (long k = (a + q - 1) / q * q; k <= b; k += q) {
                int i = (int)(k - a);
                mu[i] = 0;
            }
        }

        for (long k = a; k <= b; k++) {
            int i = (int)(k - a);
            if (mu[i] != 0 && m[i] < k) {
                mu[i] = -mu[i];
                omega[i]++;
            }
        }

        if (a == 1) {
            omega[0] = 0;
        }
    }

    private static long[][] binomials() {
        long[][] C = new long[64][64];
        for (int i = 0; i < C.length; i++) {
            C[i][0] = C[i][i] = 1;
            for (int j = 1; j < i; j++)
                C[i][j] = C[i-1][j] + C[i-1][j-1];
        }
        return C;
    }

    private static int primorial(long N) {
        int k = 0;
        long x = 1, p = 2;
        while (x <= N) {
            x *= p * p;
            p = nextPrime(p);
            k++;
        }
        return k;
    }

    public static void main(String[] args) {
        System.out.println(solve((long)1e16, 1_000_000_007));
    }
}
