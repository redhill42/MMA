package euler;

public class Palindrome {
    private int length;
    private long n, limit;

    public Palindrome() {
        init(1);
    }

    public Palindrome(int len) {
        init(len);
    }

    private void init(int len) {
        length = len;
        n = 1;
        for (int k = (length + 1) / 2; k > 1; k--)
            n *= 10;
        limit = n * 10;
    }

    public long next() {
        long a = n, b = n;
        if (length % 2 != 0)
            b /= 10;
        for (int i = 1; i < length; i += 2) {
            a = a * 10 + (b % 10);
            b /= 10;
        }
        if (++n >= limit) {
            init(length + 1);
        }
        return a;
    }
}
