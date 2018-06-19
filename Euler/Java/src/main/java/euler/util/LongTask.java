package euler.util;

/**
 * A task that compute series of input integers in a range, return the result
 * as a long.
 */
@FunctionalInterface
public interface LongTask {
    /**
     * Compute series of input integers in a range.
     *
     * @param from the lower bound of integer range
     * @param to the higher bound of integer range
     * @return the computation result
     */
    long compute(int from, int to);
}
