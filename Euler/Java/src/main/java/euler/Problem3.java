package euler;

import java.util.Scanner;
import static euler.util.Utils.isqrt;

public class Problem3 {
    private final long n;

    public Problem3(long n) {
        this.n = n;
    }

    public long solve() {
        return solve(n);
    }

    public static long solve(long n) {
        long lastFactor = 0;

        if (n % 2 == 0) {
            lastFactor = 2;
            do {
                n /= 2;
            } while (n % 2 == 0);
        }

        long factor = 3;
        long maxFactor = isqrt(n);
        while (n > 1 && factor <= maxFactor) {
            if (n % factor == 0) {
                lastFactor = factor;
                do {
                    n /= factor;
                } while (n % factor == 0);
                maxFactor = isqrt(n);
            }
            factor += 2;
        }

        return n == 1 ? lastFactor : n;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        while (--t >= 0) {
            long n = in.nextLong();
            System.out.println(solve(n));
        }
    }
}
