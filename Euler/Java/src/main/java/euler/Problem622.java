package euler;

import euler.algo.Factorization;

import static euler.algo.Library.factorize;
import static euler.algo.Library.pow;

public final class Problem622 {
    private Problem622() {}

    public static long solve(int m) {
        long sum = 0;
        for (long n : factorize(m).divisors())
            sum += factorize(n).moebius() * F(m / (int)n);
        return sum;
    }

    private static long F(int n) {
        Factorization factors = factorize(pow(2, n) - 1);
        return factors.sigma(1) + factors.sigma(0);
    }

    public static void main(String[] args) {
        System.out.println(solve(60));
    }
}
