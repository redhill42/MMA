package euler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class Problem284 {
    private Problem284() {}

    public static long solve(int n, int radix) {
        long sum = 1;
        for (int p : factorize(radix)) {
            BigInteger a = BigInteger.valueOf(p).pow(n);
            BigInteger b = BigInteger.valueOf(radix / p).pow(n);
            sum += accumulate(chineseRemainder(a, b), radix);
        }
        return sum;
    }

    private static List<Integer> factorize(int n) {
        List<Integer> result = new ArrayList<>();
        for (int p = 2; p <= n; p++) {
            if (n % p == 0) {
                int f = 1;
                while (n % p == 0) {
                    f *= p;
                    n /= p;
                }
                result.add(f);
            }
        }
        return result;
    }

    private static long accumulate(BigInteger n, int radix) {
        char[] digits = n.toString(radix).toCharArray();
        long accum = 0, sum = 0;
        for (int i = digits.length - 1; i >= 0; i--) {
            char c = digits[i];
            int d = (c >= 'a') ? c - 'a' + 10 : c - '0';
            if (d != 0) {
                accum += d;
                sum += accum;
            }
        }
        return sum;
    }

    // simplified Chinese Remainder Theorem
    //     x = 0 (mod m)
    //     x = 1 (mod n)
    private static BigInteger chineseRemainder(BigInteger m, BigInteger n) {
        return m.modInverse(n).multiply(m);
    }

    public static void main(String[] args) {
        System.out.println(Long.toString(solve(10000, 14), 14));
    }
}
