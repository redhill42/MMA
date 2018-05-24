package euler;

import java.util.stream.LongStream;

import euler.algo.Permutations;
import static euler.algo.Library.fromDigits;

public final class Problem571 {
    private Problem571() {}

    public static long[] solve(int radix, int count) {
        int[] digits = new int[radix];
        digits[0] = 1; digits[1] = 0;
        for (int i = 2; i < radix; i++) {
            digits[i] = i;
        }

        long[] numbers = new long[count];
        int i = 0;
        do {
            long n = fromDigits(digits, radix);
            if (checkAll(n, radix - 1))
                numbers[i++] = n;
        } while (i < count && Permutations.next(digits));
        return numbers;
    }

    private static boolean checkAll(long n, int radix) {
        if (radix >= 11 && !check(n, 11)) // this optimization is weird
            return false;
        for (int b = radix; b >= 2; b--)
            if (!check(n, b))
                return false;
        return true;
    }

    private static boolean check(long n, int b) {
        int mask = 0, pan = (1 << b) - 1;
        while (n != 0 && mask != pan) {
            mask |= 1 << (n % b);
            n /= b;
        }
        return mask == pan;
    }

    public static void main(String[] args) {
        int radix = 12, count = 10;
        if (args.length > 0)
            radix = Integer.parseInt(args[0]);
        if (args.length > 1)
            count = Integer.parseInt(args[1]);
        System.out.println(LongStream.of(solve(radix, count)).sum());
    }
}
