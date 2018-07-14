package euler;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import static euler.algo.Library.big;
import static euler.algo.Library.even;
import static euler.algo.Library.modmul;
import static euler.algo.Library.pow;
import static euler.algo.Library.tri;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public final class Problem325 {
    private Problem325() {}

    private static final double PHI = (Math.sqrt(5) + 1) / 2;

    @SuppressWarnings("unused")
    private static int[][] getSG(int max) {
        int[][] sg = new int[max + 1][max + 1];
        BitSet hash = new BitSet(max + 1);

        for (int a = 1; a < max; a++) {
            hash.clear();
            for (int b = a + 1; b <= max; b++) {
                for (int k = a; k <= b; k += a) {
                    if (a < b - k)
                        hash.set(sg[a][b - k]);
                    else
                        hash.set(sg[b - k][a]);
                }
                sg[a][b] = hash.nextClearBit(0);
            }
        }
        return sg;
    }

    @SuppressWarnings("unused")
    public static long bruteForce(long N, long M) {
        long N0  = (int)(N / PHI);

        long S1 = 0;
        for (long n = 1; n <= N0; n++) {
            long k = (long)((PHI - 1) * n);
            S1 += modmul(n + n, k, M);
            S1 += tri(k, M);
            S1 %= M;
        }

        long S2 = modmul(N, N+1, M) - modmul(N0, N0+1, M);
        if (even(S2))
            S2 = modmul(S2/2, N - N0 - 1, M);
        else
            S2 = modmul(S2, (N - N0 - 1)/2, M);

        return (S1 + S2) % M;
    }

    private static class Solver {
        private final Map<Long, BigInteger> memoF = new HashMap<>();
        private final Map<Long, BigInteger> memoG = new HashMap<>();

        private static final BigInteger SIX = big(6);

        Solver() {
            memoF.put(0L, ZERO);
            memoG.put(0L, ZERO);
        }

        public BigInteger solve(long n) {
            BigInteger N = big(n);
            return N.multiply(N.add(ONE))
                    .multiply(N.shiftLeft(2).subtract(ONE))
                    .divide(SIX)
                    .subtract(F(n));
        }

        private BigInteger F(long n) {
            BigInteger res = memoF.get(n);
            if (res != null)
                return res;

            BigInteger x = big(n);
            BigInteger t = big((long)(n / PHI));

            res = x.multiply(t).multiply(t.add(ONE)).shiftRight(1);
            res = res.subtract(x.pow(3).subtract(x).divide(SIX));
            res = res.add(G(n)).subtract(G((long)(n / PHI)));
            memoF.put(n, res);
            return res;
        }

        private BigInteger G(long n) {
            BigInteger res = memoG.get(n);
            if (res != null)
                return res;

            BigInteger x = big(n);
            BigInteger t = big((long)(n / PHI));

            res = x.multiply(x.add(ONE)).multiply(t).shiftRight(1);
            res = res.add(x.multiply(x.add(ONE)).multiply(x.shiftLeft(1).add(ONE)).divide(SIX));
            res = res.subtract(F((long)(n / PHI)));
            memoG.put(n, res);
            return res;
        }
    }

    public static BigInteger solve(long n) {
        return new Solver().solve(n);
    }

    public static long solve(long n, long m) {
        Solver solver = new Solver();
        return solver.solve(n).mod(big(m)).longValue();
    }

    public static void main(String[] args) {
        long n = (long)1e16, m = pow(7, 10);
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        if (args.length > 1)
            m = Long.parseLong(args[1]);
        if (m <= 0)
            m = (long)1e18;
        System.out.println(solve(n, m));
    }
}
