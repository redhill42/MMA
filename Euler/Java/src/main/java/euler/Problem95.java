package euler;

import java.util.Arrays;

public class Problem95 {
    static final int LIMIT = 1000000;

    final int[] divsum = new int[LIMIT + 1];
    final int[] chain = new int[LIMIT + 1];

    public Problem95() {
        Arrays.fill(divsum, 1);
        for (int q = 2; q <= Math.sqrt(LIMIT); q++) {
            for (int c = q; c*q <= LIMIT; c++) {
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

        for (int i = 3; i <= LIMIT; i++) {
            if (chain[i] != 0)
                continue;

            int nx = i;
            while (nx <= LIMIT && chain[nx] == 0) {
                chain[nx] = L++;
                nx = divsum[nx];
            }
            if (nx <= LIMIT && chain[nx] >= chain[i]) {
                int len = L - chain[nx];
                if (len > maxlen) {
                    maxlen = len;
                    minval = minval(nx);
                }
            }
        }

        return minval;
    }

    public static void main(String[] args) {
        System.out.println(new Problem95().solve());
    }
}
