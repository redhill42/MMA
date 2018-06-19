package euler;

import java.util.List;

import euler.algo.FactorizationSieve;
import euler.algo.PrimeFactor;
import euler.util.LongRangedTask;

import static euler.algo.Library.chineseRemainder;
import static euler.algo.Library.even;
import static euler.algo.Library.isPowerOfTwo;

public final class Problem451 {
    private Problem451() {}

    public static long solve(int low, int high) {
        FactorizationSieve sieve = new FactorizationSieve(high);
        return LongRangedTask.parallel(low, high, (from, to) -> {
            long sum = 0;
            for (int n = from; n <= to; n++)
                sum += solve(sieve, n);
            return sum;
        });
    }

    private static long solve(FactorizationSieve sieve, int n) {
        if (sieve.isPrime(n) || n == 4)
            return 1;
        if (isPowerOfTwo(n))
            return (n >> 1) + 1;

        List<PrimeFactor> factors = sieve.factors(n);
        if (factors.size() == 1)
            return 1;

        int[] gene = new int[factors.size() + 1];
        int k = 0;

        // get the generating set of square roots of unity modulo n
        for (PrimeFactor f : factors) {
            int a = (int)f.value();
            gene[k++] = (int)chineseRemainder(-1, a, 1, n / a);
        }

        // fix for even numbers
        if (even(n)) {
            int a = factors.get(0).power();
            if (a == 2) {
                gene[0] = n / 2 + 1;
            } else if (a >= 3) {
                gene[k++] = n / 2 + 1;
            }
        }

        // get all solutions from generating set and find largest one
        long max = 0;
        int ord = (1 << k) - 1;
        for (int mask = 1; mask < ord; mask++) {
            long prod = 1;
            for (int x = mask, i = 0; x > 0; x >>= 1, i++) {
                if ((x & 1) != 0)
                    prod = prod * gene[i] % n;
            }
            if (prod != n - 1 && prod > max) {
                max = prod;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(solve(3, 20_000_000));
    }
}
