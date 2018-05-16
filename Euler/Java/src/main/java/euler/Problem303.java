package euler;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import euler.algo.Permutations;
import static euler.algo.Library.pow;

public final class Problem303 {
    private Problem303() {}

    public static long solve(int limit) {
        Set<Integer> numbers = new TreeSet<>();
        for (int i = 1; i <= limit; i++)
            numbers.add(i);
        long sum = precompute(numbers);

        for (long n = 1; n < Long.MAX_VALUE && !numbers.isEmpty(); n++) {
            long t = toTernary(n);
            Iterator<Integer> it = numbers.iterator();
            while (it.hasNext()) {
                int i = it.next();
                if (i > t)
                    break;
                if (t % i == 0) {
                    sum += t / i;
                    it.remove();
                }
            }
        }
        return sum;
    }

    // precompute patterns for 9, 99, 999...
    private static long precompute(Set<Integer> numbers) {
        long sum = 0;

        // 9999's multiple is too large
        if (numbers.contains(9999)) {
            numbers.remove(9999);
            sum += 1111333355557778L;
        }

        for (int i = 1; i <= 3; i++) {
            for (int factor = 1; factor <= 10; factor++) {
                int n = factor * (int)(pow(10, i) - 1);
                if (!numbers.contains(n))
                    continue;

                int[] digits = new int[i * 5];
                Arrays.fill(digits, 2);
                Arrays.fill(digits, 0, i, 1);

                do {
                    long multiple = fromDigits(digits);
                    if (n % 5 == 0)
                        multiple *= 10;
                    if (multiple % n == 0) {
                        numbers.remove(n);
                        sum += multiple / n;
                        break;
                    }
                } while (Permutations.next(digits));
            }
        }
        return sum;
    }

    private static long fromDigits(int[] digits) {
        long r = 0;
        for (int d : digits)
            r = r * 10 + d;
        return r;
    }

    private static long toTernary(long n) {
        long r = 0, b = 1;
        while (n != 0) {
            r += b * (n % 3);
            n /= 3;
            b *= 10;
        }
        return r;
    }

    public static void main(String[] args) {
        int limit = 10000;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
