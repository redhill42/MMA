package euler;

public final class Problem154 {
    private Problem154() {}

    private static class Solver {
        private static final int LAYERS = 200000;
        private static final int PRIME1 = 2;
        private static final int EXPONENT1 = 12;
        private static final int PRIME2 = 5;
        private static final int EXPONENT2 = 12;

        private final int[] sum1 = new int[LAYERS + 2];
        private final int[] sum2 = new int[LAYERS + 2];

        public Solver() {
            int count1 = 0, count2 = 0;
            for (int i = 1; i <= LAYERS; i++) {
                int current = i;

                while (current % PRIME1 == 0) {
                    current /= PRIME1;
                    count1++;
                }
                sum1[i] = count1;

                while (current % PRIME2 == 0) {
                    current /= PRIME2;
                    count2++;
                }
                sum2[i] = count2;
            }
        }

        private static int choose(int[] sums, int n, int k) {
            return sums[n] - (sums[n - k] + sums[k]);
        }

        public long solve() {
            long result = 0;

            for (int i = 0; i <= LAYERS; i++) {
                int s1 = choose(sum1, LAYERS, i);
                int s2 = choose(sum2, LAYERS, i);

                if (s1 >= EXPONENT1 && s2 >= EXPONENT2) {
                    result += i + 1;
                    continue;
                }

                for (int j = 0; j <= (i+1)/2; j++) {
                    if (s1 + choose(sum1, i, j) >= EXPONENT1 &&
                        s2 + choose(sum2, i, j) >= EXPONENT2) {
                        result++;
                        if (j < i / 2)
                            result++;
                    }
                }
            }
            return result;
        }
    }

    public static long solve() {
        return new Solver().solve();
    }

    public static void main(String[] args) {
        System.out.println(solve());
    }
}
