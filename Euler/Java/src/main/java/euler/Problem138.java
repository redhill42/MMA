package euler;

import euler.algo.Pair;
import euler.algo.PellEquation;

public final class Problem138 {
    private Problem138() {}

    public static long solve(int count) {
        long sum = 0;
        for (Pair p : PellEquation.series(5, -1)) {
            if (count == 0)
                break;
            if (p.x != 2 && p.x != -2) {
                sum += p.y;
                count--;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solve(12));
    }
}
