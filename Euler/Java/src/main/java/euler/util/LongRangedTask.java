package euler.util;

/**
 * Convenient ForkJoinTask to compute series input integers in a range.
 * Summing partial result to get the final result.
 */
public class LongRangedTask extends RangedTask<Long> {
    private static final long serialVersionUID = -2906321794613741178L;
    private final LongTask task;

    public LongRangedTask(int from, int to, LongTask task) {
        super(from, to);
        this.task = task;
    }

    public LongRangedTask(int from, int to, int segments, LongTask task) {
        super(from, to, segments);
        this.task = task;
    }

    @Override
    protected Long compute(int from, int to) {
        return task.compute(from, to);
    }

    @Override
    protected Long combine(Long v1, Long v2) {
        return v1 + v2;
    }

    @Override
    protected RangedTask<Long> fork(int from, int to) {
        return new LongRangedTask(from, to, task);
    }

    /**
     * Parallel compute ranged inputs.
     */
    public static long parallel(int from, int to, LongTask task) {
        return new LongRangedTask(from, to, task).invoke();
    }

    /**
     * Parallel compute ranged inputs.
     */
    public static long parallel(int from, int to, int segments, LongTask task) {
        return new LongRangedTask(from, to, segments, task).invoke();
    }

    /**
     * Sequential compute ranged inputs for test purposes.
     */
    @SuppressWarnings("unused")
    public static long sequential(int from, int to, LongTask task) {
        return task.compute(from, to);
    }

    /**
     * Sequential compute ranged inputs for test purposes.
     */
    @SuppressWarnings("unused")
    public static long sequential(int from, int to, int segments, LongTask task) {
        return task.compute(from, to);
    }
}
