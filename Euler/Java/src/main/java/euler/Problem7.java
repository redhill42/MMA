package euler;

import static euler.util.Utils.isPrime;

public class Problem7 {
    private final int limit;

    public Problem7(int limit) {
        this.limit = limit;
    }

    public int solve() {
        return solve(limit);
    }

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
        System.out.println(new Problem7(10001).solve());
    }
}
