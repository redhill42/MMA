package euler;

import java.util.Set;
import java.util.TreeSet;

import euler.algo.PrimeSieve;

import static euler.algo.Library.isqrt;
import static euler.algo.Library.pow;

public final class Problem616 {
    private Problem616() {}

    public static long solve(long limit) {
        int max_a = (int)isqrt(limit);
        PrimeSieve sieve = new PrimeSieve(max_a);
        Set<Long> sol = new TreeSet<>();

        for (int a = 2; a <= max_a; a++) {
            for (int b = 2; ; b++) {
                long n = pow(a, b);
                if (n > limit)
                    break;
                if (sieve.isPrime(a) && sieve.isPrime(b))
                    continue;
                sol.add(n);
            }
        }
        return sol.stream().mapToLong(x->x).sum() - 16;
    }

    public static void main(String[] args) {
        long n = (long)1e12;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
