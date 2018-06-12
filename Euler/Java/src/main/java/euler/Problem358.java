package euler;

import static euler.algo.Library.factorize;
import static euler.algo.Library.isPrime;
import static euler.algo.Library.modinv;
import static euler.algo.Library.pow;
import static java.lang.Math.log10;

public final class Problem358 {
    private Problem358() {}

    public static long solve(int leadingZeros, int leadingDigits, int trailingDigits) {
        long mod = pow(10, integerLength(trailingDigits));
        long additive = mod - modinv(trailingDigits, mod);
        long low = recip(leadingZeros, leadingDigits + 1);
        long high = recip(leadingZeros, leadingDigits);

        low = low - low % mod + additive;
        for (long p = low; p <= high; p += mod)
            if (isPrime(p) && factorize(p).isPrimitiveRoot(10))
                return 9 * (p - 1) / 2;
        return -1;
    }

    private static int integerLength(int n) {
        return (int)(log10(n) + 1);
    }

    private static long recip(int leadingZeros, int leadingDigits) {
        return (long)(Math.pow(10, leadingZeros + integerLength(leadingDigits)) / leadingDigits);
    }

    public static void main(String[] args) {
        System.out.println(solve(8, 137, 56789));
    }
}
