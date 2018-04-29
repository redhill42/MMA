package euler;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import euler.util.SegmentedSieve;
import static euler.util.Utils.modpow;

public final class Problem429 {
    private Problem429() {}

    private static class SolveTask implements Callable<Long> {
        private final int n, m;
        private final SegmentedSieve.Segment primes;

        SolveTask(int n, int m, SegmentedSieve.Segment primes) {
            this.n = n;
            this.m = m;
            this.primes = primes;
        }

        @Override
        public Long call() {
            long r = 1;
            int p;
            while ((p = (int)primes.next()) > 0) {
                int a = v(n, p);
                r = r * (1 + modpow(p, 2 * a, m)) % m;
            }
            return r;
        }

        private static int v(int n, int p) {
            int s = n;
            for (int t = n; t != 0; t /= p)
                s -= t % p;
            return s / (p - 1);
        }
    }

    public static long solve(int n, int m)
        throws ExecutionException, InterruptedException
    {
        SegmentedSieve sieve = new SegmentedSieve(n);
        SegmentedSieve.Segment[] blocks = sieve.partition();

        ForkJoinPool pool = new ForkJoinPool();
        SolveTask[] tasks = new SolveTask[blocks.length];
        Arrays.setAll(tasks, i -> new SolveTask(n, m, blocks[i]));

        long result = 1;
        for (Future<Long> res : pool.invokeAll(Arrays.asList(tasks))) {
            result = result * res.get() % m;
        }

        pool.shutdown();
        return result;
    }

    public static void main(String[] args)
        throws ExecutionException, InterruptedException
    {
        System.out.println(solve(100_000_000, 1_000_000_009));
    }
}
