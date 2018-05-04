package euler;

import java.util.Arrays;

import static euler.util.Utils.modpow;
import static euler.util.Utils.pow;

public final class Problem250 {
    private Problem250() {}

    public static long solve(int limit, int modulo, int digits) {
        long digitMod = pow(10L, digits);

        int[] residues = new int[limit + 1];
        Arrays.setAll(residues, x -> modpow(x, x, modulo));

        long[] subsets = new long[modulo + 1];
        long[] current = new long[modulo + 1];
        subsets[0] = current[0] = 1;

        for (int i = 1; i <= limit; i++) {
            for (int j = 0; j < modulo; j++) {
                int residue = residues[i] + j;
                if (residue >= modulo)
                    residue -= modulo;
                subsets[residue] += current[j];
                subsets[residue] %= digitMod;
            }
            System.arraycopy(subsets, 0, current, 0, subsets.length);
        }
        return subsets[0] - 1;
    }

    public static void main(String[] args) {
        System.out.println(solve(250250, 250, 16));
    }
}
