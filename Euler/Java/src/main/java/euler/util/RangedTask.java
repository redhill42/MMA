package euler.util;

import java.util.concurrent.RecursiveTask;

public abstract class RangedTask<V> extends RecursiveTask<V> {
    private static final long serialVersionUID = 2169885264130215721L;
    private final int from, to, segments;

    protected RangedTask(int from, int to) {
        this(from, to, 100);
    }

    protected RangedTask(int from, int to, int segments) {
        this.from = from;
        this.to = to;
        this.segments = segments;
    }

    protected abstract V compute(int from, int to);
    protected abstract V combine(V v1, V v2);
    protected abstract RangedTask<V> fork(int from, int to);

    @Override
    public V compute() {
        if (to - from <= segments) {
            return compute(from, to);
        }

        int middle = (from + to) / 2;
        RangedTask<V> left = fork(from, middle);
        RangedTask<V> right = fork(middle + 1, to);
        left.fork();
        right.fork();
        return combine(left.join(), right.join());
    }
}
