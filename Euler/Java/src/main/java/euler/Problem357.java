package euler;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import euler.util.PrimeSieve;
import static euler.util.Utils.isqrt;

public final class Problem357 {
    private Problem357() {}

    private static class Solver {
        private final int limit;
        private final PrimeSieve sieve;

        public Solver(int limit) {
            this.limit = limit;
            this.sieve = new PrimeSieve(limit);
        }

        public long solve() {
            ForkJoinPool pool = new ForkJoinPool();
            long result = pool.invoke(new SolveTask(sieve, 2, limit));
            pool.shutdown();
            return result;
        }
    }

    @SuppressWarnings("serial")
    private static class SolveTask extends RecursiveTask<Long> {
        private final PrimeSieve sieve;
        private final int from, to;

        SolveTask(PrimeSieve sieve, int from, int to) {
            this.sieve = sieve;
            this.from = from;
            this.to = to;
        }

        @Override
        public Long compute() {
            if (to - from < 100) {
                long sum = 0;
                for (int i = from; i <= to; i++) {
                    if (sieve.isPrime(i) && check(i - 1))
                        sum += i - 1;
                }
                return sum;
            } else {
                int middle = (from + to) / 2;
                SolveTask left = new SolveTask(sieve, from, middle);
                SolveTask right = new SolveTask(sieve, middle + 1, to);
                left.fork();
                right.fork();
                return left.join() + right.join();
            }
        }

        private boolean check(int n) {
            for (int i = 1, sq = isqrt(n); i <= sq; i++) {
                if (n % i == 0 && !sieve.isPrime(i + n / i))
                    return false;
            }
            return true;
        }
    }

    public static long solve(int limit) {
        return new Solver(limit).solve();
    }

    public static void main(String[] args) {
        System.out.println(solve(100_000_000));
    }
}