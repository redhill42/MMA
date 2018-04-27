package euler;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import euler.util.FactorizationSieve;

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
        List<Integer> inputs = new ArrayList<>();
        int limit = 0;

        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        while (--t >= 0) {
            int n = in.nextInt();
            if (n > limit)
                limit = n;
            inputs.add(n);
        }

        Solver solver = new Solver(limit);
        for (int n : inputs) {
            System.out.println(solver.solve(n));
        }
    }
}
