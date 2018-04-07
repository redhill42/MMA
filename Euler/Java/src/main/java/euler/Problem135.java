package euler;

import java.util.stream.IntStream;

public class Problem135 {
    static final int LIMIT = 1000000;

    public long solve() {
        int[] solutions = new int[LIMIT + 1];
        for (int x = 1; x <= LIMIT; x++) {
            for (int y = 1; x * y <= LIMIT; y++) {
                if ((x + y) % 4 == 0 && 3 * y > x) {
                    solutions[x * y]++;
                }
            }
        }
        return IntStream.of(solutions).filter(x -> x == 10).count();
    }

    public static void main(String[] args) {
        System.out.println(new Problem135().solve());
    }
}