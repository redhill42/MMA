package euler;

import java.util.Set;
import java.util.TreeSet;

import static euler.algo.Library.nextPrime;

public final class Problem293 {
    private Problem293() {}

    private static void search(long p, long n, long limit, Set<Long> sol) {
        long q = nextPrime(p);
        for (n *= p; n < limit; n *= p) {
            sol.add(nextPrime(n+1) - n);
            search(q, n, limit, sol);
        }
    }

    public static long solve(long limit) {
        Set<Long> sol = new TreeSet<>();
        search(2, 1, limit, sol);
        return sol.stream().mapToLong(x->x).sum();
    }

    public static void main(String[] args) {
        long n = (long)1e9;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
