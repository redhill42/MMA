package euler;

import java.util.Arrays;

public final class Problem213 {
    private Problem213() {}

    public static double solve(int dim, int rounds) {
        // the probability of empty squares are initially set to 100%
        double[][] empty = new double[dim][dim];
        for (int i = 0; i < dim; i++)
            Arrays.fill(empty[i], 1.0);

        // simulate jumps for each flea
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                double[][] grid = simulate(x, y, dim, rounds);
                for (int i = 0; i < dim; i++)
                    for (int j = 0; j < dim; j++)
                        empty[i][j] *= 1 - grid[i][j];
            }
        }

        // sum of the probability of empty squares is the number of
        // expected empty squares
        double res = 0;
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                res += empty[i][j];
        return res;
    }

    private static double[][] simulate(int x, int y, int dim, int rounds) {
        double[][] grid = new double[dim][dim];

        // the flea is initially occupy the given position
        grid[x][y] = 1.0;

        // compute the probability for each square the flea can occupy
        for (int r = 0; r < rounds; r++) {
            double[][] next = new double[dim][dim];

            for (int i = 0; i < dim; i++)
                for (int j = 0; j < dim; j++) {
                    if (grid[i][j] == 0)
                        continue;

                    int dirs = 4;
                    if (i == 0 || i == dim-1)
                        dirs--;
                    if (j == 0 || j == dim-1)
                        dirs--;

                    // the probability for flea can jump to adjacent square
                    double p = grid[i][j] / dirs;
                    if (i > 0)
                        next[i-1][j] += p;
                    if (i+1 < dim)
                        next[i+1][j] += p;
                    if (j > 0)
                        next[i][j-1] += p;
                    if (j+1 < dim)
                        next[i][j+1] += p;
                }

            // prepare for next round
            grid = next;
        }

        return grid;
    }

    public static void main(String[] args) {
        System.out.printf("%.6f%n", solve(30, 50));
    }
}
