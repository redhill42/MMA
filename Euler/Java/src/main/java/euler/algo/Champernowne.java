package euler.algo;

/**
 * Champernowne constant is defined by concatenating representations
 * of successive integers.
 *     C = 0.12345678910111213141516...
 */
public final class Champernowne {
    private Champernowne() {}

    /**
     * Returns the nth digit in the Champernowne constant sequence.
     */
    public static int digit(long i) {
        int k = 1;
        long n = 1;
        while (i > n * 9 * k) {
            i -= n * 9 * k;
            k++;
            n *= 10;
        }

        long num = n + (i - 1) / k;
        int offset = (int)((i - 1) % k);
        return Long.toString(num).charAt(offset) - '0';
    }

    /**
     * Returns the position of given number in the Champernowne constant sequence.
     */
    public static long position(long num) {
        int len = Long.toString(num).length();
        long n = 1, offset = 1;
        for (int k = 1; k < len; k++, n *= 10)
            offset += n * 9 * k;
        return offset + (num - n) * len;
    }
}
