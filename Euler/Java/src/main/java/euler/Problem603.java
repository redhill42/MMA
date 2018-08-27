package euler;

import euler.algo.PrimeCounter;
import euler.algo.SegmentedSieve;
import euler.util.IntArray;

import static euler.algo.Library.modinv;
import static euler.algo.Library.modmul;
import static euler.algo.Library.modpow;
import static euler.algo.Library.reverse;
import static euler.algo.Library.tri;

public final class Problem603 {
    private Problem603() {}

    public static long solve(int n, long k, long m) {
        IntArray digits = buildDigits(n);
        int N = digits.length;
        long s1 = 0, s2 = 0, s3 = 0, s4 = 0;
        long t = 1;

        for (int i = 0; i < N; i++) {
            int d = digits.a[N - i - 1];
            s1 += d;
            s2 += i * d % m;
            s3 += t * d % m;
            s4 += i * t * d % m;
            t = t * 10 % m;
        }

        long pow_N  = modpow(10, N, m);
        long pow_kN = modpow(pow_N, k, m);
        long res = 0;

        t = modmul(tri(k, m), N, m);
        t = modmul(t, s1, m);
        res -= t;

        res += modmul(k, s2, m);

        t = pow_kN - k + modmul(pow_kN - pow_N, modinv(pow_N - 1, m), m);
        t = modmul(t, modinv(pow_N - 1, m), m);
        t = modmul(t, 10 * N, m);
        t = modmul(t, s3, m);
        res += t;

        t = modmul(pow_kN - 1, modinv(pow_N - 1, m), m) * 10;
        t = modmul(t, s4, m);
        res -= t;

        return modmul(res, modinv(9, m), m);
    }

    private static IntArray buildDigits(int n) {
        SegmentedSieve sieve = new SegmentedSieve(PrimeCounter.approxPrime(n));
        SegmentedSieve.Segment segment = sieve.segment();
        IntArray digits = new IntArray();
        long p;

        for (int i = 0; i < n && (p = segment.next()) > 0; i++) {
            long q = reverse(p);
            while (q > 0) {
                digits.add((int)(q % 10));
                q /= 10;
            }
        }
        return digits;
    }

    public static void main(String[] args) {
        System.out.println(solve((int)1e6, (long)1e12, 1_000_000_007));
    }
}
