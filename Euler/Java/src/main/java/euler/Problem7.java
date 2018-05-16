package euler;

import java.util.Scanner;
import static euler.algo.Library.isPrime;

public final class Problem7 {
    private Problem7() {}

    public static int solve(int limit) {
        int count = 2; // 2 and 3
        for (int n = 6; ; n += 6) {
            if (isPrime(n - 1) && ++count == limit)
                return n - 1;
            if (isPrime(n + 1) && ++count == limit)
                return n + 1;
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
