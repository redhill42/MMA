package euler;

import euler.algo.FareySequence;
import euler.algo.Ratio;

public final class Problem71 {
    private Problem71() {}

    public static long solve(long a, long b, long n) {
        FareySequence farey = new FareySequence(n);
        return farey.previous(Ratio.valueOf(a, b)).numer();
    }

    public static void main(String[] args) {
        System.out.println(solve(3, 7, 1_000_000));
    }
}
