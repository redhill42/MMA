package euler.algo;

public class Palindrome {
    private final int base;
    private int length;
    private long n, limit;

    public Palindrome() {
        this(1, 10);
    }

    public Palindrome(int len) {
        this(len, 10);
    }

    public Palindrome(int len, int base) {
        this.base = base;
        init(len);
    }

    private void init(int len) {
        length = len;
        n = 1;
        for (int k = (length + 1) / 2; k > 1; k--)
            n *= base;
        limit = n * base;
    }

    public long next() {
        long a = n, b = n;
        if (length % 2 != 0) {
            b /= base;
        }
        for (int i = 1; i < length; i += 2) {
            a = a * base + b % base;
            b /= base;
        }
        if (++n >= limit) {
            init(length + 1);
        }
        return a;
    }
}
