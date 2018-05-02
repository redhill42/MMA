package euler;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import euler.util.SegmentedSieve;
import static euler.util.Utils.factorialExponent;

public final class Problem231 {
    private Problem231() {}

    private static class SolveTask implements Callable<Long> {
        private final long n, k;
        private final SegmentedSieve.Segment segment;

        SolveTask(long n, long k, SegmentedSieve.Segment segment) {
            this.n = n;
            this.k = k;
            this.segment = segment;
        }

        @Override
        public Long call() throws Exception {
            long p, a, r = 0;
            while ((p = segment.next()) > 0) {
                a  = factorialExponent(n, p);
                a -= factorialExponent(k, p);
                a -= factorialExponent(n - k, p);
                r += p * a;
            }
            return r;
        }
    }

    public static long solve(long n, long k)
        throws ExecutionException, InterruptedException
    {
        SegmentedSieve sieve = new SegmentedSieve(n);
        SegmentedSieve.Segment[] blocks = sieve.partition();

        ForkJoinPool pool = new ForkJoinPool();
        SolveTask[] tasks = new SolveTask[blocks.length];
        Arrays.setAll(tasks, i -> new SolveTask(n, k, blocks[i]));

        long result = 0;
        for (Future<Long> res : pool.invokeAll(Arrays.asList(tasks))) {
            result += res.get();
        }

        pool.shutdown();
        return result;
    }

    public static void main(String[] args)
        throws ExecutionException, InterruptedException
    {
        System.out.println(solve(10, 3));
        System.out.println(solve(20_000_000, 15_000_000));
    }
}
