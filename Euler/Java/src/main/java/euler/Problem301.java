package euler;

import static euler.algo.Library.exponent;
import static euler.algo.Library.fibonacci;
import static euler.algo.Library.isPowerOfTwo;
import static euler.algo.Library.pow;

public final class Problem301 {
    private Problem301() {}

    public static int solve(int limit) {
        if (isPowerOfTwo(limit)) {
            // this is work but I don't know why
            return (int)fibonacci(exponent(limit, 2) + 2);
        }

        int sum = 0;
        for (int n = 1; n <= limit; n++)
            if ((n ^ (2*n) ^ (3*n)) == 0)
                sum++;
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solve((int)pow(2, 30)));
    }
}
