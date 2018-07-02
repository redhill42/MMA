package euler;

import euler.algo.FareySequence;
import euler.algo.Ratio;

public final class Problem73 {
    private Problem73() {}

    public static long solve(long d) {
        FareySequence farey = new FareySequence(d);
        long total = 0;

        for (Ratio p : farey.ascending(Ratio.ONE_THIRD, Ratio.ONE_HALF))
            total++;
        return total - 1; // exclude 1/3
    }

    public static void main(String[] args) {
        System.out.println(solve(12000));
    }
}
