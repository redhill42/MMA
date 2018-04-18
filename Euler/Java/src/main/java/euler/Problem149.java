package euler;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.max;

public final class Problem149 {
    private Problem149() {}

    static class SampleGenerator {
        private final int[] sample;
        private int current = 1;

        SampleGenerator() {
            sample = new int[55];
            for (long k = 1; k <= 55; k++) {
                sample[(int)k - 1] = (int)((100003 - 200003*k + 300007*k*k*k) % 1000000 - 500000);
            }
        }

        public int next() {
            int k = current++;
            if (k <= 55) {
                return sample[k - 1];
            } else {
                int s1 = sample[(k - 25) % 55];
                int s2 = sample[(k - 56) % 55];
                return sample[(k - 1) % 55] = (s1 + s2 + 1000000) % 1000000 - 500000;
            }
        }
    }

    static class AccumMax {
        private int accum;

        int offer(int val, int maxval) {
            if ((accum += val) < 0)
                accum = 0;
            return max(accum, maxval);
        }

        void reset() {
            accum = 0;
        }
    }

    public static int solve(int dim) throws InterruptedException, ExecutionException {
        AccumMax horizontal = new AccumMax();
        AccumMax[] vertical = new AccumMax[dim];
        AccumMax[] diagonal = new AccumMax[2 * dim - 1];
        AccumMax[] antidiagonal = new AccumMax[2 * dim - 1];

        Arrays.setAll(vertical, i->new AccumMax());
        Arrays.setAll(diagonal, i->new AccumMax());
        Arrays.setAll(antidiagonal, i->new AccumMax());

        SampleGenerator g = new SampleGenerator();
        int maxval = 0;
        for (int i = 0; i < dim; i++) {
            horizontal.reset();
            for (int j = 0; j < dim; j++) {
                int val = g.next();
                maxval = horizontal.offer(val, maxval);
                maxval = vertical[j].offer(val, maxval);
                maxval = diagonal[dim + j - i - 1].offer(val, maxval);
                maxval = antidiagonal[i + j].offer(val, maxval);
            }
        }
        return maxval;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(solve(2000));
    }
}
