package euler;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import euler.util.TotientSieve;
import static euler.util.Utils.chineseRemainder;

public final class Problem531 {
    private Problem531() {}

    public static long solve(int start, int end) {
        TotientSieve sieve = new TotientSieve(end);
        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new SolveTask(sieve, start, end - 1, end));
        pool.shutdown();
        return result;
    }

    @SuppressWarnings("serial")
    private static class SolveTask extends RecursiveTask<Long> {
        private final TotientSieve sieve;
        private final int from, to, end;

        SolveTask(TotientSieve sieve, int from, int to, int end) {
            this.sieve = sieve;
            this.from = from;
            this.to = to;
            this.end = end;
        }

        @Override
        public Long compute() {
            if (to - from < 100) {
                long sum = 0;
                for (int n = from; n <= to; n++) {
                    for (int m = n + 1; m < end; m++)
                        sum += chineseRemainder(sieve.phi(n), n, sieve.phi(m), m);
                }
                return sum;
            } else {
                int middle = (from + to) / 2;
                SolveTask left = new SolveTask(sieve, from, middle, end);
                SolveTask right = new SolveTask(sieve, middle + 1, to, end);
                left.fork();
                right.fork();
                return left.join() + right.join();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000, 1_005_000));
    }
}
