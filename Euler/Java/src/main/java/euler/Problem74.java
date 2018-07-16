package euler;

import java.util.HashMap;
import java.util.Map;

public final class Problem74 {
    private Problem74() {}

    private static class Solver {
        private final Map<Integer, Integer> memo = new HashMap<>();
        private final int[] factorials;

        Solver() {
            factorials = new int[10];
            factorials[0] = 1;
            for (int i = 1; i <= 9; i++) {
                factorials[i] = factorials[i-1] * i;
            }
        }

        int solve(int limit) {
            memo.put(1, 1);
            memo.put(2, 1);
            memo.put(145, 1);
            memo.put(40585, 1);
            memo.put(871, 2);
            memo.put(872, 2);
            memo.put(45361, 2);
            memo.put(45362, 2);
            memo.put(169, 3);
            memo.put(36301, 3);
            memo.put(1454, 3);

            int count = 0;
            for (int n = 1; n <= limit; n++)
                if (chain(n) == 60)
                    count++;
            return count;
        }

        private int chain(int n) {
            return memo.computeIfAbsent(n, x -> 1 + chain(factorialSum(n)));
        }

        private int factorialSum(int n) {
            int res = 0;
            while (n > 0) {
                res += factorials[n % 10];
                n /= 10;
            }
            return res;
        }
    }

    public static int solve(int limit) {
        return new Solver().solve(limit);
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000));
    }
}
