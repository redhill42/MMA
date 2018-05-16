package euler;

import euler.algo.Champernowne;

public final class Problem40 {
    private Problem40() {}

    public static int solve(int limit) {
        int sum = 1;
        for (int i = 1; i <= limit; i *= 10)
            sum *= Champernowne.digit(i);
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000));
    }
}
