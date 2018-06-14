package euler;

import static euler.algo.Library.isPrime;

public final class Problem128 {
    private Problem128() {}

    public static long solve(int target) {
        int found = 2; // PD(1)=3 and PD(2)=3
        for (long n = 2; ; n++) {
            if (!isPrime(6 * n - 1))
                continue;
            if (isPrime(6 * n + 1) && isPrime(12 * n + 5)) {
                if (++found == target)
                    return 3 * n * (n - 1) + 2;
            }
            if (isPrime(6 * n + 5) && isPrime(12 * n - 7)) {
                if (++found == target)
                    return 3 * n * (n + 1) + 1;
            }
        }
    }

    public static void main(String[] args) {
        int n = 2000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
