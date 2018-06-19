package euler;

import euler.util.LongRangedTask;

public final class Problem154 {
    private Problem154() {}

    private static class Solver {
        private final int a1, a2, layers;
        private final int[] sum1, sum2;

        Solver(int p1, int a1, int p2, int a2, int layers) {
            int[] sum1 = new int[layers + 2];
            int[] sum2 = new int[layers + 2];

            int c1 = 0, c2 = 0;
            for (int i = 1; i <= layers; i++) {
                int current = i;

                while (current % p1 == 0) {
                    current /= p1;
                    c1++;
                }
                sum1[i] = c1;

                while (current % p2 == 0) {
                    current /= p2;
                    c2++;
                }
                sum2[i] = c2;
            }

            this.a1 = a1;
            this.a2 = a2;
            this.layers = layers;
            this.sum1 = sum1;
            this.sum2 = sum2;
        }

        public long solve() {
            return LongRangedTask.parallel(0, layers, this::solve);
        }

        public long solve(int from, int to) {
            int[] sum1 = this.sum1;
            int[] sum2 = this.sum2;
            long result = 0;

            for (int i = from; i <= to; i++) {
                int s1 = choose(sum1, layers, i);
                int s2 = choose(sum2, layers, i);

                if (s1 >= a1 && s2 >= a2) {
                    result += i + 1;
                    continue;
                }

                for (int j = 0; j <= (i+1)/2; j++) {
                    if (s1 + choose(sum1, i, j) >= a1 && s2 + choose(sum2, i, j) >= a2) {
                        result++;
                        if (j < i / 2)
                            result++;
                    }
                }
            }
            return result;
        }

        private static int choose(int[] sums, int n, int k) {
            return sums[n] - (sums[n - k] + sums[k]);
        }
    }

    public static long solve(int p1, int a1, int p2, int a2, int layers) {
        Solver solver = new Solver(p1, a1, p2, a2, layers);
        return solver.solve();
    }

    public static void main(String[] args) {
        System.out.println(solve(2, 12, 5, 12, 200000));
    }
}
