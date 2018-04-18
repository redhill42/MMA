package euler;

import java.util.Arrays;
import java.util.stream.IntStream;

public final class Problem122 {
    private static final int MAX_DEPTH = 100;

    private Problem122() {}

    public static int[] solve(int limit) {
        final int[] results = new int[limit];
        final int[] powers = new int[MAX_DEPTH + 1];
        final int[] levels = new int[MAX_DEPTH + 1];

        Arrays.fill(results, Integer.MAX_VALUE);
        results[0] = 0;
        results[1] = 1;
        powers[0] = 1;
        powers[1] = 2;
        levels[0] = 0;
        levels[1] = 1;

        // The algo always starts by adding the current power to itself
        // and will later backtrack to adding the power of previous nodes
        // to itself. When a node has been fully evaluated, the power of
        // the previous node becomes the current power.

        int n = 1;
        while (true) {
            int i = levels[n];                  // which node needs to be added
            if (i < 0) {                        // the node have been fully evaluated
                if (--n == 0)                   // continue with previous node
                    return results;             // the result array is now finalized
            } else {
                int k = powers[i] + powers[n];  // get power of proper node and add current power
                levels[n]--;                    // decrement for the next evaluation
                if (k <= limit && results[k-1] >= n+1) { // check if this power had required fewer additions before
                    n++;                        // increment number of additions
                    results[k-1] = n;           // update result array with a lower (or equal) number of additions
                    powers[n] = k;              // store current power for this branch
                    levels[n] = n;              // store starting level for this branch
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(IntStream.of(solve(200)).sum());
    }
}
