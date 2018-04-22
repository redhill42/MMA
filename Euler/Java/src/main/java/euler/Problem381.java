package euler;

import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import euler.util.PrimeSieve;

public final class Problem381 {
    private Problem381() {}

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
            if (to - from <= 1000) {
                long sum = 0;
                int p = sieve.nextPrime(from - 1);
                while (p > 0 && p <= to) {
                    sum += ((3 * p % 8) * p - 3) / 8;
                    p = sieve.nextPrime(p);
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
    }

    public static long solve(int limit) {
        PrimeSieve sieve = new PrimeSieve(limit);
        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new SolveTask(sieve, 5, limit-1));
        pool.shutdown();
        return result;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        System.out.println(solve(n));
    }
}
