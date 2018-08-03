package euler;

import static euler.algo.Library.factorize;
import static euler.algo.Library.pow;

public final class Problem157 {
    private Problem157() {}

    public static long solve(int n) {
        long sum = 0;
        for (int k = 1; k <= n; k++)
            sum += compute(k);
        return sum;
    }

    private static long compute(int k) {
        long n = pow(10, k);
        int  i, j;
        long a, b;
        long sum = 0;

        // find all divisors of n which is coprime to 1
        for (i = 0, a = 1; i <= k; i++, a *= 2)
            for (j = 0, b = 1; j <= k; j++, b *= 5)
                sum += factorize(n + a * b).tau();

        // find all coprime divisor pairs (2^i, 5^j)
        for (i = 1, a = 2; i <= k; i++, a *= 2)
            for (j = 1, b = 5; j <= k; j++, b *= 5)
                sum += factorize(n/a + n/b).tau();

        return sum;
    }

    public static void main(String[] args) {
        int n = 9;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
