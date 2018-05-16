package euler;

import java.util.Arrays;

import euler.algo.Permutations;
import static euler.algo.Library.gcd;

public final class Problem170 {
    private Problem170() {}

    public static long solve() {
        int[] digits = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        do {
            if (digits[0] == 0)
                continue;

            for (int split = 1; split < digits.length; split++) {
                if (digits[split] == 0)
                    continue;

                long left = fromDigits(digits, 0, split);
                long right = fromDigits(digits, split, digits.length);
                long g = gcd(left, right);
                for (long factor = 2; factor <= g; factor++) {
                    if (isPandigital(factor, left, right))
                        return fromDigits(digits, 0, digits.length);
                }
            }
        } while (Permutations.previous(digits));
        throw new IllegalStateException("not found");
    }

    private static long fromDigits(int[] digits, int from, int to) {
        long n = 0;
        for (int i = from; i < to; i++)
            n = n * 10 + digits[i];
        return n;
    }

    private static boolean isPandigital(long factor, long left, long right) {
        if (left % factor != 0 || right % factor != 0)
            return false;

        String s = Long.toString(factor) +
                   Long.toString(left / factor) +
                   Long.toString(right / factor);
        char[] digits = s.toCharArray();

        if (digits.length != 10)
            return false;
        Arrays.sort(digits);
        for (int i = 0; i < 10; i++)
            if (digits[i] != '0' + i)
                return false;

        System.out.printf("%d x (%d, %d)%n", factor, left / factor, right / factor);
        return true;
    }

    public static void main(String[] args) {
        System.out.println(solve());
    }
}
