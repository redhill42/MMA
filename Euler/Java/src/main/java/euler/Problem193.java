package euler;

import static euler.algo.Sublinear.sqfn;

public final class Problem193 {
    private Problem193() {}

    public static long solve(long limit) {
        return sqfn(limit - 1);
    }

    public static void main(String[] args) {
        System.out.println(solve(1L << 50));
    }
}
