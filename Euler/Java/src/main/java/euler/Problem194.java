package euler;

import java.math.BigInteger;
import static java.math.BigInteger.ONE;

import euler.algo.Graph;
import static euler.algo.Library.choose;
import static euler.algo.Library.big;

public final class Problem194 {
    private Problem194() {}

    public static long solve(int n, int m, int k, long mod) {
        Graph b = new Graph(7);
        b.addEdge(0, 1).addEdge(0, 2).addEdge(0, 5)
         .addEdge(1, 2).addEdge(1, 6).addEdge(2, 3)
         .addEdge(3, 4).addEdge(4, 5).addEdge(4, 6);

        Graph a = new Graph(b).addEdge(5, 6);

        BigInteger K = big(k);
        BigInteger ka = a.getChromaticPolynomial().evaluate(K);
        BigInteger kb = b.getChromaticPolynomial().evaluate(K);

        BigInteger res = ka.pow(n).multiply(kb.pow(m));
        res = res.divide(K.multiply(K.subtract(ONE)).pow(n+m-1));
        res = res.multiply(choose(n+m, n));
        res = res.mod(big(mod));
        return res.longValue();
    }

    public static void main(String[] args) {
        System.out.println(solve(25, 75, 1984, 100_000_000));
    }
}
