package euler;

import euler.algo.PrimeSieve;

public final class Problem35 {
    private Problem35() {}

    public static int solve(int limit) {
        PrimeSieve sieve = new PrimeSieve(limit);
        int count = 0;
        for (int p = 2; p > 0; p = sieve.nextPrime(p))
            if (isCircularPrime(sieve, p))
                count++;
        return count;
    }

    private static boolean isCircularPrime(PrimeSieve sieve, int p) {
        int k = 0, n = 1;
        for (int q = p; q >= 10; q /= 10, n *= 10)
            k++;
        for (int i = 0; i < k; i++) {
            p = n * (p % 10) + p / 10;
            if (!sieve.isPrime(p))
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000));
    }
}
