package euler;

import static euler.algo.Library.isPowerOfTwo;

public final class Problem207 {
    private Problem207() {}

    public static long solve(int numerator, int denominator) {
        int perfect = 1, total = 1;
        long x = 3;

        while (perfect * denominator > total * numerator) {
            if (isPowerOfTwo(x))
                perfect++;
            total++;
            x++;
        }
        return x * (x - 1);
    }

    public static void main(String[] args) {
        int numerator = 1, denominator = 12345;
        if (args.length >= 2) {
            numerator = Integer.parseInt(args[0]);
            denominator = Integer.parseInt(args[1]);
        }
        System.out.println(solve(numerator, denominator));
    }
}
