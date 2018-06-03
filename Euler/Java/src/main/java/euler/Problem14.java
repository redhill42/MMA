package euler;

import static euler.algo.Library.even;

public final class Problem14 {
    private Problem14() {}

    public static int solve(int limit) {
        int max_len = 0, max_n = 0;
        for (int n = 1; n <= limit; n++) {
            long chain = n;
            int k = 1;
            while (chain != 1) {
                ++k;
                chain = even(chain) ? chain / 2 : 3 * chain + 1;
            }
            if (k >= max_len) {
                max_len = k;
                max_n = n;
            }
        }
        return max_n;
    }

    public static void main(String[] args) {
        int limit = 1_000_000;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
