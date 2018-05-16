package euler;

import static euler.algo.Library.lcm;

public final class Problem5 {
    private Problem5() {}

    public static long solve(int n) {
        long res = 1;
        for (int i = 2; i <= n; i++)
            res = lcm(res, i);
        return res;
    }

    public static void main(String[] args) {
        System.out.println(solve(20));
    }
}
