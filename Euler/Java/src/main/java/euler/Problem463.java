package euler;

import java.util.HashMap;
import java.util.Map;

import static euler.algo.Library.mod;
import static euler.algo.Library.pow;

public final class Problem463 {
    private Problem463() {}

    private static class Solver {
        private final long modulo;
        private final Map<Long, Long> memo = new HashMap<>();

        Solver(long modulo) {
            this.modulo = modulo;
        }

        long solve(long n) {
            memo.put(0L, 0L);
            memo.put(1L, 1L);
            memo.put(2L, 2L);
            memo.put(3L, 5L);
            return S(n);
        }

        private long S(long x) {
            return memo.computeIfAbsent(x, t -> {
                long n = t / 4;
                switch ((int)(t % 4)) {
                case 0:
                    return mod(6*S(2*n) - 5*S(n) - 3*S(n-1) - 1, modulo);
                case 1:
                    return mod(2*S(2*n+1) + 4*S(2*n) - 6*S(n) - 2*S(n-1) - 1, modulo);
                case 2:
                    return mod(3*S(2*n+1) + 3*S(2*n) - 6*S(n) - 2*S(n-1) - 1, modulo);
                case 3:
                    return mod(6*S(2*n+1) - 8*S(n) - 1, modulo);
                default:
                    throw new Error("imposible");
                }
            });
        }
    }

    public static long solve(long n, long m) {
        return new Solver(m).solve(n);
    }

    public static void main(String[] args) {
        System.out.println(solve(pow(3, 37), pow(10, 9)));
    }
}
