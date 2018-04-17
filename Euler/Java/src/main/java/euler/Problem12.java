package euler;

import java.util.Scanner;
import euler.util.FactorizationSieve;

public class Problem12 {
    private final int limit;

    public Problem12(int limit) {
        this.limit = limit;
    }

    public int solve() {
        return solve(limit);
    }

    public static int solve(int limit) {
        FactorizationSieve sieve = new FactorizationSieve(50000);
        for (int n = 1; ; n++) {
            int a, b;
            if (n % 2 == 0) {
                a = n / 2;
                b = n + 1;
            } else {
                a = n;
                b = (n + 1) / 2;
            }

            int cnt = sieve.sigma(a) * sieve.sigma(b);
            if (cnt > limit) {
                return n * (n + 1) / 2;
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        while (--t >= 0) {
            int n = in.nextInt();
            System.out.println(solve(n));
        }
    }
}
