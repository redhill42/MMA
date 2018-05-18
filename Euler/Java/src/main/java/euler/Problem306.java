package euler;

import java.util.BitSet;

public final class Problem306 {
    private Problem306() {}

    @SuppressWarnings("unused")
    private static int[] getSG(int max) {
        int[] sg = new int[max + 1];
        BitSet hash = new BitSet(max + 1);
        for (int n = 2; n <= max; n++) {
            hash.clear();
            for (int k = 0; k <= n - 2; k++) {
                hash.set(sg[k] ^ sg[n - k - 2]);
            }
            sg[n] = hash.nextClearBit(0);
        }
        return sg;
    }

    public static int solve(int max) {
        // OEIS A215721, for n > 14, a(n) = a(n-5) + 34
        int[] domino = {1, 5, 9, 15, 21, 25, 29, 35, 39, 43, 55, 59, 63};
        int total;

        total = max;
        for (int n : domino) {
            if (n > max)
                return total;
            total--;
        }

        total = domino.length;
        for (int i = domino.length - 5; i < domino.length; i++)
            total += (max - domino[i]) / 34;
        return max - total;
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000));
    }
}
