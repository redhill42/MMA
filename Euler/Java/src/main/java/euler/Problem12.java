package euler;

import static euler.algo.Library.even;
import static euler.algo.Library.factorize;

public final class Problem12 {
    private Problem12() {}

    public static long solve(int limit) {
        int a = 1;
        for (int n = 2; ; n++) {
            int b = factorize(even(n) ? n / 2 : n).tau();
            if (a * b > limit)
                return (long)n * (n - 1) / 2;
            a = b;
        }
    }

    public static void main(String[] args) {
        int limit = 500;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
