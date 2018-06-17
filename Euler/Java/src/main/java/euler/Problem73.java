package euler;

import euler.algo.FareySequence;
import euler.algo.Pair;

public final class Problem73 {
    private Problem73() {}

    public static long solve(long d) {
        FareySequence farey = new FareySequence(d);
        long total = 0;

        for (Pair p : farey.ascending(1, 3)) {
            if (p.x == 1 && p.y == 2)
                break;
            total++;
        }
        return total - 1; // exclude 1/3
    }

    public static void main(String[] args) {
        System.out.println(solve(12000));
    }
}
