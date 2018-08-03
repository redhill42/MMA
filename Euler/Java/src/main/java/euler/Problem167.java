package euler;

import java.util.ArrayDeque;
import java.util.Deque;

public final class Problem167 {
    private Problem167() {}

    public static long solve(int n, long k) {
        long sum = 0;
        for (int i = 2; i <= n; i++)
            sum += compute(i, k);
        return sum;
    }

    // OEIS A100729
    private static final int[] ulam_period = {
        0, 0,
        32, 26, 444, 1628, 5906, 80, 126960, 380882, 2097152,
        1047588, 148814, 8951040, 5406720, 242, 127842440
    };

    // OEIS A100730
    private static final int[] ulam_difference = {
        0, 0,
        126, 126, 1778, 6510, 23622, 510, 507842, 1523526, 8388606,
        4194302, 597870, 35791394, 21691754, 2046, 511305630
    };

    private static long compute(int n, long k) {
        // The second even term is (2n+1) + (2n+3) = 4n+4
        int even_term = 4 * n + 4;

        // These terms are not in the period:
        // 2, 2n+1, 2n+3, ..., 4n+3, 4n+4
        int lead = n + 4;

        long q = k / ulam_period[n];
        int  r = (int)(k - q * ulam_period[n]);
        if (r <= lead) {
            q--;
            r += ulam_period[n];
        }

        Deque<Long> seq = new ArrayDeque<>();
        for (long t = 2*n+1; t < even_term; t += 2)
            seq.offerFirst(t);

        long term = 0;
        for (int i = lead; i < r; i++) {
            long a = seq.peekLast() + even_term;
            long b = seq.peekFirst() + 2;
            if (a == b) {
                seq.removeLast();
                term = seq.pollLast() + even_term;
            } else if (a < b) {
                term = a;
                seq.removeLast();
            } else {
                term = b;
            }
            seq.offerFirst(term);
        }

        return term + q * ulam_difference[n];
    }

    public static void main(String[] args) {
        System.out.println(solve(10, (long)1e11));
    }
}
