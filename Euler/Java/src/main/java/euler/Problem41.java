package euler;

import static euler.algo.Library.fromDigits;
import static euler.algo.Library.isPrime;
import euler.algo.Permutations;

public final class Problem41 {
    private Problem41() {}

    public static int solve() {
        int[] digits = {7, 6, 5, 4, 3, 2, 1};
        do {
            int n = (int)fromDigits(digits);
            if (isPrime(n))
                return n;
        } while (Permutations.previous(digits));
        throw new IllegalStateException("No solution found");
    }

    public static void main(String[] args) {
        System.out.println(solve());
    }
}
