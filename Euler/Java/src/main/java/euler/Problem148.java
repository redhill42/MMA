package euler;

import java.util.Arrays;

public class Problem148 {
    private static final long LEVELS = 1_000_000_000;
    private static final int MODULOS = 7;

    public long solve() {
        int[] basep = new int[(int)(Math.log(LEVELS)/Math.log(MODULOS)) + 1];
        Arrays.fill(basep, 1);

        long result = 1;
        for (int i = 1; i < LEVELS; i++) {
            basep[0]++;
            for (int carry = 0; basep[carry] == MODULOS + 1; carry++) {
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
        System.out.println(new Problem148().solve());
    }
}
