package euler;

import static euler.algo.Library.isPrime;

public final class Problem27 {
    private Problem27() {}

    public static int solve(int limit) {
        int max = 0, res = 0;
        for (int a = -limit; a <= limit; a++) {
            for (int b = -a + 1; b <= limit; b++) {
                int k = count(a, b);
                if (k > max) {
                    max = k;
                    res = a * b;
                }
            }
        }
        return res;
    }

    private static int count(int a, int b) {
        int k = 0;
        for (int n = 0; ; n++) {
            int p = n * n + a * n + b;
            if (!(p > 0 && isPrime(p)))
                break;
            k++;
        }
        return k;
    }

    public static void main(String[] args) {
        System.out.println(solve(1000));
    }
}
