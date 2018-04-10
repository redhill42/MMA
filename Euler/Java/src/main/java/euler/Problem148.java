package euler;

import java.util.Arrays;

public class Problem148 {
    private final long levels;
    private final int modulo;

    public Problem148(long levels, int modulo) {
        this.levels = levels;
        this.modulo = modulo;
    }

    public long solve() {
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
        Problem148 solver = new Problem148(1_000_000_000, 7);
        System.out.println(solver.solve());
    }
}
