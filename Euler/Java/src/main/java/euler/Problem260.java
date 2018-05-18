package euler;

import java.util.BitSet;

public final class Problem260 {
    private Problem260() {}

    private static class Solver {
        private final int piles;

        Solver(int piles) {
            this.piles = piles;
        }

        // encode pair of two integers
        private int hash(int a, int b) {
            return a * (piles + 1) + b;
        }

        public long solve() {
            // compact array to record lose position
            BitSet one = new BitSet((piles + 1) * (piles + 1));
            BitSet two = new BitSet((piles + 1) * (piles + 1));
            BitSet all = new BitSet((piles + 1) * (piles + 1));

            long count = 0;

            for (int a = 0; a <= piles; a++) {
                for (int b = a; b <= piles; b++) {
                    if (one.get(hash(a, b))) // quick check
                        continue;
                    for (int c = b; c <= piles; c++) {
                        // can current player win by clearing one pile?
                        if (one.get(hash(a, c)) || one.get(hash(b, c)))
                            continue;

                        // can current player win by clearing one pile and
                        // removing the same number of stones from a second pile?
                        if (two.get(hash(b - a, c)) || two.get(hash(c - a, b)) || two.get(hash(c - b, a)))
                            continue;

                        // can current player win by clearing one pile and
                        // removing the same number of stones from all other piles?
                        if (all.get(hash(b - a, c - a)))
                            continue;

                        // current player is in the lose position
                        count += a + b + c;

                        // record the new lose position
                        one.set(hash(a, b));
                        one.set(hash(a, c));
                        one.set(hash(b, c));
                        two.set(hash(b - a, c));
                        two.set(hash(c - a, b));
                        two.set(hash(c - b, a));
                        all.set(hash(b - a, c - a));
                        break;
                    }
                }
            }
            return count;
        }
    }

    public static long solve(int piles) {
        Solver solver = new Solver(piles);
        return solver.solve();
    }

    public static void main(String[] args) {
        System.out.println(solve(1000));
    }
}
