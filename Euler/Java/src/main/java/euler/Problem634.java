package euler;

import euler.algo.PrimeSieve;
import euler.algo.Sublinear;

import static euler.algo.Library.icbrt;
import static euler.algo.Library.isqrtExact;
import static euler.algo.Library.isurd;
import static euler.algo.Library.pow;

public final class Problem634 {
    private Problem634() {}

    public static long solve(long limit) {
        long count = 0;

        int L = icbrt(limit / 4);
        PrimeSieve sieve = new PrimeSieve(L);
        byte[] mu = Sublinear.moebius(sieve, L);

        // count for squarefree numbers
        for (int b = 2; b <= L; b++) {
            if (mu[b] != 0)
                count += isqrtExact(limit / pow(b, 3)) - 1;
        }

        // count for perfect square numbers by inclusion-exclusion
        int L2 = (int)isurd(limit, 6);
        long T = isqrtExact(limit);
        for (int b = 2; b <= L2; b++) {
            if (mu[b] != 0)
                count -= mu[b] * T / pow(b, 3);
            if (sieve.isPrime(b))
                count -= 1; // exclude for a=1
        }

        return count;
    }

    public static void main(String[] args) {
        long n = (long)9e18;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
