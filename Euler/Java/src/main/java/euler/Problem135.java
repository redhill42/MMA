package euler;

import java.util.stream.IntStream;

public class Problem135 {
    private final int limit;

    public Problem135(int limit) {
        this.limit = limit;
    }

    public long solve() {
        int[] solutions = new int[limit + 1];
        for (int x = 1; x <= limit; x++) {
            for (int y = 1; x * y <= limit; y++) {
                if ((x + y) % 4 == 0 && 3 * y > x) {
                    solutions[x * y]++;
                }
            }
        }
        return IntStream.of(solutions).filter(x -> x == 10).count();
    }

    public static void main(String[] args) {
        Problem135 solver = new Problem135(1000000);
        System.out.println(solver.solve());
    }
}