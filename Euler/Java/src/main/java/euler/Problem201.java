package euler;

import java.util.Arrays;

public final class Problem201 {
    private Problem201() {}

    public static int solve(int n, int k) {
        int[] set = new int[n];
        for (int i = 1; i <= n; i++)
            set[i-1] = i * i;
        return solve(set, k);
    }

    public static int solve(int[] set, int choose) {
        int size = set.length;
        Arrays.sort(set);

        // allocate data structure to store reachable and duplicate sums
        boolean[][] reachable = new boolean[choose + 1][];
        boolean[][] duplicate = new boolean[choose + 1][];
        for (int k = 0; k <= choose; k++) {
            int maxsum = 0;
            for (int i = size - k; i < size; i++)
                maxsum += set[i];
            reachable[k] = new boolean[maxsum + 1];
            duplicate[k] = new boolean[maxsum + 1];
        }

        // the empty set is always possible
        reachable[0][0] = true;

        // for each element in the set...
        for (int value : set) {
            // try to add it to any valid set
            for (int k = choose; k > 0; k--) {
                // try every valid sum
                for (int sum = 0; sum < reachable[k-1].length; sum++) {
                    // sum must be reachable
                    if (!reachable[k-1][sum])
                        continue;

                    // generate new sum and test for reachable and uniqueness
                    int newsum = sum + value;
                    if (reachable[k][newsum] || duplicate[k-1][sum])
                        duplicate[k][newsum] = true;
                    reachable[k][newsum] = true;
                }
            }
        }

        // count all unique sums
        int res = 0;
        for (int sum = 0; sum < reachable[choose].length; sum++)
            if (reachable[choose][sum] && !duplicate[choose][sum])
                res += sum;
        return res;
    }

    public static void main(String[] args) {
        System.out.println(solve(100, 50));
    }
}
