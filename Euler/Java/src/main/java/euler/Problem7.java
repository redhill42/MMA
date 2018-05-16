package euler;

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
        int limit = 10001;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
