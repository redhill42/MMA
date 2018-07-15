package euler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import euler.algo.Ratio;

import static euler.algo.Library.fromDigits;

public final class Problem259 {
    private Problem259() {}

    private static Set<Ratio> search(int[] digits, int from, int to) {
        Set<Ratio> result = new HashSet<>();

        // the number itself is reachable if no operations are applied
        result.add(Ratio.valueOf(fromDigits(digits, from, to)));

        for (int split = from + 1; split < to; split++) {
            // recursively find all fractions in splits
            Set<Ratio> left = search(digits, from, split);
            Set<Ratio> right = search(digits, split, to);

            // merge both side with arithmetic operations (+, -, *, /)
            for (Ratio x : left) {
                for (Ratio y : right) {
                    result.add(x.add(y));
                    result.add(x.subtract(y));
                    result.add(x.multiply(y));
                    if (y.signum() != 0)
                        result.add(x.divide(y));
                }
            }
        }

        return result;
    }

    public static long solve(int maxDigits) {
        int[] digits = new int[maxDigits];
        Arrays.setAll(digits, i -> i + 1);

        long sum = 0;
        for (Ratio x : search(digits, 0, maxDigits))
            if (x.numer() > 0 && x.denom() == 1)
                sum += x.numer();
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solve(9));
    }
}
