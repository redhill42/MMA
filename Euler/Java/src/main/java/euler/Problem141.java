package euler;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static euler.util.Utils.gcd;
import static euler.util.Utils.isSquare;

public final class Problem141 {
    private Problem141() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RecursiveTask<Long> {
        private final long from, to, limit;

        SolveTask(long limit) {
            this.from = 2;
            this.to = (long)Math.pow(limit, 1.0/3);
            this.limit = limit;
        }

        SolveTask(long from, long to, long limit) {
            this.from = from;
            this.to = to;
            this.limit = limit;
        }

        @Override
        public Long compute() {
            if (to - from <= 100) {
                return compute(from, to, limit);
            } else {
                long middle = (from + to) / 2;
                SolveTask left = new SolveTask(from, middle, limit);
                SolveTask right = new SolveTask(middle + 1, to, limit);
                left.fork();
                right.fork();
                return left.join() + right.join();
            }
        }

        static long compute(long from, long to, long limit) {
            long sum = 0;
            for (long a = from; a <= to; a++) {
                for (long b = 1; b < a; b++) {
                    if (gcd(a, b) != 1)
                        continue;
                    for (long c = 1; c < limit; c++) {
                        long n = a*a*a*b*c*c + b*b*c;
                        if (n > limit)
                            break;
                        if (isSquare(n))
                            sum += n;
                    }
                }
            }
            return sum;
        }
    }

    public static long solve(long limit) {
        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new SolveTask(limit));
        pool.shutdown();
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000_000_000L));
    }
}
