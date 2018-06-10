package euler;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.TreeSet;

import static euler.algo.Library.modmul;
import euler.algo.PrimeSieve;

public final class Problem266 {
    private Problem266() {}

    @SuppressWarnings("ComparableImplementedButEqualsNotOverridden")
    private static class LogarithmAndMask implements Comparable<LogarithmAndMask> {
        final double logrithm;
        final long mask;

        LogarithmAndMask(double logrithm, long mask) {
            this.logrithm = logrithm;
            this.mask = mask;
        }

        @Override
        public int compareTo(LogarithmAndMask o) {
            return Double.compare(this.logrithm, o.logrithm);
        }
    }

    private static class Solver {
        private final int[] primes;
        private final double[] logarithms;
        private final double logSqrt;
        private final NavigableSet<LogarithmAndMask> precompute;

        Solver(int limit) {
            this.primes = new PrimeSieve(limit).getPrimes();
            this.logarithms = Arrays.stream(primes).mapToDouble(Math::log).toArray();
            this.logSqrt = Arrays.stream(logarithms).sum() / 2;
            this.precompute = new TreeSet<>();

            int start = logarithms.length / 2;
            int length = logarithms.length - start;
            for (long mask = 1; mask < 1 << length; mask++) {
                double sum = getLogarithm(mask, start);
                if (sum < logSqrt) {
                    precompute.add(new LogarithmAndMask(sum, mask << start));
                }
            }
        }

        long solve(long mod) {
            double max = 0;
            long maxMask = 0;

            int half = logarithms.length / 2;
            for (long mask = 1; mask < 1 << half; mask++) {
                double sum = getLogarithm(mask, 0);
                if (sum > logSqrt) {
                    continue;
                }

                LogarithmAndMask key = new LogarithmAndMask(logSqrt - sum, 0);
                LogarithmAndMask right = precompute.floor(key);
                if (right != null) {
                    sum += right.logrithm;
                    if (sum > max) {
                        max = sum;
                        maxMask = mask | right.mask;
                    }
                }
            }

            return getValue(maxMask, mod);
        }

        private double getLogarithm(long mask, int start) {
            double res = 0;
            for (int i = 0; mask != 0; i++, mask >>= 1) {
                if ((mask & 1) != 0)
                    res += logarithms[start + i];
            }
            return res;
        }

        private long getValue(long mask, long mod) {
            long product = 1;
            for (int i = 0; i < primes.length; i++, mask >>= 1)
                if ((mask & 1) != 0)
                    product = modmul(product, primes[i], mod);
            return product;
        }
    }


    public static long solve(int limit, long mod) {
        Solver solver = new Solver(limit);
        return solver.solve(mod);
    }

    public static void main(String[] args) {
        System.out.println(solve(190, (long)1e16));
    }
}
