package euler;

import static euler.algo.Library.big;
import static euler.algo.Library.digitSum;
import static euler.algo.Library.isSquare;
import static euler.algo.Library.isqrt;

public final class Problem80 {
    private Problem80() {}

    public static long solve(int limit, int len) {
        int sum = 0;
        for (int n = 2; n <= limit; n++)
            if (!isSquare(n))
                sum += rootDigitSum(n, len);
        return sum;
    }

    private static int rootDigitSum(int x, int len) {
        for (int y = x; y > 0; y /= 100)
            len--;
        return digitSum(isqrt(big(x).multiply(big(10).pow(len*2))));
    }

    public static void main(String[] args) {
        int n = 100, len = 100;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        if (args.length > 1)
            len = Integer.parseInt(args[1]);
        System.out.println(solve(n, len));
    }
}
