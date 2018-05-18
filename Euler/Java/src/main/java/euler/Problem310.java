package euler;

import java.util.BitSet;

public final class Problem310 {
    private Problem310() {}

    private static int[] getSG(int max) {
        int[] sg = new int[max + 1];
        BitSet hash = new BitSet(max + 1);

        for (int i = 0; i <= max; i++) {
            hash.clear();
            for (int j = 1; ; j++) {
                int r = j * j;
                if (r > i)
                    break;
                hash.set(sg[i - r]);
            }
            sg[i] = hash.nextClearBit(0);
        }
        return sg;
    }

    public static long solve(int max) {
        int[] sg = getSG(max);

        int max_sg = 0;
        for (int s : sg)
            max_sg = Math.max(max_sg, s);

        int[] tally = new int[max_sg + 1];
        for (int s : sg)
            tally[s]++;

        long total = 0;
        for (int a = 0; a <= max_sg; a++) {
            for (int b = 0; b <= max_sg; b++) {
                int c = a ^ b;
                if (c <= max_sg)
                    total += (long)tally[a] * tally[b] * tally[c];
            }
        }

        // fix so that a <= b <= c
        total = (total - tally[0] * (3 * max + 1)) / 6;
        total += tally[0] * (max + 1);
        return total;
    }

    public static void main(String[] args) {
        System.out.println(solve(100000));
    }
}
