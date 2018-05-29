package euler;

import euler.algo.PrimeSieve;
import static euler.algo.Library.pow;
import static java.lang.Math.ceil;
import static java.lang.Math.log;

public final class Problem333 {
    private Problem333() {}

    private static class Solver {
        private final int limit;
        private final int[] counter;

        Solver(int limit) {
            this.limit = limit;
            this.counter = new int[limit];
        }

        public long solve() {
            search(2, 3);
            search(3, 2);

            PrimeSieve sieve = new PrimeSieve(limit);
            long sum = 5; // 2 and 3 are not sum but valid partition
            for (int p = 2; p > 0; p = sieve.nextPrime(p))
                if (counter[p] == 1)
                    sum += p;
            return sum;
        }

        // a prime number can only be partitioned with 2^i + 3^j + zero or more 2^i*3^j
        private void search(int p, int q) {
            for (int a = 1, x = p; ; a++, x *= p) {
                int b = (int)ceil(a * log(p) / log(q)); // make sure y > x
                int y = (int)pow(q, b);
                if (x + y >= limit)
                    break;
                for (; x + y < limit; b++, y *= q) {
                    counter[x + y]++;
                    search2(x + y, p, a, q, 1, b);
                }
            }
        }

        private void search2(int s, int p, int a, int q, int b1, int b2) {
            for (int i = 1, x = p; i < a; i++, x *= p) {
                int y = (int)pow(q, b1);
                if (s + x * y >= limit)
                    break;
                for (int j = b1; j < b2 && s + x * y < limit; j++, y *= q) {
                    counter[s + x * y]++;
                    search2(s + x * y, p, i, q, j + 1, b2);
                }
            }
        }
    }

    public static long solve(int limit) {
        Solver solver = new Solver(limit);
        return solver.solve();
    }

    public static void main(String[] args) {
        int n = 1_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
