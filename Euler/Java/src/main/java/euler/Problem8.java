package euler;

import java.math.BigInteger;
import java.util.Scanner;

public final class Problem8 {
    private Problem8() {}

    public static long solve(BigInteger input, int len) {
        char[] digits = input.toString().toCharArray();
        long largest = 0;
        long product = 1;

        for (int i = 0, j = 0; i < digits.length; i++) {
            if (digits[i] == '0') {
                product = 1;
                j = i + 1;
            } else {
                product *= digits[i] - '0';
                if (i - j + 1 == len) {
                    if (product > largest)
                        largest = product;
                    product /= digits[j] - '0';
                    j++;
                }
            }
        }
        return largest;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        while (--t >= 0) {
            int n = in.nextInt();
            int k = in.nextInt();
            BigInteger input = in.nextBigInteger();
            System.out.println(solve(input, k));
        }
    }
}
