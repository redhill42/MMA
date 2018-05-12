package euler;

import java.util.stream.LongStream;

import euler.util.Permutations;

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
            if (checkAll(n, radix))
                numbers[i++] = n;
        } while (i < count && Permutations.next(digits));
        return numbers;
    }

    private static long fromDigits(int[] digits, int radix) {
        long n = 0;
        for (int d : digits)
            n = n * radix + d;
        return n;
    }

    private static boolean checkAll(long n, int radix) {
        for (int b = radix; b >= 2; b--)
            if (!check(n, b))
                return false;
        return true;
    }

    private static boolean check(long n, int b) {
        int mask = 0;
        while (n != 0) {
            mask |= 1 << (n % b);
            n /= b;
        }
        return mask == (1L << b) - 1;
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
