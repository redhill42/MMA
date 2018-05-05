package euler.util;

import java.util.Arrays;
import static java.lang.Integer.min;

/**
 * Utility class to generate integer partitions.
 */
public final class IntegerPartitions {
    private IntegerPartitions() {}

    @FunctionalInterface
    public interface PartitionOperator<T> {
        T apply(T t, int[] partition, int length);
    }

    @FunctionalInterface
    public interface PartitionConsumer extends PartitionOperator<Void> {
        void accept(int[] partition, int length);

        @Override
        default Void apply(Void t, int[] partition, int length) {
            accept(partition, length);
            return null;
        }
    }

    /**
     * Generate all unrestricted partitions of a positive integer.
     *
     * @param n the integer to be partitioned
     * @param consumer consumes the integer partitions
     */
    public static void partitions(int n, PartitionConsumer consumer) {
        partitions(n, null, consumer);
    }

    /**
     * Generate all unrestricted partitions of a positive integer.
     *
     * @param n the integer to be partitioned
     * @param z the identity value for the accumulator
     * @param accumulator accumulate the integer partitions
     * @see <a href="https://arxiv.org/pdf/0909.2331.pdf">Jerome Kelleher</a>
     */
    public static <T> T partitions(int n, T z, PartitionOperator<T> accumulator) {
        int[] a = new int[n + 1];
        int k, x, y;

        k = 1;
        y = n - 1;
        while (k != 0) {
            x = a[k - 1] + 1;
            k--;
            while (x + x <= y) {
                a[k++] = x;
                y -= x;
            }
            while (x <= y) {
                a[k] = x;
                a[k+1] = y;
                z = accumulator.apply(z, a, k + 2);
                x++;
                y--;
            }
            a[k] = x + y;
            y = x + y - 1;
            z = accumulator.apply(z, a, k + 1);
        }
        return z;
    }

    /**
     * Generate all partitions of a positive integer with restricted length and
     * maximum part.
     *
     * @param n the integer to be partitioned
     * @param k the length of partitions
     * @param p the maximum part in the partitions
     * @param consumer consumes the partitions
     */
    public static void partitions(int n, int k, int p, PartitionConsumer consumer) {
        partitions(n, k, p, null, consumer);
    }

    /**
     * Generate all partitions of a positive integer with restricted length and
     * maximum part.
     *
     * @param n the integer to be partitioned
     * @param k the length of partitions
     * @param p the maximum part in the partitions
     * @param z the identity value of the accumulator
     * @param accumulator accumulate the partitions
     */
    public static <T> T partitions(int n, int k, int p, T z, PartitionOperator<T> accumulator) {
        int[] a = new int[k];
        partition(a, n, k, p);
        do {
            z = accumulator.apply(z, a, k);
        } while (nextPartition(a));
        return z;
    }

    private static void partition(int[] a, int n, int k, int p) {
        for (int i = k; --i >= 0; ) {
            a[i] = min(p, n - i);
            n -= a[i];
        }
    }

    private static boolean nextPartition(int[] a) {
        if (a[1] >= a[0] + 2) {
            a[1]--;
            a[0]++;
            return true;
        }

        if (a[1] == a[0] + 1) {
            for (int j = 2; j < a.length; j++) {
                if (a[j] == a[0] + 1)
                    continue;
                if (a[j] == a[0] + 2) {
                    a[j]--;
                    a[0]++;
                    return true;
                }
            }
        }

        int k, s = 0;
        for (k = 0; k < a.length && a[k] - a[0] <= 1; k++)
            s += a[k];
        if (k == a.length)
            return false;
        partition(a, s + a[k], k + 1, a[k] - 1);
        return true;
    }

    /**
     * Generate all combinations of integers upto the given bound.
     *
     * @param k the length of combinations
     * @param p the maximum part in the combinations
     * @param consumer consums the combinations
     */
    public static void combinations(int k, int p, PartitionConsumer consumer) {
        combinations(k, p, null, consumer);
    }

    /**
     * Generate all combinations of integers upto the given bound.
     *
     * @param k the length of combinations
     * @param p the maximum part in the combinations
     * @param z the identity value of the accumulator
     * @param accumulator accumulate the combinations
     */
    public static <T> T combinations(int k, int p, T z, PartitionOperator<T> accumulator) {
        int[] a = new int[k];
        Arrays.fill(a, 0, k, 1);
        do {
            z = accumulator.apply(z, a, k);
        } while (nextCombination(a, k, p));
        return z;
    }

    private static boolean nextCombination(int[] a, int k, int p) {
        int i;
        for (i = k; --i >= 0 && a[i] >= p; )
            ;
        if (i < 0)
            return false;
        int x = ++a[i];
        while (++i < k)
            a[i] = x;
        return true;
    }
}
