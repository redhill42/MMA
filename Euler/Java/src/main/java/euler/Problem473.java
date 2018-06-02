package euler;

import static euler.algo.Library.fibonacci;

public final class Problem473 {
    private Problem473() {}

    public static long solve(long limit) {
        long total = 3; // special case for 1 and 2
        total = search1(1, limit, 0, total, false);
        total = search2(3, limit, 0, total);
        return total;
    }

    private static long search1(int i, long limit, long sum, long total, boolean rec) {
        for (; ; i += 2) {
            long n = sum + fibonacci(i) + fibonacci(i + 6);
            if (n >= limit)
                break;
            total = search1(i + 6, limit, n, total + n, true);
            if (!rec) break;
        }
        return total;
    }

    private static long search2(int i, long limit, long sum, long total) {
        for (; ; i += 2) {
            long n = sum + fibonacci(i) + fibonacci(i + 6);
            if (n >= limit)
                break;
            total += n;
            if (n + 2 < limit)
                total += n + 2;
            total = search2(i + 6, limit, n, total);
        }
        return total;
    }

    public static void main(String[] args) {
        long limit = (long)1e10;
        if (args.length > 0)
            limit = Long.parseLong(args[0]);
        System.out.println(solve(limit));
    }
}
