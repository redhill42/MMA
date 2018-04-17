package euler;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import euler.util.FactorizationSieve;

public class Problem21 {
    private final int limit;
    private final Set<Integer> amicables = new TreeSet<>();

    public Problem21(int limit) {
        this.limit = limit;

        FactorizationSieve sieve = new FactorizationSieve(limit);
        for (int a = 2; a <= limit; a++) {
            int b = sieve.sigma(1, a) - a;
            if (b > a && b <= limit && sieve.sigma(1, b) - b == a) {
                amicables.add(a);
                amicables.add(b);
            }
        }
    }

    public int solve() {
        return solve(limit);
    }

    public int solve(int limit) {
        return amicables.stream().filter(n -> n <= limit).reduce(0, Integer::sum);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        List<Integer> inputs = new ArrayList<>();
        int limit = 0;
        while (--t >= 0) {
            int n = in.nextInt();
            if (n > limit)
                limit = n;
            inputs.add(n);
        }

        Problem21 solver = new Problem21(limit);
        for (int n : inputs) {
            System.out.println(solver.solve(n));
        }
    }
}
