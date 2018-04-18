package euler;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import euler.util.TotientSieve;

public final class Problem531 {
    private Problem531() {}

    static class Pair {
        long x, y;
    }

    static long exgcd(long a, long b, Pair p) {
        if (b == 0) {
            p.x = 1;
            p.y = 0;
            return a;
        } else {
            long r = exgcd(b, a % b, p);
            long t = p.x;
            p.x = p.y;
            p.y = t - a / b *p.y;
            return r;
        }
    }

    static boolean merge(long a, long b, long m, Pair p) {
        if (m < 0)
            m = -m;
        if ((a %= m) < 0)
            a += m;
        if ((b %= m)  < 0)
            b += m;

        long d = exgcd(a, m, p);
        if (b % d != 0)
            return false;

        long x = (p.x % m + m) % m;
        x = x * (b / d) % m;

        p.x = m / d;
        p.y = x % p.x;
        return true;
    }

    static long chineseRemainder(long a, long n, long b, long m) {
        if (n < 0)
            n = -n;
        if (m < 0)
            m = -m;
        if ((a %= n) < 0)
            a = -a;
        if ((b %= m) < 0)
            b = -b;

        Pair p = new Pair();
        if (!merge(n, b - a, m, p))
            return 0;

        long t = p.x * n;
        return ((a + p.y * n) % t + t) % t;
    }

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
