package euler;

import euler.algo.FareySequence;
import euler.algo.Ratio;
import euler.util.RangedTask;

import static euler.algo.Library.isqrt;
import static euler.algo.Sublinear.moebius;
import static java.lang.Long.min;
import static java.lang.Math.sqrt;

public final class Problem370 {
    private Problem370() {}

    private static final double PHI = (1 + sqrt(5)) / 2;

    @SuppressWarnings("unused")
    public static long bruteForce(long limit) {
        FareySequence farey = new FareySequence((long)sqrt(limit / 3));
        long sum = 0;

        for (Ratio p : farey.ascending(Ratio.ONE)) {
            long a = p.numer(), b = p.denom();
            if (a > b * PHI)
                break;
            sum += limit / ((a + b) * (a + b) - a * b);
        }
        return sum;
    }

    private static class Solver {
        final long limit;

        byte[] mu;  // Mobius number μ(n)
        int[]  M;   // Mertens number M(n) = Σμ(i) for i = 1 to n
        int[]  SQF; // count of square-free numbers Σ|μ(i)| for i = 1 to n

        Solver(long limit) {
            this.limit = limit;
            init(((int)isqrt(limit / (2*2+2*3+3*3)) + 1) * 16);
        }

        void init(int limit) {
            mu  = moebius(limit);
            M   = new int[limit];
            SQF = new int[limit];

            // compute M(n), and count of square-free numbers
            int nsqf = 0; // count of square-free numbers
            for (int n = 1; n < limit; n++) {
                M[n] = M[n-1] + mu[n];
                if (mu[n] != 0)
                    nsqf++;
                SQF[n] = nsqf;
            }
        }

        // Returns the count of square-free numbers <= n
        long sqf(long n) {
            // return precomputed value
            if (n < SQF.length) {
                return SQF[(int)n];
            }

            // for larger one we combine Σμ(k)[n/k^2] for k=1 to sqrt(n)
            // and Σ(n/k^2)(M(sqk)-M(k-1)) for {k,sqk} to sqrt(n)
            // where sqk is the highest usable k that gives the same quotient as k
            long nsqf = n;
            int maxk = (int)isqrt(n);
            int k;

            // summing with Σμ(k)[n/k^2]
            for (k = 2; k <= maxk/7; k++) {
                nsqf += mu[k] * n / k / k;
            }

            // change to Σ(n/k^2)(M(sqk)-M(k-1)) to cover the tail
            for (; k <= maxk; k++) {
                long q = n / k / k;
                int sqk = (int)isqrt(n / q);
                if (sqk > maxk)
                    sqk = maxk;
                nsqf += q * (M[sqk] - M[k-1]);
                k = sqk;
            }

            return nsqf;
        }

        @SuppressWarnings("serial")
        class Stage1 extends RangedTask<Long> {
            Stage1(int from, int to) {
                super(from, to);
            }

            @Override
            protected Long compute(int from, int to) {
                long count = 0;
                for (long a = from; a <= to; a++) {
                    long maxb = (long)(a * PHI);
                    for (long b = a + 1; b <= maxb; b++)
                        count += sqf(limit / (a*a + a*b + b*b));
                }
                return count;
            }

            @Override
            protected Long combine(Long v1, Long v2) {
                return v1 + v2;
            }

            @Override
            protected RangedTask<Long> fork(int from, int to) {
                return new Stage1(from, to);
            }
        }

        @SuppressWarnings("serial")
        class Stage2 extends RangedTask<Long> {
            Stage2(int from, int to) {
                super(from, to);
            }

            @Override
            protected Long compute(int from, int to) {
                long count = 0;

                for (long a = from; a <= to; a++) {
                    long maxb = (long)(a * PHI);
                    for (long b = a + 1; b <= maxb; b++) {
                        long q = limit / (a*a + a*b + b*b);
                        long qsqf = sqf(q);

                        // smalest q to give same square-free result
                        if (q < SQF.length) {
                            while (qsqf == SQF[(int)q-1])
                                q--;
                        }

                        // solve the quadratic to provide the max b that gives the same quotient
                        long bsq = (isqrt(4*limit/q - 3*a*a) - a) / 2;
                        if (bsq > maxb)
                            bsq = maxb;
                        count += qsqf * (bsq - b + 1);
                        b = bsq;
                    }
                }
                return count;
            }

            @Override
            protected Long combine(Long v1, Long v2) {
                return v1 + v2;
            }

            @Override
            protected RangedTask<Long> fork(int from, int to) {
                return new Stage2(from, to);
            }
        }

        public long solve() {
            long count = limit / 3; // for equilateral triangles
            int  max_a = (int)(isqrt((4 * limit - 1)/3) - 1)/2;
            int  mid_a = (int)(isqrt((2 * limit - 1)/3) + 1)/2;

            // start with a=2 where quotient varies rapidly
            Stage1 stage1 = new Stage1(2, mid_a/60);
            stage1.fork();

            // continue where the quotient varies more slowly
            Stage2 stage2 = new Stage2(mid_a/60+1, mid_a);
            stage2.fork();

            // continue where quotient is guaranteed to be 1
            for (long a = mid_a + 1; a <= max_a; a++) {
                long b = min((long)(a*PHI), (isqrt(4*limit - 3*a*a) - a) / 2);
                count += b - a;
            }

            return count + stage1.join() + stage2.join();
        }
    }

    public static long solve(long limit) {
        return new Solver(limit).solve();
    }

    public static void main(String[] args) {
        long n = (long)2.5e13;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
