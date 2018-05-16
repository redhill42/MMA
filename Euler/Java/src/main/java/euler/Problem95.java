package euler;

import java.util.Arrays;
import static euler.algo.Library.isqrt;

public final class Problem95 {
    private Problem95() {}

    private static class Solver {
        private final int limit;
        private final int[] divsum;
        private final int[] chain;

        public Solver(int limit) {
            this.limit = limit;
            this.divsum = new int[limit + 1];
            this.chain = new int[limit + 1];

            Arrays.fill(divsum, 1);
            for (int q = 2; q <= isqrt(limit); q++) {
                for (int c = q; c*q <= limit; c++) {
                    divsum[c*q] += c+q;
                }
            }
        }

        int minval(int a) {
            int start = a, m = a;
            do {
                m = Math.min(m, a);
                a = divsum[a];
            } while (a != start);
            return m;
        }

        public int solve() {
            int L = 1;
            int maxlen = 0, minval = 0;

            for (int i = 3; i <= limit; i++) {
                if (chain[i] != 0)
                    continue;

                int nx = i;
                while (nx <= limit && chain[nx] == 0) {
                    chain[nx] = L++;
                    nx = divsum[nx];
                }
                if (nx <= limit && chain[nx] >= chain[i]) {
                    int len = L - chain[nx];
                    if (len > maxlen) {
                        maxlen = len;
                        minval = minval(nx);
                    }
                }
            }

            return minval;
        }
    }

    public static int solve(int limit) {
        return new Solver(limit).solve();
    }

    public static void main(String[] args) {
        System.out.println(solve(1000000));
    }
}
