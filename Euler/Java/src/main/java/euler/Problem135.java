package euler;

import java.util.stream.IntStream;

public final class Problem135 {
    private Problem135() {}

    public static long solve(int limit) {
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
        System.out.println(solve(1000000));
    }
}