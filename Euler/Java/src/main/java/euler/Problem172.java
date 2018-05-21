package euler;

import euler.algo.IntegerPartitions;
import static euler.algo.Library.choose;
import static euler.algo.Library.factorials;

public final class Problem172 {
    private Problem172() {}

    private static class Solver {
        final int numDigits, maxRepeats;
        final long[] factorials;

        Solver(int numDigits, int maxRepeats) {
            this.numDigits = numDigits;
            this.maxRepeats = maxRepeats;
            this.factorials = factorials(numDigits);
        }

        class Partitioner implements IntegerPartitions.PartitionOperator<Long> {
            final int numFixed;

            Partitioner(int numFixed) {
                this.numFixed = numFixed;
            }

            @Override
            public Long apply(Long sum, int[] a, int k) {
                if (k > 9)
                    return sum;

                long n = factorials[numDigits - 1];
                int[] repeats = new int[maxRepeats + 1];

                n /= factorials[numFixed];
                for (int i = 0; i < k; i++) {
                    if (a[i] > maxRepeats)
                        return sum;
                    repeats[a[i]]++;
                    n /= factorials[a[i]];
                }

                n *= 9;
                int digits = 9;
                for (int i = 1; i <= maxRepeats; i++) {
                    n *= choose(digits, repeats[i]).longValue();
                    digits -= repeats[i];
                }

                return sum + n;
            }

            long compute() {
                return IntegerPartitions.partitions(numDigits - numFixed - 1, 0L, this);
            }
        }

        public long solve() {
            long sum = 0;
            for (int i = 0; i < maxRepeats; i++) {
                Partitioner p = new Partitioner(i);
                sum += p.compute();
            }
            return sum;
        }
    }

    public static long solve(int numDigits, int maxRepeats) {
        Solver solver = new Solver(numDigits, maxRepeats);
        return solver.solve();
    }

    public static void main(String[] args) {
        System.out.println(solve(18, 3));
    }
}
