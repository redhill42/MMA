package euler;

import euler.algo.PrimeSieve;
import static euler.algo.Library.even;
import static euler.algo.Library.isqrt;

public final class Problem193 {
    private Problem193() {}

    public static class Solver {
        private final byte[] moebius;

        public Solver(long limit) {
            int dlimit = (int)isqrt(limit - 1);
            PrimeSieve sieve = new PrimeSieve(dlimit);

            byte[] moebius = new byte[dlimit + 1];
            for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
                moebius[p] = 1;
                for (int n = p + p; n <= dlimit; n += p) {
                    if (moebius[n] == -1 || (n / p) % p == 0) {
                        moebius[n] = -1;
                    } else {
                        moebius[n]++;
                    }
                }
            }
            this.moebius = moebius;
        }

        public long solve(long limit) {
            byte[] moebius = this.moebius;
            int dlimit = moebius.length;
            long q = 0;

            for (int d = 1; d < dlimit; d++) {
                int mu = moebius[d];
                if (mu != -1) {
                    mu = even(mu) ? 1 : -1;
                    q += ((limit - 1) / d / d) * mu;
                }
            }
            return q;
        }
    }

    public static long solve(long limit) {
        return new Solver(limit).solve(limit);
    }

    public static void main(String[] args) {
        System.out.println(solve(1L << 50));
    }
}
