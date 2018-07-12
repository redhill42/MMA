package euler;

import java.util.Arrays;

import euler.util.LongRangedTask;

import static euler.algo.Library.factorize;
import static euler.algo.Library.lcm;
import static euler.algo.Library.pow;
import static java.lang.Math.max;

public final class Problem411 {
    private Problem411() {}

    public static int longestSubsequence(long[] X) {
        int N = X.length;
        int[] M = new int[N + 1];
        int L = 0;

        for (int i = 0; i < N; i++) {
            // Binary search for the largest positive j <= L
            // such that X[M[j]] < X[i]
            int lo = 1, hi = L;
            while (lo <= hi) {
                int mid = (lo + hi + 1) >>> 1;
                if (X[M[mid]] <= X[i])
                    lo = mid + 1;
                else
                    hi = mid - 1;
            }

            // After searching, lo is 1 greater than the
            // length of the longest prefix of X[i]
            int newL = lo;

            // The predecessor of X[i] is the last index of
            // the subsequence of length newL-1
            M[newL] = i;

            if (newL > L) {
                // If we found a subsequence longer than any we've
                // found yet, update L
                L = newL;
            }
        }

        return L;
    }

    private static int S(int k) {
        int n = (int)pow(k, 5);

        // The value n=27^5=3^15 is special since all points starting from
        // i=15 are of the form (x,0) so in this case we could simply returning
        // the number of these points. The number of these points is the period
        // of 2^i mod 27^5, which is the multiplicative order of 2 modulo 27^5.
        if (k == 27) {
            return (int)factorize(n).ord(2);
        }

        // When n=k^5 is coprime to 2 and 3, we could consider only φ(n)
        // first values of i since point (2^i mod n, 3^i mod n) = (1,1)
        // for i=φ(n) and we have the cycle.  Precisely, the period of cycle
        // is lcm(ord(2,n), ord(3,n)), where lcm represents least common
        // divisor and ord(a,n) is a's multiplicative order modulo n.
        int period = period(n);
        long[] coords = new long[period];

        long x = 1, y = 1;
        for (int i = 0; i < period; i++) {
            coords[i] = (x << 32) + y;
            x = x * 2 % n;
            y = y * 3 % n;
        }

        // sort coordinates by x and y, only y needs to keep
        Arrays.sort(coords);
        for (int i = 0; i < period; i++)
            coords[i] = (int)coords[i];
        return longestSubsequence(coords);
    }

    private static int period(int n) {
        int m, a2, a3;
        long p2, p3;

        for (a2 = 0, m = n; m % 2 == 0; m /= 2)
            a2++;
        p2 = factorize(m).ord(2);

        for (a3 = 0, m = n; m % 3 == 0; m /= 3)
            a3++;
        p3 = factorize(m).ord(3);

        return max(a2, a3) + (int)lcm(p2, p3);
    }

    public static long solve(int n) {
        return LongRangedTask.parallel(1, n, 0, (from, to) -> {
            long sum = 0;
            for (int k = from; k <= to; k++)
                sum += S(k);
            return sum;
        });
    }

    public static void main(String[] args) {
        int n = 30;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
