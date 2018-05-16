package euler;

import euler.algo.Permutations;
import static euler.algo.Library.fromDigits;

public final class Problem24 {
    private Problem24() {}

    public static long solve(int index) {
        int[] a = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int i = 1; i < index; i++)
            Permutations.next(a);
        return fromDigits(a);
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000));
    }
}
