package euler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import euler.util.LongArray;

import static euler.algo.Library.isCoprime;
import static euler.algo.Library.isSquare;
import static euler.algo.Library.isqrt;

public final class Problem143 {
    private Problem143() {}

    public static long solve(int limit) {
        LongArray pairs = populate(limit);
        int[] idx = prepare(pairs, limit);
        return splice(pairs, idx, limit);
    }

    /**
     * Generate all 120 degree triangle triples (a,b,c). Keep the pairs (b,c)
     * in a list.
     */
    private static LongArray populate(int limit) {
        LongArray pairs = new LongArray();
        int max_m = isqrt(limit + 1) - 1;

        for (int m = 1; m <= max_m; m++) {
            int max_n = Math.min(m - 1, (limit - m*m) / (2*m));
            for (int n = 1; n <= max_n; n++) {
                if (isCoprime(m, n) && (m - n) % 3 != 0) {
                    int b = 2 * m * n + n * n;
                    int c = m * m - n * n;
                    for (int x = b, y = c; x + y <= limit; x += b, y += c)
                        pairs.add(((long)x << 32) | y); // pack int pair into a long
                }
            }
        }
        return pairs;
    }

    /**
     * Sort the pairs in increasing order of (x,y), look up all pairs (x',y')
     * with x' = y. To do so, prepare an index array idx[x], whereby idx[x] =
     * smallest index i whose pair (x_i, y_i) satisfies x=x_i.
     */
    private static int[] prepare(LongArray pairs, int limit) {
        pairs.sort();

        int[] idx = new int[limit];
        Arrays.fill(idx, -1);

        for (int i = 0; i < pairs.length; i++) {
            int x = (int)(pairs.a[i] >>> 32);
            if (idx[x] == -1)
                idx[x] = i;
        }
        return idx;
    }

    /**
     * Splice 60 degree triangles (p,q,a), (q,r,b), (r,q,c) into a triangle (a,b,c).
     */
    private static long splice(LongArray pairs, int[] idx, int limit) {
        int p, q, r;
        Set<Long> values = new HashSet<>();

        for (int i = 0; i < pairs.length; i++) {
            p = (int)(pairs.a[i] >>> 32);
            q = (int)(pairs.a[i]);
            for (int k = idx[q]; k >= 0 && k < pairs.length; k++) {
                if (pairs.a[k] >>> 32 != q)
                    break;
                r = (int)pairs.a[k];
                if (p + q + r <= limit && isSatisfy(p, r))
                    values.add((long)(p + q + r));
            }
        }
        return values.stream().mapToLong(x->x).sum();
    }

    /**
     * Make sure the side a, b satisfy the 60 degree triangle:
     *     a^2 + ab + b^2 == c^2
     */
    private static boolean isSatisfy(long a, long b) {
        return isSquare(a*a + a*b + b*b);
    }

    public static void main(String[] args) {
        int n = 120000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
