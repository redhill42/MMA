package euler;

import static euler.algo.Library.lcm;

public final class Problem601 {
    private Problem601() {}

    public static long solve(int limit) {
        long sum = 0;
        for (int i = 1; i <= limit; i++)
            sum += P(i, 1L << (2*i));
        return sum;
    }

    private static long P(int s, long n) {
        long k = 1;
        for (int i = 2; i <= s; i++)
            k = lcm(k, i);
        return (n - 2) / k - (n - 2) / lcm(k, s + 1);
    }

    public static void main(String[] args) {
        System.out.println(solve(31));
    }
}
