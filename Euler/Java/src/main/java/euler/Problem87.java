package euler;

import java.util.BitSet;

import euler.algo.PrimeSieve;

import static euler.algo.Library.icbrt;
import static euler.algo.Library.isqrt;
import static euler.algo.Library.isurd;

public final class Problem87 {
    private Problem87() {}

    public static int solve(int limit) {
        int max_a = isqrt(limit);
        PrimeSieve sieve = new PrimeSieve(max_a);
        BitSet numbers = new BitSet(limit + 1);

        for (int a = 2; a > 0; a = sieve.nextPrime(a)) {
            int max_b = icbrt(limit - a*a);
            for (int b = 2; b <= max_b; b = sieve.nextPrime(b)) {
                int max_c = isurd(limit - a*a - b*b*b, 4);
                for (int c = 2; c <= max_c; c = sieve.nextPrime(c)) {
                    numbers.set(a*a + b*b*b + c*c*c*c);
                }
            }
        }
        return numbers.cardinality();
    }

    public static void main(String[] args) {
        int n = 50_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
