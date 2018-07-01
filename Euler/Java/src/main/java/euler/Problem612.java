package euler;

import java.math.BigInteger;
import java.util.Arrays;

import static euler.algo.Library.big;
import static euler.algo.Library.modinv;
import static euler.algo.Library.modmul;
import static euler.algo.Library.modpow;

public final class Problem612 {
    private Problem612() {}

    public static long solve(int a, long m) {
        long[] S = new long[10];
        long[] T = new long[10];
        long[] U = new long[10];

        S[1] = T[1] = 1;
        for (int i = 2; i <= a; i++) {
            for (int k = 9; k >= 1; k--) {
                S[k] = (k * S[k] + S[k-1]) % m;
                T[k] = (T[k] + S[k]) % m;
            }
        }
        for (int k = 1; k < 9; k++) {
            U[k] = k * T[k+1] % m;
        }

        long e = 0;
        for (int k1 = 1; k1 < 9; k1++) {
            for (int k2 = 1; k2 <= 9 - k1; k2++) {
                long x = 0;
                x += modmul(T[k1], T[k2], m);
                x += modmul(T[k1], U[k2], m);
                x += modmul(U[k1], T[k2], m);
                e += modmul(x, P(9, k1 + k2, m), m);
            }
        }

        long N = modpow(10, a, m);
        long all = modmul(N - 1, N - 2, m);
        return modmul(all - e, modinv(2, m), m);
    }

    private static long P(int n, int k, long m) {
        long res = 1;
        for (int i = 0; i < k; i++)
            res = res * (n - i) % m;
        return res;
    }

    public static BigInteger solve(int a) {
        BigInteger[] S = new BigInteger[10];
        BigInteger[] T = new BigInteger[10];
        BigInteger[] U = new BigInteger[10];

        Arrays.fill(S, BigInteger.ZERO);
        Arrays.fill(T, BigInteger.ZERO);
        S[1] = T[1] = BigInteger.ONE;

        for (int i = 2; i <= a; i++) {
            for (int k = 9; k >= 1; k--) {
                S[k] = big(k).multiply(S[k]).add(S[k-1]);
                T[k] = T[k].add(S[k]);
            }
        }
        for (int k = 1; k < 9; k++) {
            U[k] = big(k).multiply(T[k+1]);
        }

        BigInteger e = BigInteger.ZERO;
        for (int k1 = 1; k1 < 9; k1++) {
            for (int k2 = 1; k2 <= 9 - k1; k2++) {
                BigInteger x = BigInteger.ZERO;
                x = x.add(T[k1].multiply(T[k2]));
                x = x.add(T[k1].multiply(U[k2]));
                x = x.add(U[k1].multiply(T[k2]));
                e = e.add(x.multiply(P(9, k1 + k2)));
            }
        }

        BigInteger N = big(10).pow(a);
        BigInteger all = N.subtract(big(1)).multiply(N.subtract(big(2)));
        return all.subtract(e).shiftRight(1);
    }

    private static BigInteger P(int n, int k) {
        BigInteger res = BigInteger.ONE;
        for (int i = 0; i < k; i++)
            res = res.multiply(big(n - i));
        return res;
    }

    public static void main(String[] args) {
        int n = 18;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n, 1000267129));
    }
}
