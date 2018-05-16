package euler;

import java.util.Arrays;

import euler.algo.PrimeSieve;
import static euler.algo.Library.pow;

public final class Problem249 {
    private Problem249() {}

    public static long solve(int limit, int digits) {
        long digitMod = pow(10L, digits);

        int[] primes = new PrimeSieve(limit).getPrimes();
        int maxSum = Arrays.stream(primes).sum();

        long[] subsets = new long[maxSum + 1];
        subsets[0] = 1;

        int largest = 0;
        for (int p : primes) {
            largest += p;
            for (int i = largest; i >= p; i--) {
                subsets[i] += subsets[i - p];
                subsets[i] %= digitMod;
            }
        }

        long result = 0;
        PrimeSieve sieve = new PrimeSieve(maxSum);
        for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
            result += subsets[p];
            result %= digitMod;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solve(5000, 16));
    }
}
