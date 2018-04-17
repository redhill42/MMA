package euler;

import java.util.Scanner;

public class Problem2 {
    private final long limit;

    public Problem2(long limit) {
        this.limit = limit;
    }

    public long solve() {
        return solve(limit);
    }

    public static long solve(long limit) {
        long a = 2, b = 8, s = 0, t;
        while (a < limit) {
            s += a;
            t = b;
            b = 4 * b + a;
            a = t;
        }
        return s;
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
