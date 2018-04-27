package euler;

import static java.lang.Integer.min;

public final class Problem150 {
    private Problem150() {}

    private static class RandomGenerator {
        private long t;

        public int next() {
            t = (615949 * t + 797807) % (1L << 20);
            return (int)(t - (1L << 19));
        }
    }

    public static int solve(int limit) {
        int[][] triangle = new int[limit][];
        RandomGenerator rg = new RandomGenerator();

        for (int i = 0; i < limit; i++) {
            triangle[i] = new int[i + 1];
            for (int j = 0; j <= i; j++) {
                triangle[i][j] = rg.next();
            }
        }
        return solve(triangle);
    }

    public static int solve(int[][] triangle) {
        // accumulate triangle summations from bottom to top
        int maxrows = triangle.length;
        int[][] accum = new int[maxrows][];
        for (int i = 0; i < maxrows; i++) {
            accum[i] = new int[i + 1];
        }

        int minval = 0;
        for (int row = 0; row < maxrows - 1; row++) {
            // make a clean copy from original triangle
            for (int i = 0; i <= row + 1; i++) {
                System.arraycopy(triangle[i], 0, accum[i], 0, triangle[i].length);
            }

            // accumulate last row in the sub-triangle
            for (int j = 0; j <= row; j++) {
                accum[row][j] += accum[row+1][j] + accum[row+1][j+1];
                minval = min(minval, accum[row][j]);
            }

            // accumulate intermeidate rows in the sub-triangle
            for (int i = row - 1; i >= 0; i--) {
                for (int j = 0; j <= i; j++) {
                    accum[i][j] += accum[i+1][j] + accum[i+1][j+1];
                    accum[i][j] -= accum[i+2][j+1];
                    minval = min(minval, accum[i][j]);
                }
            }
        }
        return minval;
    }

    public static void main(String[] args) {
        int limit = 1000;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
