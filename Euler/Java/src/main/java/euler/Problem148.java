package euler;

import java.util.Arrays;

public final class Problem148 {
    private Problem148() {}

    public static long solve(long levels, int modulo) {
        int[] basep = new int[(int)(Math.log(levels) / Math.log(modulo)) + 1];
        Arrays.fill(basep, 1);

        long result = 1;
        for (int i = 1; i < levels; i++) {
            basep[0]++;
            for (int carry = 0; basep[carry] == modulo + 1; carry++) {
                basep[carry] = 1;
                basep[carry+1]++;
            }

            long accum = 1;
            for (int x : basep)
                accum *= x;
            result += accum;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000_000, 7));
    }
}
