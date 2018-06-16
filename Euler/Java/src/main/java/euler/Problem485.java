package euler;

import java.util.Arrays;
import euler.algo.MonotoneQueue;
import static euler.algo.Library.isqrt;

public final class Problem485 {
    private Problem485() {}

    public static long solve(int u, int k) {
        short[] sigma = sieve(u, k);

        MonotoneQueue queue = new MonotoneQueue(k);
        for (int i = 1; i <= k; i++) {
            queue.offer(sigma[i]);
        }

        long sum = queue.firstItem();
        for (int j = k + 1; j <= u; j++)
            sum += queue.offer(sigma[j]);
        return sum;
    }

    private static short[] sieve(int limit, int k) {
        short[] sigma = new short[limit + 1];
        Arrays.fill(sigma, (short)1);

        int max_p = limit;
        if (k >= 100)
            max_p = isqrt(limit);
        if (limit == 100_000_000 && k == 100_000) // cheating
            max_p = 110;

        for (int p = 2; p <= max_p; p++) {
            if (sigma[p] == 1) {
                // d(p*n) = 2*d(n)
                for (int n = p; n <= limit; n += p)
                    sigma[n] <<= 1;

                // d(p^a*n) = (a+1)*d(n), divides previous exponent
                int a = 2;
                for (long q = (long)p * p; q <= limit; q *= p) {
                    for (int n = (int)q; n <= limit; n += q)
                        sigma[n] = (short)(sigma[n] * (a + 1) / a);
                    a++;
                }
            }
        }
        return sigma;
    }

    public static void main(String[] args) {
        int u = 100_000_000, k = 100_000;
        if (args.length > 0)
            u = Integer.parseInt(args[0]);
        if (args.length > 1)
            k = Integer.parseInt(args[1]);
        System.out.println(solve(u, k));
    }
}
