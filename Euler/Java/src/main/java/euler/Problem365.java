package euler;

import static euler.algo.Library.modinv;
import euler.algo.PrimeSieve;
import euler.util.IntArray;
import euler.util.LongRangedTask;

public final class Problem365 {
    private Problem365() {}

    public static long solve(long n, long k, int from, int to) {
        int[] primes = getPrimes(from, to);
        int[] reminders = new int[primes.length];

        for (int i = 0; i < primes.length; i++) {
            reminders[i] = lucas(n, k, primes[i]);
        }

        return LongRangedTask.parallel(0, primes.length-1, 10, (lo, hi) -> {
            long sum = 0;
            for (int a = lo; a <= hi; a++) {
                for (int b = a + 1; b < primes.length; b++) {
                    for (int c = b + 1; c < primes.length; c++) {
                        sum += chineseRemainder(
                            reminders[a], reminders[b], reminders[c],
                            primes[a], primes[b], primes[c]);
                    }
                }
            }
            return sum;
        });
    }

    private static int[] getPrimes(int from, int to) {
        PrimeSieve sieve = new PrimeSieve(to);
        IntArray primes = new IntArray();
        for (int p = sieve.nextPrime(from-1); p > 0; p = sieve.nextPrime(p))
            primes.add(p);
        return primes.toArray();
    }

    private static int lucas(long n, long k, int p) {
        int r = 1;
        while (k != 0) {
            int n1 = (int)(n % p);
            int k1 = (int)(k % p);
            if (n1 < k1)
                return 0;
            r = r * choose(n1, k1, p) % p;
            n /= p;
            k /= p;
        }
        return r;
    }

    private static int choose(int n, int k, int p) {
        int num = 1, den = 1;
        for (int i = 1; i <= k; i++) {
            num = num * (n - k + i) % p;
            den = den * i % p;
        }
        return num * (int)modinv(den, p) % p;
    }

    private static long chineseRemainder(int a, int b, int c, int p, int q, int r) {
        long res = 0;
        res += (a == 0) ? 0 : a * modinv(q * r, p) * q * r;
        res += (b == 0) ? 0 : b * modinv(p * r, q) * p * r;
        res += (c == 0) ? 0 : c * modinv(p * q, r) * p * q;
        return res % ((long)p * q * r);
    }

    public static void main(String[] args) {
        System.out.println(solve((long)1e18, (long)1e9, 1000, 5000));
    }
}
