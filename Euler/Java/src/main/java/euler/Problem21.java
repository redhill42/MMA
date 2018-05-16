package euler;

import java.util.Set;
import java.util.TreeSet;

import euler.algo.FactorizationSieve;

public final class Problem21 {
    private Problem21() {}

    private static class Solver {
        private final Set<Integer> amicables = new TreeSet<>();

        public Solver(int limit) {
            FactorizationSieve sieve = new FactorizationSieve(limit);
            for (int a = 2; a <= limit; a++) {
                int b = (int)sieve.sigma(1, a) - a;
                if (b > a && b <= limit && sieve.sigma(1, b) - b == a) {
                    amicables.add(a);
                    amicables.add(b);
                }
            }
        }

        public int solve(int limit) {
            return amicables.stream().filter(n -> n <= limit).reduce(0, Integer::sum);
        }
    }

    public static int solve(int limit) {
        return new Solver(limit).solve(limit);
    }

    public static void main(String[] args) {
        int limit = 10000;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
