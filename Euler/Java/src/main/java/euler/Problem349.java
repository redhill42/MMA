package euler;

import static euler.algo.Library.mod;

public final class Problem349 {
    private Problem349() {}

    private static final int CYCLE = 104;

    private static class Ant {
        boolean[][] grid;
        int x, y, dx, dy;
        int count;

        Ant(int dim) {
            grid = new boolean[dim][dim];
            x = dim / 2;
            y = dim / 2;
            dx = 0;
            dy = -1;
        }

        void step() {
            if (grid[x][y]) {
                // on black square, rotate 90 degree counterclockwise
                int t = dx;
                dx = -dy;
                dy = t;

                grid[x][y] = false;
                count--;
            } else {
                // on white square, rotate 90 degree clockwise
                int t = dx;
                dx = dy;
                dy = -t;

                grid[x][y] = true;
                count++;
            }

            // forward one square
            x += dx;
            y += dy;
        }

        int numberOfBlacks() {
            return count;
        }

        public int[][] toArray() {
            int n = grid.length;
            int[][] a = new int[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    a[i][j] = grid[i][j] ? 1 : 0;
            return a;
        }
    }

    public static long solve(long maxSteps) {
        Ant ant = new Ant(80);
        int steps = 0;

        // The first 10000 steps are in chaos, we just leave the ant to walk.
        int remainder = (int)(maxSteps % CYCLE);
        int chaos = 10000 + mod(remainder - 10000 % CYCLE, CYCLE);
        while (steps < chaos) {
            ant.step();
            steps++;
        }

        // Emergent order. Finally the ant starts building a recurrent "highway"
        // pattern of 104 steps that repeats indefintely.
        int previous = ant.numberOfBlacks();
        for (int i = 0; i < CYCLE; i++) {
            ant.step();
            steps++;
        }

        int diff = ant.numberOfBlacks() - previous;
        return ant.numberOfBlacks() + (maxSteps - steps) / CYCLE * diff;
    }

    public static void main(String[] args) {
        System.out.println(solve((long)1e18));
    }
}
